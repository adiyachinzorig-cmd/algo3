# Projet INFO-F-203 : Détection de communautés dans DBLP

Ce projet, réalisé dans le cadre du cours d'Algorithmique 2 (Année académique 2025-2026), porte sur l'analyse de la base de données bibliographiques DBLP. L'objectif est d'identifier des communautés d'auteurs en modélisant leurs collaborations sous forme de graphe et en extrayant les Composantes Fortement Connexes (SCC).

## 1. Description du problème
Le projet traite un flux massif de données XML pour :
1.  Identifier les paires d'auteurs ayant collaboré au moins 6 fois.
2.  Construire un graphe orienté où chaque arc représente cette collaboration fréquente.
3.  Détecter les communautés via un algorithme de calcul de SCC.
4.  Analyser la structure de ces communautés en calculant le diamètre des 10 plus grandes d'entre elles.

## 2. Structure du projet
Le projet est organisé de la manière suivante :
* `base/` : Outils de parsing fournis.
    * `DblpPublicationGenerator.java` : Lecteur séquentiel (SAX) du flux XML pour limiter l'empreinte mémoire.
    * `dblp.dtd` : Fichier de définition pour la validation XML.
* `Tache2.java` : Cœur algorithmique du projet (Traitement "online", construction du graphe, algorithme de Tarjan/Kosaraju).
* `Makefile` : Automatisation de la compilation et de l'exécution.
* `README.md` : Documentation du projet.

## 3. Prérequis
* **Java JDK 21** ou supérieur.
* **GNU Make** (ou `mingw32-make` sous Windows/MSYS2).
* **Données** : Le fichier `dblp-2026-01-01.xml.gz` doit être placé à la racine du projet.

## 4. Utilisation

### Compilation
```bash
make compile
# Ou sous Windows (Git Bash/MSYS2) : mingw32-make compile