package se.vxu.msi.malteval.treeviewer.core;

import java.util.Collection;
import java.util.Vector;

/**
 * This class holds and manages all the sentences being currently edited. It is similar to the class Treebank, except that its contains sentences of type EditableSentence. "The term current sentence list" refers to the list being displayed at the bottom of the screen
 */
public class CurrentSentenceList {
	/** the sentences in the current list */
	protected Vector<TreeViewerSentence> sentences;

	/** treebank id (set after an import) */
	protected String id = null;

	/** create an empty current sentence list */
	public CurrentSentenceList() {
		sentences = new Vector<TreeViewerSentence>();
	}

	/** reset (empty) the object (clear everything) */
	public void reset() {
		sentences.removeAllElements();
//		errors = null;
		id = null;
	}

	/** returns the number of elements in this object */
	public int size() {
		return sentences.size();
	}

	/** get the treebank id generated when importing the sentences */
	public String getId() {
		if (id == null)
			return "";
		else
			return id;
	}

	/** set the treebank id */
	public void setId(String i) {
		if (i != null)
			id = i.trim();
		else
			id = null;
	}

	/** returns the sentence num i */
	public TreeViewerSentence getSentence(int i) {
		return (TreeViewerSentence) sentences.get(i);
	}

	/** add a sentence to the sentence list */
	public void addSentence(TreeViewerSentence s) {
		sentences.add(s);
	}

	/** deletes the sentence at index i */
	public void deleteSentence(int i) {
		sentences.remove(i);
	}

	/** returns true is all the sentences are valid */
	public boolean areSentencesValid() {
		boolean res = true;
		int i = 0;

		while (i < size() && res) {
			res = getSentence(i).isValid();
			i++;
		}

		return res;
	}

	public String toString() {
		StringBuffer f = new StringBuffer();

		for (int i = 0; i < sentences.size(); i++) {
			f.append(sentences.get(i));
			if (i < sentences.size() - 1)
				f.append("\n");
		}

		return f.toString();
	}

	/** get access to the inner Collection object used to store the sentences. Not used, but provided if needed */
	public Collection<TreeViewerSentence> getCollectionObject() {
		return sentences;
	}
}
