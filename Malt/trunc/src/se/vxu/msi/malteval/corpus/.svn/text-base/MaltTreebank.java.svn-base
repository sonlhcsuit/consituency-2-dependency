package se.vxu.msi.malteval.corpus;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.XMLReader;

import se.vxu.msi.malteval.MaltEvalConfig;
import se.vxu.msi.malteval.datareaders.ReadCoNLL;
import se.vxu.msi.malteval.datareaders.ReadMaltTab;
import se.vxu.msi.malteval.datareaders.ReadMaltXML;
import se.vxu.msi.malteval.exceptions.MaltEvalException;

/**
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class MaltTreebank {
	private ReadMaltTab readMaltTab;
	private ReadCoNLL readCoNLL;
	private String fileName;

	private TreeSet<String> postags;
	private TreeSet<String> deprels;
	private Vector<MaltSentence> sentences_;
	private boolean allPostagPresent_;
	private boolean allHeadsPresent_;
	private boolean allDeprelPresent_;
	private boolean hasPostag_;
	private boolean hasHead_;
	private boolean hasDeprel_;
	private boolean parseError_;

	public MaltTreebank(StringBuffer buffer, String bufferFormat, String postagFilename, String deprelFilename, boolean hasPostag,
			boolean hasHead, boolean hasDeprel) throws MaltEvalException {
		super();
		if (bufferFormat.equals(MaltEvalConfig.MALT_TAB)) {
			init(bufferFormat, postagFilename, deprelFilename, hasPostag, hasHead, hasDeprel);
			importCorpus(buffer, bufferFormat);
		} else {
			throw new MaltEvalException("The supported treebank format (MaltTAB) for StringBuffer is not selected", this.getClass());
		}
	}

	public MaltTreebank(String fn, String fileNameFormat, String postagFilename, String deprelFilename, boolean hasPostag,
			boolean hasHead, boolean hasDeprel) throws MaltEvalException {
		super();
		if (fileNameFormat.equals(MaltEvalConfig.MALT_TAB) || fileNameFormat.equals(MaltEvalConfig.CONLL)) {
			init(fileNameFormat, postagFilename, deprelFilename, hasPostag, hasHead, hasDeprel);
		}
		fileName = fn;
		importCorpus(fn, fileNameFormat);
	}

	public MaltTreebank(InputStreamReader fileStream, String fileNameFormat, String postagFilename, String deprelFilename,
			boolean hasPostag, boolean hasHead, boolean hasDeprel) throws MaltEvalException {
		super();
		if (fileNameFormat.equals(MaltEvalConfig.MALT_TAB) || fileNameFormat.equals(MaltEvalConfig.CONLL)) {
			init(fileNameFormat, postagFilename, deprelFilename, hasPostag, hasHead, hasDeprel);
		}
		importCorpus(fileStream, fileNameFormat);
	}

	private void init(String fileNameFormat, String postagFilename, String deprelFilename, boolean hasPostag, boolean hasHead,
			boolean hasDeprel) {
		allPostagPresent_ = true;
		allHeadsPresent_ = true;
		allDeprelPresent_ = true;
		this.hasPostag_ = hasPostag;
		this.hasHead_ = hasHead;
		this.hasDeprel_ = hasDeprel;
		parseError_ = true;
		if (fileNameFormat.equals(MaltEvalConfig.MALT_TAB)) {
			readMaltTab = new ReadMaltTab();
			if (postagFilename != null) {
				postags = readMaltTab.readTabPostagFile(postagFilename);
			}
			if (deprelFilename != null) {
				deprels = readMaltTab.readTabDeprelFile(deprelFilename);
			}
		} else {
			readCoNLL = new ReadCoNLL();
			if (postagFilename != null) {
				postags = readCoNLL.readTabPostagFile(postagFilename);
			}
			if (deprelFilename != null) {
				deprels = readCoNLL.readTabDeprelFile(deprelFilename);
			}
		}
	}

	protected void importCorpus(String fileName, String format) throws MaltEvalException {
		if (format.equals(MaltEvalConfig.MALT_TAB)) {
			readMaltTab.readTabFile(fileName, hasPostag_, hasHead_, hasDeprel_);
			initImportCorpus(format);
		} else if (format.equals(MaltEvalConfig.CONLL)) {
			readCoNLL.readTabFile(fileName);
			initImportCorpus(format);
		} else if (format.equals(MaltEvalConfig.MALT_XML)) {
			SAXParserFactory saxParserFactory;
			SAXParser saxParser;
			XMLReader parser;
			resetDataStructures();
			saxParserFactory = SAXParserFactory.newInstance();
			try {
				saxParser = saxParserFactory.newSAXParser();
				parser = saxParser.getXMLReader();
				parser.setContentHandler(new ReadMaltXML(sentences_, postags, deprels));
				parser.parse(new org.xml.sax.InputSource(fileName));
			} catch (Exception e) {
				throw new MaltEvalException(e.getMessage(), this.getClass());
			}
		}
	}

	protected void importCorpus(InputStreamReader fileStream, String format) throws MaltEvalException {
		if (format.equals(MaltEvalConfig.MALT_TAB)) {
			readMaltTab.readTabFile(fileStream, hasPostag_, false, hasHead_, hasDeprel_, false, false);
			initImportCorpus(format);
		} else if (format.equals(MaltEvalConfig.CONLL)) {
			readCoNLL.readTabFile(fileStream);
			initImportCorpus(format);
		} else if (format.equals(MaltEvalConfig.MALT_XML)) {
			SAXParserFactory saxParserFactory;
			SAXParser saxParser;
			XMLReader parser;
			resetDataStructures();
			saxParserFactory = SAXParserFactory.newInstance();
			try {
				saxParser = saxParserFactory.newSAXParser();
				parser = saxParser.getXMLReader();
				parser.setContentHandler(new ReadMaltXML(sentences_, postags, deprels));
				parser.parse(new org.xml.sax.InputSource(fileStream));
			} catch (Exception e) {
				throw new MaltEvalException(e.getMessage(), this.getClass());
			}
		}
	}

	protected void importCorpus(StringBuffer buffer, String format) throws MaltEvalException {
		readMaltTab.readTabFile(buffer, hasPostag_, hasHead_, hasDeprel_);
		initImportCorpus(format);
	}

	private void initImportCorpus(String format) {
		if (format.equals(MaltEvalConfig.MALT_TAB)) {
			allPostagPresent_ = readMaltTab.isAllPostagPresent();
			allDeprelPresent_ = readMaltTab.isAllDeprelPresent();
			allHeadsPresent_ = readMaltTab.isAllHeadsPresent();
			parseError_ = readMaltTab.isParseError();
			sentences_ = readMaltTab.getSentences();
			if (postags == null) {
				postags = readMaltTab.getPostags();
			}
			if (deprels == null) {
				deprels = readMaltTab.getDeprels();
			}
		} else {
			allPostagPresent_ = true;
			allDeprelPresent_ = true;
			allHeadsPresent_ = true;
			parseError_ = readCoNLL.isParseError();
			sentences_ = readCoNLL.getSentences();
			if (postags == null) {
				postags = readCoNLL.getPostags();
			}
			if (deprels == null) {
				deprels = readCoNLL.getDeprels();
			}
		}
	}

	public int getSentenceCount() {
		if (sentences_ == null) {
			return 0;
		} else {
			return sentences_.size();
		}
	}

	private void resetDataStructures() {
		sentences_ = new Vector<MaltSentence>();
		postags = new TreeSet<String>();
		deprels = new TreeSet<String>();
	}

	public boolean isAllPostagPresent() {
		return allPostagPresent_;
	}

	public boolean isAllHeadPresent() {
		return allHeadsPresent_;
	}

	public boolean isAllDeprelPresent() {
		return allDeprelPresent_;
	}

	public MaltSentence getSentence(int index) {
		if (!parseError_ && (index >= 0 && index < sentences_.size())) {
			return (MaltSentence) sentences_.elementAt(index);
		} else {
			return null;
		}
	}

	public String getPostag(int index) {
		String[] temp = (String[]) postags.toArray(new String[0]);
		if (index >= 0 && index < temp.length) {
			return temp[index];
		} else {
			return null;
		}
	}

	public String[] getPostagsAsArray() {
		return (String[]) postags.toArray(new String[0]);
	}

	public TreeSet<String> getPostags() {
		return postags;
	}

	static public int getPostagIndex(TreeSet<String> postags, String pos) {
		String[] temp = (String[]) postags.toArray(new String[0]);
		if (temp != null) {
			for (int i = 0; i < temp.length; i++) {
				if (temp[i].equals(pos)) {
					return i;
				}
			}
		}
		return -1;
	}

	public String getDeprel(int index) {
		String[] temp = (String[]) deprels.toArray(new String[0]);
		if (index >= 0 && index < temp.length) {
			return temp[index];
		} else {
			return null;
		}
	}

	public String[] getDeprelsAsArray() {
		return (String[]) deprels.toArray(new String[0]);
	}

	public TreeSet<String> getDeprels() {
		return deprels;
	}

	static public int getDeprelIndex(TreeSet<String> deprels, String deprel) {
		String[] temp = (String[]) deprels.toArray(new String[0]);
		if (temp != null) {
			for (int i = 0; i < temp.length; i++) {
				if (temp[i].equals(deprel)) {
					return i;
				}
			}
		}
		return -1;
	}

	public int getNumberOfPostags() {
		if (postags == null)
			return 0;
		else
			return postags.size();
	}

	public int getNumberOfDeprels() {
		if (deprels == null)
			return 0;
		else
			return deprels.size();
	}

	public int getLongestSentence(int min, int max) {
		int i, longest = -1;
		for (i = 0; i < sentences_.size(); i++) {
			if (!getSentence(1).discard(min, max) && getSentence(i).getSentenceLength() > longest) {
				longest = getSentence(i).getSentenceLength();
			}
		}
		return longest;
	}

	public double getMedianSentenceLength() {
		int i;
		int[] l = new int[getSentenceCount()];
		for (i = 0; i < getSentenceCount(); i++) {
			l[i] = getSentence(i).getSentenceLength();
		}
		Arrays.sort(l);
		return l.length % 2 == 0 ? (double) (l[l.length / 2 - 1] + l[l.length / 2]) / 2.0 : l[l.length / 2];
	}

	public int getNumberOfDiscardedSentences(int min, int max) {
		int i, discarded = 0;
		for (i = 0; i < getSentenceCount(); i++) {
			if (getSentence(i).discard(min, max)) {
				discarded++;
			}
		}
		return discarded;
	}

	public String getFileName() {
		return fileName;
	}
	
	public void addTreebank(MaltTreebank maltTreebank) {
		int i;
		postags.addAll(maltTreebank.getPostags());
		deprels.addAll(maltTreebank.getDeprels());
		for (i = 0; i < maltTreebank.getSentenceCount(); i++) {
			maltTreebank.getSentence(i).setId(sentences_.lastElement().getId() + 1);
			sentences_.add(maltTreebank.getSentence(i));
		}
	}
	
	public void removeSentene(int index) {
		sentences_.remove(index);
	}
}