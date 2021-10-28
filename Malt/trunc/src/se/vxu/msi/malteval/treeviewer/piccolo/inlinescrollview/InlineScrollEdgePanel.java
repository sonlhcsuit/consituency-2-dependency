package se.vxu.msi.malteval.treeviewer.piccolo.inlinescrollview;

import se.vxu.msi.malteval.treeviewer.core.TreeViewerEdge;
import se.vxu.msi.malteval.treeviewer.core.TreeViewerSentence;
import se.vxu.msi.malteval.treeviewer.core.TreeViewerTerminal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * This class is the GUI container where the dependency arrows beetween 2 words are drawn in the linear representation. This class manages all the arrows between the words. The position of arrows are calculated by using the position of the word objects in the buttonPanel object provited while
 * constructing the object
 */

class InlineScrollEdgePanel extends JLabel {
	private static final long serialVersionUID = 1L;

	/** Reference to the ButtonPanel object, used to get the coordinates of the words */
	private InlineScrollTerminalPanel bPanel;

	/** array of arrows */
	private InlineScrollEdge[] arrows = null;

	/** selected arrow */
	private InlineScrollEdge selected = null;

	/** hold the degree of all vertices (nb of arrows on 1 node) */
	private int vertDegs[] = null;

	/** hold vertice (word) X coordinate */
	private int posX[] = null;

