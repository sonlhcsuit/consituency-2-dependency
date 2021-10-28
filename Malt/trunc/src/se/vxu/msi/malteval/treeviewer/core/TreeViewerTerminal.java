package se.vxu.msi.malteval.treeviewer.core;

import java.util.*;

import se.vxu.msi.malteval.corpus.MaltSentence;
import se.vxu.msi.malteval.corpus.MaltWord;

/**
 * This class manages a dependency graph for an editable sentence, and includes some functionalities to test if the graph is valid or not. All modifications applied (add? update, remove) to the graph are reflected automatically on the editable sentence.
 */
public class TreeViewerTerminal {
	/** list of graph edges */
	private Vector<TreeViewerEdge> edges;

	/** the editable sentence coresponding to this graph */
	private TreeViewerSentence srcSentence;

	/** constructs a new empty dependency graph */
	public TreeViewerTerminal(TreeViewerSentence s) {
		srcSentence = s;
		edges = new Vector<TreeViewerEdge>(s.size() * 2);
	}

	/** returns the number of edges */
	public int size() {
		return edges.size();
	}

	/** returns the number of vertices (the number of words) */
	public int nbVertices() {
		return srcSentence.size();
	}

	/** returns the corresponding editable sentence */
	public TreeViewerSentence getSrcSentence() {
		return srcSentence;
	}

	/** reinitialize the graph and reconstruct the edges */
	public void reset() {
		edges.removeAllElements();
		MaltSentence s = srcSentence.getSentenceObject();
		for (int i = 0; i < s.getSentenceLength(); i++) {
			if (s.getWord(i).getHead() >= 0)
				edges.add(new TreeViewerEdge(s.getWord(i).getHead(), i, s.getWord(i).getDeprel()));
		}
	}

	/** update the given edge so that the update is reflected also in the underlying editable sentence */
	public void updateEdge(TreeViewerEdge e) {
		majParentVertexInfo();
	}

	/** returns the edge at the index i */
	public TreeViewerEdge getEdge(int i) {
		return (TreeViewerEdge) edges.get(i);
	}

	/**
	 * add a new edge. throw an exception if it is invalid
	 */
	public void addEdge(TreeViewerEdge e) {
		if (edges.contains(e))
			throw new RuntimeException("Edge already present in the graph !!!");
		else if (e.getSrc() == e.getDst())
			throw new RuntimeException("Invalid edge, must connect to separate words !!!");

		edges.add(e);
		majParentVertexInfo();
	}

	/** removes an edge */
	public void deleteEdge(int i) {
		edges.remove(i);
		majParentVertexInfo();
	}

	/** removes an edge */
	public void deleteEdge(TreeViewerEdge e) {
		edges.remove(e);
		majParentVertexInfo();
	}

	/** find an edge having src=srcV and dst=dstV */
	public TreeViewerEdge findEdge(int srcV, int dstV) {
		int i = 0;
		TreeViewerEdge e, e2 = null;

		while (i < edges.size() && e2 == null) {
			e = getEdge(i);
			if (e.getSrc() == srcV && e.getDst() == dstV) {
				e2 = e;
			} else
				i++;
		}
		return e2;
	}

	/** returns the degree of vertex ver */
	public int getVertDeg(int ver) {
		int res = 0;
		for (int i = 0; i < size(); i++) {
			if (getEdge(i).getDst() == ver || getEdge(i).getSrc() == ver)
				res++;
		}
		return res;
	}

	/** finds all the vertices connected to the sourc end of edges incomming on edge dstver */
	public int[] findSrcVertices(int dstVer) {
		Vector<Integer> v = new Vector<Integer>(50);
		int[] res;

		for (int i = 0; i < size(); i++) {
			if (getEdge(i).getDst() == dstVer)
				v.add(new Integer(getEdge(i).getSrc()));
		}

		res = new int[v.size()];
		for (int i = 0; i < v.size(); i++) {
			res[i] = ((Integer) v.get(i)).intValue();
		}

		return res;
	}

	/** finds all the vertices connected to the dest end of edges outgoing from edge srcver */

	public int[] findDstVertices(int srcVer) {
		Vector<Integer> v = new Vector<Integer>(50);
		int[] res;

		for (int i = 0; i < size(); i++) {
			if (getEdge(i).getSrc() == srcVer)
				v.add(new Integer(getEdge(i).getDst()));
		}

		res = new int[v.size()];
		for (int i = 0; i < v.size(); i++) {
			res[i] = ((Integer) v.get(i)).intValue();
		}

		return res;
	}

