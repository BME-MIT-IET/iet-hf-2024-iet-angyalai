package org.bme.mit.iet.view.button;


import org.bme.mit.iet.Game;
import org.bme.mit.iet.field.Field;
import org.bme.mit.iet.view.GameView;

/**
 * A mezőlehelyezés gombot megvalósító osztály.
 */
public class PlaceFieldBtn extends ActionButton {
    /**
     * A mezőlehelyezés gombot megvalósító osztály konstruktora, amely továbbhívja az ősosztály konstruktorát,
     * majd a játék állapota, a jelenlegi játékos és a jelenlegi mező alapján megállapítja,
     * hogy a gombot meg kell-e jeleníteni vagy sem a képernyő alján található buttongroupban.
     * Végül az addActionListener hozzáadásával megadja, hogy a gombra kattintáskor mi történjen.
     *
     * @param gameView a gombot tartalmazó gameView
     * @param field    a mező, amelyre a tevékenység irányul
     */
    public PlaceFieldBtn(GameView gameView, Field field) {
        super(gameView, "place-field");
        var game = Game.getInstance();
        if (game.getBoard().getPipes().contains(field)) {
            //field is pipe and we want to place a pump
            if (game.getCurrentPlayer().getPump() != null) {
                addActionListener(e -> game.getCurrentPlayer().placeField(game.getCurrentPlayer().getPump()));
            } else {
                setVisible(false);
            }
        } else {

            //field is pump and we want to place a pipe
            if (game.getCurrentPlayer().getPipes()[0] != null) {
                addActionListener(e -> game.getCurrentPlayer().placeField(game.getCurrentPlayer().getPipes()[0]));
            } else if (game.getCurrentPlayer().getPipes()[1] != null) {
                addActionListener(e -> game.getCurrentPlayer().placeField(game.getCurrentPlayer().getPipes()[1]));
            } else {
                setVisible(false);
            }
        }

        addActionListener(e -> {
            game.setCurrentPlayerMadeAction(true);
        });
    }
}