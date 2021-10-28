package se.vxu.msi.malteval.treeviewer.piccolo.netgraphlikezoom;

import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.SwingUtilities;

import se.vxu.msi.malteval.corpus.MaltSentence;
import se.vxu.msi.malteval.corpus.MaltWord;
import se.vxu.msi.malteval.treeviewer.MaltTreeViewerGui;
import se.vxu.msi.malteval.treeviewer.core.TreeViewerSentence;
import se.vxu.msi.malteval.treeviewer.piccolo.NodeDragHandler;
import se.vxu.msi.malteval.treeviewer.piccolo.ZoomView;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PBounds;

public class NetGraphLikeZoomView extends ZoomView {
	private static final long serialVersionUID = -8935407974008525425L;

	public NetGraphLikeZoomView(MaltTreeViewerGui maltTreeViewerGui) {
		super(maltTreeViewerGui);
		NodeDragHandler nodeDragHandler = new NodeDragHandler(this);
		nodeLayer.addInputEventListener(nodeDragHandler);
		edgeLayer.addInputEventListener(nodeDragHandler);
		labelLayer.addInputEventListener(nodeDragHandler);
		aCanvas.getCamera().addChild(nodeDragHandler.getTooltip());
	}

	
	@Override
	public void changeSentence(TreeViewerSentence s, HashSet<Integer> selectedWords) {
		float maxXValue = 0.0f;
		float maxYValue = 0.0f;
		float yValue;
		
		resetItems();

		viewBound = new PBounds();
		maxXValue = 0.0f;
		maxYValue = 0.0f;
		for (int i = 0; i < s.size(); i++) {
			yValue = (float) (60.0 * (i == 0 ? 0 : getDepth(s.getSentenceObject(), i)));
			NetGraphLikeZoomTerminal terminal = new NetGraphLikeZoomTerminal(s.getWord(i), i, maxXValue, yValue, s.isShowDiff(),
					i == 0 ? false : s.getLaDiff().get(i - 1), (selectedWords != null ? selectedWords.contains(i) : false));
			maxXValue += Math.max(terminal.getWidth(), terminal.getLabel().getWidth()) + 20;
			if (yValue > maxYValue) {
				maxYValue = yValue;
			}
			PBounds.union(viewBound, terminal.getBounds(), viewBound);
			PBounds.union(viewBound, terminal.getLabel().getBounds(), viewBound);
			terminal.addAttribute("edges", new ArrayList<PNode>());
			addItem(terminal);
			addItem(terminal.getLabel());
		}
		
		for (int i = 1; i < s.size(); i++) {
			PNode child = getTerminal(i);
			PNode head = getTerminal(s.getWord(i).getHead());

			NetGraphLikeZoomEdge edge = new NetGraphLikeZoomEdge(head, child, s.getWord(s.getWord(i).getHead()), s.getWord(i), s.isShowDiff(), s.getUasDiff().get(i - 1),
					(selectedWords != null ? selectedWords.contains(i) : false));
			addItem(edge);
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				getCamera().setViewBounds(viewBound);
			}
		});
	}

	private int getDepth(MaltSentence maltSentence, int wordId) {
		int depth = 0;
		MaltWord word = maltSentence.getWord(wordId);
		MaltWord startWord = word;
		while (word.getHead() != -1) {
			word = maltSentence.getWord(word.getHead());
			depth++;
			if (startWord == word) {
				return -1;
			}
		}
		return depth;
	}
}
