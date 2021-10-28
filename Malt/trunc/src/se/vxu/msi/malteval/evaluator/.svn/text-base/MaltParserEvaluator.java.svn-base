/*
 * Created on 2004-sep-03
 */
package se.vxu.msi.malteval.evaluator;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.zip.ZipFile;

import se.vxu.msi.malteval.MaltEvalConfig;
import se.vxu.msi.malteval.configdata.util.Evaluation;
import se.vxu.msi.malteval.configdata.util.Evaluator;
import se.vxu.msi.malteval.configdata.util.Feature;
import se.vxu.msi.malteval.configdata.util.Requirement;
import se.vxu.msi.malteval.corpus.MaltSentence;
import se.vxu.msi.malteval.corpus.MaltTreebank;
import se.vxu.msi.malteval.exceptions.MaltEvalException;
import se.vxu.msi.malteval.grouping.Grouping;
import se.vxu.msi.malteval.grouping.Sentence;
import se.vxu.msi.malteval.metric.Metric;
import se.vxu.msi.malteval.plugin.PluginLoader;
import se.vxu.msi.malteval.specialgrouping.grouping.ComplexGrouping;
import se.vxu.msi.malteval.util.DataContainer;
import se.vxu.msi.malteval.util.IntegerCounter;
import se.vxu.msi.malteval.util.Pair;

