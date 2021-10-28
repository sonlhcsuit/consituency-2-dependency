package se.vxu.msi.malteval.treeviewer.piccolo.inlinescrollview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.HashSet;

import javax.swing.JScrollPane;

import edu.umd.cs.piccolo.util.PAffineTransform;
import edu.umd.cs.piccolo.util.PBounds;

import se.vxu.msi.malteval.treeviewer.MaltTreeViewerGui;
import se.vxu.msi.malteval.treeviewer.core.TreeViewerSentence;
import se.vxu.msi.malteval.treeviewer.gui.ViewMode;

public class InlineScrollView extends ViewMode {
	private static final long serialVersionUID = 1L;

	/** the editing section is composed of those 2 elements */
	private InlineScrollPanel editPanel;

	/**
	 * Construct an object to edit visually a sentence using a linear representation. Empty by default until a sentence is selected
	 * 
	 * @param maltTreeViewerGui
	 *            a reference to the main mediator, used when this object needs to communicate with other external objects (like the sentence list)
	 */
	public InlineScrollView(MaltTreeViewerGui maltTreeViewerGui) {
		super(maltTreeViewerGui);
		scrollPane = new JScrollPane();
		scrollPane.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				maltTreeViewerGui_.changeHorizontalPosition(e.getValue());
			}
		});
		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				maltTreeViewerGui_.changeVerticalPosition(e.getValue());
			}
		});
		scrollPane.getViewport().setBackground(Color.white); // set background white color
		add(scrollPane, BorderLayout.CENTER);
		editPanel = new InlineScrollPanel();
		scrollPane.setViewportView(editPanel);
	}

	/** recalculate the display elements for this new sentence. Used when the user selects a new sentence */
	public void changeSentence(TreeViewerSentence s, HashSet<Integer> selectedWords) {
		editPanel.reCreate(s, selectedWords);
	}

	public void setHorizontalScollPane(int value) {
	}

	public void changeViewPosition(PBounds viewPosition, boolean scale) {
	}

	public void changeViewPositionByTransformation(PAffineTransform transformation) {
	}

	public InlineScrollPanel getJPanelToImage() {
		return editPanel;
	}
}
