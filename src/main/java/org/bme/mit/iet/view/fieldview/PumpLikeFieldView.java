package org.bme.mit.iet.view.fieldview;



import org.bme.mit.iet.Game;
import org.bme.mit.iet.field.Field;
import org.bme.mit.iet.view.ActionButtonGroup;
import org.bme.mit.iet.view.GameView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * A pumpához hasonló mezők view ősosztálya, közös kirajzolási függvényeiket tartalmazza.
 */
public abstract class PumpLikeFieldView extends JButton {
    protected final ActionButtonGroup btnGroup;
    protected final GameView gameView;
    protected final Field field;

    /**
     * A pumpához hasonló mezők view ősosztálya konstruktora
     *
     * @param gameView a PumpLikeFieldViewet tartalmazó gameView
     * @param btnGroup az alsó buttongroup melyben a gombok jelennek meg
     * @param field    a mező maga, melyhez a view tartozik
     * @param name     a mező meve
     */
    public PumpLikeFieldView(GameView gameView, ActionButtonGroup btnGroup, Field field, String name) {
        this.gameView = gameView;
        this.btnGroup = btnGroup;
        this.field = field;
        setBorder(BorderFactory.createEmptyBorder());
        setContentAreaFilled(false);
        loadFieldImage(name);
        var currentField = Game.getInstance().getCurrentPlayer().getCurrentField();
        if (currentField == field) {
            btnGroup.clearButtons();
            fieldIsCurrentField();
            btnGroup.updateButtonGroup(gameView);
        }
        addActionListener(e -> {
            System.out.println(name + ": " + field.getId() + " is pressed!");
            loadBtnGroupOnPress(currentField);
        });
    }

    /**
     * A mezőre kattintáskor lefutó függvény, mely meghatározza, hogy a kattintást követően
     * a mező szomszédkét vagy jelenlegi mezőként viselkedjen, az alsó buttongroupban melyik gombokat jelenítse meg
     *
     * @param currentField a jelenlegi játékos mezője
     */
    private void loadBtnGroupOnPress(Field currentField) {
        btnGroup.clearButtons();
        if (currentField != field && !currentField.getNeighbours().contains(field)) {
        } else if (currentField == field) {
            fieldIsCurrentField();
        } else {
            fieldIsNeighbour();
        }
        btnGroup.updateButtonGroup(gameView);
    }

    /**
     * A konstruktorban átadott filename alapján betölti a mező képét az assetekből
     *
     * @param name a mező neve a képfájlbetöltéshez
     */
    private void loadFieldImage(String name) {
        String filePath = "assets/board/" + name + ".png";
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            if (inputStream != null) {
                BufferedImage image = ImageIO.read(inputStream);
                Image scaledImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                setIcon(new ImageIcon(scaledImage));
            } else {
                JOptionPane.showMessageDialog(null, "Image not found: " + filePath, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Empty method, override repaint behavior to disable it
     */
    @Override
    public void repaint() {
        // Empty method, override repaint behavior to disable it
    }

    /**
     * Visszaadja a view mezőjét
     *
     * @return a viewhez tartozó mező
     */
    public Field getField() {
        return field;
    }

    /**
     * Absztrakt függvény, az alosztályok megvalósítják, ha a mező jelenleg szomszédos és rákattintottak
     */
    public abstract void fieldIsNeighbour();

    /**
     * Absztrakt függvény, az alosztályok megvalósítják, ha a mező a jelenlegi mező, azaz a jelenlegi játékos rajta áll
     */
    public abstract void fieldIsCurrentField();
}