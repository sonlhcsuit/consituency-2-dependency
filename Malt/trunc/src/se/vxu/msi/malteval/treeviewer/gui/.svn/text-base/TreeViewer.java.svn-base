package se.vxu.msi.malteval.treeviewer.gui;

import java.awt.BorderLayout;
import java.util.HashSet;
import java.util.Locale;
import java.util.concurrent.Semaphore;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import se.vxu.msi.malteval.corpus.MaltTreebank;
import se.vxu.msi.malteval.treeviewer.MaltTreeViewerGui;
import se.vxu.msi.malteval.treeviewer.core.CurrentSentenceList;
import se.vxu.msi.malteval.treeviewer.core.TreeViewerSentence;
import se.vxu.msi.malteval.treeviewer.hierarchicalzoom.HierarchicalZoomView;
import se.vxu.msi.malteval.treeviewer.piccolo.inlinescrollview.InlineScrollView;
import se.vxu.msi.malteval.treeviewer.piccolo.inlinezoom.InlineZoomView;
import se.vxu.msi.malteval.treeviewer.piccolo.netgraphlikezoom.NetGraphLikeZoomView;
import se.vxu.msi.malteval.util.DataContainer;

/**
 * This class controls all the GUI components and defines the main application behavior. this class is also the application entry point.
 */
public class TreeViewer {
	MaltTreeViewerGui maltTreeViewerGui_;

	/** view modes availables */
	private ViewMode viewMode = null;

	/** program main global data structures */
	private TreeViewerSentence currentSentence; // currrent sentence being displayed
	private MaltTreebank treebank; // treebank
	
	private JPanel nPanel;
	private CurrentSentenceList sentenceList; // list of sentences being edited

	/** GUI components */
//	private MainForm mainForm; // main display
//	private CurrentSentenceListComponent currentSentenceListComponent; // bottom list

	/**
	 * This method builds the initial display and initialize the application
	 */
	public TreeViewer(MaltTreeViewerGui maltTreeViewerGui) {
		maltTreeViewerGui_ = maltTreeViewerGui;
		Locale.setDefault(Locale.ENGLISH); // force us locale to avoid mix of languages
		sentenceList = new CurrentSentenceList();
		currentSentence = null;

		//currentSentenceListComponent = new CurrentSentenceListComponent(maltTreeViewerGui_);
		//mainForm = new MainForm(this);
		nPanel = new JPanel(new BorderLayout());
		changeViewer(1);
	}
	
	public void constructViewMode(ViewMode viewForm) {
		nPanel.removeAll();
		nPanel.add(viewForm);
		nPanel.revalidate();
		nPanel.repaint();
	}

	public void changeViewer(int viewId) {
		switch (viewId) {
		case 1:
			viewMode = new InlineZoomView(maltTreeViewerGui_);
			break;
		case 2:
			viewMode = new HierarchicalZoomView(maltTreeViewerGui_);
			break;
		case 3:
			viewMode = new NetGraphLikeZoomView(maltTreeViewerGui_);
			break;
		case 4:
			viewMode = new InlineScrollView(maltTreeViewerGui_);
			break;
		}
		constructViewMode(viewMode);
		viewMode.repaint();
	}

	/**
	 * returns the algorithms availables to edit or view sentences
	 */
	public ViewMode getViewMode() {
		return viewMode;
	}

//	/**
//	 * returns the currentSentenceList object (this object holds the sentence being edited)
//	 */
//	public CurrentSentenceList getCurrentSentenceList() {
//		return sentenceList;
//	}

	/**
	 * returns the treebank object
	 */
	public MaltTreebank getTreebank() {
		return treebank;
	}

	/**
	 * updates the current sentence being displayed
	 */
	public void setCurrentSentence(int currentSentenceIndex, HashSet<Integer> selectedWords) {
		if (currentSentenceIndex < 0 || currentSentenceIndex >= sentenceList.size()) {
			viewMode.changeSentence(null, selectedWords);
		} else {
			currentSentence = sentenceList.getSentence(currentSentenceIndex);
			viewMode.changeSentence(currentSentence, selectedWords);
		}
	}

	/**
	 * updates the horizontal scroll bar of the sentence being displayed
	 */
	public void setCurrentHorizontalBar(int value) {
		viewMode.setCurrentHorizontalBar(value);
	}

	/**
	 * updates the horizontal scroll bar of the sentence being displayed
	 */
	public void setCurrentVericalBar(int value) {
		viewMode.setCurrentVericalBar(value);
	}


	public CurrentSentenceList getSentenceList() {
		return sentenceList;
	}

	public JPanel getSplitPane() {
		return nPanel;
	}

	public void importSentences(MaltTreebank tb, DataContainer uas, DataContainer la) throws Exception {
		int i = 0, tokenCount = 0;
		treebank = tb;
		for (i = 0; i < treebank.getSentenceCount(); i++) {
			sentenceList.addSentence(new TreeViewerSentence(treebank.getSentence(i), uas, la, tokenCount));
			tokenCount += treebank.getSentence(i).getSentenceLength();
		}
	}
}
