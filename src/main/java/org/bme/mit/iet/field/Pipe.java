package org.bme.mit.iet.field;

import org.bme.mit.iet.Game;
import org.bme.mit.iet.player.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * Cso osztaly megvalositja a Mezo os osztalyt. Nyilvan tartja a szomszedos pumpakat, illetve a rajta allo jatekosokat.
 */
public class Pipe extends Field {

    /**
     * A csovel szomszedos pumpak listaja. Min 0 Max 2 pumpaval lehet szomszedos 1 cso.
     */
    private ArrayList<Field> pumps;
    /**
     * Megadja, hogy a cso mennyi ideig nem lyukaszthato meg ki.
     */
    private int piercedCounter;
    /**
     * Megadja, hogy a cso csuszos-e.
     */
    private boolean isSlippery = false;
    /**
     * Megadja, hogy egy cso mennyi ideig csuszos meg.
     */
    private int slipperyCounter;
    /**
     * Megadja, hogy a cso ragado-e.
     */
    private boolean isSticky = false;
    /**
     * Megadja, hogy egy cso mennyi ideig ragados  meg.
     */
    private int stickyCounter;

    private Random random = new Random();

    /**
     * A cso konstruktora. Inicializala a kapcsolodo pumpakat es jatekosok listajat.
     */
    public Pipe() {
        pumps = new ArrayList<Field>();
        players = new ArrayList<Player>();
        this.capacity = 1;
        this.id = idCount++;
    }

    public int getStickyCounter() {
        return stickyCounter;
    }

    /**
     * Beallitja a stickyCounter erteket.
     *
     * @value a beallitani kivant ertek
     */
    @Override
    public void setStickyCounter(int value) {
        stickyCounter = value;
    }

    public boolean isSlippery() {
        return isSlippery;
    }

    @Override
    public void setSlippery(boolean slippery) {
        isSlippery = slippery;
    }

    /**
     * Getter a szomszedos pumpakhoz.
     */
    public ArrayList<Field> getNeighbours() {
        return pumps;
    }

    @Override
    public void setNeighbours(ArrayList<Field> neighbours) {
        pumps = neighbours;
    }

    /**
     * A vizet mozgatja. Ha cso lyukas a viz egybol kifolyik belola. Pontot kapnak a szabotorok.
     */
    @Override
    public void moveWater() {
        if (alreadyMovedWater) {
            return;
        }
        setAlreadyMovedWater(true);
        if (!isWorking || pumps.size() == 1) {
            if (currentWaterLevel > 0) {
                currentWaterLevel = 0;
                Game.getInstance().waterLost();
            }
            return;
        }
        for (Field neighbour : pumps) {
            if (!neighbour.isAlreadyMovedWater()) {
                neighbour.moveWater();
                return;
            }
        }
    }

    /**
     * Visszaadja, hogy a paraméterül kapott játékos ráléphet-e az adott csore:
     * Ha nem all a csovon senki, akkor ralephet, ha all rajta valaki, akkor nem
     * Ha a cso csuszos, akkor a ralepo jatekos veletlenszeruen lecsuszik rola az egyik
     * szomszedos pumpara. Ha valamiert nem tudna a pumpara racsuszni, akkor marad ott ahol van, false-val terunk vissza
     *
     * @param player jatekos amelyik ra szeretne lepni a csore
     * @return visszaadja, hogy melyik mezore lepett a jatekos, null, ha nem lephet oda
     */
    public Field movePlayer(Player player) {
        if (players.size() > 0) throw new IllegalStateException();
        if (isSlippery) {
            Field randomNeighbourPump = pumps.get(random.nextInt(2)).movePlayer(player);
            return randomNeighbourPump;
        }
        this.addPlayer(player);
        return this;
    }

    /**
     * cső raqgadósságának gettere, a field-ben absztraktként szerepel,
     * azért kell, mert a játékos nem tudta lekéreni a mezőtől, hogy a cső, amin éppen áll, az ragad-e
     *
     * @return visszaadja, hogy a cső ragadós-e
     */
    @Override
    public boolean isSticky() {
        return isSticky;
    }

