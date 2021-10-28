/*
 * Created on 20 feb 2008
 */
package se.vxu.msi.malteval.grouping;

import java.util.TreeMap;

import se.vxu.msi.malteval.MaltEvalConfig;
import se.vxu.msi.malteval.corpus.MaltSentence;
import se.vxu.msi.malteval.specialgrouping.grouping.SentenceAndToken;
import se.vxu.msi.malteval.util.DataContainer;
import se.vxu.msi.malteval.util.IntegerCounter;

public class Clustering2 implements Grouping {
	private SentenceAndToken groupByStrategy;

	public void initialize() {
		groupByStrategy = new SentenceAndToken();
		groupByStrategy.initialize();
	}

	public Object getKey(MaltSentence sentence, int wordIndex) {
		return groupByStrategy.getKey(sentence, wordIndex);
	}

	public boolean isComplexGroupItem() {
		return false;
	}

	public DataContainer postProcess(DataContainer dataContainer) {
		boolean hasAccuracyAttribute = false;
		int i, j;
		TreeMap<Integer, IntegerCounter> errorPropagationByPosition = new TreeMap<Integer, IntegerCounter>();
		IntegerCounter integerCounter;
		String currentSid = null;

		DataContainer ep = new DataContainer(dataContainer.getColumnCount(), showDetails(), showTableHeader(), "Relative token position");
		for (i = 0; i < dataContainer.getColumnCount(); i++) {
			ep.setColumnHeading(dataContainer.getColumnsHeader(i));
			if (dataContainer.getColumnsHeader(i).equals(MaltEvalConfig.accuracy)) {
				hasAccuracyAttribute = true;
			}
		}
		if (!hasAccuracyAttribute) {
			System.out.println("Warning: attribute \"accuracy\" should be specified.");
		}
		i = 0;
		currentSid = dataContainer.getRowHeaders(i).split("_")[0];
		while (i < dataContainer.getRowCount()) {
			j = 1;
			while (i + j < dataContainer.getRowCount() && dataContainer.getRowHeaders(i + j).split("_")[0].equals(currentSid)) {
				if ((integerCounter = errorPropagationByPosition.get(j)) == null) {
					integerCounter = new IntegerCounter();
					errorPropagationByPosition.put(j, integerCounter);
				}
				integerCounter.incGsTotal();
				if (dataContainer.getEvaluationData(i + j, 0).intValue() == 1) {
					integerCounter.incGsCorrect();
					break;
				}
				j++;
			}
			if (i + j < dataContainer.getRowCount() && dataContainer.getRowHeaders(i + j).split("_")[0].equals(currentSid)) {
				i = i + j;
			} else {
				i = i + j - 1;
				if (i + 1 < dataContainer.getRowCount()) {
					currentSid = dataContainer.getRowHeaders(i + 1).split("_")[0];
				} else {
					break;
				}
			}
		}
		for (Integer key : errorPropagationByPosition.keySet()) {
			ep.addRow(String.valueOf(key));
			for (i = 0; i < dataContainer.getColumnCount(); i++) {
				if (dataContainer.getColumnsHeader(i).equals(MaltEvalConfig.accuracy)) {
					ep.addCellData((double) errorPropagationByPosition.get(key).getGsCorrect() / errorPropagationByPosition.get(key).getGsTotal());
				} else if (dataContainer.getColumnsHeader(i).equals(MaltEvalConfig.counter)) {
					ep.addCellData(errorPropagationByPosition.get(key).getGsTotal());
				} else if (dataContainer.getColumnsHeader(i).equals(MaltEvalConfig.correctcounter)) {
					ep.addCellData(errorPropagationByPosition.get(key).getGsCorrect());
				}
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
		return groupByStrategy.getClass().getSimpleName();
	}

	public String correspondingMetric() {
		return null;
	}

	public boolean isSimpleAccuracyGrouping() {
		return true;
	}
}
