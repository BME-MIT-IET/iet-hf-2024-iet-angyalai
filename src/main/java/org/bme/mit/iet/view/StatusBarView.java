package org.bme.mit.iet.view;




import org.bme.mit.iet.Game;

import javax.swing.*;
import java.awt.*;

/**
 * A játék állapotát tükröző sáv megjelenítése, ezen keresztül lehet látni hogy melyik játékos van soron éppen, a játék éppen milyen szakaszában van,
 * melyik csapatnak mennyi pontja van, a soron lévő játékos milyen pálya elemeket tud lerakni vagy felvenni ha nincs nála.
 */
public class StatusBarView extends JPanel {

    private final Game game;

    /**
     * Konstruktor, letrehoz paneleket,amelyeken megjelenik a soron levo jatekos id-ja, az aktualis kor sorszama, valamint hogy a jatekosnal van-e cso/pumpa.
     */
    public StatusBarView() {
        this.game = Game.getInstance();
        this.setLayout(new GridLayout(1, 3));
        this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(4, 4, 2, 4, Color.BLACK), BorderFactory.createEmptyBorder(0, 0, 0, 0)));

        var currentPlayerPanel = new JPanel();
        currentPlayerPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(4, 4, 2, 4, Color.BLACK), BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        currentPlayerPanel.add(new JLabel("Current Player(ID): "));
        currentPlayerPanel.add(new JLabel(String.valueOf(game.getCurrentPlayer().getId())));
        this.add(currentPlayerPanel);

        var currentRoundPanel = new JPanel();
        currentRoundPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(4, 4, 2, 4, Color.BLACK), BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        currentRoundPanel.add(new JLabel("Round: "));
        currentRoundPanel.add(new JLabel(String.valueOf(game.getCurrentRound())));

        this.add(currentRoundPanel);

        var invPanel = new JPanel(new GridLayout(2, 1));
        invPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(4, 4, 2, 4, Color.BLACK), BorderFactory.createEmptyBorder(0, 0, 0, 0)));

        var pipePanel = new JPanel();
        pipePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK), BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        var pipeLabel = new JLabel("Pipe: ");
        var pipes = game.getCurrentPlayer().getPipes();
        var pipeNumber = new JLabel(String.valueOf(pipes[0] != null || pipes[1] != null ? 1 : 0));
        pipePanel.add(pipeLabel);
        pipePanel.add(pipeNumber);
        invPanel.add(pipePanel);

        var pumpPanel = new JPanel();
        pumpPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK), BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        var pumpLabel = new JLabel("Pump: ");
        var pumpNumber = new JLabel(String.valueOf(game.getCurrentPlayer().getPump() != null ? 1 : 0));
        pumpPanel.add(pumpLabel);
        pumpPanel.add(pumpNumber);
        invPanel.add(pumpPanel);
        this.add(invPanel);
    }
}
