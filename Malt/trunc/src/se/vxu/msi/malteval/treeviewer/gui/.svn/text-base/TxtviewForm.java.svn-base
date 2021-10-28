
/*
    Window to display and save the results
*/

package se.vxu.msi.malteval.treeviewer.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;


/**
    This class displays a modal dialog box to browse a large text. (used to display import errors and some other things) 
*/
public class TxtviewForm extends JDialog {
	private static final long serialVersionUID = 1L;

	private JTextArea jTextArea1;
    
    /** display the dialog box containing the text message */
    public TxtviewForm(String message) {
        initGUIComponents(null, message);
    }
    
    /** display the dialog box containing the text message, and center the dialog box relatively to the component parent */ 
    public TxtviewForm(Component parent, String message) {
        initGUIComponents(parent, message);
    }

    /** constructs the components */
    private void initGUIComponents(Component parent, String message) {
        JPanel jPanel3=new JPanel();
        JPanel jPanel2=new JPanel();
        JPanel jPanel1=new JPanel();
        
        JScrollPane jScrollPane1;

        JLabel jLabel1;
        JButton jButton1;
        
        setModal(true);
        
        jScrollPane1 = new JScrollPane();
        
        jTextArea1 = new JTextArea(message);
        jTextArea1.setEditable(false);
        
        addWindowListener(new WindowAdapter() {
                              public void windowClosing(WindowEvent evt) {
                                  closeDialog(evt);
                              }
                          }
                         );

        jPanel2.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        
        jLabel1=new JLabel("Results");
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jPanel2.add(jLabel1);

        getContentPane().add(jPanel2, BorderLayout.NORTH);

        jPanel3.setLayout(new GridLayout(1, 0));

        jPanel3.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        jScrollPane1.setBorder(null);
        jScrollPane1.setPreferredSize(new Dimension(400, 200));
        jScrollPane1.setViewportView(jTextArea1);

        jPanel3.add(jScrollPane1);

        getContentPane().add(jPanel3, BorderLayout.CENTER);

        jPanel1.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        
        jButton1=new JButton("Close");
        jButton1.addActionListener(new ActionListener() {
                                       public void actionPerformed(ActionEvent evt) {
                                           closeDialog(null);
                                       }
                                   }
                                  );
        jPanel1.add(jButton1);

        getContentPane().add(jPanel1, BorderLayout.SOUTH);

        pack();
        if(parent!=null)
            setLocationRelativeTo(parent);
            
        setVisible(true);
    }

    /** called when the close button is pressed */
    private void closeDialog(WindowEvent evt) {
        setVisible(false);
        dispose();
    }
}
