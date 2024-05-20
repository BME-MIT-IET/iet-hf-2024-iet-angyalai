package org.bme.mit.iet;

import org.bme.mit.iet.board.Board;
import org.bme.mit.iet.field.*;
import org.bme.mit.iet.player.Player;
import org.bme.mit.iet.player.Plumber;
import org.bme.mit.iet.player.Saboteur;
import org.json.simple.JSONObject;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Singleton osztály, a játék állapotát felügyelő és “globális” feladatokat (víz folyatás, pontszámítás, pumpa hiba generálás, játékosok sorrendje) végrehajtó egység.
 */
public class Game implements Serializable {
    /**
     * A Game osztaly egyetlen peldanya
     */
    private static Game game = null;

    /**
     * Az osztaly esemenyeit tarolo lista
     */
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    /**
     * Jatekot alkoto palya
     */
    private Board board;
    /**
     * Az aktualisan soron levo jatekos
     */
    private Player currentPlayer;
    /**
     * A korok szama
     */
    private int rounds;
    /**
     * Az aktualis kor sorszama
     */
    private int currentRound;
    /**
     * A szerelok altal szerzett pontok
     */
    private int plumberPoints;
    /**
     * A szabotorok altal szerzett pontok
     */
    private int saboteursPoints;
    /**
     * A soron levo jatekos vegzett-e mar valamilyen muveletet
     */
    private boolean isCurrentPlayerMadeAction;
    /**
     * A soron levo jatekos lepett-e mar
     */
    private boolean isCurrentPlayerMadeMove;

    public int getAlreadyPlayed() {
        return alreadyPlayed;
    }

    /**
     * Az adott korben hany jatekos kerult mar sorra
     */
    private int alreadyPlayed;
    /**
     * A véletlenszám-generátor
     */
    private Random random = new Random();

    private Game() {
    }

    public static Game getInstance() {
        if (game == null) game = new Game();
        return game;
    }

    /**
     * A palya megalkotasaert felelos fuggveny. Letrehoz csoveket, pumpakat, ciszternakat, forrasokat, osszekoti ezeket, es a jatekosokat elhelyezi rajtuk.
     *
     * @param numberOfPlums a palyan szereplo szerelok szama
     * @param numberOfSabs  a palyan szereplo szabotorok szama
     * @return a megalkotott palya
     */
    public Board createBoard(int numberOfPlums, int numberOfSabs) {
        Field.setIdCount(0);
        Player.setIdCount(0);
        board = null;
        var numberOfPlayers = numberOfPlums + numberOfSabs;
        var numberOfDest = Math.round(numberOfPlayers / 2f) + 1;
        var numberOfSrc = Math.round(numberOfPlayers / 2f) + 1;
        var minNumberOfPumps = Math.toIntExact(Math.round(numberOfDest * 1.5));
        var maximumNumberOfPumps = Math.round(numberOfDest * 2f);
        var numOfPumps = random.nextInt(maximumNumberOfPumps - minNumberOfPumps + 1) + minNumberOfPumps;
        var numOfPipes = Math.toIntExact(Math.round(numOfPumps * 1.5));

        ArrayList<Field> pipes = new ArrayList<>();
        ArrayList<Field> pumps = new ArrayList<>();
        ArrayList<Field> src = new ArrayList<>();
        ArrayList<Field> dest = new ArrayList<>();
        var pumpLikeFields = new ArrayList<Field>();

        for (int i = 0; i < numOfPipes; i++) {
            pipes.add(new Pipe());
        }
        for (int i = 0; i < numOfPumps; i++) {
            pumps.add(new Pump());
        }
        for (int i = 0; i < numberOfSrc; i++) {
            src.add(new Source());
        }
        for (int i = 0; i < numberOfDest; i++) {
            dest.add(new Destination());
        }
        pumpLikeFields.addAll(pumps);
        pumpLikeFields.addAll(src);
        pumpLikeFields.addAll(dest);
        for (int i = 0; i < pumpLikeFields.size(); i++) {
            Field p = pipes.remove(0);
            Field current = pumpLikeFields.get(i);
            if (p.getNeighbours().size() < 2) {
                current.addField(p);
                p.addField(current);
            }
            pipes.add(p);
        }
        Collections.shuffle(pumpLikeFields);
        for (int i = 0; i < pumpLikeFields.size(); i++) {
            if (random.nextInt(10) < 9) {
                Field p = pipes.remove(0);//
                Field current = pumpLikeFields.get(i);
                if (p.getNeighbours().size() < 2) {
                    current.addField(p);
                    p.addField(current);
                }
                pipes.add(p);
            }
        }

        ArrayList<Player> plums = new ArrayList<>();
        for (int i = 0; i < numberOfPlums; i++) {
            var startingField = dest.get(random.nextInt(dest.size()));
            var plum = new Plumber(startingField);
            startingField.movePlayer(plum);
            plums.add(plum);
        }

        ArrayList<Player> sabs = new ArrayList<>();
        for (int i = 0; i < numberOfSabs; i++) {
            var startingField = pumps.get(random.nextInt(pumps.size()));
            var sab = new Saboteur(startingField);
            startingField.movePlayer(sab);
            sabs.add(sab);
        }
        return new Board(plums, sabs, pumps, pipes, dest, src);
    }

