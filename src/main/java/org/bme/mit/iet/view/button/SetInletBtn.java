package org.bme.mit.iet.view.button;

import org.bme.mit.iet.Game;
import org.bme.mit.iet.field.Field;
import org.bme.mit.iet.view.GameView;

/**
 * A pumpa bemenetbeállító gombot megvalósító osztály.
 */
public class SetInletBtn extends ActionButton {
    /**
     * A pumpa bemenetbeállító gombot megvalósító osztály konstruktora, amely továbbhívja az ősosztály konstruktorát,
     * majd a játék állapota, a jelenlegi játékos és a jelenlegi mező alapján megállapítja,
     * hogy a gombot meg kell-e jeleníteni vagy sem a képernyő alján található buttongroupban.
     * Végül az addActionListener hozzáadásával megadja, hogy a gombra kattintáskor mi történjen.
     *
     * @param gameView a gombot tartalmazó gameView
     * @param field    a mező, amelyre a tevékenység irányul
     */
    public SetInletBtn(GameView gameView, Field field) {
        super(gameView, "setinlet");
        var game = Game.getInstance();

        if (game.getBoard().getPlums().contains(field)) {
            //clicked field is a pump
            setVisible(false);
        }
        if (game.getBoard().getSrcs().contains(game.getCurrentPlayer().getCurrentField())) {
            //current field is a source
            setVisible(false);
        }

        addActionListener(e -> {

            Field[] inAndOutlet = game.getCurrentPlayer().getCurrentField().getInOutlet();

            if (inAndOutlet[1] != null) {
                game.getCurrentPlayer().getCurrentField().setInOutlet(field, inAndOutlet[1]);
            } else {
                game.getCurrentPlayer().getCurrentField().setInOutlet(field, null);
            }

            game.setCurrentPlayerMadeAction(true);
        });
    }
}