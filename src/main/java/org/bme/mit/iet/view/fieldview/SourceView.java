package org.bme.mit.iet.view.fieldview;

import org.bme.mit.iet.field.Field;
import org.bme.mit.iet.view.ActionButtonGroup;
import org.bme.mit.iet.view.GameView;
import org.bme.mit.iet.view.button.MoveBtn;
import org.bme.mit.iet.view.button.PlaceFieldBtn;

/**
 * A források megjelenítésért felelős osztály
 */
public class SourceView extends PumpLikeFieldView {
    /**
     * A források megjelenítésért felelős osztály konstruktora, továbbhívja az ősosztálya
     * konstruktorát, amelyben átadja önmagát és a fájlnenet,
     * amely a betöltendő kép nevét tartalmazza
     *
     * @param gameView a destViewet tartalmazó gameView
     * @param btnGroup az alsó buttongroup melyben a gombok jelennek meg
     * @param dest     a ciszterna maga, melyhez a destView tartozik
     */
    public SourceView(GameView gameView, ActionButtonGroup btnGroup, Field dest) {
        super(gameView, btnGroup, dest, "source");
    }

    /**
     * az ősosztály fieldIsNeighbour függvényének override-olása, mely hozzáadja a mozgásgombot
     * az alsó buttongrouphoz akkor, ha ez a mező szomszéd mezőként aktív, azaz mellete lévő mezőről
     * kattintottak rá
     */
    @Override
    public void fieldIsNeighbour() {
        btnGroup.addButton(new MoveBtn(gameView, field));
    }

    /**
     * az ősosztály fieldIsCurrentField függvényének override-olása, mely hozzáadja a mezőlehejezés gombot
     * az alsó buttongrouphoz akkor, ha ez a mező jelenlegi mezőként aktív, azaz a játékos jelenleg rajta áll
     */
    @Override
    public void fieldIsCurrentField() {
        btnGroup.addButton(new PlaceFieldBtn(gameView, field));
    }
}