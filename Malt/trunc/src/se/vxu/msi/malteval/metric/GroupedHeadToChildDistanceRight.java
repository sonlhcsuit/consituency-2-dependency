/*
 * Created on 28 aug 2007
 */
package se.vxu.msi.malteval.metric;

import se.vxu.msi.malteval.corpus.MaltSentence;

public class GroupedHeadToChildDistanceRight implements Metric {
	public void initialize() {
	}

	public boolean isCorrect(int wordIndex, MaltSentence goldSentence, MaltSentence parsedSentence) {
		return getHeadToChildDistance(wordIndex, goldSentence).equals(getHeadToChildDistance(wordIndex, parsedSentence));
	}
	
	private String getHeadToChildDistance(int wordIndex, MaltSentence sentence) {
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

	public String getSecondName() {
		return null;
	}
}
