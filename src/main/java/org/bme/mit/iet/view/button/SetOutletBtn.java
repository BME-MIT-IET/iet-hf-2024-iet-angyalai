package org.bme.mit.iet.view.button;


import org.bme.mit.iet.Game;
import org.bme.mit.iet.field.Field;
import org.bme.mit.iet.view.GameView;

/**
 * A pumpa kimenetbeállító gombot megvalósító osztály.
 */
public class SetOutletBtn extends ActionButton {
    /**
     * A pumpa kimenetbeállító gombot megvalósító osztály konstruktora, amely továbbhívja az ősosztály konstruktorát,
     * majd a játék állapota, a jelenlegi játékos és a jelenlegi mező alapján megállapítja,
     * hogy a gombot meg kell-e jeleníteni vagy sem a képernyő alján található buttongroupban.
     * Végül az addActionListener hozzáadásával megadja, hogy a gombra kattintáskor mi történjen.
     *
     * @param gameView a gombot tartalmazó gameView
     * @param field    a mező, amelyre a tevékenység irányul
     */
    public SetOutletBtn(GameView gameView, Field field) {
        super(gameView, "setoutlet");
        var game = Game.getInstance();

        if (game.getBoard().getPlums().contains(field)) {
            //clicked field is a pump
            setVisible(false);
        }
        if (game.getBoard().getDests().contains(game.getCurrentPlayer().getCurrentField())) {
            //current field is a destination
            setVisible(false);
        }
        addActionListener(e -> {


            Field[] inAndOutlet = game.getCurrentPlayer().getCurrentField().getInOutlet();

            if (inAndOutlet[0] != null) {
                game.getCurrentPlayer().getCurrentField().setInOutlet(inAndOutlet[0], field);
            } else {
                game.getCurrentPlayer().getCurrentField().setInOutlet(null, field);
            }

            game.setCurrentPlayerMadeAction(true);
        });
    }
}

