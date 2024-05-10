package Field;

import Main.Game;
import Player.Player;
import org.json.simple.JSONObject;


/**
 * Ciszterna osztaly, oroklodik a Pumpa osztalybol. A ciszternaka a jatek tabla egyik vegpont tipusa. Nem tud elromlani. Ha vizet kap pontot szerznek a szerelok.
 */
public class Destination extends Pump {

    private Field pump;
    private Field pipe;

    /**
     * Ciszterna osztaly konstruktora. Szulo osztaly konstruktorat hivaj
     */
    public Destination() {
        super();
    }

    /**
     * A vizet mozgatja a szomszedos bemeneti csobol, megvizsgalja hogy a cso nem lyukas illetve hogy van-e benne viz amit eltud "szivni" belole.
     */
    @Override
    public void moveWater() {
        if (alreadyMovedWater) {
            return;
        }
        setAlreadyMovedWater(true);
        if (inlet == null) return;
        int amount = inlet.removeWater(1);
        addWater(amount);
    }

    /**
     * A ciszternaba jutott-e viz
     *
     * @param amount vizmennyiseg
     * @return
     */
    @Override
    public boolean addWater(int amount) {
        if (amount > 0) {
            Game.getInstance().waterGained();
        }
        return true;
    }

    /**
     * A Ciszterna bemenetet lehet megvaltoztatni. A ciszterna kimenetet nem lehet megvaltoztatni.
     *
     * @param in  a beallitani kivant bemeneti cso
     * @param out a beallitani kivant kimeneti cso
     */
    @Override
    public void setInOutlet(Field in, Field out) {
        this.inlet = in;
    }

    /**
     * Minden kor vegen uj pumpat es csovet general a destination.
     */
    @Override
    public void resetResources() {
        if (this.pipe == null) {
            this.pipe = new Pipe();
            pipe.addField(this);
            addField(pipe);
            Game.getInstance().getBoard().addPipe(pipe);
        }
        if (this.pump == null) {
            this.pump = new Pump();
        }
    }

    /**
     * Ellenorzi, hogy nem egy uj csovet/pumpat akar a jatekos felvenni, egyebkent tovabb hivja a szulo fuggvenyet
     *
     * @param field  a felvenni kivant mezo
     * @param player a jatekos amelyik a mezot fel szeretne venni
     * @return egy uj csovet/pumpat akar a jatekos felvenni
     */
    @Override
    public Field pickUpNeighbourField(Field field, Player player) {
        Field res;
        if (field == pipe) {
            if (!player.addPipe(pipe)) {
                return null;
            } else {
                pipe.removeField(this);
                removeField(pipe);
                res = pipe;
                this.pipe = null;
            }
        } else if (field == pump) {
            if (!player.addPump(pump)) {
                return null;
            } else {
                res = pump;
                this.pump = null;
            }
        } else {
            res = super.pickUpNeighbourField(field, player);
        }
        return res;
    }

    /**
     * Getter az azonositohoz
     *
     * @return azonosito
     */
    public int getId() {
        return this.id;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jo = super.toJSON();
        jo.put("pump", pump.getId());
        jo.put("pipe", pipe.getId());

        return jo;
    }

    @Override
    public Field getPump() {
        return pump;
    }

    @Override
    public void setPump(Field pump) {
        this.pump = pump;
    }

    @Override
    public Field getPipe() {
        return pipe;
    }

    @Override
    public void setPipe(Field pipe) {
        this.pipe = pipe;
    }
}
