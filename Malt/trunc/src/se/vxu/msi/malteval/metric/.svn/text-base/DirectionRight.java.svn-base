/*
 * Created on 28 aug 2007
 */
package se.vxu.msi.malteval.metric;

import se.vxu.msi.malteval.corpus.MaltSentence;
import se.vxu.msi.malteval.grouping.ArcDirection;

public class DirectionRight implements Metric {
	private ArcDirection arcDirection;
	
	public void initialize() {
		arcDirection = new ArcDirection();
	}

	public boolean isCorrect(int wordIndex, MaltSentence goldSentence, MaltSentence parsedSentence) {
		return arcDirection.isCorrect(wordIndex, goldSentence, parsedSentence);
	}

	public String getSecondName() {
		return null;
	}
}
