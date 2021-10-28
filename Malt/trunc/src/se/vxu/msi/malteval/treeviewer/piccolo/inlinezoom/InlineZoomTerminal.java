/*
 * Created on 30 okt 2008
 */
package se.vxu.msi.malteval.treeviewer.piccolo.inlinezoom;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;

import se.vxu.msi.malteval.corpus.MaltWord;
import se.vxu.msi.malteval.treeviewer.MaltTreeViewerGui;
import se.vxu.msi.malteval.treeviewer.piccolo.GraphConstants;
import se.vxu.msi.malteval.treeviewer.piccolo.ZoomTerminal;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PBounds;

public class InlineZoomTerminal extends ZoomTerminal implements Comparable<InlineZoomTerminal> {
	private static final long serialVersionUID = -1951705489743512814L;

	private PPath box;
	private boolean rightPointingChild;

	public InlineZoomTerminal(MaltWord word, int wordId, float x, float y, boolean searchHit) {
		super(word, x, y);
		rightPointingChild = false;
		setX(x);
		setY(y);
		PText text = new PText(word.getForm().equals("") ? "<ROOT>" : word.getForm());
		text.setFont(new Font(MaltTreeViewerGui.getFontFamily(), (GraphConstants.EMPHASIZE_WORDS && searchHit ? Font.BOLD : Font.PLAIN),
				2 * GraphConstants.FONT_SIZE));

		text.setX(x + 5f);
		text.setY(y + 5f);
//		box = PPath.createRectangle(x, y, (text.getWidth() > 40 ? (float) text.getWidth() + 10f : 40), (float) text.getHeight() + 10f);

		PText wordIdText = new PText(String.valueOf(wordId));
		wordIdText.setFont(new Font(MaltTreeViewerGui.getFontFamily(), (GraphConstants.EMPHASIZE_WORDS && searchHit ? Font.BOLD : 0)
				| Font.ITALIC, GraphConstants.FONT_SIZE * 3 / 2));

		wordIdText.setX(text.getX() + (text.getWidth() - wordIdText.getWidth()) / 2.0);
		wordIdText.setY(text.getY() + text.getHeight() + GraphConstants.FONT_SIZE / 2.0);

		box = PPath.createRectangle(x, y + GraphConstants.FONT_SIZE / 2.0f, (text.getWidth() > 40 ? (float) text.getWidth() + 10f : 40),
				(float) (wordIdText.getY() + wordIdText.getHeight() - text.getY()));
		box.setStrokePaint(null);

		addChild(box);
		addChild(text);
		addChild(wordIdText);
		PBounds pb = new PBounds();
		PBounds.union(text.getBounds(), wordIdText.getBounds(), pb);
		PBounds.union(pb, box.getBounds(), pb);
		setBounds(pb);
	}

	public int compareTo(InlineZoomTerminal o) {
		if (o.isRightPointingChild() == isRightPointingChild()) {
			return Double.compare(getX(), o.getX());
		} else {
			return Double.compare(getX(), o.getX()) * (isRightPointingChild() ? -1 : 1) * (o.isRightPointingChild() ? -1 : 1);
		}
	}

	public int hashCode() {
		return (getX() + "").hashCode();
	}

	public boolean equals(Object o) {
		if (o instanceof InlineZoomTerminal) {
			return ((InlineZoomTerminal) o).getX() == getX() && ((InlineZoomTerminal) o).isRightPointingChild() == isRightPointingChild();
		} else {
			return false;
		}
	}

	private boolean isRightPointingChild() {
		return rightPointingChild;
	}

	public void setRightPointingChild(boolean f) {
		rightPointingChild = f;
	}

	public void setPaint(Paint newPaint) {
		super.setPaint(newPaint);
		box.setPaint(newPaint);
	}

	public void setStrokePaint(Paint newPaint) {
		box.setStrokePaint(newPaint);
	}

	public void highlight() {
		box.setStrokePaint(Color.blue);
	}

	public void unhighlight() {
		box.setStrokePaint(null);
	}
}
