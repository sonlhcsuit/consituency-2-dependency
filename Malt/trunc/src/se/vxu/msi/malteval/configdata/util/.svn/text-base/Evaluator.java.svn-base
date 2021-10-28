/*
 * Created on 2004-aug-23
 */
package se.vxu.msi.malteval.configdata.util;

import java.util.Iterator;
import java.util.Vector;

/**
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class Evaluator {
	private String name;
	private String type;
	private String url;
	private String urlType;
	private Vector<Feature> options;
	private Vector<Requirement> requirements;

	public Evaluator() {
		name = null;
		type = null;
		url = null;
		options = new Vector<Feature>();
		requirements = new Vector<Requirement>();
	}

	public void setName(String n) {
		name = n;
	}

	public void setType(String t) {
		type = t;
	}

	public void setUrl(String u) {
		url = u;
	}

	public void setUrlType(String u) {
		urlType = u;
	}

	public void addOptionFeature(Feature f) {
		options.add(f);
	}

	public void addRequirement(Requirement r) {
		requirements.add(r);
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getUrl() {
		return url;
	}

	public String getUrlType() {
		return urlType;
	}

	public Iterator<Feature> getOptionFeatureIterator() {
		return options.iterator();
	}

	public Feature getOptionFeature(int index) {
		return options.get(index);
	}

	public int getOptionsCount() {
		return options.size();
	}

	public String[] getFeatureNames() {
		String[] fn = new String[options.size()];
		for (int i = 0; i < options.size(); i++) {
			fn[i] = options.get(i).getName();
		}
		return fn;
	}

	public String[] getDefaultValues() {
		String[] dv = new String[options.size()];
		for (int i = 0; i < options.size(); i++) {
			dv[i] = options.get(i).getDefaultValue();
		}

		return dv;
	}

	public String[] getDefaultFormats() {
		String[] dv = new String[options.size()];
		for (int i = 0; i < options.size(); i++) {
			dv[i] = options.get(i).getFormat();
		}

		return dv;
	}

	public Requirement getRequirement(int index) {
		return requirements.get(index);
	}

	public Iterator<Requirement> getRequirementIterator() {
		return requirements.iterator();
	}

	public int getRequirementCount() {
		return requirements.size();
	}
	
	public String toString() {
		Iterator<?> i;
		StringBuffer sb = new StringBuffer();
		sb.append(name + "\n" + type + "\n" + url + "\t" + urlType + "\n");
		i = options.iterator();
		while (i.hasNext()) {
			sb.append(i.next().toString() + "\n");
		}
		sb.append("\n");
		i = requirements.iterator();
		while (i.hasNext()) {
			sb.append(i.next().toString() + "\n");
		}
		return sb.toString();
	}
}