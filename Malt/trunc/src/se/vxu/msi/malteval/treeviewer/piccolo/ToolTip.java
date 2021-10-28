/*
 * Created on 6 nov 2008
 */
package se.vxu.msi.malteval.treeviewer.piccolo;

import java.awt.Color;
import java.awt.Font;

import se.vxu.msi.malteval.treeviewer.MaltTreeViewerGui;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class ToolTip extends PPath {
	private static final long serialVersionUID = -5671598734361495206L;

	private PText text;
	private PPath box;
	
	public ToolTip() {
		setPickable(false);
		box = PPath.createRectangle(0, 0, 1, 1);
		box.setPaint(new Color(184, 207, 229));
		box.setStrokePaint(new Color(99, 130, 191));
		text = new PText();
		text.setTextPaint(new Color(51, 51, 51));
		text.setFont(new Font(MaltTreeViewerGui.getFontFamily(), Font.PLAIN, GraphConstants.FONT_SIZE));
		
		addChild(box);
		addChild(text);
	}
	
	public void setText(String newText) {
		text.setText(newText);
		box.setBounds(text.getBounds());
	}
	
	public void move(double x, double y) {
		setVisible(true);
		setOffset(x, y);
	}
}
