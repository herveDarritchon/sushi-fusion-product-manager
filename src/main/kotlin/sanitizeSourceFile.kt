import infrastructure.CsvReaderFactory
import infrastructure.CsvWriterFactory
import io.blackmo18.kotlin.grass.dsl.grass
import model.CatalogProduct
import model.WooCommerceProduct
import java.io.File
import java.text.Normalizer
import kotlin.system.exitProcess

@OptIn(ExperimentalStdlibApi::class)
fun main(args: Array<String>) {
    // this is tsv reader's option
    val tsvReader = CsvReaderFactory.build(';')

    val sanitizeSourceFile = tsvReader.open("D:\\Programmation\\Sushi Fusion\\Tableau-click-and-collect-UTF8.csv") {
        readAllAsSequence().map { rows ->
            rows.map {
                it.replace("\n", " ")
            }
        }.toList()
    }.map { rows ->
        rows.map {
            it.replace("?", "?\n")
        }
    }

    val tsvWriter = CsvWriterFactory.build()

    tsvWriter.writeAll(sanitizeSourceFile, "D:\\Programmation\\Sushi Fusion\\click-and-collect-intermediaite-UTF8.csv")

    // read from `java.io.File`
    val file = File("D:\\Programmation\\Sushi Fusion\\click-and-collect-intermediaite-UTF8.csv")
    val csvContents = tsvReader.readAllWithHeader(file)

    val catalogProducts = grass<CatalogProduct> {
        customKeyMap = mapOf(
            "ID" to "id",
            "TYPES" to "types",
            "PRODUITS" to "produits",
            "INGREDIENTS" to "ingredients",
            "DESCRIPTION" to "description",
            "CONSEILS D'UTILISATIONS" to "conseilDUtilisation",
            "ALERGENES" to "alergenes",
            "Prix Qte 1p" to "prixQte1P",
            "Prix Qte 2p" to "prixQte2P",
            "Prix Qte 3p" to "prixQte3P",
            "Prix Qte 4p" to "prixQte4P",
            "Prix Qte 5p" to "prixQte5P",
            "Prix Qte 6p" to "prixQte6P",
            "Prix Qte 8p" to "prixQte8P",
            "Prix Qte 10p" to "prixQte10P",
        )
        ignoreUnknownFields = true
    }.harvest(csvContents)

    val enhancedProduct = catalogProducts.map { catalogProduct: CatalogProduct ->
        val description = catalogProduct.construitDescription()

        catalogProduct.copy(produits = catalogProduct.produits.toCapitalizeAllString(), description = description)
    }

    val targetContent = enhancedProduct.map {
        listOf(
            it.id.toUnNull(),
            it.produits.toUnNull(),
            it.ingredients.toUnNull(),
            it.description.toUnNull(),
            it.conseilDUtilisation.toUnNull(),
            it.alergenes.toUnNull(),
            it.prixQte1P.toUnNull(),
            it.prixQte2P.toUnNull(),
            it.prixQte3P.toUnNull(),
            it.prixQte4P.toUnNull(),
            it.prixQte5P.toUnNull(),
            it.prixQte6P.toUnNull(),
            it.prixQte8P.toUnNull(),
            it.prixQte10P.toUnNull()
        )
    }

    tsvWriter.writeAll(targetContent, "D:\\Programmation\\Sushi Fusion\\click-and-collect-target-UTF8.csv")

    val wooCommerceReader = CsvReaderFactory.build(';')

    val wooCommerceFile = File("D:\\Programmation\\Sushi Fusion\\woo-commerce-catalog-initial.csv")
    val wooCommerceSourceFile = wooCommerceReader.readAllWithHeader(wooCommerceFile)

    val wooProducts = grass<WooCommerceProduct> {
        customKeyMap = mapOf(
            "ID" to "id",
            "Type" to "type",
            "UGS" to "ugs",
            "Nom" to "nom",
            "Publié" to "publie",
            "Mis en avant ?" to "miseEnAvant",
            "Visibilité dans le catalogue" to "visibilite",
            "Description courte" to "descriptionCourte",
            "Description" to "description",
            "Date de début de promo" to "dateDebutPromo",
            "Date de fin de promo" to "dateFinPromo",
            "État de la TVA" to "etatTva",
            "Classe de TVA" to "classeTva",
            "En stock ?" to "enStock",
            "Stock" to "stockVal",
            "Montant de stock faible" to "montantStockFaible",
            "Autoriser les commandes de produits en rupture ?" to "autoriserCommandeProduitEnRupture",
            "Vendre individuellement ?" to "vendreIndividuellement",
            "Poids (kg)" to "poids",
            "Longueur (cm)" to "longueur",
            "Largeur (cm)" to "largeur",
            "Hauteur (cm)" to "hauteur",
            "Autoriser les avis clients ?" to "autoriserAvisClient",
            "Note de commande" to "noteDeCommande",
            "Tarif promo" to "tarifPromo",
            "Tarif régulier" to "tarifRegulier",
            "Catégories" to "categories",
            "Étiquettes" to "etiquettes",
            "Classe d’expédition" to "classeDExpedition",
            "Images" to "images",
            "Limite de téléchargement" to "limiteTelechargement",
            "Jours d’expiration du téléchargement" to "jourDExpirationDuTelechargement",
            "Parent" to "parent",
            "Groupes de produits" to "groupesDeProduits",
            "Produits suggérés" to "produitsSuggeres",
            "Ventes croisées" to "ventesCroisees",
            "URL externe" to "urlExterne",
            "Libellé du bouton" to "libelleBouton",
            "Position" to "position",
            "Nom de l’attribut 1" to "nomAttribut1",
            "Valeur(s) de l’attribut 1" to "valeurAttribut1",
            "Attribut 1 visible" to "attribut1Visible",
            "Attribut 1 global" to "attribut1Global",
        )
        ignoreUnknownFields = true
    }.harvest(wooCommerceSourceFile)

    val productId = catalogProducts.map { it.id }

    val transform = mutableListOf<String?>()

    val filteredProducts = wooProducts.filter { wooProduct ->
        val element = wooProduct.id
        transform.add(element)
        productId.contains(element)
    }

    println("Nombre de produits filtrés ${filteredProducts.size} sur ${productId.size}")

    if (filteredProducts.size != productId.size) {
        val filteredIds = filteredProducts.map { it.id }
        val notIncluded = catalogProducts.filter {
            !filteredIds.contains(it.id)
        }.map {
            it.produits
        }
        println("Produits absent de la liste de sortie : $notIncluded")
        exitProcess(1)
    }

    val wooCommerceProductsEnriched = wooProducts.map { wooCommerceProduct ->
        if (productId.contains(wooCommerceProduct.id)) {
            val catalogProduct = catalogProducts.find { wooCommerceProduct.id == it.id }
            wooCommerceProduct.copy(descriptionCourte = catalogProduct?.ingredients, description = catalogProduct?.construitDescription())
        } else {
            wooCommerceProduct
        }
    }


    val targetWooCommerContent = listOf(listOf(
        "ID",
        "Type",
        "UGS",
        "Nom",
        "Publié",
        "Mis en avant ?",
        "Visibilité dans le catalogue",
        "Description courte",
        "Description",
        "Date de début de promo",
        "Date de fin de promo",
        "État de la TVA",
        "Classe de TVA",
        "En stock ?",
        "Stock",
        "Montant de stock faible",
        "Autoriser les commandes de produits en rupture ?",
        "Vendre individuellement ?",
        "Poids (kg)",
        "Longueur (cm)",
        "Largeur (cm)",
        "Hauteur (cm)",
        "Autoriser les avis clients ?",
        "Note de commande",
        "Tarif promo",
        "Tarif régulier",
        "Catégories",
        "Étiquettes",
        "Classe d’expédition",
        "Images",
        "Limite de téléchargement",
        "Jours d’expiration du téléchargement",
        "Parent",
        "Groupes de produits",
        "Produits suggérés",
        "Ventes croisées",
        "URL externe",
        "Libellé du bouton",
        "Position",
        "Nom de l’attribut 1",
        "Valeur(s) de l’attribut 1",
        "Attribut 1 visible",
        "Attribut 1 global"
    )) + wooCommerceProductsEnriched . map {
        listOf(
            it.id.toUnNull(),
            it.type.toUnNull(),
            it.ugs.toUnNull(),
            it.nom.toUnNull(),
            it.publie.toUnNull(),
            it.miseEnAvant.toUnNull(),
            it.visibilite.toUnNull(),
            it.descriptionCourte.toUnNull(),
            it.description.toUnNull(),
            it.dateDebutPromo.toUnNull(),
            it.dateFinPromo.toUnNull(),
            it.etatTva.toUnNull(),
            it.classeTva.toUnNull(),
            it.enStock.toUnNull(),
            it.stockVal.toUnNull(),
            it.montantStockFaible.toUnNull(),
            it.autoriserCommandeProduitEnRupture.toUnNull(),
            it.vendreIndividuellement.toUnNull(),
            it.poids.toUnNull(),
            it.longueur.toUnNull(),
            it.largeur.toUnNull(),
            it.hauteur.toUnNull(),
            it.autoriserAvisClient.toUnNull(),
            it.noteDeCommande.toUnNull(),
            it.tarifPromo.toUnNull(),
            it.tarifRegulier.toUnNull(),
            it.categories.toUnNull(),
            it.etiquettes.toUnNull(),
            it.classeDExpedition.toUnNull(),
            it.images.toUnNull(),
            it.limiteTelechargement.toUnNull(),
            it.jourDExpirationDuTelechargement.toUnNull(),
            it.parent.toUnNull(),
            it.groupesDeProduits.toUnNull(),
            it.produitsSuggeres.toUnNull(),
            it.ventesCroisees.toUnNull(),
            it.urlExterne.toUnNull(),
            it.libelleBouton.toUnNull(),
            it.position.toUnNull(),
            it.nomAttribut1.toUnNull(),
            it.valeurAttribut1.toUnNull(),
            it.attribut1Visible.toUnNull(),
            it.attribut1Global.toUnNull(),
        )
    }

    val wooCommerceWriter = CsvWriterFactory.build(',')

    wooCommerceWriter.writeAll(targetWooCommerContent, "D:\\Programmation\\Sushi Fusion\\click-and-collect-woo-commerce-target-UTF8.csv")

}

fun String?.toUnNull(): String = this ?: ""
fun Int?.toUnNull(): String = this.toString() ?: ""

private fun String?.toCapitalizeAllString(): String {
    if (this == null) return ""
    return this.split(" ").joinToString(separator = " ") { element ->
        element.lowercase().replaceFirstChar { it.uppercaseChar() }
    }
}

private val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()

fun CharSequence.unaccent(): String {
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    return REGEX_UNACCENT.replace(temp, "")
}