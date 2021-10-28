

import java.util.Vector;

import se.vxu.msi.malteval.MaltEvalConfig;
import se.vxu.msi.malteval.corpus.MaltSentence;
import se.vxu.msi.malteval.grouping.ArcDirection;
import se.vxu.msi.malteval.grouping.Deprel;
import se.vxu.msi.malteval.grouping.Grouping;
import se.vxu.msi.malteval.util.DataContainer;

public class ArcDirectionAndDeprel implements Grouping {
	private Vector<String> validAttributes;
	private Vector<String> defaultAttributes;

	private ArcDirection arcDirection;
	private Deprel deprel;

	public Object getKey(MaltSentence sentence, int wordIndex) {
		return arcDirection.getKey(sentence, wordIndex) + " / " + deprel.getKey(sentence, wordIndex);
	}

	public Vector<String> getDefaultAttributes() {
		return defaultAttributes;
	}

	public Vector<String> getValidAttributes() {
		return validAttributes;
	}

	public void initialize() {
		arcDirection = new ArcDirection();
		deprel = new Deprel();
		arcDirection.initialize();
		deprel.initialize();
		validAttributes = MaltEvalConfig.getValidPrecisionAndRecallAttributes();
		defaultAttributes = MaltEvalConfig.getDefaultPrecisionAndRecallAttributes();
	}

	public boolean isComplexGroupItem() {
		return false;
	}

	public DataContainer postProcess(DataContainer arg0) {
		return arg0;
	}

	public boolean showDetails() {
		return true;
	}

	public boolean showTableHeader() {
		return false;
	}
}
