/*
 * Created on 13 mar 2008
 */
package se.vxu.msi.malteval;

import java.io.File;
import java.util.ArrayList;

public class CoNLLEval {

	static public String[][] getEvalParameters(String[] args) {
		int i;
		ArrayList<String> argsListWithoutOutputFlag = new ArrayList<String>();
		for (i = 0; i < args.length; i++) {
			if (args[i].equals(MaltEvalConfig.OUTPUT_DIRECTORY)) {
				i++;
			} else {
				argsListWithoutOutputFlag.add(args[i]);
			}
		}
		String[][] parameters = null;
		ArrayList<String> fileNames = new ArrayList<String>();
		File evalplDir = new File("evalpl_comparison");
		if (!evalplDir.exists()) {
			evalplDir = new File("../evalpl_comparison");
			if (!evalplDir.exists()) {
				System.err.println("Could not find evalpl_comparison directory. Aborting");
				System.exit(1);
			}
		}
		File[] filesArray = evalplDir.listFiles();
		for (File file : filesArray) {
			if (file.getName().endsWith(".xml")) {
				fileNames.add(file.getAbsolutePath());
			}
		}
		parameters = new String[fileNames.size()][];
		i = 0;
		for (String fileName : fileNames) {
			parameters[i] = argsListWithoutOutputFlag.toArray(new String[0]);
			parameters[i][1] = fileName;
			i++;
		}
		return parameters;
	}

	static public String[] getDescriptions() {
		String[] desciptions = new String[10];
		desciptions[0] = "";
		desciptions[1] = "###########################################################\n"
				+ "# The overall accuracy and its distribution over CPOSTAGs #\n"
				+ "###########################################################\n";
		desciptions[2] = "#############################################################\n"
				+ "# The overall error rate and its distribution over CPOSTAGs #\n"
				+ "#############################################################\n";
		desciptions[3] = "##################################\n" + "# Precision and recall of DEPREL #\n"
				+ "##################################\n";
		desciptions[4] = "###############################################\n" + "# Precision and recall of DEPREL + ATTACHMENT #\n"
				+ "###############################################\n";
		desciptions[5] = "#################################################\n" + "# Precision and recall of binned HEAD direction #\n"
				+ "#################################################\n";
		desciptions[6] = "################################################\n" + "# Precision and recall of binned HEAD distance #\n"
				+ "################################################\n";
		desciptions[7] = "####################\n" + "# Frame confusions #\n" + "####################\n";
		desciptions[8] = "################################################################\n"
				+ "# 5 focus words where most of the errors occur                 #\n"
				+ "# one-token preceeding contexts where most of the errors occur #\n"
				+ "# two-token preceeding contexts where most of the errors occur #\n"
				+ "# one-token following contexts where most of the errors occur  #\n"
				+ "# two-token following contexts where most of the errors occur  #\n"
				+ "################################################################\n";
		desciptions[9] = "#########################################################\n"
				+ "# Sentence with the highest number of word errors       #\n"
				+ "# Sentence with the highest number of head errors       #\n"
				+ "# Sentence with the highest number of dependency errors #\n"
				+ "#########################################################\n";
		return desciptions;
	}
}
