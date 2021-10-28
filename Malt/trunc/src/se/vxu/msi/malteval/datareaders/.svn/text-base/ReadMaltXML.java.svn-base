/*
 * Created on 2004-apr-01
 */
package se.vxu.msi.malteval.datareaders;

import java.util.TreeSet;
import java.util.Vector;

import se.vxu.msi.malteval.corpus.MaltSentence;
import se.vxu.msi.malteval.corpus.MaltWord;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * An object of this class reads a MALT XML-file.
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class ReadMaltXML extends DefaultHandler implements ContentHandler {
	private Locator locator;

	private Vector<MaltSentence> sentences;
	private TreeSet<String> postags;
	private TreeSet<String> deprels;

	private boolean allPostagPresent;
	private boolean allHeadsPresent;
	private boolean allDeprelPresent;
	private boolean allKnownTPresent;
	private boolean allKnownPPresent;
	private boolean allChunkPresent;

	private int treebankStatus, headStatus, annotationStatus, attributeStatus, valueStatus, bodyStatus, sentenceStatus, wordStatus;
	private String currentAttribute;

	// some auxiliary variables
	private Vector<MaltWord> currentSentence;
	private String theParser, theUser, theDate;

	// new sentence attribute
	private double theScore, theTime;

	private int theId;

	/**
	 * This constructor sets the variables sentences, postags and delrels.
	 */
	public ReadMaltXML(Vector<MaltSentence> s, TreeSet<String> pt, TreeSet<String> dr) {
		sentences = s;
		postags = pt;
		deprels = dr;
	}

	/**
	 * Keeps track of the line number if it encounters an error.
	 */
	public void setDocumentLocator(Locator l) {
		locator = l;
	}

	/**
	 * Executed once just before the dokument is about to be parsed. It initializes a number of vaiables used durung parsing.
	 */
	public void startDocument() {
		treebankStatus = -1;
		headStatus = -1;
		annotationStatus = -1;
		attributeStatus = -1;
		valueStatus = -1;
		bodyStatus = -1;
		sentenceStatus = -1;
		wordStatus = -1;
		currentAttribute = "";

		allPostagPresent = true;
		allHeadsPresent = true;
		allDeprelPresent = true;
		allKnownTPresent = true;
		allKnownPPresent = true;
		allChunkPresent = true;
	}

	/**
	 * Envoked when a start element is found. Depending on the name of the element, different things happen. Make sure that the xml file conforms with the MALT XML-format. An exception with an error massage will otherwise be raised.
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equals("treebank")) {
			if (treebankStatus != -1) {
				throw new SAXParseException("treebank element at the wrong place", locator);
			}
			treebankStatus = 0;
		} else if (qName.equals("head")) {
			if (treebankStatus != 0 || headStatus != -1 || bodyStatus != -1) {
				throw new SAXParseException("head element at the wrong place", locator);
			}
			headStatus = 0;
		} else if (qName.equals("annotation")) {
			if (headStatus != 0 || annotationStatus != -1) {
				throw new SAXParseException("annoation element at the wrong place", locator);
			}
			annotationStatus = 0;
		} else if (qName.equals("attribute")) {
			if (annotationStatus != 0 || attributeStatus == 0) {
				throw new SAXParseException("attribute element at the wrong place", locator);
			}
			if ((currentAttribute = attributes.getValue("name")) == null) {
				throw new SAXParseException("attribute \"name\" is missing", locator);
			}
			attributeStatus = 0;
		} else if (qName.equals("value")) {
			if (attributeStatus != 0 || valueStatus == 0) {
				throw new SAXParseException("value element at the wrong place", locator);
			}
			String theValue = attributes.getValue("name");
			if (theValue == null) {
				throw new SAXParseException("attribute \"name\" is missing", locator);
			}
			if (currentAttribute.equals("postag")) {
				postags.add(theValue);
			} else if (currentAttribute.equals("deprel")) {
				deprels.add(theValue);
			}
			valueStatus = 0;
		} else if (qName.equals("body")) {
			if (treebankStatus != 0 || headStatus != 1 || bodyStatus != -1) {
				throw new SAXParseException("body element at the wrong place", locator);
			}
			bodyStatus = 0;
		} else if (qName.equals("sentence")) {
			if (bodyStatus != 0 || sentenceStatus == 0) {
				throw new SAXParseException("sentence element at the wrong place", locator);
			}
			try {
				theId = Integer.parseInt(attributes.getValue("id"));
			} catch (NumberFormatException nfe) {
				theId = 0;
			}
			if ((theParser = attributes.getValue("parser")) == null) {
				theParser = "";
			}
			if ((theUser = attributes.getValue("user")) == null) {
				theUser = "";
			}
			if ((theDate = attributes.getValue("date")) == null) {
				theDate = "";
			}
			if (attributes.getValue("score") == null) {
				theScore = 0.0;
			} else {
				try {
					theScore = Double.parseDouble(attributes.getValue("score"));
				} catch (NumberFormatException nfe) {
					throw new SAXParseException("attribute \"score\" could not be interpreted as floating value", locator);
				}
			}
			if (attributes.getValue("time") == null) {
				theTime = 0.0;
			} else {
				try {
					theTime = Double.parseDouble(attributes.getValue("time"));
				} catch (NumberFormatException nfe) {
					throw new SAXParseException("attribute \"time\" could not be interpreted as floating value", locator);
				}
			}

			currentSentence = new Vector<MaltWord>();
			sentenceStatus = 0;
		} else if (qName.equals("word")) {
			if (sentenceStatus != 0 || wordStatus == 0) {
				throw new SAXParseException("word element at the wrong place", locator);
			}
			String theForm, theLemma, theCpostag, thePostag, theFeats, theDeprel, thePDeprel;
			int theHead = -1, thePHead = -1;
			if ((theForm = attributes.getValue("form")) == null) {
				throw new SAXParseException("attribute \"form\" is missing", locator);
			}
			theLemma = attributes.getValue("lemma");
			theFeats = attributes.getValue("feats");
			theCpostag = attributes.getValue("cpostag");
			if ((thePostag = attributes.getValue("postag")) == null) {
				allPostagPresent = false;
				throw new SAXParseException("attribute \"postag\" is missing", locator);
			}
			if (!postags.contains(thePostag)) {
				throw new SAXParseException("postag not in postag set", locator);
			}
			theFeats = attributes.getValue("feats");
			if (attributes.getValue("head") == null) {
				throw new SAXParseException("attribute \"head\" is missing", locator);
			} else {
				try {
					theHead = Integer.parseInt(attributes.getValue("head"));
					if (theHead < 0) {
						throw new SAXParseException("attribute \"head\" is not correct", locator);
					}
				} catch (NumberFormatException nfe) {
					throw new SAXParseException("attribute \"head\" could not be interpreted", locator);
				}
			}
			if ((theDeprel = attributes.getValue("deprel")) == null) {
				allDeprelPresent = false;
				theDeprel = "";
			}
			if (!deprels.contains(theDeprel)) {
				throw new SAXParseException("deprel not in deprel set", locator);
			}
			if (attributes.getValue("phead") != null) {
				if (!attributes.getValue("phead").equals("_")) {
					try {
						thePHead = Integer.parseInt(attributes.getValue("phead"));
						if (thePHead < 0) {
							throw new SAXParseException("attribute \"phead\" is not correct", locator);
						}
					} catch (NumberFormatException nfe) {
						throw new SAXParseException("attribute \"phead\" could not be interpreted", locator);
					}
				} else {
					thePHead = 0;
				}
			}
			thePDeprel = attributes.getValue("pdeprel");
			currentSentence.add(new MaltWord(theForm, theLemma, theCpostag, thePostag, theFeats, theHead, theDeprel, thePHead, thePDeprel));
			wordStatus = 0;
		} else {
			throw new SAXParseException("attribute \"" + qName + "\" is not part of MALT XML", locator);
		}
	}

	/**
	 * This method is the counterpart of the startElement method. Will also raise an exception if the xml file does not comform with the MALT XML-format.
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equals("treebank")) {
			if (treebankStatus != 0) {
				throw new SAXParseException("treebank element at the wrong place", locator);
			}
			treebankStatus = 1;
		} else if (qName.equals("head")) {
			if (headStatus != 0) {
				throw new SAXParseException("head element at the wrong place", locator);
			}
			headStatus = 1;
		} else if (qName.equals("annotation")) {
			if (annotationStatus != 0) {
				throw new SAXParseException("annoation element at the wrong place", locator);
			}
			annotationStatus = 1;
		} else if (qName.equals("attribute")) {
			if (attributeStatus != 0) {
				throw new SAXParseException("attribute element at the wrong place", locator);
			}
			attributeStatus = 1;
		} else if (qName.equals("value")) {
			if (valueStatus != 0) {
				throw new SAXParseException("value element at the wrong place", locator);
			}
			valueStatus = 1;
		} else if (qName.equals("body")) {
			if (bodyStatus != 0) {
				throw new SAXParseException("body element at the wrong place", locator);
			}
			bodyStatus = 1;
		} else if (qName.equals("sentence")) {
			if (sentenceStatus != 0) {
				throw new SAXParseException("sentence element at the wrong place", locator);
			}
			sentences.add(new MaltSentence(currentSentence, theId, theParser, theUser, theDate, theScore, theTime));
			sentenceStatus = 1;
		} else if (qName.equals("word")) {
			if (wordStatus != 0) {
				throw new SAXParseException("word element at the wrong place", locator);
			}
			wordStatus = 1;
		}
	}

	public boolean isAllPostagPresent() {
		return allPostagPresent;
	}

	public boolean isAllHeadPresent() {
		return allHeadsPresent;
	}

	public boolean isAllDeprelPresent() {
		return allDeprelPresent;
	}

	public boolean isAllKnownTPresent() {
		return allKnownTPresent;
	}

	public boolean isAllKnownPPresent() {
		return allKnownPPresent;
	}

	public boolean isAllChunkPresent() {
		return allChunkPresent;
	}
}
