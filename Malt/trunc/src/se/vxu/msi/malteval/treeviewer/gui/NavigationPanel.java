/*
 * Created on 11 nov 2007
 */
package se.vxu.msi.malteval.treeviewer.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.SortedSet;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import se.vxu.msi.malteval.treeviewer.MaltTreeViewerGui;

public class NavigationPanel extends JSplitPane implements ComponentListener, ActionListener {
	private static final long serialVersionUID = -4559613620446678630L;

	private MaltTreeViewerGui maltTreeViewerGui_;
	
	private JPanel navigationPanel;
	private JButton prev;
	private JButton next;
	private JButton prevError;
	private JButton nextError;
	
	private int maxButtonWidth;
	private int maxButtonHeight;

	public NavigationPanel(MaltTreeViewerGui maltTreeViewerGui) {
		super(JSplitPane.HORIZONTAL_SPLIT);
		maltTreeViewerGui_ = maltTreeViewerGui;
		navigationPanel = new JPanel();
		navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.Y_AXIS));
		navigationPanel.addComponentListener(this);
		navigationPanel.add(Box.createVerticalStrut(5));
		
		navigationPanel.add(prev = new JButton("Prev sent."));
		prev.addActionListener(this);
		prev.setAlignmentX(CENTER_ALIGNMENT);
		
		navigationPanel.add(next = new JButton("Next sent."));
		next.addActionListener(this);
		next.setAlignmentX(CENTER_ALIGNMENT);
		
		navigationPanel.add(Box.createVerticalStrut(5));
		navigationPanel.add(prevError = new JButton("Prev error"));
		prevError.addActionListener(this);
		prevError.setAlignmentX(CENTER_ALIGNMENT);
		
		navigationPanel.add(nextError = new JButton("Next error"));
		nextError.addActionListener(this);
		nextError.setAlignmentX(CENTER_ALIGNMENT);
		navigationPanel.add(Box.createVerticalGlue());
		
		setRightComponent(navigationPanel);
		setDividerSize(2);
		setEnabled(false);
		navigationPanel.validate();
		setMaxButtonWidth();
	}
	
	private void setMaxButtonWidth() {
		maxButtonWidth = Math.max((int) prev.getPreferredSize().getWidth(), (int) next.getPreferredSize().getWidth());
		maxButtonWidth = Math.max(maxButtonWidth, (int) prevError.getPreferredSize().getWidth());
		maxButtonWidth = Math.max(maxButtonWidth, (int) nextError.getPreferredSize().getWidth());
		maxButtonWidth += 10;

		maxButtonHeight = Math.max((int) prev.getPreferredSize().getHeight(), (int) next.getPreferredSize().getHeight());
		maxButtonHeight = Math.max(maxButtonWidth, (int) prevError.getPreferredSize().getHeight());
		maxButtonHeight = Math.max(maxButtonWidth, (int) nextError.getPreferredSize().getHeight());
		maxButtonHeight += 20;

	}
	
	public int getHeight() {
		return navigationPanel.getHeight();
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		setDividerLocation(getWidth() - maxButtonWidth);
	}

	public void componentShown(ComponentEvent e) {
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == prev) {
			maltTreeViewerGui_.changeSentence(maltTreeViewerGui_.getCurrentSentence() - 1, null, true);
		} else if (e.getSource() == next) {
			maltTreeViewerGui_.changeSentence(maltTreeViewerGui_.getCurrentSentence() + 1, null, true);
		} else if (e.getSource() == prevError) {
			if (maltTreeViewerGui_.getErroneousSentences() != null) {
				SortedSet<Integer> sm = maltTreeViewerGui_.getErroneousSentences().headSet(maltTreeViewerGui_.getCurrentSentence() + 1);
				if (!sm.isEmpty()) {
					maltTreeViewerGui_.changeSentence(sm.last() - 1, null, true);
				}
			}
		} else if (e.getSource() == nextError) {
			if (maltTreeViewerGui_.getErroneousSentences() != null) {
				SortedSet<Integer> sm = maltTreeViewerGui_.getErroneousSentences().tailSet(maltTreeViewerGui_.getCurrentSentence() + 2);
				if (!sm.isEmpty()) {
					maltTreeViewerGui_.changeSentence(sm.first() - 1, null, true);
				}
			}
		}
	}

	public int getMaxButtonWidth() {
		return maxButtonWidth;
	}

	public int getMaxButtonHeight() {
		return maxButtonHeight;
	}
}
