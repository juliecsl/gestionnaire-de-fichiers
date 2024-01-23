package fr.uvsq.cprog;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Implémentation de tests pour la class Commandes.
 */
public class CommandesTest {

  /**
   * Teste la méthode visu() avec un fichier texte (.txt) non vide.
   * Vérifie si le contenu du fichier "fable.txt" est correctement lu.
   */
  @Test
  public void testVisuFichierTxt() {
    String expectedOutput = "Rien ne sert de courir ; il faut partir a point.\n"
            + "Le Lievre et la Tortue en sont temoignage.\n"
            + "Gageons, dit celle-ci, que vous n'atteindrez point\n"
            + "Sitot que moi ce but. --Sitot ? Etes vous sage ?\n"
            + "...\n"
            + "\n"
            + "- La Fontaine\n";

    assertEquals(expectedOutput, Commandes.visu("fable.txt"));
  }

  /**
   * Teste la méthode visu() avec un fichier texte (.txt) vide.
   * Vérifie si le contenu du fichier "vide.txt" est correctement lu.
   */
  @Test
  public void testVisuFichierTxtVide() {
    String expectedOutput = "";

    assertEquals(expectedOutput, Commandes.visu("vide.txt"));
  }

  /**
   * Teste la méthode visu() avec un fichier non texte.
   * Vérifie si la taille du fichier "fable.pdf" est correct.
   */
  @Test
  public void testVisuFichierNonTxt() {
    String expectedOutput = "La taille du fichier est de 57396 octet(s).";

    assertEquals(expectedOutput, Commandes.visu("fable.pdf"));
  }

