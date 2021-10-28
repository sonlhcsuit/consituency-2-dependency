/*
 * Created on 2007 aug 24
 */
package se.vxu.msi.malteval.specialgrouping.grouping;

import java.text.DecimalFormat;

import se.vxu.msi.malteval.corpus.MaltSentence;
import se.vxu.msi.malteval.grouping.Grouping;
import se.vxu.msi.malteval.metric.BothRight;
import se.vxu.msi.malteval.metric.Metric;
import se.vxu.msi.malteval.util.DataContainer;

/**
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class SentenceAndToken implements Grouping {
	private Metric metric;
	private DecimalFormat decimalFormat;
	
	public void initialize() {
		decimalFormat = new DecimalFormat("000000000");
		metric = new BothRight();
	}

	public String getKey(MaltSentence sentence, int wordIndex) {
		return decimalFormat.format(sentence.getId()) + "_" + decimalFormat.format(wordIndex);
	}


	public boolean isComplexGroupItem() {
		return true;
	}

	public DataContainer postProcess(DataContainer dataContainer) {
		return dataContainer;
	}

	public boolean showDetails() {
		return false;
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
		return false;
	}
}
