/*
 * Created on 2004-feb-19
 *
 */

package se.vxu.msi.malteval.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.TreeMap;
import java.util.Vector;

import se.vxu.msi.malteval.configdata.GenericEvaluationParameters;
import se.vxu.msi.malteval.configdata.util.Evaluation;
import se.vxu.msi.malteval.exceptions.MaltEvalException;

/**
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class DataContainer {
	static private DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
	//static private DecimalFormat aggregatedDecimalFormat = new DecimalFormat("0.####");

	static public boolean printHeader = true;
	static public boolean printRowHeading = true;
	static public Boolean overridePrintDetails = null;
	static public Boolean overrideShowTableColumnHeaderInfo_ = null;
	static private boolean printSeparatoraAsTab = false;
	final private int whiteSpaceDivisionCount = 3;

	private double[] columnAggreator;
	private String aggreatorHeader;
	private int[] columnCount;
	private String countHeader;
	private int[] columnLengths;
	private int sumOfColumnLengths;
	private Vector<Number[]> tableData;
	private String[] columnsHeaders;
	private String rowHeadingsHeading;
	private Vector<String> rowHeaders;
	private int rowHeadersLength;
	private int currentColumnIndex;
	private int currentColumnHeadingIndex;

	private boolean hasBeenCut_;
	private int removedRowsByCutCount;
	private boolean showTableColumnHeader_;
	private boolean printDetails;

	private Evaluation evaluation_;

	private DataContainer confusionTable_;
	private DataContainer confusionMatrix_;

	private String caption_;

	public DataContainer(int cols, boolean showDetails, boolean showTableColumnHeaderInfo) {
		int i;
		rowHeadingsHeading = "";
		columnAggreator = new double[cols];
		columnCount = new int[cols];
		for (i = 0; i < columnAggreator.length; i++) {
			columnAggreator[i] = Double.NaN;
			columnCount[i] = 0;
		}
		columnLengths = new int[cols];
		sumOfColumnLengths = 0;
		tableData = new Vector<Number[]>();
		columnsHeaders = new String[cols];
		rowHeaders = new Vector<String>();
		rowHeadersLength = 0;
		currentColumnIndex = 0;
		currentColumnHeadingIndex = 0;
		hasBeenCut_ = false;
		removedRowsByCutCount = 0;
		setAggreatorHeader("Row mean");
		setCountHeader("Row count");

		confusionTable_ = null;
		confusionMatrix_ = null;

		if (overridePrintDetails == null) {
			printDetails = showDetails;
		} else {
			printDetails = overridePrintDetails;
		}
		if (overrideShowTableColumnHeaderInfo_ == null) {
			showTableColumnHeader_ = showTableColumnHeaderInfo;
		} else {
			showTableColumnHeader_ = overrideShowTableColumnHeaderInfo_;
		}

		caption_ = null;
	}

	public DataContainer(int cols, boolean showDetails, boolean showTableHeader, String rhh) {
		this(cols, showDetails, showTableHeader);
		setRowHeadingsHeading(rhh);
	}

	public void setRowHeadingsHeading(String rhh) {
		if (rhh.length() > rowHeadersLength) {
			rowHeadersLength = rhh.length();
		}
		rowHeadingsHeading = rhh;
	}

	private void setAggreatorHeader(String ah) {
		if (ah.length() > rowHeadersLength) {
			rowHeadersLength = ah.length();
		}
		aggreatorHeader = ah;
	}

	private void setCountHeader(String ch) {
		if (ch.length() > rowHeadersLength) {
			rowHeadersLength = ch.length();
		}
		countHeader = ch;
	}

	public void addRow(String rowHeading) {
		if (rowHeading.length() > rowHeadersLength) {
			rowHeadersLength = rowHeading.length();
		}
		rowHeaders.add(rowHeading);
		tableData.add(new Number[columnsHeaders.length]);
		currentColumnIndex = 0;
	}

	public void setColumnHeading(String heading) {
		if (heading.length() > columnLengths[currentColumnHeadingIndex]) {
			sumOfColumnLengths -= columnLengths[currentColumnHeadingIndex];
			columnLengths[currentColumnHeadingIndex] = heading.length();
			sumOfColumnLengths += columnLengths[currentColumnHeadingIndex];
		}
		columnsHeaders[currentColumnHeadingIndex++] = heading;
	}

	public void setColumnAggregation(int col, double data) {
		if (String.valueOf(decimalFormat.format(data)).length() > columnLengths[col]) {
			sumOfColumnLengths -= columnLengths[col];
			columnLengths[col] = String.valueOf(decimalFormat.format(data)).length();
			sumOfColumnLengths += columnLengths[col];
		}
		columnAggreator[col] = data;
	}

	public void setColumnCount(int col, int data) {
		if (String.valueOf(String.valueOf(data)).length() > columnLengths[col]) {
			sumOfColumnLengths -= columnLengths[col];
			columnLengths[col] = String.valueOf(data).length();
			sumOfColumnLengths += columnLengths[col];
		}
		columnCount[col] = data;
	}

	public void addCellData(int data) {

		if (String.valueOf(decimalFormat.format(data)).length() > columnLengths[currentColumnIndex]) {
			sumOfColumnLengths -= columnLengths[currentColumnIndex];
			columnLengths[currentColumnIndex] = String.valueOf(decimalFormat.format(data)).length();
			sumOfColumnLengths += columnLengths[currentColumnIndex];
		}
		((Number[]) tableData.lastElement())[currentColumnIndex++] = new Integer(data);
	}

	public void addCellData(double data) {
		if (String.valueOf(decimalFormat.format(data)).length() > columnLengths[currentColumnIndex]) {
			sumOfColumnLengths -= columnLengths[currentColumnIndex];
			columnLengths[currentColumnIndex] = String.valueOf(decimalFormat.format(data)).length();
			sumOfColumnLengths += columnLengths[currentColumnIndex];
		}
		((Number[]) tableData.lastElement())[currentColumnIndex++] = new Double(data);
	}

	public void addCellData(int nominator, int demoninator) {
		Number data = new Double(((double) nominator) / demoninator);
		if (String.valueOf(decimalFormat.format(data)).length() > columnLengths[currentColumnIndex]) {
			sumOfColumnLengths -= columnLengths[currentColumnIndex];
			columnLengths[currentColumnIndex] = String.valueOf(decimalFormat.format(data)).length();
			sumOfColumnLengths += columnLengths[currentColumnIndex];
		}
		((Number[]) tableData.lastElement())[currentColumnIndex++] = data;
	}

	public void addCellData(Number data) {
		if (data == null) {
			data = Double.NaN;
		}
		if (String.valueOf(decimalFormat.format(data)).length() > columnLengths[currentColumnIndex]) {
			sumOfColumnLengths -= columnLengths[currentColumnIndex];
			columnLengths[currentColumnIndex] = String.valueOf(decimalFormat.format(data)).length();
			sumOfColumnLengths += columnLengths[currentColumnIndex];
		}
		((Number[]) tableData.lastElement())[currentColumnIndex++] = data;
	}

	public String getRowHeadingsHeading() {
		return rowHeadingsHeading;
	}

	public String getColumnsHeader(int i) {
		return (String) columnsHeaders[i];
	}

	public String getRowHeaders(int i) {
		return (String) rowHeaders.get(i);
	}

	public Number getEvaluationData(int row, int col) {
		return ((Number[]) tableData.get(row))[col];
	}

	public double getColumnAggregationData(int col) {
		return columnAggreator[col];
	}

	public int getColumnCountData(int col) {
		return columnCount[col];
	}

	public int getRowCount() {
		return rowHeaders.size();
	}

	public int getColumnCount() {
		return columnsHeaders.length;
	}

	public Number getMinValue(int col) {
		int index = 0;
		for (int i = 1; i < getRowCount(); i++) {
			if (getEvaluationData(i, col).doubleValue() < getEvaluationData(index, col).doubleValue()) {
				index = i;
			}
		}
		return getEvaluationData(index, col);
	}

	public Number getMaxValue(int col) {
		int index = 0;
		for (int i = 1; i < getRowCount(); i++) {
			if (getEvaluationData(i, col).doubleValue() > getEvaluationData(index, col).doubleValue()) {
				index = i;
			}
		}
		return getEvaluationData(index, col);
	}

	public String toString() {
		int i, j;
		StringBuffer sb = new StringBuffer();

		if (printHeader) {
			if (caption_ != null) {
				sb.append(caption_ + "\n");
			}
		}
		for (j = 0; j < getColumnCount(); j++) {
			sb.append(getFormatedData(getColumnsHeader(j), j));
		}
		if (printRowHeading) {
			sb.append(getRowHeadingsHeading());
		}
		sb.append("\n");
		for (j = 0; j < sumOfColumnLengths + rowHeadersLength + (getColumnCount() * whiteSpaceDivisionCount); j++) {
			sb.append("-");
		}
		sb.append("\n");
		if (showTableColumnHeader_) {
			for (j = 0; j < getColumnCount(); j++) {
				if (!Double.isNaN(getColumnAggregationData(j))) {
					sb.append(getFormatedData(decimalFormat.format(getColumnAggregationData(j)), j));
				} else {
					sb.append(getFormatedData("-", j));
				}
			}
			if (printRowHeading) {
				sb.append(aggreatorHeader);
			}
			sb.append("\n");
			for (j = 0; j < getColumnCount(); j++) {
				if (!Double.isNaN(getColumnCountData(j))) {
					sb.append(getFormatedData(getColumnCountData(j), j));
				} else {
					sb.append(getFormatedData("-", j));
				}
			}
			if (printRowHeading) {
				sb.append(countHeader);
			}
			sb.append("\n");
			for (j = 0; j < sumOfColumnLengths + rowHeadersLength + (getColumnCount() * whiteSpaceDivisionCount); j++) {
				sb.append("-");
			}
			sb.append("\n");
		}
		if (printDetails) {
			for (i = 0; i < getRowCount(); i++) {
				for (j = 0; j < getColumnCount(); j++) {
					if (getEvaluationData(i, j) != null) {
						if (getEvaluationData(i, j) instanceof Double) {
							if (!Double.isNaN((Double) getEvaluationData(i, j))) {
								sb.append(getFormatedData(decimalFormat.format(getEvaluationData(i, j).doubleValue()), j));
							} else {
								sb.append(getFormatedData("-", j));
							}
						} else {
							sb.append(getFormatedData(getEvaluationData(i, j), j));
						}
					} else {
						sb.append(getFormatedData("-", j));
					}
				}
				if (printRowHeading) {
					sb.append(getRowHeaders(i));
				}
				sb.append("\n");
			}
		}
		if (printHeader) {
			if (hasBeenCut_) {
				sb.append(removedRowsByCutCount + " more...");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	private String getFormatedData(Object o, int col) {
		int i;
		if (printSeparatoraAsTab) {
			return o.toString() + "\t";
		} else {
			StringBuffer sb = new StringBuffer(o.toString());
			for (i = o.toString().length(); i < columnLengths[col] + whiteSpaceDivisionCount; i++) {
				sb.append(" ");
			}
			return sb.toString();
		}
	}

	public static void setDecimalFormat(String pattern) {
		DataContainer.decimalFormat.applyPattern(pattern);
	}

	public static void setPrintSeparatoraAsTab(boolean psat) {
		printSeparatoraAsTab = psat;
	}

	public void sortRows(String attribute, boolean ascending, int rowsToShow) throws MaltEvalException {
		int i, columnToSort = 0;
		TreeMap<Object, Vector<Number[]>> sortTable;
		TreeMap<Object, Vector<String>> rowHeaderSortTable;
		Vector<Number[]> sortTableVector;
		Vector<String> rowHeaderSortTableVector;

		if (rowsToShow != -1 && rowsToShow < tableData.size()) {
			hasBeenCut_ = true;
			removedRowsByCutCount = tableData.size() - rowsToShow;
		}
		if (attribute.equals(rowHeadingsHeading)) {
			columnToSort = -1;
		} else {
			columnToSort = 0;
			for (String header : columnsHeaders) {
				if (attribute.equals(header)) {
					break;
				}
				columnToSort++;
			}
			if (columnToSort == getColumnCount()) {
				throw new MaltEvalException("Could not find the column \"" + attribute + "\" during sorting. \"" + attribute + "\" may not be a valid attribute for current grouping strategy.", this.getClass());
			}
		}
		sortTable = new TreeMap<Object, Vector<Number[]>>();
		rowHeaderSortTable = new TreeMap<Object, Vector<String>>();
		while (getRowCount() > 0) {
			if (columnToSort == -1) {
				if ((sortTableVector = sortTable.get(getRowHeaders(0))) == null) {
					sortTableVector = new Vector<Number[]>();
					sortTable.put(getRowHeaders(0), sortTableVector);
					rowHeaderSortTableVector = new Vector<String>();
					rowHeaderSortTable.put(getRowHeaders(0), rowHeaderSortTableVector);
				} else {
					rowHeaderSortTableVector = rowHeaderSortTable.get(getRowHeaders(0));
				}
				sortTableVector.add(0, tableData.get(0));
				rowHeaderSortTableVector.add(0, rowHeaders.get(0));
			} else {
				if ((sortTableVector = sortTable.get(getEvaluationData(0, columnToSort))) == null) {
					sortTableVector = new Vector<Number[]>();
					sortTable.put(getEvaluationData(0, columnToSort), sortTableVector);
					rowHeaderSortTableVector = new Vector<String>();
					rowHeaderSortTable.put(getEvaluationData(0, columnToSort), rowHeaderSortTableVector);
				} else {
					rowHeaderSortTableVector = rowHeaderSortTable.get(getEvaluationData(0, columnToSort));
				}
				sortTableVector.add(0, tableData.get(0));
				rowHeaderSortTableVector.add(0, rowHeaders.get(0));
			}
			tableData.remove(0);
			rowHeaders.remove(0);
		}
		for (Object o : sortTable.keySet()) {
			for (Number[] row : sortTable.get(o)) {
				tableData.add(row);
			}
			for (String header : rowHeaderSortTable.get(o)) {
				rowHeaders.add(header);
			}
		}
		if (!ascending) {
			for (i = 0; i < tableData.size(); i++) {
				tableData.add(i, tableData.remove(tableData.size() - 1));
				rowHeaders.add(i, rowHeaders.remove(rowHeaders.size() - 1));
			}
		}
		if (rowsToShow != -1 && rowsToShow < tableData.size()) {
			tableData = new Vector<Number[]>(tableData.subList(0, rowsToShow));
			rowHeaders = new Vector<String>(rowHeaders.subList(0, rowsToShow));
		}
	}

	public Vector<Number> getColumn(int col) {
		int i;
		Vector<Number> column = new Vector<Number>();
		for (i = 0; i < getRowCount(); i++) {
			column.add(getEvaluationData(i, col));
		}
		return column;
	}

	public Vector<Boolean> getColumnAsBoolean(int col) throws MaltEvalException {
		int i;
		Vector<Boolean> column = new Vector<Boolean>();
		for (i = 0; i < getRowCount(); i++) {
			Integer value;
			if (getEvaluationData(i, col) instanceof Integer) {
				value = (Integer) getEvaluationData(i, col);
				if ((value.intValue() == 0 || value.intValue() == 1)) {
					column.add((getEvaluationData(i, col).equals(1) ? true : false));
				} else {
					throw new MaltEvalException("Integer values of column." + getColumnsHeader(col) + " should be either 0 or 1.", this
							.getClass());
				}
			} else {
				throw new MaltEvalException("Could not convert a column to booleans.", this.getClass());
			}
		}
		return column;
	}

	public Evaluation getEvaluation() {
		return evaluation_;
	}

	public void setEvaluation(Evaluation evaluation) {
		this.evaluation_ = evaluation;
	}

	public DataContainer getConfusionMatrix() {
		return confusionMatrix_;
	}

	public void setConfusionMatrix(DataContainer confusionMatrix) {
		this.confusionMatrix_ = confusionMatrix;
	}

	public DataContainer getConfusionTable() {
		return confusionTable_;
	}

	public void setConfusionTable(DataContainer confusionTable) {
		this.confusionTable_ = confusionTable;
	}

	public String getCaption() {
		return caption_;
	}

	public void setCaption(String caption) {
		caption_ = caption;
	}

	static public DataContainer collectAggregationData(DataContainer[] edcs) {
		int i, j;
		DataContainer edc = new DataContainer(edcs[0].getColumnCount(), true, true);
		double[] sum = new double[edcs[0].getColumnCount()];
		int[] count = new int[edcs[0].getColumnCount()];

		for (i = 0; i < edcs.length; i++) {
			edc.addRow("<" + (i + 1) + ">");
			for (j = 0; j < edcs[i].getColumnCount(); j++) {
				if (i == 0) {
					edc.setColumnHeading("Agg. " + edcs[0].getColumnsHeader(j));
				}
				if (!Double.isNaN(edcs[i].getColumnAggregationData(j))) {
					sum[j] += edcs[i].getColumnAggregationData(j);
					count[j]++;
				}
				edc.addCellData(edcs[i].getColumnAggregationData(j));
			}
		}
		for (j = 0; j < edcs[0].getColumnCount(); j++) {
			edc.setColumnCount(j, edcs.length);
			if (count[j] != 0) {
				edc.setColumnAggregation(j, sum[j] / count[j]);
			} else {
				edc.setColumnAggregation(j, Double.NaN);
			}
		}
		return edc;
	}

	static public DataContainer merge(DataContainer[] edcs, GenericEvaluationParameters gep) throws MaltEvalException {
		DataContainer merged;
		int i, j, k, columnCount;
		Vector<String> headers = new Vector<String>();
		Vector<Integer> rowCounts = new Vector<Integer>();
		Vector<Double> rowMeans = new Vector<Double>();
		if (edcs.length > 0) {
			Evaluation combinedEvaluation = gep.getMimicOfMergedEvaluation(false);
			columnCount = 0;
			gep.createExperimentEngineInputDataArray();
			for (i = 0; i < edcs.length; i++) {
				if (i > 0
						&& (edcs[i].getRowCount() != edcs[i - 1].getRowCount() || !edcs[i].getRowHeadingsHeading().equals(
								edcs[i - 1].getRowHeadingsHeading()))) {
					throw new MaltEvalException(
							"Error: cannot merge DataContainer with different number of rows or different row headings heading.", null);
				}
				columnCount += edcs[i].getColumnCount();
				for (j = 0; j < edcs[i].getColumnCount(); j++) {
					headers.add(edcs[i].getColumnsHeader(j) + " / "
							+ gep.getNextEvaluationParameters().getMutualEvaluationValues(combinedEvaluation));
					rowCounts.add(edcs[i].getColumnCountData(j));
					rowMeans.add(edcs[i].getColumnAggregationData(j));
				}
				gep.incToNextEvaluationParameters();
			}
			merged = new DataContainer(columnCount, edcs[0].isPrintOnlyAggregated(), edcs[0].isShowTableHeader(), edcs[0]
					.getRowHeadingsHeading());
			merged.setEvaluation(gep.getMimicOfMergedEvaluation(true));
			for (i = 0; i < merged.getColumnCount(); i++) {
				merged.setColumnHeading(headers.get(i));
				merged.setColumnCount(i, rowCounts.get(i));
				merged.setColumnAggregation(i, rowMeans.get(i));
			}
			for (i = 0; i < edcs[0].getRowCount(); i++) {
				merged.addRow(edcs[0].getRowHeaders(i));
				for (j = 0; j < edcs.length; j++) {
					if (j > 0 && !edcs[j].getRowHeaders(i).equals(edcs[j - 1].getRowHeaders(i))) {
						throw new MaltEvalException("Error: cannot merge DataContainer with different row headers.", null);
					}
					for (k = 0; k < edcs[j].getColumnCount(); k++) {
						merged.addCellData(edcs[j].getEvaluationData(i, k));
					}
				}
			}
			return merged;
		}
		return null;
	}

	static public boolean areMergable(DataContainer[] edcs) throws MaltEvalException {
		int i, j;
		if (edcs.length > 0) {
			for (i = 0; i < edcs.length; i++) {
				if (edcs[i] == null) {
					System.out.println();
				}
				if (i > 0
						&& (edcs[i].getRowCount() != edcs[i - 1].getRowCount() || !edcs[i].getRowHeadingsHeading().equals(
								edcs[i - 1].getRowHeadingsHeading()))) {
					return false;
				}
			}
			for (i = 1; i < edcs[0].getRowCount(); i++) {
				for (j = 0; j < edcs.length; j++) {
					if (j > 0 && !edcs[j].getRowHeaders(i).equals(edcs[j - 1].getRowHeaders(i))) {
						return false;
					}
				}
			}

		}
		return true;
	}

	public boolean isPrintOnlyAggregated() {
		return printDetails;
	}

	public boolean isShowTableHeader() {
		return showTableColumnHeader_;
	}
}
