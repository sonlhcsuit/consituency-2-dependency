/*
 * Created on 2007 aug 24
 */
package se.vxu.msi.malteval.grouping;

import se.vxu.msi.malteval.corpus.MaltSentence;
import se.vxu.msi.malteval.util.DataContainer;

/**
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class GroupedRelationLength implements Grouping {

	public void initialize() {
	}

	public String getKey(MaltSentence sentence, int wordIndex) {
		if (sentence.getWord(wordIndex).getHead() == 0) {
			return "to_root";
		} else if (Math.abs(sentence.getWord(wordIndex).getHead() - (wordIndex + 1)) <= 1) {
			return "1";
		} else if (Math.abs(sentence.getWord(wordIndex).getHead() - (wordIndex + 1)) <= 2) {
			return "2";
		} else if (Math.abs(sentence.getWord(wordIndex).getHead() - (wordIndex + 1)) <= 6) {
			return "3-6";
		} else {
			return "7-...";
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

	public boolean isSimpleAccuracyGroupingStrategy() {
		return false;
	}

	public String correspondingMetric() {
		return "GroupedHeadToChildDistanceRight";
	}

	public boolean isSimpleAccuracyGrouping() {
		return false;
	}
}
