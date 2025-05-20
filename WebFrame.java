
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class WebFrame extends JFrame {
    private static final String FILE_PATH = "links.txt";
    private DefaultTableModel tableModel;
    private JTable urlTable;
    private JButton fetchSingleBtn, fetchMultiBtn, stopBtn;
    private JTextField threadLimitField;
    private JLabel activeLabel, finishedLabel, timerLabel;
    private JProgressBar loadBar;

    private ArrayList<String> urlList;
    private ArrayList<WebWorker> activeWorkers;
    private Launcher taskLauncher;
    private Semaphore fetchGate;
    private AtomicInteger activeCount, finishedCount;
    private long startTimestamp;

    // Constructor: sets up GUI and loads URLs
    public WebFrame() {
        super("Parallel Web Loader");
        urlList = new ArrayList<>();
        activeWorkers = new ArrayList<>();
        loadURLs();
        setupGUI();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    // Reads URLs from the file into urlList
    private void loadURLs() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                urlList.add(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to read URLs.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Builds the main GUI layout and components
    private void setupGUI() {
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));

        tableModel = new DefaultTableModel(new String[]{"URL", "Status"}, 0);
        urlList.forEach(url -> tableModel.addRow(new String[]{url, ""}));
        urlTable = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(urlTable);
        scroll.setPreferredSize(new Dimension(600, 300));
        root.add(scroll);

        fetchSingleBtn = new JButton("Fetch Single");
        fetchMultiBtn = new JButton("Fetch Concurrent");
        stopBtn = new JButton("Stop");
        stopBtn.setEnabled(false);

        threadLimitField = new JTextField("4", 5);
        threadLimitField.setMaximumSize(threadLimitField.getPreferredSize());

        activeLabel = new JLabel("Running: 0");
        finishedLabel = new JLabel("Completed: 0");
        timerLabel = new JLabel("Elapsed: 0.0s");
        loadBar = new JProgressBar(0, urlList.size());

        root.add(fetchSingleBtn);
        root.add(fetchMultiBtn);
        root.add(threadLimitField);
        root.add(activeLabel);
        root.add(finishedLabel);
        root.add(timerLabel);
        root.add(loadBar);
        root.add(stopBtn);

        add(root);

        attachEventHandlers();
    }


    // Connects button actions to their event handlers
    private void attachEventHandlers() {
        fetchSingleBtn.addActionListener(e -> launchFetch(1));
        fetchMultiBtn.addActionListener(e -> {
            try {
                int limit = Integer.parseInt(threadLimitField.getText().trim());
                if (limit > 0) launchFetch(limit);
            } catch (NumberFormatException ignored) {}
        });

        stopBtn.addActionListener(e -> cancelAll());
    }


    // Starts the Launcher thread with given concurrency level
    private void launchFetch(int concurrencyLevel) {
        resetState();
        taskLauncher = new Launcher(concurrencyLevel);
        taskLauncher.start();
    }
    // Resets counters, UI labels, and table for a new fetch session
    private void resetState() {
        fetchSingleBtn.setEnabled(false);
        fetchMultiBtn.setEnabled(false);
        stopBtn.setEnabled(true);

        activeCount = new AtomicInteger(1); // Include launcher thread
        finishedCount = new AtomicInteger(0);
        startTimestamp = System.currentTimeMillis();
        loadBar.setValue(0);

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt("", i, 1);
        }

        updateLabels();
    }

    // Cancels all running tasks and resets buttons
    private void cancelAll() {
        if (taskLauncher != null) taskLauncher.interrupt();
        for (WebWorker w : activeWorkers) w.interrupt();
        resetButtons();
    }

    private void resetButtons() {
        fetchSingleBtn.setEnabled(true);
        fetchMultiBtn.setEnabled(true);
        stopBtn.setEnabled(false);
    }

    private void updateLabels() {
        SwingUtilities.invokeLater(() -> {
            activeLabel.setText("Running: " + activeCount.get());
            finishedLabel.setText("Completed: " + finishedCount.get());
        });
    }

    // Called by WebWorker when it finishes or fails
    public void reportFinished(String status, int rowIndex) {
        activeCount.decrementAndGet();
        finishedCount.incrementAndGet();
        tableModel.setValueAt(status, rowIndex, 1);
        loadBar.setValue(finishedCount.get());

        updateLabels();
        checkIfComplete();
    }

    private void checkIfComplete() {
        if (activeCount.get() == 0) {
            long duration = System.currentTimeMillis() - startTimestamp;
            SwingUtilities.invokeLater(() -> {
                timerLabel.setText("Elapsed: " + (duration / 1000.0) + "s");
                resetButtons();
            });
        }
    }

    private class Launcher extends Thread {
        private  WebFrame frame;
        public Launcher(int limit) {
            this.frame = WebFrame.this;
            fetchGate = new Semaphore(limit);

        }

        @Override
        public void run() {
            for (int i = 0; i < urlList.size(); i++) {
                if (isInterrupted()) break;

                // Skip invalid URLs
                String url = urlList.get(i);
                if (url == null || !url.startsWith("http")) {

                    frame.reportFinished("err", i);
                    continue;
                }

                try {
                    fetchGate.acquire();
                    WebWorker worker = new WebWorker(url, i, WebFrame.this, fetchGate);
                    activeWorkers.add(worker);
                    activeCount.incrementAndGet();
                    worker.start();
                    updateLabels();
                } catch (InterruptedException ex) {
                    break;
                }
            }
            activeCount.decrementAndGet();
            updateLabels();
            checkIfComplete();
        }
    }

    // Launches the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(WebFrame::new);
    }
}
