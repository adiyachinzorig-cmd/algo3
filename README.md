## 1. Structure du projet
Le projet est organisé de la manière suivante :
* `base/` : Outils de parsing fournis.
    * `DblpPublicationGenerator.java` : Lecteur séquentiel (SAX) du flux XML pour limiter l'empreinte mémoire.
* `Tache1/` : Outils/Classes pour la Tache1
    * `Cammunauté.java`
    * `UnionFind.java`
* `Tache1.java` : Résolution de la tache 1
* `Tache2.java` : Résolution de la tache 2
* `dblp.dtd` : fichier dblp.dtd
* `graphique.py` : code python qui affiche les résultats de la tache 1
* `dblp-2026-01-01.xml`

Note: Il faut placer dblp-2026-01-01.xml à la racine du projet

## 2. Compilation 
Pour compiler la tache 1 :
    >>javac Tache1.java Tache1/*.java base/DblpPublicationGenerator.java



Pour compiler la tache 2 :
    >>javac Tache2.java base/DblpPublicationGenerator.java


## 3. Execution
Pour executer Tache 1 :
    >>java Tache1 dblp-2026-01-01.xml.gz dblp.dtd

Vous pouvez également ajouter l’argument --limit=N pour vous limiter aux premières N publications du fichier xml.

Pour l'histogramme, il faut simplement lancer `graphique.py` sans argument



Pour executer Tache 2 :

    >>java Tache1 dblp-2026-01-01.xml.gz dblp.dtd


## 4. Sortie 
Tache 1 :
Un document csv `histogrammeTache1.csv` contenant la taille ainsi que la fréquence d'apparition des communautés. Il peut être mis sous forme de graphe avec `graphique.py`


Tache 2 :
La commande d'exécution affiche, dès le lancement dans le terminal, les 10 plus grandes communautés identifiées de la tâche 2, suivies de l'histogramme des tailles de communautés de la même tâche.
