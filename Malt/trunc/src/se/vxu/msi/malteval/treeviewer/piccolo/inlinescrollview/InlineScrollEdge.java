package se.vxu.msi.malteval.treeviewer.piccolo.inlinescrollview;

import se.vxu.msi.malteval.treeviewer.core.TreeViewerEdge;
import se.vxu.msi.malteval.treeviewer.piccolo.GraphConstants;

import java.awt.*;
import java.awt.geom.*;

/**
 * class representing &amp; managing one arrow drawn between 2 words on the screen using the linear representation
 */
public class InlineScrollEdge {
	/** edge class representing the src and dst word indexes */
	TreeViewerEdge edge = null;

	/** arrow length in words beetween src and dst points */
	int dst = 0;

	/** x-coordinate source postion */
	int srcX = 0;

	/** x-corrdinate destination postion */
	int dstX = 0;

	/** arrow's height in pixels */
	int height = 0;

	/** holds arrow label text dimensions */
	Rectangle2D labelDims = null;

	/** hold typical text height */
	static int charHeight = 0;

	/**
	 * is the arc different from the gold standard
	 */
	boolean showDiff_;
	boolean uasDiff_;
	boolean laDiff_;
	boolean searchHit_;

	public InlineScrollEdge(boolean showDiff, boolean uasDiff, boolean laDiff, boolean searchHit) {
		showDiff_ = showDiff;
		uasDiff_ = uasDiff;
		laDiff_ = laDiff;
		searchHit_ = searchHit;
	}

	/** return the height of horizontal edge of the arrow */
	public int getArrowHeight() {
		return Math.max(charHeight, GraphConstants.FONT_SIZE);
	}

	/**
	 * compute the dimensions in pixels of the label text and initialize the labelDims &amp; charHeight field
	 * 
	 * @param g
	 *            Graphics component used to compute the font characteristics
	 */
	public void initLabelDims(Graphics g) {
		labelDims = g.getFontMetrics().getStringBounds(edge.getLabel(), g);
		if (labelDims.getHeight() > charHeight)
			charHeight = (int) labelDims.getHeight();
	}

	public String toString() {
		return edge + "\ndst: " + dst + " srcX: " + srcX + " dstX: " + dstX + " height: " + height;
	}

	/**
	 * calculate the distance and initialize the dst field in words between 2 arrows
	 */
	public void calcDst() {
		dst = Math.abs(edge.getSrc() - edge.getDst());
	}

