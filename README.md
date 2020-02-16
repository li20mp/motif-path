# motif-path

This repository contains codes and datasets used in [On Analysing Graphs with Motif-Paths].

## Authors 

- Anonymous during paper review

## Dependencies.
* [JavaSE-1.8.](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)
* [gs-core-1.3 (for visuilization part only).](http://graphstream-project.org/download/)

## Datasets
The code takes the **edge list** of the graph. Every row indicates an edge between two nodes separated by a "\t". The first row is a header. Nodes should be indexed starting with 1. The datasets used in the paper are included in the  `data/` directory.

* The three protein-protein interaction (PPI) networks are within folder `data/ppi/`, named `ppi-gavin` (GAVI in the paper), `ppi-kcore` (KCOR in the paper) and `ppi-kextend` (EXTE in the paper). In each file, each line (except the first line) denotes an edge. The ground-truth communities are `ppi-gavin.gt`, `ppi-kcore.gt` and `ppi-kextend.gt` repectively, in which a line denotes a ground-truth community.
* The two social networks are within folder `data/social/`, named `dblp` (DBLP in the paper) and `amazon` (AMAZ in the paper). The format is same as ppi networks. The ground-truth communities are `dblp.gt` and `amazon.gt` repectively. The subgraphs of DBLP for node ranking, DBLP-1 and DBLP-2 are also included, named `dblp1` and `dblp2`. The two files `dblp1.nodes` and `dblp2.nodes` decsribe the information of author_names and H-Index values, where each line is in the format `node_id	author_name	H-index`.
* The synthetic networks are within folder `data/syn/`, named `s1`, `s2`, `s3`, `s4` and `s5`. The format is same as ppi networks. The format is same as ppi networks.
 
## Usage of Codes
The codes are included in the  `codes/` directory, which are compiled into `codes/mpath.jar` and provide the following command line arguments. Note that the basic usage is `java -jar codes/mpath.jar [option:value]`.

#### Motif-path options
```
-g	The path of the graph.				Default is `data/ppi/gavin`.
-m	The motif pattern:				Default is `5`.
	  The supported motif ID is in the figure below.
-s	The source node ID.				Default is `1`.
-t	The target node ID.				Default is `2`.
-d	The defragmentation manner:			Default is `0`.
	  0 for non-defragmentation,
	  1 for injecting bridging edges, 
	  2 for injecting motif-clique edges.	
```
<p align="center">
  <img width="800" src="motifs.PNG">
</p>

For example, the command `java -jar codes/mpath.jar -g:data/ppi/gavin -m:5 -d:1 -s:1 -t:100` will output a shortest motif-path between nodes (1,100) from AMAZ based on triangle and bridging-edge-based defragmentation. To run bridging-edge-based defragmentation `-d:1`, the bridging edges should be calculated beforehand, e.g., `data/ppi/gavin-m5.bedges`, by running `java -jar codes/mpath.jar -bedges -m:5 -g:data/ppi/gavin`. Similarly, before running `-d:2`, you need to run the codes from [EdMot](https://github.com/benedekrozemberczki/EdMot), and get the enhanced motif-graph by adding `nx.write_edgelist(self.graph, "gavin.cedges")` into function `_fill_blocks` of `src/edmot.py`. [EdMot](https://github.com/benedekrozemberczki/EdMot) only supports triangle. A copy of `bedges` (the bridging edges for each dataset) and `cedges` (the enhanced motif-graph for each dataset, except dblp and amazon which cannot terminate in one day) have been prepared in `data/`.

#### Motif-path based link prediction options
```             
  --cutoff       INT     Random seed.                   Default is 2.
  --components   INT     Number of motif components.    Default is 1.
```

#### Motif-path based local graph clustering options
The following commands learn an EdMot Clustering.
```sh
$ python src/main.py
```

#### Motif-path based node ranking options
Increasing the motif graph component number.
```sh
$ python src/main.py --components 2
```

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
