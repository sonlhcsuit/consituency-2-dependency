/*
 * Created on 2007 aug 24
 */
package se.vxu.msi.malteval.grouping;

import se.vxu.msi.malteval.corpus.MaltSentence;
import se.vxu.msi.malteval.util.DataContainer;

/**
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class RelationLength implements Grouping {

	public void initialize() {
	}

	public Integer getKey(MaltSentence sentence, int wordIndex) {
		if (sentence.getWord(wordIndex).getHead() == 0) {
			return -1;
		} else {
			return Math.abs(sentence.getWord(wordIndex).getHead() - (wordIndex + 1));
		}
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
		return getKey(goldSentence, wordIndex).equals(getKey(parsedSentence, wordIndex));
	}

	public String getSelfMetricName() {
		return getClass().getSimpleName();
	}

	public String correspondingMetric() {
		return "HeadToChildDistanceRight";
	}

	public boolean isSimpleAccuracyGrouping() {
		return false;
	}
}
