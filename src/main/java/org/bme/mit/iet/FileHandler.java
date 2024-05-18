package org.bme.mit.iet;

import org.bme.mit.iet.board.Board;
import org.bme.mit.iet.exceptions.LoadException;
import org.bme.mit.iet.field.*;
import org.bme.mit.iet.player.Player;
import org.bme.mit.iet.player.Plumber;
import org.bme.mit.iet.player.Saboteur;
import org.bme.mit.iet.view.GUI;
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

   /* public void save(String filename, Board board) {

        JSONObject f = new JSONObject();
        JSONArray pipes = new JSONArray();
        for (Field element : board.getPipes()) {
            JSONObject pipe = new JSONObject();
            pipe.put("ID", element.getId());
            pipe.put("CurrentWaterLevel", element.getCurrentWaterLevel());
            pipe.put("Capacity", element.getCapacity());
            pipe.put("IsWorking", element.isWorking());
            pipe.put("IsSticky", element.isSticky());
            pipe.put("IsSlippery", element.isSlippery());
            JSONArray neighbours = new JSONArray();
            for (Field neighbour : element.getNeighbours()) {
                neighbours.add(neighbour.getId());
            }
            pipe.put("Neighbours", neighbours);

            JSONArray players = new JSONArray();
            for (Player player : element.getPlayers()) {
                if (player != null) {
                    players.add(player.getId());
                }
            }
            pipe.put("Players", players);

            pipes.add(pipe);
        }
        f.put("Pipes", pipes);

        JSONArray pumps = new JSONArray();
        for (Field element : board.getPumps()) {
            JSONObject pump = new JSONObject();
            pump.put("ID", element.getId());
            pump.put("CurrentWaterLevel", element.getCurrentWaterLevel());
            pump.put("Capacity", element.getCapacity());
            pump.put("IsWorking", element.isWorking());
            JSONArray neighbours = new JSONArray();
            for (Field neighbour : element.getNeighbours()) {
                neighbours.add(neighbour.getId());
            }
            pump.put("Neighbours", neighbours);

            JSONArray players = new JSONArray();
            for (Player player : element.getPlayers()) {
                if (player != null) {
                    players.add(player.getId());
                }
            }
            pump.put("Players", players);

            pump.put("Inlet", (element.getInOutlet()[0]).getId());
            pump.put("Outlet", (element.getInOutlet()[1]).getId());

            pumps.add(pump);
        }
        f.put("Pumps", pumps);


        JSONArray sources = new JSONArray();
        for (Field element : board.getSrcs()) {
            JSONObject source = new JSONObject();
            source.put("ID", element.getId());
            source.put("CurrentWaterLevel", element.getCurrentWaterLevel());
            source.put("Capacity", element.getCapacity());
            source.put("IsWorking", element.isWorking());
            JSONArray neighbours = new JSONArray();
            for (Field neighbour : element.getNeighbours()) {
                neighbours.add(neighbour.getId());
            }
            source.put("Neighbours", neighbours);

            JSONArray players = new JSONArray();
            for (Player player : element.getPlayers()) {
                players.add(player.getId());
            }
            source.put("Players", players);
            source.put("Outlet", (element.getInOutlet()[1]).getId());

            sources.add(source);
        }
        f.put("Sources", sources);

        JSONArray dests = new JSONArray();
        for (Field element : board.getDests()) {
            JSONObject dest = new JSONObject();
            dest.put("ID", element.getId());
            dest.put("CurrentWaterLevel", element.getCurrentWaterLevel());
            dest.put("Capacity", element.getCapacity());
            dest.put("IsWorking", element.isWorking());
            dest.put("Inlet", (element.getInOutlet()[0]).getId());

            JSONArray players = new JSONArray();
            for (Player player : element.getPlayers()) {
                players.add(player.getId());
            }
            dest.put("Players", players);

            dests.add(dest);
        }
        f.put("Destinations", dests);

        JSONArray players = new JSONArray();
        for (Player element : board.getPlayers()) {
            JSONObject player = new JSONObject();
            player.put("ID", element.getId());
            JSONArray pipe_inventory = new JSONArray();
            for (Field pipe : element.getPipes()) {
                if (pipe != null) pipe_inventory.add(pipe.getId());
            }
            player.put("Pipes", pipe_inventory);
            if (element.getPump() != null) player.put("Pump", element.getPump().getId());
            player.put("CurrentField", element.getCurrentField().getId());

            players.add(player);
        }
        f.put("Players", players);


        try {
            FileWriter file = new FileWriter(filename);
            file.write(f.toJSONString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("JSON file created: " + f);
    }*/

    /*public void save_map(String filename, Board board) {

        JSONObject f = new JSONObject();
        JSONArray pipes = new JSONArray();
        for (Field element : board.getPipes()) {
            JSONObject pipe = new JSONObject();
            pipe.put("ID", element.getId());
            pipe.put("CurrentWaterLevel", element.getCurrentWaterLevel());
            pipe.put("Capacity", element.getCapacity());
            pipe.put("IsWorking", element.isWorking());
            pipe.put("IsSticky", element.isSticky());
            pipe.put("IsSlippery", element.isSlippery());
            JSONArray neighbours = new JSONArray();
            for (Field neighbour : element.getNeighbours()) {
                neighbours.add(neighbour.getId());
            }
            pipe.put("Neighbours", neighbours);

            JSONArray players = new JSONArray();
            for (Player player : element.getPlayers()) {
                players.add(player.getId());
            }
            pipe.put("Players", players);

            pipes.add(pipe);
        }
        f.put("Pipes", pipes);

        JSONArray pumps = new JSONArray();
        for (Field element : board.getPumps()) {
            JSONObject pump = new JSONObject();
            pump.put("ID", element.getId());
            pump.put("CurrentWaterLevel", element.getCurrentWaterLevel());
            pump.put("Capacity", element.getCapacity());
            pump.put("IsWorking", element.isWorking());
            JSONArray neighbours = new JSONArray();
            for (Field neighbour : element.getNeighbours()) {
                neighbours.add(neighbour.getId());
            }
            pump.put("Neighbours", neighbours);

            JSONArray players = new JSONArray();
            for (Player player : element.getPlayers()) {
                players.add(player.getId());
            }
            pump.put("Players", players);

            //TODO: Ezzel valamit kezdeni mert ez így nagyon csúnya
            pump.put("Inlet", element.getInOutlet()[0].getId());
            pump.put("Outlet", element.getInOutlet()[1].getId());

            pumps.add(pump);
        }
        f.put("Pumps", pumps);


        JSONArray sources = new JSONArray();
        for (Field element : board.getSrcs()) {
            JSONObject source = new JSONObject();
            source.put("ID", element.getId());
            source.put("CurrentWaterLevel", element.getCurrentWaterLevel());
            source.put("Capacity", element.getCapacity());
            source.put("IsWorking", element.isWorking());
            JSONArray neighbours = new JSONArray();
            for (Field neighbour : element.getNeighbours()) {
                neighbours.add(neighbour.getId());
            }
            source.put("Neighbours", neighbours);

            JSONArray players = new JSONArray();
            for (Player player : element.getPlayers()) {
                players.add(player.getId());
            }
            source.put("Players", players);
            source.put("Outlet", element.getInOutlet()[1].getId());

            sources.add(source);
        }
        f.put("Sources", sources);

        JSONArray dests = new JSONArray();
        for (Field element : board.getDests()) {
            JSONObject dest = new JSONObject();
            dest.put("ID", element.getId());
            dest.put("CurrentWaterLevel", element.getCurrentWaterLevel());
            dest.put("Capacity", element.getCapacity());
            dest.put("IsWorking", element.isWorking());
            dest.put("Inlet", element.getInOutlet()[0].getId());

            JSONArray players = new JSONArray();
            for (Player player : element.getPlayers()) {
                players.add(player.getId());
            }
            dest.put("Players", players);

            dests.add(dest);
        }
        f.put("Destinations", dests);


        try {
            FileWriter file = new FileWriter(filename);
            file.write(f.toJSONString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("JSON file created: " + f);
    }*/

    public Game load(String filename) throws LoadException {
        try {
            Object obj = new JSONParser().parse(new FileReader(filename));

            JSONObject jo = (JSONObject) obj;
            JSONObject game = (JSONObject) jo.get("game");
            jo = (JSONObject) game.get("Board");

            //Create fields
            ArrayList<Field> pipes = createPipes(jo, PIPES);
            ArrayList<Field> pumps = createPumps(jo, PUMPS);
            ArrayList<Field> srcs = createSources(jo, "srcs");
            ArrayList<Field> dests = createDestinations(jo, "dests");

            ArrayList<Field> fields = new ArrayList<>();
            fields.addAll(pipes);
            fields.addAll(pumps);
            fields.addAll(srcs);
            fields.addAll(dests);

            //Create players
            ArrayList<Player> plums = createPlumbers(jo, "plums", fields);
            ArrayList<Player> sabs = createSaboteurs(jo, "sabs", fields);
            ArrayList<Player> players = new ArrayList<>();
            players.addAll(plums);
            players.addAll(sabs);

            //Set missing attributes
            setPipeAttributes(jo, PIPES, pipes, fields, players);
            setPumpAttributes(jo, PUMPS, pumps, fields, players);
            setSourceAttributes(jo, "srcs", srcs, fields, players);
            setDestinationAttributes(jo, "dests", dests, fields, players);

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

    private ArrayList<Field> createPipes(JSONObject jo, String key) {
        JSONArray objects = (JSONArray) jo.get(key);
        Iterator<?> itr1 = objects.iterator();
        ArrayList<Field> objects_b = new ArrayList<>();
        while (itr1.hasNext()) {
            JSONObject object = (JSONObject) itr1.next();
            Field object_b = new Pipe();
            //Field
            object_b.setCurrentWaterLevel((int) (long) object.get(CURRENT_WATER_LEVEL));
            object_b.setCapacity((int) (long) object.get(CAPACITY));
            object_b.setWorking((boolean) object.get(IS_WORKING));
            object_b.setAlreadyMovedWater((boolean) object.get(ALREADY_MOVED_WATER));
            object_b.setId((int) (long) object.get("id"));
            //Pipe specific
            object_b.setPiercedCounter((int) (long) object.get("piercedCounter"));
            object_b.setSlippery((boolean) object.get("isSlippery"));
            object_b.setSlipperyCounter((int) (long) object.get("slipperyCounter"));
            object_b.setSticky((boolean) object.get("isSticky"));
            object_b.setStickyCounter((int) (long) object.get("stickyCounter"));

            objects_b.add(object_b);
        }
        return objects_b;
    }

    private void setPipeAttributes(JSONObject jo, String key, ArrayList<Field> pipes_l, ArrayList<Field> fields_l, ArrayList<Player> players_l) {
        JSONArray objects = (JSONArray) jo.get(key);
        Iterator<?> itr1 = objects.iterator();
        int i = 0;
        while (itr1.hasNext()) {
            JSONObject object = (JSONObject) itr1.next();

            ArrayList<Field> neighbours_b = new ArrayList<>();
            JSONArray neighbours = (JSONArray) object.get(PUMPS);
            Iterator<?> itr2 = neighbours.iterator();
            while (itr2.hasNext()) {
                long neighbour = (long) itr2.next();
                neighbours_b.add(searchField(fields_l, (int) neighbour));
            }
            pipes_l.get(i).setNeighbours(neighbours_b);

            ArrayList<Player> players_b = new ArrayList<>();
            JSONArray players = (JSONArray) object.get(PLAYERS);
            itr2 = players.iterator();
            while (itr2.hasNext()) {
                long player = (long) itr2.next();
                players_b.add(searchPlayer(players_l, (int) player));
            }
            pipes_l.get(i).setPlayers(players_b);

            i++;
        }
    }


    private ArrayList<Field> createPumps(JSONObject jo, String key) {
        JSONArray objects = (JSONArray) jo.get(key);
        Iterator<?> itr1 = objects.iterator();
        ArrayList<Field> objects_b = new ArrayList<>();
        while (itr1.hasNext()) {
            JSONObject object = (JSONObject) itr1.next();
            Field object_b = new Pump();
            //Field
            object_b.setCurrentWaterLevel((int) (long) object.get(CURRENT_WATER_LEVEL));
            object_b.setCapacity((int) (long) object.get(CAPACITY));
            object_b.setWorking((boolean) object.get(IS_WORKING));
            object_b.setAlreadyMovedWater((boolean) object.get(ALREADY_MOVED_WATER));
            object_b.setId((int) (long) object.get("id"));
            //Pump specific

            objects_b.add(object_b);
        }
        return objects_b;
    }

    private void setPumpAttributes(JSONObject jo, String key, ArrayList<Field> pumps_l, ArrayList<Field> fields_l, ArrayList<Player> players_l) {
        JSONArray objects = (JSONArray) jo.get(key);
        Iterator<?> itr1 = objects.iterator();
        int i = 0;
        while (itr1.hasNext()) {
            JSONObject object = (JSONObject) itr1.next();

            ArrayList<Field> neighbours_b = new ArrayList<>();
            JSONArray neighbours = (JSONArray) object.get(PIPES);
            Iterator<?> itr2 = neighbours.iterator();
            while (itr2.hasNext()) {
                long neighbour = (long) itr2.next();
                neighbours_b.add(searchField(fields_l, (int) neighbour));
            }
            pumps_l.get(i).setNeighbours(neighbours_b);

            pumps_l.get(i).setInOutlet(object.get(INLET) != null ? searchField(fields_l, (int) (long) object.get(INLET)) : null,
                    object.get(OUTLET) != null ? searchField(fields_l, (int) (long) object.get(OUTLET)) : null);

            ArrayList<Player> players_b = new ArrayList<Player>();
            JSONArray players = (JSONArray) object.get(PLAYERS);
            itr2 = players.iterator();
            while (itr2.hasNext()) {
                long player = (long) itr2.next();
                players_b.add(searchPlayer(players_l, (int) player));
            }
            pumps_l.get(i).setPlayers(players_b);

            i++;
        }
    }

    private ArrayList<Field> createSources(JSONObject jo, String key) {
        JSONArray objects = (JSONArray) jo.get(key);
        Iterator<?> itr1 = objects.iterator();
        ArrayList<Field> objects_b = new ArrayList<>();
        while (itr1.hasNext()) {
            JSONObject object = (JSONObject) itr1.next();
            Field object_b = new Source();
            //Field
            object_b.setCurrentWaterLevel((int) (long) object.get(CURRENT_WATER_LEVEL));
            object_b.setCapacity((int) (long) object.get(CAPACITY));
            object_b.setWorking((boolean) object.get(IS_WORKING));
            object_b.setAlreadyMovedWater((boolean) object.get(ALREADY_MOVED_WATER));
            object_b.setId((int) (long) object.get("id"));
            //Pump specific

            objects_b.add(object_b);
        }
        return objects_b;
    }

    private void setSourceAttributes(JSONObject jo, String key, ArrayList<Field> pumps_l, ArrayList<Field> fields_l, ArrayList<Player> players_l) {
        setPumpAttributes(jo, key, pumps_l, fields_l, players_l);
    }

    private ArrayList<Field> createDestinations(JSONObject jo, String key) {
        JSONArray objects = (JSONArray) jo.get(key);
        Iterator<?> itr1 = objects.iterator();
        ArrayList<Field> objects_b = new ArrayList<>();
        while (itr1.hasNext()) {
            JSONObject object = (JSONObject) itr1.next();
            Field object_b = new Destination();
            //Field
            object_b.setCurrentWaterLevel((int) (long) object.get(CURRENT_WATER_LEVEL));
            object_b.setCapacity((int) (long) object.get(CAPACITY));
            object_b.setWorking((boolean) object.get(IS_WORKING));
            object_b.setAlreadyMovedWater((boolean) object.get(ALREADY_MOVED_WATER));
            object_b.setId((int) (long) object.get("id"));
            //Pump specific

            objects_b.add(object_b);
        }
        return objects_b;
    }

    private void setDestinationAttributes(JSONObject jo, String key, ArrayList<Field> pumps_l, ArrayList<Field> fields_l, ArrayList<Player> players_l) {
        JSONArray objects = (JSONArray) jo.get(key);
        Iterator<?> itr1 = objects.iterator();
        int i = 0;
        while (itr1.hasNext()) {
            JSONObject object = (JSONObject) itr1.next();

            ArrayList<Field> neighbours_b = new ArrayList<>();
            JSONArray neighbours = (JSONArray) object.get(PIPES);
            Iterator<?> itr2 = neighbours.iterator();
            while (itr2.hasNext()) {
                long neighbour = (long) itr2.next();
                neighbours_b.add(searchField(fields_l, (int) neighbour));
            }
            pumps_l.get(i).setNeighbours(neighbours_b);

            pumps_l.get(i).setInOutlet(object.get(INLET) != null ? searchField(fields_l, (int) (long) object.get(INLET)) : null,
                    object.get(OUTLET) != null ? searchField(fields_l, (int) (long) object.get(OUTLET)) : null);

            ArrayList<Player> players_b = new ArrayList<Player>();
            JSONArray players = (JSONArray) object.get(PLAYERS);
            itr2 = players.iterator();
            while (itr2.hasNext()) {
                long player = (long) itr2.next();
                players_b.add(searchPlayer(players_l, (int) player));
            }
            pumps_l.get(i).setPlayers(players_b);

            pumps_l.get(i).setPump(searchField(pumps_l, (int) (long) object.get("pump")));
            pumps_l.get(i).setPipe(searchField(pumps_l, (int) (long) object.get("pipe")));

            i++;
        }
    }


    private ArrayList<Player> createSaboteurs(JSONObject jo, String key, ArrayList<Field> fields) {
        JSONArray objects = (JSONArray) jo.get(key);
        Iterator<?> itr1 = objects.iterator();
        ArrayList<Player> objects_b = new ArrayList<>();
        while (itr1.hasNext()) {
            JSONObject object = (JSONObject) itr1.next();
            Player object_b;
            object_b = new Saboteur(searchField(fields, (int) (long) object.get("id")));
            object_b.setId((int) (long) object.get("id"));

            int i = 0;
            JSONArray pipes = (JSONArray) object.get(PIPES);
            Iterator<?> itr2 = pipes.iterator();
            while (itr2.hasNext()) {
                long pipe = (long) itr2.next();
                try {
                    object_b.setPipeInventory(i++, searchField(fields, (int) pipe));
                } catch (Exception e) {
                    //Nem baj ha nincsen cső az invenoryban
                }
            }

            try {
                object_b.addPump(searchField(fields, (int) (long) object.get("pump")));
            } catch (Exception e) {
                //Nem baj ha nincsen pumpa az inventoryban
            }

            objects_b.add(object_b);
        }
        return objects_b;
    }

    private ArrayList<Player> createPlumbers(JSONObject jo, String key, ArrayList<Field> fields) {
        JSONArray objects = (JSONArray) jo.get(key);
        Iterator<?> itr1 = objects.iterator();
        ArrayList<Player> objects_b = new ArrayList<>();
        while (itr1.hasNext()) {
            JSONObject object = (JSONObject) itr1.next();
            Player object_b;
            object_b = new Plumber(searchField(fields, (int) (long) object.get("id")));
            object_b.setId((int) (long) object.get("id"));

            int i = 0;
            JSONArray pipes = (JSONArray) object.get(PIPES);
            Iterator<?> itr2 = pipes.iterator();
            while (itr2.hasNext()) {
                long pipe = (long) itr2.next();
                try {
                    object_b.setPipeInventory(i++, searchField(fields, (int) pipe));
                } catch (Exception e) {
                    //Nem baj ha nincsen cső az invenoryban
                }
            }

            try {
                object_b.addPump(searchField(fields, (int) (long) object.get("pump")));
            } catch (Exception e) {
                //Nem baj ha nincsen pumpa az inventoryban
            }
            objects_b.add(object_b);
        }
        return objects_b;
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