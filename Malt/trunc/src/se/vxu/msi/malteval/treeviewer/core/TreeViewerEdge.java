package se.vxu.msi.malteval.treeviewer.core;

/**
 * This class is for a basic edge in the dependency graph. It's constitued by a src vertex index, a dst vertex index and a label. When modifying an edge, one's should make sure that the WordGraph class is aware of the modifications by calling the updateEdge method
 */
public class TreeViewerEdge {
	/** src vertex index */
	private int src;

	/** dst vertex index */
	private int dst;

	/** edge label */
	private String label;

	/** constructs a new edge without any label */
	public TreeViewerEdge(int s, int d) {
		src = s;
		dst = d;
		label = null;
	}

	/** constructs a new edge with a label */
	public TreeViewerEdge(int s, int d, String l) {
		src = s;
		dst = d;
		setLabel(l);
	}

	/** get the src vertex index */
	public int getSrc() {
		return src;
	}

	/** get the dst vertex index */
	public int getDst() {
		return dst;
	}

	/** updates the label */
	public void setLabel(String t) {
		if (t != null) {
			t.trim();
			if (t.length() == 0)
				t = null;
		}

		label = t;
	}

	/** returns the label. */
	public String getLabel() {
		if (label == null)
			return ""; // to avoid null pointer problems and simplify tests
		else
			return label;
	}

	/** compare the vertices */
	public boolean equals(Object e) {
		TreeViewerEdge a;

		if (e == this)
			return true;
		else if (e instanceof TreeViewerEdge) {
			a = (TreeViewerEdge) e;
			return (src == a.src) && (dst == a.dst);
		} else
			return false;
	}

	public String toString() {
		return src + "->" + dst + " (" + label + ")";
	}
}
