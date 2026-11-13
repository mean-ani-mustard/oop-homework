import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


 public class SudokuFrame extends JFrame {
	 private JTextArea puzzle;
	 private JTextArea solution;
	 private Box buttonBox;
	 private JButton check;
	 private JCheckBox autoCheck;
	
	public SudokuFrame() {
		super("Sudoku Solver");

		setLayout(new BorderLayout(4, 4));

		puzzle = new JTextArea(15, 20);
		puzzle.setBorder(new TitledBorder("Puzzle"));
		solution = new JTextArea(15, 20);
		solution.setBorder(new TitledBorder("Solution"));
		solution.setEditable(false);

		add(puzzle, BorderLayout.WEST);
		add(solution, BorderLayout.EAST);

		buttonBox = Box.createHorizontalBox();

		check = new JButton("Check");
		autoCheck = new JCheckBox("Auto Check");
		autoCheck.setSelected(true);

		buttonBox.add(check);
		buttonBox.add(autoCheck);

		add(buttonBox, BorderLayout.SOUTH);

		check.addActionListener(e -> remakeSolutions());

// Create a single method to reduce repetition
		DocumentListener docListener = new DocumentListener() {
			private void update() {
				if (autoCheck.isSelected()) {
					remakeSolutions();
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				update();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				update();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				update();
			}
		};

		puzzle.getDocument().addDocumentListener(docListener);



		setLocationByPlatform(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	private void remakeSolutions(){

		Sudoku sudoku;

		try {
			sudoku = new Sudoku(Sudoku.textToGrid(puzzle.getText()));
		} catch (Exception e) {
			solution.setText("Parsing Problem");
			return;
		}
		String solText;
		int num = sudoku.solve();
		if(num>0){
			solText = sudoku.getSolutionText() + "\n" + "Solutions: " + num + "\n"
					+ "Elapsed time: " + sudoku.getElapsed() +"ms";

		}else {
			solText = "";
		}

		solution.setText(solText);

	}
	
	
	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		SudokuFrame frame = new SudokuFrame();
	}

}
