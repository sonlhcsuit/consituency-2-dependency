/*
 * Created on 30 okt 2008
 */
package se.vxu.msi.malteval.treeviewer.piccolo.inlinezoom;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;

import se.vxu.msi.malteval.corpus.MaltWord;
import se.vxu.msi.malteval.treeviewer.piccolo.GraphConstants;
import se.vxu.msi.malteval.treeviewer.piccolo.ZoomEdge;
import edu.umd.cs.piccolo.PNode;

public class InlineZoomEdge extends ZoomEdge {
	private static final long serialVersionUID = -5899997142080457189L;
	
	private InlineZoomLabel label;
	private String toolTip;

	public static final float HEIGHT_FACTOR = 30.0f;
	public static final float ARROWHEAD_WIDTH = 5.0f;
	public static final float ARROWHEAD_HEIGHT = 15.0f;
	public static final float SIBLING_DIST = 5.0f;
	public static final float CORNER_WIDTH = 10.0f;

	private int height_;
	private int direction_;

	public InlineZoomEdge(PNode head, PNode child, MaltWord maltHead, MaltWord maltChild, int height, boolean showDiff, boolean uasDiff, boolean laDiff, boolean searchHit) {
		super(head, child, maltHead, maltChild, showDiff, uasDiff, searchHit);
		height_ = height;
		label = new InlineZoomLabel(this, showDiff, laDiff, searchHit);
		String headForm = (maltHead_.getHead() == -1 ? "<ROOT>" : "'" + maltHead_.getForm() + "'");
		if (head_.getX() < child_.getX()) {
			toolTip = maltChild_.getDeprel() + " | " + headForm + " => '" + maltChild_.getForm() + "'";
		} else {
			toolTip = maltChild_.getDeprel() + " | '" + maltChild_.getForm() + "' <= " + headForm;
		}
		move();
	}

	@SuppressWarnings("unchecked")
	public void move() {
		int siblingNumber, siblingCount, side;

		if (((ArrayList<PNode>) head_.getAttribute("lc")).indexOf(child_) != -1) {
			siblingCount = ((ArrayList<PNode>) head_.getAttribute("lc")).size() + 1;
			siblingNumber = ((ArrayList<PNode>) head_.getAttribute("lc")).indexOf(child_) + 1;
			side = 1;
		} else {
			siblingCount = ((ArrayList<PNode>) head_.getAttribute("rc")).size() + 1;
			siblingNumber = ((ArrayList<PNode>) head_.getAttribute("rc")).size()
					- ((ArrayList<PNode>) head_.getAttribute("rc")).indexOf(child_);
			side = -1;
		}
		if (child_.getX() < head_.getX()) {
			direction_ = 1;
		} else {
			direction_ = -1;
		}
		childBounds = child_.getFullBounds().getBounds2D();
		headBounds = head_.getFullBounds().getBounds2D();

		float cornerOffset = CORNER_WIDTH * direction_;
		float headX = (float) (headBounds.getCenterX() - SIBLING_DIST * side - (side * (head_.getWidth() / 2.0f - SIBLING_DIST)) * siblingNumber / siblingCount);
		float childX = (float) childBounds.getCenterX();
		float headY = (float) -(headBounds.getY() + height_ * HEIGHT_FACTOR);
		float childY = (float) -(childBounds.getY() + height_ * HEIGHT_FACTOR);
		//setBounds(Math.min(headX, childX), childY, Math.abs(headX - childX), -childY);
		
		setStroke(new BasicStroke((searchHit_ && GraphConstants.EMPHASIZE_ARCS ? 2 : 1), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0.0f, lineType, 0.0f));
		
		//arrow start
		moveTo(headX, (float) -headBounds.getMinY());
		lineTo(headX, headY + CORNER_WIDTH);

		//arrow top
		curveTo(headX, headY + CORNER_WIDTH, headX, headY, headX - cornerOffset, headY);
		lineTo(childX + cornerOffset, headY);
		curveTo(childX + cornerOffset, headY, childX, childY, childX, childY + CORNER_WIDTH);

		lineTo((float) childBounds.getCenterX(), (float) -headBounds.getY());

		//arrow head
		setStroke(new BasicStroke((searchHit_ && GraphConstants.EMPHASIZE_ARCS ? 2 : 1), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0.0f, null, 0.0f));
		lineTo(childX - ARROWHEAD_WIDTH, (float) -(headBounds.getY() + ARROWHEAD_HEIGHT));
		moveTo(childX, (float) -headBounds.getY());
		lineTo(childX + ARROWHEAD_WIDTH, (float) -(headBounds.getY() + ARROWHEAD_HEIGHT));

//		getLabel().reset();
		getLabel().move();
	}

	public int getDirection() {
		return direction_;
	}

	public InlineZoomLabel getLabel() {
		return label;
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
