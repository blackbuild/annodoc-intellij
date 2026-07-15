package fixture

import com.blackbuild.groovy.configdsl.transform.DSL

@DSL
class DocumentedModel {
    List<DocumentedItem> items
}

@DSL
class DocumentedItem {
    String value
}
