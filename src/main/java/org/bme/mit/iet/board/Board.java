package org.bme.mit.iet.board;

import org.bme.mit.iet.field.Field;
import org.bme.mit.iet.player.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Tabla osztaly nyilvantartja a mezoket es a jatekeosokat, felel a pumpa hibak generalasaert es a viz folyatasaert.
 */
public class Board {
    /**
     * Számon tartja a pályán szereplő szereloket.
     */
    private final List<Player> plums;
    /**
     * Számon tartja a pályán szereplő szabotoroket.
     */
    private final List<Player> sabs;
    /**
     * Számon tartja a pályán szereplő jatekosokat.
     */
    private final List<Player> players;
    /**
     * Számon tartja a pályán szereplő pumpakat.
     */
    private final List<Field> pumps;
    /**
     * Számon tartja a pályán szereplő csoveket.
     */
    private final List<Field> pipes;
    /**
     * Számon tartja a pályán szereplő ciszternakat.
     */
    private final List<Field> dests;
    /**
     * Számon tartja a pályán szereplő forrasokat.
     */
    private final List<Field> srcs;

    /**
     * Konstruktor, letrehoz egy palyat
     *
     * @param plums pályán szereplő szerelok
     * @param sabs  pályán szereplő szabotorok
     * @param pumps pályán szereplő pumpak
     * @param pipes pályán szereplő csovek
     * @param dests pályán szereplő ciszternak
     * @param srcs  pályán szereplő forrasok
     */
    public Board(List<Player> plums, List<Player> sabs, List<Field> pumps, List<Field> pipes, List<Field> dests, List<Field> srcs) {
        this.plums = new ArrayList<>();
        this.plums.addAll(plums);
        this.sabs = new ArrayList<>();
        this.sabs.addAll(sabs);
        this.pumps = new ArrayList<>();
        this.pumps.addAll(pumps);
        this.pipes = new ArrayList<>();
        this.pipes.addAll(pipes);
        this.srcs = new ArrayList<>();
        this.srcs.addAll(srcs);
        this.dests = new ArrayList<>();
        this.dests.addAll(dests);
        this.players = new ArrayList<>();
        this.players.addAll(plums);
        this.players.addAll(sabs);
    }

    public boolean isBoardSet() {
        return (plums != null && sabs != null && pipes != null && srcs != null && dests != null);
    }

    /**
     * Hozzaad egy csovet a palyahoz
     *
     * @param pipe hozzaadni kivant cso
     */

    public void addPipe(Field pipe) {
        if (!pipes.contains(pipe)) pipes.add(pipe);
    }

    /**
     * Eltavolit egy csovet a palyarol
     *
     * @param pipe az eltavolitani kivant cso
     */
    public void removePipe(Field pipe) {
        pipes.remove(pipe);
    }

    /**
     * Hozzaad egy pumpat a palyahoz
     *
     * @param pump hozzaadni kivant pumpa
     */
    public void addPump(Field pump) {
        if (!pumps.contains(pump)) pumps.add(pump);
    }

    /**
     * Getter a palyan szereplo jatekosokhoz
     *
     * @return jatekosok
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Getter a palya mezoihez
     *
     * @return a palyat alkoto mezok
     */
    public List<Field> getFields() {
        var result = new ArrayList<Field>();
        result.addAll(pipes);
        result.addAll(pumps);
        result.addAll(srcs);
        result.addAll(dests);
        return result;
    }

    /**
     * Getter a palyan szereplo szerelokhoz
     *
     * @return szerelok
     */
    public List<Player> getPlums() {
        return plums;
    }

    /**
     * Getter a palyan szereplo szabotorokhoz
     *
     * @return szabotorok
     */
    public List<Player> getSabs() {
        return sabs;
    }

    /**
     * Getter a palyat alkoto csovekhez
     *
     * @return csovek
     */
    public List<Field> getPipes() {
        return pipes;
    }

    /**
     * Getter a palyat alkoto pumpakhoz
     *
     * @return pumpak
     */
    public List<Field> getPumps() {
        return pumps;
    }

    /**
     * Getter a palyat alkoto ciszternakhoz
     *
     * @return ciszternak
     */
    public List<Field> getDests() {
        return dests;
    }

    /**
     * Getter a palyat alkoto forrasokhoz
     *
     * @return forrasok
     */
    public List<Field> getSrcs() {
        return srcs;
    }

    public Field getFieldById(int id) {
        for (Field f : getFields()) {
            if (f.getId() == id) return f;
        }
        return null;
    }

    public Player getPlayerById(int id) {
        for (Player p : getPlayers()) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    /**
     * JSON formatumba alakitja az palya alkotoelemeit
     *
     * @return JSONObject a palyat tartalmazo elemekkel
     */
    public JSONObject toJSON() {
        JSONObject jo = new JSONObject();

        JSONArray jsonPlums = new JSONArray();
        for (Player plum : plums) {
            jsonPlums.add(plum.toJSON());
        }
        jo.put("plums", jsonPlums);

        JSONArray jsonSabs = new JSONArray();
        for (Player sab : sabs) {
            jsonSabs.add(sab.toJSON());
        }
        jo.put("sabs", jsonSabs);

        JSONArray jsonPumps = new JSONArray();
        for (Field pump : pumps) {
            jsonPumps.add(pump.toJSON());
        }
        jo.put("pumps", jsonPumps);

        JSONArray jsonPipes = new JSONArray();
        for (Field pipe : pipes) {
            jsonPipes.add(pipe.toJSON());
        }
        jo.put("pipes", jsonPipes);

        JSONArray jsonDests = new JSONArray();
        for (Field dest : dests) {
            jsonDests.add(dest.toJSON());
        }
        jo.put("dests", jsonDests);

        JSONArray jsonSrcs = new JSONArray();
        for (Field src : srcs) {
            jsonSrcs.add(src.toJSON());
        }
        jo.put("srcs", jsonSrcs);

        return jo;
    }

}
