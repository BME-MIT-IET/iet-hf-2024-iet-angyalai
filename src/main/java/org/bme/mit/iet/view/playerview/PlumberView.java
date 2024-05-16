package org.bme.mit.iet.view.playerview;

import org.bme.mit.iet.player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Egy szerelő rajzolásért felelős osztály.
 */
public class PlumberView extends PlayerView {

    public PlumberView(Player plumber) {
        super(plumber);
    }

    /**
     * Betolti a szerelohoz tartozo kepet
     *
     * @param current attol fuggoen, hogy az adott szerelo a soron levo jatekos-e, lesz "" vagy "current"
     * @return a beolvasott kep
     */
    @Override
    public Image loadPlayerImage(String current) {
        String filePath = "/assets/board/plumber" + current + ".png";
        try {
            InputStream inputStream = getClass().getResourceAsStream(filePath);
            if (inputStream != null) {
                BufferedImage image = ImageIO.read(inputStream);
                Image scaledImage = image.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                return scaledImage;
            } else {
                JOptionPane.showMessageDialog(null, "Image not found: " + filePath, "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
