/*
 * Created on 2007 aug 24
 */
package se.vxu.msi.malteval.grouping;

import se.vxu.msi.malteval.corpus.MaltSentence;
import se.vxu.msi.malteval.util.DataContainer;

/**
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class ArcDirection implements Grouping {
	public void initialize() {
	}

	public String getKey(MaltSentence sentence, int wordIndex) {
		 if (sentence.getWord(wordIndex).getHead() == 0) {
			return "to_root";
		} else if (sentence.getWord(wordIndex).getHead() - 1 < wordIndex) {
			return "left";
		} else if (sentence.getWord(wordIndex).getHead() - 1 > wordIndex) {
			return "right";
		} else {
			return "self";
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
		return "DirectionRight";
	}

	public boolean isSimpleAccuracyGrouping() {
		return false;
	}
}
