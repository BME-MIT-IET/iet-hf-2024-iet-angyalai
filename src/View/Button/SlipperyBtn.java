package View.Button;

import Main.Game;
import View.GameView;

/**
 * A cső csúszóssátétel gombot megvalósító osztály.
 */
public class SlipperyBtn extends ActionButton {
    /**
     * A cső csúszóssátétel gombot megvalósító osztály konstruktora, amely továbbhívja az ősosztály konstruktorát,
     * majd a játék állapota, a jelenlegi játékos és a jelenlegi mező alapján megállapítja,
     * hogy a gombot meg kell-e jeleníteni vagy sem a képernyő alján található buttongroupban.
     * Végül az addActionListener hozzáadásával megadja, hogy a gombra kattintáskor mi történjen.
     *
     * @param gameView a gombot tartalmazó gameView
     */
    public SlipperyBtn(GameView gameView) {
        super(gameView, "slippery");
        var game = Game.getInstance();

        if (game.getCurrentPlayer().getCurrentField().isSlippery()) {
            //pipe is already slippery
            setVisible(false);
        }
        if (Game.getInstance().getBoard().getPlums().contains(game.getCurrentPlayer())) {
            //current player is plumber
            setVisible(false);
        }


        addActionListener(e -> {
            game.getCurrentPlayer().changeField();
            game.setCurrentPlayerMadeAction(true);
        });
    }
}
