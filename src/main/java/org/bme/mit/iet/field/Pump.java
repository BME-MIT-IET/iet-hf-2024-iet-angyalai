package org.bme.mit.iet.field;

import org.bme.mit.iet.player.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Pumpa osztaly megvalositja a Mezo os osztaly-t. Nyilvan tartja a szomszedos csoveket es hogy eppen melyik a be es kimenete. Illetve hogy eppen mely jatekosok allnak rajta.
 */
public class Pump extends Field {
    /**
     * A pumpahoz kapcsolod csovek listaja.
     */
    protected ArrayList<Field> pipes;

    /**
     * A pumpa bemeneti csove.
     */
    protected Field inlet;

    /**
     * A pumpa kimeneti csove.
     */
    protected Field outlet;

    /**
     * A pumpa konstruktora. Inicializala a kapcsolodo csovek es jatekosok listajat.
     */
    public Pump() {
        pipes = new ArrayList<Field>();
        players = new ArrayList<Player>();
        this.capacity = 3;
        this.id = idCount++;
    }
    
    /**
     * Visszaadja, hogy a paraméterül kapott játékos ráléphet-e az adott pumpára:
     * Mivel a pumpan barhanyan allhatnak, igy mindig ralephet a jatekos
     *
     * @param player a jatekos amelyik ra szeretne lepni a pumpara
     * @return erteke a pumpa ha ralephetunk, null, ha nem lephetunk a pupara
     */
    public Field movePlayer(Player player) {
        this.addPlayer(player);
        return this;
    }

    /**
     * Getter a pumpa csolistajahoz
     *
     * @return a pumpa csolistaja
     */
    public List<Field> getPipes() {
        return this.pipes;
    }

    /**
     * Setter a pumpa csolistajahoz
     *
     * @param pipes a beallitani kivant csolistaja
     */
    public void setPipes(ArrayList<Field> pipes) {
        this.pipes = pipes;
    }

    /**
     * Beallitja a cso bemeneti csovet
     *
     * @param inlet a beallitani kivant bemeneti cso
     */
    public void setInlet(Pipe inlet) {
        this.inlet = inlet;
    }

    /**
     * Beallitja a cso kimeneti csovet
     *
     * @param outlet a beallitani kivant kimeneti cso
     */
    public void setOutlet(Pipe outlet) {
        this.outlet = outlet;
    }

    /**
     * Beállítja, hogy a pumpa melyik csőből melyik csőbe szivattyúzzon.
     *
     * @param in  a beallitani kivant bemeneti cso
     * @param out a beallitani kivant kimeneti cso
     */
    @Override
    public void setInOutlet(Field in, Field out) {
        if (inlet != null && in == out) {
            throw new IllegalArgumentException();
        }
        this.inlet = in;
        this.outlet = out;
    }

    /**
     * Getter a pumpa be- es kimeneti csovehez
     *
     * @return a be- es kimeneti csobol allo tomb
     */
    @Override
    public Field[] getInOutlet() {
        return new Field[]{inlet, outlet};
    }

    /**
     * Visszaadja a pumpára csatlakoztatott csöveket.
     *
     * @return a pumpára csatlakoztatott csövek
     */
    public ArrayList<Field> getNeighbours() {
        return pipes;
    }

    @Override
    public void setNeighbours(ArrayList<Field> neighbours) {
        pipes = neighbours;
    }

    /**
     * Hozzaadja a szomszed listajahoz a parameterkent kapott objektumot.
     *
     * @param field a hozzaadni kivant mezo
     */
    public void addField(Field field) {
        if (!pipes.contains(field)) {
            pipes.add(field);
        }
    }

    /**
     * Kiveszi a parameterkent kapott mezot a szomszed listajarol
     *
     * @param field a kivennni kivant mezo
     */
    public void removeField(Field field) {
        pipes.remove(field);
    }

    /**
     * Kiveszi a vizet a bementi csobol a tartalyaba, majd a tartalyabol a kimeneti csobe teszi azt. Ha a pumpa
     * nem mukodik nem mozgat vizet. Lyukas csobol nem mozgat vizet, tovabb hivja a MoveWater()-t a kimeneti csovon.
     * A mozgatott viz jelzot igazra allitja
     */
    public void moveWater() {
        if (alreadyMovedWater) {
            return;
        }
        setAlreadyMovedWater(true);
        if (!isWorking) {
            return;
        }
        int amount = 0;
        if (inlet != null) {
            amount = inlet.removeWater(1);
            if (!addWater(amount)) {
                inlet.addWater(amount);
            }
        }
        if (outlet == null) return;
        var success = outlet.addWater(amount);
        if (!success) {
            return;
        }
        removeWater(amount);
        outlet.moveWater();
    }

