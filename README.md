# motif-path

This repository contains codes and datasets used in [On Analysing Graphs with Motif-Paths].


## Authors 

- Anonymous during paper review

### Settings and Dependencies.
* [JavaSE-1.8.](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)
* [gs-core-1.3. (for visuilization part only)](http://graphstream-project.org/download/)

## Datasets

### Protein-protein Interacttion (PPI) Networks
### Social Networks
### Syhthetic Networks
 
## Codes
### Usage of Jar files 

## Competitors
In this section, we introduce the usage of the competitors. For the competitors with codes released, please refer the original introduction. For the competitors without codes released, we implemented them and integegrated them together with graph mining codes. Below please find their usages.
### Competitors with released codes
- [Motif-Aware Graph Clustering (Tectonic)](https://github.com/tsourolampis/tectonic) 
- [Local Higher-Order Graph Clustering (MAPPR)](http://snap.stanford.edu/mappr/)
- [Edge Enhancement Motif Clustering (EdMot)](https://github.com/benedekrozemberczki/EdMot) 
- [Motif PageRank based node ranking (MPR)](https://github.com/HKUST-KnowComp/Motif-based-PageRank) 
	- PageRank (PR) is integrated in MPR by setting alpha_value = 1. 
	- Weighted PageRank (WPR) is integrated in MPR by setting alpha_value = 1 and remove line 216 of motif_construct_direct.py ("adjacency_matrix.data = np.ones((1, lennn), dtype=np.float64)[0]").

### Competitors implemented by us
- Common Neighbors (CN)
- Jaccard Coefficient (JC)
- Adaminc/Adar (AA)
- Preferential Attachment (PA)
- Friends Measure (FM)
- Hitting Time (HT)
- Rooted PageRank (RPR)
- Motif-based Common Neighbors (MCN)
- Motif-based Link Prediction (MLP)
- Degree based Node Ranking (DEG)
