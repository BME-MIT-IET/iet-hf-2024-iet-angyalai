package org.bme.mit.iet.view.button;


import org.bme.mit.iet.Game;
import org.bme.mit.iet.view.GameView;

/**
 * A javítás gombot megvalósító osztály.
 */
public class RepairBtn extends ActionButton {
    /**
     * A javítás gombot megvalósító osztály konstruktora, amely továbbhívja az ősosztály konstruktorát,
     * majd a játék állapota, a jelenlegi játékos és a jelenlegi mező alapján megállapítja,
     * hogy a gombot meg kell-e jeleníteni vagy sem a képernyő alján található buttongroupban.
     * Végül az addActionListener hozzáadásával megadja, hogy a gombra kattintáskor mi történjen.
     *
     * @param gv a gombot tartalmazó gameView
     */
    public RepairBtn(GameView gv) {
        super(gv, "repair");
        var game = Game.getInstance();

        if (game.getCurrentPlayer().getCurrentField().isWorking()) {
            //field is working, nothing to repair
            setVisible(false);
        }
        if (Game.getInstance().getBoard().getSabs().contains(game.getCurrentPlayer())) {
            //current player is saboteur, who cant repair
            setVisible(false);
        }

        addActionListener(e -> {
            game.getCurrentPlayer().changeField();
            game.setCurrentPlayerMadeAction(true);
        });
    }
}
