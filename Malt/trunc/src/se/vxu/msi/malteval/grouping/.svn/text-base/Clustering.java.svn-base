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

public class Clustering implements Grouping {
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
		int i, j;
		TreeMap<Integer, IntegerCounter> errorPropagationByPosition = new TreeMap<Integer, IntegerCounter>();
		String currentSid = null;

		for (j = -10; j < 11; j++) {
			errorPropagationByPosition.put(j, new IntegerCounter());
		}

		DataContainer ep = new DataContainer(/*dataContainer.getColumnCount()*/ 1, showDetails(), showTableHeader(), "Relative token position");
		ep.setColumnHeading(MaltEvalConfig.accuracy);
		//		for (i = 0; i < dataContainer.getColumnCount(); i++) {
//			ep.setColumnHeading(dataContainer.getColumnsHeader(i));
//		}
		for (i = 0; i < dataContainer.getRowCount(); i++) {
			currentSid = dataContainer.getRowHeaders(i).split("_")[0];
			if (dataContainer.getEvaluationData(i, 0).intValue() == 1) {
				for (j = -10; j < 11; j++) {
					if (j != 0 && 0 <= i + j && i + j < dataContainer.getRowCount()
							&& dataContainer.getRowHeaders(i + j).split("_")[0].equals(currentSid)) {
						errorPropagationByPosition.get(j).incGsTotal();
						if (dataContainer.getEvaluationData(i + j, 0).intValue() == 1) {
							errorPropagationByPosition.get(j).incGsCorrect();
						}
					}
				}
			}
		}
		for (Integer key : errorPropagationByPosition.keySet()) {
			ep.addRow(String.valueOf(key));
			for (i = 0; i < ep.getColumnCount(); i++) {
//				if (dataContainer.getColumnsHeader(i).equals(MaltEvalConfig.accuracy)) {
					ep.addCellData((float) errorPropagationByPosition.get(key).getGsCorrect()
							/ errorPropagationByPosition.get(key).getGsTotal());
//				} else if (dataContainer.getColumnsHeader(i).equals(MaltEvalConfig.counter)) {
//					ep.addCellData(errorPropagationByPosition.get(key).getGsCorrect());
//				} else if (dataContainer.getColumnsHeader(i).equals(MaltEvalConfig.correctcounter)) {
//					ep.addCellData(errorPropagationByPosition.get(key).getGsTotal());
//				}
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
