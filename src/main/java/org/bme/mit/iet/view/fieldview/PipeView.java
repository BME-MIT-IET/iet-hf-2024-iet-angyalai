package org.bme.mit.iet.view.fieldview;

import org.bme.mit.iet.Game;
import org.bme.mit.iet.field.Field;
import org.bme.mit.iet.view.ActionButtonGroup;
import org.bme.mit.iet.view.GameView;
import org.bme.mit.iet.view.button.*;

import java.awt.*;
import java.io.Serializable;

/**
 * A csövek megjelenítésért felelős osztály
 */
public class PipeView implements Serializable {
    private final ActionButtonGroup btnGroup;
    private final GameView gameView;
    private final Field pipe;
    private Point startPoint, endPoint;

    /**
     * A csövek megjelenítésért felelős osztály konstruktora
     *
     * @param gameView a pipeviewet tartalmazó gameView
     * @param btnGroup az alsó buttongroup melyben a gombok jelennek meg
     * @param pipe     a cső maga, melyhez a pipeView tartozik
     */
    public PipeView(GameView gameView, ActionButtonGroup btnGroup, Field pipe) {
        this.gameView = gameView;
        this.btnGroup = btnGroup;
        this.pipe = pipe;
        var currentField = Game.getInstance().getCurrentPlayer().getCurrentField();
        if (currentField == pipe) {
            btnGroup.clearButtons();
            fieldIsCurrentField();
            btnGroup.updateButtonGroup(gameView);
        }
    }

    /**
     * Ez a függvény akkor hívódik, amikor a csőre kattintanak és következményképpen
     * megváltoztatja az alsó buttongroupban megjelenített gombokat az alapján,
     * hoyg szomszédos cső, vagy a játékos jelenleg rajta áll-e
     */
    public void pipeClicked() {
        var currentField = Game.getInstance().getCurrentPlayer().getCurrentField();
        btnGroup.clearButtons();
        if (currentField != pipe && !currentField.getNeighbours().contains(pipe)) {
        } else if (currentField == pipe) {
            fieldIsCurrentField();
        } else {
            btnGroup.add(new MoveBtn(gameView, pipe));
            btnGroup.add(new PickUpFieldBtn(gameView, pipe));
            btnGroup.add(new SetInletBtn(gameView, pipe));
            btnGroup.add(new SetOutletBtn(gameView, pipe));
        }
        btnGroup.updateButtonGroup(gameView);
    }

    /**
     * Ha ezen a mezőn áll a játékos, akkor a függvény az alábbi gombokat adja hozzá az alsó buttongrouphoz
     */
    public void fieldIsCurrentField() {
        btnGroup.add(new RepairBtn(gameView));
        btnGroup.add(new PierceBtn(gameView));
        btnGroup.add(new PlaceFieldBtn(gameView, pipe));
        btnGroup.add(new SlipperyBtn(gameView));
        btnGroup.add(new StickyBtn(gameView));
    }

    /**
     * Visszaadja a cső kezdőpontját
     *
     * @return a cső kezdőpontját
     */
    public Point getStartPoint() {
        return startPoint;
    }

    /**
     * Beállítja a cső kezdőpontját
     *
     * @param sp a cső kezdőpontja
     */
    public void setStartPoint(Point sp) {
        this.startPoint = sp;
    }

    /**
     * Visszaadja a cső végpontját
     *
     * @return a cső végpontja
     */
    public Point getEndPoint() {
        return endPoint;
    }

    /**
     * Beállítja a cső végpontját
     *
     * @param ep a cső végpontja
     */
    public void setEndPoint(Point ep) {
        this.endPoint = ep;
    }

    /**
     * Visszadja a csövet magát, mint mező
     *
     * @return a PipeView-hez tartozó cső
     */
    public Field getField() {
        return pipe;
    }
}
