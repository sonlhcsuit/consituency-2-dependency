/*
 * Created on 30 okt 2008
 */
package se.vxu.msi.malteval.treeviewer.hierarchicalzoom;

import java.awt.Color;
import java.awt.Font;

import se.vxu.msi.malteval.corpus.MaltWord;
import se.vxu.msi.malteval.treeviewer.MaltTreeViewerGui;
import se.vxu.msi.malteval.treeviewer.piccolo.GraphConstants;
import se.vxu.msi.malteval.treeviewer.piccolo.ZoomTerminal;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PBounds;

public class HierarchicalZoomTerminal extends ZoomTerminal {
	private static final long serialVersionUID = 2298839120234723943L;
	
	private PPath ellipse;

	public HierarchicalZoomTerminal(MaltWord word, int wordId, float x, float y, boolean searchHit) {
		super(word, x, y);
		PText wordFormText = new PText(word.getForm().equals("") ? "<ROOT>" : word.getForm());
		PText wordIdText = new PText(String.valueOf(wordId));

		wordFormText.setFont(new Font(MaltTreeViewerGui.getFontFamily(), (GraphConstants.EMPHASIZE_WORDS && searchHit ? Font.BOLD : Font.PLAIN), 2 * GraphConstants.FONT_SIZE));
		ellipse = PPath.createEllipse(x, y, (wordFormText.getWidth() > 40 ? (float) wordFormText.getWidth() + 40f : 80), (float) wordFormText.getHeight() + 10f);
		wordFormText.setX(x + (ellipse.getWidth() - wordFormText.getWidth()) / 2.0);
		wordFormText.setY(y + (ellipse.getHeight() - wordFormText.getHeight()) / 3.0);

		wordIdText.setFont(new Font(MaltTreeViewerGui.getFontFamily(), (GraphConstants.EMPHASIZE_WORDS && searchHit ? Font.BOLD : Font.PLAIN), GraphConstants.FONT_SIZE * 3 / 2));
		wordIdText.setX(x + (ellipse.getWidth() - wordIdText.getWidth()) / 2.0);
		wordIdText.setY(y + (ellipse.getHeight()));

		addChild(ellipse);
		addChild(wordFormText);
		addChild(wordIdText);
		PBounds pd = new PBounds();
		PBounds.union(wordFormText.getBounds(), wordIdText.getBounds(), pd);
		PBounds.union(pd, ellipse.getBounds(), pd);
		setBounds(pd);
	}

	public void highlight() {
		ellipse.setStrokePaint(Color.blue);
	}

	public void unhighlight() {
		ellipse.setStrokePaint(Color.black);
	}
}
