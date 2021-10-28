/*
 * Created on 2007 aug 24
 */
package se.vxu.msi.malteval.grouping;

import se.vxu.msi.malteval.corpus.MaltSentence;
import se.vxu.msi.malteval.metric.BothRight;
import se.vxu.msi.malteval.metric.Metric;
import se.vxu.msi.malteval.util.DataContainer;

/**
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class RelativeWordPosition implements Grouping {
	private Metric metric;
	
	public void initialize() {
		metric = new BothRight();
	}

	public Integer getKey(MaltSentence sentence, int wordIndex) {
		return 1 + (int) Math.floor(14.0 * ((double) (wordIndex + 1)) / sentence.getSentenceLength());
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
