/*
 * Created on 7 nov 2007
 */
package se.vxu.msi.malteval.treeviewer.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import se.vxu.msi.malteval.MaltEvalAPI;
import se.vxu.msi.malteval.corpus.MaltTreebank;
import se.vxu.msi.malteval.evaluator.MaltParserEvaluator;
import se.vxu.msi.malteval.exceptions.MaltEvalException;
import se.vxu.msi.malteval.treeviewer.MaltTreeViewerGui;

public class MaltTreeViewerToolBar extends JToolBar implements ActionListener, ItemListener {
	private static final long serialVersionUID = -6424100727079185235L;

	private MaltTreeViewerGui maltTreeViewerGui_;

	private JComboBox searchIn;
	private JComboBox searchBy;
	private JComboBox searchFor;
	private JComboBox result;
	private JButton startSearch;
	private JCheckBox negateSearch;
	private JButton searchLeft;
	private JButton searchRight;

	private TreeMap<String, MaltTreebank> dataSets;
	private TreeMap<Integer, HashSet<Integer>> foundSentences;

	public MaltTreeViewerToolBar(MaltTreeViewerGui maltTreeViewerGui) {
		int i;
		setFloatable(false);
		maltTreeViewerGui_ = maltTreeViewerGui;
		foundSentences = null;
		dataSets = new TreeMap<String, MaltTreebank>();
		dataSets.put("-:Choose data:-", null);
		if (maltTreeViewerGui.getGsTreebank() != null) {
			dataSets.put("Gold-standard", maltTreeViewerGui.getGsTreebank());
		}
		for (i = 0; i < maltTreeViewerGui.getParsedTreebanks().size(); i++) {
			dataSets.put("Parsed " + (i + 1), maltTreeViewerGui.getParsedTreebanks().get(i));
		}
		add(new JLabel("Search in: "));
		searchIn = new JComboBox(dataSets.keySet().toArray());
		searchIn.addItemListener(this);
		add(searchIn);
		addSeparator();

		Iterator<String> groupByStrategiesIterator = MaltParserEvaluator.getGroupByStrategyNames();
		TreeSet<String> groupByStrategies = new TreeSet<String>();
		groupByStrategies.add("-:Choose search strategy:-");
		while (groupByStrategiesIterator.hasNext()) {
			groupByStrategies.add(groupByStrategiesIterator.next());
		}
		add(new JLabel("Search by: "));
		searchBy = new JComboBox(groupByStrategies.toArray());
		searchBy.addItemListener(this);
		add(searchBy);
		addSeparator();

		add(new JLabel("Search for: "));
		searchFor = new JComboBox();
		searchFor.setEditable(true);
		add(searchFor);
		negateSearch = new JCheckBox("Negate", false);
		add(negateSearch);

		startSearch = new JButton("Search");
		startSearch.addActionListener(this);
		add(startSearch);
		addSeparator();

		add(new JLabel("Result:"));
		result = new JComboBox();
		add(result);
		result.addItemListener(this);
		addSeparator();

		add(new JLabel("Search direction:"));
		searchLeft = new JButton("<<");
		searchLeft.addActionListener(this);
		add(searchLeft);

		searchRight = new JButton(">>");
		searchRight.addActionListener(this);
		add(searchRight);
	}

	private void search() {
		MaltTreebank data;
		result.setEnabled(false);
		searchLeft.setEnabled(false);
		searchRight.setEnabled(false);

		if ((data = dataSets.get(searchIn.getSelectedItem())) == null) {
			return;
		}
		if (searchBy.getSelectedIndex() == 0) {
			return;
		}
		if (searchFor.getSelectedItem().equals("")) {
			return;
		}
		try {
			foundSentences = MaltEvalAPI.simpleMaltEvalSearch(data, searchBy.getSelectedItem().toString(), searchFor.getSelectedItem()
					.toString(), negateSearch.isSelected());
			result.removeAllItems();
			for (Integer sentenceId : foundSentences.keySet()) {
				result.addItem(sentenceId);
			}
		} catch (MaltEvalException e1) {
			JOptionPane.showMessageDialog(maltTreeViewerGui_, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		if (result.getItemCount() > 0) {
			result.setEnabled(true);
			searchLeft.setEnabled(true);
			searchRight.setEnabled(true);
		}
	}

	private void updateSearchBy() {
		int i;
		MaltTreebank data;
		TreeSet<Object> values;
		searchFor.removeAllItems();
		if ((data = dataSets.get(searchIn.getSelectedItem())) == null) {
			return;
		}
		if (searchBy.getSelectedIndex() == 0) {
			return;
		}
		i = 0;
		try {
			values = MaltEvalAPI.getAllMaltEvalSearchValues(data, searchBy.getSelectedItem().toString());
			for (Object item : values) {
				searchFor.addItem(item);
				i++;
				if (i >= 300) {
					searchFor.addItem("(" + (values.size() - 300) + " values not shown...)");
					break;
				}
			}
		} catch (MaltEvalException e1) {
			JOptionPane.showMessageDialog(maltTreeViewerGui_, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startSearch) {
			search();
		} else if (e.getSource() == searchLeft) {
			if (foundSentences != null) {
				SortedMap<Integer, HashSet<Integer>> sm = foundSentences.headMap(maltTreeViewerGui_.getCurrentSentence() + 1);
				if (!sm.isEmpty()) {
					maltTreeViewerGui_.changeSentence(sm.lastKey() - 1, sm.get(sm.lastKey()), true);
				}
			}
		} else if (e.getSource() == searchRight) {
			if (foundSentences != null) {
				SortedMap<Integer, HashSet<Integer>> sm = foundSentences.tailMap(maltTreeViewerGui_.getCurrentSentence() + 2);
				if (!sm.isEmpty()) {
					maltTreeViewerGui_.changeSentence(sm.firstKey() - 1, sm.get(sm.firstKey()), true);
				}
			}
		}
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == searchBy) {
			updateSearchBy();
		} else if (e.getSource() == searchIn) {
			updateSearchBy();
		} else if (e.getSource() == result) {
			if (result.getSelectedItem() != null) {
				int sentenceId = ((Integer) result.getSelectedItem());
				maltTreeViewerGui_.changeSentence(sentenceId - 1, foundSentences.get(sentenceId), true);
			}
		}
	}
}
