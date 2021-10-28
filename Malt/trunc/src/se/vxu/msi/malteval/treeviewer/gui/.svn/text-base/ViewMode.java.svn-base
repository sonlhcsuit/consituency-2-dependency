
package se.vxu.msi.malteval.treeviewer.gui;

import java.awt.BorderLayout;
import java.util.HashSet;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import se.vxu.msi.malteval.treeviewer.MaltTreeViewerGui;
import se.vxu.msi.malteval.treeviewer.core.TreeViewerSentence;
import edu.umd.cs.piccolo.util.PAffineTransform;
import edu.umd.cs.piccolo.util.PBounds;

public abstract class ViewMode extends JPanel {
	private static final long serialVersionUID = -2703689765905956063L;
	protected JScrollPane scrollPane;
	protected MaltTreeViewerGui maltTreeViewerGui_;

	public ViewMode(MaltTreeViewerGui maltTreeViewerGui) {
		maltTreeViewerGui_ = maltTreeViewerGui;
		setLayout(new BorderLayout());
	}

	/** informs the object that the user has selected a new sentence */
    public abstract void changeSentence(TreeViewerSentence s, HashSet<Integer> selectedWords);
    
    public abstract JPanel getJPanelToImage();
	
	public abstract void changeViewPosition(PBounds viewPosition, boolean scale);
	public abstract void changeViewPositionByTransformation(PAffineTransform transformation);

	public void setCurrentHorizontalBar(int value) {
		scrollPane.getHorizontalScrollBar().setValue(value);
	}

	public void setCurrentVericalBar(int value) {
		scrollPane.getVerticalScrollBar().setValue(value);
	}
	
	public MaltTreeViewerGui getMaltTreeViewerGui() {
		return maltTreeViewerGui_;
	}
}
