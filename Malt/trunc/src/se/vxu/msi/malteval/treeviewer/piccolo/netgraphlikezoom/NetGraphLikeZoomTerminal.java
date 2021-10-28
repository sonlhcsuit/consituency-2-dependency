/*
 * Created on 30 okt 2008
 */
package se.vxu.msi.malteval.treeviewer.piccolo.netgraphlikezoom;

import java.awt.Color;

import se.vxu.msi.malteval.corpus.MaltWord;
import se.vxu.msi.malteval.treeviewer.piccolo.GraphConstants;
import se.vxu.msi.malteval.treeviewer.piccolo.ZoomTerminal;
import edu.umd.cs.piccolo.nodes.PPath;

public class NetGraphLikeZoomTerminal extends ZoomTerminal {
	private static final long serialVersionUID = 2298839120234723943L;
	
	private NetGraphLikeZoomLabel label;
	private PPath ellipse;

	public NetGraphLikeZoomTerminal(MaltWord word, int wordId, float x, float y, boolean showDiff, boolean laDiff, boolean searchHit) {
		super(word, x, y);
		ellipse = PPath.createEllipse(x, y, GraphConstants.FONT_SIZE / 2.0f, GraphConstants.FONT_SIZE / 2.0f);
		ellipse.setPaint(Color.white);
		addChild(ellipse);
		setBounds(ellipse.getBounds());
		label = new NetGraphLikeZoomLabel(this, showDiff, laDiff, searchHit);
	}

	public NetGraphLikeZoomLabel getLabel() {
		return label;
	}
	
	public void highlight() {
		ellipse.setPaint(Color.blue);
	}

	public void unhighlight() {
		ellipse.setPaint(Color.white);
	}
}
