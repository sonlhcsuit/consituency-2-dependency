package se.vxu.msi.malteval.treeviewer.gui;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AboutDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JLabel aboutTextDescription1;
	private JLabel aboutTextDescription2;
	private JLabel aboutTextDescription3;
	private JLabel aboutTextDescription4;
	private JButton closeButton;

	public AboutDialog(JFrame f, String title) {
		super(f, "MaltEval, version " + title, true);
		JPanel p = new JPanel();
		getContentPane().add(p);
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

		aboutTextDescription1 = new JLabel("This application is developed at");
		aboutTextDescription1.setAlignmentX(CENTER_ALIGNMENT);
		aboutTextDescription2 = new JLabel("Vaxjo University, Sweden");
		aboutTextDescription2.setForeground(Color.BLUE);
		aboutTextDescription2.setAlignmentX(CENTER_ALIGNMENT);
		aboutTextDescription3 = new JLabel("within the Malt project by");
		aboutTextDescription3.setAlignmentX(CENTER_ALIGNMENT);
		aboutTextDescription4 = new JLabel("Jens Nilsson (jni@msi.vxu.se)");
		aboutTextDescription4.setForeground(Color.BLUE);
		aboutTextDescription4.setAlignmentX(CENTER_ALIGNMENT);
		closeButton = new JButton("Close");
		closeButton.setAlignmentX(CENTER_ALIGNMENT);

		p.add(Box.createVerticalGlue());
		p.add(aboutTextDescription1);
		p.add(Box.createVerticalStrut(5));
		p.add(aboutTextDescription2);
		p.add(Box.createVerticalStrut(5));
		p.add(aboutTextDescription3);
		p.add(Box.createVerticalStrut(5));
		p.add(aboutTextDescription4);
		p.add(Box.createVerticalStrut(10));
		p.add(closeButton);
		p.add(Box.createVerticalGlue());

		closeButton.addActionListener(this);

		setLocation(f.getLocation().x + f.getWidth() / 2 - getWidth() / 2, f.getLocation().y + f.getHeight() / 2 - getHeight() / 2);
		setSize(300, 180);
		setResizable(false);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == closeButton) {
			setVisible(false);
		}
	}
}
