package fr.uvsq.cprog;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.StringsCompleter;


/**
 * Cette classe gère les entrées utilisateur
 * Lecture d'une ligne (commande entrée par l'utilisateur)
 * Découpage de cette entrée
 * Affectation des attributs de classe en fonction de ces parties d'entrées (arguments de commande).
 */
public class Cli {
  /**
   * Pour acquérir les entrées utilisateur.
   */
  public final LineReader lineReader = LineReaderBuilder.builder()
          .appName("InteractiveCLI")
          .completer(new StringsCompleter(new String[] {
              "copy", "cut", "del", "past", "..", "mkdir", "visu", "find", "help"}))
          .parser(new DefaultParser())
          .build();

  /**
   * Entrée utilisateur brute lue par LineReader.
   */
  public String lineRead = null;

  //--- Attributs de la commande
  /**
   * Stockage du NER
   * ner = -1 indique que celui ci n'a pas été initialisé.
   */
  public int ner = -1;

  /**
   * Commande à exécuter.
   */
  public Commandes.TypeCommande commande;

  /**
   * Argument de la commande.
   */
  public String nom = null;

  /**
   * Vrai si il faut cut.
   * Faux s'il faut faire un copier simple.
   */
  public boolean cut;

  /**
   * Lit l'entrée utilisateur.
   */
  public void userInput() {
    try {
      this.lineRead = lineReader.readLine();
    } catch (UserInterruptException e) {
      System.exit(0);
    }
  }

  /**
   * Affecte les attributs NER, Commande et Nom de la classe à partir de l'entrée utilisateur.
   * À chaque exécution l'attribut commande est nécéssairement réaffecté.
   * L'attribut de classe NER ne change que si présent dans l'entrée utilisateur.
   * L'attribut de classe Nom ne change que si la commande doit utiliser ce champ.
   * Pas assez d'argument devrait lancer une IllegalArgumentException.
   * Les arguments en trop seront ignorés.
   */
  public void traitementEntree() {

    //On coupe l'entrée en maximum 2 pour avoir un tableau :
    // - ["NER", "Commande + (Argument)"] ou
    // - ["Commande", "Argument"] ou
    // - ["Commande"]
    // On teste si la commande commence par le NER
    // Si oui, on va devoir couper l'entrée en 3 pour séparer Commande et Argument
    String[] lineArray = this.lineRead.split("\\s+", 2);
    try {
      if (lineArray[0].matches("^[0-9]*[1-9][0-9]*$")) {

        //On coupe l'entrée en 3 car on veut : "NER", "Commande", "Argument"
        //(On sépare la commande de l'argument)
        lineArray = this.lineRead.split("\\s+", 3);

        if (lineArray.length == 1) {
          //Si on a que le NER, on effectuera la commande NER (ne fait rien)
          this.commande = Commandes.TypeCommande.NER;
        } else {
          //Si on a NER et Commande
          switch (lineArray[1].toLowerCase()) {
            case "copy" -> {
              this.commande = Commandes.TypeCommande.COPY;
              cut = false;
            }
            case "cut" -> {
              this.commande = Commandes.TypeCommande.COPY;
              cut = true;
            }
            case "." -> this.commande = Commandes.TypeCommande.DIR;
            case "del" -> this.commande = Commandes.TypeCommande.DEL;
            case "visu" -> this.commande = Commandes.TypeCommande.VISU;
            case "+" -> {
              this.commande = Commandes.TypeCommande.ANNOTE;
              this.nom = lineArray[2]; }
            case "-" -> this.commande = Commandes.TypeCommande.SUPPRNOTE;
            default ->  throw new IllegalArgumentException("Commande invalide.");
          }
        }

        //On finit par traiter le NER (on ne le change que si commande valide)
        this.ner = Integer.parseInt(lineArray[0]);

      } else {
        switch (lineArray[0].toLowerCase()) {
          case "copy" -> {
            this.commande = Commandes.TypeCommande.COPY;
            cut = false;
          }
          case "cut" -> {
            this.commande = Commandes.TypeCommande.COPY;
            cut = true;
          }
          case "past" -> {
            if (cut) {
              this.commande = Commandes.TypeCommande.CUT;
            } else {
              this.commande = Commandes.TypeCommande.PAST;
            }
          }
          case ".." -> this.commande = Commandes.TypeCommande.PD;
          case "." -> this.commande = Commandes.TypeCommande.DIR;
          case "mkdir" -> {
            this.commande = Commandes.TypeCommande.MKDIR;
            this.nom = lineArray[1]; }
          case "del" -> this.commande = Commandes.TypeCommande.DEL;
          case "visu" -> this.commande = Commandes.TypeCommande.VISU;
          case "find" -> {
            this.commande = Commandes.TypeCommande.FIND;
            this.nom = lineArray[1]; }
          case "+" -> {
            this.commande = Commandes.TypeCommande.ANNOTE;
            this.nom = lineArray[1]; }
          case "-" -> this.commande = Commandes.TypeCommande.SUPPRNOTE;
          //Ne va pas exécuter de commande car exception
          case "help" -> throw new IllegalArgumentException(messageAide());
          default -> throw new IllegalArgumentException("NER ou commande invalide.");
        }
      }
    } catch (ArrayIndexOutOfBoundsException e) {

      //Si le tableau lineArray a moins d'éléments que prévu par la commande associée
      throw new IllegalArgumentException("Argument(s) manquant(s).");
    }
  }