    /**
     * Lecsatlakoztatunk egy csovet a pumparol. Ha a csovon allnak, nem lehet lecsatlakoztatni, false-szal ter vissza,
     * egyebkent true-val. Ha a csoben volt viz, elfolyik.
     *
     * @param pipeToBePickedUp a lecsatlakoztatni kivant cso
     * @param player           a jatekos aki fel akarja venni a csovet
     * @return a lecsatlakoztatott cso
     */
    @Override
    public Field pickUpNeighbourField(Field pipeToBePickedUp, Player player) {
        Field success = pipeToBePickedUp.pickUpNeighbourField(this, player);
        if (success == null) {
            return null;
        }
        if (pipeToBePickedUp == inlet) {
            inlet = null;
        } else if (pipeToBePickedUp == outlet) {
            outlet = null;
        }
        this.removeField(pipeToBePickedUp);
        return pipeToBePickedUp;
    }

    /**
     * Uj csovet csatlakoztatunk a pumpahoz.
     *
     * @param newNeighbour a csatlakoztatni kivant cso
     * @return Mindig igazzal fog visszaterni, mivel a pumpakhoz minden korulmeny kozott lehet csovet csatlakoztatni.
     */
    @Override
    public boolean placeNewNeighbourField(Field newNeighbour) {
        this.addField(newNeighbour);
        newNeighbour.addField(this);
        return true;
    }

    /**
     * Pumpat nem lehet csuszossa tenni.
     */
    @Override
    public void makeSlippery() {
        //DOES NOTHING HERE
    }

    /**
     * cső raqgadósságának gettere, a field-ben absztraktként szerepel,
     * azért kell, mert a játékos nem tudta lekéreni a mezőtől, hogy a cső amin éppen áll, az ragad-e
     * mivel ez pumpa, nem lehet ragadós, így mindig false-szal tér vissza
     *
     * @return a pumpa sosem ragadós (a forrás és a ciszterna sem)
     */
    public boolean isSticky() {
        return false;
    }

    /**
     * Pumpat nem lehet ragadossa tenni.
     */
    @Override
    public void makeSticky() {
        //DOES NOTHING HERE
    }

    public boolean isSlippery() {
        return false;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("\t\t{\n");
        str.append("\t\t\t\"id\": " + id + "\n");
        str.append("\t\t\t\"players\": ");
        for (Player player : players) {
            str.append(player.getId() + ", ");
        }
        if (players.isEmpty()) {
            str.append("null");
        } else {
            str.deleteCharAt(str.length() - 2);
        }
        str.append("\n");
        str.append("\t\t\t\"inlet\": " + (inlet != null ? inlet.getId() : "null") + "\n");
        str.append("\t\t\t\"outlet\": " + (outlet != null ? outlet.getId() : "null") + "\n");
        str.append("\t\t\t\"working\": " + isWorking + "\n");
        str.append("\t\t\t\"pipes\": ");
        for (Field pipe : pipes) {
            str.append(pipe.getId() + ", ");
        }
        str.deleteCharAt(str.length() - 2);
        str.append("\n");
        str.append("\t\t\t\"waterLevel\": " + currentWaterLevel + "\n");
        str.append("\t\t\t\"capacity\": " + capacity + "\n");
        str.append("\t\t},");
        return str.toString();
    }

    /**
     * JSON formatumba alakitja a pumpat
     *
     * @return JSONObject a pumpa adattagjaival
     */
    @Override
    public JSONObject toJSON() {
        JSONObject jo = super.toJSON();
        JSONArray pipes = new JSONArray();
        for (Field pipe : this.pipes) {
            pipes.add(pipe.getId());
        }
        jo.put("pipes", pipes);
        if (inlet != null) {
            jo.put("inlet", inlet.getId());
        }
        if (outlet != null) {
            jo.put("outlet", outlet.getId());
        }

        return jo;
    }
}
