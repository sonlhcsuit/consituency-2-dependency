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
public class ReadCoNLL {
	static private String charSetName = MaltEvalConfig.CHARACTER_SET_ENCODING;

	private boolean parseError;

	private Vector<MaltSentence> sentences;
	private TreeSet<String> postags;
	private TreeSet<String> deprels;

	private boolean isPostagFileRead;
	private boolean isDeprelFileRead;
	
	private String fileName_;

	public ReadCoNLL() {
		parseError = true;
		sentences = null;
		postags = null;
		deprels = null;
		fileName_ = null;

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

		fileName_ = fileName;
		
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

	public void readTabFile(String filename) throws MaltEvalException {
		try {
			readTabFile(new BufferedReader(new InputStreamReader(new FileInputStream(filename), charSetName)));
		} catch (FileNotFoundException e) {
			throw new MaltEvalException(e.getMessage(), this.getClass());
		} catch (UnsupportedEncodingException e) {
			throw new MaltEvalException(e.getMessage(), this.getClass());
		}
	}

	public void readTabFile(InputStreamReader fileStream) throws MaltEvalException {
		readTabFile(new BufferedReader(fileStream));
	}

	public void readTabFile(StringBuffer buffer) throws MaltEvalException {
		readTabFile(new BufferedReader(new CharArrayReader(buffer.toString().toCharArray())));
	}

	private void readTabFile(BufferedReader file) throws MaltEvalException {
		StringTokenizer stLine;

		String fileLine = null;
		int sentenceIndex = 0, lineIndex = 0;
		Vector<MaltWord> tempWords = new Vector<MaltWord>();

		// Required attributes: ID, FORM, CPOSTAG, POSTAG, HEAD and DEPREL
		String FORM, CPOSTAG, HEAD = null, DEPREL;
		@SuppressWarnings("unused")
		String ID, POSTAG;
		int HEADint = -1, PHEADint = -1;
		// Optional: LEMMA, FEATS, PHEAD, PDEPREL
		String LEMMA, FEATS, PDEPREL, PHEAD;

		parseError = false;
		try {
			sentences = new Vector<MaltSentence>();
			sentenceIndex = 0;
			while ((fileLine = file.readLine()) != null) {
				lineIndex++;
				if (!fileLine.equals("")) {
					stLine = new StringTokenizer(fileLine, "\t\n");

					ID = stLine.nextToken();
					FORM = stLine.nextToken();
					LEMMA = stLine.nextToken();
					CPOSTAG = stLine.nextToken();
					POSTAG = stLine.nextToken();
					if (!isPostagFileRead) {
						postags.add(POSTAG);
					} else if (MaltTreebank.getPostagIndex(postags, POSTAG) == -1) {
						parseError = true;
						throw new MaltEvalException("CPOSTAG error at line: " + lineIndex + ", word: " + FORM + ", POSTAG: " + POSTAG + (fileName_ != null ? " in " + fileName_: ""),
								this.getClass());
					}
					FEATS = stLine.nextToken();
					HEAD = stLine.nextToken();
					try {
						HEADint = Integer.parseInt(HEAD);
					} catch (NumberFormatException nfe) {
						throw new MaltEvalException("Head error at line " + lineIndex + ", word " + FORM + ". Head value " + HEAD
								+ " not valid " + (fileName_ != null ? " in " + fileName_: ""), this.getClass());
					}
					if (HEADint < 0) {
						parseError = true;
						throw new MaltEvalException("Head error at line " + lineIndex + ", word " + FORM + ". Head value " + HEAD
								+ " not valid " + (fileName_ != null ? " in " + fileName_: ""), this.getClass());
					}
					DEPREL = stLine.nextToken();
					if (!isDeprelFileRead) {
						deprels.add(DEPREL);
					} else if (MaltTreebank.getDeprelIndex(deprels, DEPREL) == -1) {
						parseError = true;
						throw new MaltEvalException("Deprel error at line " + lineIndex + ", word " + FORM + ". Deprel value " + DEPREL
								+ " not valid " + (fileName_ != null ? " in " + fileName_: ""), this.getClass());
					}
					try {
						PHEAD = stLine.nextToken();
					} catch (NoSuchElementException e) {
						PHEAD = "_";
					}
					try {
						PHEADint = Integer.parseInt(PHEAD);
					} catch (NumberFormatException nfe) {
						if (!PHEAD.equals("_")) {
							throw new MaltEvalException("PHead error at line " + lineIndex + ", word " + FORM + ". PHead value " + HEAD
									+ " not valid " + (fileName_ != null ? " in " + fileName_: ""), this.getClass());
						}
					}
					if (HEADint < 0) {
						parseError = true;
						throw new MaltEvalException("PHEAD error at line " + lineIndex + ", word " + FORM + ". Head value " + PHEAD
								+ " not valid " + (fileName_ != null ? " in " + fileName_: ""), this.getClass());
					}
					try {
						PDEPREL = stLine.nextToken();
					} catch (NoSuchElementException e) {
						PDEPREL = "_";
					}
					tempWords.add(new MaltWord(FORM, LEMMA, CPOSTAG, POSTAG, FEATS, HEADint, DEPREL, PHEADint, PDEPREL));
				} else {
					sentences.add(new MaltSentence(tempWords, sentenceIndex + 1, "?", "?", "?", 0.0, 0.0));
					sentenceIndex++;
					tempWords = new Vector<MaltWord>();
				}
			}
			if (tempWords.size() > 0) {
				sentences.add(new MaltSentence(tempWords, sentenceIndex + 1, "?", "?", "?", 0.0, 0.0));
			}
		} catch (NumberFormatException nfe) {
			parseError = true;
			throw new MaltEvalException("Error at  line " + lineIndex + ": NumberFormatException.\n" + fileLine + (fileName_ != null ? " in " + fileName_: ""), this.getClass());
		} catch (NoSuchElementException nsee) {
			parseError = true;
			throw new MaltEvalException("Error at  line " + lineIndex + ": an attribute is missing\n" + fileLine + (fileName_ != null ? " in " + fileName_: ""), this.getClass());
		} catch (IOException e) {
			throw new MaltEvalException(e.getMessage(), this.getClass());
		}
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

}
