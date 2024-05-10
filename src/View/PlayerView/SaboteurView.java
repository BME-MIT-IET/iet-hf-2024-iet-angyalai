package View.PlayerView;

import Player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Egy szabotőr rajzolásért felelős osztály.
 */
public class SaboteurView extends PlayerView {
    public SaboteurView(Player saboteur) {
        super(saboteur);
    }

    /**
     * Betolti a szabotorhoz tartozo kepet
     *
     * @param current attol fuggoen, hogy az adott szabotor a soron levo jatekos-e, lesz "" vagy "current"
     * @return a beolvasott kep
     */
    @Override
    public Image loadPlayerImage(String current) {
        String filePath = "/assets/board/saboteur" + current + ".png";
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
