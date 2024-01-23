package fr.uvsq.cprog;

import static org.fusesource.jansi.Ansi.ansi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import org.fusesource.jansi.AnsiConsole;

/**
 * Classe représentant le répertoire courant du programme.
 * Une instance stocke :
 * - Le chemin du répertoire
 * - Les chemins des fichiers du répertoire, sous forme de liste
 * Les méthodes de cette classe permet l'affichage de ces derniers avec leur NER
 */
public class Repertoire {
  /**
   * Le chemin du répertoire courant.
   */
  public String repertoireCourant;

  /**
   * Les chemins des fichiers du répertoire, sous forme de liste.
   * Indice de la liste +1 <=> NER du fichier
   */
  public List<Path> contenuRepertoire;

  /**
   * Chaine de caractères du chemin de l'élément dans le presse papier.
   */
  public String pressePapier = "";

  /**
   * Constructeur : Affecte les attributs à partir du chemin courant donné.
   *
   * @param repertoireCourant : le répertoire que l'on veut lister
   */
  public Repertoire(String repertoireCourant) {
    //Attribut répertoire
    this.repertoireCourant = Paths.get(repertoireCourant).toAbsolutePath().toString();
    //Attribut contenuRepertoire
    actualiser();
  }

  /**
   * Génère la liste des chemins des éléments du répertoire (profondeur 1).
   */
  public void actualiser() {
    try {
      this.contenuRepertoire = Files.list(Paths.get(repertoireCourant).toAbsolutePath())
              .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException("Erreur lors de la lecture du répertoire.");
    }
  }

  /**
   * Affiche les noms de fichiers du répertoire courant et leur NER associé.
   *
   * @param nbLignes le nombres de lignes disponibles pour cet affichage
   * @param nerCourant le NER courant dont on veut afficher la note
   */
  public void afficheCourant(int nbLignes, int nerCourant) {
    //Le nombre de lignes à afficher
    //(on réserve X lignes (2+ pour l'instant) pour afficher d'autres choses)
    //On prend le minimum afin d'arrêter de parcourir la liste si on a atteint sa fin

    //Première ligne
    System.out.println(ansi().eraseScreen());
    System.out.println(ansi().render("@|bold,magenta "
          + "Gestionnaire de Fichiers en Commandes en Ligne avec Annotations |@").reset());
    System.out.println(ansi().render("Contenu de: @|yellow " + repertoireCourant + "|@").reset());

    //Contenu du répertoire
    int maxAffichage = Math.min((nbLignes - 2), (contenuRepertoire.size()));
    for (int ner = 0; ner < maxAffichage; ner++) {
      afficheLigne(ner + 1, contenuRepertoire.get(ner));
    }

    //Dernière ligne
    if (contenuRepertoire.size() > (nbLignes - 2)) {
      System.out.println(ansi().render("@|italic ("
              + (contenuRepertoire.size() - (nbLignes - 2))
                    + " éléments du répertoire n'ont pas été affichés.)" + "|@").reset());
    }

    //Affichage note (si NER est initialisé)
    if (nerCourant > 0) {
      AnsiConsole.out().println();
      System.out.println(ansi().render("Note associé au NER @|bold " + nerCourant + "|@:").reset());
      System.out.println(ansi().render(("@|green " + recupereNote(nerCourant) + "|@")).reset());
    }

    AnsiConsole.out().println();
    AnsiConsole.out().print("Entrer une commande: ");
  }

  /**
   * Affiche une ligne (des fichiers).
   *
   * @param ner : NER à afficher
   * @param chemin : chemin du fichier
   */
  private void afficheLigne(int ner, Path chemin) {
    //TODO Taille max ligne
    System.out.println(ansi().render(("@|magenta " + String.format("%03d", ner) + "|@"
            + " " + chemin.getFileName())).reset());
  }

  /**
   * Récupère la note ou le message à afficher correspondant pour un NER.

   * @param ner le NER dont on veut la note (doit correspondre au NER courant).
   * @return la note ou le message correspondant s'il n'y a pas de note.
   */
  public String recupereNote(int ner) {
    String path;
    String afficher = "";
    //Si NER est dans le répertoire ou non
    try {
      path = nerToPath(ner);
    } catch (IllegalArgumentException e) {
      return "Pas de note car NER invalide pour ce répertoire.";
    }
    //Si note existe ou non
    try {
      afficher = Annotation.afficheNote(path);
    } catch (IllegalArgumentException e) {
      afficher = e.getMessage();
    } finally {
      return afficher;
    }
  }

  /**
   * Permet de récupérer le chemin d'un élément à partir de son NER.

   * @param ner : NER de l'élément
   * @return : Chemin de fichier de l'élément, en chaine de caractères
   */
  public String nerToPath(int ner) {
    try {
      return contenuRepertoire.get(ner - 1).toString();
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("NER invalide.");
    }
  }
}
