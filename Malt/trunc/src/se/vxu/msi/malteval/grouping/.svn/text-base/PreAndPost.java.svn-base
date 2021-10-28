/*
 * Created on 20 feb 2008
 */
package se.vxu.msi.malteval.grouping;

import se.vxu.msi.malteval.MaltEvalConfig;
import se.vxu.msi.malteval.corpus.MaltSentence;
import se.vxu.msi.malteval.specialgrouping.grouping.SentenceAndToken;
import se.vxu.msi.malteval.util.DataContainer;

public class PreAndPost implements Grouping {

	private SentenceAndToken groupByStrategy;

	public Object getKey(MaltSentence sentence, int wordIndex) {
		return groupByStrategy.getKey(sentence, wordIndex);
	}

	public void initialize() {
		groupByStrategy = new SentenceAndToken();
		groupByStrategy.initialize();
	}

	public boolean isComplexGroupItem() {
		return false;
	}

	public DataContainer postProcess(DataContainer dataContainer) {
		int i, totalFirst = 0, totalFollow = 0, correctFirst = 0, correctFollow = 0;
		boolean foundFirstError = false, hasAccuracyAttribute = false;
		String[] split;
		String currentSid = null;
		DataContainer ep = new DataContainer(dataContainer.getColumnCount(), showDetails(), showTableHeader(), "Type");
		for (i = 0; i < dataContainer.getColumnCount(); i++) {
			ep.setColumnHeading(dataContainer.getColumnsHeader(i));
			if (dataContainer.getColumnsHeader(i).equals(MaltEvalConfig.accuracy)) {
				hasAccuracyAttribute = true;
			}
		}
		if (!hasAccuracyAttribute) {
			System.out.println("Warning: attribute \"accuracy\" should be specified.");
		}
		for (i = 0; i < dataContainer.getRowCount(); i++) {
			split = dataContainer.getRowHeaders(i).split("_");
			if (!split[0].equals(currentSid)) {
				currentSid = split[0];
				foundFirstError = false;
			}
			if (foundFirstError) {
				totalFollow++;
			} else {
				totalFirst++;
			}
			if (dataContainer.getEvaluationData(i, 0).intValue() == 1) {
				if (foundFirstError) {
					correctFollow++;
				} else {
					correctFirst++;
				}
			} else {
				foundFirstError = true;
			}
		}
		ep.addRow("First");
		for (i = 0; i < ep.getColumnCount(); i++) {
			if (dataContainer.getColumnsHeader(i).equals(MaltEvalConfig.accuracy)) {
				ep.addCellData(((double) correctFirst) / totalFirst);
			} else if (dataContainer.getColumnsHeader(i).equals(MaltEvalConfig.counter)) {
				ep.addCellData(totalFirst);
			} else if (dataContainer.getColumnsHeader(i).equals(MaltEvalConfig.correctcounter)) {
				ep.addCellData(correctFirst);
			}
		}
		ep.addRow("Follow");
		for (i = 0; i < ep.getColumnCount(); i++) {
			if (dataContainer.getColumnsHeader(i).equals(MaltEvalConfig.accuracy)) {
				ep.addCellData(((double) correctFollow) / totalFollow);
			} else if (dataContainer.getColumnsHeader(i).equals(MaltEvalConfig.counter)) {
				ep.addCellData(totalFollow);
			} else if (dataContainer.getColumnsHeader(i).equals(MaltEvalConfig.correctcounter)) {
				ep.addCellData(correctFollow);
			}
		}
		return ep;
	}

	public boolean showDetails() {
		return true;
	}

	public boolean showTableHeader() {
		return false;
	}

	public String getSecondName() {
		return null;
	}

	public boolean isCorrect(int wordIndex, MaltSentence goldSentence, MaltSentence parsedSentence) {
		return getKey(goldSentence, wordIndex).equals(getKey(parsedSentence, wordIndex));
	}

	public String getSelfMetricName() {
		return getClass().getSimpleName();
	}

	public String correspondingMetric() {
		return null;
	}

	public boolean isSimpleAccuracyGrouping() {
		return true;
	}
}