    /**
     * Egy uj jatek letrehozasaert felelos fuggveny
     *
     * @param rounds        hany korbol all a jatek
     * @param numberOfPlums a palyan szereplo szerelok szama
     * @param numberOfSabs  a palyan szereplo szabotorok szama
     */
    public void createNewGame(int rounds, int numberOfPlums, int numberOfSabs) {
        this.board = createBoard(numberOfPlums, numberOfSabs);
        this.board.getDests().forEach(dest -> dest.resetResources());
        this.rounds = rounds;
        this.currentRound = 1;

        this.currentPlayer = board.getPlayers().get(0);
        this.isCurrentPlayerMadeAction = false;
        this.isCurrentPlayerMadeMove = false;

        this.plumberPoints = 0;
        this.saboteursPoints = 0;

        this.alreadyPlayed = 0;
    }

    /**
     * Jatek betolteseert felelos fuggveny
     *
     * @param board                     a palya
     * @param rounds                    a korok szama
     * @param currentRound              az aktualis kor sorszama
     * @param currentPlayer             a soron levo jatekos
     * @param isCurrentPlayerMadeAction a soron levo jatekos vegzett-e mar valamilyen muveletet
     * @param isCurrentPlayerMadeMove   a soron levo jatekos lepett-e mar
     * @param alreadyPlayed             az adott korben hany jatekos kerult mar sorra
     */
    public void loadGame(Board board, int rounds, int currentRound, Player currentPlayer, boolean isCurrentPlayerMadeAction, boolean isCurrentPlayerMadeMove, int alreadyPlayed) {
        this.board = board;
        this.rounds = rounds;
        this.currentRound = currentRound;
        this.currentPlayer = currentPlayer;
        this.isCurrentPlayerMadeAction = isCurrentPlayerMadeAction;
        this.isCurrentPlayerMadeMove = isCurrentPlayerMadeMove;
        this.alreadyPlayed = alreadyPlayed;
    }

    /**
     * A szerelok pontjainak szamat noveli
     */
    public void waterGained() {
        plumberPoints += 1;
    }

    /**
     * A szabotorok pontjainak szamat csokkenti
     */
    public void waterLost() {
        saboteursPoints += 1;
    }

    /**
     * Veletlenszeruen kivalaszt egy pumpat es elrontja.
     */
    public void generatePumpError() {
        var pumps = board.getPumps();
        pumps.get(random.nextInt(pumps.size())).breakField();
    }

    /**
     * A viz mozgatasaert felelos fuggveny, minden forrasra, pumpara, csore es ciszternara meghivja a moveWater fuggvenyt.
     */
    public void flowWater() {
        board.getSrcs().forEach(source -> source.moveWater());
        board.getPumps().forEach(pump -> pump.moveWater());
        board.getPipes().forEach(pipe -> pipe.moveWater());
        board.getFields().forEach(field -> field.setAlreadyMovedWater(false));
    }

    /**
     * Beallitja, hogy a sorra kerulo jatekos meg nem lepett/vegzett semmilyen muveletet.
     * Ha mar minden jatekos sorra kerult az adott korben, meghivja az endRound fuggvenyt.
     */
    public void nextPlayer() {
        isCurrentPlayerMadeAction = false;
        isCurrentPlayerMadeMove = false;
        board.getPlayers().remove(currentPlayer);
        board.getPlayers().add(currentPlayer);
        currentPlayer = board.getPlayers().get(0);
        alreadyPlayed += 1;
        if (alreadyPlayed == board.getPlayers().size()) {
            alreadyPlayed = 0;
            endRound();
        }
    }

    /**
     * Ha az aktualis kornek a sorszama megegyezik a korok szamaval, kivaltodik a gameOver esemeny. Kulonben minden kor vegen meghivjuk a vizet mozgato,
     * pumpa elromlast okozo, illetve a ciszternaknal a csoveket/pumpakat ujratolto fuggvenyeket.
     */
    public void endRound() {
        if (currentRound == rounds) {
            pcs.firePropertyChange("gameOver", false, true);
        } else {
            flowWater();
            generatePumpError();
            resetResources();
            currentRound += 1;
            System.out.println("Sabs: " + saboteursPoints + "  Plums: " + plumberPoints);
        }
    }

    /**
     * Meghivja a ciszternakra es a csovekre a resetResources fuggvenyt.
     */
    private void resetResources() {
        board.getDests().forEach(dest -> dest.resetResources());
        board.getPipes().forEach(pipe -> pipe.resetResources());
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getRounds() {
        return rounds;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public int getPlumberPoints() {
        return plumberPoints;
    }

    public int getSaboteursPoints() {
        return saboteursPoints;
    }

    public boolean isCurrentPlayerMadeAction() {
        return isCurrentPlayerMadeAction;
    }

    public void setCurrentPlayerMadeAction(boolean b) {
        isCurrentPlayerMadeAction = b;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isCurrentPlayerMadeMove() {
        //Might not need
        return isCurrentPlayerMadeMove;
    }

    public void setCurrentPlayerMadeMove(boolean b) {
        isCurrentPlayerMadeMove = b;
    }

    /**
     * Feliratkozik az esemenyre
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * JSON formatumba alakitja a Game adattagjait
     *
     * @return JSONObject a game adattagjaival
     */
    public JSONObject toJSON() {
        JSONObject jo = new JSONObject();
        jo.put("Board", board.toJSON());
        jo.put("currentPlayer", currentPlayer.getId());
        jo.put("rounds", rounds);
        jo.put("currentRound", currentRound);
        jo.put("plumberPoints", plumberPoints);
        jo.put("saboteursPoints", saboteursPoints);
        jo.put("isCurrentPlayerMadeAction", isCurrentPlayerMadeAction);
        jo.put("isCurrentPlayerMadeMove", isCurrentPlayerMadeMove);
        jo.put("alreadyPlayed", alreadyPlayed);

        return jo;
    }
}
