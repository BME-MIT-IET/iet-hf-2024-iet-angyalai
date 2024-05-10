package Field;

import Player.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Mezo os osztaly. Nyilvan tartja a szomszedjait es hogy eppen mely jatekosok allnak rajta.
 */
public abstract class Field {
    /**
     * Az azonosito novelesehez szukseges statikus valtozo
     */
    protected static int idCount = 0;
    /**
     * A mezon tartozkodo jatekosok listaja
     */
    protected ArrayList<Player> players;
    /**
     * A mezo tartalyaban tarolt viz mindenkori mennyisege
     */
    protected int currentWaterLevel = 0;
    /**
     * A mezo tartalyanak maximalis kapacitasa
     */
    protected int capacity;
    /**
     * Ha hamis, akkor a mezo nem kepes vizet atereszteni magan
     */
    protected boolean isWorking = true;
    /**
     * Megadja, hogy az elem mozgatott-e mar vizet az adott korben
     */
    protected boolean alreadyMovedWater = false;
    /**
     * A mezo azonositoja
     */
    protected int id;

    public static int getIdCount() {
        return idCount;
    }

    public static void setIdCount(int idCount) {
        Field.idCount = idCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Getter a mezon tartozkodo jatekosokhoz
     *
     * @return mezon tartozkodo jatekosok listaja
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Beallitja a mezon elhelyezkedo jatekosokat.
     *
     * @param players a mezon elhelyezendo jatekosok listaja
     */
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    /**
     * Getter a mezo tartalyaban tarolt vizmennyiseghez
     *
     * @return mezo tartalyaban tarolt vizmennyiseg
     */
    public int getCurrentWaterLevel() {
        return currentWaterLevel;
    }

    public void setCurrentWaterLevel(int currentWaterLevel) {
        this.currentWaterLevel = currentWaterLevel;
    }

    /**
     * Getter az alreadyMovedWater valtozohoz
     *
     * @return az elem mozgatott-e mar vizet az adott korben
     */
    public boolean isAlreadyMovedWater() {
        return alreadyMovedWater;
    }

    /**
     * Setter az alreadyMovedWater valtozohoz
     *
     * @param alreadyMovedWater mozgatott e mar vizet az adott korben
     */
    public void setAlreadyMovedWater(boolean alreadyMovedWater) {
        this.alreadyMovedWater = alreadyMovedWater;
    }

    /**
     * Beállítja, hogy  az adott játékos az aktív elemen áll.
     *
     * @param player a játékos, amelyik rálépett a mezőre
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Eltávolítja a paraméterül kapott játékost az adott aktív elemről.
     *
     * @param player eltavolitandó játékos
     */
    public void removePlayer(Player player) {
        players.remove(player);
    }

    /**
     * Noveli a mezo jelenlegi vizszintjet ha van meg hely a tarban
     *
     * @param amount az ertek amivel a vizszintet noveljuk
     * @return bele tudta-e tolteni a vizet a tartalyba
     */
    public boolean addWater(int amount) {
        if (currentWaterLevel + amount > capacity) {
            return false;
        }
        currentWaterLevel += amount;
        return true;
    }

    /**
     * Megprobalja csokkenteni a viz szintjet
     *
     * @param amountToRemove a mennyiseg amivel a vizszintet csokkenteni szeretnenk
     * @return a sikeresen csokkentett viz mennyisege
     */
    public int removeWater(int amountToRemove) {
        int res = 0;
        if (amountToRemove >= currentWaterLevel) {
            res = currentWaterLevel;
            currentWaterLevel = 0;
        } else {
            res = amountToRemove;
            currentWaterLevel -= amountToRemove;
        }
        return res;
    }

    /**
     * Megjavitja az adott mezot, ha az el volt romolva.
     */
    public void repairField() {
        if (!isWorking) isWorking = true;
    }

    /**
     * Elrontja az adott mezot, ha eddig mukodott.
     */
    public void breakField() {
        if (isWorking) isWorking = false;
    }

    public abstract Field movePlayer(Player player);

    public abstract ArrayList<Field> getNeighbours();

    public abstract void setNeighbours(ArrayList<Field> neighbours);

    public abstract void addField(Field field);

    public abstract void removeField(Field field);

    public abstract void moveWater();

    public abstract Field pickUpNeighbourField(Field neighbour, Player player);

    public abstract boolean placeNewNeighbourField(Field neighbour);

    public abstract void setInOutlet(Field in, Field out);

    public abstract Field[] getInOutlet();

    public Field getPump() {
        return null;
    }

    public void setPump(Field pump) {
    }

    public Field getPipe() {
        return null;
    }

    public void setPipe(Field pipe) {
    }

    public void setSlipperyCounter(int randomValue) {
    }

    public void setStickyCounter(int value) {
    }

    public abstract void makeSlippery();

    public abstract boolean isSticky();

    public void setSticky(boolean sticky) {
    }

    public abstract boolean isSlippery();

    public void setSlippery(boolean slippery) {
    }

    public abstract void makeSticky();

    public void resetResources() {
    }

    public int getPiercedCounter() {
        return -1;
    }

    public void setPiercedCounter(int randomValue) {
    }

    /**
     * JSON formatumba alakitja a mezot
     *
     * @return JSONObject a mezo adattagjaival
     */
    public JSONObject toJSON() {
        JSONObject jo = new JSONObject();
        JSONArray players = new JSONArray();
        for (Player player : this.players) {
            players.add(player.getId());
        }
        jo.put("players", players);
        jo.put("currentWaterLevel", currentWaterLevel);
        jo.put("capacity", capacity);
        jo.put("isWorking", isWorking);
        jo.put("alreadyMovedWater", alreadyMovedWater);
        jo.put("id", id);

        return jo;
    }
}
