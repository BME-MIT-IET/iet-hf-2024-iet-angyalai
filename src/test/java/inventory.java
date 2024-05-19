import org.bme.mit.iet.board.Board;
import org.bme.mit.iet.field.*;
import org.bme.mit.iet.player.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class inventory {

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
        pipe2.addWater(1);
        pump2.breakField();
        //TODO Ezt amúgy nem kézzel kéne hívni hanem konstruktorból első körben


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
        dest.resetResources();
    }

    @Test
    void pick_up_pipe_from_dest() {
        //Mozog a ciszternára
        board.getPlayers().get(4).moveTo(board.getDests().get(0));

        Field pipe = new Pipe();

        //Felvesz egy csövet a ciszternából
        board.getPlayers().get(4).getCurrentField().pickUpNeighbourField(pipe, board.getPlayers().get(4));
        Field[] pipes = board.getPlayers().get(4).getPipes();
        assertEquals(1, pipes.length);
        assertEquals(pipe, pipes[0]);
    }

    @Test
    void pick_up_pipe_from_board() {
        //Felvett cső
        Field pipe = board.getPlayers().get(0).getCurrentField().getNeighbours().get(0);
        //Cső felvétele megtörténik
        board.getPlayers().get(0).pickUpField(pipe);
        //Bekerül az inventoryba
        assertEquals(pipe, board.getPlayers().get(0).getPipes()[0]);
        //A pumpa szomszédai között már nem szerepel
        assertFalse(board.getPlayers().get(0).getCurrentField().getNeighbours().contains(pipe));
    }

    @Test
    void pick_up_both_ennds_of_pipe_from_board() {
        //Felvett cső
        Field pipe = board.getPipes().get(2);

        //Cső egyik vége felvevődik
        board.getPlayers().get(0).pickUpField(pipe);
        //Bekerül az inventoryba
        assertEquals(pipe, board.getPlayers().get(0).getPipes()[0]);
        //A pumpa szomszédai között már nem szerepel
        assertFalse(board.getPlayers().get(0).getCurrentField().getNeighbours().contains(pipe));

        //Cső másik vége felvevődik
        board.getPlayers().get(2).pickUpField(pipe);
        //Bekerül az inventoryba
        assertEquals(pipe, board.getPlayers().get(2).getPipes()[0]);
        //A pumpa szomszédai között már nem szerepel
        assertFalse(board.getPlayers().get(2).getCurrentField().getNeighbours().contains(pipe));
    }

    @Test
    void pick_up_both_ends_of_pipe_with_one_end_empty() {
        //Felvett cső
        Field pipe = board.getPipes().get(3);
        //Cső felvétele megtörténik
        board.getPlayers().get(0).pickUpField(pipe);
        //Bekerül az inventoryba
        assertEquals(pipe, board.getPlayers().get(0).getPipes()[0]);
        assertEquals(pipe, board.getPlayers().get(0).getPipes()[1]);
        //A pumpa szomszédai között már nem szerepel
        assertFalse(board.getPlayers().get(0).getCurrentField().getNeighbours().contains(pipe));
    }

    @Test
    void pick_up_pump() {
        //Mozog a ciszternára
        board.getPlayers().get(4).moveTo(board.getDests().get(0));

        Field pump = new Pump();

        //Felvesz egy csövet a ciszternából
        board.getPlayers().get(4).getCurrentField().pickUpNeighbourField(pump, board.getPlayers().get(4));

        Field pickedPump = board.getPlayers().get(4).getPump();
        assertEquals(pump, pickedPump);

    }

    @Test
    void place_pipe() {
        Pipe pipe = new Pipe();
        board.getPlayers().get(0).addPipe(pipe);
        //Cső lerakása
        board.getPlayers().get(0).placeField(board.getPlayers().get(0).getPipes()[0]);
        //Már nincs az inventoryban
        assertNotEquals(pipe, board.getPlayers().get(0).getPipes()[0]);
        //A pumpa szomszédai között ismét szerepel
        assertTrue(board.getPlayers().get(0).getCurrentField().getNeighbours().contains(pipe));
    }

    @Test
    void place_pump() {
        Pump pump = new Pump();
        board.getPlayers().get(0).addPump(pump);
        //Arrébb mozog
        board.getPlayers().get(0).moveTo(board.getPipes().get(1));
        //Pumpa lerakása
        board.getPlayers().get(0).placeField(board.getPlayers().get(0).getPump());
        //Már nincs az inventoryban
        assertNotEquals(pump, board.getPlayers().get(0).getPump());
        //A cső szomszédai között viszont szerepel
        assertTrue(board.getPlayers().get(0).getCurrentField().getNeighbours().contains(pump));
    }


}
