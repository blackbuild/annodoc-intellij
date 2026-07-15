---
name: grill-with-docs
description: Run a repository-grounded grilling session that sharpens a Klum IDEA plugin plan while capturing agreed domain language and durable architectural decisions. Use when the user asks for a grilling session, wants an idea stress-tested before implementation, or wants the result recorded in project docs.
---

# Grill with Docs

Use the `grilling` skill for the interview discipline and the `domain-modeling` skill for durable documentation.

Before asking questions, read `AGENTS.md`, `docs/agents/domain.md`, `CONTEXT.md` if present, relevant ADRs, and enough code to avoid asking factual questions the repository can answer. Treat template-derived code and metadata as provisional unless the user confirms it represents product intent.

Ask one decision question at a time and provide a recommended answer. Capture an agreed domain term in `CONTEXT.md` immediately. Offer an ADR only when the decision meets the criteria in `domain-modeling`; do not manufacture ADRs merely to summarize the conversation.

Do not implement the plan, publish issues, or change external state during the interview. Finish with a concise statement of agreed decisions, rejected alternatives, open questions, and the best next workflow (`research`, `to-prd`, `to-issues`, or `implement`).
