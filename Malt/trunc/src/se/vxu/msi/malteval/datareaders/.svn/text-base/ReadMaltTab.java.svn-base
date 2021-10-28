/*
 * Created on 2004-sep-02
 */
package se.vxu.msi.malteval.datareaders;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;

import se.vxu.msi.malteval.MaltEvalConfig;
import se.vxu.msi.malteval.corpus.MaltSentence;
import se.vxu.msi.malteval.corpus.MaltTreebank;
import se.vxu.msi.malteval.corpus.MaltWord;
import se.vxu.msi.malteval.exceptions.MaltEvalException;

/**
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class ReadMaltTab {
	static private String charSetName = MaltEvalConfig.CHARACTER_SET_ENCODING;

	private boolean parseError;

	private Vector<MaltSentence> sentences;
	private TreeSet<String> postags;
	private TreeSet<String> deprels;

	private boolean allPostagPresent;
	private boolean allHeadsPresent;
	private boolean allDeprelPresent;

	private boolean isPostagFileRead;
	private boolean isDeprelFileRead;

	public ReadMaltTab() {
		parseError = true;
		sentences = null;
		postags = null;
		deprels = null;

		isPostagFileRead = false;
		isDeprelFileRead = false;
		postags = new TreeSet<String>();
		deprels = new TreeSet<String>();
	}

	static public void setCharsetName(String cs) {
		if (cs != null) {
			charSetName = cs;
		}
	}

	public TreeSet<String> readTabPostagFile(String fileName) {
		BufferedReader br;
		String fileLine;
		StringTokenizer st;

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), charSetName));

			while ((fileLine = br.readLine()) != null) {
				st = new StringTokenizer(fileLine, "\t");
				postags.add(st.nextToken());
			}
			br.close();
			isPostagFileRead = true;
		} catch (FileNotFoundException fnfe) {
			System.err.println("The file " + fileName + " doesn't exist");
			System.exit(1);
		} catch (IOException ioe) {
			System.err.println("File error");
			System.exit(1);
		}
		return postags;
	}

	public TreeSet<String> readTabDeprelFile(String fileName) {
		BufferedReader br;
		String fileLine;
		StringTokenizer st;

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), charSetName));

			while ((fileLine = br.readLine()) != null) {
				st = new StringTokenizer(fileLine, "\t");
				deprels.add(st.nextToken());
			}
			br.close();
			isDeprelFileRead = false;
		} catch (FileNotFoundException fnfe) {
			System.err.println("The file " + fileName + " doesn't exist");
			System.exit(1);
		} catch (IOException ioe) {
			System.err.println("File error");
			System.exit(1);
		}
		return deprels;
	}

	public void readTabFile(String filename, boolean hasPostag, boolean hasHead, boolean hasDeprel) throws MaltEvalException {
		try {
			readTabFile(new BufferedReader(new InputStreamReader(new FileInputStream(filename), charSetName)), hasPostag, hasHead,
					hasDeprel);
		} catch (FileNotFoundException e) {
			throw new MaltEvalException(e.getMessage(), this.getClass());
		} catch (UnsupportedEncodingException e) {
			throw new MaltEvalException(e.getMessage(), this.getClass());
		}
	}

	public void readTabFile(InputStreamReader fileStream, boolean hasPostag, boolean hasKnownT, boolean hasHead, boolean hasDeprel,
			boolean hasKnownP, boolean hasChunk) throws MaltEvalException {
		readTabFile(new BufferedReader(fileStream), hasPostag, hasHead, hasDeprel);
	}

	public void readTabFile(StringBuffer buffer, boolean hasPostag, boolean hasHead, boolean hasDeprel) throws MaltEvalException {
		readTabFile(new BufferedReader(new CharArrayReader(buffer.toString().toCharArray())), hasPostag, hasHead, hasDeprel);
	}

	private void readTabFile(BufferedReader file, boolean hasPostag, boolean hasHead, boolean hasDeprel) throws MaltEvalException {
		StringTokenizer stLine;

		String fileLine;
		int head = 0, sentenceIndex = 0, lineIndex = 0;
		String wordForm = "", deprel = "", postag = "";
		Vector<MaltWord> tempWords = new Vector<MaltWord>();

		allPostagPresent = hasPostag;
		allHeadsPresent = hasHead;
		allDeprelPresent = hasDeprel;

		parseError = false;
		try {
			sentences = new Vector<MaltSentence>();
			sentenceIndex = 0;
			while ((fileLine = file.readLine()) != null) {
				lineIndex++;
				if (!fileLine.equals("")) {
					stLine = new StringTokenizer(fileLine, "\t\n");

					wordForm = stLine.nextToken();
					if (hasPostag) {
						postag = stLine.nextToken();
						if (!isPostagFileRead) {
							postags.add(postag);
						} else if (MaltTreebank.getPostagIndex(postags, postag) == -1) {
							parseError = true;
							throw new MaltEvalException("Postag error at line: " + lineIndex + ", word " + wordForm + ", postag " + postag,
									this.getClass());
						}
					}
					if (hasHead) {
						head = Integer.parseInt(stLine.nextToken());
						if (head < 0) {
							parseError = true;
							throw new MaltEvalException("Head error at line " + lineIndex + ", word " + wordForm + ". Head value " + head
									+ " not valid.", this.getClass());
						}
					}
					if (hasDeprel) {
						deprel = stLine.nextToken();
						if (!isDeprelFileRead) {
							deprels.add(deprel);
						} else if (MaltTreebank.getDeprelIndex(deprels, deprel) == -1) {
							parseError = true;
							throw new MaltEvalException("Deprel error at line " + lineIndex + ", word " + wordForm + ". Deprel value "
									+ deprel + " not valid", this.getClass());
						}
					}
					tempWords.add(new MaltWord(wordForm, postag, head, deprel));
				} else {
					sentences.add(new MaltSentence(tempWords, sentenceIndex + 1, "?", "?", "?", 0.0, 0.0));
					sentenceIndex++;
					tempWords = new Vector<MaltWord>();
				}
			}
			if (tempWords.size() > 0) {
				sentences.add(new MaltSentence(tempWords, sentenceIndex + 1, "?", "?", "?", 0.0, 0.0));
			}
		} catch (NoSuchElementException nsee) {
			parseError = true;
			throw new MaltEvalException("Error at  line " + lineIndex + ": an attribute is missing", this.getClass());
		} catch (IOException e) {
			throw new MaltEvalException(e.getMessage(), this.getClass());
		}
		// catch (Throwable e) {
		// System.err.println("Throwable");
		// System.out.print("(" + wordForm + ", " + sentenceIndex + ")");
		// e.printStackTrace();
		// System.exit(1);
		// }
		// return "Tab file is ok";
	}

	/**
	 * @return Returns the deprels.
	 */
	public TreeSet<String> getDeprels() {
		return deprels;
	}

	/**
	 * @return Returns the postags.
	 */
	public TreeSet<String> getPostags() {
		return postags;
	}

	/**
	 * @return Returns the sentences.
	 */
	public Vector<MaltSentence> getSentences() {
		return sentences;
	}

	/**
	 * @return Returns the parseError.
	 */
	public boolean isParseError() {
		return parseError;
	}

	/**
	 * @return Returns the allDeprelPresent.
	 */
	public boolean isAllDeprelPresent() {
		return allDeprelPresent;
	}

	/**
	 * @return Returns the allHeadsPresent.
	 */
	public boolean isAllHeadsPresent() {
		return allHeadsPresent;
	}

	/**
	 * @return Returns the allPostagPresent.
	 */
	public boolean isAllPostagPresent() {
		return allPostagPresent;
	}
}
