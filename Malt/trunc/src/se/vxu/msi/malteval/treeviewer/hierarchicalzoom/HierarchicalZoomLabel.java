/*
 * Created on 31 okt 2008
 */
package se.vxu.msi.malteval.treeviewer.hierarchicalzoom;

import java.awt.Color;
import java.awt.Font;

import se.vxu.msi.malteval.treeviewer.MaltTreeViewerGui;
import se.vxu.msi.malteval.treeviewer.piccolo.GraphConstants;
import se.vxu.msi.malteval.treeviewer.piccolo.ZoomLabel;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PBounds;

public class HierarchicalZoomLabel extends ZoomLabel {
	private static final long serialVersionUID = 946390648851318482L;

	private PText deprelText;
	private PPath box;
	private HierarchicalZoomEdge edge_;
	private String toolTip;

	public HierarchicalZoomLabel(HierarchicalZoomEdge edge, boolean showDiff, boolean laDiff, boolean searchHit) {
		super(showDiff, laDiff, searchHit);
		edge_ = edge;
		deprelText = new PText(edge.getMaltChild().getDeprel());
		box = PPath.createRectangle(0, 0, 1, 1);
		unhighlight();
//		box.setPaint(Color.white);
//		box.setStrokePaint(Color.gray);
//		deprelText.setTextPaint((showDiff ? (laDiff ? Color.red : GraphConstants.darkGreen) : Color.black));
		deprelText.setFont(new Font(MaltTreeViewerGui.getFontFamily(), (GraphConstants.EMPHASIZE_ARCS && searchHit ? Font.BOLD : Font.PLAIN), GraphConstants.FONT_SIZE));
		addChild(box);
		addChild(deprelText);
		toolTip = edge.getToolTip();
	}
	
	public void move() {
		deprelText.setX(edge_.getBounds().getCenterX() - deprelText.getWidth() / 2.0);
		deprelText.setY(edge_.getBounds().getCenterY() - deprelText.getHeight() / 2.0);
		box.setBounds((float) (edge_.getBounds().getCenterX() - deprelText.getWidth()/2.0 - 5), (float) (edge_.getBounds().getCenterY() - deprelText.getHeight() / 2.0), (float) (deprelText.getWidth() + 10), (float) (deprelText.getHeight()));
		PBounds pd = new PBounds();
		PBounds.union(deprelText.getBounds(), box.getBounds(), pd);
		setBounds(pd);
	}

	public String getToolTip() {
		return toolTip;
	}

	public void highlight() {
		box.setStrokePaint(Color.blue);
		deprelText.setTextPaint(Color.blue);
		edge_.highlight();
		repaint();
	}

	public void unhighlight() {
		box.setStrokePaint((showDiff_ ? (uasDiff_ ? Color.red : GraphConstants.darkGreen) : Color.gray));
		deprelText.setTextPaint((showDiff_ ? (uasDiff_ ? Color.red : GraphConstants.darkGreen) : Color.black));
		edge_.unhighlight();
		repaint();
	}
}
