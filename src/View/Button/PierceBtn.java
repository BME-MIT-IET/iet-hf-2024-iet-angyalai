package View.Button;

import Main.Game;
import View.GameView;

/**
 * A lyukasztás gombot megvalósító osztály.
 */
public class PierceBtn extends ActionButton {
    /**
     * A lyukasztás gombot megvalósító osztály konstruktora, amely továbbhívja az ősosztály konstruktorát,
     * majd a játék állapota, a jelenlegi játékos és a jelenlegi mező alapján megállapítja,
     * hogy a gombot meg kell-e jeleníteni vagy sem a képernyő alján található buttongroupban.
     * Végül az addActionListener hozzáadásával megadja, hogy a gombra kattintáskor mi történjen.
     *
     * @param gameView a gombot tartalmazó gameView
     */
    public PierceBtn(GameView gameView) {
        super(gameView, "pierce");
        var game = Game.getInstance();

        if (!game.getCurrentPlayer().getCurrentField().isWorking() || game.getCurrentPlayer().getCurrentField().getPiercedCounter() != 0) {
            setVisible(false);
        }

        addActionListener(e -> {
            game.getCurrentPlayer().piercePipe();
            game.setCurrentPlayerMadeAction(true);
        });
    }
}