/**
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class MaltParserEvaluator extends AbstractMaltEvaluator {
	public static boolean COMPUTE_CONFUSION_MATRIX = false;

	private static HashMap<String, Vector<String>> validAttributes_;
	private String groupBy_;
	private String metric_;

	private HashMap<Integer, HashSet<String>> complexGrouping_ = null;

	private String sortBy_;
	private boolean ascendedSorting_;
	private int rowsToShow_;

	private int tokenIndex_;

	static private TreeMap<String, Grouping> groupByStrategy_;
	static private HashSet<String> complexGroupingTypes;

	static private TreeMap<String, Metric> metricStrategy_;

	public MaltParserEvaluator(StringBuffer processedDataBuffer, String processedDataBufferFormat, StringBuffer corpusDataBuffer,
			String corpusDataBufferFormat, String postagFilename, String deprelFilename) throws MaltEvalException {
		super(MaltParserEvaluator.constructMaltParserEvaluator(), processedDataBuffer, processedDataBufferFormat, corpusDataBuffer,
				corpusDataBufferFormat, postagFilename, deprelFilename);
	}

	public MaltParserEvaluator(String processedFileName, String processedFileFormat, StringBuffer corpusDataBuffer,
			String corpusDataBufferFormat, String postagFilename, String deprelFilename) throws MaltEvalException {
		super(MaltParserEvaluator.constructMaltParserEvaluator(), processedFileName, processedFileFormat, corpusDataBuffer,
				corpusDataBufferFormat, postagFilename, deprelFilename);
	}

	public MaltParserEvaluator(String processedFileName, String processedFileFormat, String corpusFileName, String corpusFileFormat,
			String postagFilename, String deprelFilename) throws MaltEvalException {
		super(MaltParserEvaluator.constructMaltParserEvaluator(), processedFileName, processedFileFormat, corpusFileName, corpusFileFormat,
				postagFilename, deprelFilename);
	}

	public MaltParserEvaluator(String processedFileName, String processedFileFormat, InputStreamReader corpusStream,
			String corpusFileFormat, String postagFilename, String deprelFilename) throws MaltEvalException {
		super(MaltParserEvaluator.constructMaltParserEvaluator(), processedFileName, processedFileFormat, corpusStream, corpusFileFormat,
				postagFilename, deprelFilename);
	}

	public MaltParserEvaluator(MaltTreebank processedData, MaltTreebank corpusData) throws MaltEvalException {
		super(MaltParserEvaluator.constructMaltParserEvaluator(), processedData, corpusData);
	}

	protected MaltTreebank importData(StringBuffer buffer, String format) throws MaltEvalException {
		return new MaltTreebank(buffer, format, postagFilename_, deprelFilename_, true, true, true);
	}

	protected MaltTreebank importData(String fileName, String format) throws MaltEvalException {
		return new MaltTreebank(fileName, format, postagFilename_, deprelFilename_, true, true, true);
	}

	protected MaltTreebank importData(InputStreamReader stream, String format) throws MaltEvalException {
		return new MaltTreebank(stream, format, postagFilename_, deprelFilename_, true, true, true);
	}

	public static void initialize() throws MaltEvalException {
		validAttributes_ = new HashMap<String, Vector<String>>();
		buildGroupingCombinations();
		buildMetricCombinations();
	}

	private void setAttributesToShow(String attributes) {
		StringTokenizer st = new StringTokenizer(attributes, "|");
		attributesToShow = new Vector<String>();
		while (st.hasMoreTokens()) {
			attributesToShow.add(st.nextToken());
		}
	}

	public DataContainer evaluate(Evaluation evaluation) throws MaltEvalException {
		Iterator<Feature> i;
		Feature f;
		String value, format;

		metric_ = null;
		sortBy_ = null;
		ascendedSorting_ = true;
		rowsToShow_ = -1;
		i = evaluator.getOptionFeatureIterator();
		while (i.hasNext()) {
			f = i.next();
			if ((value = evaluation.getParameterValue(f.getName())) == null) {
				value = f.getDefaultValue();
				format = f.getFormat();
			} else {
				format = evaluation.getParameterFormat(f.getName());
			}

			if (f.getName().equals(MaltEvalConfig.METRIC)) {
				if (metricStrategy_.keySet().contains(value) || groupByStrategy_.containsKey(value)) {
					metric_ = value;
				} else {
					throw new MaltEvalException("Incorrect value for " + MaltEvalConfig.METRIC + ": \"" + value
							+ "\". Available Metric values are " + metricStrategy_.keySet(), this.getClass());
				}
			} else if (f.getName().equals(MaltEvalConfig.GROUP_BY)) {
				if (parseComplexGrouping(value)) {
					if (isGroupingComplex()) {
						groupBy_ = value;
						groupByStrategy_.put(groupBy_, new ComplexGrouping());
						((ComplexGrouping) groupByStrategy_.get(groupBy_)).setComplexGrouping(complexGrouping_);
						if (format.equals("")) {
							// attributesToShow = ((ComplexGrouping) groupByStrategy_.get(groupBy_)).getDefaultAttributes();
						} else {
							setAttributesToShow(format);
						}
					} else {
						groupBy_ = complexGrouping_.values().iterator().next().iterator().next();
						if (format.equals("")) {
							// attributesToShow = groupByStrategy_.get(value).getDefaultAttributes();
						} else {
							setAttributesToShow(format);
						}
					}
				} else {
					throw new MaltEvalException("Incorrect value for " + MaltEvalConfig.GROUP_BY + ": \"" + value
							+ "\". Possible values are: " + groupByStrategy_.keySet(), this.getClass());
				}
			} else if (MaltEvalConfig.isExcludeType("--" + f.getName())) {
				setExcludeTypes(f.getName(), value);
			} else if (f.getName().equals(MaltEvalConfig.excludeUnicodePunc)) {
				if (value.equals("0") || value.equals("1")) {
					setExcludeUnicodePunc(value.equals("1"));
				} else {
					throw new MaltEvalException("Incorrect value for " + MaltEvalConfig.excludeUnicodePunc + ": \"" + value
							+ "\". Possible values are 0 and 1.", this.getClass());
				}
			} else if (f.getName().equals(MaltEvalConfig.minSentenceLength)) {
				setMinSentenceLength(value);
			} else if (f.getName().equals(MaltEvalConfig.maxSentenceLength)) {
				setMaxSentenceLength(value);
			} else {
				throw new MaltEvalException("Mismatching feature for MaltParseEvaluator: \"" + f.getName()
						+ "\". (Remove it from the evaluation file)", this.getClass());
			}
		}
		createShowWhatAttributes();
		checkShowWhatAttributes();
		DataContainer evaluationDataContainer = evaluate();
		if (sortBy_ != null) {
			evaluationDataContainer.sortRows(sortBy_, ascendedSorting_, rowsToShow_);
		}
		evaluationDataContainer.setEvaluation(evaluation);
		return evaluationDataContainer;
	}

	private void createShowWhatAttributes() {
		if (isGroupingComplex()) {
			for (Integer integer : complexGrouping_.keySet()) {
				Iterator<String> a = complexGrouping_.get(integer).iterator();
				while (a.hasNext()) {
					String g = a.next();
					validAttributes_.put(g, MaltEvalConfig.getValidAccuracyAttributes());
				}
			}
		} else if (groupByStrategy_.get(groupBy_).isSimpleAccuracyGrouping() || groupBy_.equals("Token")
				|| groupBy_.equals("SentenceAndToken")) {
			validAttributes_.put(groupBy_, MaltEvalConfig.getValidAccuracyAttributes());
		} else {
			if (groupBy_.equals("Sentence")) {
				validAttributes_.put(groupBy_, ((Sentence) groupByStrategy_.get(groupBy_)).getValidAttributes());
			} else if (metric_.equals(groupBy_) || (groupByStrategy_.get(groupBy_).correspondingMetric() != null && groupByStrategy_.get(groupBy_).correspondingMetric().contains(metric_))) {
				validAttributes_.put(groupBy_, MaltEvalConfig.getValidPrecisionAndRecallAttributes());
			} else {
				validAttributes_.put(groupBy_, MaltEvalConfig.getValidParserAndTreebankAccuracyAttributes());
			}
		}
		if (attributesToShow == null) {
			if (groupByStrategy_.get(groupBy_).isSimpleAccuracyGrouping() || groupBy_.equals("Token") || isGroupingComplex()) {
				attributesToShow = MaltEvalConfig.getDefaultAccuracyAttributes();
			} else {
				if (groupBy_.equals("Sentence")) {
					attributesToShow = ((Sentence) groupByStrategy_.get(groupBy_)).getDefaultAttributes();
				} else if (metric_.equals(groupBy_) || metric_.equals(groupByStrategy_.get(groupBy_).correspondingMetric())) {
					attributesToShow = MaltEvalConfig.getDefaultPrecisionAndRecallAttributes();
				} else {
					attributesToShow = MaltEvalConfig.getDefaultParserAndTreebankAccuracyAttributes();
				}
			}
		}
	}

	private boolean isGroupingComplex() {
		return complexGrouping_.size() > 1 || complexGrouping_.keySet().iterator().next() != 0
				|| complexGrouping_.get(complexGrouping_.keySet().iterator().next()).size() > 1;
	}

	private boolean parseComplexGrouping(String groupByString) {
		String[] offetArray;
		String[] offsetArray;
		String[] groupByArray;
		HashSet<String> attibutes;
		Integer integerPos;
		try {
			complexGrouping_ = new HashMap<Integer, HashSet<String>>();
			offetArray = groupByString.split("#");
			for (String offset : offetArray) {
				offsetArray = offset.split("@");
				if (offsetArray.length < 2) {
					integerPos = new Integer(0);
				} else {
					integerPos = new Integer(offsetArray[1]);
				}
				if ((attibutes = complexGrouping_.get(integerPos)) == null) {
					attibutes = new HashSet<String>();
				}
				groupByArray = offsetArray[0].split(",");
				for (String groupBy : groupByArray) {
					if ((offetArray.length == 1 && offsetArray.length == 1 && groupByArray.length == 1)
							&& groupByStrategy_.keySet().contains(groupBy)) {
						attibutes.add(groupBy);
					} else if (complexGroupingTypes.contains(groupBy)) {
						attibutes.add(groupBy);
					} else {
						return false;
					}
				}

				complexGrouping_.put(integerPos, attibutes);
			}
		} catch (Exception e) {
			complexGrouping_ = null;
			return false;
		}
		return true;
	}

	private void checkShowWhatAttributes() throws MaltEvalException {
		int index;
		String attribute;
		if (attributesToShow.size() == 0) {
			if (complexGrouping_.size() > 1) {
				attributesToShow.addAll(validAttributes_.get(MaltEvalConfig.GROUP_BY_WORD_FORM));
			} else {
				attributesToShow.addAll(validAttributes_.get(complexGrouping_.get(complexGrouping_.keySet().iterator().next()).iterator()
						.next()));
			}
		} else {
			for (Integer offset : complexGrouping_.keySet()) {
				for (int i = 0; i < attributesToShow.size(); i++) {
					attribute = attributesToShow.get(i);
					if ((index = attribute.indexOf("+")) != -1) {
						if (!attribute.endsWith("+")) {
							try {
								rowsToShow_ = Integer.parseInt(attribute.substring(index + 1));
							} catch (NumberFormatException numberFormatException) {
								throw new MaltEvalException("Incorrect value \"" + attribute
										+ "\" for a format attribute. Expected an integer value after \"+\".", this.getClass());
							}
						}
						attribute = attribute.substring(0, index);
						attributesToShow.set(i, attribute);
						sortBy_ = attribute;
						ascendedSorting_ = true;
					} else if ((index = attribute.indexOf("-")) != -1) {
						if (!attribute.endsWith("-")) {
							try {
								rowsToShow_ = Integer.parseInt(attribute.substring(index + 1));
							} catch (NumberFormatException numberFormatException) {
								throw new MaltEvalException("Incorrect value \"" + attribute
										+ "\" for a format attribute. Expected an integer value after \"-\".", this.getClass());
							}
						}
						attribute = attribute.substring(0, index);
						attributesToShow.set(i, attribute);
						sortBy_ = attribute;
						ascendedSorting_ = false;
					}
					if (!attributesToShow.contains(MaltEvalConfig.showAllAttributes)
							&& !validAttributes_.get(complexGrouping_.get(offset).iterator().next()).contains(attribute)) {
						throw new MaltEvalException("Incorrect attribute value \"" + attribute + "\" for grouping strategy \""
								+ complexGrouping_.get(offset).iterator().next() + "\". Possible values  are "
								+ validAttributes_.get(complexGrouping_.get(offset).iterator().next()) + ".", this.getClass());
					}
				}
			}
			if (attributesToShow.contains(MaltEvalConfig.showAllAttributes)) {
				attributesToShow.clear();
				if (complexGrouping_.size() > 1) {
					attributesToShow.addAll(validAttributes_.get(MaltEvalConfig.GROUP_BY_WORD_FORM));
				} else {
					attributesToShow.addAll(validAttributes_.get(complexGrouping_.get(complexGrouping_.keySet().iterator().next())
							.iterator().next()));
				}
			}
		}
	}

	private DataContainer evaluate() throws MaltEvalException {
		int i, j;
		IntegerCounter integerCounter;
		TreeMap<Object, IntegerCounter> counter;
		Object treebankKey, parsedKey;

		HashMap<Pair<Object>, Integer> confusionHashMap = new HashMap<Pair<Object>, Integer>();
		Pair<Object> confusionMatrixKey;
		TreeSet<Object> treebankKeys, parsedKeys;
		Integer confusionMatrixValue;

		tokenIndex_ = 1;
		counter = new TreeMap<Object, IntegerCounter>();
		treebankKeys = new TreeSet<Object>();
		parsedKeys = new TreeSet<Object>();
		for (i = 0; i < processedTrees.getSentenceCount(); i++) {
			if (!processedTrees.getSentence(i).discard(getMinSentenceLength(), getMaxSentenceLength())) {
				for (j = 0; j < processedTrees.getSentence(i).getSentenceLength(); j++) {
					treebankKey = null;
					parsedKey = null;
					if (!excludeType(corpusTrees.getSentence(i).getWord(j)) && !excludeUnicodePunc(corpusTrees.getSentence(i).getWord(j))) { // recall
						treebankKey = getKey(corpusTrees.getSentence(i), j, groupBy_);
						treebankKeys.add(treebankKey);
						if ((integerCounter = counter.get(treebankKey)) == null) {
							integerCounter = new IntegerCounter();
							counter.put(treebankKey, integerCounter);
						}
						integerCounter.incGsTotal();
						if (isCorrect(i, j)) {
							integerCounter.incGsCorrect();
						}
					}
					if (!excludeType(processedTrees.getSentence(i).getWord(j))
							&& !excludeUnicodePunc(processedTrees.getSentence(i).getWord(j))) { // precision
						parsedKey = getKey(processedTrees.getSentence(i), j, groupBy_);
						parsedKeys.add(parsedKey);
						if ((integerCounter = counter.get(parsedKey)) == null) {
							integerCounter = new IntegerCounter();
							counter.put(parsedKey, integerCounter);
						}
						integerCounter.incParsedTotal();
						if (isCorrect(i, j)) {
							integerCounter.incParsedCorrect();
						}
					}
					if (isConfusionMatrixComputable()) {
						if (treebankKey != null && parsedKey != null && !treebankKey.equals(parsedKey)) { // && treebankKey.toString().split(" ").length != parsedKey.toString().split(" ").length
							confusionMatrixKey = new Pair<Object>(parsedKey, treebankKey);
							if ((confusionMatrixValue = confusionHashMap.get(confusionMatrixKey)) == null) {
								confusionMatrixValue = new Integer(0);
							}
							confusionMatrixValue = confusionMatrixValue.intValue() + 1;
							confusionHashMap.put(confusionMatrixKey, confusionMatrixValue);
						}
					}
					tokenIndex_++;
				}
			}
		}
		// printing
		DataContainer evaluationDataContainer = createEvaluationDataContainer(counter);
		if (isConfusionMatrixComputable()) {
			evaluationDataContainer.setConfusionTable(creteConfusionTable(confusionHashMap));
			if (treebankKeys.size() * parsedKeys.size() < 2500) {
				evaluationDataContainer.setConfusionMatrix(creteConfusionMatrix(confusionHashMap, treebankKeys, parsedKeys));
			}
		}
		return evaluationDataContainer;
	}

	private boolean isConfusionMatrixComputable() {
		return COMPUTE_CONFUSION_MATRIX
/*				&& validAttributes_.get(complexGrouping_.get(complexGrouping_.keySet().iterator().next()).iterator().next()).contains(
						MaltEvalConfig.precision)
				&& validAttributes_.get(complexGrouping_.get(complexGrouping_.keySet().iterator().next()).iterator().next()).contains(
						MaltEvalConfig.recall)*/;
	}

	private DataContainer creteConfusionTable(HashMap<Pair<Object>, Integer> confusionMatrix) throws MaltEvalException {
		DataContainer evaluationDataContainer = new DataContainer(1, true, false, "Gold / System");
		evaluationDataContainer.setCaption("Confusion table for "
				+ complexGrouping_.get(complexGrouping_.keySet().iterator().next()).iterator().next());
		evaluationDataContainer.setColumnHeading("frequency");
		for (Pair<Object> pair : confusionMatrix.keySet()) {
			evaluationDataContainer.addRow(pair.getSecond() + " / " + pair.getFirst());
			evaluationDataContainer.addCellData(confusionMatrix.get(pair));
		}
		evaluationDataContainer.sortRows("frequency", false, 50);
		return evaluationDataContainer;
	}

	private DataContainer creteConfusionMatrix(HashMap<Pair<Object>, Integer> confusionMatrix, TreeSet<Object> treebankKeys,
			TreeSet<Object> parsedKeys) throws MaltEvalException {
		Integer value;
		DataContainer evaluationDataContainer = new DataContainer(treebankKeys.size(), true, false, "Col: gold / Row: system");
		evaluationDataContainer.setCaption("Confusion matrix for "
				+ complexGrouping_.get(complexGrouping_.keySet().iterator().next()).iterator().next());
		for (Object treebankKey : treebankKeys) {
			evaluationDataContainer.setColumnHeading(treebankKey.toString());
		}
		for (Object parsedKey : parsedKeys) {
			evaluationDataContainer.addRow(parsedKey.toString());
			for (Object treebankKey : treebankKeys) {
				value = confusionMatrix.get(new Pair<Object>(treebankKey, parsedKey));
				if (treebankKey.equals(parsedKey)) {
					evaluationDataContainer.addCellData(Double.NaN);
				} else if (value != null) {
					evaluationDataContainer.addCellData(value);
				} else {
					evaluationDataContainer.addCellData(0);
				}
			}
		}
		return evaluationDataContainer;
	}

	private boolean isCorrect(int i, int j) {
		if (metric_.equals(groupBy_)) {
			return groupByStrategy_.get(groupBy_).isCorrect(j, corpusTrees.getSentence(i), processedTrees.getSentence(i));
		} else {
			return metricStrategy_.get(metric_).isCorrect(j, processedTrees.getSentence(i), corpusTrees.getSentence(i));
		}
	}

	public static Object getKey(MaltSentence sentence, int wordIndex, String groupBy) throws MaltEvalException {
		if (groupByStrategy_.keySet().contains(groupBy)) {
			return groupByStrategy_.get(groupBy).getKey(sentence, wordIndex);
		} else {
			throw new MaltEvalException("Could not select a type of computation", null);
		}
	}

	public Grouping getGrouping(String groupBy) {
		return groupByStrategy_.get(groupBy);
	}

	static public Object getSimpleGroupingKey(MaltSentence sentence, int wordIndex, String groupBy) throws MaltEvalException {
		if (groupByStrategy_.keySet().contains(groupBy)) {
			return groupByStrategy_.get(groupBy).getKey(sentence, wordIndex);
		} else {
			throw new MaltEvalException("Could not select a type of computation", null);
		}
	}

	private void computeAggregated(DataContainer evaluationDataContainer) {
		int i, j, count;
		double sum;
		Number cellData;

		for (i = 0; i < evaluationDataContainer.getColumnCount(); i++) {
			sum = 0.0;
			count = 0;
			for (j = 0; j < evaluationDataContainer.getRowCount(); j++) {
				cellData = evaluationDataContainer.getEvaluationData(j, i);
				if (!(cellData instanceof Double && ((Double) cellData).isNaN())) {
					sum += cellData.doubleValue();
					count++;
				}
			}
			evaluationDataContainer.setColumnAggregation(i, sum / count);
			evaluationDataContainer.setColumnCount(i, count);
		}
	}

	static public Evaluator constructMaltParserEvaluator() {
		Feature f;
		Requirement requirement;
		Evaluator evaluator = new Evaluator();

		evaluator.setName("MaltParserEvaluator");
		evaluator.setType("parser");
		evaluator.setUrl("");
		evaluator.setUrlType("int");

		f = new Feature();
		f.setName(MaltEvalConfig.METRIC);
		f.setDefaultValue(MaltEvalConfig.DEFAULT_METRIC, "");
		for (String metric : metricStrategy_.keySet()) {
			try {
				f.addValue(metric, "");
				// if (!file.getName().split("\\.")[0].equals("Metric")) {
				// f.addValue(file.getName().split("\\.")[0], "");
				// }
			} catch (Exception e) {
			}
		}
		evaluator.addOptionFeature(f);
		f = new Feature();
		f.setName(MaltEvalConfig.GROUP_BY);
		f.setDefaultValue(MaltEvalConfig.DEFAULT_GROUPING, MaltEvalConfig.showAllAttributes);
		for (String grouping : groupByStrategy_.keySet()) {
			try {
				f.addValue(grouping, MaltEvalConfig.showAllAttributes);
			} catch (Exception e) {
			}
		}
		evaluator.addOptionFeature(f);
		f = new Feature();
		f.setName(MaltEvalConfig.excludeWordforms);
		f.setDefaultValue("", "");
		evaluator.addOptionFeature(f);
		f = new Feature();
		f.setName(MaltEvalConfig.excludeLemma);
		f.setDefaultValue("", "");
		evaluator.addOptionFeature(f);
		f = new Feature();
		f.setName(MaltEvalConfig.excludeCpostags);
		f.setDefaultValue("", "");
		evaluator.addOptionFeature(f);
		f = new Feature();
		f.setName(MaltEvalConfig.excludePostags);
		f.setDefaultValue("", "");
		evaluator.addOptionFeature(f);
		f = new Feature();
		f.setName(MaltEvalConfig.excludeFeats);
		f.setDefaultValue("", "");
		evaluator.addOptionFeature(f);
		f = new Feature();
		f.setName(MaltEvalConfig.excludeDeprels);
		f.setDefaultValue("", "");
		evaluator.addOptionFeature(f);
		f = new Feature();
		f.setName(MaltEvalConfig.excludePdeprels);
		f.setDefaultValue("", "");
		evaluator.addOptionFeature(f);
		f = new Feature();
		f.setName(MaltEvalConfig.excludeUnicodePunc);
		f.setDefaultValue("0", "");
		evaluator.addOptionFeature(f);
		f = new Feature();
		f.setName(MaltEvalConfig.minSentenceLength);
		f.setDefaultValue("0", "");
		evaluator.addOptionFeature(f);
		f = new Feature();
		f.setName(MaltEvalConfig.maxSentenceLength);
		f.setDefaultValue("0", "");
		evaluator.addOptionFeature(f);
		requirement = new Requirement();
		requirement.setName("form");
		evaluator.addRequirement(requirement);
		requirement = new Requirement();
		requirement.setName("postag");
		evaluator.addRequirement(requirement);
		requirement = new Requirement();
		requirement.setName("head");
		evaluator.addRequirement(requirement);
		requirement = new Requirement();
		requirement.setName("deprel");
		evaluator.addRequirement(requirement);
		return evaluator;
	}

	static private File[] getClassFiles(String path) {
		URL url = ClassLoader.getSystemResource("se");
		Vector<File> vectorOfFiles = new Vector<File>();
		if (url.getFile().contains("!")) {
			try {
				String[] splittedPath = url.getPath().split("!");
				String jarfilePath;
				ZipFile zipFile;
				try {
					if (splittedPath[0].indexOf(":") != -1) {
						jarfilePath = splittedPath[0].substring(splittedPath[0].indexOf(":") + 1);
					} else {
						jarfilePath = splittedPath[0];
					}
					jarfilePath = jarfilePath.replace("%20", " ");
					zipFile = new ZipFile(jarfilePath);
				} catch (Exception e) {
					if (splittedPath[0].indexOf(":") != -1) {
						jarfilePath = splittedPath[0].substring(splittedPath[0].indexOf(":") + 2);
					} else {
						jarfilePath = splittedPath[0];
					}
					jarfilePath = jarfilePath.replace("%20", " ");
					zipFile = new ZipFile(jarfilePath);
				}
				Enumeration<?> enumeration = zipFile.entries();
				while (enumeration.hasMoreElements()) {
					String fileName = enumeration.nextElement().toString();
					if (fileName.contains(path) && !fileName.equals(path)) {
						vectorOfFiles.add(new File(fileName.split("/")[fileName.split("/").length - 1]));
					}
				}
				zipFile.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		} else {
			return (new File(ClassLoader.getSystemResource(path).getPath())).listFiles();
		}
		return (File[]) vectorOfFiles.toArray(new File[0]);
	}

	private static void initGroupByStrategy(String className) throws MaltEvalException {
		groupByStrategy_.get(className).initialize();
		if (groupByStrategy_.get(className).isComplexGroupItem()) {
			complexGroupingTypes.add(className);
		}
	}

	private static void buildGroupingCombinations() throws MaltEvalException {
		String className, classPath = "se/vxu/msi/malteval/grouping/";
		groupByStrategy_ = new TreeMap<String, Grouping>();
		complexGroupingTypes = new HashSet<String>();
		File[] files = getClassFiles(classPath);
		for (File file : files) {
			className = file.getName().split("\\.")[0];
			if (!className.equals("Grouping")) {
				if (groupByStrategy_.keySet().contains(className)) {
					throw new MaltEvalException("Multiple declarations of the class " + className, null);
				}
				try {
					groupByStrategy_.put(className, (Grouping) ((Class<?>) Class.forName(classPath.replace("/", ".") + className))
							.newInstance());
				} catch (Exception e) {
					throw new MaltEvalException("Failed to load the class " + file.getName() + ".", null);
				}
				initGroupByStrategy(className);
			}
		}
		PluginLoader pluginLoader = new PluginLoader();
		String[] splittedName;
		String[] paths = System.getProperty("java.class.path").split(";");
		for (String path : paths) {
			File maltJarPath = new File(path);
			// maltJarPath = new File(maltJarPath.getParent() + File.separator + "plugin");
			// pluginLoader.loadPlugins(maltJarPath);
//			System.out.println(maltJarPath.getAbsoluteFile().getParent());
			maltJarPath = new File(maltJarPath.getAbsoluteFile().getParent() + File.separator + "plugin").getAbsoluteFile();
//			System.out.println(maltJarPath);
			pluginLoader.loadPlugins(maltJarPath);
			// maltJarPath = new File(System.getProperty("user.dir") + File.separator + path);
			// pluginLoader.loadPlugins(maltJarPath);
			for (String name : pluginLoader.getClassNames()) {
//				System.out.println(name + "!");
				splittedName = name.split("\\.");
				groupByStrategy_.put(splittedName[splittedName.length - 1], (Grouping) pluginLoader.newInstance(name, null, null));
				// if (groupByStrategy_.keySet().contains(splittedName[splittedName.length - 1])) {
				// throw new MaltEvalException("Multiple declations of the class " + splittedName[splittedName.length - 1], null);
				// }
				initGroupByStrategy(splittedName[splittedName.length - 1]);
			}
		}
		if (!groupByStrategy_.keySet().contains(MaltEvalConfig.DEFAULT_GROUPING)) {
			throw new MaltEvalException("The default grouping (" + MaltEvalConfig.DEFAULT_GROUPING + ") was not found and loaded.", null);
		}
		if (!groupByStrategy_.keySet().contains(MaltEvalConfig.GROUP_BY_WORD_FORM)) {
			throw new MaltEvalException("The wordform grouping (" + MaltEvalConfig.GROUP_BY_WORD_FORM + ") was not found and loaded.", null);
		}
	}

	private static void buildMetricCombinations() throws MaltEvalException {
		String className, classPath = "se/vxu/msi/malteval/metric/";
		metricStrategy_ = new TreeMap<String, Metric>();
		File[] files = getClassFiles(classPath);
		for (File file : files) {
			className = file.getName().split("\\.")[0];
			try {
				if (!className.equals("Metric")) {
					metricStrategy_.put(className, (Metric) ((Class<?>) Class.forName(classPath.replace("/", ".") + className))
							.newInstance());
					metricStrategy_.get(className).initialize();
					if (metricStrategy_.get(className).getSecondName() != null) {
						metricStrategy_.put(metricStrategy_.get(className).getSecondName(), (Metric) ((Class<?>) Class
								.forName("se.vxu.msi.malteval.metric." + className)).newInstance());
						if (validAttributes_.keySet().contains(metricStrategy_.get(className).getSecondName())) {
							throw new MaltEvalException("Multiple declations of the class " + classPath.replace("/", ".")
									+ metricStrategy_.get(className).getSecondName(), null);
						}
					}
				}
			} catch (Exception e) {
			}
		}
		if (!metricStrategy_.keySet().contains(MaltEvalConfig.DEFAULT_METRIC)) {
			throw new MaltEvalException("The default metric (" + MaltEvalConfig.DEFAULT_METRIC + ") was not found and loaded.", null);
		}
	}

	private DataContainer createEvaluationDataContainer(TreeMap<Object, IntegerCounter> counter) throws MaltEvalException {
		DataContainer evaluationDataContainer = new DataContainer(attributesToShow.size(), groupByStrategy_.get(groupBy_).showDetails(),
				groupByStrategy_.get(groupBy_).showTableHeader(), groupBy_);
		Iterator<String> iterator = attributesToShow.iterator();
		while (iterator.hasNext()) {
			evaluationDataContainer.setColumnHeading(iterator.next());
		}
		for (Object o : counter.keySet()) {
			evaluationDataContainer.addRow(o.toString());
			iterator = attributesToShow.iterator();
			while (iterator.hasNext()) {
				String attribute = iterator.next();
				if (attribute.equals(MaltEvalConfig.accuracy)) {
					if (counter.get(o).getParsedTotal() != 0) {
						if (groupBy_.equals(MaltEvalConfig.DEFAULT_GROUPING)) {
							evaluationDataContainer.addCellData(counter.get(o).getParsedCorrect() / counter.get(o).getParsedTotal());
						} else {
							evaluationDataContainer.addCellData((double) counter.get(o).getParsedCorrect() / counter.get(o).getParsedTotal());
						}
					} else {
						evaluationDataContainer.addCellData(Double.NaN);
					}
				} else if (attribute.equals(MaltEvalConfig.counter)) {
					evaluationDataContainer.addCellData(counter.get(o).getGsTotal());
				} else if (attribute.equals(MaltEvalConfig.correctcounter)) {
					evaluationDataContainer.addCellData(counter.get(o).getGsCorrect());
				} else if (attribute.equals(MaltEvalConfig.precision)) {
					evaluationDataContainer.addCellData((double) counter.get(o).getParsedCorrect() / counter.get(o).getParsedTotal());
				} else if (attribute.equals(MaltEvalConfig.recall)) {
					evaluationDataContainer.addCellData((double) counter.get(o).getGsCorrect() / counter.get(o).getGsTotal());
				} else if (attribute.equals(MaltEvalConfig.fscore)) {
					double recall = (double) counter.get(o).getGsCorrect() / counter.get(o).getGsTotal();
					double precision = (double) counter.get(o).getParsedCorrect() / counter.get(o).getParsedTotal();
					evaluationDataContainer.addCellData((2 * recall * precision) / (recall + precision));
				} else if (attribute.equals(MaltEvalConfig.parsercounter)) {
					evaluationDataContainer.addCellData(counter.get(o).getParsedTotal());
				} else if (attribute.equals(MaltEvalConfig.parsercorrectcounter)) {
					evaluationDataContainer.addCellData(counter.get(o).getParsedCorrect());
				} else if (attribute.equals(MaltEvalConfig.parseraccuracy)) {
					evaluationDataContainer.addCellData((double) counter.get(o).getParsedCorrect() / counter.get(o).getParsedTotal());
				} else if (attribute.equals(MaltEvalConfig.treebankcounter)) {
					evaluationDataContainer.addCellData(counter.get(o).getGsTotal());
				} else if (attribute.equals(MaltEvalConfig.treebankcorrectcounter)) {
					evaluationDataContainer.addCellData(counter.get(o).getGsCorrect());
				} else if (attribute.equals(MaltEvalConfig.treebankaccuracy)) {
					evaluationDataContainer.addCellData((double) counter.get(o).getGsCorrect() / counter.get(o).getGsTotal());
				} else if (attribute.equals(MaltEvalConfig.exactmatch)) {
					evaluationDataContainer.addCellData((counter.get(o).getParsedCorrect() == counter.get(o).getParsedTotal() ? 1 : 0));
				} else if (attribute.equals(MaltEvalConfig.includedtokenscount)) {
					evaluationDataContainer.addCellData(counter.get(o).getGsTotal());
				} else if (attribute.equals(MaltEvalConfig.sentencelength)) {
					evaluationDataContainer.addCellData(corpusTrees.getSentence(((Integer) o).intValue() - 1).getSentenceLength());
				} else if (attribute.equals(MaltEvalConfig.isparserconnected)) {
					evaluationDataContainer.addCellData((processedTrees.getSentence(((Integer) o).intValue() - 1).isConnected() ? 1 : 0));
				} else if (attribute.equals(MaltEvalConfig.istreebankconnected)) {
					evaluationDataContainer.addCellData((corpusTrees.getSentence(((Integer) o).intValue() - 1).isConnected() ? 1 : 0));
				} else if (attribute.equals(MaltEvalConfig.hasparsercycle)) {
					evaluationDataContainer.addCellData((processedTrees.getSentence(((Integer) o).intValue() - 1).hasCycle() ? 1 : 0));
				} else if (attribute.equals(MaltEvalConfig.hastreebankcycle)) {
					evaluationDataContainer.addCellData((corpusTrees.getSentence(((Integer) o).intValue() - 1).hasCycle() ? 1 : 0));
				} else if (attribute.equals(MaltEvalConfig.isparserprojective)) {
					evaluationDataContainer.addCellData((processedTrees.getSentence(((Integer) o).intValue() - 1).isProjective() ? 1 : 0));
				} else if (attribute.equals(MaltEvalConfig.istreebankprojective)) {
					evaluationDataContainer.addCellData((corpusTrees.getSentence(((Integer) o).intValue() - 1).isProjective() ? 1 : 0));
				} else if (attribute.equals(MaltEvalConfig.id)) {
					evaluationDataContainer.addCellData(corpusTrees.getSentence(((Integer) o).intValue() - 1).getId());
				}
			}
		}
		computeAggregated(evaluationDataContainer);
		if (getGrouping(groupBy_) != null) {
			return getGrouping(groupBy_).postProcess(evaluationDataContainer);
		} else {
			return evaluationDataContainer;
		}
	}

	public static Iterator<String> getGroupByStrategyNames() {
		return groupByStrategy_.keySet().iterator();
	}

	public static Grouping getGroupByStrategy(String group) {
		return groupByStrategy_.get(group);
	}
}
