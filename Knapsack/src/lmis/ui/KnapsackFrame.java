package lmis.ui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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

import lmis.algorithm.KnapsackFinder;

public class KnapsackFrame extends JFrame {
	private static final int WINDOW_WIDTH = 500;
	private static final int WINDOW_HEIGHT = 700;

	private JTextArea caseArea;
	private JTextArea logArea;
	private JButton inputCaseButton;
	private JButton randomCaseButton;
	private JButton startButton;
	private JComboBox algorithmComboBox;

	private int[][] items;
	private int capacity;
	private List<Integer> result;
	private long time;
	private boolean manual = true;
	private boolean resultCalculated = false;

	public KnapsackFrame() {
		super("Snapsack Problem");
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
		JLabel pointLabel = new JLabel("construct a test case:");
		pointPane.add("North", pointLabel);
		caseArea = new JTextArea();
		caseArea.setEditable(true);
		caseArea.setLineWrap(true);
		caseArea.setBorder(BorderFactory.createEtchedBorder());
		pointPane.add("Center", new JScrollPane(caseArea));
		JPanel buttonPanel1 = new JPanel();
		inputCaseButton = new JButton("Input Case");
		inputCaseButton.addActionListener(new InputSequenceAction());
		buttonPanel1.add(inputCaseButton);
		randomCaseButton = new JButton("Random Case");
		randomCaseButton.addActionListener(new RandomCaseAction());
		buttonPanel1.add(randomCaseButton);

		pointPane.add("South", buttonPanel1);
		panel.add(pointPane);

		panel.add(Box.createVerticalStrut(10));

		JPanel startPane = new JPanel();
		startPane.setLayout(new BoxLayout(startPane, BoxLayout.X_AXIS));
		algorithmComboBox = new JComboBox(
				new String[] { "O(nw) dynamic programming" });
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
		logArea.setBorder(BorderFactory.createEtchedBorder());
		resultPane.add("Center", new JScrollPane(logArea));
		panel.add(resultPane);
	}

	public int[][] getItems() {
		return items;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setItems(int[][] items) {
		this.items = items;
	}

	private void refresh() {
		caseArea.setText("");
		logArea.setText("");
		int[][] seqs = this.getItems();
		if (seqs == null || seqs.length == 0) {
			this.caseArea
					.setText("No case defined, please construct one, the following case is just a sample:");
			this.caseArea.append("\n<bag capacity>");
			this.caseArea.append("\n<weight of item 1> <value of item 1>");
			this.caseArea.append("\n<weight of item 2> <value of item 2>");
			this.caseArea.append("\n<weight of item 3> <value of item 3>");
			this.caseArea.append("\n<weight of item 4> <value of item 4>");
			this.caseArea.append("\n...");
		} else {
			caseArea.setText("");
			caseArea.append(this.getCapacity() + "\n");
			for (int[] item : seqs) {
				caseArea.append(item[0] + "\t" + item[1] + "\n");
			}
		}

		if (resultCalculated) {
			logArea.append("You can take " + getResult().size() + " items.\n");
			int w = 0, v = 0;
			for (int i : getResult()) {
				w += getItems()[i][0];
				v += getItems()[i][1];
			}
			logArea.append("Total Weight: " + w + "\n");
			logArea.append("Total Value: " + v + "\n");
			logArea.append("Consumed Time: " + (time) / 1000000 + "ms\n\n");
			logArea.append("Items Should Be Taken:\n\n");
			for (int i : getResult()) {
				logArea.append("[ITEM " + i + "]");
				logArea.append("  WEIGHT = " + getItems()[i][0]);
				logArea.append("  VALUE = " + getItems()[i][1]+"\n");
			}
		} else {
			this.logArea.setText("");
		}
	}

	public List<Integer> getResult() {
		return result;
	}

	public void setResult(List<Integer> result) {
		this.result = result;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	private boolean validateCase() {
		if (!manual) {
			return true;
		} else {
			String[] lines = this.caseArea.getText().split("\n");
			LinkedList<Integer[]> list = new LinkedList<Integer[]>();
			int cap = -1;
			for (String line : lines) {
				line = line.trim();
				if (line.length() == 0)
					continue;
				try {
					if (cap < 0) {
						cap = Integer.parseInt(line);
						if (cap < 0) {
							throw new NumberFormatException();
						}
						this.setCapacity(cap);
					} else {
						String[] values = line.split("[\\s|\\t]");
						if (values.length != 2) {
							throw new NumberFormatException();
						}
						Integer[] ints = new Integer[2];
						ints[0] = Integer.parseInt(values[0]);
						ints[1] = Integer.parseInt(values[1]);
						list.add(ints);
					}
				} catch (NumberFormatException e) {
					return false;
				}
			}
			int[][] items = new int[list.size()][2];
			int i = 0;
			Iterator<Integer[]> itr = list.iterator();
			while (itr.hasNext()) {
				Integer[] ints = itr.next();
				items[i][0] = ints[0].intValue();
				items[i++][1] = ints[1].intValue();
			}
			setItems(items);
			return true;
		}

	}

	class StartAction extends AbstractAction {
		public void actionPerformed(ActionEvent arg0) {
			if (!validateCase()) {
				logArea
						.setText("Format Error: please input the sequence again.");
				return;
			}
			Timer timer = new Timer();
			KnapsackFinder finder = new KnapsackFinder();
			long start = System.nanoTime();
			setResult(finder.whatToTake(getItems(), getCapacity()));
			long end = System.nanoTime();
			setTime(end - start);
			resultCalculated = true;
			refresh();
		}
	}

	class InputSequenceAction extends AbstractAction {
		public void actionPerformed(ActionEvent arg0) {
			caseArea.setText("");
			caseArea.setEditable(true);
			setItems(null);
			resultCalculated = false;
		}
	}

	class RandomCaseAction extends AbstractAction {
		public void actionPerformed(ActionEvent arg0) {
			caseArea.setText("");
			caseArea.setEditable(false);
			Random rand = new Random();
			int[][] items = new int[rand.nextInt(100)][2];
			int cap = 500 + rand.nextInt(500);
			for (int i = 0; i < items.length; i++) {
				items[i][0] = (1 + rand.nextInt(19)) * 5;
				items[i][1] = (1 + rand.nextInt(29)) * 10;
			}
			setCapacity(cap);
			setItems(items);
			refresh();
			resultCalculated = false;
		}
	}
}
