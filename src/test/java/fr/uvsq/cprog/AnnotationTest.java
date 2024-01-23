package fr.uvsq.cprog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Tests de la classe Annotation.
 */
public class AnnotationTest {

  /**
   * Teste la méthode annote() dans le cas où on veut ajouter une annotation
   * alors que le fichier notes.xml n'existe pas encore.
   */
  @Test
  public void testAnnote() {
    String actualPath = System.getProperty("user.dir");
    String pathAnnote = actualPath + File.separator + "fable.txt";
    String text = "ceci est un test";
    String notePath = actualPath + File.separator + "notes.xml";

    // On lance la méthode à tester
    Annotation.annote(pathAnnote, text);

    File noteFile = new File(notePath);
    // On récupère le contenu de la note:
    List<Fichier> objectsList = Annotation.readNoteObjects(noteFile);

    assertEquals(1, objectsList.size());
    assertEquals(text, objectsList.get(0).getTexteAssocie());
  }

  /**
   * Suppression de la note créée.
   */
  @AfterEach
  public void deleteFile() {
    String actualPath = System.getProperty("user.dir");
    Path path = FileSystems.getDefault().getPath(actualPath + File.separator + "notes.xml");

    try {
      // On essaie de supprimer
      Files.deleteIfExists(path);
    } catch (IOException ex) {
      // Si impossible on renvoie une erreur.
      String errorMsg = "Le fichier n'a pas pu etre supprimé.";
      throw new RuntimeException(errorMsg, ex);
    }
  }

  /**
  * Teste la méthode annote() avec notes.xml déjà existante.
  * Et un fichier à annoté qui a déjà une note.
  */
  @Test
    public void testAnnoteExistingFile() {
    String actualPath = System.getProperty("user.dir");
    String pathAnnote = actualPath + File.separator + "fable.txt";
    String text1 = "Ma premiere note.";
    String text2 = "Ma seconde note !";

    // Création d'une première annotation
    Annotation.annote(pathAnnote, text1);

    // Création d'une seconde annotation
    Annotation.annote(pathAnnote, text2);

    String notePath = actualPath + File.separator + "notes.xml";
    File noteFile = new File(notePath);

    // Verifie si la note à bien était mise à jour.
    List<Fichier> objectsList = Annotation.readNoteObjects(noteFile);
    assertEquals(1, objectsList.size());
    assertEquals("fable.txt", objectsList.get(0).getNom());
    assertEquals(text1 + " " + text2, objectsList.get(0).getTexteAssocie());
  }

  /**
  * Teste la méthode annote() sur l'annotation sur un fichier qui n'existe pas.
  */
  @Test
  public void testAnnoteNonExistingFile() {
    String filePath = "ce/chemin/nexiste/pas.pdf";
    String text = "Le fichier est inconnu.";

    assertThrows(IllegalArgumentException.class, () -> Annotation.annote(filePath, text));
  }

  /**
  * Teste la méthode readNoteObjects() sur la lecture d'un fichier XML inexistant.
  */
  @Test
  public void testReadNonExistingFile() {
    // Crée un objet File pour un fichier qui n'existe pas
    File nonExistingFile = new File("ce/chemin/nexiste/pas.xml");

    // Vérifie que la méthode lève l'exception attendue
    assertThrows(IllegalArgumentException.class, () -> Annotation.readNoteObjects(nonExistingFile));
  }


  /**
   * Teste de la méthode afficheNote() quand le fichier notes.xml
   * n'existe pas.
   */
  @Test
  public void testAfficheNoteNotExistingXml() {
    String actualPath = System.getProperty("user.dir");
    String filePath = actualPath + File.separator + "fable.txt";
    String result = Annotation.afficheNote(filePath);
    String expectedOutPut = "Cet élément n'a pas d'annotation.";

    // Vérification du résultat attendu
    assertEquals(expectedOutPut, result);
  }

