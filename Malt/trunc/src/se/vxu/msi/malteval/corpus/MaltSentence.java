package se.vxu.msi.malteval.corpus;

import java.util.Vector;

import se.vxu.msi.malteval.MaltEvalConfig;

/**
 * Class for representing a sentence.
 * 
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class MaltSentence {
	private MaltWord[] words_;
	private int id_;
	private String parser_;
	private String user_;
	private String date_;

	// the new attributes
	private double score_;
	private double time_;

	/**
	 * Constructor for creating a new MaltSentence object when the words/tokens are still unknown.
	 * 
	 * @param nOfw
	 *            Number of words
	 * @param i
	 *            Sentence id
	 * @param p
	 *            Parser name
	 * @param u
	 *            User name
	 * @param d
	 *            Date
	 * @param score
	 *            The score of the sentence
	 * @param time
	 *            The parsing time of the sentence
	 */
	public MaltSentence(int nOfw, int i, String p, String u, String d, double score, double time) {
		words_ = new MaltWord[nOfw];
		id_ = i;
		parser_ = p;
		user_ = u;
		date_ = d;

		score_ = score;
		time_ = time;
	}

	/**
	 * Constructor for creating a new MaltSentence object when the words/tokens are known.
	 * 
	 * @param w
	 *            The words in a vector
	 * @param i
	 *            Sentence id
	 * @param p
	 *            Parser name
	 * @param u
	 *            User name
	 * @param d
	 *            Date
	 * @param score
	 *            The score of the sentence
	 * @param time
	 *            The parsing time of the sentence
	 */
	public MaltSentence(Vector<MaltWord> w, int i, String p, String u, String d, double score, double time) {
		words_ = (MaltWord[]) w.toArray(new MaltWord[0]);
		id_ = i;
		parser_ = p;
		user_ = u;
		date_ = d;

		this.score_ = score;
		this.time_ = time;
	}

	/**
	 * Putting a word in a certain position in the sentence
	 * 
	 * @param i
	 *            Word index
	 * @param w
	 *            The word
	 */
	public void setWord(int i, MaltWord w) {
		if (i >= 0 && i < words_.length) {
			words_[i] = w;
		} else {
			System.err.print("Wrong index in sentence: " + i);
			// System.exit(1);
		}
	}

	/**
	 * Returns the word at position i
	 * 
	 * @param i
	 * @return The word at position i
	 */
	public MaltWord getWord(int i) {
		if (!(i >= 0 && i < words_.length)) {
			System.err.print("Wrong index in sentence: " + i);
			// System.exit(1);
		}
		return words_[i];
	}

	/**
	 * Get the sentence id
	 * 
	 * @return The sentence id
	 */
	public int getId() {
		return id_;
	}

	/**
	 * Get the parser name
	 * 
	 * @return The parser name. Comes as an attribute to the sentence element in MaltXML.
	 */
	public String getParser() {
		return parser_;
	}

	/**
	 * Get the user name
	 * 
	 * @return The user name
	 */
	public String getUser() {
		return user_;
	}

	/**
	 * Get the date. Comes as an attribute to the sentence element in MaltXML.
	 * 
	 * @return The date
	 */
	public String getDate() {
		return date_;
	}

	/**
	 * Get the score of the sentence. Comes as an attribute to the sentence element in MaltXML.
	 * 
	 * @return The score of the sentence
	 */
	public double getScore() {
		return score_;
	}

	/**
	 * Get the parsing time of the sentence
	 * 
	 * @return The parsing time of the sentence. Comes as an attribute to the sentence element in MaltXML.
	 */
	public double getTime() {
		return time_;
	}

	/**
	 * Get the sentence length, including the virtual root token.
	 * 
	 * @return The sentence length
	 */
	public int getSentenceLength() {
		return words_.length;
	}

	/**
	 * The depth of the token at position <code>wordId</code>. The virtual root is located on depth 0.
	 * 
	 * @param wordId
	 *            The word index
	 * @return The depth of the token at position <code>wordId</code>.
	 */
	public int getDepth(int wordId) {
		int depth = 0;
		MaltWord word = getWord(wordId);
		MaltWord startWord = word;
		while (word.getHead() != 0) {
			word = getWord(word.getHead() - 1);
			depth++;
			if (startWord == word) {
				return -1;
			}
		}
		return depth;
	}

	/**
	 * Get the frame of the token at position <code>wordId</code>.
	 * 
	 * @param wordIndex
	 *            The word index
	 * @return the frame of the token at position <code>wordId</code>.
	 */
	public String getFrame(int wordIndex) {
		StringBuffer frame = new StringBuffer();
		for (int i = 0; i < getSentenceLength(); i++) {
			if (getWord(i).getHead() - 1 == wordIndex) {
				frame.append(getWord(i).getDeprel() + " ");
			} else if (i == wordIndex) {
				frame.append("*" + getWord(i).getDeprel() + "* ");
			}
		}
		return frame.toString();
	}

	/**
	 * Get the branching factor of the token at position <code>wordId</code>.
	 * 
	 * @param wordIndex
	 *            The word index
	 * @return the branching factor of the token at position <code>wordId</code>.
	 */
	public int getBranchingFactor(int wordIndex) {
		int childCount = 0;
		for (int i = 0; i < getSentenceLength(); i++) {
			if (getWord(i).getHead() - 1 == wordIndex) {
				childCount++;
			}
		}
		return childCount;
	}

	/**
	 * Is the sentence projective or not?
	 * 
	 * @return <code>true>/code> or <code>false</code> of the sentence is projective or not.
	 */
	public boolean isProjective() {
		int i, j;
		int ileft, iright, jleft, jright;

		for (i = 0; i < getSentenceLength() - 1; i++) {
			for (j = i + 1; j < getSentenceLength(); j++) {
				if (getWord(i).getHead() - 1 != j && getWord(j).getHead() - 1 != i) {
					if (i < getWord(i).getHead() - 1) {
						ileft = i;
						iright = getWord(i).getHead() - 1;
					} else {
						ileft = getWord(i).getHead() - 1;
						iright = i;
					}
					if (j < getWord(j).getHead() - 1) {
						jleft = j;
						jright = getWord(j).getHead() - 1;
					} else {
						jleft = getWord(j).getHead() - 1;
						jright = j;
					}

					if (ileft == -1) {
						if (jleft != -1 && jleft < iright && iright < jright) {
							// System.out.print(id + " ");
							return false;
						}
					} else if (jleft == -1) {
						if (ileft != -1 && ileft < jright && jright < iright) {
							// System.out.print(id + " ");
							return false;
						}
					} else {
						if (ileft < jleft && jleft < iright && iright < jright) {
							// System.out.print(id + " ");
							return false;
						} else if (jleft < ileft && ileft < jright && jright < iright) {
							// System.out.print(id + " ");
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Does the sentence contain cycles?
	 * 
	 * @return <code>true>/code> or <code>false</code> of the sentence has a cycle or not.
	 */
	public boolean hasCycle() {
		int i, j;
		int[] visited = new int[getSentenceLength()];

		try {
			for (i = 0; i < visited.length; i++) {
				visited[i] = -1;
			}
			for (i = 0; i < getSentenceLength(); i++) {
				j = i;
				while (j != -1) {
					visited[j] = i;
					j = getWord(j).getHead() - 1;
					if (j == -1) {
						break;
					}
					if (visited[i] == visited[j]) {
						return true;
					}
				}
			}
		} catch (Throwable e) {
			System.err.println("Warning at sentence: '" + id_ + "'. Invalid head value for token.");
			return false;
		}
		return false;
	}

	/**
	 * Is the sentence connected?
	 * 
	 * @return <code>true>/code> or <code>false</code> of the sentence is connected or not.
	 */
	public boolean isConnected() {
		int i;
		boolean foundARoot = false;

		for (i = 0; i < getSentenceLength(); i++) {
			if (getWord(i).getHead() == 0) {
				if (!foundARoot) {
					foundARoot = true;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Is the token at position <code>wordIndex</code> projective or not.
	 * 
	 * @param wordIndex
	 * @return <code>true>/code> or <code>false</code> of the token is projective or not.
	 */
	public boolean isProjective(int wordIndex) {
		int i, j;
		if (hasCycle()) {
			return false;
		}
		if (wordIndex < getWord(wordIndex).getHead()) {
			for (i = wordIndex + 1; i < getWord(wordIndex).getHead() - 1; i++) {
				j = i;
				while (j != -1 && j != getWord(wordIndex).getHead() - 1) {
					j = getWord(j).getHead() - 1;
				}
				if (j != getWord(wordIndex).getHead() - 1 && j == -1) {
					return false;
				}
//				j = getWord(i).getHead() - 1;
//				while (wordIndex < j + 1 && j + 1 < getWord(wordIndex).getHead()) {
//					j = getWord(j).getHead() - 1;
//				}
//				if (j + 1 != getWord(wordIndex).getHead()) {
//					return false;
//				}
			}
		} else {
			for (i = getWord(wordIndex).getHead(); i < wordIndex; i++) {
//				j = getWord(i).getHead() - 1;
//				while (getWord(wordIndex).getHead() < j + 1 && j < wordIndex + 1) {
//					j = getWord(j).getHead() - 1;
//				}
//				if (j + 1 != getWord(wordIndex).getHead()) {
//					return false;
//				}
				j = i - 1;
				while (j != -1 && j != getWord(wordIndex).getHead() - 1) {
					j = getWord(j).getHead() - 1;
				}
				if (j != getWord(wordIndex).getHead() - 1 && j == -1) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Is the sentence inside or outside the interval specified by <code>min</code> and <code>max</code>
	 * 
	 * @param min
	 *            The lower border (sentences of length <code>min</code> are inside the interval
	 * @param max
	 *            The upper border (sentences of length <code>max</code> are outside the interval. <code>max = 0</code> represents positive infinity.
	 * @return <code>true</code> of the sentence is outside the interval, and <code>false</code> otherwise.
	 */
	public boolean discard(int min, int max) {
		if (getSentenceLength() < min) {
			return true;
		} else if (max > 0 && getSentenceLength() > max) {
			return true;
		}
		return false;
	}

	public Number getData(String attribute) {
		if (attribute.equals("score")) {
			return new Double(getScore());
		} else if (attribute.equals("time")) {
			return new Double(getTime());
		} else if (attribute.equals("sentencelength")) {
			return new Integer(getSentenceLength());
		} else if (attribute.equals(MaltEvalConfig.isparserconnected) || attribute.toLowerCase().equals(MaltEvalConfig.istreebankconnected)) {
			return new Integer(isConnected() ? 1 : 0);
		} else if (attribute.equals(MaltEvalConfig.hasparsercycle) || attribute.toLowerCase().equals(MaltEvalConfig.hastreebankcycle)) {
			return new Integer(hasCycle() ? 1 : 0);
		} else if (attribute.equals(MaltEvalConfig.isparserprojective)
				|| attribute.toLowerCase().equals(MaltEvalConfig.istreebankprojective)) {
			return new Integer(isProjective() ? 1 : 0);
		} else if (attribute.equals("id")) {
			return new Integer(getId());
		}
		return null;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (MaltWord w : words_) {
			sb.append(w + " ");
		}
		return sb.toString();
		// return words_.toString();
	}

	/**
	 * Get all words of the sentence
	 * 
	 * @return All words as an array
	 */
	public MaltWord[] getWords() {
		return words_;
	}

	public Object clone() {
		MaltSentence newMaltSentence = new MaltSentence(getSentenceLength(), id_, parser_, user_, date_, score_, time_);
		for (int i = 0; i < newMaltSentence.getSentenceLength(); i++) {
			newMaltSentence.setWord(i, (MaltWord) getWord(i).clone());
		}
		return newMaltSentence;
	}

	public void setId(int id) {
		id_ = id;
	}
}
