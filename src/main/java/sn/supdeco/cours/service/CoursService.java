package sn.supdeco.cours.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sn.supdeco.cours.dao.CoursDAO;
import sn.supdeco.cours.modele.Cours;

/**
 * Couche metier : regles de gestion et validation.
 * Le controleur ne parle JAMAIS directement au DAO, il passe par ce service.
 */
public class CoursService {

    /** Nombre de cours affiches par page (bonus pagination). */
    public static final int TAILLE_PAGE = 5;

    private final CoursDAO dao = new CoursDAO();

    // ------------------------------------------------------------------
    // Consultation
    // ------------------------------------------------------------------

    public List<Cours> listerTout() {
        return dao.trouverTout();
    }

    /** Liste paginee ; le numero de page est ramene dans les bornes valides. */
    public List<Cours> listerPage(int numeroPage) {
        return dao.trouverPage(normaliserPage(numeroPage), TAILLE_PAGE);
    }

    /** Nombre total de pages (au minimum 1). */
    public int nombreDePages() {
        int total = dao.compter();
        return Math.max(1, (int) Math.ceil((double) total / TAILLE_PAGE));
    }

    /** Ramene un numero de page demande entre 1 et nombreDePages(). */
    public int normaliserPage(int numeroPage) {
        if (numeroPage < 1) {
            return 1;
        }
        return Math.min(numeroPage, nombreDePages());
    }

    public int compter() {
        return dao.compter();
    }

    public Optional<Cours> trouverParId(int id) {
        return dao.trouverParId(id);
    }

    // ------------------------------------------------------------------
    // Enregistrement
    // ------------------------------------------------------------------

    /**
     * Cree ou met a jour un cours selon que son id vaut 0 ou non.
     *
     * @return la liste des erreurs de validation ; vide si l'enregistrement
     *         a bien eu lieu.
     */
    public List<String> enregistrer(Cours cours) {
        List<String> erreurs = valider(cours);
        if (!erreurs.isEmpty()) {
            return erreurs;
        }

        // Nettoyage des chaines avant stockage
        cours.setCode(cours.getCode().trim().toUpperCase());
        cours.setIntitule(cours.getIntitule().trim());
        cours.setEnseignant(cours.getEnseignant().trim());

        if (cours.isNouveau()) {
            dao.inserer(cours);
        } else if (!dao.modifier(cours)) {
            erreurs.add("Ce cours n'existe plus : il a peut-etre ete supprime.");
        }
        return erreurs;
    }

    public boolean supprimer(int id) {
        return dao.supprimer(id);
    }

    // ------------------------------------------------------------------
    // Validation
    // ------------------------------------------------------------------

    /** Regles de gestion appliquees avant tout enregistrement. */
    public List<String> valider(Cours cours) {
        List<String> erreurs = new ArrayList<>();

        if (estVide(cours.getCode())) {
            erreurs.add("Le code est obligatoire.");
        } else if (cours.getCode().trim().length() > 10) {
            erreurs.add("Le code ne doit pas depasser 10 caracteres.");
        } else if (dao.codeExiste(cours.getCode(), cours.getId())) {
            erreurs.add("Le code " + cours.getCode().trim().toUpperCase()
                    + " est deja utilise par un autre cours.");
        }

        if (estVide(cours.getIntitule())) {
            erreurs.add("L'intitule est obligatoire.");
        }

        if (cours.getCredits() < 1 || cours.getCredits() > 30) {
            erreurs.add("Les credits doivent etre compris entre 1 et 30.");
        }

        if (estVide(cours.getEnseignant())) {
            erreurs.add("L'enseignant est obligatoire.");
        }

        return erreurs;
    }

    private boolean estVide(String valeur) {
        return valeur == null || valeur.trim().isEmpty();
    }
}
