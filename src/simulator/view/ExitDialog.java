package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;




public class ExitDialog extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExitDialog() {
		super();
		this.setName("Quit");

		initDialog();
	}
	
	public void initDialog() {
		JPanel mainDialog = new JPanel();
		JLabel labelE = new JLabel("Do you want to exit the app?");
		JButton yes = new JButton("Yes");
		yes.setVisible(true);
		JButton no = new JButton("No");
		no.setVisible(true);
		mainDialog.add(labelE);
		mainDialog.add(yes);
		mainDialog.add(no);
		mainDialog.setVisible(true);
		yes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		no.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExitDialog.this.dispose();
			}

		});
		
		this.add(mainDialog);
		this.setVisible(true);
	}

}
