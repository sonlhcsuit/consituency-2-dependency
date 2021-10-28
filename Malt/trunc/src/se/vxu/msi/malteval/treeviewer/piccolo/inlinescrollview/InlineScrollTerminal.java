package se.vxu.msi.malteval.treeviewer.piccolo.inlinescrollview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import se.vxu.msi.malteval.corpus.MaltWord;
import se.vxu.msi.malteval.treeviewer.MaltTreeViewerGui;
import se.vxu.msi.malteval.treeviewer.piccolo.GraphConstants;

/**
 * a class holding and managing GUI word component (a button and a label for the word class)
 */

public class InlineScrollTerminal extends JPanel {
	private static final long serialVersionUID = 1L;

	/** the corresponding word index in the sentence */
	private int id;
	/** the GUI object (button) for the word */
	private JLabel wordComp;

	private JPanel tags;

	/** the GUI object (label) for the coarse grained part-of-speech */
	private JLabel cPostagComp;
	/** the GUI object (label) for the fine grained part-of-speech */
	private JLabel postagComp;
	/** the GUI object (label) for the feats */
	private JLabel idComp;

	private MaltWord maltWord_;
	private boolean isSelected_;

	/**
	 * Creates and initialize a GUI object for a word.
	 * 
	 * @param i
	 *            the index of this word in the sentence
	 * @param maltWord
	 *            the word to be displayed
	 * @param nba
	 *            the number of arrows incoming or outgoing from/to this word
	 */
	public InlineScrollTerminal(int i, MaltWord maltWord, int nba, boolean searchHit) {
		this.id = i;
		maltWord_ = maltWord;
		maltWord_.setWordComp(this);
		wordComp = new JLabel(maltWord_.getForm() + "  ");
		wordComp.getPreferredSize();
		//wordComp.setForeground(EMPHASIZE_WORDS && searchHit ? Color.BLUE : Color.BLACK );
		wordComp.setFont(new Font(MaltTreeViewerGui.getFontFamily(), (GraphConstants.EMPHASIZE_WORDS && searchHit ? Font.BOLD : Font.PLAIN), GraphConstants.FONT_SIZE));
		wordComp.setHorizontalAlignment(JLabel.LEFT);
		wordComp.setBackground(null); // inherits parent's color

		idComp = new JLabel(String.valueOf(id), SwingConstants.LEFT);
		idComp.setBackground(null); // inherits parent's color
		idComp.setFont(new Font(MaltTreeViewerGui.getFontFamily(), Font.PLAIN, (int) (GraphConstants.FONT_SIZE * 0.7)));
		cPostagComp = new JLabel(maltWord_.getCPostag(), SwingConstants.LEFT);
		cPostagComp.setBackground(null); // inherits parent's color
		cPostagComp.setFont(new Font(MaltTreeViewerGui.getFontFamily(), Font.ITALIC, (int) (GraphConstants.FONT_SIZE * 0.85)));
		postagComp = new JLabel(maltWord_.getPostag(), SwingConstants.LEFT);
		postagComp.setBackground(null); // inherits parent's color
		postagComp.setFont(new Font(MaltTreeViewerGui.getFontFamily(), Font.ITALIC, (int) (GraphConstants.FONT_SIZE * 0.85)));

		tags = new JPanel();
		tags.setBackground(null);
		tags.setLayout(new GridLayout(3, 1));
		tags.add(idComp);
		tags.add(cPostagComp);
		tags.add(postagComp);

		setLayout(new BorderLayout());
		add(wordComp, BorderLayout.NORTH);
		add(tags, BorderLayout.SOUTH);
		setBackground(null);
		setToolTipText("lemma: " + maltWord_.getLemma() + "   feats: " + maltWord_.getFeats());
		updateButtonSize(nba); // if the buttons are too small for the arrows
	}

	/**
	 * updates the size of this component if not enough space is available for all the arrows
	 * 
	 * @param nba
	 *            the number of arrows incoming on this word
	 */
	public void updateButtonSize(int nba) {
		 double width = Math.max(wordComp.getMinimumSize().getWidth(), tags.getMinimumSize().getWidth());
		 width = Math.max(width, nba * InlineScrollConsts.arrowHeadWidth);
		 wordComp.setPreferredSize(new Dimension((int) width, (int) getPreferredSize().getHeight()));
		 wordComp.setMinimumSize(new Dimension((int) width, (int) getMinimumSize().getHeight()));
	}

	/** updates the word call text */
	public void updateWordClass(String t) {
		cPostagComp.setText(t);
	}

	public String getWordForm() {
		return maltWord_.getForm();
	}

	public boolean isSelected() {
		return isSelected_;
	}

	public void setSelected_(boolean isSelected) {
		isSelected_ = isSelected;
	}
}
