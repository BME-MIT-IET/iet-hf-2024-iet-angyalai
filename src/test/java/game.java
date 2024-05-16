
import org.bme.mit.iet.board.Board;
import org.bme.mit.iet.field.*;
import org.bme.mit.iet.player.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class game {

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
        ArrayList<Player> players = new ArrayList<>(List.of(player1, player2, player3, player4, player5));

        //inicializaljuk a jatek tablat
        board = new Board(players,players, pipes, pumps, sources, dests);

    }

    @Test
    void skip() {
        //Még nem kell
    }

    @Test
    void new_game() {
        //Meg nem kell
    }

}
