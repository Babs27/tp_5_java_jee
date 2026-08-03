package sn.supdeco.cours.launcher;

import java.io.File;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

/**
 * Lance l'application avec un Tomcat EMBARQUE.
 *
 * Interet : permet d'executer le projet dans "Eclipse IDE for Java Developers"
 * (clic droit > Run As > Java Application) sans installer les outils Web (WTP)
 * ni un serveur Tomcat externe.
 *
 * Une fois demarre : http://localhost:8080/cours
 * Pour arreter : bouton rouge "Terminate" de la console Eclipse.
 */
public class Main {

    private static final int PORT = 8081;

    public static void main(String[] args) throws Exception {

        File dossierWebapp = new File("src/main/webapp");
        File dossierClasses = new File("target/classes");

        if (!dossierWebapp.exists()) {
            System.err.println("Dossier introuvable : " + dossierWebapp.getAbsolutePath());
            System.err.println("Lance cette classe depuis la racine du projet supdeco-cours.");
            return;
        }

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("target/tomcat");
        tomcat.setPort(PORT);
        tomcat.getConnector();

        // Contexte racine "" -> l'appli repond sur http://localhost:8080/
        StandardContext contexte =
                (StandardContext) tomcat.addWebapp("", dossierWebapp.getAbsolutePath());
        contexte.setReloadable(false);

        // On expose target/classes comme WEB-INF/classes pour que Tomcat
        // detecte l'annotation @WebServlet du controleur.
        WebResourceRoot ressources = new StandardRoot(contexte);
        ressources.addPreResources(new DirResourceSet(
                ressources, "/WEB-INF/classes", dossierClasses.getAbsolutePath(), "/"));
        contexte.setResources(ressources);

        tomcat.start();

        System.out.println();
        System.out.println("=================================================");
        System.out.println("  Gestion des cours Supdeco");
        System.out.println("  http://localhost:" + PORT + "/cours");
        System.out.println("=================================================");
        System.out.println();

        tomcat.getServer().await();
    }
}
