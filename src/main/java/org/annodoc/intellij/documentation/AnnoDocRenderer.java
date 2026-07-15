package org.annodoc.intellij.documentation;

import com.intellij.lang.java.JavaDocumentationProvider;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.util.IncorrectOperationException;

import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class AnnoDocRenderer {
    private static final Pattern PARAMETER_TAG = Pattern.compile("(?m)^\\s*@param\\s+(\\S+)\\s+(.+)$");

    private AnnoDocRenderer() {
    }

    static String render(PsiModifierListOwner owner, String rawJavadoc) {
        try {
            String commentBody = rawJavadoc.lines()
                    .map(line -> " * " + line)
                    .collect(Collectors.joining("\n"));
            String commentText = "/**\n" + commentBody + "\n */";
            PsiDocComment comment = createAttachedComment(owner, commentText);
            String rendered = new JavaDocumentationProvider().generateRenderedDoc(comment);
            return appendMissingParameterTags(rendered, rawJavadoc);
        } catch (IncorrectOperationException exception) {
            return "<div class='content'>" + StringUtil.escapeXmlEntities(rawJavadoc) + "</div>";
        }
    }

    private static String appendMissingParameterTags(String rendered, String rawJavadoc) {
        Matcher matcher = PARAMETER_TAG.matcher(rawJavadoc);
        StringBuilder rows = new StringBuilder();
        while (matcher.find()) {
            String name = StringUtil.escapeXmlEntities(matcher.group(1));
            String description = StringUtil.escapeXmlEntities(matcher.group(2).trim());
            if (!rendered.contains(description)) {
                rows.append("<p><code>")
                        .append(name)
                        .append("</code> &ndash; ")
                        .append(description);
            }
        }
        if (rows.isEmpty()) {
            return rendered;
        }
        return rendered
                + "<table class='sections'><tr><td valign='top' class='section'><p>Parameters:</td>"
                + "<td valign='top'>" + rows + "</td></table>";
    }

    private static PsiDocComment createAttachedComment(PsiModifierListOwner owner, String commentText) {
        PsiElementFactory factory = PsiElementFactory.getInstance(owner.getProject());
        PsiDocComment comment = switch (owner) {
            case PsiMethod method -> factory.createMethodFromText(
                    commentText + "\nvoid __annodoc(" + parameterList(method) + ") {}",
                    owner
            ).getDocComment();
            case PsiClass ignored -> factory.createClassFromText(
                    commentText + "\nclass __AnnoDocTarget {}",
                    owner
            ).getDocComment();
            case PsiField ignored -> factory.createFieldFromText(
                    commentText + "\nObject __annodocTarget;",
                    owner
            ).getDocComment();
            default -> null;
        };
        return comment == null ? factory.createDocCommentFromText(commentText, owner) : comment;
    }

    private static String parameterList(PsiMethod method) {
        return java.util.Arrays.stream(method.getParameterList().getParameters())
                .map(PsiParameter::getName)
                .map(name -> "Object " + name)
                .collect(Collectors.joining(", "));
    }
}
