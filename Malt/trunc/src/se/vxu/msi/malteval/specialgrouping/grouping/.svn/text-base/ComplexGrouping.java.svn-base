/*
 * Created on 13 mar 2008
 */
package se.vxu.msi.malteval.specialgrouping.grouping;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import se.vxu.msi.malteval.MaltEvalConfig;
import se.vxu.msi.malteval.corpus.MaltSentence;
import se.vxu.msi.malteval.evaluator.MaltParserEvaluator;
import se.vxu.msi.malteval.exceptions.MaltEvalException;
import se.vxu.msi.malteval.grouping.Grouping;
import se.vxu.msi.malteval.metric.BothRight;
import se.vxu.msi.malteval.metric.Metric;
import se.vxu.msi.malteval.util.DataContainer;

public class ComplexGrouping implements Grouping {
	private Metric metric;
	private HashMap<Integer, HashSet<String>> complexGrouping_ = null;

	private Vector<String> validAttributes;
	private Vector<String> defaultAttributes;
	
	public void initialize() {
		metric = new BothRight();
		validAttributes = MaltEvalConfig.getValidAccuracyAttributes();
		defaultAttributes = MaltEvalConfig.getDefaultAccuracyAttributes();
	}

	public String getKey(MaltSentence sentence, int wordIndex) {
		StringBuffer sb = new StringBuffer();
		for (Integer offset : complexGrouping_.keySet()) {
			for (String attribute : complexGrouping_.get(offset)) {
				if (wordIndex + offset < 0) {
					sb.append("^,");
				} else if (sentence.getSentenceLength() <= wordIndex + offset) {
					sb.append("$,");
				} else {
					try {
						sb.append("\"" + MaltParserEvaluator.getKey(sentence, wordIndex + offset, attribute) + "\",");
					} catch (MaltEvalException e) {
						e.printStackTrace();
						System.exit(0);
					}
				}
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("@" + offset + ";");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
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

	public void setComplexGrouping(HashMap<Integer, HashSet<String>> complexGrouping_) {
		this.complexGrouping_ = complexGrouping_;
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
		return true;
	}
}
