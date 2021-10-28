/*
 * Created on 2004-sep-03
 */
package se.vxu.msi.malteval.evaluator;

import java.io.InputStreamReader;

import se.vxu.msi.malteval.configdata.util.Evaluation;
import se.vxu.msi.malteval.configdata.util.Evaluator;
import se.vxu.msi.malteval.corpus.MaltTreebank;
import se.vxu.msi.malteval.exceptions.MaltEvalException;
import se.vxu.msi.malteval.util.DataContainer;

/**
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public abstract class AbstractEvaluator {
	protected Evaluator evaluator;

	protected MaltTreebank processedData_;
	protected MaltTreebank corpusData_;
	
	private int minSentenceLength_;
	private int maxSentenceLength_;

	public AbstractEvaluator(Evaluator e) throws MaltEvalException {
		evaluator = e;
	}
	
	abstract protected MaltTreebank importData(StringBuffer buffer, String format) throws MaltEvalException;
	abstract protected MaltTreebank importData(String fileName, String format) throws MaltEvalException;
	abstract protected MaltTreebank importData(InputStreamReader stream, String format) throws MaltEvalException;
	abstract public DataContainer evaluate(Evaluation e) throws MaltEvalException;
	abstract protected boolean checkConformity() throws MaltEvalException;

	/**
	 * @param maxSentenceLength The maxSentenceLength to set.
	 */
	protected void setMaxSentenceLength(int maxSentenceLength) {
		this.maxSentenceLength_ = maxSentenceLength;
	}
	/**
	 * @param minSentenceLength The minSentenceLength to set.
	 */
	protected void setMinSentenceLength(int minSentenceLength) {
		this.minSentenceLength_ = minSentenceLength;
	}
	/**
	 * @param maxSentenceLength The maxSentenceLength to set.
	 */
	protected void setMaxSentenceLength(String maxSentenceLength) {
		setMaxSentenceLength(Integer.parseInt(maxSentenceLength));
	}
	/**
	 * @param minSentenceLength The minSentenceLength to set.
	 */
	protected void setMinSentenceLength(String minSentenceLength) {
		setMinSentenceLength(Integer.parseInt(minSentenceLength));
	}

	/**
	 * @return Returns the maxSentenceLength.
	 */
	public int getMaxSentenceLength() {
		return maxSentenceLength_;
	}
	/**
	 * @return Returns the minSentenceLength.
	 */
	public int getMinSentenceLength() {
		return minSentenceLength_;
	}
}
