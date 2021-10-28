package se.vxu.msi.malteval.corpus;

import se.vxu.msi.malteval.MaltEvalConfig;
import se.vxu.msi.malteval.treeviewer.piccolo.inlinescrollview.InlineScrollTerminal;

/**
 * The class representing a token in a MaltSentence.
 * 
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class MaltWord {
	private String form_;
	private String cpostag_;
	private int head_;
	private String deprel_;

	private String lemma_;
	private String postag_;
	private String feats_;
	private int pHead_;
	private String pDeprel_;
	
	private InlineScrollTerminal wordComp_;

	/**
	 * This constructor could be used if only word form, coarse grained part-of-speech, head index and dependency label is known.
	 * 
	 * @param f
	 *            The word form
	 * @param p
	 *            The coarse grained part-of-speech
	 * @param h
	 *            The head index (virtual root has index 0)
	 * @param d
	 *            The dependency label
	 */
	public MaltWord(String f, String p, int h, String d) {
		form_ = f;
		cpostag_ = p;
		head_ = h;
		deprel_ = d;

		lemma_ = null;
		postag_ = null;
		feats_ = null;
		pHead_ = -1;
		pDeprel_ = null;
	}

	/**
	 * Constructor for specifying every possible value of the CoNLL-format.
	 * 
	 * @param form
	 *            The word form
	 * @param lemma
	 *            The lemma
	 * @param cpostag
	 *            The coarse grained part-of-speech
	 * @param postag
	 *            The fine grained part-of-speech
	 * @param feats
	 *            The feats
	 * @param head
	 *            The head index (virtual root has index 0)
	 * @param deprel
	 *            The dependency label
	 * @param pHead
	 *            The pHead value
	 * @param pDeprel
	 *            The pDeprel value
	 */
	public MaltWord(String form, String lemma, String cpostag, String postag, String feats, int head, String deprel, int pHead,
			String pDeprel) {
		this(form, cpostag, head, deprel);
		lemma_ = lemma;
		postag_ = postag;
		feats_ = feats;
		pHead_ = pHead;
		pDeprel_ = pDeprel;
	}

	/**
	 * Constructor for making a copy of another MaltWord
	 * 
	 * @param w
	 *            The MaltWord to make of copy of.
	 */
	public MaltWord(MaltWord w) {
		form_ = w.getForm();
		cpostag_ = w.getCPostag();
		head_ = w.getHead();
		deprel_ = w.getDeprel();

		lemma_ = w.getLemma();
		postag_ = w.getPostag();
		feats_ = w.getFeats();
		pHead_ = w.getPHead();
		pDeprel_ = w.getPDeprel();
	}

	/**
	 * @return The word form
	 */
	public String getForm() {
		return form_;
	}

	/**
	 * @return The coarse grained part-of-speech
	 */
	public String getCPostag() {
		return cpostag_;
	}

	/**
	 * @return The head index
	 */
	public int getHead() {
		return head_;
	}

	/**
	 * @return The deprel label
	 */
	public String getDeprel() {
		return deprel_;
	}

	public String toString() {
		return getForm();
	}

	/**
	 * @return The feats value
	 */
	public String getFeats() {
		return feats_;
	}

	/**
	 * @return The lemma
	 */
	public String getLemma() {
		return lemma_;
	}

	/**
	 * @return The dDeprel
	 */
	public String getPDeprel() {
		return pDeprel_;
	}

	/**
	 * @return The pHead
	 */
	public int getPHead() {
		return pHead_;
	}

	/**
	 * @return The fine grained part-of-speech
	 */
	public String getPostag() {
		return postag_;
	}

	public String getType(String type) {
		if (type.equals(MaltEvalConfig.excludeWordforms)) {
			return getForm();
		} else if (type.equals(MaltEvalConfig.excludeLemma)) {
			return getLemma();
		} else if (type.equals(MaltEvalConfig.excludeCpostags)) {
			return getCPostag();
		} else if (type.equals(MaltEvalConfig.excludePostags)) {
			return getPostag();
		} else if (type.equals(MaltEvalConfig.excludeFeats)) {
			return getFeats();
		} else if (type.equals(MaltEvalConfig.excludeDeprels)) {
			return getDeprel();
		} else if (type.equals(MaltEvalConfig.excludePdeprels)) {
			return getPDeprel();
		} else {
			return null;
		}
	}

	/**
	 * Change the head index of the token.
	 * 
	 * @param head
	 *            New head index
	 */
	public void setHead(int head) {
		head_ = head;
	}

	/**
	 * Change the deprel label of the token.
	 * 
	 * @param deprel
	 *            New deprel label
	 */
	public void setDeprel(String deprel) {
		deprel_ = deprel;
	}

	public Object clone() {
		return new MaltWord(this);
	}

	/**
	 * @return A string of the token as a line in the CoNLL format.
	 */
	public String allToString() {
		return form_ + "\t" + lemma_ + "\t" + cpostag_ + "\t" + postag_ + "\t" + feats_ + "\t" + head_ + "\t" + deprel_ + "\t"
				+ (pHead_ == -1 ? "_" : String.valueOf(pHead_)) + "\t" + pDeprel_;
	}

	public void setWordComp(InlineScrollTerminal wordComp) {
		wordComp_ = wordComp;
	}

	public InlineScrollTerminal getWordComp() {
		return wordComp_;
	}
}
