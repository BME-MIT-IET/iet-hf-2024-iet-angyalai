package org.bme.mit.iet.view;

import org.bme.mit.iet.FileHandler;
import org.bme.mit.iet.Game;
import org.bme.mit.iet.exceptions.LoadException;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

/**
 * Létrehozza a felhasználói ablakot, létrehozza a menü sávot, új játékot generál.
 */
public class GUI extends JFrame {
    private final GameView gameView;
    private static final String SABOTEUR_POINTS = "Saboteur points: ";
    private static final String PLUMBER_POINTS = "Plumber points: ";
    private static final String GAME_OVER = "Game Over";
    private static final Logger LOGGER = Logger.getLogger(GUI.class.getName());

    /**
     * Konstruktor, beallitja a JFrame tulajdonsagait. Amikor kivaltodik a gameOver esemeny, megjelenit egy JOptionPane-t, amely felajanlja, hogy
     * uj jatekot kezdjunk, vagy kilepunk, kiirja a pontszamokat, illetve hogy ki nyert.
     */
    public GUI() {
        super();
        gameView = new GameView();
        add(this.gameView);
        setJMenuBar(new MenuBarView(this));
        setMinimumSize(new Dimension(960, 960));
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Schemmischeg");
        setLocationRelativeTo(null);
        newGame();

        JFrame frame = this;
        Game.getInstance().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Object[] options = {"Start New Game", "End Game"};
                int n;

                if (Game.getInstance().getPlumberPoints() < Game.getInstance().getSaboteursPoints()) {
                    n = JOptionPane.showOptionDialog(frame, PLUMBER_POINTS + Game.getInstance().getPlumberPoints() + "\n" + SABOTEUR_POINTS + Game.getInstance().getSaboteursPoints() + "\nSaboteurs win!", GAME_OVER, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
                } else if (Game.getInstance().getPlumberPoints() > Game.getInstance().getSaboteursPoints()) {
                    n = JOptionPane.showOptionDialog(frame, PLUMBER_POINTS + Game.getInstance().getPlumberPoints() + "\n" + SABOTEUR_POINTS + Game.getInstance().getSaboteursPoints() + "\nPlumbers win!", GAME_OVER, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
                } else
                    n = JOptionPane.showOptionDialog(frame, PLUMBER_POINTS + Game.getInstance().getPlumberPoints() + "\n" + SABOTEUR_POINTS + Game.getInstance().getSaboteursPoints() + "\nDraw!", GAME_OVER, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);

                if (n == JOptionPane.YES_OPTION) {   //ha azt valasztja, hogy uj jatek kezdese
                    newGame();
                } else if (n == JOptionPane.NO_OPTION)//azt valasztja, hogy kilepes a jatekbol
                    frame.dispose();
            }
        });
    }

    /**
     * egy új játék létrehozásáért felelős függvény.
     */
    public void newGame() {
        NewGamePanelView options = null;
        try {
            options = new NewGamePanelView(this);
        } catch (NumberFormatException e) {
            newGame();
            return;
        }
        if (options.getNumberOfSaboteurs() == 0 && options.getNumberOfPlumbers() == 0 && options.getNumberOfRounds() == 0) {
            System.exit(0);
        }
        if (options.getNumberOfRounds() <= 0 || options.getNumberOfPlumbers() < 2 || options.getNumberOfSaboteurs() < 2) {
            newGame();
            return;
        }

        Game.getInstance().createNewGame(options.getNumberOfRounds(), options.getNumberOfPlumbers(), options.getNumberOfSaboteurs());
        gameView.setGame();
    }

    /**
     * fájlba menti  a játékállást.
     */
    public void saveGame() {
        var game = Game.getInstance();
        new FileHandler().save("save.json", game);
    }

    /**
     * fájlból betölti a játékállást.
     */
    public void loadGame() {
        try {
            new FileHandler().load("save.json");
        } catch (LoadException e) {
            LOGGER.info(e.getMessage());
        }
        gameView.setGame();
    }
}
