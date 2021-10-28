/*
 * Created on 12 nov 2008
 */
package se.vxu.msi.malteval.treeviewer.piccolo;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import se.vxu.msi.malteval.corpus.MaltWord;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;

public abstract class ZoomEdge extends PPath implements ToolTipObject {
	private static final long serialVersionUID = 7305809648800721251L;

	protected Rectangle2D childBounds;
	protected Rectangle2D headBounds;
	protected MaltWord maltChild_;
	protected MaltWord maltHead_;
	protected boolean searchHit_;
	protected boolean showDiff_;
	protected boolean uasDiff_;
	protected float[] lineType;
	protected PNode head_;
	protected PNode child_;

	public ZoomEdge(PNode head, PNode child, MaltWord maltHead, MaltWord maltChild, boolean showDiff, boolean uasDiff, boolean searchHit) {
		head_ = head;
		child_ = child;
		maltHead_ = maltHead;
		maltChild_ = maltChild;
		showDiff_ = showDiff;
		uasDiff_ = uasDiff;
		searchHit_ = searchHit;
		lineType = null;
		if (showDiff_ && uasDiff_) {
			lineType = GraphConstants.dashSpaceValues;
		}
		setStrokePaint((showDiff_ ? (uasDiff_ ? Color.red : GraphConstants.darkGreen) : Color.black));
	}
	
	public MaltWord getMaltChild() {
		return maltChild_;
	}
	
	public MaltWord getMaltHead() {
		return maltHead_;
	}
	
	abstract public void move();
}
