package lmis.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import lmis.algorithm.LMISFinder;

public class LMISFrame extends JFrame {
	private static final int WINDOW_WIDTH = 500;
	private static final int WINDOW_HEIGHT = 700;

	private JTextArea sequenceArea;
	private JTextArea logArea;
	private JButton inputSequenceButton;
	private JButton randomSequenceButton;
	private JButton startButton;
	private JComboBox algorithmComboBox;

	private int[] sequence;
	private int[] result;
	private long time;
	private boolean manual = true;
	private boolean resultCalculated = false;

	public LMISFrame() {
		super("Longest Monotonically Increasing Sub-sequence");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		int startX = (Toolkit.getDefaultToolkit().getScreenSize().width - WINDOW_WIDTH) / 2;
		int startY = (Toolkit.getDefaultToolkit().getScreenSize().height - WINDOW_HEIGHT) / 2;
		this.setBounds(startX, startY, WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initComponents();
		refresh();
	}

	private void initComponents() {
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setHgap(5);
		borderLayout.setVgap(5);
		this.setLayout(borderLayout);
		this.add("East", new JPanel());
		this.add("West", new JPanel());
		this.add("North", new JPanel());
		this.add("South", new JPanel());
		JPanel panel = new JPanel();
		this.add("Center", panel);
		BoxLayout bl = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(bl);

		JPanel pointPane = new JPanel(new BorderLayout());
		JLabel pointLabel = new JLabel(
				"Input a sequence (one element each line):");
		pointPane.add("North", pointLabel);
		sequenceArea = new JTextArea();
		sequenceArea.setEditable(true);
		sequenceArea.setLineWrap(true);
		sequenceArea.setFont(new Font(null,Font.PLAIN,12));
		pointPane.add("Center", new JScrollPane(sequenceArea));
		JPanel buttonPanel1 = new JPanel();
		inputSequenceButton = new JButton("Input Sequence");
		inputSequenceButton.addActionListener(new InputSequenceAction());
		buttonPanel1.add(inputSequenceButton);
		randomSequenceButton = new JButton("Random Sequence");
		randomSequenceButton.addActionListener(new RandomSequenceAction());
		buttonPanel1.add(randomSequenceButton);

		pointPane.add("South", buttonPanel1);
		panel.add(pointPane);

		panel.add(Box.createVerticalStrut(10));

		JPanel startPane = new JPanel();
		startPane.setLayout(new BoxLayout(startPane, BoxLayout.X_AXIS));
		algorithmComboBox = new JComboBox(
				new String[] { "nlogn dynamic programming" });
		startButton = new JButton("       Start       ");
		startButton.addActionListener(new StartAction());
		startPane.add(new JLabel("Select Algorithm:"));
		startPane.add(algorithmComboBox);
		startPane.add(Box.createHorizontalGlue());
		startPane.add(startButton);
		panel.add(startPane);

		panel.add(Box.createVerticalStrut(10));

		JPanel resultPane = new JPanel(new BorderLayout());
		JLabel resultLabel = new JLabel("Result:");
		resultPane.add("North", resultLabel);
		logArea = new JTextArea();
		logArea.setEditable(false);
		logArea.setLineWrap(true);
		logArea.setFont(new Font(null,Font.PLAIN,12));
		resultPane.add("Center", new JScrollPane(logArea));
		panel.add(resultPane);
	}

	public int[] getSequence() {
		return sequence;
	}

	public void setSequence(int[] sequence) {
		this.sequence = sequence;
	}

	private void refresh() {
		int[] seqs = this.getSequence();
		if (seqs == null || seqs.length == 0) {
			this.sequenceArea
					.setText("No sequence defined, please input it manually or generate a random sequence.");
		} else {
			sequenceArea.setText("");
			for (int i : seqs) {
				sequenceArea.append(i + "\n");
			}
		}

		if (resultCalculated) {
			logArea.setText("");
			logArea.append("Subsequence Length: \t" + result.length + "\n\n");
			logArea.append("Consumed Time: \t" + (time) / 1000000 + "ms\n\n");
			logArea.append("Longest Monotonically Increasing Sequence:\n\n");
			logArea.append(Arrays.toString(result));
		} else {
			this.logArea.setText("");
		}
	}

	public int[] getResult() {
		return result;
	}

	public void setResult(int[] result) {
		this.result = result;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	private boolean validateSequence() {
		if (!manual) {
			return true;
		} else {
			String[] lines = this.sequenceArea.getText().split("\n");
			LinkedList<Integer> list = new LinkedList<Integer>();
			for (String line : lines) {
				if (line.trim().length() == 0)
					continue;
				try {
					int value = Integer.parseInt(line.trim());
					list.add(value);
				} catch (NumberFormatException e) {
					return false;
				}
			}
			int[] sequence = new int[list.size()];
			int i = 0;
			Iterator<Integer> itr = list.iterator();
			while (itr.hasNext()) {
				sequence[i++] = itr.next().intValue();
			}
			setSequence(sequence);
			return true;
		}

	}

	class StartAction extends AbstractAction {
		public void actionPerformed(ActionEvent arg0) {
			logArea.setText("");
			if (!validateSequence()) {
				logArea
						.setText("Format Error: please input the sequence again.");
				return;
			}
			Timer timer = new Timer();
			LMISFinder finder = new LMISFinder();
			long start = System.nanoTime();
			setResult(finder.find(getSequence()));
			long end = System.nanoTime();
			setTime(end - start);
			resultCalculated = true;
			refresh();
		}
	}

	class InputSequenceAction extends AbstractAction {
		public void actionPerformed(ActionEvent arg0) {
			sequenceArea.setText("");
			sequenceArea.setEditable(true);
			logArea.setText("");
			setSequence(null);
			resultCalculated = false;
		}
	}

	class RandomSequenceAction extends AbstractAction {
		public void actionPerformed(ActionEvent arg0) {
			sequenceArea.setText("");
			sequenceArea.setEditable(false);
			logArea.setText("");
			RandomSequenceDialog dialog = new RandomSequenceDialog(
					LMISFrame.this);
			dialog.setVisible(true);
			if (dialog.getReturnStatus() == RandomSequenceDialog.OK) {
				int quantity = dialog.getQuantity();
				int seqs[] = new int[quantity];
				Random rand = new Random();
				for (int i = 0; i < quantity; i++) {
					seqs[i] = rand.nextInt(quantity);
				}
				setSequence(seqs);
				refresh();
			}
			resultCalculated = false;
		}
	}
}
