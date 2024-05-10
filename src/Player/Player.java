package Player;

import Field.Field;
import Field.Pipe;
import Field.Pump;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


/**
 * Jatekeos os osztaly. A jatekban resztvevo jatekosok altalanos tevekenysegeit valositja meg.
 */
public abstract class Player {

    /**
     * Az azonosito novelesehez szukseges statikus valtozo
     */
    protected static int idCount = 0;
    /**
     * A jatekos "taskajaban" levo cso. Egyszerre csak egy cso lehet egy jatekosnal.
     * Egy cso lehet felig egy jateksonal es felig valamihez csatlakoztatva vagy
     * teljesen vagy
     * a cso egyik fele az egyik jatekosnal a masik fele egy masik jatekosnal.
     * Ha a cso teljesen a jatekosnal van akkor az Array mindket reszeben ugyan az a cso van.
     */
    private final Field[] pipes;
    /**
     * A jatekos nyilvan tartja, mikor melyik mezon all eppen.
     */
    protected Field currentField;
    /**
     * A mezo azonositoja
     */
    protected int id;
    /**
     * A jatekosnal levo pumpa. Egy pumpa csak egy jateksonal lehet. A pumpa vagy teljesen a jatekosnal van vagy semmennyire.
     */
    private Field pump;

    /**
     * Jatekos konstruktora. Beallitja hogy melyik a kiindulo mezoje. Inicializalja a "csotartojat".
     *
     * @param startField a jatekos kezdo mezoje
     */
    public Player(Field startField) {
        this.currentField = startField;
        this.pipes = new Pipe[2];
    }

    public static int getIdCount() {
        return idCount;
    }

    public static void setIdCount(int idCount) {
        Player.idCount = idCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter a jatekos jelenlegi helyzetehez.
     *
     * @return a jelenlegi pozicio
     */
    public Field getCurrentField() {
        return currentField;
    }

    public void setCurrentField(Field currentField) {
        this.currentField = currentField;
    }

    /**
     * Getter a jatekos "csotartojahoz".
     *
     * @return a csoveket tartalmazo tomb
     */
    public Field[] getPipes() {
        return this.pipes;
    }

    /**
     * Setter a jatekos "csotartojahoz".
     *
     * @param index hanyadik helyen helyezze el a csovet
     * @param pipe  a "csotartoba" felvenni kivant cso
     */
    public void setPipeInventory(int index, Field pipe) {
        this.pipes[index] = pipe;
    }

    /**
     * Getter a jatekos pumpajahoz.
     *
     * @return a jatekosnal levo pumpa
     */
    public Field getPump() {
        return this.pump;
    }

    //TODO: Ez amúgy nem illegális? :D

    /**
     * Setter a jatekos pumpajahoz.
     *
     * @param pump a beallitani kivant pumpa
     */
    public void setPump(Pump pump) {
        this.pump = pump;
    }

    /**
     * Átállítja a pumpa be- és kimenetét a kapott paraméterek alapján, úgy, hogy az in nevű cső legyen a pumpa bemenete, az out nevű cső pedig a pumpa kimenete.
     *
     * @param in  a pumpa uj bemenete
     * @param out a pumpa uj kimenete
     */
    public void setPumpConnections(Field in, Field out) {
        this.currentField.setInOutlet(in, out);
    }

    /**
     * A játékos karakterének mozgatása a paraméterként kapott mezőre, ha lehetseges es ha a jelenlegi mezoje nem ragados.
     *
     * @param field a mezo ahova a jatekos szeretne lepni
     */
    public void moveTo(Field field) {
        if (!this.currentField.isSticky()) {
            if (!currentField.getNeighbours().contains((field))) {
                return;
            }
            Field movedTo = field.movePlayer(this);
            if (movedTo != null) {
                currentField.removePlayer((this));
                currentField = movedTo;
            }
        }
    }

    /**
     * Mező (pumpa vagy cső) lehelyezése az inventory-ból arra a mezőre, amelyen a játékos karaktere éppen áll.
     *
     * @param fieldToPlace a lehelyezendo mezo
     */
    public void placeField(Field fieldToPlace) {
        if (fieldToPlace != pump && fieldToPlace != pipes[0] && fieldToPlace != pipes[1]) {
            return;
        }
        if (this.currentField.placeNewNeighbourField(fieldToPlace)) {
            if (fieldToPlace == pump) {
                this.pump = null;
            } else if (fieldToPlace == pipes[0]) {
                this.pipes[0] = null;
            } else {
                this.pipes[1] = null;
            }
        }
    }

    /**
     * Pumpa felvétele a pályáról az inventory-ba arról a mezőről, ahol a játékos karaktere éppen áll.
     *
     * @param toBePicked a jatekos jelenlegi tartozkodasi helye
     */
    public void pickUpField(Field toBePicked) {
        this.currentField.pickUpNeighbourField(toBePicked, this);
    }

    /**
     * A jatekosok ki tudjak lyukasztani azt a csovet, amelyiken allnak, meghivva az adott mezon a Break fuggvenyt.
     */
    public void piercePipe() {
        this.currentField.breakField();
    }

    /**
     * Beallitja, hogy melyik cso, es annak melyik vege van a jatekosnal
     *
     * @param pipe a beallitani kivant cso
     * @return sikerult-e beallitani a csovet
     */
    public boolean addPipe(Field pipe) {
        if (pipe.getNeighbours().size() == 1) {
            if (pipes[0] == null && pipes[1] == null) {
                pipes[0] = pipe;
                pipes[1] = pipe;
                return true;
            } else {
                return false;
            }
        }
        if (pipes[0] == null) {
            pipes[0] = pipe;
            return true;
        } else if (pipes[1] == null) {
            pipes[1] = pipe;
            return true;
        }
        return false;
    }

    /**
     * Beallitja, hogy melyik pumpa van a jatekosnal
     *
     * @param pump a beallitani kivant pumpa
     * @return sikerult-e a muvelet
     */
    public boolean addPump(Field pump) {
        if (this.pump != null) {
            return false;
        }
        this.pump = pump;
        return true;
    }

    /**
     * Mind a szabotőrök, mind a szerelők azt a csövet, amin állnak,
     * rövid időre ragadóssá tudják tenni. Aki legközelebb rálép, egy ideig nem tud
     * továbblépni. Továbbhívja annak a csőnek a MakeSticky() függvényét, amelyen áll.
     *
     * @param isRandom megadja, hogy a cso 1 es 3 kor kozott veletlenszeru ideig legyen
     *                 ragados, vagy fixen csak 1 korig legyen ragados (leheto legkisebb ertek)
     */
    public void makePipeSticky() {
        this.currentField.makeSticky();
    }

    /**
     * Absztrakt fuggveny a jatekosk ezen keresztul manipulaljak a mezok belso allapotait.
     * A csovet kilyukasztjak/megfoltozzak vagy Pumpat javitanak.
     */
    public abstract void changeField();

    /**
     * JSON formatumba alakitja a jatekos adattagjait
     *
     * @return JSONObject a jatekos adattagjaival
     */
    public JSONObject toJSON() {
        JSONObject jo = new JSONObject();
        JSONArray pipes = new JSONArray();
        for (Field pipe : this.getPipes()) {
            if (pipe != null) {
                pipes.add(pipe.getId());
            }
        }
        jo.put("pipes", pipes);
        jo.put("currentField", currentField.getId());
        jo.put("id", id);
        if (getPump() != null) {
            jo.put("Pump", getPump().getId());
        }
        return jo;
    }
}