    @Override
    public void setSticky(boolean sticky) {
        isSticky = sticky;
    }

    /**
     * Egy jatekos ezt a csovet probalja felvenni a tablarol. Ha a csovon allnak a csovet nem lehet felvenni.
     * A csoben levo osszes elfolyik. A csovet felveszi a jatekos es bekerul a csoveket tarto mezojebe.
     * Amennyiben a csonek nincs tobb szomszedja (teljesen a jatekos(ok)nal van) szol a tablanak, hogy nem aktiv mezo.
     *
     * @param neighbour A szomszedos pumpa amirol a jatekos fel akarja venni.
     * @param player    A jatekos aki fel akarja venni a csovet
     * @return Ha a csovet fel lehet venni visszater sajat magaval, egyebkent null-val.
     */
    @Override
    public Field pickUpNeighbourField(Field neighbour, Player player) {
        if (!players.isEmpty()) {
            return null;
        }
        if (!player.addPipe(this)) {
            return null;
        }
        if (currentWaterLevel > 0) {
            Game.getInstance().waterLost();
            currentWaterLevel = 0;
        }
        removeField(neighbour);
        if (pumps.isEmpty()) {
            Game.getInstance().getBoard().removePipe(this);
        }
        return this;
    }

    /**
     * A jatekos egy uj pumpat rak le a cso kozepere. Csore nem lehet masik csovet csatlakoztatni. Letrehoz
     * egy uj csovet, hozzacsatlakoztatja az egyik szomszedos pumpahoz az uj csovet. Ha a regi cso be/kimenet volt,
     * beallitja a pumpan az uj csovet be/kimenetkent. Lecsatlakozik a szomszedos pumparol amihez az uj csovet
     * illesztette. Hozzacsatlakoztatja sajat magat es az uj csovet az uj, eredetileg lerakodo pumpahoz.
     *
     * @param newNeighbourPump a letenni kivant pumpa
     * @return mindig igazzal ter vissza mert egy csore minden esetben lehet pumpat tenni.
     */
    @Override
    public boolean placeNewNeighbourField(Field newNeighbourPump) {
        Field oldPump = pumps.get(0);
        Field newPipe = new Pipe();
        oldPump.addField(newPipe);
        Field[] inout = oldPump.getInOutlet();
        if (inout[0] == this) {
            oldPump.setInOutlet(newPipe, inout[1]);
        } else if (inout[1] == this) {
            oldPump.setInOutlet(inout[0], newPipe);
        }
        newPipe.addField(oldPump);
        oldPump.removeField(this);
        pumps.remove(oldPump);
        pumps.add(newNeighbourPump);
        newPipe.addField(newNeighbourPump);
        newNeighbourPump.addField(newPipe);
        newNeighbourPump.addField(this);
        Game.getInstance().getBoard().addPipe(newPipe);
        Game.getInstance().getBoard().addPump(newNeighbourPump);
        return true;
    }

    /**
     * Egy szomszedos mezo lecsatlakoztatasa a csorol
     *
     * @param field a lecsatlakoztatni kivant mezo
     */
    @Override
    public void removeField(Field field) {
        pumps.remove(field);
    }

    /**
     * Uj mezo csatlakoztatasa a csohoz
     *
     * @param field a csatlakoztatni kivant mezo
     */
    @Override
    public void addField(Field field) {
        if (pumps.size() < 2 && !pumps.contains(field)) {
            pumps.add(field);
        }
    }

    /**
     * Ha a csovet ki lehet lyukasztani es meg nem lyukas, akkor beallitja a piercedCounter erteket, es az isWorking erteket false-ra allitja
     */
    public void breakField() {
        if (piercedCounter == 0 && isWorking) {
            setPiercedCounter(random.nextInt(3) + 1);
            super.breakField();
        }
    }

