package sn.supdeco.cours.controleur;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sn.supdeco.cours.modele.Cours;
import sn.supdeco.cours.service.CoursService;

/**
 * Controleur unique de l'application (front controller).
 *
 * Actions traitees :
 *   GET  /cours?action=liste[&page=n]  -> tableau des cours (pagine)
 *   GET  /cours?action=nouveau         -> formulaire vide
 *   GET  /cours?action=modifier&id=n   -> formulaire pre-rempli
 *   GET  /cours?action=supprimer&id=n  -> suppression puis retour a la liste
 *   POST /cours (action=sauvegarder)   -> creation ou mise a jour
 */
@WebServlet(name = "coursControleur", urlPatterns = { "/cours" })
public class CoursControleur extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String VUE_LISTE = "/WEB-INF/vues/liste_cours.jsp";
    private static final String VUE_FORMULAIRE = "/WEB-INF/vues/formulaire_cours.jsp";

    private CoursService service;

    @Override
    public void init() throws ServletException {
        service = new CoursService();
    }

    // ==================================================================
    // GET
    // ==================================================================
    @Override
    protected void doGet(HttpServletRequest requete, HttpServletResponse reponse)
            throws ServletException, IOException {

        requete.setCharacterEncoding("UTF-8");
        String action = requete.getParameter("action");
        if (action == null || action.isBlank()) {
            action = "liste";
        }

        switch (action) {
            case "nouveau":
                afficherFormulaireCreation(requete, reponse);
                break;
            case "modifier":
                afficherFormulaireModification(requete, reponse);
                break;
            case "supprimer":
                supprimer(requete, reponse);
                break;
            case "liste":
            default:
                afficherListe(requete, reponse);
                break;
        }
    }

    // ==================================================================
    // POST
    // ==================================================================
    @Override
    protected void doPost(HttpServletRequest requete, HttpServletResponse reponse)
            throws ServletException, IOException {

        requete.setCharacterEncoding("UTF-8");
        String action = requete.getParameter("action");

        if (action == null || "sauvegarder".equals(action)) {
            sauvegarder(requete, reponse);
        } else {
            doGet(requete, reponse);
        }
    }

    // ==================================================================
    // Traitements
    // ==================================================================

    /** Affiche le tableau des cours, page par page. */
    private void afficherListe(HttpServletRequest requete, HttpServletResponse reponse)
            throws ServletException, IOException {

        int page = service.normaliserPage(lireEntier(requete.getParameter("page"), 1));

        requete.setAttribute("listeCours", service.listerPage(page));
        requete.setAttribute("pageCourante", page);
        requete.setAttribute("nombrePages", service.nombreDePages());
        requete.setAttribute("taillePage", CoursService.TAILLE_PAGE);
        requete.setAttribute("totalCours", service.compter());

        transmettre(VUE_LISTE, requete, reponse);
    }

    /** Formulaire vierge pour un nouveau cours. */
    private void afficherFormulaireCreation(HttpServletRequest requete, HttpServletResponse reponse)
            throws ServletException, IOException {

        requete.setAttribute("cours", new Cours());
        requete.setAttribute("titrePage", "Nouveau cours");
        transmettre(VUE_FORMULAIRE, requete, reponse);
    }

    /** Formulaire pre-rempli avec le cours a modifier. */
    private void afficherFormulaireModification(HttpServletRequest requete, HttpServletResponse reponse)
            throws ServletException, IOException {

        int id = lireEntier(requete.getParameter("id"), 0);
        Optional<Cours> trouve = service.trouverParId(id);

        if (trouve.isEmpty()) {
            rediriger(requete, reponse, 1, "Cours introuvable.", "erreur");
            return;
        }

        requete.setAttribute("cours", trouve.get());
        requete.setAttribute("titrePage", "Modifier le cours");
        transmettre(VUE_FORMULAIRE, requete, reponse);
    }

    /** Cree ou met a jour un cours a partir des donnees du formulaire. */
    private void sauvegarder(HttpServletRequest requete, HttpServletResponse reponse)
            throws ServletException, IOException {

        Cours cours = new Cours();
        cours.setId(lireEntier(requete.getParameter("id"), 0));
        cours.setCode(requete.getParameter("code"));
        cours.setIntitule(requete.getParameter("intitule"));
        cours.setCredits(lireEntier(requete.getParameter("credits"), 0));
        cours.setEnseignant(requete.getParameter("enseignant"));

        boolean creation = cours.isNouveau();
        List<String> erreurs = service.enregistrer(cours);

        if (!erreurs.isEmpty()) {
            // On reaffiche le formulaire avec les valeurs saisies et les erreurs
            requete.setAttribute("cours", cours);
            requete.setAttribute("erreurs", erreurs);
            requete.setAttribute("titrePage", creation ? "Nouveau cours" : "Modifier le cours");
            transmettre(VUE_FORMULAIRE, requete, reponse);
            return;
        }

        // Pattern POST-Redirect-GET : evite le renvoi du formulaire au F5
        int pageCible = creation ? service.nombreDePages() : lireEntier(requete.getParameter("page"), 1);
        String message = creation
                ? "Cours " + cours.getCode() + " ajoute."
                : "Cours " + cours.getCode() + " mis a jour.";
        rediriger(requete, reponse, pageCible, message, "succes");
    }

    /** Supprime un cours puis revient sur la liste. */
    private void supprimer(HttpServletRequest requete, HttpServletResponse reponse)
            throws IOException {

        int id = lireEntier(requete.getParameter("id"), 0);
        int page = lireEntier(requete.getParameter("page"), 1);

        boolean supprime = service.supprimer(id);
        page = service.normaliserPage(page);

        if (supprime) {
            rediriger(requete, reponse, page, "Cours supprimé.", "erreur");
        } else {
            rediriger(requete, reponse, page, "Suppression impossible : cours introuvable.", "erreur");
        }
    }

    // ==================================================================
    // Utilitaires
    // ==================================================================

    private void transmettre(String vue, HttpServletRequest requete, HttpServletResponse reponse)
            throws ServletException, IOException {
        reponse.setContentType("text/html;charset=UTF-8");
        requete.getRequestDispatcher(vue).forward(requete, reponse);
    }

    private void rediriger(HttpServletRequest requete, HttpServletResponse reponse,
                           int page, String message, String type) throws IOException {
        String url = requete.getContextPath() + "/cours?action=liste&page=" + page
                + "&message=" + URLEncoder.encode(message, StandardCharsets.UTF_8)
                + "&type=" + type;
        reponse.sendRedirect(url);
    }

    private int lireEntier(String valeur, int defaut) {
        try {
            return Integer.parseInt(valeur.trim());
        } catch (NumberFormatException | NullPointerException e) {
            return defaut;
        }
    }
}
