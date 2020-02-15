====================================
Datasets for benchmarking ClusterONE
====================================

This archive contains the protein-protein interaction datasets that we used to
benchmark ClusterONE against alternative protein complex detection methods.

Each dataset is contained in a single file in the ``datasets`` folder. These
files are in a simple tabular format where each row represents one interaction.
Columns are separated by whitespace.  The first two columns contain the
systematic names of the two interactors involved in the interaction, while the
third column contains a weight (i.e.  confidence) associated to the
interaction. The third column is optional; when it is given, it always contains
values between zero and one.

The datasets are as follows:

``collins2007.txt``
    The weighted interaction map of Collins et al [1]_, using the top 9074
	interactions as suggested by the authors.

``krogan2006_core.txt``
    The core interaction dataset from Krogan et al [2]_.

``krogan2006_extended.txt``
	The extended interaction dataset from Krogan et al [2]_; this is thought
	to contain less reliable interactions than the core dataset, but its
	coverage is higher.

``gavin2006_socioaffinities_rescaled.txt``
	The dataset of Gavin et al [3]_, where the interactions are weighted by
	the socio-affinity scores as calculated by Gavin et al [3]_. Since the
	socio-affinity indices may take negative values as well as weights larger
	than one, we have rescaled the scores into the range 0 to 1 in order not
	to put those algorithms at a disadvantage which assume the weights to be
	between 0 and 1.

``biogrid_yeast_physical_unweighted.txt``
    Physical protein-protein interactions between yeast proteins, as
	downloaded from BioGRID 3.1.77 [5]_. Since BioGRID does not contain
	confidence scores for all interactions, and confidence scores of
	interactions from different data sources may be incompatible with
	each other, we used all interactions without any confidence filters
	and we did not add any weight information to this dataset.

References
----------

.. [1] Collins SR et al: Toward a comprehensive atlas of the physical
       interactome of Saccharomyces cerevisiae. *Mol. Cell. Proteomics*
	   6:439-450 (2007).

.. [2] Krogan N et al: Global landscape of protein complexes in the yeast
       Saccharomyces cerevisiae. *Nature* 440:637-643 (2006).

.. [3] Gavin A et al: Proteome survey reveals modularity of the yeast
       cell machinery. *Nature* 440:631-636 (2006).

.. [4] Pu S, Wong J, Turner B, Cho E and Wodak S: Up-to-date catalogues
       of yeast protein complexes. *Nucl. Acids Res.* 37:825-831 (2009).

.. [5] Stark C, Breitkreutz B-J, Reguly T, Boucher L, Breitkreutz A
       and Tyers M: BioGRID: a general repository for interaction
	   datasets. *Nucl. Acids Res.* 34:D535-D539 (2005).

