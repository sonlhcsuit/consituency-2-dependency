/*
 * Created on 2004-aug-23
 */
package se.vxu.msi.malteval.configdata;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import se.vxu.msi.malteval.MaltEvalConfig;
import se.vxu.msi.malteval.datareaders.XMLEmptyNodes;
import se.vxu.msi.malteval.evaluator.MaltParserEvaluator;
import se.vxu.msi.malteval.exceptions.MaltEvalException;
import se.vxu.msi.malteval.util.DataContainer;

/**
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class ReadEvaluationSetup {
	private GenericExperimentSetup experimentSetup;
	private String outputPath_;
	private boolean printSettingsFile_;
	private boolean computeStatisticalSignificance_;

	public ReadEvaluationSetup(GenericExperimentSetup es) {
		experimentSetup = es;
		setDefaultFormatting();
	}

	public void readFromFile(String filename) throws MaltEvalException {
		DocumentBuilderFactory docBuilderFactory;
		DocumentBuilder docBuilder;
		Document doc = null;
		try {
			docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docBuilderFactory.newDocumentBuilder();
			doc = docBuilder.parse(new File(filename));
			getExperimentSetup(doc.getDocumentElement());
		} catch (SAXException saxe) {
			throw new MaltEvalException(saxe.toString(), this.getClass());
		} catch (ParserConfigurationException pce) {
			throw new MaltEvalException(pce.toString(), this.getClass());
		} catch (IOException ioe) {
			throw new MaltEvalException("Error: I/O exception. File " + filename + " might be missing:\n" + ioe.toString(), this.getClass());
		} catch (NullPointerException npe) {
			throw new MaltEvalException("Error when reading the file " + filename + ": incorrect file content:\n" + npe.toString(), this.getClass());
		}
	}

	public void setDefultEvaluation() throws MaltEvalException {
		HashMap<String, String> values;
		values = new HashMap<String, String>();
		values.put(MaltEvalConfig.DEFAULT_METRIC, "");
		experimentSetup.addEvaluationParameter(MaltEvalConfig.METRIC, values);
		values = new HashMap<String, String>();
		values.put(MaltEvalConfig.DEFAULT_GROUPING, "");
		experimentSetup.addEvaluationParameter(MaltEvalConfig.GROUP_BY, values);
	}

	public void setDefaultFormatting() {
		DataContainer.printHeader = true;
		//DataContainer.printRowHeading = true;
		//DataContainer.overridePrintOnlyAggregated = new Boolean(true);
		DataContainer.setPrintSeparatoraAsTab(false);
		outputPath_ = MaltEvalConfig.STDOUT;
		printSettingsFile_ = false;
		computeStatisticalSignificance_ = false;
	}

	public void setFormattingValue(String argument, String format) throws MaltEvalException {
		if (argument.equals(MaltEvalConfig.PRINT_TABLE_HEADER_INFO)) {
			if (format.equals("0") || format.equals("1")) {
				DataContainer.printHeader = format.equals("1");
			} else {
				throw new MaltEvalException("Incorrect value (" + format + ") for argument " + argument, this.getClass());
			}
		} else if (argument.equals(MaltEvalConfig.PRINT_TABLE_ROW_HEADING_INFO)) {
			if (format.equals("0") || format.equals("1")) {
				DataContainer.printRowHeading = format.equals("1");
			} else {
				throw new MaltEvalException("Incorrect value (" + format + ") for argument " + argument, this.getClass());
			}
		} else if (argument.equals(MaltEvalConfig.PRINT_TABLE_COLUMN_HEADING_INFO)) {
			if (format.equals("0") || format.equals("1")) {
				DataContainer.overrideShowTableColumnHeaderInfo_ = format.equals("1");
			} else {
				throw new MaltEvalException("Incorrect value (" + format + ") for argument " + argument, this.getClass());
			}
		} else if (argument.equals(MaltEvalConfig.PRINT_DETAILS)) {
			if (format.equals("0") || format.equals("1")) {
				DataContainer.overridePrintDetails = format.equals("1");
			} else {
				throw new MaltEvalException("Incorrect value (" + format + ") for argument " + argument, this.getClass());
			}
		} else if (argument.equals(MaltEvalConfig.TAB_SEPARATION)) {
			if (format.equals("0") || format.equals("1")) {
				DataContainer.setPrintSeparatoraAsTab(format.equals("1"));
			} else {
				throw new MaltEvalException("Incorrect value (" + format + ") for argument " + argument, this.getClass());
			}
		} else if (argument.equals(MaltEvalConfig.SEPARATE_SETTINGS)) {
			if (format.equals("0") || format.equals("1")) {
				setPrintSettingsFile(format.equals("1"));
			} else {
				throw new MaltEvalException("Incorrect value (" + format + ") for argument " + argument, this.getClass());
			}
		} else if (argument.equals(MaltEvalConfig.COMPUTE_STATISTICAL_SIGNIFICANCE)) {
			if (format.equals("0") || format.equals("1")) {
				computeStatisticalSignificance_ = format.equals("1");
			} else {
				throw new MaltEvalException("Incorrect value (" + format + ") for argument " + argument, this.getClass());
			}
		} else if (argument.equals(MaltEvalConfig.COMPUTE_CONFUSION_MATRIX)) {
			if (format.equals("0") || format.equals("1")) {
				MaltParserEvaluator.COMPUTE_CONFUSION_MATRIX = format.equals("1");
			} else {
				throw new MaltEvalException("Incorrect value (" + format + ") for argument " + argument, this.getClass());
			}
		} else if (argument.equals(MaltEvalConfig.MERGE_TABLES)) {
			if (format.equals("0") || format.equals("1")) {
				MaltEvalConfig.MERGE_TABLES_VALUE = format.equals("1");
			} else {
				throw new MaltEvalException("Incorrect value (" + format + ") for argument " + argument, this.getClass());
			}
		} else if (argument.equals(MaltEvalConfig.MERGE_CROSSVALIDATION_FILES)) {
			if (format.equals("0") || format.equals("1")) {
				MaltEvalConfig.MICRO_AVERAGE = format.equals("1");
			} else {
				throw new MaltEvalException("Incorrect value (" + format + ") for argument " + argument, this.getClass());
			}
		} else if (argument.equals(MaltEvalConfig.DEBUG)) {
			if (format.equals("0") || format.equals("1")) {
				MaltEvalConfig.DEBUG_MODE = format.equals("1");
			} else {
				throw new MaltEvalException("Incorrect value (" + format + ") for argument " + argument, this.getClass());
			}
		} else if (argument.equals(MaltEvalConfig.DECIMAL_PATTERN)) {
			DataContainer.setDecimalFormat(format);
		} else if (argument.equals(MaltEvalConfig.OUTPUT_DIRECTORY)) {
			setOutputPath(format);
		} else {
			throw new MaltEvalException("Expected some other formatting argument. Found: " + argument, this.getClass());
		}
	}

	public void setParameterValue(String parameterName, String values) throws MaltEvalException {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		String[] s1, s2;
		if (parameterName.startsWith("--")) {
			s1 = values.split(";");
			hashMap.clear();
			for (String item : s1) {
				s2 = item.split(":");
				if (s2.length == 1) {
					hashMap.put(s2[0], "");
				} else if (s2.length == 2) {
					hashMap.put(s2[0], s2[1]);
				} else {
					throw new MaltEvalException("Could not parse value of the argument \"" + parameterName + "\"", this.getClass());
				}
			}
			experimentSetup.addEvaluationParameter(parameterName.substring(2), hashMap);
		} else {
			throw new MaltEvalException("Parameter arguments should start with \"--\".", this.getClass());
		}
	}

	private void getExperimentSetup(Node node) throws MaltEvalException {
		Node element, parameter;
		element = node;
		if (element.getNodeType() == Node.ELEMENT_NODE && element.getNodeName().equals("evaluation")) {
			Iterator<Node> evaluationParameters = XMLEmptyNodes.removeEmptyNodes(element.getChildNodes()).iterator();
			while (evaluationParameters.hasNext()) {
				parameter = evaluationParameters.next();
				if (parameter.getNodeName().equals("parameter")) {
					experimentSetup.addEvaluationParameter(parameter.getAttributes().getNamedItem("name").getNodeValue().trim(),
							getValues(XMLEmptyNodes.removeEmptyNodes(parameter.getChildNodes()).iterator()));
				} else if (parameter.getNodeName().equals("formatting")) {
					setFormattingValue("--" + parameter.getAttributes().getNamedItem("argument").getNodeValue().trim(), parameter
							.getAttributes().getNamedItem("format").getNodeValue().trim());
				} else if (parameter.getNodeName().equals("#comment")) {
				} else {
					throw new MaltEvalException("Incorrect element name. Found \"" + parameter.getNodeName()
							+ "\", expected element node: \"parameter\"", this.getClass());
				}
			}
		} else {
			throw new MaltEvalException("Incorrect element name. Found \"" + element.getNodeName()
					+ "\", expected element node: \"evaluations\"", this.getClass());
		}
	}

	private HashMap<String, String> getValues(Iterator<Node> nodes) throws MaltEvalException {
		Node node;
		HashMap<String, String> values = new HashMap<String, String>();
		while (nodes.hasNext()) {
			node = nodes.next();
			if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("value")) {
				Vector<Node> value = XMLEmptyNodes.removeEmptyNodes(node.getChildNodes());
				if (value.size() == 1 && value.get(0).getNodeType() == Node.TEXT_NODE) {
					if (node.getAttributes().getNamedItem("format") == null) {
						values.put(value.get(0).getNodeValue().trim(), "");
					} else {
						values.put(value.get(0).getNodeValue().trim(), node.getAttributes().getNamedItem("format").getNodeValue().trim());
					}
				} else {
					values.put("", "");
				}
			} else {
				throw new MaltEvalException("Incorrect element name. Found \"" + node.getNodeName()
						+ "\", expected element node: \"value\"", this.getClass());
			}
		}
		return values;
	}

	public GenericEvaluationParameters getGenericEvaluationParameters() throws MaltEvalException {
		return experimentSetup.getGenericEvaluationParameters();
	}

	public String getOutputPath() {
		return outputPath_;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath_ = outputPath;
	}

	public boolean isPrintSettingsFile() {
		return printSettingsFile_;
	}

	public void setPrintSettingsFile(boolean printSettingsFile) {
		this.printSettingsFile_ = printSettingsFile;
	}

	public boolean isComputeStatisticalSignificance() {
		return computeStatisticalSignificance_;
	}
}