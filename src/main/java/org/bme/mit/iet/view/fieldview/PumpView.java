package org.bme.mit.iet.view.fieldview;

import org.bme.mit.iet.field.Field;
import org.bme.mit.iet.view.ActionButtonGroup;
import org.bme.mit.iet.view.GameView;
import org.bme.mit.iet.view.button.MoveBtn;
import org.bme.mit.iet.view.button.PlaceFieldBtn;
import org.bme.mit.iet.view.button.RepairBtn;

/**
 * A pumpák megjelenítésért felelős osztály
 */
public class PumpView extends PumpLikeFieldView {
    /**
     * A pumpák megjelenítésért felelős osztály konstruktora
     *
     * @param gameView a PumpLikeFieldViewet tartalmazó gameView
     * @param btnGroup az alsó buttongroup melyben a gombok jelennek meg
     * @param field    a mező maga, melyhez a view tartozik
     * @param fileName a mező meve
     */
    public PumpView(GameView gameView, ActionButtonGroup btnGroup, Field field, String fileName) {
        super(gameView, btnGroup, field, fileName);
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
     * az ősosztály fieldIsCurrentField függvényének override-olása, mely hozzáadja a
     * javítás gombot és a mezőlehejezés gombot
     * az alsó buttongrouphoz akkor, ha ez a mező jelenlegi mezőként aktív, azaz a játékos jelenleg rajta áll
     */
    @Override
    public void fieldIsCurrentField() {
        btnGroup.addButton(new RepairBtn(gameView));
        btnGroup.addButton(new PlaceFieldBtn(gameView, field));
    }
}