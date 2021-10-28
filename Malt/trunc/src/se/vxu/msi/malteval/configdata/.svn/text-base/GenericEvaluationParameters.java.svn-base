/*
 * Created on 2004-nov-10
 */
package se.vxu.msi.malteval.configdata;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import se.vxu.msi.malteval.MaltEvalConfig;
import se.vxu.msi.malteval.configdata.util.Evaluation;
import se.vxu.msi.malteval.configdata.util.Evaluator;
import se.vxu.msi.malteval.evaluator.MaltParserEvaluator;
import se.vxu.msi.malteval.exceptions.MaltEvalException;

/**
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class GenericEvaluationParameters {
	private Hashtable<String, HashMap<String, String>> evaluationsParameters;

	private String[][] evaluationParametersValues;
	private String[] evaluationParameters;
	private int[] evaluationParametersValuesMaxCount;
	private int[] evaluationParametersValuesCurrentCount;

	public GenericEvaluationParameters() {
		evaluationsParameters = new Hashtable<String, HashMap<String, String>>();
	}

	public void addEvaluationParameter(String parameterName, HashMap<String, String> values) throws MaltEvalException {
		if (!MaltEvalConfig.isParameterArgument("--" + parameterName) && !MaltEvalConfig.isExcludeType("--" + parameterName)
				&& !MaltEvalConfig.isIntervalType("--" + parameterName)
				&& !MaltEvalConfig.isExcludeUnicodePunctuation("--" + parameterName)) {
			throw new MaltEvalException("\"" + parameterName + "\" is not a valid evaluation parameter name", this.getClass());
		}
		evaluationsParameters.put(parameterName, values);
	}

	public Iterator<String> getEvaluationParamtersIterator() {
		return evaluationsParameters.keySet().iterator();
	}

	public int getEvaluationParametersCount() {
		return evaluationsParameters.size();
	}

	public boolean hasEvaluationParameter(String parameterName) {
		return evaluationsParameters.get(parameterName) != null;
	}

	public int getEvaluationParameterValuesCount(String parameterName) {
		return evaluationsParameters.get(parameterName).size();
	}

	public Iterator<String> getEvaluationParameterValuesIterator(String parameterName) {
		return evaluationsParameters.get(parameterName).keySet().iterator();
	}

	public String getEvaluationParameterValueFormat(String parameterName, String value) {
		return evaluationsParameters.get(parameterName).get(value);
	}

	public int getTotalNumberOfCombinationsCount() {
		return getTotalNumberOfCombinationsCount(0);
	}

	private int getTotalNumberOfCombinationsCount(int slot) {
		if (slot == evaluationParametersValuesMaxCount.length) {
			return 1;
		} else {
			return evaluationParametersValuesMaxCount[slot] * getTotalNumberOfCombinationsCount(slot + 1);
		}
	}

	public boolean incToNextEvaluationParameters() {
		return increaseCounters(0);
	}

	public Evaluation getNextEvaluationParameters() {
		int i;
		Evaluation evaluation = new Evaluation();
		for (i = 0; i < evaluationParameters.length; i++) {
			evaluation.addParameter(evaluationParameters[i], evaluationParametersValues[i][evaluationParametersValuesCurrentCount[i]],
					evaluationsParameters.get(evaluationParameters[i]).get(
							evaluationParametersValues[i][evaluationParametersValuesCurrentCount[i]]));
		}
		if (evaluation.getParameterValue(MaltEvalConfig.METRIC).equals("self")) {
			evaluation.addParameter(MaltEvalConfig.METRIC, MaltParserEvaluator.getGroupByStrategy(evaluation.getParameterValue(MaltEvalConfig.GROUP_BY)).getSelfMetricName(), evaluation.getParameterFormat(MaltEvalConfig.METRIC));
		}
		return evaluation;
	}

	private boolean increaseCounters(int slot) {
		if (slot < 0) {
			System.err.println("Error!");
			return false;
		} else if (slot < evaluationParametersValuesMaxCount.length) {
			if (evaluationParametersValuesCurrentCount[slot] == evaluationParametersValuesMaxCount[slot] - 1) {
				evaluationParametersValuesCurrentCount[slot] = 0;
				return increaseCounters(slot + 1);
			} else {
				evaluationParametersValuesCurrentCount[slot]++;
				return true;
			}
		} else if (slot == evaluationParametersValuesMaxCount.length) {
			return false;
		} else {
			System.err.println("Error");
			return false;
		}
	}

	public void createExperimentEngineInputDataArray() {
		int i, j;
		Iterator<String> iterator1, iterator2;
		Evaluator evaluator = MaltParserEvaluator.constructMaltParserEvaluator();
		String[] features = evaluator.getFeatureNames();
		String[] defaultValues = evaluator.getDefaultValues();
		String[] defaultFormats = evaluator.getDefaultFormats();

		addDefaultEvaluationParameters(features, defaultValues, defaultFormats);

		evaluationParametersValues = new String[getEvaluationParametersCount()][];
		evaluationParameters = new String[getEvaluationParametersCount()];
		evaluationParametersValuesCurrentCount = new int[getEvaluationParametersCount()];
		evaluationParametersValuesMaxCount = new int[getEvaluationParametersCount()];
		iterator1 = getEvaluationParamtersIterator();
		i = 0;
		while (iterator1.hasNext()) {
			evaluationParameters[i] = (String) iterator1.next();
			evaluationParametersValues[i] = new String[getEvaluationParameterValuesCount(evaluationParameters[i])];
			evaluationParametersValuesMaxCount[i] = getEvaluationParameterValuesCount(evaluationParameters[i]);
			iterator2 = getEvaluationParameterValuesIterator(evaluationParameters[i]);
			j = 0;
			while (iterator2.hasNext()) {
				evaluationParametersValues[i][j] = (String) iterator2.next();
				j++;
			}
			i++;
		}
		reset();
	}

	private void addDefaultEvaluationParameters(String[] features, String[] defaultValues, String[] defaultFormats) {
		int i;
		Vector<String> vector;

		for (i = 0; i < features.length; i++) {
			if (!hasEvaluationParameter(features[i]) || getEvaluationParameterValuesCount(features[i]) == 0) {
				vector = new Vector<String>();
				vector.add(defaultValues[i]);
			}
		}
	}

	private void reset() {
		int i;
		if (evaluationParametersValuesCurrentCount.length > 0) {
			evaluationParametersValuesCurrentCount[0] = 0;
		}
		for (i = 1; i < evaluationParametersValuesCurrentCount.length; i++) {
			evaluationParametersValuesCurrentCount[i] = 0;
		}
	}

	public Evaluation getMimicOfMergedEvaluation(boolean returnSingle) {
		StringBuffer values;
		Evaluation evaluation = new Evaluation();
		for (String parameter : evaluationsParameters.keySet()) {
			values = new StringBuffer();
			for (String value : evaluationsParameters.get(parameter).keySet()) {
				values.append(value + ";");
			}
			values.deleteCharAt(values.length() - 1);
			if ((returnSingle && evaluationsParameters.get(parameter).size() == 1)
					|| (!returnSingle && evaluationsParameters.get(parameter).size() > 1)) {
				evaluation.addParameter(parameter, values.toString(), evaluationsParameters.get(parameter).get(values));
			}
		}
		if (evaluation.getParameterValue(MaltEvalConfig.METRIC) != null && evaluation.getParameterValue(MaltEvalConfig.METRIC).equals("self")) {
			evaluation.addParameter(MaltEvalConfig.METRIC, MaltParserEvaluator.getGroupByStrategy(evaluation.getParameterValue(MaltEvalConfig.GROUP_BY)).getSelfMetricName(), evaluation.getParameterFormat(MaltEvalConfig.METRIC));
		}
		return evaluation;
	}
}