  /**
   * Permet de récupérer et traiter une nouvelle ligne d'entrée utilisateur.
   */
  public void getNewCommand() {
    userInput();
    traitementEntree();
  }

  /**
   * Méthode qui affiche un message puis attend que l'utilisateur appuie sur entrée.

   * @param message le message à afficher
   */
  public void afficheEtAttend(String message) {
    System.out.print(message);
    try {
      lineReader.readLine();
    } catch (UserInterruptException f) {
      System.exit(0);
    }
  }

  /**
   * Renvoie le message d'aide.

   * @return message d'aide.
   */
  private String messageAide() {
    //Couleurs
    return "@|bold,cyan Gestionnaire de Fichiers en Commandes en Ligne avec Annotations|@\n\n"//Gras
            + "@|bold Usage|@\n" //Gras
            + "\t[<NER>] [<commande>] [<nom>]\n\n"
            + "@|bold Description|@\n" //Gras
            + "\tPermet d'effectuer diverses opérations sur les répertoires et fichiers.\n\n"
            + "@|bold NER|@\n" //Gras
            //Commande en gras
            + "\tNuméro de l'élément du répertoire à désigner.\n\n"
            + "@|bold Commandes|@\n" //Gras
            + "\t@|bold [<NER>] copy|@\t\tPour désigner l'élément à copier.\n"
            + "\t@|bold [<NER>] cut|@\t\tPour désigner l'élément à couper.\n"
            + "\t@|bold [<NER>] del|@\t\tPour désigner l'élément à supprimer.\n"
            + "\t@|bold past|@\t\t\tPour coller l'élément coupé/copié.\n"
            + "\t@|bold ..|@\t\t\tPour remonter d'un cran dans le système de fichiers.\n"
            + "\t@|bold [<NER>] .|@\t\tPour entrer dans un répertoire.\n"
            + "\t@|bold mkdir <nom>|@\t\tPour créer un répertoire.\n"
            + "\t@|bold [<NER>] visu|@\t\t"
            + "Permet de visualiser la taille ou le contenu d'un fichier.\n"
            + "\t@|bold find <nom fichier>|@\tRecherche d'un fichier dans les sous-répertoires.\n"
            + "\t@|bold help|@\t\t\tAffiche cet aide.\n"
            + "\t@|bold [<NER>] + <Note>|@\tAjoute ou concatène une note.\n"
            + "\t@|bold [<NER>] -|@\t\tSupprime la note.\n\n"
            + "Voir le manuel utilisateur pour plus d'informations.\n";
  }
}
