package lmis.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RandomSequenceDialog extends JDialog {
	public static final int OK = 0;
	public static final int CANCEL = 1;

	private static final int WINDOW_WIDTH = 250;
	private static final int WINDOW_HEIGHT = 100;
	
	private JButton okButton;
	private JButton cancelButton;
	private JComboBox quantityCombo;

	private int quantity = 0;
	private int returnStatus = CANCEL;

	public RandomSequenceDialog(JFrame owner) {
		super(owner, true);
		this.setTitle("Generate Random Sequence");
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		int startX = (Toolkit.getDefaultToolkit().getScreenSize().width - WINDOW_WIDTH) / 2;
		int startY = (Toolkit.getDefaultToolkit().getScreenSize().height - WINDOW_HEIGHT) / 2;
		this.setBounds(startX, startY, WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setResizable(false);
		initComponents();
	}

	private void initComponents() {

		JPanel pane = new JPanel();
		BoxLayout bl = new BoxLayout(pane, BoxLayout.X_AXIS);
		pane.setLayout(bl);
		pane.add(new JLabel("Size of the sequence:"));
		quantityCombo = new JComboBox(new String[] { "10", "100", "1,000",
				"10,000" });
		pane.add(quantityCombo);
		JPanel panel = new JPanel();
		panel.add(pane);
		this.getContentPane().add("Center", panel);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
		okButton = new JButton("OK");
		okButton.setPreferredSize(new Dimension(80, 25));
		okButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				int[] nums = { 10, 100, 1000, 10000, 100000, 1000000 };
				quantity = nums[quantityCombo.getSelectedIndex()];
				returnStatus = OK;
				dispose();
			}
		});
		cancelButton = new JButton("Cancel");
		okButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				returnStatus = CANCEL;
				dispose();
			}
		});
		cancelButton.setPreferredSize(new Dimension(80, 25));
		buttonPane.setPreferredSize(new Dimension(500, 40));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(okButton);
		buttonPane.add(Box.createHorizontalStrut(10));
		buttonPane.add(cancelButton);
		buttonPane.add(Box.createHorizontalStrut(20));
		this.getContentPane().add("South", buttonPane);
	}

	public int getQuantity() {
		return quantity;
	}

	public int getReturnStatus() {
		return returnStatus;
	}
}
