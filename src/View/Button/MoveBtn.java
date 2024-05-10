package View.Button;

import Field.Field;
import Main.Game;
import View.GameView;

/**
 * A mozgás gombot megvalósító osztály.
 */
public class MoveBtn extends ActionButton {
    /**
     * A mozgás gombot megvalósító osztály konstruktora, amely továbbhívja az ősosztály konstruktorát,
     * majd a játék állapota, a jelenlegi játékos és a jelenlegi mező alapján megállapítja,
     * hogy a gombot meg kell-e jeleníteni vagy sem a képernyő alján található buttongroupban.
     * Végül az addActionListener hozzáadásával megadja, hogy a gombra kattintáskor mi történjen.
     *
     * @param gameView a gombot tartalmazó gameView
     * @param field    a mező, amelyre a tevékenység irányul
     */
    public MoveBtn(GameView gameView, Field field) {
        super(gameView, "move");
        setVisible(true);
        var game = Game.getInstance();
        if (game.getBoard().getPipes().contains(field)) {
            //ha csőre kattintottunk
            if (!field.getPlayers().isEmpty()) {
                //ha van valaki a csövön
                setVisible(false);
            }
            if (field.getNeighbours().size() < 2) {
                //ha a cső egyik fele szabadon van
                setVisible(false);
            }
        } else {
            //ha pumpára, ciszternára vagy forrásra kattintottunk, azaz csövön állunk
            if (game.getCurrentPlayer().getCurrentField().isSticky()) {
                //ha a cső amin áll ragadós
                setVisible(false);
            }
        }
        if (game.isCurrentPlayerMadeMove()) {
            setVisible(false);
        }


        addActionListener(e -> {
            game.getCurrentPlayer().moveTo(field);
            game.setCurrentPlayerMadeMove(true);
        });
    }
}
