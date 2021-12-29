package model

data class WooCommerceProduct(
    val id: String?,
    val type: String?,
    val ugs: String?,
    val nom: String?,
    val publie: Int?,
    val miseEnAvant: Int?,
    val visibilite: String?,
    val descriptionCourte: String?,
    val description: String?,
    val dateDebutPromo: String?,
    val dateFinPromo: String?,
    val etatTva: String?,
    val classeTva: String?,
    val enStock: Int?,
    val stockVal : Int?,
    val montantStockFaible: Int?,
    val autoriserCommandeProduitEnRupture: Int?,
    val vendreIndividuellement: Int?,
    val poids: Int?,
    val longueur: Int?,
    val largeur: Int?,
    val hauteur: Int?,
    val autoriserAvisClient: Int?,
    val noteDeCommande: String?,
    val tarifPromo: String?,
    val tarifRegulier: String?,
    val categories: String?,
    val etiquettes: String?,
    val classeDExpedition: String?,
    val images: String?,
    val limiteTelechargement: String?,
    val jourDExpirationDuTelechargement: String?,
    val parent: String?,
    val groupesDeProduits: String?,
    val produitsSuggeres: String?,
    val ventesCroisees: String?,
    val urlExterne: String?,
    val libelleBouton: String?,
    val position: Int?,
    val nomAttribut1: String?,
    val valeurAttribut1: String?,
    val attribut1Visible: Int?,
    val attribut1Global: Int?,
)

/*
ID,
Type,
UGS,
Nom,
Publié,
"Mis en avant ?",
"Visibilité dans le catalogue",
"Description courte",
Description,
"Date de début de promo",
"Date de fin de promo",
"État de la TVA",
"Classe de TVA",
"En stock ?",
Stock,
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
Catégories,
Étiquettes,
"Classe d’expédition",
Images,
"Limite de téléchargement",
"Jours d’expiration du téléchargement",
Parent,
"Groupes de produits",
"Produits suggérés",
"Ventes croisées",
"URL externe",
"Libellé du bouton",
Position,
"Nom de l’attribut 1",
"Valeur(s) de l’attribut 1",
"Attribut 1 visible",
"Attribut 1 global"
 */