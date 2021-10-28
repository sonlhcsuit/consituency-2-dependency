/*
 * Created on 2004-aug-23
 */
package se.vxu.msi.malteval.configdata.util;

/**
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class Requirement {
	private String name;
	private boolean output;
	
	public Requirement() {
		name = null;
		output = false;
	}
	
	public void setName(String n) {
		name = n;
	}
	
	public void setOutput(String o) {
		output = Boolean.valueOf(o).booleanValue();
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isOutput() {
		return output;
	}
	
	public String toString() {
		return name + " " + output;
	}
}
