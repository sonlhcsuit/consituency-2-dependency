package se.vxu.msi.malteval.treeviewer.core;

import java.util.Vector;

import se.vxu.msi.malteval.corpus.MaltSentence;
import se.vxu.msi.malteval.corpus.MaltWord;
import se.vxu.msi.malteval.util.DataContainer;

/**
 * This class codes and manages a sentence being edited. It is created from a sentence object and encapsulate 2 sentences objects, one refering to the orginal object, and the other refering to current sentence state. This way ht eoperationcan be undoed by copying again the original sentence to the
 * current one.
 */
public class TreeViewerSentence {
	/** current object that reflects the actual state */
	private MaltSentence currentSentence;
	private Vector<Boolean> uasDiff;
	private Vector<Boolean> laDiff;
	private Vector<Boolean> isProjective;
	private boolean showDiff;

	/** the current sentence dependency graph */
	private TreeViewerTerminal graph;

	private boolean valid; // valid forest sentence
	private String error; // current error

	/**
	 * 
	 * 
	 * @param s
	 *            
	 * @param imp
	 *            true if the sentence is imported
	 * @param errMsg
	 *            the import error message (if any)
	 */

	
/**
 * Constructs a new EditableSentence, given the original sentence.
 * @param s
 * the sentence that should be edited
 * @param uas
 * the unlabeled attachment score of the file containing <code>s</code> 
 * @param la
 * the label score of the file containing <code>s</code> 
 * @param start
 * the row index of <code>uas</code> and <code>la</code> where <code>s</code> starts
 */
	public TreeViewerSentence(MaltSentence s, DataContainer uas, DataContainer la, int start) {
		uasDiff = new Vector<Boolean>();
		laDiff = new Vector<Boolean>();
		isProjective = new Vector<Boolean>();
		currentSentence = new MaltSentence(s.getSentenceLength() + 1, s.getId(), s.getParser(), s.getUser(), s.getDate(), s.getScore(), s
				.getTime());
		currentSentence.setWord(0, new MaltWord("", "", "<ROOT>", "", "", -1, "", -1, ""));
		showDiff = uas != null && la != null;
		for (int i = 0; i < s.getSentenceLength(); i++) {
			currentSentence.setWord(i + 1, s.getWord(i));
			if (uas != null) {
				uasDiff.add(uas.getEvaluationData(start + i, 0).intValue() == 0);
			} else {
				uasDiff.add(false);
			}
			if (la != null) {
				laDiff.add(la.getEvaluationData(start + i, 0).intValue() == 0);
			} else {
				laDiff.add(false);
			}
			isProjective.add(s.isProjective(i));
		}
		graph = new TreeViewerTerminal(this);
		graph.reset();
	}

	/** returns the number of words */
	public int size() {
		return currentSentence.getSentenceLength();
	}

	/** returns the sentence id */
	public int getId() {
		return currentSentence.getId();
	}

	/** returns the sentence parser info */
	public String getParser() {
		return currentSentence.getParser();
	}

	/** get the text for word i */
	public MaltWord getWord(int i) {
		return currentSentence.getWord(i);
	}

	/** returns the user */
	public String getUser() {
		return currentSentence.getUser();
	}

	/** returns the sentence as a string */
	public String getSentence() {
		return currentSentence.toString();
	}

	/** returns true if the current sentence has an error message */
	public boolean isError() {
		return error != null;
	}

	/** returns the error message */
	public String getErrorMsg() {
		return error;
	}

	/** returns true if the sentence is valid (is a forest) */
	public boolean isValid() {
		return valid;
	}

	/** set the valid state */
	public void setValid(boolean f) {
		valid = f;
	}

	/** set the error msg */
	public void setErrorMsg(String e) {
		error = e;
	}

	/** get the current sentence object */
	public MaltSentence getSentenceObject() {
		return currentSentence;
	}

	/** returns the dependency graph */
	public TreeViewerTerminal getGraph() {
		return graph;
	}

	public String toString() {
		StringBuffer f = new StringBuffer();

		f.append(currentSentence + "\ngraph\n" + graph);
		return f.toString();
	}

	public Vector<Boolean> getUasDiff() {
		return uasDiff;
	}

	public Vector<Boolean> getLaDiff() {
		return laDiff;
	}

	public Vector<Boolean> getIsProjective() {
		return isProjective;
	}

	public boolean isShowDiff() {
		return showDiff;
	}
}
