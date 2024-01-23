package fr.uvsq.cprog;

import static org.fusesource.jansi.Ansi.ansi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;

/**
 * Classe pour l'implémentation des commandes du gestion de fichiers.
 */
public class Commandes {

  /**
   * Commandes disponibles.
   */
  public enum TypeCommande {
    COPY,
    PAST,
    CUT,
    PD, //..
    DIR, //.
    MKDIR,
    VISU,
    FIND,
    ANNOTE,
    SUPPRNOTE,
    NER,
    DEL
  }

  /**
  * Cette méthode prend en entrée le chemin d'un fichier et retourne son contenu,
  * s'il s'agit d'un fichier texte ou renvoie la taille du fichier si ce n'est pas le cas.

  * @param filePath chemin d'un fichier
  * @return retourne le contenu d'un fichier texte ou la taille d'un fichier non texte.
  */
  public static String visu(String filePath) {
    File file = new File(filePath);

    if (file.exists()) {
      // Si le fichier est un fichier .txt
      if (isTxtFile(file)) {
        // On retourne le contenu du fichier
        try
          (// Ouverture du fichier pour lecture.
            FileReader fileReader = new FileReader(file);
              
            // Création du buffer pour pour pouvoir utiliser la fonction .readLine().
            BufferedReader bufferedReader = new BufferedReader(fileReader)
          ) {
          String line;
          String fileContent = "";

          while ((line = bufferedReader.readLine()) != null) {
            fileContent += line + "\n";
          }

          return fileContent;

        } catch (IOException ex) {
          // Gestion d'erreur si le fichier n'a pas réussi a etre lu.
          throw new RuntimeException("Erreur lors de la lecture du fichier texte.", ex);
        }
      } else {
        // Sinon si ce n'est pas un fichier texte, on retourne
        // la taille du fichier.
        long bytes = file.length();
        return "La taille du fichier est de " + Long.toString(bytes) + " octet(s).";
      }
    } else {
      // Gestion d'erreur si le fichier passé en argument n'existe pas.
      throw new IllegalArgumentException("Argument illégal, le fichier n'existe pas.");
    }
  }

    
  /**
  * Cette méthode vérifie si un fichier est un fichier de type texte.

  * @param file un fichier
  * @return True s'il file est un fichier .txt Faux sinon.
  */
  public static boolean isTxtFile(File file) {
    String fileName = file.getName();
    return (fileName.endsWith(".txt") || fileName.endsWith(".text"));
  }

  /**
   * Cette méthode crée un répertoire (ou plusieurs si les répertoires parents n'existent pas).

   * @param repCourant chemin du répertoire courant
   * @param nouveauRep nom du répertoire à créer
   */
  public static void makeDir(String repCourant, String nouveauRep) {
    //Représente le chemin du répertoire à créer
    String absoluRepCourant = (new File(repCourant).getAbsolutePath());
    File file = new File(absoluRepCourant + File.separator + nouveauRep);
    //Car File.mkdir() renvoie une erreur si le répertoire existe déja
    if (file.exists()) {
      throw new IllegalArgumentException("Le répertoire existe déjà.");
    }
    if (!file.mkdirs()) {
      throw new RuntimeException("Le répertoire n'a pas pu être créé.");
    }
  }

  /**
   * Cette méthode permet de vérifier si un chemin de fichier 
   * (ou repertoire) existe bien sur la machine que l'on est 
   * en train d'utiliser.

   * @param chemin chemin de fichier ou repetoire (String)
   * @return Vrai si le chemin existe, Faux sinon.
   */
  public static boolean pathExists(String chemin) {
    // Convertion du chemin en Path
    Path path = FileSystems.getDefault().getPath(chemin);

    // Vérifie si le chemin existe
    return Files.exists(path);
  }

  /**
   * Cette méthode mime le fonctionnement de la commande bash cd.
   * Elle permet de rentrer dans le repertoire 'childName'.

   * @param childPath chemin du répertoire dans lequel on veut entrer (String)
   * @return retourne le chemin absolu du repertoire childName
   */
  public static String childDir(String childPath) {
    // On vérifie si le nouveau chemin est valide
    // Et si le childName correspondait bien à un dossier et non pas à un fichier.
    if (pathExists(childPath) && Files.isDirectory(Paths.get(childPath))) {
      return childPath;
    } else {
      throw new IllegalArgumentException("Chemin fils inexistant ou numero NER invalide.");
    }
  }

