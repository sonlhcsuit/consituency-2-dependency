/*
 * Created on 28 aug 2007
 */
package se.vxu.msi.malteval.metric;

import se.vxu.msi.malteval.corpus.MaltSentence;

public class HeadToChildDistanceRight implements Metric {
	public void initialize() {
	}

	public boolean isCorrect(int wordIndex, MaltSentence goldSentence, MaltSentence parsedSentence) {

		return getHeadToChildDistance(wordIndex, goldSentence) == getHeadToChildDistance(wordIndex, parsedSentence);
	}
	
	private int getHeadToChildDistance(int wordIndex, MaltSentence sentence) {
		if (sentence.getWord(wordIndex).getHead() == 0) {
			return -1;
		}
		return Math.abs(sentence.getWord(wordIndex).getHead() - (wordIndex + 1));
	}

	public String getSecondName() {
		return null;
	}
}
