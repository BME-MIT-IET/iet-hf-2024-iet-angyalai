package org.bme.mit.iet.view;

import org.bme.mit.iet.view.button.ActionButton;
import org.bme.mit.iet.view.button.SkipBtn;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Összefogja a vezérlő gombokat egy osztályba.
 */
public class ActionButtonGroup extends JPanel {
    /**
     * A jatek vezerlesere alkalmas gombok listaja
     */
    private final List<ActionButton> buttons = new ArrayList<>();

    public ActionButtonGroup() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(4, 8, 8, 8, Color.BLACK), BorderFactory.createEmptyBorder(0, 0, 0, 0)));
    }

    /**
     * A jelenleg látható gombok listájának ürítése.
     */
    public void clearButtons() {
        buttons.clear();
        removeAll();
    }

    /**
     * A jelenleg látható gombok listájának bővítésére alkalmas metódus.
     *
     * @param btn hozzaadni kivant button
     */
    public void addButton(ActionButton btn) {
        buttons.add(btn);
    }

    /**
     * Frissiti a button-ok listajat
     *
     * @param gameView
     */
    public void updateButtonGroup(GameView gameView) {
        for (ActionButton btn : buttons) {
            add(btn);
        }
        add(new SkipBtn(gameView));
        revalidate();
        repaint();
    }
}