    /**
     * Ha a cso jelenleg nem csuszos, akkor azza valtoztatja egy veletlen hosszusagu ideig.
     */
    @Override
    public void makeSlippery() {
        if (!isSlippery) {
            isSlippery = true;
            setSlipperyCounter(random.nextInt(3) + 1);
        }
    }

    /**
     * Ha a cso jelenleg nem ragados, akkor azza valtoztatja egy veletlen szamu korig.
     *
     * @param isRandom megadja, hogy a cso 1 es 3 kor kozott veletlenszeru korig legyen
     *                 ragados, vagy fixen csak 1 korig legyen ragados (leheto legkisebb ertek)
     */
    @Override
    public void makeSticky() {
        if (!isSticky) {
            isSticky = true;
            setStickyCounter(random.nextInt(3) + 1);
        }
    }

    /**
     * Csonek nem lehet beallitani a be- es kimenetet.
     *
     * @param in  a beallitani kivant bemenet
     * @param out a beallitani kivant kimenet
     */
    @Override
    public void setInOutlet(Field in, Field out) {
        //DOES NOTHING HERE
    }

    /**
     * Nem vegez semmilyen muveletet
     *
     * @return null
     */
    @Override
    public Field[] getInOutlet() {
        return null;
    }

    public ArrayList<Field> getPumps() {
        return pumps;
    }

    /**
     * Beallitja a szomszedos pumpakat.
     */
    public void setPumps(ArrayList<Field> pumps) {
        this.pumps = pumps;
    }

    @Override
    public int getPiercedCounter() {
        return piercedCounter;
    }

    /**
     * Beallitja a piercedCounter erteket
     *
     * @param value veletlen ertek
     */
    @Override
    public void setPiercedCounter(int value) {
        piercedCounter = value;
    }

    public int getSlipperyCounter() {
        return slipperyCounter;
    }

    /**
     * Beallitja a slipperyCounter erteket.
     *
     * @param value veletlen ertek
     */
    @Override
    public void setSlipperyCounter(int value) {
        slipperyCounter = value;
    }

    /**
     * Minden kor vegen csokkenti a slipperyCounter erteket, ha a cso csuszos, a stickyCounter erteket ha a cso ragados, es a piercedCounter erteket ha a cso lyukas
     */
    @Override
    public void resetResources() {
        slipperyCounter = slipperyCounter > 0 ? slipperyCounter - 1 : slipperyCounter;
        if (slipperyCounter == 0) {
            setSlippery(false);
        }
        stickyCounter = stickyCounter > 0 ? stickyCounter - 1 : stickyCounter;
        if (stickyCounter == 0) {
            setSticky(false);
        }
        piercedCounter = piercedCounter > 0 ? piercedCounter - 1 : piercedCounter;

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
        str.append("\t\t\t\"broken\": " + isWorking + "\n");
        str.append("\t\t\t\"pumps\": ");
        for (Field pump : pumps) {
            str.append(pump.getId() + ", ");
        }
        str.deleteCharAt(str.length() - 2);
        str.append("\n");
        str.append("\t\t\t\"waterLevel\": " + currentWaterLevel + "\n");
        str.append("\t\t\t\"capacity\": " + capacity + "\n");
        str.append("\t\t\t\"slippery\": " + isSlippery + "\n");
        str.append("\t\t\t\"sticky\": " + isSticky + "\n");
        str.append("\t\t}");
        return str.toString();
    }

    /**
     * JSON formatumba alakitja a csovet
     *
     * @return JSONObject a cso adattagjaival
     */
    @Override
    public JSONObject toJSON() {
        JSONObject jo = super.toJSON();
        JSONArray pumps = new JSONArray();
        for (Field pump : this.pumps) {
            pumps.add(pump.getId());
        }
        jo.put("pumps", pumps);
        jo.put("piercedCounter", piercedCounter);
        jo.put("isSlippery", isSlippery);
        jo.put("slipperyCounter", slipperyCounter);
        jo.put("isSticky", isSticky);
        jo.put("stickyCounter", stickyCounter);

        return jo;
    }
}
