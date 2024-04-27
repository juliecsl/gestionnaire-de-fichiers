# Gestionnaire de Fichiers en Commandes en Ligne avec Annotations
*Réalisé par Julie Ciesla et Anaël Messan*
![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.png)

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table des matières</summary>
  <ol>
    <li><a href="#🎯-objectif-du-projet">Objectif du projet</a></li>
    <li>
      <a href="#📑-manuel-utilisateur">Manuel utilisateur</a>
        <ul>
        <li><a href="#quest-ce-que-le-ner">Qu'est ce que le NER ?</a></li>
        <li><a href="#modifier-le-NER-courant">Modifier le NER courant</a></li>
        <li><a href="#copier-un-fichier-ou-dossier">Copier un fichier ou dossier</a></li>
        <li><a href="#couper-un-fichier-ou-dossier">Couper un fichier ou dossier</a></li>
        <li><a href="#coller-un-fichier-ou-dossier">Coller un fichier ou dossier</a></li>
        <li><a href="#supprimer-un-fichier-ou-dossier">Supprimer un fichier ou dossier</a></li>
        <li><a href="#remonter-dans-le-répertoire-parent">Remonter dans le répertoire parent</a></li>
        <li><a href="#entrer-dans-un-répertoire">Entrer dans un répertoire</a></li>
        <li><a href="#créer-un-répertoire">Créer un répertoire</a></li>
        <li><a href="#visualiser-le-contenu-dun-fichier">Visualiser le contenu d'un fichier</a></li>
        <li><a href="#rechercher-des-fichiersrépertoires-dont-les-noms-contiennent-un-certain-motif">Rechercher fichier/répertoire par nom</a></li>
        <li><a href="#ajouter-une-note-à-un-fichierrépertoire">Ajouter une note</a></li>
        <li><a href="#supprimer-une-note-associée-à-un-fichierrépertoire">Supprimer une note</a></li>
        <li><a href="#quitter-le-programme">Quitter le programme</a></li>
      </ul>
    </li>
    <li><a href="#🛠️-manuel-technique">Manuel technique</a></li>
      <ul>
        <li><a href="#installation---versions">Installation - versions</a></li>
        <li><a href="#compilation-du-projet">Compilation du projet</a></li>
        <li><a href="#execution-de-lapplication">Execution de l'application</a></li>
        <li><a href="#consultation-du-code-de-courverture">Consultation du code de courverture</a></li>
        <li><a href="#execution-de-lapplication">Execution de l'application</a></li>
        <li><a href="#classes-implémentées">Classes implémentées</a></li>
        <li><a href="#bibliothèques-utilisées">Bibliothèques utilisées</a></li>
        <li><a href="#gestion-des-entrées-utilisateur">Gestion des entrées utilisateur</a></li>
      </ul>
    <li><a href="#📂-contenu-du-projet">Contenu du projet</a></li>
    <li><a href="#💭-axes-damélioration">Axes d'amélioration</a></li>
  </ol>
</details>


## 🎯 Objectif du projet
<p style="text-align:justify;"> L'objectif du projet est de développer un gestionnaire de fichiers en ligne de commande en Java, par groupes de deux étudiants collaborant à travers un référentiel Git commun. Le gestionnaire  doit permettre d'effectuer des opérations telles que copier, couper, remonter dans le système de fichiers, créer des répertoires, visualiser le contenu des fichiers, et annoter les éléments du répertoire. Les annotations sont stockées dans un fichier "notes" dans le répertoire courant. </p>

