/*
 * Created on 2007 aug 24
 */
package se.vxu.msi.malteval.grouping;

import java.util.Vector;

import se.vxu.msi.malteval.MaltEvalConfig;
import se.vxu.msi.malteval.corpus.MaltSentence;
import se.vxu.msi.malteval.metric.BothRight;
import se.vxu.msi.malteval.metric.Metric;
import se.vxu.msi.malteval.util.DataContainer;

/**
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class Sentence implements Grouping {
	private Metric metric;
	private Vector<String> validAttributes;
	private Vector<String> defaultAttributes;

	public void initialize() {
		metric = new BothRight();

		validAttributes = new Vector<String>();
		validAttributes.add(MaltEvalConfig.accuracy);
		validAttributes.add(MaltEvalConfig.exactmatch);
		validAttributes.add(MaltEvalConfig.correctcounter);
		validAttributes.add(MaltEvalConfig.includedtokenscount);
		validAttributes.add(MaltEvalConfig.sentencelength);
		validAttributes.add(MaltEvalConfig.isparserconnected);
		validAttributes.add(MaltEvalConfig.istreebankconnected);
		validAttributes.add(MaltEvalConfig.hasparsercycle);
		validAttributes.add(MaltEvalConfig.hastreebankcycle);
		validAttributes.add(MaltEvalConfig.isparserprojective);
		validAttributes.add(MaltEvalConfig.istreebankprojective);
		validAttributes.add(MaltEvalConfig.id);

		defaultAttributes = new Vector<String>();
		defaultAttributes.add(MaltEvalConfig.accuracy);
		defaultAttributes.add(MaltEvalConfig.exactmatch);
		defaultAttributes.add(MaltEvalConfig.sentencelength);
	}

	public Integer getKey(MaltSentence sentence, int wordIndex) {
		return sentence.getId();
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
		return false;
	}

	public boolean showTableHeader() {
		return true;
	}

	public String getSecondName() {
		return null;
	}

	public boolean isCorrect(int wordIndex, MaltSentence goldSentence, MaltSentence parsedSentence) {
		return metric.isCorrect(wordIndex, goldSentence, parsedSentence);
	}

	public String getSelfMetricName() {
		return metric.getClass().getSimpleName();
	}

	public String correspondingMetric() {
		return null;
	}

	public boolean isSimpleAccuracyGrouping() {
		return false;
	}
}
