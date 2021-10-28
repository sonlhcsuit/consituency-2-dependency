/*
 * Created on 2006 sep 4
 */
package se.vxu.msi.malteval.selectionfile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Vector;

import se.vxu.msi.malteval.corpus.MaltTreebank;

/**
 * You are discouraged to use this class. Only used for compatibility reasons.
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class SelectionFileComputation {
	Vector<SelectionFileLine> selectionFileLines;
	MaltTreebank goldStandardFile;
	MaltTreebank parserFile;

	/**
	 * @param gf
	 *            gold-standard file
	 * @param pf
	 *            parser output file
	 * @param sf
	 *            selection file
	 */
	public SelectionFileComputation(MaltTreebank gf, MaltTreebank pf, String sf) {
		goldStandardFile = gf;
		parserFile = pf;
		SelectionFileLine.setLiftedDeprels(gf.getDeprels());
		readSelectionFile(sf);
	}

	private void readSelectionFile(String selectionFile) {
		BufferedReader br;
		String fileLine;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(selectionFile)));
			selectionFileLines = new Vector<SelectionFileLine>();
			while ((fileLine = br.readLine()) != null) {
				selectionFileLines.add(new SelectionFileLine(fileLine));
			}
			br.close();
		} catch (FileNotFoundException fnfe) {
			System.err.println("The file " + selectionFile + " doesn't exist");
			System.exit(1);
		} catch (IOException ioe) {
			System.err.println("File error");
			System.exit(1);
		}
	}

	public void compute() {
		BufferedWriter bwH, bwHD;
		DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
		int sentenceId, wordId;
		int i, deprelIndex, syntacticHeadDeprel = -1, liftedDeprelIndex;
		int syntacticHead;
		SelectionFileLine sfl;
		int totalCount = 0;
		int unlabeledCorrectCount = 0;
		int labeledCorrectCount = 0;
		int labelCorrectCount = 0;

		int perDeprelCounter[] = new int[SelectionFileLine.getDeprels().size()];
		int perDeprelLabeledCorrect[] = new int[SelectionFileLine.getDeprels().size()];
		int perDeprelUnlabeledCorrect[] = new int[SelectionFileLine.getDeprels().size()];
		int perDeprelLabelCorrect[] = new int[SelectionFileLine.getDeprels().size()];

		int perLiftedDeprelCounter[] = new int[SelectionFileLine.getLiftedDeprels().size()];
		int perLiftedDeprelLabeledCorrect[] = new int[SelectionFileLine.getLiftedDeprels().size()];
		int perLiftedDeprelUnlabeledCorrect[] = new int[SelectionFileLine.getLiftedDeprels().size()];
		int perLiftedDeprelLabelCorrect[] = new int[SelectionFileLine.getLiftedDeprels().size()];

		int perSyntacticHeadDeprelCounter[] = new int[SelectionFileLine.getLiftedDeprels().size()];
		int perSyntacticHeadDeprelLabeledCorrect[] = new int[SelectionFileLine.getLiftedDeprels().size()];
		int perSyntacticHeadDeprelUnlabeledCorrect[] = new int[SelectionFileLine.getLiftedDeprels().size()];
		int perSyntacticHeadDeprelLabelCorrect[] = new int[SelectionFileLine.getLiftedDeprels().size()];

		try {
			bwH = new BufferedWriter(new FileWriter("head.dat"));
			bwHD = new BufferedWriter(new FileWriter("head_deprel.dat"));
			for (i = 0; i < selectionFileLines.size(); i++) {
				sfl = (SelectionFileLine) selectionFileLines.get(i);
				deprelIndex = sfl.getDeprelIndex();

				sentenceId = sfl.getSentenceId() - 1;
				wordId = sfl.getWordId() - 1;
				syntacticHead = goldStandardFile.getSentence(sentenceId).getWord(wordId).getHead();
				if (syntacticHead != 0) {
					syntacticHeadDeprel = SelectionFileLine.getLiftedDeprelIndex(goldStandardFile.getSentence(sentenceId).getWord(
							syntacticHead - 1).getDeprel());
				}
				liftedDeprelIndex = sfl.getLiftedDeprelIndex();
				if (goldStandardFile.getSentence(sentenceId).getWord(wordId).getHead() == parserFile.getSentence(sentenceId)
						.getWord(wordId).getHead()
						&& goldStandardFile.getSentence(sentenceId).getWord(wordId).getDeprel().equals(
								parserFile.getSentence(sentenceId).getWord(wordId).getDeprel())) {
					labeledCorrectCount++;
					perDeprelLabeledCorrect[deprelIndex]++;
					perLiftedDeprelLabeledCorrect[liftedDeprelIndex]++;
					if (syntacticHeadDeprel != -1) {
						perSyntacticHeadDeprelLabeledCorrect[syntacticHeadDeprel]++;
					}
					bwHD.write("1\n");
				} else {
					bwHD.write("0\n");
				}
				if (goldStandardFile.getSentence(sentenceId).getWord(wordId).getDeprel().equals(
						parserFile.getSentence(sentenceId).getWord(wordId).getDeprel())) {
					labelCorrectCount++;
					perDeprelLabelCorrect[deprelIndex]++;
					perLiftedDeprelLabelCorrect[liftedDeprelIndex]++;
					if (syntacticHeadDeprel != -1) {
						perSyntacticHeadDeprelLabelCorrect[syntacticHeadDeprel]++;
					}
				}
				if (goldStandardFile.getSentence(sentenceId).getWord(wordId).getHead() == parserFile.getSentence(sentenceId)
						.getWord(wordId).getHead()) {
					unlabeledCorrectCount++;
					perDeprelUnlabeledCorrect[deprelIndex]++;
					perLiftedDeprelUnlabeledCorrect[liftedDeprelIndex]++;
					if (syntacticHeadDeprel != -1) {
						perSyntacticHeadDeprelUnlabeledCorrect[syntacticHeadDeprel]++;
					}
					bwH.write("1\n");
				} else {
					bwH.write("0\n");
				}
				totalCount++;
				perDeprelCounter[deprelIndex]++;
				perLiftedDeprelCounter[liftedDeprelIndex]++;
				if (syntacticHeadDeprel != -1) {
					perSyntacticHeadDeprelCounter[syntacticHeadDeprel]++;
				}
			}
			bwH.close();
			bwHD.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("labeledCorrectMean:\t" + df.format(((double) labeledCorrectCount / totalCount)));
		System.out.println("labelCorrectMean:\t" + df.format(((double) labelCorrectCount / totalCount)));
		System.out.println("unlabeledCorrectMean:\t" + df.format(((double) unlabeledCorrectCount / totalCount)));
		System.out.println("Counter:\t" + totalCount);
		System.out.println();

		System.out.println("Per deprel:\n\t#\tLH\tL\tH");
		totalCount = 0;
		for (i = 0; i < perDeprelCounter.length; i++) {
			System.out.println(SelectionFileLine.getDeprelFromIndex(i) + ":\t" + perDeprelCounter[i] + "\t"
					+ df.format(((double) perDeprelLabeledCorrect[i] / perDeprelCounter[i])) + "\t"
					+ df.format(((double) perDeprelLabelCorrect[i] / perDeprelCounter[i])) + "\t"
					+ df.format((double) perDeprelUnlabeledCorrect[i] / perDeprelCounter[i]));
			totalCount += perDeprelCounter[i];
		}
		System.out.println("Counter:\t" + totalCount);

		System.out.println("Per lifted deprel:\n\t#\tLH\tL\tH");
		totalCount = 0;
		for (i = 0; i < perLiftedDeprelCounter.length; i++) {
			System.out.println(SelectionFileLine.getLiftedDeprelFromIndex(i) + ":\t" + perLiftedDeprelCounter[i] + "\t"
					+ df.format(((double) perLiftedDeprelLabeledCorrect[i] / perLiftedDeprelCounter[i])) + "\t"
					+ df.format(((double) perLiftedDeprelLabelCorrect[i] / perLiftedDeprelCounter[i])) + "\t"
					+ df.format((double) perLiftedDeprelUnlabeledCorrect[i] / perLiftedDeprelCounter[i]));
			totalCount += perLiftedDeprelCounter[i];
		}
		System.out.println("Counter:\t" + totalCount);

		System.out.println("Per syntactic head deprel:\n\t#\tLH\tL\tH");
		totalCount = 0;
		for (i = 0; i < perSyntacticHeadDeprelCounter.length; i++) {
			System.out.println(SelectionFileLine.getLiftedDeprelFromIndex(i) + ":\t" + perSyntacticHeadDeprelCounter[i] + "\t"
					+ df.format(((double) perSyntacticHeadDeprelLabeledCorrect[i] / perSyntacticHeadDeprelCounter[i])) + "\t"
					+ df.format(((double) perSyntacticHeadDeprelLabelCorrect[i] / perSyntacticHeadDeprelCounter[i])) + "\t"
					+ df.format((double) perSyntacticHeadDeprelUnlabeledCorrect[i] / perSyntacticHeadDeprelCounter[i]));
			totalCount += perLiftedDeprelCounter[i];
		}
		System.out.println("Counter:\t" + totalCount);
	}

}
