/*
 * Created on 2004-sep-08
 */
package se.vxu.msi.malteval.configdata.util;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class Evaluation {
	private HashMap<String, String> parameters;
	private HashMap<String, String> parametersFormat;
	
	public Evaluation() {
		parameters = new HashMap<String, String>();
		parametersFormat = new HashMap<String, String>();
	}

	public void addParameter(String name, String value, String format) {
		parameters.put(name, value);
		parametersFormat.put(name, format);
	}
	
	public String getParameterValue(String name) {
		return (String) parameters.get(name);
	}
	
	public String getParameterFormat(String name) {
		return (String) parametersFormat.get(name);
	}
	
	public Iterator<String> getParameterKeysAsIterator() {
		return parameters.keySet().iterator();
	}
	
	public String toString() {
		String name;
		StringBuffer sb = new StringBuffer();
		Iterator<String> i = getParameterKeysAsIterator();
		while (i.hasNext()) {
			name = (String) i.next();
			sb.append(name + "-> " + getParameterValue(name));
			if (getParameterFormat(name) != null && !getParameterFormat(name).equals("")) {
				sb.append(":" + getParameterFormat(name));
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public String getMutualEvaluationValues(Evaluation evaluation) {
		StringBuffer sb = new StringBuffer();
		Iterator<String> parametersIterator = evaluation.getParameterKeysAsIterator();
		while (parametersIterator.hasNext()) {
			String parameter = parametersIterator.next();
			if (getParameterValue(parameter) != null) {
				sb.append(parameter + ":" + getParameterValue(parameter) + ", ");
			}
		}
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
}
