/*
 * Created on 28 aug 2007
 */
package se.vxu.msi.malteval.metric;

import se.vxu.msi.malteval.corpus.MaltSentence;

public class HeadRight implements Metric {
	public void initialize() {
	}

	public boolean isCorrect(int wordIndex, MaltSentence goldSentence, MaltSentence parsedSentence) {
		return goldSentence.getWord(wordIndex).getHead() == parsedSentence.getWord(wordIndex).getHead();
	}

	public String getSecondName() {
		return "UAS";
	}
}
