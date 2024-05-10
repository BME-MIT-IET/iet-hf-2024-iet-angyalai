package View.Button;

import Main.Game;
import View.GameView;

/**
 * A  cső ragadóssátétel gombot megvalósító osztály.
 */
public class StickyBtn extends ActionButton {
    /**
     * A  cső ragadóssátétel gombot megvalósító osztály konstruktora, amely továbbhívja az ősosztály konstruktorát,
     * majd a játék állapota, a jelenlegi játékos és a jelenlegi mező alapján megállapítja,
     * hogy a gombot meg kell-e jeleníteni vagy sem a képernyő alján található buttongroupban.
     * Végül az addActionListener hozzáadásával megadja, hogy a gombra kattintáskor mi történjen.
     *
     * @param gameView a gombot tartalmazó gameView
     */
    public StickyBtn(GameView gameView) {
        super(gameView, "sticky");
        var game = Game.getInstance();

        if (game.getCurrentPlayer().getCurrentField().isSticky() || game.getCurrentPlayer().getCurrentField().isSlippery()) {
            setVisible(false);
        }

        addActionListener(e -> {
            game.getCurrentPlayer().makePipeSticky();
            game.setCurrentPlayerMadeAction(true);
        });
    }
}
