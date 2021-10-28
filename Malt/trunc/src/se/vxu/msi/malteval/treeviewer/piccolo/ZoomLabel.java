/*
 * Created on 12 nov 2008
 */
package se.vxu.msi.malteval.treeviewer.piccolo;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;

public abstract class ZoomLabel extends PNode implements ToolTipObject {
	private static final long serialVersionUID = 5959932146739786605L;

	protected boolean searchHit_;
	protected boolean showDiff_;
	protected boolean uasDiff_;

	public ZoomLabel(boolean showDiff, boolean uasDiff, boolean searchHit) {
		setChildrenPickable(false);
		showDiff_ = showDiff;
		uasDiff_ = uasDiff;
		searchHit_ = searchHit;
	}
	
	abstract public void move();
}
