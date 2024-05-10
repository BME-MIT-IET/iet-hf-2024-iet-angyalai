package View;

import javax.swing.*;

/**
 * A felhasználói felület tetején látható menüsor megjelenítése, azon lévő három gomb kezelése.
 */
public class MenuBarView extends JMenuBar {
    private static final String NEW_GAME = "New game";
    private static final String SAVE_GAME = "Save game";
    private static final String LOAD_GAME = "Load game";
    private static final String FILE = "File";

    /**
     * Konstruktor, beallitja a JMenuItem-eket, es ActionListenereket ad hozza
     *
     * @param gui
     */
    public MenuBarView(GUI gui) {
        super();
        var newGame = new JMenuItem(NEW_GAME);
        var saveGame = new JMenuItem(SAVE_GAME);
        var loadGame = new JMenuItem(LOAD_GAME);

        newGame.addActionListener(e -> gui.newGame());
        saveGame.addActionListener(e -> gui.saveGame());
        loadGame.addActionListener(e -> gui.loadGame());

        var fileMenu = new JMenu(FILE);
        fileMenu.add(newGame);
        fileMenu.add(saveGame);
        fileMenu.add(loadGame);

        add(fileMenu);
    }
}
