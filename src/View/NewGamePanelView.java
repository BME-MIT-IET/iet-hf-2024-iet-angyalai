package View;

import javax.swing.*;
import java.awt.*;

/**
 * Felugró dialógus ablakot megjelenítő osztály új játék létrehozásához, a felhasználó által megadott információkat tárolja.
 */
public class NewGamePanelView extends JPanel {
    private static final String PANEL_TITTLE = "Start New Game";
    private static final String[] BTN_OPTIONS = {"Start Game", "Exit Game"};
    private final int numberOfRounds;
    private final int numberOfPlumbers;
    private final int numberOfSaboteurs;
    /**
     * A játék hossza, körök számában megadva.
     */
    private JTextField numberOfRoundsTField;
    /**
     * A játékban résztvevő szerelők száma.
     */
    private JTextField numberOfPlumbersTField;
    /**
     * A játékban résztvevő szabotorok száma.
     */
    private JTextField numberOfSaboteursTField;

    /**
     * Konstruktor, letrehoz egy JOptionPane-t.
     *
     * @param gui
     * @throws NumberFormatException
     */
    public NewGamePanelView(JFrame gui) throws NumberFormatException {
        this.build();
        int shouldStart = JOptionPane.showOptionDialog(gui, this, PANEL_TITTLE, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, BTN_OPTIONS, BTN_OPTIONS[0]);

        if (shouldStart != 0) {
            numberOfSaboteurs = 0;
            numberOfPlumbers = 0;
            numberOfRounds = 0;
            return;
        }
        try {
            numberOfRounds = Integer.parseInt(numberOfRoundsTField.getText());
            numberOfPlumbers = Integer.parseInt(numberOfPlumbersTField.getText());
            numberOfSaboteurs = Integer.parseInt(numberOfSaboteursTField.getText());
        } catch (NumberFormatException e) {
            throw e;
        }

    }

    /**
     * Megjeleníti a dialógusablakot a játék létrehozásához szükséges adatok begyűjtésére szolgáló beviteli mezőkkel.
     */
    private void build() {
        numberOfRoundsTField = new JTextField("10", 10);
        numberOfPlumbersTField = new JTextField("2", 10);
        numberOfSaboteursTField = new JTextField("2", 10);


        var upperPanel = new JPanel();
        upperPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        upperPanel.add(new JLabel(("Plumbers:")));
        upperPanel.add(numberOfPlumbersTField);
        upperPanel.add(new JLabel(("Saboteurs:")));
        upperPanel.add(numberOfSaboteursTField);

        var lowerPanel = new JPanel();
        lowerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        lowerPanel.add(new JLabel(("Rounds:")));
        lowerPanel.add(numberOfRoundsTField);

        this.setLayout(new GridLayout(2, 1));
        this.add(upperPanel);
        this.add(lowerPanel);
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public int getNumberOfPlumbers() {
        return numberOfPlumbers;
    }

    public int getNumberOfSaboteurs() {
        return numberOfSaboteurs;
    }
}
