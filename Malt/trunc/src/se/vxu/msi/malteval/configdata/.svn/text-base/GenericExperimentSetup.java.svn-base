/*
 * Created on 2004-aug-23
 */
package se.vxu.msi.malteval.configdata;

import java.util.Date;
import java.util.HashMap;

import se.vxu.msi.malteval.exceptions.MaltEvalException;

/**
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class GenericExperimentSetup {
	private String experimentName_;
	private Date date;
	private String evaluationFormat;
	private GenericEvaluationParameters genericEvaluationParameters;
	private int[] processorParametersValuesMaxCount;
	private int dataResourceNamesAndVersionsMaxCount;
	private int experimentTypesMaxCount;

	public GenericExperimentSetup() {
		experimentName_ = null;
		date = null;
		evaluationFormat = null;
		genericEvaluationParameters = new GenericEvaluationParameters();
	}

	public void setExperimentName(String experimentName) {
		experimentName_ = experimentName;
	}

	public void setDate(Date d) {
		date = d;
	}

	public void setEvaluationsFormat(String ef) {
		evaluationFormat = ef;
	}

	public void addEvaluationParameter(String parameterName, HashMap<String,String> values) throws MaltEvalException {
		genericEvaluationParameters.addEvaluationParameter(parameterName, values);
	}

	public String getExperimentName() {
		return experimentName_;
	}

	public Date getDate() {
		return date;
	}

	public String getEvaluationsFormat() {
		return evaluationFormat;
	}

	public GenericEvaluationParameters getGenericEvaluationParameters() {
		return genericEvaluationParameters;
	}

	public int getTotalNumberOfCombinationsCount() {
		return getTotalNumberOfCombinationsCount(0);
	}
	
	private int getTotalNumberOfCombinationsCount(int slot) {
		if (slot < processorParametersValuesMaxCount.length) {
			return processorParametersValuesMaxCount[slot] * getTotalNumberOfCombinationsCount(slot + 1);
		}
		else if (slot == processorParametersValuesMaxCount.length) {
			return dataResourceNamesAndVersionsMaxCount * getTotalNumberOfCombinationsCount(slot + 1);
		}
		else if (slot == processorParametersValuesMaxCount.length + 1) {
			return experimentTypesMaxCount;
		}
		else {
			return 0;
		}
	}
}