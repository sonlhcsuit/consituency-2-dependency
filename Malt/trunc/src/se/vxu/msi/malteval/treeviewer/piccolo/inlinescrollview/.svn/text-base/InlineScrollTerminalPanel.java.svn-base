package se.vxu.msi.malteval.treeviewer.piccolo.inlinescrollview;

import se.vxu.msi.malteval.treeviewer.core.TreeViewerSentence;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

/**
 * The panel holding and managing all the words and word classes GUI objects
 */
class InlineScrollTerminalPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	/** words GUI objects */
	private InlineScrollTerminal[] words;

	/** last 2 word indexes clicked */
	private int[] clicked;
	
	
	private TreeViewerSentence editableSentence;

	/**
	 * Create the container object that will hold the words and word classes, and initialize the behavior routines
	 */

	public InlineScrollTerminalPanel() {
		setBackground(null); // inherits parent's color
		setLayout(new FlowLayout());
		((FlowLayout) getLayout()).setHgap(15);
		((FlowLayout) getLayout()).setAlignment(FlowLayout.LEFT);

		words = null;
		clicked = new int[2];
		clearSelection();

		setDoubleBuffered(true);
	}

	/** clear selected words */
	public void clearSelection() {
		clicked[0] = -1;
		clicked[1] = -1;
	}

	/**
	 * a new word was clicked, add it to the list of the word clicked.
	 * 
	 * @param i
	 *            word clicked
	 */
	public void buttonClicked(int i) {
		clicked[1] = clicked[0];
		clicked[0] = i;
	}

	/** get last (most recent) word index clicked */
	public int get1stClicked() {
		return clicked[0];
	}

	/** get second word index clicked */
	public int get2ndClicked() {
		return clicked[1];
	}

	/** get word object i 's width */
	public int getWordWidth(int i) {
		return Math.max(words[i].getSize().width, 300);
	}

	/** get word object i 's x position */
	public int getWordPosX(int i) {
		return words[i].getLocation().x;
	}

	/** update word class for word i */
	public void updateWordClass(int i, String txt) {
		words[i].updateWordClass(txt);
	}

	/**
	 * update button sizes (if necessary) so that they can hold the number of arrows. This is important if the button is too small for all the arrows.
	 * 
	 * @param s
	 *            The sentence object, used to calculate the degree of each vertice
	 */

	public void updateButtonSizes(TreeViewerSentence s) {
		 for (int i = 0; i < s.size(); i++) {
		 words[i].updateButtonSize(s.getGraph().getVertDeg(i));
		 }
	}

	/**
	 * recreate all the objects from the given sentence object. This is done typically when the user updates something or select a new sentence
	 * 
	 * @param s
	 *            the sentence containing all the info
	 */
	public void reCreate(TreeViewerSentence s, HashSet<Integer> selectedWords) {
		editableSentence = s;
		removeAll();
		if (s != null) {

			words = new InlineScrollTerminal[s.size()];
			for (int i = 0; i < s.size(); i++) {
				words[i] = new InlineScrollTerminal(i, s.getWord(i), s.getGraph().getVertDeg(i), (selectedWords != null ?selectedWords.contains(i) : false));
				add(words[i]);
			}
		}
	}

	public InlineScrollTerminal getWord(int i) {
		if (0 <= i && i < words.length) {
			return words[i];
		} else {
			return null;
		}
	}

	public TreeViewerSentence getEditableSentence() {
		return editableSentence;
	}
}
