package org.bme.mit.iet.view.button;

import org.bme.mit.iet.Game;
import org.bme.mit.iet.field.Field;
import org.bme.mit.iet.view.GameView;

/**
 * A pumpafelvétel a ciszternánál gombot megvalósító osztály.
 */
public class PickUpPumpAtDest extends ActionButton {
    /**
     * A pumpafelvétel a ciszternánál gombot megvalósító osztály konstruktora, amely továbbhívja az ősosztály konstruktorát,
     * majd a játék állapota, a jelenlegi játékos és a jelenlegi mező alapján megállapítja,
     * hogy a gombot meg kell-e jeleníteni vagy sem a képernyő alján található buttongroupban.
     * Végül az addActionListener hozzáadásával megadja, hogy a gombra kattintáskor mi történjen.
     *
     * @param gameView a gombot tartalmazó gameView
     */
    public PickUpPumpAtDest(GameView gameView) {
        super(gameView, "pick-up-pump-at-dest");
        var game = Game.getInstance();

        if (game.getCurrentPlayer().getPump() != null) {
            //már van pumpa az inventoryban
            setVisible(false);
        }
        if (game.getCurrentPlayer().getCurrentField().getPump() == null) {
            //nincs pumpa a destinationon
            setVisible(false);
        }

        addActionListener(e -> {
            Field fieldToPickUp = game.getCurrentPlayer().getCurrentField().getPump();

            game.getCurrentPlayer().getCurrentField().pickUpNeighbourField(fieldToPickUp, game.getCurrentPlayer());

            game.setCurrentPlayerMadeAction(true);
        });
    }
}
