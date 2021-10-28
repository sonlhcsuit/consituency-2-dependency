/*
 * Created on 2007 aug 24
 */
package se.vxu.msi.malteval.grouping;

import java.util.Vector;

import se.vxu.msi.malteval.MaltEvalConfig;
import se.vxu.msi.malteval.corpus.MaltSentence;
import se.vxu.msi.malteval.metric.Metric;
import se.vxu.msi.malteval.util.DataContainer;

/**
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class GroupedStartWordPosition implements Grouping {
	private Metric metric;
	private Vector<String> validAttributes;
	private Vector<String> defaultAttributes;
	
	public void initialize() {
		validAttributes = MaltEvalConfig.getValidAccuracyAttributes();
		defaultAttributes = MaltEvalConfig.getDefaultAccuracyAttributes();
	}

	public String getKey(MaltSentence sentence, int wordIndex) {
		if (wordIndex == 0) {
			return " 1";
		} else if (wordIndex < 4) {
			return " " + (wordIndex + 1);
		} else if (wordIndex < 10) {
			return " " + (wordIndex - wordIndex % 2 + 1) + "-" + (wordIndex - wordIndex % 2 + 2);
		} else if (wordIndex < 12) {
			return (wordIndex - wordIndex % 2 + 1) + "-" + (wordIndex - wordIndex % 2 + 2);
		} else if (wordIndex < 20) {
			return "" + (wordIndex - wordIndex % 4 + 1) + "-" + (wordIndex - wordIndex % 4 + 4);
		} else {
			return "20-";
		}
	}

	public Vector<String> getValidAttributes() {
		return validAttributes;
	}

	public Vector<String> getDefaultAttributes() {
		return defaultAttributes;
	}

	public boolean isComplexGroupItem() {
		return false;
	}

	public DataContainer postProcess(DataContainer dataContainer) {
		return dataContainer;
	}

	public boolean showDetails() {
		return true;
	}

	public boolean showTableHeader() {
		return false;
	}
}
