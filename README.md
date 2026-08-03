# Gestion des cours Supdeco — application MVC (Servlet / JSP)

Application web Java respectant le patron **MVC** :

| Couche | Classe / fichier | Rôle |
|---|---|---|
| Modèle | `Cours.java` | id, code, intitulé, crédits, enseignant |
| DAO | `CoursDAO.java` | stockage **en mémoire** (`LinkedHashMap` statique) |
| Service | `CoursService.java` | règles de gestion, validation, pagination |
| Contrôleur | `CoursControleur.java` | servlet unique, actions `liste / nouveau / sauvegarder / modifier / supprimer` |
| Vues | `liste_cours.jsp`, `formulaire_cours.jsp` , `header.jsp` , `footer.jsp` | affichage HTML + JSTL |

Bonus demandé : **BONUS : Ajoutez une barre de recherche avec filtre côté client (JavaScript)** (constante `CoursService.TAILLE_PAGE`).

---

## Arborescence

```
supdeco-cours/
├── pom.xml
└── src/main/
    ├── java/sn/supdeco/cours/
    │   ├── modele/Cours.java
    │   ├── dao/CoursDAO.java
    │   ├── service/CoursService.java
    │   ├── controleur/CoursControleur.java
    │   └── launcher/Main.java          ← lance Tomcat embarqué
    └── webapp/
        ├── index.jsp                    ← redirige vers /cours
        ├── css/style.css
        └── WEB-INF/
            ├── web.xml
            └── vues/
                ├── liste_cours.jsp
                └── formulaire_cours.jsp
```

Les JSP sont dans `WEB-INF/vues/` : elles ne sont accessibles que via le
contrôleur, comme l'exige le MVC (impossible d'appeler la vue directement
dans la barre d'adresse).

---

##  lancement sans installer de serveur

Fonctionne avec **Eclipse IDE for Java Developers** tel quel : le support
Maven (m2e) est inclus, et Tomcat est démarré par la classe `Main`.

1. Décompresser `supdeco-cours.zip` quelque part (hors du workspace de préférence).
2. Eclipse → `File` → `Import…` → `Maven` → **Existing Maven Projects** → `Next`.
3. `Browse…` → sélectionner le dossier `supdeco-cours` → cocher le `pom.xml` → `Finish`.
4. Attendre la fin du téléchargement des dépendances (barre de progression en bas à droite).
   Si des erreurs rouges persistent : clic droit sur le projet → `Maven` → `Update Project…` → cocher `Force Update of Snapshots/Releases` → `OK`.
5. Vérifier le JDK : clic droit projet → `Properties` → `Java Build Path` → `Libraries`. Il faut un **JDK 17 ou supérieur** (pas un JRE).
6. Ouvrir `Main.java` → clic droit → `Run As` → **Java Application**.
7. La console affiche `http://localhost:8080/cours` → ouvrir cette adresse dans le navigateur.
8. Pour arrêter : bouton rouge **Terminate** de la vue Console.

> Si le port 8080 est déjà pris, changer `PORT` dans `Main.java`.

