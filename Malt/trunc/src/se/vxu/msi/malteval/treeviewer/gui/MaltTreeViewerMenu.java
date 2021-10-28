/*
 * Created on 2 nov 2007
 */
package se.vxu.msi.malteval.treeviewer.gui;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

import se.vxu.msi.malteval.treeviewer.MaltTreeViewerGui;
import se.vxu.msi.malteval.treeviewer.piccolo.GraphConstants;

public class MaltTreeViewerMenu extends JMenuBar implements ActionListener {
	private static final long serialVersionUID = -7866616542523187189L;

	MaltTreeViewerGui maltTreeViewerGui_;

	private JMenuItem exitMenuItem;

	private Vector<JMenuItem> exportImageMenuItems;
	private ButtonGroup imageFormatButtonGroup;


	private JRadioButtonMenuItem inlineScroll;
	private JRadioButtonMenuItem inlineZoom;
	private JRadioButtonMenuItem hierarchicalZoom;
	private JRadioButtonMenuItem netGraphLikeZoom;
	
	private JCheckBoxMenuItem emphasizeWords;
	private JCheckBoxMenuItem emphasizeArcs;
	private JMenuItem increaseFontSize;
	private JMenuItem decreaseFontSize;
	private JMenuItem defaultFontSize;

	private JMenuItem previous;
	private JMenuItem next;
	private JMenuItem previousError;
	private JMenuItem nextError;

	private JMenuItem about;

