/*
 * Created on 14 nov 2008
 */
package se.vxu.msi.malteval.treeviewer.piccolo;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.event.PDragSequenceEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;

public class NodeDragHandler extends PDragSequenceEventHandler {
	private ZoomView zoomView;
	private ToolTip tooltip;

	public NodeDragHandler(ZoomView zv) {
		zoomView = zv;
		tooltip = new ToolTip();
		getEventFilter().setMarksAcceptedEventsAsHandled(true);
	}

	public void mouseWheelRotated(PInputEvent event) {
		zoomView.processMouseWheelRotated(event);
	}

	public void mouseMoved(PInputEvent event) {
		ToolTipObject n = (ToolTipObject) event.getPickedNode();
		updateToolTip(event);
		n.highlight();
		event.getPickedNode().repaint();
	}

	public void mouseClicked(PInputEvent event) {
		if (!(event.getPickedNode() instanceof PCamera)) {
			zoomView.getMaltTreeViewerGui().changeViewPosition(event.getPickedNode().getFullBounds(), null, false);
		}
	}

	public void mouseEntered(PInputEvent event) {
		ToolTipObject n = (ToolTipObject) event.getInputManager().getMouseOver().getPickedNode();
		tooltip.setVisible(true);
		tooltip.setText(n.getToolTip());
	}

	public void mouseExited(PInputEvent event) {
		ToolTipObject n = (ToolTipObject) event.getPickedNode();
		tooltip.setVisible(false);
		n.unhighlight();
		event.getPickedNode().repaint();
	}

	public void updateToolTip(PInputEvent event) {
		tooltip.move(event.getCanvasPosition().getX(), event.getCanvasPosition().getY() + 20);
	}
	
	public ToolTip getTooltip() {
		return tooltip;
	}
}
