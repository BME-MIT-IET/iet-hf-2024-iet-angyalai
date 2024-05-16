package org.bme.mit.iet.view;


import javax.swing.*;
import java.awt.*;

/**
 * A játék megjelenítésére használt osztályok egy modulba foglalása, játék állapotának megjelenítése, kirajzolja a játéktáblát és a játék vezérléséhez szükséges gombokat.
 */
public class GameView extends JPanel {

    public GameView() {
        this.setLayout(new GridBagLayout());
    }

    /**
     * Letrehoz egy StatusBarView, egy ActionButtonGroup es egy BoardView objektumot es a megfelelo pozicioba helyezi oket.
     */
    public void setGame() {
        removeAll();
        var statusBar = new StatusBarView();
        var btnGroup = new ActionButtonGroup();
        var boardView = new BoardView(this, btnGroup);

        var c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0.5;
        add(statusBar, c);

        c.gridy = 1;
        c.weighty = 10;
        add(boardView, c);

        c.gridy = 2;
        c.weighty = 0;
        add(btnGroup, c);
        revalidate();
        repaint();
    }
}
