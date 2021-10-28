/*
 * Created on 6 nov 2008
 */
package se.vxu.msi.malteval.treeviewer.piccolo;

import edu.umd.cs.piccolo.PNode;
import se.vxu.msi.malteval.corpus.MaltWord;

abstract public class ZoomTerminal extends PNode implements ToolTipObject {
	private static final long serialVersionUID = -8464821123041878945L;

	private MaltWord word_;

	public ZoomTerminal(MaltWord word, float x, float y) {
		word_ = word;
		setChildrenPickable(false);
		setX(x);
		setY(y);
	}

	public String getToolTip() {
		return "CPOS: " + word_.getCPostag() + "\nPOS: " + word_.getPostag() + "\nFEATS: " + word_.getFeats();
	}

	public MaltWord getMaltWord() {
		return word_;
	}
}
