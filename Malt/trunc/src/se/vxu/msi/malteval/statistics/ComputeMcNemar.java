/*
 * Created on 2004-dec-21
 */
package se.vxu.msi.malteval.statistics;

import java.util.HashMap;
import java.util.Vector;

import se.vxu.msi.malteval.exceptions.MaltEvalException;
import se.vxu.msi.malteval.util.DataContainer;

/**
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class ComputeMcNemar {
	public final static int ONE_PERCENT_LEVEL = 0;
	public final static int FIVE_PERCENT_LEVEL = 1;

	private HashMap<String, DataContainer> zValue_;
	private HashMap<String, DataContainer> onePercent_;
	private HashMap<String, DataContainer> fivePercent_;

	public ComputeMcNemar(DataContainer[][] evaluationDataContainers, String[] parserFiles)
			throws MaltEvalException {
		computeMcNemar(evaluationDataContainers, parserFiles);
	}

	private double computeZValueForMcNemar(Vector<Boolean> first, Vector<Boolean> second) throws MaltEvalException {
		int i;
		int trueTrue = 0, trueFalse = 0, falseTrue = 0, falseFalse = 0;

		if (first.size() != second.size()) {
			throw new MaltEvalException("The data sets to compare do not contain the same number of rows.", null);
		}

		for (i = 0; i < first.size(); i++) {
			if (first.get(i)) {
				if (second.get(i)) {
					trueTrue++;
				} else {
					trueFalse++;
				}
			} else {
				if (second.get(i)) {
					falseTrue++;
				} else {
					falseFalse++;
				}
			}
		}
		return (Math.abs(trueFalse - falseTrue) - 1) / Math.sqrt(trueFalse + falseTrue);
	}

	private boolean isZValueStatisticalSignificant(double z, int level) throws MaltEvalException {
		if (level != ONE_PERCENT_LEVEL && level != FIVE_PERCENT_LEVEL) {
			throw new MaltEvalException("The level argument equals neither ONE_PERCENT_LEVEL nor FIVE_PERCENT_LEVEL.", null);
		}
		if (z >= 2.58) {
			return true;
		} else if (z >= 1.96) {
			return level == FIVE_PERCENT_LEVEL;
		} else {
			return false;
		}
	}

	private String getMcNemarHashSetKey(DataContainer evaluationDataContainer, int column) {
		return evaluationDataContainer.getEvaluation() + "\nAttribute: " + evaluationDataContainer.getColumnsHeader(column) + "\n";
	}

	private void computeMcNemar(DataContainer[][] evaluationDataContainers, String[] parserFiles) {
		double z;
		int i, j, k, column;
		zValue_ = new HashMap<String, DataContainer>();
		onePercent_ = new HashMap<String, DataContainer>();
		fivePercent_ = new HashMap<String, DataContainer>();
		DataContainer zValueEDC, oneEDC, fiveEDC;
		if (parserFiles.length > 1) {
			for (i = 0; i < evaluationDataContainers.length; i++) {
				for (column = 0; column < evaluationDataContainers[i][0].getColumnCount(); column++) {
					try {
						evaluationDataContainers[i][0].getColumnAsBoolean(column);
					} catch (MaltEvalException exception) {
						continue;
					}
					zValueEDC = zValue_.get(getMcNemarHashSetKey(evaluationDataContainers[i][0], column));
					oneEDC = onePercent_.get(getMcNemarHashSetKey(evaluationDataContainers[i][0], column));
					fiveEDC = fivePercent_.get(getMcNemarHashSetKey(evaluationDataContainers[i][0], column));
					if (zValueEDC == null) {
						zValueEDC = new DataContainer(parserFiles.length, true, false, "McNemar: z-value");
						oneEDC = new DataContainer(parserFiles.length, true, false, "McNemar: p<0.01?");
						fiveEDC = new DataContainer(parserFiles.length, true, false, "McNemar: p<0.05?");
						for (j = 0; j < parserFiles.length; j++) {
							zValueEDC.setColumnHeading("<" + (j + 1) + ">");
							oneEDC.setColumnHeading("<" + (j + 1) + ">");
							fiveEDC.setColumnHeading("<" + (j + 1) + ">");
						}
						zValue_.put(getMcNemarHashSetKey(evaluationDataContainers[i][0], column), zValueEDC);
						onePercent_.put(getMcNemarHashSetKey(evaluationDataContainers[i][0], column), oneEDC);
						fivePercent_.put(getMcNemarHashSetKey(evaluationDataContainers[i][0], column), fiveEDC);
					}
					for (j = 0; j < evaluationDataContainers[i].length; j++) {
						zValueEDC.addRow("<" + (j + 1) + "> (" + parserFiles[j] + ")");
						oneEDC.addRow("<" + (j + 1) + "> (" + parserFiles[j] + ")");
						fiveEDC.addRow("<" + (j + 1) + "> (" + parserFiles[j] + ")");
						for (k = 0; k < evaluationDataContainers[i].length; k++) {
							if (j < k) {
								try {
									z = computeZValueForMcNemar(evaluationDataContainers[i][j].getColumnAsBoolean(column),
											evaluationDataContainers[i][k].getColumnAsBoolean(column));
									zValueEDC.addCellData(z);
									oneEDC.addCellData((isZValueStatisticalSignificant(z, ComputeMcNemar.ONE_PERCENT_LEVEL) ? 1 : 0));
									fiveEDC.addCellData((isZValueStatisticalSignificant(z, ComputeMcNemar.FIVE_PERCENT_LEVEL) ? 1 : 0));
								} catch (MaltEvalException e) {
								}
							} else {
								zValueEDC.addCellData(Double.NaN);
								oneEDC.addCellData(Double.NaN);
								fiveEDC.addCellData(Double.NaN);
							}
						}
					}
				}
			}
		}
	}

	public HashMap<String, DataContainer> getFivePercent() {
		return fivePercent_;
	}

	public HashMap<String, DataContainer> getOnePercent() {
		return onePercent_;
	}

	public HashMap<String, DataContainer> getZValue() {
		return zValue_;
	}
}