	public MaltTreeViewerMenu(MaltTreeViewerGui maltTreeViewerGui) {
		int i, j;
		maltTreeViewerGui_ = maltTreeViewerGui;
		JMenu menu;
		JMenuItem dataSet;
		JRadioButtonMenuItem formats;

		menu = new JMenu("File");
		add(menu);
		menu.setMnemonic(KeyEvent.VK_F);

		JMenu export = new JMenu("Export");
		menu.add(export);
		export.setMnemonic(KeyEvent.VK_E);

		JMenu exportDataSet = new JMenu("Current sentence");
		export.add(exportDataSet);
		exportDataSet.setMnemonic(KeyEvent.VK_C);

		exportImageMenuItems = new Vector<JMenuItem>();
		i = 0;
		if (maltTreeViewerGui_.getGsTreebank() != null) {
			dataSet = new JMenuItem("Gold-standard");
			dataSet.addActionListener(this);
			exportDataSet.add(dataSet);
			exportImageMenuItems.add(dataSet);
			i++;
		}
		for (j = 0; j < maltTreeViewerGui_.getParsedFileNames().size(); j++) {
			exportImageMenuItems.add(new JMenuItem("Parsed " + (j + 1)));
			exportDataSet.add(exportImageMenuItems.lastElement());
			exportImageMenuItems.lastElement().addActionListener(this);
			i++;
		}

		export.addSeparator();
		JMenu exportFormat = new JMenu("Format");
		export.add(exportFormat);
		exportFormat.setMnemonic(KeyEvent.VK_F);
		imageFormatButtonGroup = new ButtonGroup();
		HashSet<String> usedFormats = new HashSet<String>();
		for (String fileSuffix : ImageIO.getWriterFormatNames()) {
			if (!usedFormats.contains(fileSuffix.toLowerCase())) {
				formats = new JRadioButtonMenuItem(fileSuffix, fileSuffix.toLowerCase().equals("jpg"));
				imageFormatButtonGroup.add(formats);
				exportFormat.add(formats);
				usedFormats.add(fileSuffix.toLowerCase());
			}
		}

		menu.addSeparator();
		exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
		menu.add(exitMenuItem);
		exitMenuItem.addActionListener(this);

		menu = new JMenu("Settings");
		add(menu);
		menu.setMnemonic(KeyEvent.VK_S);

		JMenu viewMode = new JMenu("View Mode");
		menu.add(viewMode);
		viewMode.setMnemonic(KeyEvent.VK_V);
		ButtonGroup group = new ButtonGroup();
		inlineZoom = new JRadioButtonMenuItem("Inline Zoom", true);
		hierarchicalZoom = new JRadioButtonMenuItem("Hierarchical Zoom");
		netGraphLikeZoom = new JRadioButtonMenuItem("NetGraph Zoom");
		inlineScroll = new JRadioButtonMenuItem("Inline Scroll");
		inlineScroll.addActionListener(this);
		inlineZoom.addActionListener(this);
		hierarchicalZoom.addActionListener(this);
		hierarchicalZoom.addActionListener(this);
		netGraphLikeZoom.addActionListener(this);
		viewMode.add(inlineZoom);
		viewMode.add(hierarchicalZoom);
		viewMode.add(netGraphLikeZoom);
		viewMode.add(inlineScroll);
		group.add(inlineScroll);
		group.add(inlineZoom);
		group.add(hierarchicalZoom);
		group.add(netGraphLikeZoom);
		

		JMenu font = new JMenu("Font size");
		menu.add(font);
		font.setMnemonic(KeyEvent.VK_F);

		increaseFontSize = new JMenuItem("Increase");
		font.add(increaseFontSize);
		increaseFontSize.setMnemonic(KeyEvent.VK_I);
		increaseFontSize.addActionListener(this);

		decreaseFontSize = new JMenuItem("Decrease");
		font.add(decreaseFontSize);
		decreaseFontSize.setMnemonic(KeyEvent.VK_D);
		decreaseFontSize.addActionListener(this);

		font.addSeparator();

		defaultFontSize = new JMenuItem("Reset");
		font.add(defaultFontSize);
		defaultFontSize.setMnemonic(KeyEvent.VK_R);
		defaultFontSize.addActionListener(this);

		JMenu emphasize = new JMenu("Emphasize search hits");
		menu.add(emphasize);
		emphasize.setMnemonic(KeyEvent.VK_E);

		emphasizeWords = new JCheckBoxMenuItem("For words", GraphConstants.EMPHASIZE_WORDS);
		emphasize.add(emphasizeWords);
		emphasizeWords.setMnemonic(KeyEvent.VK_W);
		emphasizeWords.addActionListener(this);
		emphasizeArcs = new JCheckBoxMenuItem("For arcs", GraphConstants.EMPHASIZE_ARCS);
		emphasize.add(emphasizeArcs);
		emphasizeArcs.setMnemonic(KeyEvent.VK_A);
		emphasizeArcs.addActionListener(this);
		
		menu = new JMenu("Navigate");
		add(menu);
		menu.setMnemonic(KeyEvent.VK_N);
		previous = new JMenuItem("Previous Sencence");
		next = new JMenuItem("Next Sencence");
		previousError = new JMenuItem("Previous Erroneous Sencence");
		nextError = new JMenuItem("Next Erroneous Sencence");
		menu.add(previous);
		previous.setMnemonic(KeyEvent.VK_P);
		menu.add(next);
		next.setMnemonic(KeyEvent.VK_N);
		menu.addSeparator();
		menu.add(previousError);
		previousError.setMnemonic(KeyEvent.VK_R);
		menu.add(nextError);
		nextError.setMnemonic(KeyEvent.VK_E);
		previous.addActionListener(this);
		next.addActionListener(this);
		previousError.addActionListener(this);
		nextError.addActionListener(this);

		menu = new JMenu("Help");
		add(menu);
		menu.setMnemonic(KeyEvent.VK_H);
		about = new JMenuItem("About MaltEval");
		menu.add(about);
		about.setMnemonic(KeyEvent.VK_A);
		about.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		JMenuItem menuItem;
		if (e.getSource() == exitMenuItem) {
			maltTreeViewerGui_.setVisible(false);
			System.exit(0);
		} else if (e.getSource() == inlineZoom) {
			maltTreeViewerGui_.changeViewMode(1);
		} else if (e.getSource() == hierarchicalZoom) {
			maltTreeViewerGui_.changeViewMode(2);
		} else if (e.getSource() == netGraphLikeZoom) {
			maltTreeViewerGui_.changeViewMode(3);
		} else if (e.getSource() == inlineScroll) {
			maltTreeViewerGui_.changeViewMode(4);
		} else if (e.getSource() == increaseFontSize) {
			GraphConstants.FONT_SIZE += 2;
			maltTreeViewerGui_.resetSentence();
		} else if (e.getSource() == decreaseFontSize) {
			if (GraphConstants.FONT_SIZE > 6) {
				GraphConstants.FONT_SIZE -= 2;
				maltTreeViewerGui_.resetSentence();
			}
		} else if (e.getSource() == defaultFontSize) {
			GraphConstants.FONT_SIZE = 14;
			maltTreeViewerGui_.resetSentence();
		} else if (e.getSource() == emphasizeArcs) {
			GraphConstants.EMPHASIZE_ARCS = emphasizeArcs.isSelected();
			maltTreeViewerGui_.resetSentence();
		} else if (e.getSource() == emphasizeWords) {
			GraphConstants.EMPHASIZE_WORDS = emphasizeWords.isSelected();
			maltTreeViewerGui_.resetSentence();
		} else if (e.getSource() == previous) {
			maltTreeViewerGui_.changeSentence(maltTreeViewerGui_.getCurrentSentence() - 1, null, true);
		} else if (e.getSource() == next) {
			maltTreeViewerGui_.changeSentence(maltTreeViewerGui_.getCurrentSentence() + 1, null, true);
		} else if (e.getSource() == previousError) {
			int index = maltTreeViewerGui_.getErroneousSentences().headSet(maltTreeViewerGui_.getCurrentSentence()).size() - 2;
			if (0 <= index && index < maltTreeViewerGui_.getErroneousSentences().size()) {
				maltTreeViewerGui_.changeSentence(index, null, true);
			}
		} else if (e.getSource() == nextError) {
			int index = maltTreeViewerGui_.getErroneousSentences().tailSet(maltTreeViewerGui_.getCurrentSentence()).size();
			if (0 <= index && index < maltTreeViewerGui_.getErroneousSentences().size()) {
				maltTreeViewerGui_.changeSentence(index, null, true);
			}
		} else if (e.getSource() == about) {
			new AboutDialog(maltTreeViewerGui_, "1.0");
		} else if ((menuItem = getExportDatasetMenuItem(exportImageMenuItems, e)) != null) {
			exportImage(menuItem.getText());
		}
	}

