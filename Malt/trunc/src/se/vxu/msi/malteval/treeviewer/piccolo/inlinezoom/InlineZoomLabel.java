/*
 * Created on 31 okt 2008
 */
package se.vxu.msi.malteval.treeviewer.piccolo.inlinezoom;

import java.awt.Color;
import java.awt.Font;

import se.vxu.msi.malteval.treeviewer.MaltTreeViewerGui;
import se.vxu.msi.malteval.treeviewer.piccolo.GraphConstants;
import se.vxu.msi.malteval.treeviewer.piccolo.ZoomLabel;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PBounds;

public class InlineZoomLabel extends ZoomLabel {
	private static final long serialVersionUID = 2432420430656476190L;

	private PText deprelText;
	private PPath box;
	private InlineZoomEdge edge_;

	public InlineZoomLabel(InlineZoomEdge edge, boolean showDiff, boolean laDiff, boolean searchHit) {
		super(showDiff, laDiff, searchHit);
		edge_ = edge;
		deprelText = new PText(edge.getMaltChild().getDeprel());
		box = PPath.createRectangle(0, 0, 1, 1);
		unhighlight();
		box.setStrokePaint(null);
		deprelText.setTextPaint((showDiff ? (laDiff ? Color.red : GraphConstants.darkGreen) : Color.black));
		deprelText.setFont(new Font(MaltTreeViewerGui.getFontFamily(), (GraphConstants.EMPHASIZE_ARCS && searchHit ? Font.BOLD : Font.PLAIN), GraphConstants.FONT_SIZE));
		addChild(box);
		addChild(deprelText);
		move();
	}

	public void move() {
		deprelText.setX(edge_.getBounds().getCenterX() - deprelText.getWidth() / 2.0 + InlineZoomEdge.ARROWHEAD_WIDTH * edge_.getDirection() / 2.0f);
		deprelText.setY(edge_.getBounds().getMinY() - deprelText.getHeight() - 1f);
		box.setBounds((float) (edge_.getBounds().getCenterX() - deprelText.getWidth() * 0.5f - 5f + InlineZoomEdge.ARROWHEAD_WIDTH * edge_.getDirection() / 2.0f), (float) (edge_.getBounds().getMinY() - deprelText.getHeight() - 1), (float) (deprelText.getWidth() + 10f), (float) (deprelText.getHeight()));
		PBounds pd = new PBounds();
		PBounds.union(deprelText.getBounds(), box.getBounds(), pd);
		setBounds(pd);
	}

	public String getToolTip() {
		return edge_.getToolTip();
	}

	public void highlight() {
		box.setStrokePaint(Color.blue);
		deprelText.setTextPaint(Color.blue);
		edge_.highlight();
		repaint();
	}

	public void unhighlight() {
		box.setStrokePaint(Color.white);
		deprelText.setTextPaint((showDiff_ ? (uasDiff_ ? Color.red : GraphConstants.darkGreen) : Color.black));
		edge_.unhighlight();
		repaint();
	}
}
