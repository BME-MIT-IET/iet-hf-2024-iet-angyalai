package org.bme.mit.iet.view.button;

import org.bme.mit.iet.Game;
import org.bme.mit.iet.view.GameView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Az összes játékban szereplő gomb ősosztályát megvalósító osztály. Ebből sz osztályból származik a többi gomb, melyek az ős konstruktorát használják minden létrehozáskor.
 */
public abstract class ActionButton extends JButton {
    /**
     * A játék gombjainak ősosztályának konstruktora, magába foglalja a gombok
     * képeinek elérési útjának összerakását, a kép betöltését,
     * a gomb láthatóvá tételét és az alap kattintási eventet
     *
     * @param gameView a gombot tartalmazó gameView
     * @param btnName  a gomb neve a képfájl eléréséhez
     */
    public ActionButton(GameView gameView, String btnName) {
        setBorder(BorderFactory.createEmptyBorder());
        setContentAreaFilled(false);
        String filePath = "assets/buttons/" + btnName + ".png";
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            if (inputStream != null) {
                BufferedImage image = ImageIO.read(inputStream);
                Image scaledImage = image.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                setIcon(new ImageIcon(scaledImage));
            } else {
                JOptionPane.showMessageDialog(null, "Image not found: " + filePath, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (Game.getInstance().isCurrentPlayerMadeAction()) {
            setVisible(false);
        }
        addActionListener(e -> {
            System.out.println(btnName + " button is pressed!");
            gameView.setGame();
        });
    }
}
