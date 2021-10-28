package se.vxu.msi.malteval.treeviewer.piccolo.inlinezoom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.SwingUtilities;

import se.vxu.msi.malteval.treeviewer.MaltTreeViewerGui;
import se.vxu.msi.malteval.treeviewer.core.TreeViewerSentence;
import se.vxu.msi.malteval.treeviewer.piccolo.NodeDragHandler;
import se.vxu.msi.malteval.treeviewer.piccolo.ZoomView;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PBounds;

public class InlineZoomView extends ZoomView {
	private static final long serialVersionUID = -8935407974008525425L;

	public InlineZoomView(MaltTreeViewerGui maltTreeViewerGui) {
		super(maltTreeViewerGui);
		NodeDragHandler nodeDragHandler = new NodeDragHandler(this);
		nodeLayer.addInputEventListener(nodeDragHandler);
		edgeLayer.addInputEventListener(nodeDragHandler);
		labelLayer.addInputEventListener(nodeDragHandler);
		aCanvas.getCamera().addChild(nodeDragHandler.getTooltip());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void changeSentence(TreeViewerSentence s, HashSet<Integer> selectedWords) {
		float maxXValue = 0.0f;
		float minYValue = 0.0f;
		float maxYValue = 0.0f;

		resetItems();

		maxXValue = 0.0f;
		for (int i = 0; i < s.size(); i++) {
			InlineZoomTerminal terminal = new InlineZoomTerminal(s.getWord(i), i, maxXValue, 0.0f, (selectedWords != null ?selectedWords.contains(i) : false));
			maxXValue += terminal.getWidth() + 20;
			//terminal.addAttribute("edges", new ArrayList<PNode>());
			addItem(terminal);
			if (terminal.getFullBounds().getMinY() < minYValue) {
				minYValue = (float) terminal.getFullBounds().getMinY();
			}
			if (terminal.getFullBounds().getMaxY() > maxYValue) {
				maxYValue = (float) terminal.getFullBounds().getMaxY();
			}

			terminal.addAttribute("lc", new TreeSet<InlineZoomTerminal>());
			terminal.addAttribute("rc", new TreeSet<InlineZoomTerminal>());
		}
		int[] heights = determineHeight(s);
		for (int i = 1; i < s.size(); i++) {
			InlineZoomTerminal child = (InlineZoomTerminal) nodeLayer.getChild(i);
			InlineZoomTerminal head = (InlineZoomTerminal) nodeLayer.getChild(s.getWord(i).getHead());
			if (i < s.getWord(i).getHead()) {
				if (i < s.getWord(s.getWord(i).getHead()).getHead() && s.getWord(s.getWord(i).getHead()).getHead() < s.getWord(i).getHead()) {
					child.setRightPointingChild(true);
					((TreeSet<InlineZoomTerminal>) head.getAttribute("rc")).add(child);
				} else {
					((TreeSet<InlineZoomTerminal>) head.getAttribute("lc")).add(child);
				}
			} else {
				if (s.getWord(i).getHead() < s.getWord(s.getWord(i).getHead()).getHead() && s.getWord(s.getWord(i).getHead()).getHead() < i) {
					child.setRightPointingChild(true);
					((TreeSet<InlineZoomTerminal>) head.getAttribute("lc")).add(child);
				} else {
					((TreeSet<InlineZoomTerminal>) head.getAttribute("rc")).add(child);
				}
			}
		}
		for (int i = 0; i < s.size(); i++) {
			PNode node = nodeLayer.getChild(i);
			node.addAttribute("rc", new ArrayList(((TreeSet) node.getAttribute("rc"))));
			node.addAttribute("lc", new ArrayList(((TreeSet) node.getAttribute("lc"))));
		}

		for (int i = 1; i < s.size(); i++) {
			PNode child = nodeLayer.getChild(i);
			PNode head = nodeLayer.getChild(s.getWord(i).getHead());

			InlineZoomEdge edge = new InlineZoomEdge(head, child, s.getWord(s.getWord(i).getHead()), s.getWord(i), heights[i], s.isShowDiff(), s.getUasDiff().get(i - 1), s.getLaDiff().get(i - 1), (selectedWords != null ?selectedWords.contains(i) : false));
			if (edge.getFullBounds().getMinY() < minYValue) {
				minYValue = (float) edge.getFullBounds().getMinY();
			}
			if (edge.getFullBounds().getMaxY() > maxYValue) {
				maxYValue = (float) edge.getFullBounds().getMaxY();
			}

			addItem(edge);
			addItem(edge.getLabel());

			if (edge.getLabel().getFullBounds().getMinY() < minYValue) {
				minYValue = (float) edge.getLabel().getFullBounds().getMinY();
			}
			if (edge.getLabel().getFullBounds().getMaxY() > maxYValue) {
				maxYValue = (float) edge.getLabel().getFullBounds().getMaxY();
			}
		}
		viewBound = new PBounds(0, minYValue, maxXValue, maxYValue - minYValue);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				aCanvas.getCamera().setViewBounds(viewBound);
			}
		});
	}
	
	private int[] determineHeight(TreeViewerSentence s) {
		TreeMap<Integer, ArrayList<Integer>> arcLengths = new TreeMap<Integer, ArrayList<Integer>>();
		int[] maxHeights = new int[s.size()], heights = new int[s.size()];
		int i, left, right, maxHeight;
		Integer key;
		ArrayList<Integer> arcLength;
		
		for (i = 1; i < s.size(); i++) {
			key = Math.abs(s.getWord(i).getHead() - i);
			if (!arcLengths.containsKey(key)) {
				arcLengths.put(key, new ArrayList<Integer>());
			}
			arcLengths.get(key).add(i);
		}
		for (Integer length : arcLengths.keySet()) {
			arcLength = arcLengths.get(length);
			for (Integer wordId : arcLength) {
				if (wordId < s.getWord(wordId).getHead()) {
					left = wordId;
					right = s.getWord(wordId).getHead();
				} else {
					left = s.getWord(wordId).getHead();
					right = wordId;
				}
				maxHeight = 0;
				for (i = left; i < right; i++) {
					if (maxHeights[i] > maxHeight) {
						maxHeight = maxHeights[i];
					}
				}
				for (i = left; i < right; i++) {
					maxHeights[i] = maxHeight + 1;
				}
				heights[wordId] = maxHeight + 1;;
			}
		}
		return heights;
	}
}
