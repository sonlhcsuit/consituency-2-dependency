/*
 * Created on 30 okt 2008
 */
package se.vxu.msi.malteval.treeviewer.hierarchicalzoom;

import java.awt.BasicStroke;
import java.awt.Color;

import se.vxu.msi.malteval.corpus.MaltWord;
import se.vxu.msi.malteval.treeviewer.piccolo.GraphConstants;
import se.vxu.msi.malteval.treeviewer.piccolo.ZoomEdge;
import edu.umd.cs.piccolo.PNode;

public class HierarchicalZoomEdge extends ZoomEdge {
	private static final long serialVersionUID = 3994100353153657953L;
	private String toolTip;
	private HierarchicalZoomLabel label;

	public HierarchicalZoomEdge(PNode head, PNode child, MaltWord maltHead, MaltWord maltChild, boolean showDiff, boolean uasDiff,
			boolean laDiff, boolean searchHit) {
		super(head, child, maltHead, maltChild, showDiff, uasDiff, searchHit);
		float[] lineType = null;
		if (showDiff_ && uasDiff_) {
			lineType = GraphConstants.dashSpaceValues;
		}
		setStroke(new BasicStroke((searchHit_ && GraphConstants.EMPHASIZE_ARCS ? 2 : 1), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
				0.0f, lineType, 0.0f));
		setStrokePaint((showDiff_ ? (uasDiff_ ? Color.red : GraphConstants.darkGreen) : Color.black));
		String headForm = (maltHead_.getHead() == -1 ? "<ROOT>" : "'" + maltHead_.getForm() + "'");
		if (head_.getX() < child_.getX()) {
			toolTip = maltChild_.getDeprel() + " | " + headForm + " => '" + maltChild_.getForm() + "'";
		} else {
			toolTip = maltChild_.getDeprel() + " | '" + maltChild_.getForm() + "' <= " + headForm;
		}
		label = new HierarchicalZoomLabel(this, showDiff, laDiff, searchHit);

		move();
		label.move();
	}

	public void move() {
		childBounds = child_.getFullBounds().getBounds2D();
		headBounds = head_.getFullBounds().getBounds2D();

		moveTo((float) headBounds.getCenterX(), (float) headBounds.getCenterY());
		lineTo((float) childBounds.getCenterX(), (float) childBounds.getY());
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

	public HierarchicalZoomLabel getLabel() {
		return label;
	}
}
