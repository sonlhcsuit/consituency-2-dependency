/*
 * Created on 2004-aug-23
 */
package se.vxu.msi.malteval.datareaders;

import java.util.Vector;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class XMLEmptyNodes {
	public static Vector<Node> removeEmptyNodes(NodeList nl) {
		int i;
		Vector<Node> v = new Vector<Node>();
		for (i = 0; i < nl.getLength(); i++) {
			if (!(nl.item(i).getNodeType() == Node.TEXT_NODE && nl.item(i).getNodeValue().trim().equals(""))) {
				v.add(nl.item(i));
			}
		}
		return v;
	}
}