	private void exportImage(String buttonText) {
		try {
			BufferedImage image = new BufferedImage(maltTreeViewerGui_.getImagesJPanel(buttonText).getWidth(), maltTreeViewerGui_.getImagesJPanel(buttonText).getHeight(),
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			maltTreeViewerGui_.getImagesJPanel(buttonText).paint(g);
			g.dispose();
			File toSave = new File(buttonText + "_s" + (maltTreeViewerGui_.getCurrentSentence() + 1) + "." + getSelectedImageFormat());
			ImageIO.write(image, getSelectedImageFormat(), toSave);
			image.flush();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Failed to write image file: " + e.getMessage() + "\n" + e.getStackTrace().toString(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private JMenuItem getExportDatasetMenuItem(Vector<JMenuItem> menuItems, ActionEvent e) {
		for (JMenuItem menuItem : menuItems) {
			if (e.getSource() == menuItem) {
				return menuItem;
			}
		}
		return null;
	}

	private String getSelectedImageFormat() {
		AbstractButton toReturn;
		Enumeration<AbstractButton> imageFormatButtons = imageFormatButtonGroup.getElements();
		while (imageFormatButtons.hasMoreElements()) {
			toReturn = imageFormatButtons.nextElement();
			if (toReturn.isSelected()) {
				return toReturn.getText();
			}
		}
		return null;
	}

	public int compareTo(Object o) {
		return 0;
	}
}
