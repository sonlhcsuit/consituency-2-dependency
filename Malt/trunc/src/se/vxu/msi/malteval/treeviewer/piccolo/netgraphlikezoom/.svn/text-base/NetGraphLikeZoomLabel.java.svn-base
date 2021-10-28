/*
 * Created on 31 okt 2008
 */
package se.vxu.msi.malteval.treeviewer.piccolo.netgraphlikezoom;

import java.awt.Color;
import java.awt.Font;

import se.vxu.msi.malteval.treeviewer.MaltTreeViewerGui;
import se.vxu.msi.malteval.treeviewer.piccolo.GraphConstants;
import se.vxu.msi.malteval.treeviewer.piccolo.ZoomLabel;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class NetGraphLikeZoomLabel extends ZoomLabel {
	private static final long serialVersionUID = 946390648851318482L;

	private NetGraphLikeZoomTerminal terminal_;
	private PText deprelText;
	private PPath box;

	public NetGraphLikeZoomLabel(NetGraphLikeZoomTerminal terminal, boolean showDiff, boolean laDiff, boolean searchHit) {
		super(showDiff, laDiff, searchHit);
		setChildrenPickable(false);
		terminal_ = terminal;
		deprelText = new PText(terminal.getMaltWord().getForm() + "\n" + terminal.getMaltWord().getDeprel());
		box = PPath.createRectangle(0, 0, 1, 1);
		unhighlight();
//		box.setPaint(Color.white);
//		box.setStrokePaint(null);
		deprelText.setTextPaint((showDiff ? (laDiff ? Color.red : GraphConstants.darkGreen) : Color.black));
		deprelText.setFont(new Font(MaltTreeViewerGui.getFontFamily(), (GraphConstants.EMPHASIZE_ARCS && searchHit ? Font.BOLD : Font.PLAIN), GraphConstants.FONT_SIZE));
		addChild(box);
		addChild(deprelText);
		move();
	}
	
	public void move() {
		deprelText.setX(terminal_.getBounds().getCenterX());
		deprelText.setY(terminal_.getBounds().getCenterY() + GraphConstants.FONT_SIZE);
		box.setBounds(deprelText.getBounds());
		setX(box.getX());
		setY(box.getY());
		setBounds(box.getBounds());
	}
	
	public String getToolTip() {
		return terminal_.getToolTip();
	}

	public void highlight() {
		box.setStrokePaint(Color.blue);
		deprelText.setTextPaint(Color.blue);
		repaint();
	}

	public void unhighlight() {
		box.setStrokePaint(Color.white);
		deprelText.setTextPaint((showDiff_ ? (uasDiff_ ? Color.red : GraphConstants.darkGreen) : Color.black));
		repaint();
	}
}