  /**
   * Cette méthode mime la commande .. de bash.

   * @param dirPath chemin du repertoire où nous sommes actuellement (String).
   * @return retourne le chemin du repertoire parent.
   */
  public static String parentDir(String dirPath) {
    // Convertion du String en Path
    Path actualPath = Paths.get(dirPath); 

    // On vérifie si le chemin entrée en argument correspond à un repertoire 
    // (et non pas un fichier)
    if (!Files.isDirectory(actualPath)) {
      String erreur = "L'argument entrée doit correspondre au chemin d'un répertoire.";
      throw new IllegalArgumentException(erreur);
    }

    // Récupère le chemin absolu
    Path actualAbsPath = actualPath.toAbsolutePath();

    // Récupère le chemin parent
    Path parentPath = actualAbsPath.getParent();

    // Vérifie si le repertoire parent existe.
    if (parentPath == null || !pathExists(parentPath.toString())) {
      // Pas un chemin valide.
      throw new IllegalArgumentException("Le répertoire parent n'existe pas.");
    } else {
      // Est un chemin valide, on peut le retourner.
      String pathString = parentPath.toString();
      return pathString;
    }
  }


  /**
   * Cette méthode permet de copier un fichier ou un repertoire.

   * @param filePath chemin d'un fichier ou d'un répertoire.
   * @return retourne le chemin absolu du fichier à copier.
   */
  public static String copyFile(String filePath) {
    File file = new File(filePath);

    Path absPath = Paths.get(filePath).toAbsolutePath();
  
    if (file.exists()) {
      return absPath.toString();
    } else {
      throw new IllegalArgumentException("Argument illégal, le fichier n'existe pas.");
    }

  }

  /**
   * Cette méthode permet de concaténer '-copy' à un nom de fichier ou de repertoire.

   * @param filename le nom d'un fichier ou d'un repertoire (String)
   * @return filename + -copy
   */
  public static String renameFile(String filename) {
    int lastIndex = filename.lastIndexOf('.');
    
    if (lastIndex != -1) {  // Vérifie s'il y a un point dans le nom du fichier
      // Extraction de la partie du nom du fichier avant le point
      String prefix = filename.substring(0, lastIndex);
        
      // Concaténation de -copy selon format d'un fichier
      return (prefix + "-copy" + filename.substring(lastIndex));
    } else {
      // Si aucun point trouvé
      // Concaténation de -copy selon format de repertoire
      return (filename + "-copy");
    }
  }


  /**
   * Cette méthode permet de coller un fichier dans un repertoire donné
   * depuis un chemin de localisation du fichier d'origine.

   * @param originPath chemin où se trouve le fichier à coller.
   * @param destinationPath chemin où doit etre collé le fichier.
   */
  public static void pastFile(String originPath, String destinationPath) {
    // Construction du chemin absolu de chemin d'origine du fichier.
    Path originPathPath = Paths.get(originPath);
    String originFileName = originPathPath.getFileName().toString();

    // Construction du chemin du nouveau fichier
    // Exemple: dossier1\\dossierOùColler\\fichierCopie.txt
    String alreadyExistPath = destinationPath + File.separator + originFileName;
    File file = new File(alreadyExistPath);

    // Tant que le fichier à coller à le meme nom qu'un autre 
    // on concatène -copy.
    // Peut donner des choses du type monfichier-copy-copy-copy.txt
    // si monfichier.txt, monfichier-copy.txt et monfichier-copy-copy.txt
    // existaient déjà.
    while (file.exists()) {
      originFileName = renameFile(originFileName);
      alreadyExistPath = destinationPath + File.separator + originFileName;
      file = new File(alreadyExistPath);
    }

    // Copie collage du fichier
    Path destinationPathPath = Paths.get(alreadyExistPath);

    try {
      Files.copy(originPathPath, destinationPathPath);
    } catch (IOException ex) {
      // Si le fichier n'a pas reussi à etre coller.
      throw new RuntimeException("Erreur, le fichier n'a pas pu être collé.");
    }
  }

