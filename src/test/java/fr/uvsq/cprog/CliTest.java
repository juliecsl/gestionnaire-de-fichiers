package fr.uvsq.cprog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.fusesource.jansi.AnsiConsole;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests de la classe Cli.
 */
public class CliTest {

  /**
   * Pour éviter l'affichage de warnings en rapport avec le terminal Jline dans les tests.
   */
  @BeforeAll
  public static void jansi() {
    AnsiConsole.systemInstall();
  }

  /**
   * Teste les commandes qui ne demandent ni nom fichier, note ou nom répertoire.
   */
  @ParameterizedTest
  @CsvSource({"3 coPy, 3, COPY",
              "Copy, -1, COPY",
              "past, -1, PAST",
              "5 cut, 5, COPY",
              "5     cut, 5, COPY",
              "cut, -1, COPY",
              ".., -1, PD",
              "00505 ., 505, DIR",
              "., -1, DIR",
              "mkdir nomrep, -1, MKDIR",
              "visu, -1, VISU",
              "3 visu, 3, VISU",
              "find requete, -1, FIND",
              "004 + unenote argumentEnTrop, 4, ANNOTE",
              "6000 -, 6000, SUPPRNOTE",
              "4 del, 4, DEL",
              "del, -1, DEL"

  })
  public void testTraitementEntree1(
          String input,
          int expectedNer,
          Commandes.TypeCommande expectedCommande) {
    Cli commandeTest = new Cli();
    commandeTest.lineRead = input;
    commandeTest.traitementEntree();
    assertEquals(commandeTest.ner, expectedNer);
    assertEquals(commandeTest.commande, expectedCommande);
  }

  /**
   * Teste les commandes qui demandent un nom fichier, note ou nom répertoire
   * Vérifie que ces derniers soient corrects.
   */
  @ParameterizedTest
  @CsvSource({"mkdir nom_fichier, -1, MKDIR, nom_fichier",
              "find 3, -1, FIND, 3",
              "3 + une_note, 3, ANNOTE, une_note",
              "+ une_note, -1, ANNOTE, une_note"

  })
  public void testTraitementEntree2(
          String input,
          int expectedNer,
          Commandes.TypeCommande expectedCommande,
          String expectedNom) {
    Cli commandeTest = new Cli();
    commandeTest.lineRead = input;
    commandeTest.traitementEntree();
    assertEquals(commandeTest.ner, expectedNer);
    assertEquals(commandeTest.commande, expectedCommande);
    assertEquals(commandeTest.nom, expectedNom);
  }

  //Même test que le précédent, avec espaces
  /**
   * Teste les commandes qui demandent un nom fichier, note ou nom répertoire avec espaces
   * Vérifie que ces derniers soient corrects.
   */
  @ParameterizedTest
  @CsvSource({"mkdir nom fichier, -1, MKDIR, nom fichier",
              "find une requete, -1, FIND, une requete",
              "3 + une note, 3, ANNOTE, une note",
              "+ une note, -1, ANNOTE, une note"

  })
  public void testTraitementEntree3(
          String input,
          int expectedNer,
          Commandes.TypeCommande expectedCommande,
          String expectedNom) {
    Cli commandeTest = new Cli();
    commandeTest.lineRead = input;
    commandeTest.traitementEntree();
    assertEquals(commandeTest.ner, expectedNer);
    assertEquals(commandeTest.commande, expectedCommande);
    assertEquals(commandeTest.nom, expectedNom);
  }

  /**
   * Première partie de la commande
   * Teste quand un NER est invalide ou que la commande est erronée.
   */
  @ParameterizedTest
  @CsvSource({"coppy",
              "0 copy",
  })
  public void testTraitementEntreeException1erArg(String input) {
    Cli commandeTest = new Cli();
    commandeTest.lineRead = input;
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      commandeTest.traitementEntree();
    });

    String expectedMessage = "NER ou commande invalide.";
    assertEquals(expectedMessage, exception.getMessage());
  }

  /**
   * Seconde partie de la commande
   * Teste quand un NER est valide mais que la commande est erronée.
   */
  @ParameterizedTest
  @CsvSource({"3 coppy",
              "5 5 copy",
              "5 ..",
              "3 find"
  })
  public void testTraitementEntreeException2ndArg(String input) {
    Cli commandeTest = new Cli();
    commandeTest.lineRead = input;
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      commandeTest.traitementEntree();
    });

    String expectedMessage = "Commande invalide.";
    assertEquals(expectedMessage, exception.getMessage());
  }

  /**
   * Test quand une partie de la commande est attendue mais manquante.
   */
  @ParameterizedTest
  @CsvSource({"3 +", //Manque nom fichier, note ou nom répertoire
              "+",
              "mkdir",
              "find"
  })
  public void testTraitementEntreeExceptionArgManquant(String input) {
    Cli commandeTest = new Cli();
    commandeTest.lineRead = input;
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      commandeTest.traitementEntree();
    });

    String expectedMessage = "Argument(s) manquant(s).";
    assertEquals(expectedMessage, exception.getMessage());
  }
}
