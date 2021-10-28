/*
 * Created on 28 aug 2007
 */
package se.vxu.msi.malteval.metric;

import se.vxu.msi.malteval.corpus.MaltSentence;

public class AnyRight implements Metric {
	public void initialize() {
	}

	public boolean isCorrect(int wordIndex, MaltSentence goldSentence, MaltSentence parsedSentence) {
		return goldSentence.getWord(wordIndex).getHead() == parsedSentence.getWord(wordIndex).getHead() || goldSentence.getWord(wordIndex).getDeprel().equals(parsedSentence.getWord(wordIndex).getDeprel());
	}

	public String getSecondName() {
		return null;
	}
}
