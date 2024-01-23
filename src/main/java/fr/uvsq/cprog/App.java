package fr.uvsq.cprog;

import static org.fusesource.jansi.Ansi.ansi;

import org.fusesource.jansi.AnsiConsole;

/**
 * Cette classe contient la fonction main du programme.
 * Elle permet notamment d'initialiser le CLI
 * Et d'exécuter les commandes entrées par l'utilisateur.
 */
public class App {

  /**
   * Cette méthode permet notamment d'initialiser le CLI.
   * Et d'exécuter les commandes entrées par l'utilisateur.
   */
  public static void main(String[] args) {
    AnsiConsole.systemInstall();
    Repertoire repertoire = new Repertoire("");
    Cli cli = new Cli();

    while (true) {
      try {
        //Affichage contenu répertoire
        repertoire.actualiser();
        repertoire.afficheCourant(20, cli.ner);
        cli.getNewCommand();
        Commandes.execCommande(repertoire, cli);
      } catch (Exception e) {
        System.out.print(ansi().eraseScreen());
        System.out.println(ansi().render(e.getMessage()).reset());
        cli.afficheEtAttend("Appuyez sur entrée pour continuer:");
      }
    }
  }
}
