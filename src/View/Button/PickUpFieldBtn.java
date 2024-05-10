package View.Button;

import Field.Field;
import Main.Game;
import View.GameView;

/**
 * A mezőfelvétel gombot megvalósító osztály.
 */
public class PickUpFieldBtn extends ActionButton {
    /**
     * A mezőfelvétel gombot megvalósító osztály konstruktora, amely továbbhívja az ősosztály konstruktorát,
     * majd a játék állapota, a jelenlegi játékos és a jelenlegi mező alapján megállapítja,
     * hogy a gombot meg kell-e jeleníteni vagy sem a képernyő alján található buttongroupban.
     * Végül az addActionListener hozzáadásával megadja, hogy a gombra kattintáskor mi történjen.
     *
     * @param gameView a gombot tartalmazó gameView
     * @param field    a mező, amelyre a tevékenység irányul
     */
    public PickUpFieldBtn(GameView gameView, Field field) {

        super(gameView, "pick-up-field");
        var game = Game.getInstance();
        if (game.getBoard().getPipes().contains(field)) {
            //field is pipe
            if (!(game.getCurrentPlayer().getPipes()[0] == null || game.getCurrentPlayer().getPipes()[1] != null)) {
                setVisible(false);
            }
        } else {
            //field is pump ==> cant pick up a pump
            setVisible(false);
        }

        addActionListener(e -> {
            game.getCurrentPlayer().pickUpField(field);
            game.setCurrentPlayerMadeAction(true);
        });
    }
}