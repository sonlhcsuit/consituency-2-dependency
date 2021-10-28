/*
 * Created on 6 jul 2007
 */
package se.vxu.msi.malteval;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import se.vxu.msi.malteval.util.Pair;

/**
 * This class contains pretty much all variables used for controlling the settings of MaltEval, such as name of flag/evaluation file arguments.
 * 
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class MaltEvalConfig {
	private static HashMap<String, Pair<String>> formattingArguments;
	private static HashMap<String, String> parameterArguments;
	private static HashSet<String> excludeTypes;
	private static HashSet<String> intervalTypes;

	private static Vector<String> defaultAccuracyAttributes;
	private static Vector<String> defaultPrecisionAndRecallAttributes;
	private static Vector<String> defaultParserAndTreebankAccuracyAttributes;
	private static Vector<String> validAccuracyAttributes;
	private static Vector<String> validPrecisionAndRecallAttributes;
	private static Vector<String> validParserAndTreebankAccuracyAttributes;

	public static boolean MERGE_TABLES_VALUE = true;
	public static boolean MICRO_AVERAGE = false;
	public static boolean DEBUG_MODE = false;
	
	public static final String REMOVE_SENTENCES = "--remove";
	public enum RemoveSentences {None, Projective, NonProjective};
	public static RemoveSentences removeProjectiveTrees = RemoveSentences.None; 

	/** File arguments */
	public static final String SYSTEM_FILES = "-s";
	public static final String GOLD_FILES = "-g";
	public static final String EVAL_FILES = "-e";
	public static final String CHAR_SET_NAME = "--charset";
	public static final String SELECTION_FILE = "-f";
	public static final String CPOSTAG_FILE = "--postag";
	public static final String DEPREL_FILE = "--deprel";

	/** MaltTreebankViewer argument */
	public static final String VISUALIZATION = "-v";

	/** File formats */
	public static final String MALT_TAB = "malttab";
	public static final String MALT_XML = "maltxml";
	public static final String CONLL = "conll";

	/** Character set encoding */
	public static final String CHARACTER_SET_ENCODING = "UTF-8";

	public static final String MALT_TAB_EXT = ".tab";
	public static final String MALT_XML_EXT = ".xml";
	public static final String CONLL_EXT = ".conll";

	/** Scoring metric */
	public static final String METRIC = "Metric";
	public static final String DEFAULT_METRIC = "LAS";

	/** Group tokens by ... */
	public static final String GROUP_BY = "GroupBy";
	public static final String DEFAULT_GROUPING = "Token";
	public static final String GROUP_BY_WORD_FORM = "Wordform";

	/** Type of info to show */
	public static final String showWhatAttributes = "attributes";
	public static final String showAllAttributes = "all";

	public static final String accuracy = "accuracy";
	public static final String counter = "counter";
	public static final String correctcounter = "correctcounter";
	public static final String precision = "precision";
	public static final String recall = "recall";
	public static final String fscore = "fscore";
	public static final String parseraccuracy = "parseraccuracy";
	public static final String treebankaccuracy = "treebankaccuracy";
	public static final String parsercorrectcounter = "parsercorrectcounter";
	public static final String treebankcorrectcounter = "treebankcorrectcounter";
	public static final String parsercounter = "parsercounter";
	public static final String treebankcounter = "treebankcounter";
	public static final String exactmatch = "exactmatch";
	public static final String includedtokenscount = "includedtokenscount";
	public static final String sentencelength = "sentencelength";
	public static final String isparserconnected = "isparserconnected";
	public static final String istreebankconnected = "istreebankconnected";
	public static final String hasparsercycle = "hasparsercycle";
	public static final String hastreebankcycle = "hastreebankcycle";
	public static final String isparserprojective = "isparserprojective";
	public static final String istreebankprojective = "istreebankprojective";
	public static final String id = "id";

	/** Exclude types */
	public static final String excludeWordforms = "ExcludeWordforms";
	public static final String excludeLemma = "ExcludeLemmas";
	public static final String excludePostags = "ExcludePostags";
	public static final String excludeCpostags = "ExcludeCpostags";
	public static final String excludeFeats = "ExcludeFeats";
	public static final String excludeDeprels = "ExcludeDeprels";
	public static final String excludePdeprels = "ExcludePdeprels";

	public static final String excludeUnicodePunc = "ExcludeUnicodePunc";

	public static final String minSentenceLength = "MinSentenceLength";
	public static final String maxSentenceLength = "MaxSentenceLength";

	/** Formatting */
	public static final String PRINT_TABLE_HEADER_INFO = "--header-info";
	public static final String PRINT_TABLE_COLUMN_HEADING_INFO = "--column-header";
	public static final String PRINT_TABLE_ROW_HEADING_INFO = "--row-header";
	public static final String PRINT_DETAILS = "--details";
	public static final String SEPARATE_SETTINGS = "--hdr-file";
	public static final String COMPUTE_STATISTICAL_SIGNIFICANCE = "--stat";
	public static final String TAB_SEPARATION = "--tab";
	public static final String OUTPUT_DIRECTORY = "--output";
	public static final String DECIMAL_PATTERN = "--pattern";
	public static final String COMPUTE_CONFUSION_MATRIX = "--confusion-matrix";
	public static final String MERGE_TABLES = "--merge-tables";
	public static final String DEBUG = "--debug";
	public static final String MERGE_CROSSVALIDATION_FILES = "--micro-average";

	public static final String help = "--help";
	public static final String examples = "--examples";

	public static final String STDOUT = "STDOUT";

	private static void buildFormattingArguments() {
		formattingArguments = new HashMap<String, Pair<String>>();
		formattingArguments.put(MERGE_CROSSVALIDATION_FILES, new Pair<String>("<0|1>: merge files before evaluation for cross-validation (mirco-average)", "(defualt: 0)"));
		formattingArguments.put(PRINT_TABLE_HEADER_INFO, new Pair<String>("<0|1>: print table header info", "(defualt: 1)"));
		formattingArguments.put(PRINT_TABLE_COLUMN_HEADING_INFO, new Pair<String>("<0|1>: print table column heading info", "(defualt: GroupBy-specific)"));
		formattingArguments.put(PRINT_TABLE_ROW_HEADING_INFO, new Pair<String>("<0|1>: print table row heading info", "(defualt: GroupBy-specific)"));
		formattingArguments.put(PRINT_DETAILS, new Pair<String>("<0|1>: print details", "(default: 0)"));
		formattingArguments.put(SEPARATE_SETTINGS, new Pair<String>("<0|1>: create separate settings (i.e. hdr-) file", "(default: 0)"));
		formattingArguments.put(COMPUTE_STATISTICAL_SIGNIFICANCE, new Pair<String>(
				"<0|1>: compute statistical significance test, when possible", "(default: 0)"));
		formattingArguments.put(COMPUTE_CONFUSION_MATRIX, new Pair<String>("<0|1>: compute confusion matrix", "(default: 0)"));
		formattingArguments.put(MERGE_TABLES, new Pair<String>("<0|1>: if possible, merge results into fewer tables", "(default: 1)"));
		formattingArguments.put(TAB_SEPARATION, new Pair<String>("<0|1>: separate columns with tab", "(default: 0)"));
		formattingArguments.put(OUTPUT_DIRECTORY, new Pair<String>("<path> = output directory",
				"(default: \"STDOUT\". Note: STDOUT means Standard Output Stream)"));
		formattingArguments.put(DECIMAL_PATTERN, new Pair<String>("<pattern> = decimal pattern",
		"(help: \"http://java.sun.com/j2se/1.5.0/docs/api/java/text/DecimalFormat.html\")"));
		formattingArguments.put(DEBUG, new Pair<String>("<0|1>: enable debug information",
		"(default: 0)"));
	}

	public static boolean isFormattingArgument(String argument) {
		return formattingArguments.keySet().contains(argument);
	}

	public static void printFormattingArgumentsHelpTexts() {
		for (String argument : formattingArguments.keySet()) {
			System.out.println("\t" + argument + " " + formattingArguments.get(argument).getFirst() + "\n\t\t"
					+ formattingArguments.get(argument).getSecond());
		}
	}

	private static void buildParameterArguments() {
		parameterArguments = new HashMap<String, String>();
		parameterArguments.put(METRIC, "Type of evaluation metric");
		parameterArguments.put(GROUP_BY, "Evaluation is grouped by ...");
		parameterArguments.put(showWhatAttributes, "Attributes to show");
	}

	public static boolean isParameterArgument(String argument) {
		if (argument.length() < 2) {
			return false;
		} else {
			return argument.startsWith("--") && parameterArguments.keySet().contains(argument.substring(2));
		}
	}

	private static void buildExcludeType() {
		excludeTypes = new HashSet<String>();
		excludeTypes.add(excludeWordforms);
		excludeTypes.add(excludeLemma);
		excludeTypes.add(excludePostags);
		excludeTypes.add(excludeCpostags);
		excludeTypes.add(excludeFeats);
		excludeTypes.add(excludeDeprels);
		excludeTypes.add(excludePdeprels);
	}

	public static boolean isExcludeType(String type) {
		if (type.length() < 2) {
			return false;
		} else {
			return type.startsWith("--") && excludeTypes.contains(type.substring(2));
		}
	}

	public static boolean isExcludeUnicodePunctuation(String type) {
		if (type.length() < 2) {
			return false;
		} else {
			return type.startsWith("--") && excludeUnicodePunc.equals(type.substring(2));
		}
	}

	public static boolean isUnicodePunctuation(String wordform) {
		int i;
		for (i = 0; i < wordform.length(); i++) {
			int unicodeType = Character.getType(wordform.charAt(i));
			if (!(unicodeType == Character.CONNECTOR_PUNCTUATION || unicodeType == Character.DASH_PUNCTUATION
					|| unicodeType == Character.END_PUNCTUATION || unicodeType == Character.FINAL_QUOTE_PUNCTUATION
					|| unicodeType == Character.INITIAL_QUOTE_PUNCTUATION || unicodeType == Character.OTHER_PUNCTUATION || unicodeType == Character.START_PUNCTUATION)) {
				return false;
			}
		}
		return true;
	}

	private static void buildIntervalType() {
		intervalTypes = new HashSet<String>();
		intervalTypes.add(minSentenceLength);
		intervalTypes.add(maxSentenceLength);
	}

	public static boolean isIntervalType(String type) {
		if (type.length() < 2) {
			return false;
		} else {
			return type.startsWith("--") && intervalTypes.contains(type.substring(2));
		}
	}

	public static boolean isVizualizationFlag(String type) {
		return type.equals(VISUALIZATION);
	}

	private static void buildAttibutes() {
		validAccuracyAttributes = new Vector<String>();
		validAccuracyAttributes.add(MaltEvalConfig.accuracy);
		validAccuracyAttributes.add(MaltEvalConfig.counter);
		validAccuracyAttributes.add(MaltEvalConfig.correctcounter);

		defaultAccuracyAttributes = new Vector<String>();
		defaultAccuracyAttributes.add(MaltEvalConfig.accuracy);

		validPrecisionAndRecallAttributes = new Vector<String>();
		validPrecisionAndRecallAttributes.add(MaltEvalConfig.precision);
		validPrecisionAndRecallAttributes.add(MaltEvalConfig.recall);
		validPrecisionAndRecallAttributes.add(MaltEvalConfig.fscore);
		validPrecisionAndRecallAttributes.add(MaltEvalConfig.parsercounter);
		validPrecisionAndRecallAttributes.add(MaltEvalConfig.treebankcounter);
		validPrecisionAndRecallAttributes.add(MaltEvalConfig.correctcounter);

		defaultPrecisionAndRecallAttributes = new Vector<String>();
		defaultPrecisionAndRecallAttributes.add(MaltEvalConfig.precision);
		defaultPrecisionAndRecallAttributes.add(MaltEvalConfig.recall);
		defaultPrecisionAndRecallAttributes.add(MaltEvalConfig.fscore);

		validParserAndTreebankAccuracyAttributes = new Vector<String>();
		validParserAndTreebankAccuracyAttributes.add(MaltEvalConfig.parseraccuracy);
		validParserAndTreebankAccuracyAttributes.add(MaltEvalConfig.treebankaccuracy);
		validParserAndTreebankAccuracyAttributes.add(MaltEvalConfig.parsercounter);
		validParserAndTreebankAccuracyAttributes.add(MaltEvalConfig.treebankcounter);
		validParserAndTreebankAccuracyAttributes.add(MaltEvalConfig.parsercorrectcounter);
		validParserAndTreebankAccuracyAttributes.add(MaltEvalConfig.treebankcorrectcounter);

		defaultParserAndTreebankAccuracyAttributes = new Vector<String>();
		defaultParserAndTreebankAccuracyAttributes.add(MaltEvalConfig.parseraccuracy);
		defaultParserAndTreebankAccuracyAttributes.add(MaltEvalConfig.treebankaccuracy);
}

	public static void init() {
		buildFormattingArguments();
		buildParameterArguments();
		buildExcludeType();
		buildIntervalType();
		buildAttibutes();
	}

	/**
	 * Returns the default attribute values if the grouping strategy can compute plain accuracy, when there is no need to split the accuracy into precision and recall.
	 * 
	 * @return the default attribute values if the grouping strategy can compute plain accuracy
	 */
	@SuppressWarnings("unchecked")
	public static Vector<String> getDefaultAccuracyAttributes() {
		return (Vector<String>) defaultAccuracyAttributes.clone();
	}

	/**
	 * Returns the default attribute values if the grouping strategy can compute precision and recall, when there is a need to split the accuracy into precision and recall and not just plain accuracy.
	 * 
	 * @return the default attribute values if the grouping strategy can compute precision and recall
	 */
	@SuppressWarnings("unchecked")
	public static Vector<String> getDefaultPrecisionAndRecallAttributes() {
		return (Vector<String>) defaultPrecisionAndRecallAttributes.clone();
	}

	@SuppressWarnings("unchecked")
	public static Vector<String> getDefaultParserAndTreebankAccuracyAttributes() {
		return (Vector<String>) defaultParserAndTreebankAccuracyAttributes.clone();
	}

	/**
	 * Returns the all valid attribute values if the grouping strategy can compute plain accuracy, when there is no need to split the accuracy into precision and recall.
	 * 
	 * @return the all valid attribute values if the grouping strategy can compute plain accuracy
	 */
	@SuppressWarnings("unchecked")
	public static Vector<String> getValidAccuracyAttributes() {
		return (Vector<String>) validAccuracyAttributes.clone();
	}

	/**
	 * Returns the all valid attribute values if the grouping strategy can compute precision and recall, when there is a need to split the accuracy into precision and recall and not just plain accuracy.
	 * 
	 * @return the all valid attribute values if the grouping strategy can compute precision and recall
	 */
	@SuppressWarnings("unchecked")
	public static Vector<String> getValidPrecisionAndRecallAttributes() {
		return (Vector<String>) validPrecisionAndRecallAttributes.clone();
	}

	@SuppressWarnings("unchecked")
	public static Vector<String> getValidParserAndTreebankAccuracyAttributes() {
		return (Vector<String>) validParserAndTreebankAccuracyAttributes.clone();
	}
}
