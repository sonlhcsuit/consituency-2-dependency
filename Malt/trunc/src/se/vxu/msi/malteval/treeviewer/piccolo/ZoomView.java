/*
 * Created on 12 nov 2008
 */
package se.vxu.msi.malteval.treeviewer.piccolo;

import javax.swing.JPanel;

import se.vxu.msi.malteval.treeviewer.MaltTreeViewerGui;
import se.vxu.msi.malteval.treeviewer.gui.ViewMode;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PPanEventHandler;
import edu.umd.cs.piccolo.event.PZoomEventHandler;
import edu.umd.cs.piccolo.util.PAffineTransform;
import edu.umd.cs.piccolo.util.PBounds;

abstract public class ZoomView extends ViewMode {
	private static final long serialVersionUID = 4389608208044543521L;

	protected PBounds viewBound;

	protected PCanvas aCanvas;
	protected PLayer nodeLayer;
	protected PLayer edgeLayer;
	protected PLayer labelLayer;

	private boolean isPanned;

	public ZoomView(MaltTreeViewerGui maltTreeViewerGui) {
		super(maltTreeViewerGui);
		initialize();
		add(aCanvas);
	}
	
	public void initialize() {
		aCanvas = new PCanvas();
		nodeLayer = new PLayer();
		edgeLayer = new PLayer();
		labelLayer = new PLayer();

		aCanvas.setZoomEventHandler(new MyZoom());
		aCanvas.setPanEventHandler(new MyPan());
		aCanvas.getCamera().addLayer(0, edgeLayer);
		aCanvas.getCamera().addLayer(1, nodeLayer);
		aCanvas.getCamera().addLayer(2, labelLayer);

		aCanvas.getCamera().addInputEventListener(new PBasicInputEventHandler() {
			public void mouseClicked(PInputEvent event) {
				if (event.getButton() == 2) {
					maltTreeViewerGui_.changeViewPosition(viewBound, null, true);
				}
			}

			public void mouseWheelRotated(PInputEvent event) {
				processMouseWheelRotated(event);
			}

			public void mouseReleased(PInputEvent event) {
				if (isPanned) {
					maltTreeViewerGui_.changeViewPosition(aCanvas.getCamera().getViewBounds(), ZoomView.this, true);
					isPanned = false;
				}
			}
		});
	}
	
	protected void resetItems() {
		nodeLayer.removeAllChildren();
		edgeLayer.removeAllChildren();
		labelLayer.removeAllChildren();
	}
	
	protected void addItem(ZoomTerminal terminal) {
		nodeLayer.addChild(terminal);
	}
	
	protected void addItem(ZoomLabel label) {
		labelLayer.addChild(label);
	}
	
	protected void addItem(ZoomEdge edge) {
		edgeLayer.addChild(edge);
	}
	
	protected PNode getTerminal(int index) {
		return nodeLayer.getChild(index);
	}
	
	protected PCamera getCamera() {
		return aCanvas.getCamera();
	}
	
	protected void processMouseWheelRotated(PInputEvent event) {
		PAffineTransform transformation = aCanvas.getCamera().getViewTransform();
		if (event.getWheelRotation() == 1) {
			transformation.scaleAboutPoint(2.0 / 3.0, event.getPosition().getX(), event.getPosition().getY());
		} else {
			transformation.scaleAboutPoint(3.0 / 2.0, event.getPosition().getX(), event.getPosition().getY());
		}
		maltTreeViewerGui_.changeViewPositionByTransformation(transformation, null);
	}

	public void changeViewPosition(PBounds viewPosition, boolean scale) {
		if (viewPosition == null) {
			aCanvas.getCamera().animateViewToCenterBounds(viewBound, scale, 500);
		} else {
			aCanvas.getCamera().animateViewToCenterBounds(viewPosition, scale, 500);
		}
	}

	public void changeViewPositionByTransformation(PAffineTransform transformation) {
		aCanvas.getCamera().animateViewToTransform(transformation, 500);
	}

	@Override
	public JPanel getJPanelToImage() {
		return this;
	}
	
	private class MyPan extends PPanEventHandler {
		protected void pan(PInputEvent aEvent) {
			super.pan(aEvent);
			isPanned = true;
		}
	}

	private class MyZoom extends PZoomEventHandler {
		protected void dragActivityFirstStep(PInputEvent aEvent) {
			super.dragActivityFirstStep(aEvent);
			isPanned = true;
		}
	}
}
