/*
 * Created on 2007 aug 28
 */
package se.vxu.msi.malteval.metric;

import se.vxu.msi.malteval.corpus.MaltSentence;

public interface Metric {
	public void initialize();

	public boolean isCorrect(int wordIndex, MaltSentence goldSentence, MaltSentence parsedSentence);
	public String getSecondName();
}
