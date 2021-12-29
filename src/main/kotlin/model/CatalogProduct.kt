package model

import toUnNull

data class CatalogProduct (
    val id: String?,
    val types: String?,
    val produits: String?,
    val ingredients: String?,
    val description: String?,
    val conseilDUtilisation: String?,
    val alergenes: String?,
    val prixQte1P: String?,
    val prixQte2P: String?,
    val prixQte3P: String?,
    val prixQte4P: String?,
    val prixQte5P: String?,
    val prixQte6P: String?,
    val prixQte8P: String?,
    val prixQte10P: String?,
    ) {

    fun construitDescription(): String {
        if (this == null) return ""
        return """${description.toUnNull()}
            |
            |[tabs slidertype="top tabs"] [tabcontainer] [tabtext]Ingrédients[/tabtext] [tabtext]Conseils d'utilisations[/tabtext] [tabtext]Alergenes[/tabtext] [/tabcontainer] [tabcontent] [tab]${ingredients.toUnNull()}[/tab][tab]${conseilDUtilisation.toUnNull()}[/tab] [tab]${alergenes.toUnNull()}[/tab] [/tabcontent] [/tabs]
        """.trimMargin()
    }

}