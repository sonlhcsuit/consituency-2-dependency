package se.vxu.msi.malteval.treeviewer.gui;

import se.vxu.msi.malteval.treeviewer.MaltTreeViewerGui;
import se.vxu.msi.malteval.treeviewer.core.CurrentSentenceList;
import se.vxu.msi.malteval.treeviewer.core.TreeViewerSentence;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

import javax.swing.event.*;

/**
 * this class handles the GUI component for the current sentence list. (the table at the bottom of the screen)
 */
public class CurrentSentenceListComponent extends JScrollPane {
	private static final long serialVersionUID = 1L;

	MaltTreeViewerGui maltTreeViewerGui_;

	/** the jtable component */
	private JTable table;

	/** the sentence model used for the table */
	private SentenceModel model;

	/** create an empty component communicating with the given mediator */
	public CurrentSentenceListComponent(MaltTreeViewerGui maltTreeViewerGui) {
		maltTreeViewerGui_ = maltTreeViewerGui;
		model = new SentenceModel(maltTreeViewerGui.getSentenceList());
		table = new JTable(model);
		table.setFont(new Font(MaltTreeViewerGui.getFontFamily(), Font.PLAIN, 12));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				maltTreeViewerGui_.changeSentence(table.getSelectedRow(), null, false);
			}
		});

		updateColWidth();
		setViewportView(table);
	}

	public void setSelectedSentence(int rowindex) {
		table.changeSelection(rowindex, table.getSelectedColumn(), true, false);
	}

	/** update the column width of the table according to the columns */
	private void updateColWidth() {
		TableColumn col;
		col = table.getColumnModel().getColumn(1);
		col.setPreferredWidth(1000);
	}

	/** return the minimun size this object can have (set to 300 pixels min vertically) */
	public Dimension getMinimumSize() {
		return new Dimension(300, table.getRowHeight() * 5);
	}

	/** private inner class to handle the table model for the current sentence list */
	private class SentenceModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;

		/** the reference to the main mediator (used to get the current sentence list) */
		private CurrentSentenceList currentSentenceList = null;

		/** the column names */
		private String[] columnNames = {"id", "Sentence"};

		// private String[] columnNames = { "id", "parser", "Sentence", "Tree?", "State", "User", "Time" };

		/** creates the sentence model associated with the given mediator */
		public SentenceModel(CurrentSentenceList csl) {
			currentSentenceList = csl;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public int getRowCount() {
			return currentSentenceList.size();
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		/** cells for id and parser can be edited */
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			fireTableRowsUpdated(rowIndex, rowIndex);
		}

		public Object getValueAt(int row, int column) {
			TreeViewerSentence s = currentSentenceList.getSentence(row);
			Object result = null;
			switch (column) {
			case 0:
				result = new Integer(s.getId());
				break;
			case 1:
				result = s.getSentence();
				break;
			}
			return result;
		}

		public Class<?> getColumnClass(int columnIndex) {
			switch (columnIndex) {
			case 0:
				return Integer.class;
			case 1:
				return String.class;
			case 2:
				return String.class;
			case 3:
				return Boolean.class;
			case 4:
				return String.class;
			case 5:
				return String.class;
			case 6:
				return String.class;
			default:
				return String.class;
			}
		}
	}

}