  /**
   * Teste la méthode visu() avec un chemin inexistant.
   * Vérifie si une erreur est bien retournée.
   */
  @Test
  public void testVisuFichierInexistant() {
    String fichierInexistant = "ce/chemin/n/existe/pas.txt";

    // Utilisation de assertThrows pour vérifier que l'exception est lancée
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      Commandes.visu(fichierInexistant);
    });

    // Vérification du message d'erreur
    String expectedOutput = "Argument illégal, le fichier n'existe pas.";
    assertEquals(expectedOutput, exception.getMessage());
  }

  /**
   * Suppression du répertoire "Un répertoire" créé par les tests sur makeDir
   * après chaque test.
   */
  @AfterEach
  public void supprimerRepertoireTestMakeDir() {
    String newDir = "Un Repertoire" + File.separator + "Un sous Repertoire";
    (new File(newDir)).delete();
    newDir = "Un Repertoire";
    (new File(newDir)).delete();
  }

  /**
   * Vérifie si le répertoire créé par makeDir existe bien.
   */
  @Test
  public void testMakeDirUnRepertoire() {
    //On veut créer un répertoire "<projet>/Un Repertoire"
    String repCourant = "";
    String newDir = "Un Repertoire";
    Commandes.makeDir(repCourant, newDir);
    File file = new File(newDir);
    assertTrue(file.exists());
  }

  /**
   * Vérifie si le répertoire créé par makeDir existe bien.
   * Puis vérifie qu'une exception est bien lancée.
   */
  @Test
  public void testMakeDirRepertoireExisteDeja() {
    String repCourant = "";
    String newDir = "Un Repertoire";
    Commandes.makeDir(repCourant, newDir);
    File file = new File(newDir);
    assertTrue(file.exists());
    assertThrows(IllegalArgumentException.class, () -> Commandes.makeDir(repCourant, newDir));
  }

  /**
   * Vérifie si le sous-répertoire et le répertoire parent créés par makeDir existent bien.
   * Test utile ?
   */
  @Test
  public void testMakeDirSousRepertoire() {
    String repCourant = "";
    String newDir = "Un Repertoire" + File.separator + "Un sous Repertoire";
    Commandes.makeDir(repCourant, newDir);
    File file = new File(newDir);
    assertTrue(file.exists());
  }

  //////////////////         TESTS POUR COPY FILE         //////////////////

  /**
   * Teste la méthode copyFile avec un nom de fichier existant.
   */
  @Test
  public void testCopyFileValidFile() {
    String filePath = "fable.txt";
    assertEquals(Paths.get(filePath).toAbsolutePath().toString(), Commandes.copyFile(filePath));
  }

  /**
   * Teste la méthode copyFile() avec un chemin de fichier inexistant.
   * Vérifie si une erreur est bien retournée.
   */
  @Test
  public void testCopyFileInvalidFile() {
    String fichierInexistant = "ce/chemin/n/existe/pas.txt";

    // Utilisation de assertThrows pour vérifier que l'exception est lancée
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      Commandes.visu(fichierInexistant);
    });

    // Vérification du message d'erreur
    String expectedOutput = "Argument illégal, le fichier n'existe pas.";
    assertEquals(expectedOutput, exception.getMessage());
  }

  /**
   * Teste la méthode copyFile() avec un nom de repertoire existant.
   */
  @Test
  public void testCopyFileValidDirectory() {
    String filePath = "src/test";
    assertEquals(Paths.get(filePath).toAbsolutePath().toString(), Commandes.copyFile(filePath));
  }

  /**
   * Teste la méthode copyFile() avec un chemin de fichier vide.
   * Vérifie si une erreur est bien retournée.
   */
  @Test
  public void testCopyFileEmptyPath() {
    String fichierInexistant = "";

    // Utilisation de assertThrows pour vérifier que l'exception est lancée
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      Commandes.visu(fichierInexistant);
    });

    // Vérification du message d'erreur
    String expectedOutput = "Argument illégal, le fichier n'existe pas.";
    assertEquals(expectedOutput, exception.getMessage());
  }

  //////////////////         TESTS POUR CHILDIR         //////////////////
  /**
   * Teste la méthode childDir dans le cas où le repertoire enfant existe bien.
   */
  @Test
  public void testChildDirValidChildName() {
    String childDir = "src" + File.separator + "test";
    Path expectedPath = Paths.get((childDir));

    // Récupère le chemin absolu
    Path expectedAbsPath = expectedPath.toAbsolutePath();
    assertDoesNotThrow(() -> Commandes.childDir(expectedAbsPath.toString()));
  }

  /**
   * Teste la méthode childDir dans le cas où le repertoire enfant n'existe pas.
   */
  @Test
  public void testChildDirInvalidChildName() {
    String path = "src";
    String childDir = "Invalid";

    // Utilisation de assertThrows pour vérifier que l'exception est lancée
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      Commandes.childDir(path + File.separator + childDir);
    });

    // Vérification du message d'erreur
    String expectedOutput = "Chemin fils inexistant ou numero NER invalide.";
    assertEquals(expectedOutput, exception.getMessage());
  }

  /**
   * Teste la méthode childDir dans le cas où c'est un fichier qui est passé en argument
   * et non pas un repertoire.
   */
  @Test
  public void testChildDirIsFile() {
    String path = "src";
    String childDir = "fable.txt";

    // Utilisation de assertThrows pour vérifier que l'exception est lancée
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      Commandes.childDir(path + File.separator + childDir);
    });

    // Vérification du message d'erreur
    String expectedOutput = "Chemin fils inexistant ou numero NER invalide.";
    assertEquals(expectedOutput, exception.getMessage());
  }

  //////////////////         TESTS POUR PARENTDIR         //////////////////
  
  /** Teste de la méthode parentDir() avec en argument un nom de repertoire valide. */
  @Test
  public void testParentDirValid() {
    String path = "src/test";
    Path expectedPath = Paths.get("src"); 

    // Récupère le chemin absolu
    Path expectedAbsPath = expectedPath.toAbsolutePath();

    assertEquals(expectedAbsPath.toString(), Commandes.parentDir(path));
  }

  /** Teste de la méthode parentDir() avec en argument un chemin de fichier. */
  @Test
  public void testParentDirFile() {
    //"src\\test\\java\\fr\\uvsq\\cprog\\CommandesTest.java";
    String path = "src" + File.separator + "test" + File.separator + "java"
                + File.separator + "fr" + File.separator + "uvsq" + File.separator 
                + "cprog" + File.separator + "CommandesTest.java";

    // Utilisation de assertThrows pour vérifier que l'exception est lancée
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      Commandes.parentDir(path);
    });

    // Vérification du message d'erreur
    String expectedOutput = "L'argument entrée doit correspondre au chemin d'un répertoire.";
    assertEquals(expectedOutput, exception.getMessage());
  }

  /** Teste de la méthode parentDir() avec en argument un chemin de repertoire
   * qui n'a pas de parent. */
  @Disabled //Ne fonctionne pas sous linux
  @Test
  public void testParentDirNoParent() {
    String path = "C:\\";

    // Utilisation de assertThrows pour vérifier que l'exception est lancée
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      Commandes.parentDir(path);
    });

    // Vérification du message d'erreur
    String expectedOutput = "Le répertoire parent n'existe pas.";
    assertEquals(expectedOutput, exception.getMessage());
  }

  //////////////////         TESTS POUR RENAMEFILE         //////////////////
  
  /** Teste la méthode renameFile avec un nom de fichier. */
  @Test
  public void testRenameWithFile() {
    String filename = "test.txt";
    String expectedOutput = "test-copy.txt";

    assertEquals(expectedOutput, Commandes.renameFile(filename));
  }

  /** Teste la méthode renameFile avec un nom de repertoire. */
  @Test
  public void testRenameWithDir() {
    String filename = "repertoire";
    String expectedOutput = "repertoire-copy";

    assertEquals(expectedOutput, Commandes.renameFile(filename));
  }

  //////////////////         TESTS POUR PASTFILE         //////////////////

  /** Supprime le fichier créé par le test 'testPastFileDejaExistant1'. */
  @AfterEach
  public void clearTest1() {
    // Supprimer le fichier créé pendant le test ci-dessous
    String actualPath = System.getProperty("user.dir");
    String filePath = actualPath + File.separator + "fable-copy.txt";
    Path fileToDelete = Paths.get(filePath);

    try {
      Files.deleteIfExists(fileToDelete);
    } catch (IOException ex) {
      throw new IllegalArgumentException("Erreur, le fichier n'a pas pu être supprimé.", ex);
    }
  }


  /** 
   * Teste la méthode pastFile()
   * Avec un fichier de meme nom existant déjà dans le répertoire à coller.
   */
  @Test
  public void testPastFileDejaExistant1() {
    String actualPath = System.getProperty("user.dir");
    String filePath = actualPath + File.separator + "fable.txt";
    String destinationPath = actualPath;

    // Execution de la méthode
    Commandes.pastFile(filePath, destinationPath);

    // Vérification
    Path destinationFilePath = Paths.get(destinationPath, "fable-copy.txt");
    assertTrue(Files.exists(destinationFilePath));
  }

  /** Supprime le fichier créé par le test 'testPastFileDejaExistant2'. */
  @AfterEach
  public void clearTest2() {
    // Supprimer le fichier créé pendant le test ci-dessous
    String actualPath = System.getProperty("user.dir");
    String filePath = actualPath + File.separator + "fable-copy.pdf";
    Path fileToDelete = Paths.get(filePath);

    try {
      Files.deleteIfExists(fileToDelete);
    } catch (IOException ex) {
      throw new IllegalArgumentException("Erreur, le fichier n'a pas pu être supprimé.", ex);
    }
  }

  /** 
   * Teste la méthode pastFile().
   * Avec un fichier de meme nom existant déjà dans le répertoire à coller.
   */
  @Test
  public void testPastFileDejaExistant2() {
    String actualPath = System.getProperty("user.dir");
    String filePath = actualPath + File.separator + "fable.pdf";
    String destinationPath = actualPath;

    // Execution de la méthode
    Commandes.pastFile(filePath, destinationPath);

    // Vérification
    Path destinationFilePath = Paths.get(destinationPath, "fable-copy.pdf");
    assertTrue(Files.exists(destinationFilePath));
  }

  /** Supprime le fichier créé par le test 'testPastFileInOtherDir'. */
  @AfterEach
  public void clearTest3() {
    // Supprimer le fichier créé pendant le test ci-dessous
    String actualPath = System.getProperty("user.dir");
    String filePath = actualPath + File.separator + "src" + File.separator + "fable.txt";
    Path fileToDelete = Paths.get(filePath);

    try {
      Files.deleteIfExists(fileToDelete);
    } catch (IOException ex) {
      throw new IllegalArgumentException("Erreur, le fichier n'a pas pu être supprimé.", ex);
    }
  }

  /** 
   * Teste la méthode pastFile().
   * En collant le fichier dans un repertoire autre que celui d'origine.
   */
  @Test
  public void testPastFileInOtherDir() {
    String actualPath = System.getProperty("user.dir");
    String filePath = actualPath + File.separator + "fable.txt";
    String destinationPath = actualPath + File.separator + "src";

    // Execution de la méthode
    Commandes.pastFile(filePath, destinationPath);

    // Vérification
    Path destinationFilePath = Paths.get(destinationPath, "fable.txt");
    assertTrue(Files.exists(destinationFilePath));
  }

  //////////////////         TESTS POUR PASTDIR         //////////////////

  /** Supprime le dossier créé par le test 'testPastDirSameName' ainsi que son contenu. */
  @AfterEach
  public void clearTest4() {
    // Supprimer le dossier et son contenu créé pendant le test ci-dessous
    String actualPath = System.getProperty("user.dir");
    Path dir2ToDelete = Paths.get(actualPath + File.separator + "fichierTest-copy" 
          + File.separator + "fichierTest2" + File.separator + "vide.txt");
    Path dir3ToDelete = Paths.get(actualPath + File.separator + "fichierTest-copy" 
          + File.separator + "fichierTest2");
    Path dir4ToDelete = Paths.get(actualPath + File.separator + "fichierTest-copy" 
          + File.separator + "fable.txt");
    Path dir5ToDelete = Paths.get(actualPath + File.separator + "fichierTest-copy" 
          + File.separator + "fable.pdf");
    Path dir6ToDelete = Paths.get(actualPath + File.separator + "fichierTest-copy");


    try {
      Files.deleteIfExists(dir2ToDelete);
      Files.deleteIfExists(dir3ToDelete);
      Files.deleteIfExists(dir4ToDelete);
      Files.deleteIfExists(dir5ToDelete);
      Files.deleteIfExists(dir6ToDelete);
    } catch (IOException ex) {
      throw new IllegalArgumentException("Erreur, le fichier n'a pas pu être supprimé.", ex);
    }
  }

  /** 
  * Test la copie d'un repertoire contenant des fichiers et sous repertoire. 
  * A copier dans un repertoire qui à un repertoire de meme nom. 
  */
  @Test
  public void testPastDirSameName() {
    String actualPath = System.getProperty("user.dir");
    String filePath = actualPath + File.separator + "fichierTest";
    String destinationPath = actualPath;

    // Execution de la méthode
    Commandes.pastDir(filePath, destinationPath);

    // Tous les fichiers contenus dans le dossier fichierTest:
    Path dir1Path = Paths.get(destinationPath, "fichierTest-copy");
    assertTrue(Files.exists(dir1Path));
    Path dir2Path = Paths.get(destinationPath, "fichierTest-copy" + File.separator + "fable.txt");
    assertTrue(Files.exists(dir2Path));
    Path dir3Path = Paths.get(destinationPath, "fichierTest-copy" + File.separator + "fable.pdf");
    assertTrue(Files.exists(dir3Path));
    Path dir4Path = Paths.get(destinationPath, "fichierTest-copy" + File.separator 
          + "fichierTest2");
    assertTrue(Files.exists(dir4Path));
    Path dir5Path = Paths.get(destinationPath, "fichierTest-copy" + File.separator + "fichierTest2" 
          + File.separator + "vide.txt");
    assertTrue(Files.exists(dir5Path));

  }

  //////////////////         TESTS POUR PAST         //////////////////
  
  /** Supprime le dossier créé par le test 'testPastSameName' ainsi que son contenu. */
  @AfterEach
  public void clearTest5() {
    // Supprimer le dossier et son contenu créé pendant le test ci-dessous
    String actualPath = System.getProperty("user.dir");
    Path dir2ToDelete = Paths.get(actualPath + File.separator + "fichierTest-copy" 
          + File.separator + "fichierTest2" + File.separator + "vide.txt");
    Path dir3ToDelete = Paths.get(actualPath + File.separator + "fichierTest-copy" 
          + File.separator + "fichierTest2");
    Path dir4ToDelete = Paths.get(actualPath + File.separator + "fichierTest-copy" 
          + File.separator + "fable.txt");
    Path dir5ToDelete = Paths.get(actualPath + File.separator + "fichierTest-copy" 
          + File.separator + "fable.pdf");
    Path dir6ToDelete = Paths.get(actualPath + File.separator + "fichierTest-copy");


    try {
      // Files.deleteIfExists(dir1ToDelete);
      Files.deleteIfExists(dir2ToDelete);
      Files.deleteIfExists(dir3ToDelete);
      Files.deleteIfExists(dir4ToDelete);
      Files.deleteIfExists(dir5ToDelete);
      Files.deleteIfExists(dir6ToDelete);
    } catch (IOException ex) {
      throw new IllegalArgumentException("Erreur, le fichier n'a pas pu être supprimé.", ex);
    }
  }

  /** 
  * Test la méthode past() Plus précisement la copie d'un repertoire contenant des fichiers 
  * et sous repertoire. 
  * A copier dans un repertoire qui à un repertoire de meme nom. 
  */
  @Test
  public void testPastSameName() {
    String actualPath = System.getProperty("user.dir");
    String filePath = actualPath + File.separator + "fichierTest";
    String destinationPath = actualPath;

    // Execution de la méthode
    Commandes.past(filePath, destinationPath);

    // Tous les fichiers contenus dans le dossier fichierTest:
    Path dir1Path = Paths.get(destinationPath, "fichierTest-copy");
    assertTrue(Files.exists(dir1Path));
    Path dir2Path = Paths.get(destinationPath, "fichierTest-copy" + File.separator + "fable.txt");
    assertTrue(Files.exists(dir2Path));
    Path dir3Path = Paths.get(destinationPath, "fichierTest-copy" + File.separator + "fable.pdf");
    assertTrue(Files.exists(dir3Path));
    Path dir4Path = Paths.get(destinationPath, "fichierTest-copy" + File.separator 
          + "fichierTest2");
    assertTrue(Files.exists(dir4Path));
    Path dir5Path = Paths.get(destinationPath, "fichierTest-copy" + File.separator + "fichierTest2" 
          + File.separator + "vide.txt");
    assertTrue(Files.exists(dir5Path));

  }


  /** Supprime le fichier créé par le test 'testPastInOtherDir'. */
  @AfterEach
  public void clearTest6() {
    // Supprimer le fichier créé pendant le test ci-dessous
    String actualPath = System.getProperty("user.dir");
    String filePath = actualPath + File.separator + "src" + File.separator + "fable.txt";
    Path fileToDelete = Paths.get(filePath);

    try {
      Files.deleteIfExists(fileToDelete);
    } catch (IOException ex) {
      throw new IllegalArgumentException("Erreur, le fichier n'a pas pu être supprimé.", ex);
    }
  }

  /** 
   * Teste la méthode past().
   * En collant le fichier dans un repertoire autre que celui d'origine.
   */
  @Test
  public void testPastInOtherDir() {
    String actualPath = System.getProperty("user.dir");
    String filePath = actualPath + File.separator + "fable.txt";
    String destinationPath = actualPath + File.separator + "src";

    // Execution de la méthode
    Commandes.past(filePath, destinationPath);

    // Vérification
    Path destinationFilePath = Paths.get(destinationPath, "fable.txt");
    assertTrue(Files.exists(destinationFilePath));
  }

  /** 
   * Teste la méthode past().
   * Avec en entrée une chemin de repertoire d'origine qui fait référence à un chemin
   * de fichier.
   */
  @Test
  public void testPastDirPathIsFilePath() {
    String actualPath = System.getProperty("user.dir");
    String filePath = actualPath + File.separator + "fable.txt";
    String destinationPath = actualPath + File.separator + "fable.pdf";

    // Utilisation de assertThrows pour vérifier que l'exception est lancée
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      Commandes.past(filePath, destinationPath);
    });

    // Vérification du message d'erreur
    String expectedOutput = "Argument illégal, le chemin de destination doit etre un repertoire.";
    assertEquals(expectedOutput, exception.getMessage());
  }


  /** 
   * Teste la méthode past().
   * En passant en entrée un chemin de destination vide.
   * (Revient à tester pour un repertoire inexistant)
   */
  @Test
  public void testPastEmptyDestinationPath() {
    String actualPath = System.getProperty("user.dir");
    String filePath = actualPath + File.separator + "fable.txt";
    String destinationPath = "";

    // Utilisation de assertThrows pour vérifier que l'exception est lancée
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      Commandes.past(filePath, destinationPath);
    });

    // Vérification du message d'erreur
    String expectedOutput = "Au moins un des chemins passés en entrée est invalide.";
    assertEquals(expectedOutput, exception.getMessage());

  }

  /** 
   * Teste la méthode past().
   * En passant en entrée un chemin d'origine vide.
   * (Revient à tester pour un repertoire inexistant)
   */
  @Test
  public void testPastEmptyOriginPath() {
    String actualPath = System.getProperty("user.dir");
    String filePath = "";
    String destinationPath = actualPath;

    // Utilisation de assertThrows pour vérifier que l'exception est lancée
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      Commandes.past(filePath, destinationPath);
    });

    // Vérification du message d'erreur
    String expectedOutput = "Au moins un des chemins passés en entrée est invalide.";
    assertEquals(expectedOutput, exception.getMessage());

  }

  //////////////////         TESTS POUR CUT         //////////////////

  /** 
   * Teste la méthode cut() avec un fichier qui serait copier couper
   * dans le meme dossier. 
   * */
  @Test
  public void testCutFileSameDir() {
    String actualPath = System.getProperty("user.dir");
    String filePath = actualPath + File.separator + "vide.txt";
    String destinationPath = actualPath;

    // Execution de la méthode
    Commandes.cut(filePath, destinationPath);

    // Vérification
    // Chemin du fichier coller
    Path destinationFilePath = Paths.get(destinationPath, "vide.txt");
    // On vérifie que le fichier n'a pas été modifié.
    assertTrue(Files.exists(destinationFilePath));
  }

  /** Teste la méthode cut() avec un repertoire. */
  @Test
  public void testCutDir() {
    String actualPath = System.getProperty("user.dir");
    String filePath = actualPath + File.separator + "fichierTestBis" + File.separator 
          + "fichierTest2";
    String destinationPath = actualPath;

    // Execution de la méthode
    Commandes.cut(filePath, destinationPath);

    // Vérification
    // Chemin du fichier coller
    Path destinationFilePath = Paths.get(destinationPath, "fichierTest2");
    Path deletePath = Paths.get(filePath);
    // On vérifie que le fichier à bien été collé (qu'il existe bien au bon endroit)
    // + qu'il ait bien été supprimé (qu'il n'existe plus)
    assertTrue(Files.exists(destinationFilePath));
    assertTrue(!Files.exists(deletePath));
  }

  /** 
   * Supprime le fichier créé par le test 'testCutDir'.
   * Et remet le fichier qui a été supprimé.
   */
  @AfterAll
  public static void clearTest8() {
    // Supprimer le fichier créé pendant le test ci-dessous
    String actualPath = System.getProperty("user.dir");
    String dirToDelete = actualPath + File.separator + "fichierTest2";
    File toDeleFile = new File(dirToDelete);

    // Suppression fichier généré par la copie.
    try {
      FileUtils.deleteDirectory(toDeleFile);
    } catch (IOException ex) {
      throw new IllegalArgumentException("Erreur, le fichier n'a pas pu être supprimé.", ex);
    }

    // Ajout du fichier supprimé à cause du cut
    String originPath = actualPath + File.separator + "fichierTest" + File.separator 
          + "fichierTest2";
    String destinationPath = actualPath + File.separator + "fichierTestBis";
    Commandes.past(originPath, destinationPath);
  }  

  /** Teste la méthode cut() avec un fichier. */
  @Test
  public void testCutFile() {
    String actualPath = System.getProperty("user.dir");
    String filePath = actualPath + File.separator + "vide.txt";
    String destinationPath = actualPath + File.separator + "fichierTestBis";

    // Execution de la méthode
    Commandes.cut(filePath, destinationPath);

    // Vérification
    // Chemin du fichier coller
    Path destinationFilePath = Paths.get(destinationPath, "vide.txt");
    Path deletePath = Paths.get(filePath);
    // On vérifie que le fichier à bien été collé (qu'il existe bien au bon endroit)
    // + qu'il ait bien été supprimé (qu'il n'existe plus)
    assertTrue(Files.exists(destinationFilePath));
    assertTrue(!Files.exists(deletePath));
  }

  
  /** 
   * Supprime le fichier créé par le test 'testCutFile'.
   * Et remet le fichier qui a été supprimé.
   */
  @AfterAll
  static void clearTest7() {
    // Supprimer le fichier créé pendant le test ci-dessous
    String actualPath = System.getProperty("user.dir");
    String filePath = actualPath + File.separator + "fichierTestBis" + File.separator + "vide.txt";
    Path fileToDelete = Paths.get(filePath);

    // Suppression fichier généré par la copie.
    try {
      Files.deleteIfExists(fileToDelete);
    } catch (IOException ex) {
      throw new IllegalArgumentException("Erreur, le fichier n'a pas pu être supprimé.", ex);
    }

    // Ajout du fichier supprimé à cause du cut
    String originPath = actualPath + File.separator + "fichierTest" + File.separator 
        + "fichierTest2" + File.separator + "vide.txt";
    ;
    Commandes.past(originPath, actualPath);
  } 

  ////////////////         TESTS POUR FIND         //////////////////
  @Test
  public void testFindExistingPattern() {
    String actualPath = System.getProperty("user.dir");
    String pathTest = actualPath + File.separator + "fichierTest3";

    // Construction de l'expectedOutput
    List<List<String>> expectedOutput = new ArrayList<>();
    expectedOutput.add(List.of("fable.pdf", pathTest + File.separator + "unAutreDossier"));
    expectedOutput.add(List.of("rafale.txt", pathTest + File.separator + "unAutreDossier")); 
    expectedOutput.add(List.of("fable.txt", 
        pathTest + File.separator + "unAutreDossier" + File.separator + "SousDossier"));
    //////

    String dirPath = actualPath + File.separator + "fichierTest3";
    String pattern = "fa";
    List<List<String>> liste = new ArrayList<>();
    liste = Commandes.find(dirPath, pattern);

    assertEquals(expectedOutput, liste);
  }

  @Test
  public void testFindWrongDirPath() {
    String actualPath = System.getProperty("user.dir");
    String dirPath = actualPath + File.separator + "existePas";
    String pattern = "fa";

    // Utilisation de assertThrows pour vérifier que l'exception est lancée
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      Commandes.find(dirPath, pattern);
    });

    // Vérification du message d'erreur
    String expectedOutput = "Le chemin entrée dans correspondre à un répertoire ou n'existe pas.";
    assertEquals(expectedOutput, exception.getMessage());
  }

  //////////////////         TESTS POUR DELETE        //////////////////
  // En grande partie cette méthode est testé via les tests pour la méthode cut().

  /**
   * Teste de la méthode delete() à qui on passe en argument un chemin
   * qui n'existe pas.
   */
  @Test
  public void testDeleteWrongPath() {
    String wrongPath = "mauvais/chemin.pdf";

    // Utilisation de assertThrows pour vérifier que l'exception est lancée
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      Commandes.delete(wrongPath);
    });

    // Vérification du message d'erreur
    String expectedOutput = "Le chemin spécifié n'existe pas : " + wrongPath;
    assertEquals(expectedOutput, exception.getMessage());
  }
}