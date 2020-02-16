# motif-path

This repository contains codes and datasets used in [On Analysing Graphs with Motif-Paths].


## Authors 

- Anonymous during paper review

## Dependencies.
* [JavaSE-1.8.](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)
* [gs-core-1.3 (for visuilization part only).](http://graphstream-project.org/download/)

## Datasets

* The three protein-protein interaction (PPI) networks are within folder "./data/ppi/", named "ppi-gavin" (GAVI in the paper), "ppi-kcore" (KCOR in the paper) and "ppi-kextend" (EXTE in the paper). In each file, each line (except the first line) denotes an edge. The ground-truth communities are "ppi-gavin.sgd", "ppi-kcore.sgd" and "ppi-kextend.sgd" repectively, in which a line denotes a ground-truth community. Note that the three datasets are normalized. The raw datasets are from .
* The two social networks are within folder "./data/social/", named "dblp" (DBLP in the paper) and "amazon" (AMAZ in the paper). The format is same as ppi networks. The ground-truth communities are "dblp.gt" and "amazon.gt" repectively. The raw datasets come from .
* The synthetic networks are within folder "./data/syn/", named "s1", "s2", "s3", "s4" and "s5". The format is same as ppi networks. The format is same as ppi networks.
 
## Codes
### Usage of Jar files 

<p align="center">
  <img width="800" src="motifs.PNG">
</p>

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