  /**
   * Teste de la méthode afficheNote() quand le fichier
   * note.xml existe et qu'une note existe aussi pour le fichier
   * testé.
   */
  @Test
  public void testAfficheNoteExistingNote() {
    String actualPath = System.getProperty("user.dir");
    String filePath = actualPath + File.separator + "fable.txt";
    String expectedOutPut = "Voici le texte aosscié à la note.";

    // Création d'une note
    Annotation.annote(filePath, expectedOutPut);

    // Récupération du contenu de la note associé à fable.txt
    String result = Annotation.afficheNote(filePath);

    // Vérification du résultat attendu
    assertEquals(expectedOutPut, result);
  }

  
  /**
   * Teste de la méthode afficheNote() quand le fichier
   * note.xml existe mais qu'aucune note n'existe pour le fichier
   * testé.
   */
  @Test
  public void testAfficheNoteNotExistingNote() {
    String actualPath = System.getProperty("user.dir");
    String filePath1 = actualPath + File.separator + "fable.txt";
    String filePath2 = actualPath + File.separator + "fable.pdf";
    String text = "Voici le texte associé à la note.";

    // Création d'une note
    Annotation.annote(filePath1, text);

    // Récupération du contenu de la note associé à fable.txt
    String result = Annotation.afficheNote(filePath2);

    // Vérification du résultat attendu
    String expectedOutPut = "Cet élément n'a pas d'annotation.";
    assertEquals(expectedOutPut, result);
  }

  /**
  * Teste la méthode afficheNote() avec un chemin de fichier invalide.
  */
  @Test
  public void testAfficheNoteNotExistingFile() {
    String filePath = "ce/dosser/nexiste/pas.pdf";

    // Vérifie que la méthode lève l'exception attendue
    assertThrows(IllegalArgumentException.class, () -> Annotation.afficheNote(filePath));
  }

  /**
   * Teste la méthode deleteNote() sur un fichier qui a une annotation.
   */
  @Test
  public void testDeleteNoteExistingNote() {
    String actualPath = System.getProperty("user.dir");
    String filePath1 = actualPath + File.separator + "fable.txt";
    String filePath2 = actualPath + File.separator + "fable.pdf";
    String text = "Voici le texte associé à la note.";


    // Création de deux notes
    Annotation.annote(filePath1, text);
    Annotation.annote(filePath2, text);


    // Suppression de la note associée à filePath1
    Annotation.deleteNote(filePath1);

    // Récupération du contenu de la note associé à filePath1
    String result = Annotation.afficheNote(filePath1);

    // Vérification du résultat attendu
    String expectedOutPut = "Cet élément n'a pas d'annotation.";
    assertEquals(expectedOutPut, result);
  }

  /**
   * Teste la méthode deleteNote() sur un fichier qui a une annotation.
   */
  @Test
  public void testDeleteNoteNotExistingNote() {
    String actualPath = System.getProperty("user.dir");
    String filePath1 = actualPath + File.separator + "fable.txt";
    String filePath2 = actualPath + File.separator + "fable.pdf";
    String text = "Voici le texte associé à la note.";


    // Création de deux notes
    Annotation.annote(filePath2, text);


    // Suppression de la note associée à filePath1
    Annotation.deleteNote(filePath1);

    // Récupération du contenu de la note associé à filePath1
    String result = Annotation.afficheNote(filePath1);

    // Vérification du résultat attendu
    String expectedOutPut = "Cet élément n'a pas d'annotation.";
    assertEquals(expectedOutPut, result);
  }

  /**
  * Teste la méthode deleteNote() avec un chemin de fichier invalide.
  */
  @Test
  public void testDeleteNoteNotExistingFile() {
    String filePath = "ce/dosser/nexiste/pas.pdf";

    // Vérifie que la méthode lève l'exception attendue
    assertThrows(IllegalArgumentException.class, () -> Annotation.deleteNote(filePath));
  }

}
