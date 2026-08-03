package sn.supdeco.cours.modele;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entite metier representant un cours dispense a Supdeco.
 */
public class Cours implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String code;
    private String intitule;
    private int credits;
    private String enseignant;

    public Cours() {
    }

    public Cours(int id, String code, String intitule, int credits, String enseignant) {
        this.id = id;
        this.code = code;
        this.intitule = intitule;
        this.credits = credits;
        this.enseignant = enseignant;
    }

    public Cours(String code, String intitule, int credits, String enseignant) {
        this(0, code, intitule, credits, enseignant);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(String enseignant) {
        this.enseignant = enseignant;
    }

    /** true tant que le cours n'a pas encore ete enregistre. */
    public boolean isNouveau() {
        return id <= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cours)) {
            return false;
        }
        return id == ((Cours) o).id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Cours{id=" + id + ", code=" + code + ", intitule=" + intitule
                + ", credits=" + credits + ", enseignant=" + enseignant + "}";
    }
}
