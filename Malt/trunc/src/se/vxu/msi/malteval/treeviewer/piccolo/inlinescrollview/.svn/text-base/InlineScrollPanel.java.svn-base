package se.vxu.msi.malteval.treeviewer.piccolo.inlinescrollview;


import se.vxu.msi.malteval.treeviewer.core.TreeViewerEdge;
import se.vxu.msi.malteval.treeviewer.core.TreeViewerSentence;
import se.vxu.msi.malteval.treeviewer.gui.TxtviewForm;
import se.vxu.msi.malteval.treeviewer.piccolo.inlinescrollview.InlineScrollEdgePanel;
import se.vxu.msi.malteval.treeviewer.piccolo.inlinescrollview.InlineScrollTerminalPanel;

import javax.swing.*;
import java.awt.*;

import java.io.*;
import java.util.HashSet;

/**
 * the GUI container class composed of an arrow panel and a button panel for the editMode1. Manage everything by delegating most tasks to the inner components.
 */

public class InlineScrollPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/** the inner arrow panel where arrows are drawn and managed */
	private InlineScrollEdgePanel arrowPanel;

	/** the inner button panel where words are drawn and managed */
	private InlineScrollTerminalPanel buttonPanel;

	/** construct &amp; initialize all the elements empty at the beginning */
	public InlineScrollPanel() {
		Box box;

		setLayout(new BorderLayout());
		setBackground(null); // inherits parent's color

		box = new Box(BoxLayout.Y_AXIS);
		box.setBackground(null);

		JPanel panel = new JPanel(new BorderLayout());
		JPanel panel2 = new JPanel();

		panel.setBackground(null);
		panel2.setBackground(null);

		buttonPanel = new InlineScrollTerminalPanel();
		arrowPanel = new InlineScrollEdgePanel(buttonPanel);

		panel.add(arrowPanel, BorderLayout.NORTH);
		panel.add(buttonPanel, BorderLayout.SOUTH);

		panel2.add(panel);

		box.add(Box.createGlue());

		box.add(panel2);

		box.add(Box.createGlue());

		add(box);

		setDoubleBuffered(true);
	}

	/** clear selected items (selected arrow and word previously clicked) */
	public void clearSelection() {
		arrowPanel.clearSelection();
		buttonPanel.clearSelection();
	}

	/** select the previous arrow before the selected arrow */
	public void selectPreviousArrow() {
		arrowPanel.selectPreviousArrow();
	}

	/** select next arrow before the selected arrow */
	public void selectNextArrow() {
		arrowPanel.selectNextArrow();
	}

	/** get Edge object containing the src and dst word indexes corresponding to the selected arrow */
	public TreeViewerEdge getSelectedEdge() {
		return arrowPanel.getSelectedEdge();
	}

	/** get 1st (most recently) word clicked. -1 if no word clicked */
	public int get1stClicked() {
		return buttonPanel.get1stClicked();
	}

	/** get 2nd word clicked. -1 if not word clicked */
	public int get2ndClicked() {
		return buttonPanel.get2ndClicked();
	}

	/** update word class text for word i */
	public void updateWordClass(int i, String txt) {
		buttonPanel.updateWordClass(i, txt);
	}

	/** recreate and redraw all the components so that they reflect the given sentence */
	public void reCreate(TreeViewerSentence s, HashSet<Integer> selectedWords) {
		try {
			buttonPanel.reCreate(s, selectedWords);
			arrowPanel.reCreate(s, selectedWords);

			((JComponent) getParent()).revalidate();
			arrowPanel.repaint();
		} catch (Exception e) {
			StringWriter ss = new StringWriter();
			e.printStackTrace(new PrintWriter(ss));
			new TxtviewForm(ss.toString());
		}
	}

	/** update and redraw only the arrows */
	public void updateArrows(TreeViewerSentence s, HashSet<Integer> selectedWords) {
		buttonPanel.updateButtonSizes(s);
		arrowPanel.reCreate(s, selectedWords);
		((JComponent) getParent()).revalidate();
		arrowPanel.repaint();
	}
}
