/*
 * Created on 2004-aug-23
 */
package se.vxu.msi.malteval.configdata.util;

import java.util.Iterator;
import java.util.TreeMap;

/**
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class Feature {
	private String name;
	private String defaultValue;
	private String valueFormat;
	private TreeMap<String,String> values;
	
	public Feature() {
		name = null;
		defaultValue = null;
		valueFormat = null;
		values = new TreeMap<String,String>();
	}
	
	public void setName(String n) {
		name = n;
	}
	
	public void setDefaultValue(String d, String format) {
		defaultValue = d;
		valueFormat = format;
	}
	
	public void addValue(String value, String format) {
		values.put(value, format);
	}
	
	public String getName() {
		return name;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
	
	public String getFormat() {
		return valueFormat;
	}
	
	public boolean isValue(String v) {
		return values.keySet().contains(v);
	}
	
	public boolean isValuesEmpty() {
		return values.isEmpty();
	}
	
	public int getValuesCount() {
		return values.size();
	}
	
	public String[] getValues() {
		return (String[]) values.keySet().toArray(new String[0]);
	}
	
	public Iterator<String> getValuesIterator() {
		return values.keySet().iterator();
	}
	
	public String getFormat(String value) {
		return values.get(value);
	}
	
	public String toString() {
		Iterator<String> i;
		StringBuffer sb = new StringBuffer();
		sb.append(name + " " + defaultValue + "\n");
		i = getValuesIterator();
		while (i.hasNext()) {
			sb.append(i.next().toString() + " ");
		}
		sb.append("\n");
		return sb.toString();
	}
}
