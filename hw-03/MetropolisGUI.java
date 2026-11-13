import javax.swing.*;
import java.awt.*;
/**
 * MetropolisGUI represents the graphical user interface (GUI) for the Metropolis Viewer.
 * This GUI allows users to add and search for metropolis data, including filtering by continent, population, and match type.
 */

public class MetropolisGUI extends JFrame {

    private JTextField metropolis;
    private JTextField continent;
    private JTextField population;
    private JButton add;
    private JButton search;
    private JComboBox popuBox;
    private JComboBox matchBox;
    private JTable table;
    private JPanel upper;

    private MetropolisdbModel model; //####
    private JScrollPane scrollPane;
    // Constants for the population filter and match type options
    private static final String POPU_SMALL = "Population Smaller Then";
    private static final String POPU_LARGE  = "Population Larger Then";
    private static final String EXACT = "Exact Match";
    private static final String PARTIAL = "Partial Match";
    private static final int TXTF_SIZE = 10;
    private static final String [] cols = { "Metropolis", "Continent", "Population" };

    /**
     * Constructor to initialize the Metropolis GUI components and layout.
     */

    public MetropolisGUI(){
        // Set up the frame title and layout
        super("Metropolis Viewer");
        BorderLayout layout = new BorderLayout(4, 4);
        setLayout(layout);
        // Set default close operation for the JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        upper = new JPanel();
        metropolis = new JTextField(TXTF_SIZE);
        continent = new JTextField(TXTF_SIZE);
        population = new JTextField(TXTF_SIZE);
        JLabel m = new JLabel(" Metropolis: ");
        JLabel c = new JLabel(" Continent: ");
        JLabel p = new JLabel(" Population: ");
        upper.add(m);
        upper.add(metropolis);
        upper.add(c);
        upper.add(continent);
        upper.add(p);
        upper.add(population);
        add(upper, BorderLayout.NORTH);

        add =  new JButton("add");
        add.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        search = new JButton("search");
        search.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        JLabel opts = new JLabel("Search options: ");
        opts.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
        String [] popuOptions = new String[]{POPU_LARGE, POPU_SMALL};
        String [] matches = new String[]{EXACT, PARTIAL};
        popuBox = new JComboBox<>(popuOptions);
        matchBox =  new JComboBox<>(matches);
        Box buttons = Box.createVerticalBox();
        Box comboBoxes = Box.createVerticalBox();
        matchBox.setMaximumSize(new Dimension(matchBox.getMaximumSize().width , matchBox.getMinimumSize().height));
        popuBox.setMaximumSize(new Dimension(matchBox.getMaximumSize().width , matchBox.getMinimumSize().height));


        comboBoxes.add(opts);
        comboBoxes.add(popuBox);
        comboBoxes.add(matchBox);
        buttons.add(add);
        buttons.add(search);
        buttons.add(comboBoxes);
        add(buttons, BorderLayout.EAST);

        // Set up the table to display metropolis data
        model = new MetropolisdbModel();
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        actionEvents();
        // Pack the components into the frame and make it visible
        pack();
        setVisible(true);

    }

    private void actionEvents() {

        /*
         * In this add method we retrieve the input values from the text fields, and if the condition that fields
         * must be non-empty is met we
         * After that we query the database and the result is shown on the screen.
         *
         */

        add.addActionListener(e -> {
            String metro = metropolis.getText().trim();
            String cont = continent.getText().trim();
            String popStr = population.getText().trim();
            try {
                int pop = Integer.parseInt(popStr);
                if(!metro.isEmpty() &&!cont.isEmpty() && !popStr.isEmpty()){
                    model.addMetropolis(metro, cont, popStr);
                }else{
                    JOptionPane.showMessageDialog(this, "All fields must be filled.", "Input Error", JOptionPane.WARNING_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Population must be an integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        /*
         * In this search method we retrieve the input values from the text fields and combo boxes, and store
         * all the information needed to query the database, if the condition that population must be
         * a number is met. After that we query the database and the result is shown on the screen.
         *
         */

        search.addActionListener(e -> {
            String metro = metropolis.getText().trim();
            String cont = continent.getText().trim();
            String popStr = population.getText().trim();
            String popFilter = (String) popuBox.getSelectedItem();
            String matchType = (String) matchBox.getSelectedItem();
            Integer pop = null;
            if (!popStr.isEmpty()) {
                try {
                    pop = Integer.parseInt(popStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Population must be an integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            model.searchMetropolises(metro, cont, popStr, popFilter, matchType);
        });

    }
}
