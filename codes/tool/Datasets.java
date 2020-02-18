package tool;

import java.io.IOException;

import graph_clustering.Ground_truth;

public class Datasets {

	public Dataset ds[] = null;
	public graphReady g = null;
	String mainDir = null;

	public Dataset d;
	public String Initialize(int id) throws IOException {
		g = new graphReady();
		d = ds[id];
		if (!d.ifNormalized)
			g.normalize(d.dataPath, d.sp, d.nodeLine1, d.nodeLine2, d.weightLine, d.startLine, d.weighted);
		else
			g.readGraph(d.dataPath, d.sp, d.nodeLine1, d.nodeLine2, d.weightLine, d.startLine, d.weighted, d.ifAdd1);

		return (d.dataPath);
	}
}