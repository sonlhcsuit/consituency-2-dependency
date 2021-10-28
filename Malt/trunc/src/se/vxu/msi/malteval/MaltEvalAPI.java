/*
 * Created on 9 okt 2007
 */
package se.vxu.msi.malteval;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

import se.vxu.msi.malteval.configdata.GenericEvaluationParameters;
import se.vxu.msi.malteval.configdata.GenericExperimentSetup;
import se.vxu.msi.malteval.configdata.ReadEvaluationSetup;
import se.vxu.msi.malteval.corpus.MaltTreebank;
import se.vxu.msi.malteval.evaluator.MaltParserEvaluator;
import se.vxu.msi.malteval.exceptions.MaltEvalException;
import se.vxu.msi.malteval.util.DataContainer;

public class MaltEvalAPI {
	public static DataContainer simpleMaltEvalEvaluation(MaltTreebank parsedData, MaltTreebank goldStandardData, String metric,
			String groupByValue, String format) throws MaltEvalException {
		goldStandardData.getDeprels().addAll(parsedData.getDeprels());
		parsedData.getDeprels().addAll(goldStandardData.getDeprels());
		GenericEvaluationParameters gep = (new ReadEvaluationSetup(new GenericExperimentSetup())).getGenericEvaluationParameters();
		MaltEvalConfig.init();
		MaltParserEvaluator.initialize();
		//DataContainer.overridePrintDetails = true;
		DataContainer.setDecimalFormat("0.0000");
		HashMap<String, String> settings = new HashMap<String, String>();
		settings.put(metric, "");
		gep.addEvaluationParameter(MaltEvalConfig.METRIC, settings);
		settings = new HashMap<String, String>();
		settings.put(groupByValue, format);
		gep.addEvaluationParameter(MaltEvalConfig.GROUP_BY, settings);

		MaltParserEvaluator maltParserEvaluator = new MaltParserEvaluator(parsedData, goldStandardData);
		gep.createExperimentEngineInputDataArray();
		return maltParserEvaluator.evaluate(gep.getNextEvaluationParameters());
	}

	public static DataContainer simpleMaltEvalEvaluation(String systemfile, String goldStandardFilename, String metric,
			String groupByValue, String format) throws MaltEvalException {
		MaltTreebank parsedData = new MaltTreebank(systemfile, MaltEvalConfig.CONLL, null, null, true, true, true);
		MaltTreebank goldStandardData = new MaltTreebank(goldStandardFilename, MaltEvalConfig.CONLL, null, null, true, true, true);
		return simpleMaltEvalEvaluation(parsedData, goldStandardData, metric, groupByValue, format);
	}

	public static TreeMap<Integer, HashSet<Integer>> simpleMaltEvalSearch(MaltTreebank data, String groupByValue, String searchValue, boolean negateSearch)
			throws MaltEvalException {
		int i, j;
		TreeMap<Integer, HashSet<Integer>> sentences = new TreeMap<Integer, HashSet<Integer>>();
		TreeMap<Integer, HashSet<Integer>> negSentences = new TreeMap<Integer, HashSet<Integer>>();
		HashSet<Integer> words;
		Pattern pattern = Pattern.compile(searchValue);
		for (i = 0; i < data.getSentenceCount(); i++) {
			for (j = 0; j < data.getSentence(i).getSentenceLength(); j++) {
				if (pattern.matcher(MaltParserEvaluator.getGroupByStrategy(groupByValue).getKey(data.getSentence(i), j).toString()).matches()) {
					if ((words = sentences.get(i + 1)) == null) {
						sentences.put(i + 1, words = new HashSet<Integer>());
					}
					words.add(j + 1);
				}
			}
			if (!sentences.keySet().contains(i + 1)) {
				negSentences.put(i + 1, null);
			}
		}
		if (negateSearch) {
			return negSentences; 
		} else {
			return sentences;
		}
	}

	public static TreeSet<Object> getAllMaltEvalSearchValues(MaltTreebank data, String groupByValue) throws MaltEvalException {
		int i, j;
		TreeSet<Object> values = new TreeSet<Object>();
		for (i = 0; i < data.getSentenceCount(); i++) {
			for (j = 0; j < data.getSentence(i).getSentenceLength(); j++) {
				values.add(MaltParserEvaluator.getGroupByStrategy(groupByValue).getKey(data.getSentence(i), j));
			}
		}
		return values;
	}
}
