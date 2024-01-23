package fr.uvsq.cprog;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Teste la classe Répertoire.
 */
public class RepertoireTest {
  /**
   * Teste le constructeur de la classe Repertoire.
   */
  @Test
  public void testConstructeurRepertoire() {
    //On construit la liste de Path des fichiers de fichierTestBis/
    ArrayList<Path> expectedContenuRepertoire = new ArrayList<Path>();
    expectedContenuRepertoire.add(Paths.get("fichierTestBis/fable.pdf").toAbsolutePath());
    expectedContenuRepertoire.add(Paths.get("fichierTestBis/fable.txt").toAbsolutePath());
    expectedContenuRepertoire.add(Paths.get("fichierTestBis/fichierTest2").toAbsolutePath());
    //On construit repertoireTest et on vérifie que celui ci contient bien la même liste
    Repertoire repertoireTest = new Repertoire("fichierTestBis");
    assertEquals(repertoireTest.contenuRepertoire, expectedContenuRepertoire);
  }

  /**
   * Crée un répertoire vide pour le test.
   */
  @BeforeAll
  public static void repertoireVide() {
    File file = new File("dossierTestRepertoire");
    file.mkdir();
  }

  /**
   * Teste le constructeur sur un répertoire vide.
   */
  @Test
  public void testConstructeurRepertoireVide() {
    Repertoire repertoireTest = new Repertoire("dossierTestRepertoire");
    ArrayList<Path> expectedContenuRepertoire = new ArrayList<Path>(); //Liste vide
    assertEquals(repertoireTest.contenuRepertoire, expectedContenuRepertoire);
  }

  /**
   * Supprime le répertoire créé.
   */
  @AfterAll
  public static void supprRepertoireVide() {
    File file = new File("dossierTestRepertoire");
    try {
      FileUtils.deleteDirectory(file);
    } catch (IOException ex) {
      throw new IllegalArgumentException("Erreur, le répertoire n'a pas pu être supprimé.", ex);
    }
  }

  /**
   * Teste l'affichage
   * En particulier les index.
   */
  @Test
  public void testAffichage() {
    //Teste qu'il n'ait pas d'exception (notamment si nbLignes > Taille liste du répertoire)
    assertDoesNotThrow(() -> {
      Repertoire repertoireTest = new Repertoire("fichierTest");
      repertoireTest.afficheCourant(20, 2);
    });
    assertDoesNotThrow(() -> {
      Repertoire repertoireTest = new Repertoire("fichierTest");
      repertoireTest.afficheCourant(20, 27);
    });
  }

  /**
   * Teste si aucune erreur n'est lancée par la méthode nerToPath().
   */
  @Test
  public void testNer() {
    Repertoire repertoireTest = new Repertoire("fichierTest");
    assertDoesNotThrow(() -> {
      repertoireTest.nerToPath(2);
    });
  }

  /**
   * Teste si un erreur est lancée par la méthode nerToPath() avec un NER invalide.
   */
  @Test
  public void testNerInvalide() {
    Repertoire repertoireTest = new Repertoire("fichierTest");
    assertThrows(IllegalArgumentException.class, () -> {
      repertoireTest.nerToPath(4);
    });
    assertThrows(IllegalArgumentException.class, () -> {
      repertoireTest.nerToPath(0);
    });
    assertThrows(IllegalArgumentException.class, () -> {
      repertoireTest.nerToPath(-1);
    });
  }

  /**
   * Teste que la méthode nerToPath renvoie bien la bonne chaine de caractères.
   */
  @Test
  public void testReturnNer() {
    Repertoire repertoireTest = new Repertoire("fichierTest");
    String expectedOutput = repertoireTest.contenuRepertoire.get(1).toString();
    assertEquals(repertoireTest.nerToPath(2), expectedOutput);
  }

  /**
   * Teste si la note est bien récupérée à partir d'un NER.
   */
  @Test
  public void testRecupereNote() {
    Repertoire repertoire = new Repertoire("fichierTestAffNote");
    assertEquals("Voici le texte associé à la note.", repertoire.recupereNote(2));
  }

  /**
   * Teste si le message est le bon en cas d'NER invalide.
   */
  @Test
  public void testRecupereNoteExcep1() {
    Repertoire repertoire = new Repertoire("fichierTestAffNote");
    assertEquals("Pas de note car NER invalide pour ce répertoire.", repertoire.recupereNote(3));
  }

  /**
   * Teste si le message est le bon en cas d'absence de note.
   */
  @Test
  public void testRecupereNoteExcep2() {
    Repertoire repertoire = new Repertoire("fichierTestAffNote");
    assertEquals("Cet élément n'a pas d'annotation.", repertoire.recupereNote(1));
  }
}
