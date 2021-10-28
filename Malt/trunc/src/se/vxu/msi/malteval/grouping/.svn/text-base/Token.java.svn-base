/*
 * Created on 2007 aug 24
 */
package se.vxu.msi.malteval.grouping;

import java.util.HashMap;

import se.vxu.msi.malteval.corpus.MaltSentence;
import se.vxu.msi.malteval.metric.BothRight;
import se.vxu.msi.malteval.metric.Metric;
import se.vxu.msi.malteval.util.DataContainer;
import se.vxu.msi.malteval.util.Pair;

/**
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class Token implements Grouping {
	private Metric metric;
	private int index;
	private HashMap<Pair<Integer>, Integer> tokens;
	
	public void initialize() {
		metric = new BothRight();
		index = 1;
		tokens = new HashMap<Pair<Integer>, Integer>();
	}

	public Integer getKey(MaltSentence sentence, int wordIndex) {
		Integer value;
		Pair<Integer> key = new Pair<Integer>(sentence.getId(), wordIndex);
		if ((value = tokens.get(key)) == null) {
			value = new Integer(index);
			tokens.put(key, value);
			index++;
		}
		return value;
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
