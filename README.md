# motif-path

This repository contains codes and datasets used in [On Analysing Graphs with Motif-Paths].

## Authors 

- Anonymous during paper review

## Dependencies.
* [JavaSE-1.8.](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)
* [gs-core-1.3 (for visuilization part only).](http://graphstream-project.org/download/)

## Datasets
The code takes the **edge list** of the graph. Every row indicates an edge between two nodes separated by a comma. The first row is a header. Nodes should be indexed starting with 1. The datasets used in the paper are included in the  `data/` directory.

* The three protein-protein interaction (PPI) networks are within folder `data/ppi/`, named `ppi-gavin` (GAVI in the paper), `ppi-kcore` (KCOR in the paper) and `ppi-kextend` (EXTE in the paper). In each file, each line (except the first line) denotes an edge. The ground-truth communities are `ppi-gavin.gt`, `ppi-kcore.gt` and `ppi-kextend.gt` repectively, in which a line denotes a ground-truth community.
* The two social networks are within folder `data/social/`, named `dblp` (DBLP in the paper) and `amazon` (AMAZ in the paper). The format is same as ppi networks. The ground-truth communities are `dblp.gt` and `amazon.gt` repectively. The subgraphs of DBLP for node ranking, DBLP-1 and DBLP-2 are also included, named `dblp1` and `dblp2`. The two files `dblp1.nodes` and `dblp2.nodes` decsribe the information of author_names and H-Index values, where each line is in the format `node_id	author_name	H-Index`.
* The synthetic networks are within folder `data/syn/`, named `s1`, `s2`, `s3`, `s4` and `s5`. The format is same as ppi networks. The format is same as ppi networks.
 
## Usage of Codes
The codes are included in the  `codes/` directory, which are compiled into `codes/mpath.jar` and provide the following command line arguments. Note that the basic usage is `java -jar codes/mpath.jar [option:value]`.

#### Motif-path options	`codes/mpath.jar`
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

For example, the command `java -jar codes/mpath.jar -g:data/ppi/gavin -m:5 -d:1 -s:1 -t:100` will output a shortest motif-path between nodes (1,100) from AMAZ based on triangle and bridging-edge-based defragmentation. Both shortest motif-path and corresponding shortest motif-sequence will be outputed. Note that a traditional shortest path will be calculated by using `-m:1`.

#### Motif-graph defragmentation options	`codes/mpath.jar`
```             
-b	Calculate motif-components.
```
To run bridging-edge-based defragmentation `-d:1`, the motif-components should be calculated beforehand, e.g., `data/ppi/gavin-m5.mcom`, by running `java -jar codes/mpath.jar -b -m:5 -g:data/ppi/gavin`. A file named `data/ppi/gavin-m5.mcom` will be generated with a line recording the motif-component ID of each node.

Similarly, before running `-d:2`, you need to run the codes from [EdMot](https://github.com/benedekrozemberczki/EdMot), and get the enhanced motif-graph by adding `nx.write_edgelist(self.graph, "gavin.edmot")` into function `_fill_blocks` of `src/edmot.py`. [EdMot](https://github.com/benedekrozemberczki/EdMot) only supports triangle. A copy of `x.mcom` and `x.edmot` have been prepared in `data/` for each dataset `x`, except `data/social/dblp.edmot` and `data/social/amazon.edmot`, since EdMot cannot terminate in one day).

#### Motif-path based link prediction options
```
-g	The path of the graph.				Default is `data/ppi/gavin`.
-m	The motif pattern:				Default is `5`.
-d	The defragmentation manner:			Default is `0`.
	  0 for non-defragmentation,
	  1 for injecting bridging edges, 
	  2 for injecting motif-clique edges.	
-n	The number of iterations.			Default is `1000`.        
-t	The metric to be applied:			Default is `0`.   
	  0 for Graph Distance,
	  1 for Katz Index.	  
-q	Generate missing and nonexistent queries.
```
For example, the command `java -jar codes/linkp.jar -g:data/ppi/gavin -m:5 -t:1 -d:1` will output the triangle-path based link prediction results, with bridging-edge-based defragmentation and Katz Index as the link prediction metric.

To run motif-path based link prediction, the query nodes of missing-links (positive sampling) and nonexistent-links (negative sampling) should be sampled beforehand, e.g., `data/ppi/gavin.linkp`, by running `java -jar codes/linkp.jar -q -n:1000 -g:data/ppi/gavin`. A file named `data/ppi/gavin.linkp` will be generated where each line records the ID of missing-link nodes (the first and second number) and the ID of nonexistent-link nodes (the third and last number). Note that the missing-links and the nonexistent-links follow the same shortest path distance distribution. A copy of `x.linkp` for each dataset has been prepared in `data`.

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
We implement the following competitors and integegrate them into our codes. To demonstrate the usage, we show the parameters with dataset gavin and 1000 interactions.

- Common Neighbors (CN)
```
java -jar codes/linkp.jar -g:data/ppi/gavin -o -n:1000
```
- Motif-based Common Neighbors (MCN)
```
java -jar codes/linkp.jar -g:data/ppi/gavin -c -n:1000
```
- Jaccard Coefficient (JC)
```
java -jar codes/linkp.jar -g:data/ppi/gavin -j -n:1000
```
- Adaminc/Adar (AA)
```
java -jar codes/linkp.jar -g:data/ppi/gavin -a -n:1000
```
- Preferential Attachment (PA)
```
java -jar codes/linkp.jar -g:data/ppi/gavin -p -n:1000
```
- Friends Measure (FM)
```
java -jar codes/linkp.jar -g:data/ppi/gavin -f -n:1000
```
- Hitting Time (HT)
```
java -jar codes/linkp.jar -g:data/ppi/gavin -h -n:1000
```
- Rooted PageRank (RPR)
```
java -jar codes/linkp.jar -g:data/ppi/gavin -r -n:1000
```
- Motif-based Link Prediction (MLP)
- Degree based Node Ranking (DEG)