	/**
	 * draw this arrow in the given Graphics.
	 * 
	 * @param srcY
	 *            height of the graphics area (arrows are drawn starting from the bottom)
	 * @param g
	 *            graphics area the draw the arrow in
	 */
	public void drawArrow(int srcY, Graphics g) {
		int radius, thickness;
		float[] lineType = null;
		if (Math.abs(srcX - dstX) > 20) {
			radius = 20;
		} else {
			radius = Math.abs(srcX - dstX) / 2;
		}

		g.setColor((showDiff_ ? (uasDiff_ ? Color.red : GraphConstants.darkGreen) : Color.black));
		thickness = (searchHit_ && GraphConstants.EMPHASIZE_ARCS ? 2 : 1);
		if (showDiff_ && uasDiff_) {
			lineType = GraphConstants.dashSpaceValues;
		}
		((Graphics2D) g).setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0.0f, lineType, 0.0f));

		if (srcX > dstX) {
			g.drawArc(srcX - radius, srcY - height, radius, radius, 0, 90);
			g.drawArc(dstX, srcY - height, radius, radius, 90, 90);
			g.drawLine(srcX - radius / 2, srcY - height, dstX + radius / 2, srcY - height);
		} else {
			g.drawArc(dstX - radius, srcY - height, radius, radius, 0, 90);
			g.drawArc(srcX, srcY - height, radius, radius, 90, 90);
			g.drawLine(srcX + radius / 2, srcY - height, dstX - radius / 2, srcY - height);
		}
		g.drawLine(srcX, srcY, srcX, srcY - height + radius / 2);
		g.drawLine(dstX, srcY - height + radius / 2, dstX, srcY);

		((Graphics2D) g).setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0.0f, null, 0.0f));
		g.drawLine(dstX, srcY, dstX - InlineScrollConsts.arrowHeadDX, srcY - InlineScrollConsts.arrowHeadDY);
		g.drawLine(dstX, srcY, dstX + InlineScrollConsts.arrowHeadDX, srcY - InlineScrollConsts.arrowHeadDY);

		g.setColor((showDiff_ ? (laDiff_ ? Color.red : GraphConstants.darkGreen) : Color.black));
		if (edge.getLabel().length() > 0) {
			g.setFont(new Font("Times", Font.PLAIN, (int) (GraphConstants.FONT_SIZE * 0.85)));
			g.drawString(edge.getLabel(), getSX() + ((getDX() - getSX()) - (int) labelDims.getWidth()) / 2, srcY - height - 2);
		}
		((Graphics2D) g).setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0.0f, null, 0.0f));
	}

	/**
	 * draw this arrow as non selected
	 * 
	 * @param srcY
	 *            height of the graphics aera (arrows are drawn starting from the bottom)
	 * @param g
	 *            graphics area the draw the arrow in
	 */
	public void drawArrowNormal(int srcY, Graphics g) {
		g.setColor(Color.black);
		drawArrow(srcY, g);
	}

	/**
	 * draw this arrow as selected.
	 * 
	 * @param srcY
	 *            height of the graphics aera (arrows are drawn starting from the bottom)
	 * @param g
	 *            graphics area the draw the arrow in
	 */
	public void drawArrowSelected(int srcY, Graphics g) {
		g.setColor(Color.red);
		drawArrow(srcY, g);
	}

	/** get leftmost branch word index */
	public int getS() {
		if (edge.getSrc() < edge.getDst())
			return edge.getSrc();
		else
			return edge.getDst();
	}

	/** get rightmost branch word index */
	public int getD() {
		if (edge.getSrc() < edge.getDst())
			return edge.getDst();
		else
			return edge.getSrc();
	}

	/** get leftmost branch x postion */
	public int getSX() {
		if (edge.getSrc() < edge.getDst())
			return srcX;
		else
			return dstX;
	}

	/** get rightmost branch x position */
	public int getDX() {
		if (edge.getSrc() < edge.getDst())
			return dstX;
		else
			return srcX;
	}

	/** set the left most branch x position */
	public void setSX(int i) {
		if (edge.getSrc() < edge.getDst())
			srcX = i;
		else
			dstX = i;
	}

	/** set the right most branch x position */
	public void setDX(int i) {
		if (edge.getSrc() < edge.getDst())
			dstX = i;
		else
			srcX = i;
	}

	/**
	 * Determine if current arrow is between (or crosses) arrow e. This function is used to calculate overlapping levels. returns true if the current arrow is inside the given arrow e
	 * 
	 * @param e
	 *            arrow to test against.
	 */
	public boolean isBetweenOrCross(InlineScrollEdge e) {
		int s1 = getS(), s2 = e.getS(), d1 = getD(), d2 = e.getD();

		if ((s1 == s2 && d1 == d2) || (s2 <= s1 && d2 >= d1) || (s2 < s1 && s1 < d2 && d2 < d1) || (s2 > s1 && d2 > d1 && d1 > s2))
			return true;
		else
			return false;
	}

	/**
	 * calculate the shortest distance from the arrow to the given coordinate (x,y). this is used to calculate which arrow has to be selected when the user performs a mouse click.
	 * 
	 * @param srcY
	 *            bottom coordinate of arrow
	 */
	public int calcDistance(int srcY, int x, int y) {
		int d1 = Integer.MAX_VALUE, d2 = Integer.MAX_VALUE;

		int lx = getSX(), rx = getDX(), ty = srcY - height, by = srcY;

		if (x < lx) { // against left edge
			if (y < ty)
				d1 = Math.abs((x - lx) * (x - lx) - (y - ty) * (y - ty));
			else if (y < by)
				d1 = lx - x;
			else
				d1 = Math.abs((x - lx) * (x - lx) - (y - by) * (y - by));
		} else if (x < rx) { // against left, top, right edges
			if (y < ty)
				d1 = ty - y;
			else if (y < by) {
				d1 = y - ty;
				d2 = x - lx;
				d1 = Math.min(d1, d2);
				d2 = rx - x;
				d1 = Math.min(d1, d2);
			} else { // against right edge
				d1 = Math.abs((x - lx) * (x - lx) - (y - by) * (y - by));
				d2 = Math.abs((x - rx) * (x - rx) - (y - by) * (y - by));
				d1 = Math.min(d1, d2);
			}
		} else { // against right edge
			if (y < ty)
				d1 = Math.abs((x - rx) * (x - rx) - (y - ty) * (y - ty));
			else if (y < by)
				d1 = x - rx;
			else
				d1 = Math.abs((x - rx) * (x - rx) - (y - by) * (y - by));
		}

		return d1;
	}

	public TreeViewerEdge getEdge() {
		return edge;
	}
}
