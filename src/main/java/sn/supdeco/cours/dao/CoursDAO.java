package sn.supdeco.cours.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import sn.supdeco.cours.modele.Cours;

/**
 * Couche d'acces aux donnees. Stockage EN MEMOIRE (aucune base de donnees).
 *
 * Les donnees sont statiques : elles survivent aux requetes HTTP mais sont
 * perdues au redemarrage du serveur.
 */
public class CoursDAO {

    /** LinkedHashMap : conserve l'ordre d'insertion. */
    private static final Map<Integer, Cours> STOCKAGE =
            Collections.synchronizedMap(new LinkedHashMap<>());

    /** Generateur d'identifiants (remplace l'AUTO_INCREMENT d'une base). */
    private static final AtomicInteger SEQUENCE = new AtomicInteger(0);

    static {
        // Jeu de donnees de demonstration : 12 cours pour tester la pagination
        insererDemo("INF101", "Algorithmique et structures de donnees", 6, "M. Diop");
        insererDemo("INF102", "Programmation Java", 6, "Mme Ndiaye");
        insererDemo("INF201", "Bases de donnees relationnelles", 5, "M. Sarr");
        insererDemo("INF202", "Developpement Web JEE", 6, "M. Fall");
        insererDemo("MAT101", "Mathematiques appliquees", 4, "Mme Ba");
        insererDemo("MAT202", "Statistiques et probabilites", 4, "M. Gueye");
        insererDemo("GES101", "Comptabilite generale", 5, "Mme Sow");
        insererDemo("GES205", "Controle de gestion", 5, "M. Kane");
        insererDemo("MKT110", "Marketing fondamental", 3, "Mme Diallo");
        insererDemo("ANG100", "Anglais des affaires", 2, "Mr. Johnson");
        insererDemo("DRT150", "Droit des societes", 3, "M. Sy");
        insererDemo("ENT300", "Entrepreneuriat et innovation", 4, "Mme Thiam");
    }

    private static void insererDemo(String code, String intitule, int credits, String enseignant) {
        int id = SEQUENCE.incrementAndGet();
        STOCKAGE.put(id, new Cours(id, code, intitule, credits, enseignant));
    }

    // ------------------------------------------------------------------
    // Lecture
    // ------------------------------------------------------------------

    /** Retourne tous les cours. */
    public List<Cours> trouverTout() {
        synchronized (STOCKAGE) {
            return new ArrayList<>(STOCKAGE.values());
        }
    }

    /**
     * Retourne une "page" de cours.
     *
     * @param numeroPage numero de page, commence a 1
     * @param taillePage nombre d'elements par page
     */
    public List<Cours> trouverPage(int numeroPage, int taillePage) {
        List<Cours> tous = trouverTout();
        int debut = (numeroPage - 1) * taillePage;
        if (debut < 0 || debut >= tous.size()) {
            return new ArrayList<>();
        }
        int fin = Math.min(debut + taillePage, tous.size());
        return new ArrayList<>(tous.subList(debut, fin));
    }

    /** Nombre total de cours enregistres. */
    public int compter() {
        return STOCKAGE.size();
    }

    /** Recherche par identifiant. */
    public Optional<Cours> trouverParId(int id) {
        return Optional.ofNullable(STOCKAGE.get(id));
    }

    /**
     * Verifie si un code est deja utilise par un AUTRE cours.
     *
     * @param idAExclure id du cours en cours de modification (0 si creation)
     */
    public boolean codeExiste(String code, int idAExclure) {
        if (code == null) {
            return false;
        }
        synchronized (STOCKAGE) {
            return STOCKAGE.values().stream()
                    .anyMatch(c -> c.getId() != idAExclure
                            && c.getCode().equalsIgnoreCase(code.trim()));
        }
    }

    // ------------------------------------------------------------------
    // Ecriture
    // ------------------------------------------------------------------

    /** Insere un nouveau cours et lui affecte un identifiant. */
    public Cours inserer(Cours cours) {
        int id = SEQUENCE.incrementAndGet();
        cours.setId(id);
        STOCKAGE.put(id, cours);
        return cours;
    }

    /** Met a jour un cours existant. Retourne false si l'id est inconnu. */
    public boolean modifier(Cours cours) {
        if (!STOCKAGE.containsKey(cours.getId())) {
            return false;
        }
        STOCKAGE.put(cours.getId(), cours);
        return true;
    }

    /** Supprime un cours. Retourne false si l'id est inconnu. */
    public boolean supprimer(int id) {
        return STOCKAGE.remove(id) != null;
    }
}