  /**
   * Cette méthode permet de coller un repertoire et tout son contenu.
   * Renomme le repertoire à copier si cela est necessaire.

   * @param originPath chemin absolu de l'endroit où se trouve le repertoire à copier coller
   * @param destinationPath chemin où l'on doit coller le repetoire
   */
  public static void pastDir(String originPath, String destinationPath) {
    File originDir = new File(originPath);
    File destinationDir = new File(destinationPath);

    // On recupère le nom du fichier qui va etre copier
    String dirName = originDir.getName();
    // Construction du chemin du nouveau repertoire
    String alreadyExistPath = destinationPath + File.separator + dirName;
    File file = new File(alreadyExistPath);

    // Vérifie que le repertoire à copier n'a pas le meme nom 
    //qu'un repertoire du fichier de destination
    while (file.exists()) {
      dirName += "-copy";
      alreadyExistPath = destinationPath + File.separator + dirName;
      file = new File(alreadyExistPath);
    }

    // Copie collage du fichier
    destinationDir = new File(alreadyExistPath);

    // Essaie de copier le repertoire et son contenu.
    try {
      // Utilisation bibliothèque Commons IO.
      FileUtils.copyDirectory(originDir, destinationDir);
    } catch (IOException ex) {
      String errorMsg = "Erreur, le repertoire et son contenu n'ont pas pu être collé.";
      throw new RuntimeException(errorMsg, ex);
    }
  }


  /**
  * Cette méthode permet de coller un fichier ou un repertoire et son contenu.

   * @param originPath  le chemin qui représente le fichier/repertoire à coller.
   * @param destinationPath le chemin qui représente le repertoire où l'on veut coller.
   */
  public static void past(String originPath, String destinationPath) {

    //On vérifie si les chemins passés en commentaire sont des chemins
    // pointes vers des chemins qui existent sur notre machine.
    File fichierRepetoireOrigin = new File(originPath);
    File fichierRepetoireDestination = new File(destinationPath);

    // Si un des chemins n'est pas valide
    if ((!fichierRepetoireOrigin.exists()) || (!fichierRepetoireDestination.exists())) {
      throw new IllegalArgumentException("Au moins un des chemins passés en entrée est invalide.");
    }
    // Si les chemins sont valides on continue.

    // Si le chemin de destination pointe sur un fichier
    // Retourne une erreur.
    if (!Files.isDirectory(Paths.get(destinationPath))) {
      String errorMsg = "Argument illégal, le chemin de destination doit etre un repertoire.";
      throw new IllegalArgumentException(errorMsg);
    } else {
      if (Files.isDirectory(Paths.get(originPath))) {
        // S'il faut coller un repertoire.
        pastDir(originPath, destinationPath);
      } else {
        // S'il faut coller un fichier.
        pastFile(originPath, destinationPath);
      }
    }
  }

  /**
   * Cette méthode permet de supprimer un fichier ou un repertoire
   * ainsi que tout son contenu.

   * @param filePath chemin du fichier ou repertoire à supprimer.
   */
  public static void delete(String filePath) {
    Path toDeletePath = Paths.get(filePath).toAbsolutePath();

    // Vérification de l'existence du fichier ou du répertoire à supprimer
    if (!Files.exists(toDeletePath)) {
      throw new IllegalArgumentException("Le chemin spécifié n'existe pas : " + filePath);
    }

    // Suppression fichier ou repertoire (+ son contenu) d'origine
    if (Files.isDirectory(toDeletePath)) {
      // S'il faut supprimer un repertoire.
      // Utilisation de la bibliothèque Apache Commons IO
      try {
        // Suppression de la note associée au fichier
        Annotation.deleteNote(filePath);

        File toDeleFile = new File(filePath);
        // On essaie de supprimer récursivement le repertoire.
        FileUtils.deleteDirectory(toDeleFile);
      } catch (IOException ex) {
        // Si impossible on renvoie une erreur.
        String errorMsg = "Le repertoire n'a pas pu etre supprimé.";
        throw new RuntimeException(errorMsg, ex);
      }

    } else {
      // S'il faut supprimer un fichier.
      try {
        // Suppression de la note associée au fichier
        Annotation.deleteNote(filePath);

        // On essaie de supprimer
        Files.deleteIfExists(toDeletePath);
      } catch (IOException ex) {
        // Si impossible on renvoie une erreur.
        String errorMsg = "Le fichier n'a pas pu etre supprimé.";
        throw new RuntimeException(errorMsg, ex);
      }
    }

  }


  /**
   * Cette méthode permet de couper un fichier ou repertoire.

   * @param originPath  le chemin qui représente le fichier/repertoire à coller.
   * @param destinationPath le chemin qui représente le repertoire où l'on veut coller.
   */
  public static void cut(String originPath, String destinationPath) {
    // Si on veut copier coller cut dans le meme repertoire
    // Cela ne sert à rien
    Path path1 = Paths.get(originPath).toAbsolutePath().getParent();
    Path path2 = Paths.get(destinationPath).toAbsolutePath();
    if (path1.equals(path2)) {
      return;
    }

    // Sinon si on veut copier coller cut dans des repertoires différents:

    // Appel méthode past() pour coller
    past(originPath, destinationPath);

    // Note: la méthode past() se charge déjà de générer une erreur si les 
    // chemins sont invalides. Donc pas besoin de le refaire.

    // Suppression fichier ou repertoire (+ son contenu) d'origine
    // + suppression de la note associé au fichier (repertoire) d'origine.
    delete(originPath);
  }

