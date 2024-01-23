package fr.uvsq.cprog;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


/**
 * Cette classe permet d'annoter des fichiers.
 * En stockant ces informations dans fichier nommé "notes.xml"
 * Ce fichier contient des objets sérialisés représentant les différents
 * fichiers du repertoire.
 */
public class Annotation {

  /**
   * Cette méthode permet d'écrire dans un fichier sérialisé XML des des notes (text)
   * à propos d'un fichier.

   * @param filePath le chemin du fichier que l'on veut annoter
   * @param text ce que l'on veut ecrire à propos du fichier
   */
  public static void annote(String filePath, String text) {
    // Conversion du chemin en objet Path
    Path path = FileSystems.getDefault().getPath(filePath);

    if (!Files.exists(path)) {
      throw new IllegalArgumentException("Le chemin passé en argument n'existe pas.");
    }

    String noteName = "notes.xml";
    String filename = path.getFileName().toString();
    String parentPath = path.getParent().toString();
    String notePath = parentPath + File.separator + noteName;

    File noteFile = new File(notePath);
    final Fichier fichier = new Fichier(filename, text);

    // Si la note n'existe pas
    if (!noteFile.exists()) {
      List<Fichier> objectsList = new ArrayList<>();
      objectsList.add(fichier);
      // Ecriture de la note
      writeNote(noteFile, objectsList);

    } else {
      // Si la note existe déjà
      // On lit son contenu et le stocke
      List<Fichier> objectsList = readNoteObjects(noteFile);

      // Vérifie si un objet Fichier avec le même filename existe déjà.
      boolean fichierExists = false;
      for (Fichier existingFichier : objectsList) {
        // Si le fichier que l'on veut annoter à déjà une note:
        if (existingFichier.getNom().equals(fichier.getNom())) {
          fichierExists = true;
          // Concaténation du nouveau texte avec celui déjà existant
          String existingText = existingFichier.getTexteAssocie();
          existingFichier.setTexteAssocie(existingText + " " + text);
          break;
        }
      }

      // Si le fichier a annoté n'avait pas déjà une note:
      if (!fichierExists) {
        // Ajout du nouvel objet Fichier à la liste d'objets déjà existant
        objectsList.add(fichier);
      }

      // Ecriture de la note
      writeNote(noteFile, objectsList);
    }
  }

  /**
   * Cette méthode permet d'écrire dans un fichier sérialisé au format XML.

   * @param noteFile un fichier XML
   * @param objectsList  une liste d'object à ecrire dans noteFile
   */
  public static void writeNote(File noteFile, List<Fichier> objectsList) {
    XMLEncoder encoder = null;
  
    try {
      encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(noteFile)));
      encoder.writeObject(objectsList);
      encoder.flush();
    } catch (final java.io.IOException ex) {
      throw new RuntimeException("Erreur, le fichier notes.xml n'a pas pu etre annoté.", ex);
    } finally {
      if (encoder != null) {
        encoder.close();
      }
    }
  }

  /**
   * Cette méthode permet de lire le contenu d'un fichier sérialisé et de retourner une liste
   * du contenu du fichier.

   * @param fichier Un fichier XML
   * @return retourne la liste des objects contneu dans le fichier.xml
   */
  public static List<Fichier> readNoteObjects(File fichier) {
    // Vérifie si le fichier existe
    if (fichier.exists()) {
      try (XMLDecoder decoder = new XMLDecoder(
          new BufferedInputStream(new FileInputStream(fichier)))) {
        // Lecture de la liste d'objets depuis le fichier
        return (List<Fichier>) decoder.readObject();
      } catch (IOException ex) {
        throw new RuntimeException("Erreur, le fichier notes.xml n'a pas pu etre lu.", ex);
      }
    } else {
      // Si le fichier n'existe pas
      throw new IllegalArgumentException("Le fichier n'existe pas.");
    }
  }


  /**
   * Cette méthode permet de retourner le texte de la note associé
   * à un fichier défini par son chemin filePath.

   * @param filePath chemin représentant un fichier.
   * @return contenu de la note représentant filePath
   */
  public static String afficheNote(String filePath) {
    // Convertion du chemin en objet Path
    Path path = FileSystems.getDefault().getPath(filePath);

    if (!Files.exists(path)) {
      String errorMsg = "Le chemin passé en argument n'est pas valide";
      throw new IllegalArgumentException(errorMsg);
    }

    String noteName = "notes.xml";
    String filename = path.getFileName().toString();
    String parentPath = path.getParent().toString();
    String notePath = parentPath + File.separator + noteName;

    File noteFile = new File(notePath);

    if (noteFile.exists()) {
      List<Fichier> objectsList = readNoteObjects(noteFile);

      // Parcours de la liste d'objets pour trouver le texte associé au fichier
      for (Fichier fichier : objectsList) {
        if (filename.equals(fichier.getNom())) {
          return fichier.getTexteAssocie();
        }
      }
    }

    // Retourne un message standard si le fichier notes.xml n'existe pas
    // Ou si le fichier n'a pas d'annotation.
    return "Cet élément n'a pas d'annotation.";
  }


  /**
   * Supprime une note associé à une fichier représenté par son chemin
   * filePath, si le fichier n'a pas de note associé alors rien n'est fait.

   * @param filePath chemin représentant un fichier (String)
   */
  public static void deleteNote(String filePath) {
    // Convertion du chemin en objet Path
    Path path = FileSystems.getDefault().getPath(filePath);

    if (!Files.exists(path)) {
      throw new IllegalArgumentException("Le chemin passé en argument n'existe pas.");
    }

    String noteName = "notes.xml";
    String filename = path.getFileName().toString();
    String parentPath = path.getParent().toString();
    String notePath = parentPath + File.separator + noteName;

    File noteFile = new File(notePath);

    // Vérifie si le fichier notes.xml existe
    if (noteFile.exists()) {
      // Lecture de la liste d'objets depuis le fichier notes.xml
      List<Fichier> objectsList = readNoteObjects(noteFile);

      // Recherche l'objet avec le nom du fichier spécifié et le supprime de la liste
      for (Fichier fichier : objectsList) {
        if (filename.equals(fichier.getNom())) {
          objectsList.remove(fichier);
          break;
        }
      }

      // Réécriture de la liste mise à jour dans le fichier notes.xml
      writeNote(noteFile, objectsList);
    }
  }
}