	/**
	 * create the ArrowPanel, given a reference on the ButtonPanel. (used to compute arrows position). Doesn't create the arrows, just the GUI container and initialize the behaviour.
	 * 
	 * @param b
	 *            the parent ButtonPanel (which contains the word buttons)
	 */
	public InlineScrollEdgePanel(InlineScrollTerminalPanel b) {
		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				// selectClickedArrow(e.getX(), e.getY());
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}
		});
		addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
			}
			public void mouseMoved(MouseEvent e) {
				selectClickedArrow(e.getX(), e.getY());
				if (selected.getEdge().getSrc() < selected.getEdge().getDst()) {
					setToolTipText(selected.getEdge().getLabel() + " | " + selected.getEdge().getSrc() + ":"
							+ bPanel.getWord(selected.getEdge().getSrc()).getWordForm() + " -> " + selected.getEdge().getDst() + ":"
							+ bPanel.getWord(selected.getEdge().getDst()).getWordForm());
				} else {
					setToolTipText(selected.getEdge().getLabel() + " | " + selected.getEdge().getDst() + ":"
							+ bPanel.getWord(selected.getEdge().getDst()).getWordForm() + " <- " + selected.getEdge().getSrc() + ":"
							+ bPanel.getWord(selected.getEdge().getSrc()).getWordForm());
				}
				selected = null;
			}
		});

		setOpaque(true);
		setDoubleBuffered(true);
		setBackground(null); // inherits parent's color
		bPanel = b;
	}

	/**
	 * called automatically when the user clicks on the container to detect which arrow has to be selected
	 * 
	 * @param x
	 *            x coordinate of the mouse click
	 * @param y
	 *            y coordinate of the mouse click
	 */
	private void selectClickedArrow(int x, int y) {

		int srcY = getSize().height;
		int i, d, dmin = Integer.MAX_VALUE, imin = 0;
		InlineScrollEdge oldSelection = selected;

		for (i = 0; i < arrows.length; i++) {
			d = arrows[i].calcDistance(srcY, x, y);
			if (d < dmin) {
				dmin = d;
				imin = i;
			}
		}

		selected = arrows[imin];
		if (oldSelection != selected)
			repaint();

	}

	/**
	 * return an arrow array sorted by increased x position
	 */
	@SuppressWarnings("unchecked")
	private InlineScrollEdge[] sortArrows() {
		InlineScrollEdge[] tmp = new InlineScrollEdge[arrows.length];

		for (int i = 0; i < tmp.length; i++)
			tmp[i] = arrows[i];

		// sort the arrow array so that short arrows are placed first
		Arrays.sort(tmp, new Comparator() {
			public int compare(Object o1, Object o2) {
				InlineScrollEdge a1 = (InlineScrollEdge) o1, a2 = (InlineScrollEdge) o2;

				if (a1.getSX() < a2.getSX())
					return -1;
				else if (a1.getSX() == a2.getSX())
					return 0;
				else
					return 1;
			}
		});

		return tmp;
	}

	/** select the next arrow after the selected arrow. */
	public void selectNextArrow() {

		int i = 0;

		if (arrows == null || arrows.length == 0) {
			selected = null;
			return;
		}

		InlineScrollEdge[] tmp = sortArrows();

		if (selected == null) {
			selected = tmp[0];
		} else {
			while (i < tmp.length && tmp[i] != selected)
				i++;

			i = i + 1;
			i = i % tmp.length;
			selected = tmp[i];
		}
		repaint();
	}

	/** select the previous arrow in the array */
	public void selectPreviousArrow() {
		int i;

		if (arrows == null || arrows.length == 0) {
			selected = null;
			return;
		}

		InlineScrollEdge[] tmp = sortArrows();

		if (selected == null) {
			selected = tmp[tmp.length - 1];
		} else {
			i = tmp.length - 1;
			while (i >= 0 && tmp[i] != selected)
				i--;

			i--;
			if (i < 0)
				i = tmp.length - 1;

			selected = tmp[i];
		}
		repaint();
	}

	/** clear the selected arrow (no more arrows are selected) */
	public void clearSelection() {
		selected = null;
	}

	/** return the Edge class corresponding to the selected arrow containing the source and destination word indexes */
	public TreeViewerEdge getSelectedEdge() {
		if (selected != null)
			return selected.edge;
		else
			return null;
	}

	/**
	 * recalculate the arrows objects from the given sentence info. This is done most of the time when the user updates the arrows or select a new sentence
	 * 
	 * @param s
	 *            The EditableSentence containing the info for the sentence
	 */
	@SuppressWarnings("unchecked")
	public void reCreate(TreeViewerSentence s, HashSet<Integer> selectedWords) {
		TreeViewerTerminal wg;
		Graphics g = getGraphics();

		if (s == null) { // no sentence ?
			arrows = null;
			return;
		}

		wg = s.getGraph(); // get the dependency graph from the sentence

		vertDegs = new int[s.size()]; // will hold degree of vertices
		posX = new int[s.size()]; // will hold vertice horisontal positition

		// calculate degree of vertices
		for (int i = 0; i < s.size(); i++) {
			vertDegs[i] = wg.getVertDeg(i);
		}

		arrows = new InlineScrollEdge[wg.size()]; // array to hold the arrows

		for (int i = 0; i < arrows.length; i++) { // create the arrows from the dependency graph edges
			arrows[i] = new InlineScrollEdge(s.isShowDiff(), s.getUasDiff().get(i), s.getLaDiff().get(i), (selectedWords != null ?selectedWords.contains(i + 1) : false));
			arrows[i].edge = wg.getEdge(i);
			arrows[i].calcDst();
			arrows[i].initLabelDims(g);
		}

		// sort the arrow array so that short arrows are placed first
		Arrays.sort(arrows, new Comparator() {
			public int compare(Object o1, Object o2) {
				InlineScrollEdge a1 = (InlineScrollEdge) o1, a2 = (InlineScrollEdge) o2;

				if (a1.dst < a2.dst)
					return -1;
				else if (a1.dst == a2.dst) {
					if (a1.getS() < a2.getS())
						return -1;
					else if (a1.getS() == a2.getS())
						return 0;
					else
						return 1;
				} else
					return 1;
			}
		});

		// calculate arrow overlapping height levels
		for (int i = 0; i < arrows.length; i++) {
			calcOverlapLevel(i); // set up the arrow level (height) index
		}

	}

	/**
	 * swap some X coordinates of smaller arrows arrows that over crosses stupidely
	 * 
	 * @param j
	 *            arrow index to check
	 */
	private void checkCrossing(int j) {

		InlineScrollEdge refA = arrows[j];
		int tmp;

		for (int i = 0; i < j; i++) {

			/*
			 * case ---- | -+------ | || |
			 */
			if (arrows[i].getS() == refA.getS() && arrows[i].height <= refA.height) {

				tmp = arrows[i].getSX();
				arrows[i].setSX(refA.getSX());
				refA.setSX(tmp);

			}

			/*
			 * case |------- |-+- | | || |
			 */
			if (arrows[i].getD() == refA.getS() && arrows[i].getSX() > refA.getSX()) {

				tmp = arrows[i].getDX();
				arrows[i].setDX(refA.getSX());
				refA.setSX(tmp);
			}

			/*
			 * case |------- inversed |-+- | | || |
			 */
			if (arrows[i].getS() == refA.getD() && arrows[i].getSX() < refA.getDX()) {
				tmp = arrows[i].getSX();
				arrows[i].setSX(refA.getDX());
				refA.setDX(tmp);
			}
		}
	}

	/**
	 * calculate the arrow height by looking at all the previous (smaller) arrows
	 * 
	 * @param i
	 *            arrow index to calculate the height for
	 */
	private void calcOverlapLevel(int i) {
		InlineScrollEdge refA = arrows[i];

		refA.height = InlineScrollConsts.arrowISpace;
		for (int j = 0; j < i; j++) {
			if (arrows[j].isBetweenOrCross(refA)) {
				if (refA.height <= arrows[j].height) {
					refA.height = arrows[j].height + arrows[j].getArrowHeight();
				}
			}
		}
	}

	/** calculate the vertices (word) position from the layout of the word buttons */
	private void calcWordsPos() {

		// calculate word positions
		for (int i = 0; i < posX.length; i++) {
			posX[i] = bPanel.getWordPosX(i) + ((int) (bPanel.getWordWidth(i) - vertDegs[i] * InlineScrollConsts.arrowHeadWidth)) / 2;
		}
	}

	/** update the arrow i X coordinate */
	private void calcArrowCoordinates(int i) {
		// calculate arrows x positions
		try {
			arrows[i].srcX = posX[arrows[i].edge.getSrc()] - 135;
			posX[arrows[i].edge.getSrc()] += InlineScrollConsts.arrowHeadWidth;
			arrows[i].dstX = posX[arrows[i].edge.getDst()] - 135;
			posX[arrows[i].edge.getDst()] += InlineScrollConsts.arrowHeadWidth;
		} catch (Exception e) {
			
		}
	}

	/** draw all the arrows on the given Graphics. The selected arrow is drawn differently from the others */
	public void paint(Graphics g) {

		super.paint(g);

		if (arrows != null && arrows.length > 0) {

			// calculate vertices (word) positions
			calcWordsPos();

			// calculate arrows X coordinates
			for (int i = 0; i < arrows.length; i++) {

				// calculate arrows x positions
				calcArrowCoordinates(i);

				// swap some arrows that starts/end on the same word to avoid some crossing
				checkCrossing(i);
			}

			// draw arrows
			for (int i = 0; i < arrows.length; i++) {

				if (selected == arrows[i])
					arrows[i].drawArrowSelected(getSize().height, g);
				else
					arrows[i].drawArrowNormal(getSize().height, g);
			}
		}

	}

	/** return container size, calculated by looking at the max arrow height */
	public Dimension getPreferredSize() {
		Dimension m = new Dimension();

		int h;

		m.width = 0;
		m.height = 0;

		if (arrows == null || arrows.length == 0) { // no sentence ?
			return m;
		}

		// calculate word positions
		calcWordsPos();

		// calculate arrows coordinates
		for (int i = 0; i < arrows.length; i++) {

			// calculate arrows x positions
			calcArrowCoordinates(i);

			// swap some arrows that starts/end on the same word to avoid some crossing
			checkCrossing(i);

			// calculate complete graph dimension
			if (m.width < arrows[i].srcX) {
				m.width = arrows[i].srcX + InlineScrollConsts.arrowHeadWidth;
			}
			if (m.width < arrows[i].dstX) {
				m.width = arrows[i].dstX + InlineScrollConsts.arrowHeadWidth;
			}

			h = arrows[i].height + arrows[i].getArrowHeight();

			if (m.height < h) {
				m.height = h + 5;
			}
		}
		return m;
	}
}