	/**
	 * update the tree structure (parent info) of the editable sentence to reflect this graph. not possible if the graph is not a forest
	 */
	public void majParentVertexInfo() {
		MaltWord[] vertices = srcSentence.getSentenceObject().getWords();

		for (int i = 0; i < vertices.length; i++) {
			vertices[i].setHead(-1);
		}

		for (int i = 0; i < size(); i++) {
			vertices[getEdge(i).getDst()].setHead(getEdge(i).getSrc() - 2);
			vertices[getEdge(i).getDst()].setDeprel(getEdge(i).getLabel());
		}
	}

	/** returns true if a vertex has 2 or more incomming arrows, and updates the error message */
	public boolean checkFor2IncArrows() {
		boolean ok = true;
		int[] v = new int[srcSentence.size()];
		int i, nb = 0;
		TreeViewerEdge e;

		for (i = 0; i < v.length; i++) {
			v[i] = 0;
		}

		i = 0;
		while (i < size() && ok) {
			e = getEdge(i);
			v[e.getDst()]++;
			if (v[e.getDst()] > 1) {
				ok = false;
			} else {
				i++;
				nb++;
			}
		}
		return ok;
	}

	/** returns true if the graph doesn't have any cycles, and updates the error msg */
	public boolean checkForCycles() {

		boolean cycle;
		String res;

		CycleCheck cc = new CycleCheck(this);

		res = cc.checkForCycles();
		cycle = res != null;
		return !cycle;
	}

	/** returns true if the graph is a forest, and updates the error msg */
	public boolean isForest() {
		boolean ok = true;

		ok = checkFor2IncArrows();

		if (ok == true) {
			ok = checkForCycles();
		}

		return ok;
	}

	/** returns true if the graph is a tree */
	public boolean isTree() {
		boolean ok = true;
		int[] v = new int[srcSentence.size()];
		int i;
		int nb = 0;
		TreeViewerEdge e;

		for (i = 0; i < v.length; i++) {
			v[i] = 0;
		}

		i = 0;
		while (i < size() && ok) {
			e = getEdge(i);
			v[e.getDst()]++;
			if (v[e.getDst()] > 1)
				ok = false;
			else {
				i++;
				nb++;
			}
		}

		if (ok == true && (!checkForCycles() || nb != v.length - 1))
			ok = false;

		return ok;
	}

	public String toString() {
		return "Forest: " + isForest() + " Tree: " + isTree() + "\n" + edges.toString();
	}

	/** inner not that efficient private class to test for cycles */

	private class CycleCheck {
		private Stack<Integer> path; // current path in the graph
		private Vector<Integer>[] dstVertices; // all the destination vertices of edges outgoing for the vertex i
		private Integer[] vertices; // vertices as Integer object
		private TreeViewerTerminal wg; // the corresponding word graph
		private int nb = 0; // nb iterations

		/** initialize the class */
		@SuppressWarnings("unchecked")
		public CycleCheck(TreeViewerTerminal e) {
			int i;
			TreeViewerEdge edge;

			path = new Stack<Integer>();
			dstVertices = new Vector[e.nbVertices()];
			vertices = new Integer[e.nbVertices()];

			for (i = 0; i < vertices.length; i++) {
				vertices[i] = new Integer(i);
			}

			for (i = 0; i < dstVertices.length; i++) {
				dstVertices[i] = new Vector<Integer>(10);
			}

			for (i = 0; i < size(); i++) {
				edge = e.getEdge(i);
				dstVertices[edge.getSrc()].add(vertices[edge.getDst()]);
			}

			wg = e;
		}

		/** check recursively for a cycle completing the current path starting at vertice v */
		private boolean checkStartingFromVerticeV(int v) {
			int i;
			boolean cycle = false;

			if (path.search(vertices[v]) != -1) {
				cycle = true;
				path.push(vertices[v]);
			} else {
				if (dstVertices[v].size() > 0) {

					path.push(vertices[v]);

					i = 0;
					while (i < dstVertices[v].size() && !cycle) {
						cycle = checkStartingFromVerticeV(((Integer) dstVertices[v].get(i)).intValue());
						if (!cycle)
							i++;
					}
					if (!cycle)
						path.pop();

				}
			}
			nb++;
			return cycle;
		}

		/** check for cycles, return null if ok, an error msg otherwise */
		public String checkForCycles() {

			String msg = null;
			boolean cycle = false;

			int i = 0, current;

			i = 0;

			while (i < dstVertices.length && !cycle) {
				if (dstVertices[i].size() > 0) {
					cycle = checkStartingFromVerticeV(i);
				}
				if (!cycle)
					i++;
			}

			if (cycle) {
				msg = "Cycle detected: ";
				current = ((Integer) path.peek()).intValue();
				for (int j = path.indexOf(vertices[current]); j < path.size(); j++) {
					msg += wg.getSrcSentence().getWord(((Integer) path.get(j)).intValue());
					if (j < path.size() - 1)
						msg += "->";
				}

			}

			return msg;
		}
	}
}
