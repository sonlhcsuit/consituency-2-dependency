/*
 * Created on 30 okt 2008
 */
package se.vxu.msi.malteval.treeviewer.piccolo.netgraphlikezoom;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

import se.vxu.msi.malteval.corpus.MaltWord;
import se.vxu.msi.malteval.treeviewer.piccolo.GraphConstants;
import se.vxu.msi.malteval.treeviewer.piccolo.ZoomEdge;
import edu.umd.cs.piccolo.PNode;

public class NetGraphLikeZoomEdge extends ZoomEdge {
	private static final long serialVersionUID = 3994100353153657953L;

	private String toolTip;

	public NetGraphLikeZoomEdge(PNode head, PNode child, MaltWord maltHead, MaltWord maltChild, boolean showDiff, boolean uasDiff, boolean searchHit) {
		super(head, child, maltHead, maltChild, showDiff, uasDiff, searchHit);
		setStroke(new BasicStroke((searchHit_ && GraphConstants.EMPHASIZE_ARCS ? 2 : 1), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0.0f, lineType, 0.0f));
		String headForm = (maltHead_.getHead() == -1 ? "<ROOT>" : "'" + maltHead_.getForm() + "'");
		if (head_.getX() < child_.getX()) {
			toolTip = maltChild_.getDeprel() + " | " + headForm + " => '" + maltChild_.getForm() + "'";
		} else {
			toolTip = maltChild_.getDeprel() + " | '" + maltChild_.getForm() + "' <= " + headForm;
		}
		move();
	}
	
	public void move() {
		childBounds = child_.getFullBounds().getBounds2D();
		headBounds = head_.getFullBounds().getBounds2D();
		
		moveTo((float) headBounds.getCenterX(), (float) headBounds.getCenterY());
		lineTo((float) childBounds.getCenterX(), (float) childBounds.getY());
	}
	
	public Rectangle2D getChildBounds() {
		return childBounds;
	}

	public String getToolTip() {
		return toolTip;
	}

	public void highlight() {
		setStrokePaint(Color.blue);
		repaint();
	}

	public void unhighlight() {
		setStrokePaint((showDiff_ ? (uasDiff_ ? Color.red : GraphConstants.darkGreen) : Color.black));
		repaint();
	}
}