![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.png)
## 📑 Manuel utilisateur
Les commandes suivantes sont à [exécuter dans le fichier `explorer-1.0.jar`](#execution-de-lapplication) disponible dans le dossier `target` après avoir [compilé le projet](#compilation-du-projet).

Les commandes doivent être saisies au clavier sous la forme:   ```[<NER>] [<commande>] [<nom>]```  
L'utilisateur pourra s'aider de l'auto-complétion pour entrer le nom des commandes.
Les crochets signifiant "optionnel" et NER représentant le numéro du fichier sur l'affichage.

### Qu'est-ce que le NER ?
Le NER est un numéro associé à un fichier (ou dossier) qui permet de le designer lors de l’utilisation d’une des commandes.
Le NER est présent dans l'affichage à gauche du nom des fichiers et dossiers.

Si l’utilisateur ne place pas de NER, c’est le dernier NER utilisé qui sera utilisé pour une commande qui en nécessite.

### Modifier le NER courant
La commande `<NER>` permet d'inititaliser ou modifier le NER courant.  

### Exemple d'affichage
L'affichage ci-dessous servira d'exemples pour la présentation des commandes.
```
Contenu de : Dossier1/dossier2/dossier3
001 fable.pdf
002 rafale.txt
003 SousDossier

Entrer une commande:
```

Sur la première ligne de l'affichage on retrouve le chemin du répertoire courant.
De la seconde ligne à la quatrième, dans cet exemple, à gauche sont indiqué les numéros NER et à droite les noms des fichiers/dossiers contenues dans le répertoire courant.
Enfin la dernière ligne propose à l'utilisateur d'entrer une commande.

### Copier un fichier ou dossier
La commande `[<NER>] copy` permet de copier un fichier ou un dossier.

[Exemples](#exemple-daffichage):
```
1 copy  # Pour copier le fichier fable.pdf
2 copy  # Pour copier le fichier rafale.txt
3 copy  # Pour copier le répertoire SousDossier
copy    # Pour copier le répertoire SousDossier
```

### Coller un fichier ou dossier
La commande `past` permet de coller un fichier ou un dossier. Il est nécessaire d'utiliser au préalable la commande [`copy`](#copier-un-fichier-ou-dossier) pour pouvoir coller un élément ou la commande [`cut`](#couper-un-fichier-ou-dossier) pour pouvoir couper un élément.

Si l'élément coller est de même nom qu'un fichier/dossier déjà existant dans le répertoire alors le nom de l'élément est concatené à "-copy". 

### Couper un fichier ou dossier
La commande `[<NER>] cut` permet de couper un fichier ou un dossier. 

[Exemples](#exemple-daffichage):
```
1 cut  # coupe le fichier fable.pdf
2 cut  # coupe le fichier rafale.pdf
3 cut  # coupe le répertoire sousDossier
cut    # coupe le répertoire sousDossier
```

> **Attention**: lorsque l'on coupe un fichier/dossier son annotation est supprimée sans etre retransférée dans le dossier dans lequel le fichier/dossier a été collé. 

### Supprimer un fichier ou dossier
La commande `[<NER>] del` permet de supprimer un fichier ou un dossier.

[Exemples](#exemple-daffichage):
```
1 del  # coupe le fichier fable.pdf
2 del  # coupe le fichier rafale.pdf
3 del  # coupe le répertoire sousDossier
```

> Remarque: Lorsqu'un fichier/dossier est supprimé sa note est aussi supprimée.

### Remonter dans le répertoire parent
La commande `..` permet de se déplacer dans le répertoire parent.
 
### Entrer dans un répertoire
La commande `[<NER>] .` permet d'entrer dans un **répertoire**.

[Exemple](#exemple-daffichage):
```
3 .   # Pour entrer dans le répertoire SousDossier 
```

### Créer un répertoire
La commande `mkdir <nom répertoire>` permet de créer un ou plusieurs sous-répertoire dans le répertoire courant.

[Exemples](#exemple-daffichage):
```
mkdir nouveau dossier
mkdir un sous repertoire/un autre
```
Pour la première commande un dossier nommé "nouveau dossier" sera créé. Pour la seconde commande un répertoire nommé "un sous repertoire" sera créé et ce répertoire contiendra un répertoire nommé "un autre".

### Visualiser le contenu d'un fichier
La commande `[<NER>] visu` permet de visualiser le contenu d'un **fichier** au format texte (.txt ou .text) et pour les **fichiers** non texte d'afficher leur taille en octet(s). 

[Exemples](#exemple-daffichage):
```
1 visu  # Affiche la taille de fable.pdf en octets
2 visu  # Affiche le contenu de rafale.txt
```


### Rechercher des fichiers/répertoires dont les noms contiennent un certain motif
La commande `find <motif>` permet de rechercher dans tous les sous répertoires du répertoire courant, le(s) fichier(s)/dossier(s) qui contiennent le motif "motif". La résultat de cette commande permet d'afficher le nom des fichiers et répertoires contenant le motif ainsi que le chemin de ces fichiers/répertoires.

> **Attention**: le motif est sensible à la casse.


### Ajouter une note à un fichier/répertoire
La commande `[<NER>] + text` permet d'ajouter du texte informatif à propos du fichier/dossier désigné par le NER.

[Exemples](#exemple-daffichage):
```
1 + "ceci est un texte"  
2 + "voici un autre texte
3 + "je suis un texte"
```

### Supprimer une note associée à un fichier/répertoire
La commande `[<NER>] -` permet de supprimer la note associé au fichier.dossier associé au NER. Si aucune note n'existait au préalable cela ne renvoie pas d'erreur.

[Exemples](#exemple-daffichage):
```
1 -
2 -
3 -
```

### Quitter le programme
Pour pouvoir sortir du programme il fait appyuer sur les touches `Ctrl + C`.

![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.png)
## 🛠️ Manuel technique

### Installation - versions
- **Java**: version 17
- **JUnit5**: version 5.9.1
- **Jacoco**: 0.8.11
- **Apache Commons IO**: version 2.15.0

### Compilation du projet
Sous Linux
```
$ ./mvnw package
```

Sous Windows
```
> .\mvnw.cmd package  
```

ou

```
> mvn package  
```

### Execution de l'application
```
$ java -jar target/explorer-1.0.jar
```
### Consultation du code de courverture
Entrer la ligne de commande suivante:
```
mvn clean test jacoco:report
```
Puis aller dans `\target\site\jacoco\index.html`.

### Classes implémentées

Pour ce projet nous avons implémenté XX classes, leurs roles sont les suivants:
- [**Commandes**](src/main/java/fr/uvsq/cprog/Commandes.java): fournit des méthodes facilitant la gestion des fichiers et répertoires en Java. Elle permet notamment de visualiser le contenu d'un fichier texte, de créer des répertoires, de vérifier l'existence d'un chemin, de copier, coller, couper et renommer des fichiers et repertoires, de manipuler des répertoires (y entrer et y sortir), ainsi que de gérer la suppression de fichiers ou répertoires, tout en fournissant des mécanismes de gestion des erreurs.

-  [**Annotation**](src/main/java/fr/uvsq/cprog/Annotation.java): fournit des méthodes pour annoter des fichiers (nom du fichier + texte informatif) en stockant ces informations dans un fichier sérialisé au format XML nommé "notes.xml". Cette classe propose des méthodes permettant d'ajouter, de consulter et de supprimer des annotations associées à des fichiers.

- [**Fichier**](src/main/java/fr/uvsq/cprog/Fichier.java): La classe Fichier représente un objet sérialisable "Fichier" avec deux attributs : un nom et un texte associé. Elle est destinée à être utilisée en parallèle avec la classe Annotation pour stocker des informations sur des fichiers dans un fichier sérialisé au format XML.

- [**CLI**](src/main/java/fr/uvsq/cprog/Cli.java): Cette classe représente les différentes parties d'une commande entrée par l'utilisateur. 
Elle est implémentée de telle manière à représenter la dernière commande de l'utilisateur avec le dernier NER entré.
Les méthodes de cette classe permettent de traîter les entrées utilisateurs, c'est-à-dire reconnaître si une entrée est valide et l'opération que l'utilisateur souhaite réaliser.
- [**App**](src/main/java/fr/uvsq/cprog/App.java): C'est la classe principale du programme. Elle ne contient qu'une méthode principale qui instancie les classes Cli et Repertoire et appelle les méthodes de classes des différentes classes du programme.
- [**Repertoire**](src/main/java/fr/uvsq/cprog/Repertoire.java): Cette classe représente le répertoire courant avec ses éléments sous forme de liste de chemins de fichiers. Elle fournit notamment les méthodes qui permettent d'afficher ces éléments, d'afficher la note associée à l'élément courant et de construire et actualiser la liste des éléments de ce répertoire. C'est dans cette classe qu'est stocké le presse papier (sous forme de chemin) lorsque l'on souhaite couper ou copier un élément.


### Bibliothèques utilisées
1. Apache Commons IO

La bibliothèque Apache Commons IO est utilisée pour simplifier certaines opérations de gestion de fichiers, notamment la copie récursive de répertoires et la suppression récursive.

2. Java.io

- **BufferedReader** : Utilisé pour lire le contenu d'un fichier ligne par ligne.
- **File** : Fournit une abstraction pour représenter les chemins de fichiers ou de répertoires.
- **FileReader** : Utilisé pour créer un flux de caractères à partir d'un fichier.
- **IOException** : Traite les exceptions.
- **Serializable**:  Utilisé pour pouvoir sérialiser les instances de la classe Fichier.
- **FileInputStream**: Pour lire des octects à partir d'un fichier au format XML.
- **FileOutputStream**: Pour écrire des octects dans un fichier au format XML.

3. Java.nio.file
- **FileSystems** : Pour permettre de créer des objets Path à partir de chaînes de caractères.
- **Files** : Pour effectuer diverses opérations sur les fichiers, telle que la vérification de l'existence d'un chemin.
- **Path** : Pour permettre de représenter un chemin de fichier ou de répertoire.

4. Java.util
- **ArrayList** : Utilisée pour stocker et manipuler des listes dynamiques. Utilisées principalement pour stocker des résultats et objets.
- **List** : Utilisée pour représenter des listes et pour ofurnir des méthodes pour manipuler les éléments de la liste, tels que l'ajout, la suppression et l'accès aux éléments.
- **stream.Collectors** : Utilisée pour convertir des flux en listes.

5. Java.beans

Utilisée pour la sérialisation et désérialisation d'objets en format XML.
- **XMLEncoder**: Pour convertir des objects en document au format XML.
- **XMLDecoder**: Pour lire des documents au format XML et les convertire en objects Java.

6. org.junit.jupiter.api
- **Test**: permet l'execution de tests unitaires.
- **AfterAll**: principalement utilisée pour supprimer des fichiers crées par des tests.
- **AfterEach**: principalement utilisée pour supprimer des fichiers crées par des tests.

7. org.junit.jupiter.api.Assertions
- **assterDoesNotThrow**: utilisée pour vérifier qu'aucune exception n'est lancée.
- **assertEquals**: utilisé pour vérifier si deux valeurs sont égales.
- **assertThrows**: utilisée pour qu'une exception est levée.
- **assertTrue**: utilisée pour vérifier si une condition est vraie.

8. org.fusesource.jansi
- **AnsiConsole** et **Ansi.ansi**: utilisées pour l'affichage en conjonction avec Jline.

9. org.jline.reader
- **LineReader**: utilisée pour acqérir les entrées utilisateur.

### Gestion des entrées utilisateur
Pour déterminer la commande de l'utilisateur, le programme effectue les opérations suivantes:
- Séparation en 2 des éléments de la commande au premier espace rencontré.
  - Si le premier élément est un NER valide: la commande est séparée en 3 au niveau des espaces.
- Le type de la commande est ensuite stocké dans l'instance de la classe Cli.
- S'il y'a un élément après la commande (2nde ou 3ème partie de l'entrée utilisateur), celui-ci est aussi stocké dans l'objet.
- Enfin, le cas échéant, le nouveau NER est stocké dans l'objet.

Exemple: `3 + une note`
- Séparation:  
`["3", "+ une note"]`  c'est une commande avec NER, on sait donc que l'on va trouver la commande après le 1er espace.  
- Séparation:  
`["3", "+", "une note"]` c'est une commande pour ajouter une annotation : on stocke cette information dans Cli..ommande. On sait aussi que cette commande prend en argument le texte de la note. On le stocke donc dans Cli.nom (désigne l'argument de la commande). Enfin, on va stocker le NER sous forme d'entier.

![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.png)
## 📂 Contenu du projet

```
.
├── 1-fichierTest
│   ├── 1-fichierTest2
│   │   └── vide.txt
│   ├── fable.pdf
│   └── fable.txt
├── 2-fichierTest3
│   ├── 1-UnAutreDossier
│   |   ├── 1-SousDossier
│   |   |   └── fable.txt
│   |   ├── fable.pdf
│   │   └── rafale.txt
│   └── vide.txt
├── 3-fichierTestAffNote
│   ├── notes.xml
│   └── unFichier.txt
├── 4-fichierTestBis
│   ├── 1-fichierTest2
│   |   └── vide.txt
│   ├── fable.pdf
│   └── fable.txt
├── 5-src
│   ├── 1-main\java\uvsq\cprog
│   │   ├── Annotation.java
│   │   ├── App.java
│   │   ├── Cli.java
│   │   ├── Commandes.java
│   │   ├── Fichier.java
│   │   └── Repertoire.java
│   └── 2-test\java\uvsq\cprog
│   │   ├── AnnotationTest.java
│   │   ├── CliTest.java
│   │   ├── CommandesTest.java
│   │   └── RepertoireTest.java
├── .gitignore
├── Documentation.md
├── fable.pdf
├── fable.txt
├── mvnw
├── mvnw.cmd
├── pom.xml
├── README.adoc
└── vide.txt
```

![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/rainbow.png)

## 💭 Axes d'amélioration
Pour améliorer notre projet nous pourrions dans la commande `[<NER>] visu` gérer l'affichage de fichier au format .png ou .jpg.
Pour le faire dans une console, il faudrait lire les pixels de l'image avec la bibliothèque **java.awt.image** puis les représenter à l'aide de **JAnsi** ou **JColor**.  

Nous pourrions aussi ajouter les commandes suivantes:
- Création de nouveaux fichiers.
- Permettre d'afficher les fichiers/dossiers selon un ordre voulu par l'utilisateur tels que ordre alphabéthique, date de modification, taille de fichier ...
- Affichage les propriétés d'un fichier (date de création, date de dernière modification, taille du fichier, type d'extension...).
- Renommer des fichiers et répertoires.
- Mimer la commande `cd` de bash.