  /**
   * Cette méthode permet de rechercher dans toutes les sous répertoires du répertoire courant, 
   * le(s) fichier(s) qui contiennent le motif "pattern". Ces fichiers sont stockés dans une
   * liste avec leur chemin.

   * @param dirPath chemin du repertoire parent
   * @param pattern motif à rechercher
   * @return la liste des noms de fichier contenant le motif "pattern" + le chemin des fichiers.
   */
  public static List<List<String>> find(String dirPath, String pattern) {
    // La liste sera de la forme: [[nomFichier1, chemin1] [nomFichier2, chemin2]]
    List<List<String>> liste = new ArrayList<>();

    File dir = new File(dirPath);

    // On vérifie si le chemin passé en argument est bien un repertoire.
    if (dir.isDirectory() && dir.exists()) {
      String[] fichiers = dir.list();

      for (String fichier : fichiers) {
        File file = new File(dirPath + File.separator + fichier);

        // S'il s'agit d'un dossier
        if (file.isDirectory()) {
          String newPath = dirPath + File.separator + fichier;
          // Appel récursif pour les sous-dossiers
          liste.addAll(find(newPath, pattern));
        } else {
          // Si c'est un fichier
          // Si le fichier contient le motif
          if (fichier.contains(pattern)) {
            // On l'ajoute à la liste des resultats
            liste.add(List.of(fichier, dirPath));
          }
        }
      }
    } else {
      // Si le chemin passé en argument n'est pas un repertoire
      // ou n'existe pas
      String errorMsg = "Le chemin entrée dans correspondre à un répertoire ou n'existe pas.";
      throw new IllegalArgumentException(errorMsg);
    }
    return liste;
  }
  /**
   * Cette méthode exécute la commande demandée par l'entrée utilisateur.

   * @param repertoire le répertoire dans lequel on travaille
   * @param cli la commande utilisateur
   */
  public static void execCommande(Repertoire repertoire, Cli cli) {
    switch (cli.commande) {
      case COPY -> repertoire.pressePapier = copyFile(repertoire.nerToPath(cli.ner));
      case PAST -> past(repertoire.pressePapier, repertoire.repertoireCourant);
      case CUT -> cut(repertoire.pressePapier, repertoire.repertoireCourant);
      case PD -> repertoire.repertoireCourant = parentDir(repertoire.repertoireCourant);
      case DIR -> repertoire.repertoireCourant = childDir(repertoire.nerToPath(cli.ner));
      case MKDIR -> makeDir(repertoire.repertoireCourant, cli.nom);
      case DEL -> delete(repertoire.nerToPath(cli.ner));
      case VISU -> {
        System.out.println(ansi().eraseScreen());
        System.out.println(ansi().render(visu(repertoire.nerToPath(cli.ner))).reset());
        cli.afficheEtAttend("Appuyez sur entrée pour continuer:");
      }
      case FIND -> {
        List<List<String>> resultat = find(repertoire.repertoireCourant, cli.nom);
        //Si liste vide
        if (resultat.isEmpty()) {
          System.out.println(ansi().render("@|red Aucun résultat n'a été trouvé.|@").reset());
          cli.afficheEtAttend("Appuyez sur entrée pour continuer:");
          return;
        }
        System.out.println(ansi().eraseScreen());
        //Pour chaque couple de la liste, on affiche le nom de fichier et le chemin relatif
        resultat.forEach(
                couple -> System.out.println(ansi().render(
                        "@|bold,yellow " + couple.get(0) + "|@" + "\t" //Nom fichier(gras,jaune)
                                + "@|magenta " + Paths.get(repertoire.repertoireCourant)
                                        .relativize(Paths.get(couple.get(1)))
                                + "|@").reset())); //chemin relatif
        System.out.println();
        cli.afficheEtAttend("Appuyez sur entrée pour continuer:");
      }
      case ANNOTE -> Annotation.annote(repertoire.nerToPath(cli.ner), cli.nom);
      case SUPPRNOTE -> Annotation.deleteNote(repertoire.nerToPath(cli.ner));
      case NER -> { }
      default -> throw new IllegalArgumentException("Argument illégal.");
    }
  }
}