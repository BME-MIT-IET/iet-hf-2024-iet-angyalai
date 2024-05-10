package View.Button;

import Main.Game;
import View.GameView;

/**
 * A skip gombot megvalósító osztály.
 */
public class SkipBtn extends ActionButton {
    /**
     * A skip gombot megvalósító osztály konstruktora, amely továbbhívja az ősosztály konstruktorát,
     * majd a játék állapota, a jelenlegi játékos és a jelenlegi mező alapján megállapítja,
     * hogy a gombot meg kell-e jeleníteni vagy sem a képernyő alján található buttongroupban.
     * Végül az addActionListener hozzáadásával megadja, hogy a gombra kattintáskor mi történjen.
     *
     * @param gameView a gombot tartalmazó gameView
     */
    public SkipBtn(GameView gameView) {
        super(gameView, "skip");
        setVisible(true);
        addActionListener(e -> {
            Game.getInstance().nextPlayer();
        });
    }
}
