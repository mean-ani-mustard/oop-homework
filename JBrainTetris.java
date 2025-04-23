

import javax.swing.*;
import java.awt.*;

public class JBrainTetris extends JTetris {

    private Brain brain;
    private Brain.Move currentBestMove;
    private Brain.Move testMove;

    private JCheckBox useBrainCheckbox;
    private JCheckBox smoothDropCheckbox;
    private JSlider adversarySlider;
    private JLabel adversaryStatus;

    private int lastTickCount;

    public JBrainTetris(int squareSize) {
        super(squareSize);
        brain = new DefaultBrain();
        lastTickCount = -1;
    }


    public JComponent createControlPanel() {
        JComponent panel = super.createControlPanel();

        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Controls"));

        useBrainCheckbox = new JCheckBox("Brain");
        smoothDropCheckbox = new JCheckBox("Smooth Drop");
        smoothDropCheckbox.setSelected(true);

        panel.add(useBrainCheckbox);
        panel.add(smoothDropCheckbox);

        JPanel adversaryPanel = new JPanel();
        adversaryPanel.add(new JLabel("Adversary:"));
        adversarySlider = new JSlider(0, 100, 0);
        adversarySlider.setPreferredSize(new Dimension(100, 20));
        adversaryPanel.add(adversarySlider);
        panel.add(adversaryPanel);

        adversaryStatus = new JLabel(" OK");
        panel.add(adversaryStatus);

        return panel;
    }


    public void tick(int verb) {
        if (verb == DOWN && useBrainCheckbox.isSelected()) {
            if (lastTickCount != count) {
                board.undo();
                currentBestMove = brain.bestMove(board, currentPiece, board.getHeight() - TOP_SPACE, currentBestMove);
                lastTickCount = count;
            }

            if (currentBestMove != null) {
                if (!currentPiece.equals(currentBestMove.piece)) {
                    super.tick(ROTATE);
                }

                if (currentX < currentBestMove.x) {
                    super.tick(RIGHT);
                } else if (currentX > currentBestMove.x) {
                    super.tick(LEFT);
                } else if (!smoothDropCheckbox.isSelected() && currentY > currentBestMove.y) {
                    super.tick(DROP);
                }
            }
        }

        super.tick(verb);
    }
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {

        }
        JBrainTetris tetris = new JBrainTetris(16);
        JFrame frame = JTetris.createFrame(tetris);
        frame.setVisible(true);
    }

    public Piece pickNextPiece() {
        int chance = 1 + random.nextInt(99);
        if (chance < adversarySlider.getValue()) {
            adversaryStatus.setText("Status: *OK*");

            Piece worstPiece = pieces[0];
            double highestScore = 0;

            for (Piece candidate : pieces) {
                testMove = brain.bestMove(board, candidate, board.getHeight() - TOP_SPACE, testMove);
                if (testMove != null && testMove.score > highestScore) {
                    highestScore = testMove.score;
                    worstPiece = candidate;
                }
            }

            return worstPiece;
        }

        adversaryStatus.setText("Status: OK");
        return super.pickNextPiece();
    }


}
