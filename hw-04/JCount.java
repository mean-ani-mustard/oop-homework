

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JCount extends JPanel {
	private static final int COUNT_NUM = 4;
	private static final int TEXTFIELD_SIZE = 10;
	private static final int UPDATE_INTERVAL = 10_000;
	private static final int SLEEP_TIME_MS = 100;

	private JTextField inputField;
	private JLabel countLabel;
	private JButton startButton;
	private JButton stopButton;

	private Worker workerThread;

	/*
	 * Constructor for JCount panel.
	 * Sets up the layout, initializes UI components, and adds action listeners.
	 * The Start button starts a new counting thread after validating input.
	 * The Stop button interrupts the running counting thread if any.
	 */

	public JCount() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		inputField = new JTextField("100000000", TEXTFIELD_SIZE);
		countLabel = new JLabel("0");
		startButton = new JButton("Start");
		stopButton = new JButton("Stop");

		add(inputField);
		add(countLabel);
		add(startButton);
		add(stopButton);
		add(Box.createRigidArea(new Dimension(0, 40)));

		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (workerThread != null && workerThread.isAlive()) {
					workerThread.interrupt();
				}
				int maxCount;
				try {
					maxCount = Integer.parseInt(inputField.getText().trim());
					if (maxCount < 0) throw new NumberFormatException();
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(JCount.this,
							"Please enter a valid positive integer.",
							"Invalid Input", JOptionPane.ERROR_MESSAGE);
					return;
				}

				workerThread = new Worker(maxCount);
				workerThread.start();
			}
		});


		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (workerThread != null && workerThread.isAlive()) {
					workerThread.interrupt();
				}
			}
		});
	}
	/*
	 * Inner Worker class representing a counting thread.
	 * Counts from 0 up to maxCount, updating the label every UPDATE_INTERVAL.
     * Uses SwingUtilities.invokeLater to safely update UI from the worker thread.
     * Sleeps briefly after each update to simulate processing delay.
     * Properly handles interruption requests to stop counting early.
	 */

	private class Worker extends Thread {
		private final int maxCount;

		public Worker(int maxCount) {
			this.maxCount = maxCount;
		}

		@Override
		public void run() {
			for (int i = 0; i <= maxCount; i++) {
				if (Thread.currentThread().isInterrupted()) {
					break;
				}

				if (i % UPDATE_INTERVAL == 0) {
					final int currentCount = i;

					// Update UI safely
					SwingUtilities.invokeLater(() -> countLabel.setText(String.valueOf(currentCount)));

					try {
						Thread.sleep(SLEEP_TIME_MS);
					} catch (InterruptedException e) {
						break;  // exit if interrupted during sleep
					}
				}
			}
		}
	}

	/*
	 * Static helper method to create the main application window.
	 * Sets up a JFrame with COUNT_NUM instances of JCount panel stacked vertically.
	 * Configures frame properties such as default close operation, layout, packing,
	 * disables resizing, centers the window, and finally makes it visible.
	 */
	private static void createAndShowGUI() {
		JFrame frame = new JFrame("Counting Threads");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

		for (int i = 0; i < COUNT_NUM; i++) {
			frame.add(new JCount());
		}

		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);  // center the window
		frame.setVisible(true);
	}

	/*
	 * Main method - entry point for the application.
	 * Schedules the creation and display of the GUI on the Event Dispatch Thread
	 * using SwingUtilities.invokeLater to ensure thread safety.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(JCount::createAndShowGUI);
	}
}
