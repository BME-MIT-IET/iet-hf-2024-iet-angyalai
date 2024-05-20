package org.bme.mit.iet;

import org.bme.mit.iet.board.Board;
import org.bme.mit.iet.exceptions.LoadException;
import org.bme.mit.iet.field.*;
import org.bme.mit.iet.player.Player;
import org.bme.mit.iet.player.Plumber;
import org.bme.mit.iet.player.Saboteur;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

public class FileHandler {
    private static final String PIPES = "pipes";
    private static final String PUMPS = "pumps";
    private static final String CURRENT_WATER_LEVEL = "currentWaterLevel";
    private static final String CAPACITY = "capacity";
    private static final String IS_WORKING = "isWorking";
    private static final String ALREADY_MOVED_WATER = "alreadyMovedWater";
    private static final String PLAYERS = "players";
    private static final String INLET = "inlet";
    private static final String OUTLET = "outlet";
    private static final String DESTINATIONS = "dests";
    private static final Logger LOGGER = Logger.getLogger(FileHandler.class.getName());

    public void save(String filename, Game game) {
        JSONObject f = new JSONObject();
        f.put("game", game.toJSON());

        try(FileWriter file = new FileWriter(filename)) {
            file.write(f.toJSONString());
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
    }

    public Game load(String filename) throws LoadException {
        try {
            Object obj = new JSONParser().parse(new FileReader(filename));

            JSONObject jo = (JSONObject) obj;
            JSONObject game = (JSONObject) jo.get("game");
            jo = (JSONObject) game.get("Board");

            //Create fields
            ArrayList<Field> pipes = createFields(jo, PIPES);
            ArrayList<Field> pumps = createFields(jo, PUMPS);
            ArrayList<Field> srcs = createFields(jo, "srcs");
            ArrayList<Field> dests = createFields(jo, DESTINATIONS);

            ArrayList<Field> fields = new ArrayList<>();
            fields.addAll(pipes);
            fields.addAll(pumps);
            fields.addAll(srcs);
            fields.addAll(dests);

            //Create players
            ArrayList<Player> plums = createPlayers(jo, "plums", fields);
            ArrayList<Player> sabs = createPlayers(jo, "sabs", fields);
            ArrayList<Player> players = new ArrayList<>();
            players.addAll(plums);
            players.addAll(sabs);

            //Set missing attributes
            setFieldAttributes(jo, PIPES, pipes, fields, players);
            setFieldAttributes(jo, PUMPS, pumps, fields, players);
            setFieldAttributes(jo, "srcs", srcs, fields, players);
            setFieldAttributes(jo, DESTINATIONS, dests, fields, players);

            Board board = new Board(plums, sabs, pumps, pipes, dests, srcs);
            Game g = Game.getInstance();
            g.loadGame(board, (int) (long) game.get("rounds"), (int) (long) game.get("currentRound"),
                    searchPlayer(players, (int) (long) game.get("currentPlayer")),
                    (boolean) game.get("isCurrentPlayerMadeAction"),
                    (boolean) game.get("isCurrentPlayerMadeMove"),
                    (int) (long) game.get("alreadyPlayed"));
            return g;

        } catch (IOException e) {
            throw new LoadException("Fájl olvasási hiba: " + e.getMessage());
        } catch (ParseException e) {
            throw new LoadException("JSON feldolgozási hiba: " + e.getMessage());
        }
    }

    private ArrayList<Field> createFields(JSONObject jo, String key) {
        JSONArray objects = (JSONArray) jo.get(key);
        Iterator<?> itr1 = objects.iterator();
        ArrayList<Field> objectsB = new ArrayList<>();
        while (itr1.hasNext()) {
            JSONObject object = (JSONObject) itr1.next();
            Field objectB;
            if (key.equals(PIPES)) {
                objectB = new Pipe();
                objectB.setPiercedCounter((int) (long) object.get("piercedCounter"));
                objectB.setSlippery((boolean) object.get("isSlippery"));
                objectB.setSlipperyCounter((int) (long) object.get("slipperyCounter"));
                objectB.setSticky((boolean) object.get("isSticky"));
                objectB.setStickyCounter((int) (long) object.get("stickyCounter"));
            }
            else if (key.equals(PUMPS))
                objectB = new Pump();
            else if (key.equals("srcs"))
                objectB = new Source();
            else
                objectB = new Destination();        

            objectB.setCurrentWaterLevel((int) (long) object.get(CURRENT_WATER_LEVEL));
            objectB.setCapacity((int) (long) object.get(CAPACITY));
            objectB.setWorking((boolean) object.get(IS_WORKING));
            objectB.setAlreadyMovedWater((boolean) object.get(ALREADY_MOVED_WATER));
            objectB.setId((int) (long) object.get("id"));

            objectsB.add(objectB);
        }
        return objectsB;
    }

    private void setFieldAttributes(JSONObject jo, String key, ArrayList<Field> elementsL, ArrayList<Field> fieldsL, ArrayList<Player> playersL) {
        JSONArray objects = (JSONArray) jo.get(key);
        Iterator<?> itr1 = objects.iterator();
        int i = 0;
        String constant = key.equals(PIPES) ? PUMPS : PIPES;

        while (itr1.hasNext()) {
            JSONObject object = (JSONObject) itr1.next();

            setFieldNeighbours(object, constant, elementsL, fieldsL, i);
            setFieldPlayers(object, elementsL, playersL, i);

            if (key.equals(DESTINATIONS)) {
                elementsL.get(i).setPump(searchField(elementsL, (int) (long) object.get("pump")));
                elementsL.get(i).setPipe(searchField(elementsL, (int) (long) object.get("pipe")));
            }
            i++;
        }
    }

    private void setFieldNeighbours(JSONObject object, String constant, ArrayList<Field> elementsL, ArrayList<Field> fieldsL, int index) {
        ArrayList<Field> neighboursB = new ArrayList<>();
        JSONArray neighbours = (JSONArray) object.get(constant);
        Iterator<?> itr2 = neighbours.iterator();
        while (itr2.hasNext()) {
            long neighbour = (long) itr2.next();
            neighboursB.add(searchField(fieldsL, (int) neighbour));
        }
        elementsL.get(index).setNeighbours(neighboursB);
    
        if (constant.equals(PUMPS) || constant.equals("srcs") || constant.equals(DESTINATIONS)) {
            elementsL.get(index).setInOutlet(object.get(INLET) != null ? searchField(fieldsL, (int) (long) object.get(INLET)) : null,
                object.get(OUTLET) != null ? searchField(fieldsL, (int) (long) object.get(OUTLET)) : null);
        }
    }

    private void setFieldPlayers(JSONObject object, ArrayList<Field> elementsL, ArrayList<Player> playersL, int index) {
        ArrayList<Player> playersB = new ArrayList<>();
        JSONArray players = (JSONArray) object.get(PLAYERS);
        Iterator<?> itr2 = players.iterator();
        while (itr2.hasNext()) {
            long player = (long) itr2.next();
            playersB.add(searchPlayer(playersL, (int) player));
        }
        elementsL.get(index).setPlayers(playersB);
    }    
    

    private ArrayList<Player> createPlayers(JSONObject jo, String key, ArrayList<Field> fields) {
        JSONArray objects = (JSONArray) jo.get(key);
        Iterator<?> itr1 = objects.iterator();
        ArrayList<Player> objectsB = new ArrayList<>();
        while (itr1.hasNext()) {
            JSONObject object = (JSONObject) itr1.next();
            Player objectB;

            if (key.equals("plums")) 
                objectB = new Plumber(searchField(fields, (int) (long) object.get("id")));
            else  
                objectB = new Saboteur(searchField(fields, (int) (long) object.get("id")));

            objectB.setId((int) (long) object.get("id"));

            int i = 0;
            JSONArray pipes = (JSONArray) object.get(PIPES);
            Iterator<?> itr2 = pipes.iterator();
            while (itr2.hasNext()) {
                long pipe = (long) itr2.next();
                try {
                    objectB.setPipeInventory(i++, searchField(fields, (int) pipe));
                } catch (Exception e) {
                    //Nem baj ha nincsen cső az invenoryban
                }
            }

            try {
                objectB.addPump(searchField(fields, (int) (long) object.get("pump")));
            } catch (Exception e) {
                //Nem baj ha nincsen pumpa az inventoryban
            }

            objectsB.add(objectB);
        }
        return objectsB;
    }

    private Field searchField(ArrayList<Field> list, int id) {
        for (Field element : list) {
            if (element.getId() == id) {
                return element;
            }
        }
        return null;
    }

    private Player searchPlayer(ArrayList<Player> list, int id) {
        for (Player element : list) {
            if (element.getId() == id) {
                return element;
            }
        }
        return null;
    }
}