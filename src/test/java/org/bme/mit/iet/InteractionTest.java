package org.bme.mit.iet;

import org.bme.mit.iet.board.Board;
import org.bme.mit.iet.field.*;
import org.bme.mit.iet.player.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InteractionTest {

    private static Board board;

    @BeforeEach
    void init() {

        //Letrehozzuk a mezoket
        Pipe pipe1 = new Pipe();
        Pipe pipe2 = new Pipe();
        Pipe pipe3 = new Pipe();
        Pipe pipe4 = new Pipe();
        Pipe pipe5 = new Pipe();
        Pipe pipe6 = new Pipe();
        Pump pump1 = new Pump();
        Pump pump2 = new Pump();
        Source source = new Source();
        Destination dest = new Destination();

        //Osszekapcsoljuk a mezoket
        source.setPipes(new ArrayList<>(List.of(pipe1)));
        source.setInlet(null);
        source.setOutlet(pipe1);

        dest.setPipes(new ArrayList<>(List.of(pipe5, pipe6)));
        dest.setInlet(pipe5);
        dest.setOutlet(null);

        pump1.setPipes(new ArrayList<>(List.of(pipe1, pipe2, pipe3)));
        pump1.setInlet(pipe1);
        pump1.setOutlet(pipe2);

        pump2.setPipes(new ArrayList<>(List.of(pipe2, pipe3, pipe4, pipe5, pipe6)));
        pump2.setInlet(pipe2);
        pump2.setOutlet(pipe5);

        pipe1.setPumps(new ArrayList<>(List.of(source, pump1)));
        pipe2.setPumps(new ArrayList<>(List.of(pump1, pump2)));
        pipe3.setPumps(new ArrayList<>(List.of(pump1, pump2)));
        pipe4.setPumps(new ArrayList<>(List.of(pump2)));
        pipe5.setPumps(new ArrayList<>(List.of(pump2, dest)));
        pipe6.setPumps(new ArrayList<>(List.of(pump2, dest)));

        //Letrehozzuk a jatekosokat es elhelyezzuk oket a palyan, mezok is tudnak arrol hogy ki all rajtuk.
        Saboteur player1 = new Saboteur(pump2);
        Plumber player2 = new Plumber(pump2);
        Saboteur player3 = new Saboteur(pump1);
        Plumber player4 = new Plumber(pump1);
        Plumber player5 = new Plumber(pipe6);

        pump1.setPlayers(new ArrayList<>(List.of(player3)));
        pump1.setPlayers(new ArrayList<>(List.of(player4)));
        pump2.setPlayers(new ArrayList<>(List.of(player1)));
        pump2.setPlayers(new ArrayList<>(List.of(player2)));
        pipe6.setPlayers(new ArrayList<>(List.of(player5)));

        pipe4.makeSlippery();
        pipe5.breakField();
        pipe2.addWater(1);
        pump2.breakField();

        //Beallitjuk a teszteleshez hasznalt jatekosok mezo keszletet
        Pipe pipe7 = new Pipe();
        player2.setPipeInventory(0, pipe7);
        player2.setPipeInventory(1, pipe7);

        //Letrehozzuk a jatek tabla altal hasznalt objektum listakat
        ArrayList<Field> pumps = new ArrayList<>(List.of(pump1, pump2));
        ArrayList<Field> sources = new ArrayList<>(List.of(source));
        ArrayList<Field> dests = new ArrayList<>(List.of(dest));
        ArrayList<Field> pipes = new ArrayList<>(List.of(pipe1, pipe2, pipe3, pipe4, pipe5, pipe6));
        ArrayList<Player> plums = new ArrayList<>(List.of(player2, player4, player5));
        ArrayList<Player> sabs = new ArrayList<>(List.of(player1, player3));

        //inicializaljuk a jatek tablat
        board = new Board(plums,sabs, pipes, pumps, sources, dests);
    }

    @Test
    void pierce_working_pipe_containing_water() {
        //Van benne víz
        assertNotEquals(0, board.getPipes().get(1).getCurrentWaterLevel());
        //A csőre mozog
        board.getPlayers().get(0).moveTo(board.getPipes().get(1));
        //Kilyukasztja
        board.getPlayers().get(0).piercePipe();
        //Nem működik
        assertFalse(board.getPipes().get(1).isWorking());
        //Nincs benne víz
        assertEquals(0, board.getPipes().get(1).getCurrentWaterLevel(), board.getPipes().get(1).getCurrentWaterLevel());
    }

    @Test
    void pierce_working_empty_pipe() {
        //Nincs benne víz
        assertEquals(0, board.getPipes().get(2).getCurrentWaterLevel());
        //A csőre mozog
        board.getPlayers().get(0).moveTo(board.getPipes().get(2));
        //Kilyukasztja
        board.getPlayers().get(0).piercePipe();
        //Nem működik
        assertFalse(board.getPipes().get(2).isWorking());
        //Nincs benne víz
        assertEquals(0, board.getPipes().get(2).getCurrentWaterLevel(), board.getPipes().get(2).getCurrentWaterLevel());
    }

    @Test
    void pierce_pierced_pipe() {
        //Lyukas a cső
        assertFalse(board.getPipes().get(4).isWorking());
        //A csőre mozog
        board.getPlayers().get(0).moveTo(board.getPipes().get(4));
        //Kilyukasztja
        board.getPlayers().get(0).piercePipe();
        //Még mindig lyukas
        assertFalse(board.getPipes().get(4).isWorking());
    }

    @Test
    void pierce_repaired_pipe() {
        //Nem működik a cső
        assertFalse(board.getPipes().get(4).isWorking());
        //A csőre mozog a szerelő
        board.getPlayers().get(1).moveTo(board.getPipes().get(4));
        //Megszereli
        board.getPlayers().get(1).changeField();
        //Most már működik
        assertTrue(board.getPipes().get(4).isWorking());
        //Arrébb mozog
        board.getPlayers().get(1).moveTo(board.getPumps().get(1));
        //A csőre mozog a szabotőr
        board.getPlayers().get(0).moveTo(board.getPipes().get(4));
        //Megpróbálja kiyukasztani
        board.getPlayers().get(0).piercePipe();
        //Nem sikerül, még mindig működik
        assertTrue(board.getPipes().get(4).isWorking());
    }

    @Test
    void repair_broken_pump() {
        //Nem működik
        assertFalse(board.getPumps().get(1).isWorking());
        //Rajta áll alapból, megjavítja
        board.getPlayers().get(1).changeField();
        //Már működik
        assertTrue(board.getPumps().get(1).isWorking());
    }

    @Test
    void repair_working_pump() {
        //Működik
        assertTrue(board.getPumps().get(0).isWorking());
        //Rajta áll alapból, megjavítja
        board.getPlayers().get(3).changeField();
        //Még mindig működik
        assertTrue(board.getPumps().get(0).isWorking());
    }

    @Test
    void repair_pierced_pipe() {
        //Nem működik
        assertFalse(board.getPipes().get(4).isWorking());
        //A csőre mozog
        board.getPlayers().get(1).moveTo(board.getPipes().get(4));
        //Megjavítja
        board.getPlayers().get(1).changeField();
        //Már működik
        assertTrue(board.getPipes().get(4).isWorking());
    }

    @Test
    void repair_working_pipe() {
        //Működik
        assertTrue(board.getPipes().get(2).isWorking());
        //A csőre mozog
        board.getPlayers().get(1).moveTo(board.getPipes().get(2));
        //Megjavítja
        board.getPlayers().get(1).changeField();
        //Még mindig működik
        assertTrue(board.getPipes().get(2).isWorking());
    }

    @Test
    void set_pump() {
        //Lekérdezzük a csöveket
        List<Field> pipes = board.getPlayers().get(1).getCurrentField().getNeighbours();
        assertDoesNotThrow(() -> board.getPlayers().get(1).setPumpConnections(pipes.get(0), pipes.get(1)));
    }

    @Test
    void set_same_pipes_pump() {
        //Lekérdezzük a csöveket
        List<Field> pipes = board.getPlayers().get(1).getCurrentField().getNeighbours();
        assertThrows(Exception.class, () -> board.getPlayers().get(1).setPumpConnections(pipes.get(0), pipes.get(0)));
    }
}
