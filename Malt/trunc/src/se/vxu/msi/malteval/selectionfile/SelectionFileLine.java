package se.vxu.msi.malteval.selectionfile;

import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
class SelectionFileLine {
	private String transformationType;
	private int sentenceId;
	private int wordId;
	private String wordForm;
	private int head;
	private String deprel;
	
	private boolean isOk;

	static private TreeSet<String> deprels_ = new TreeSet<String>();
	static private TreeSet<String> liftedDeprels_;

	public SelectionFileLine(String fileLine) {
		StringTokenizer st = new StringTokenizer(fileLine, "\t");
		isOk = true;
		transformationType = st.nextToken();
		sentenceId = Integer.parseInt(st.nextToken());
		wordId = Integer.parseInt(st.nextToken());
		wordForm = st.nextToken();
		if (st.hasMoreTokens()) {
			head = Integer.parseInt(st.nextToken());
		} else {
			head = 0;
			isOk = false;
		}
		if (st.hasMoreTokens()) {
			deprel = st.nextToken();
		} else {
			deprel = "<none>";
			isOk = false;
		}

		deprels_.add(getDeprel());
	}

	static public TreeSet<String> getDeprels() {
		return deprels_;
	}

	static public TreeSet<String> getLiftedDeprels() {
		return liftedDeprels_;
	}

	public String getDeprel() {
		return deprel;
	}

	public int getDeprelIndex() {
		int i = 0;
		Iterator<String> iterator = deprels_.iterator();
		while (iterator.hasNext()) {
			if (((String) iterator.next()).equals(getDeprel())) {
				return i;
			}
			i++;
		}
		return -1;
	}

	public int getHead() {
		return head;
	}

	public int getSentenceId() {
		return sentenceId;
	}

	public String getTransformationType() {
		return transformationType;
	}

	public String getWordForm() {
		return wordForm;
	}

	public int getWordId() {
		return wordId;
	}

	public boolean isLifted() {
		return deprel.indexOf('|') != -1;
	}

	public String getLiftedDeprel() {
		if (!isLifted()) {
			return null;
		} else {
			return deprel.substring(0, deprel.indexOf('|'));
		}
	}

	public int getLiftedDeprelIndex() {
		int i = 0;
		Iterator<String> iterator = liftedDeprels_.iterator();
		while (iterator.hasNext()) {
			if (((String) iterator.next()).equals(getLiftedDeprel())) {
				return i;
			}
			i++;
		}
		return 0;
	}

	static public int getLiftedDeprelIndex(String dr) {
		int i = 0;
		Iterator<String> iterator = liftedDeprels_.iterator();
		while (iterator.hasNext()) {
			if (((String) iterator.next()).equals(dr)) {
				return i;
			}
			i++;
		}
		return -1;
	}

	public boolean isPath() {
		return deprel.indexOf('%') != -1;
	}

	public String getPathDeprel() {
		if (!isPath()) {
			return null;
		} else {
			return deprel.substring(0, deprel.lastIndexOf('%'));
		}
	}

	static public String getDeprelFromIndex(int index) {
		String deprel = null;
		int i = 0;
		Iterator<String> deps = deprels_.iterator();
		while (deps.hasNext()) {
			deprel = (String) deps.next();
			if (i >= index) {
				break;
			}
			i++;
		}
		return deprel;
	}

	static public String getLiftedDeprelFromIndex(int index) {
		String deprel = null;
		int i = 0;
		Iterator<String> deps = liftedDeprels_.iterator();
		while (deps.hasNext()) {
			deprel = (String) deps.next();
			if (i >= index) {
				break;
			}
			i++;
		}
		return deprel;
	}

	public static void setLiftedDeprels(TreeSet<String> liftedDeprels) {
		SelectionFileLine.liftedDeprels_ = liftedDeprels;
	}

	public boolean isLineOk() {
		return isOk;
	}
}
