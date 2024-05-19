package org.bme.mit.iet.view;

import org.bme.mit.iet.Game;
import org.bme.mit.iet.board.Board;
import org.bme.mit.iet.field.Field;
import org.bme.mit.iet.field.Pump;
import org.bme.mit.iet.player.Player;
import org.bme.mit.iet.view.fieldview.*;
import org.bme.mit.iet.view.playerview.PlayerView;
import org.bme.mit.iet.view.playerview.PlumberView;
import org.bme.mit.iet.view.playerview.SaboteurView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Játéktér elemeit összefogó osztály, kirajzolja a játéktér összes elemét egy JPanel-ben.
 */
public class BoardView extends JPanel {
    /**
     * PumpLikeFieldView-kat tartalmazo lista
     */
    private final ArrayList<PumpLikeFieldView> fieldViews;
    /**
     * PipeView-kat tartalmazo lista
     */
    private final ArrayList<PipeView> pipeViews;
    /**
     * PlayerView-kat tartalmazo lista
     */
    private final ArrayList<PlayerView> playerViews;

    private Random random = new Random();

    public BoardView(GameView gameView, ActionButtonGroup btnGroup) {
        this.fieldViews = new ArrayList<>();
        this.pipeViews = new ArrayList<>();
        this.playerViews = new ArrayList<>();

        setPanelBorder();

        var board = Game.getInstance().getBoard();
        setLayout(null);

        addFieldViews(gameView, btnGroup, board);
        addPipeViews(gameView, btnGroup, board);
        addPlayerViews(board);

        addMouseListenerToPanel();
    }

    private void setPanelBorder() {
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(4, 8, 4, 8, Color.BLACK), // Thick left border
                BorderFactory.createEmptyBorder(0, 0, 0, 0) // Empty border for top, right, and bottom
        ));
    }

    private void addFieldViews(GameView gameView, ActionButtonGroup btnGroup, Board board) {
        addSourceViews(gameView, btnGroup, board);
        addPumpViews(gameView, btnGroup, board);
        addDestinationViews(gameView, btnGroup, board);
    }

    private void addSourceViews(GameView gameView, ActionButtonGroup btnGroup, Board board) {
        int x = 60 + random.nextInt(10), y = 30;
        int temp = 1;
        for (Field source : board.getSrcs()) {
            var sv = new SourceView(gameView, btnGroup, source);
            setViewBounds(sv, x, y);
            x += (gameView.getWidth() / board.getSrcs().size()) + temp * random.nextInt(15);
            y += temp * 15;
            temp = -temp;
            add(sv);
            fieldViews.add(sv);
        }
    }

    private void addPumpViews(GameView gameView, ActionButtonGroup btnGroup, Board board) {
        int x = 50 + random.nextInt(10);
        int y = gameView.getHeight() / 3;
        int temp = 1;
        for (Field pump : board.getPumps()) {
            String fileNameIfWorkingOrNot = pump.isWorking() ? "pump" : "pump-not-working";
            var pv = new PumpView(gameView, btnGroup, pump, fileNameIfWorkingOrNot);
            setViewBounds(pv, x, y);
            x += (gameView.getWidth() / board.getPumps().size()) + temp * random.nextInt(15);
            y += temp * new Random().nextInt(150);
            temp = -temp;
            add(pv);
            fieldViews.add(pv);
        }
    }

    private void addDestinationViews(GameView gameView, ActionButtonGroup btnGroup, Board board) {
        int x = 60 + random.nextInt(10);
        int y = (2 * gameView.getHeight()) / 3;
        int temp = 1;
        for (Field destination : board.getDests()) {
            var dv = new DestView(gameView, btnGroup, destination);
            setViewBounds(dv, x, y);
            x += (gameView.getWidth() / board.getDests().size()) + temp * 5 + temp * random.nextInt(15);
            y += temp * 15;
            temp = -temp;
            add(dv);
            fieldViews.add(dv);
        }
    }

    private void addPipeViews(GameView gameView, ActionButtonGroup btnGroup, Board board) {
        for (Field pipe : board.getPipes()) {
            var pv = new PipeView(gameView, btnGroup, pipe);
            pipeViews.add(pv);
        }
    }

    private void addPlayerViews(Board board) {
        for (Player plumber : board.getPlums()) {
            playerViews.add(new PlumberView(plumber));
        }

        for (Player saboteur : board.getSabs()) {
            playerViews.add(new SaboteurView(saboteur));
        }
    }

    private void setViewBounds(JComponent view, int x, int y) {
        view.setBounds(x, y, view.getPreferredSize().width, view.getPreferredSize().height);
    }

    private void addMouseListenerToPanel() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                pipeViews.forEach(pipeView -> {
                    var startPoint = pipeView.getStartPoint();
                    var endPoint = pipeView.getEndPoint();
                    if (startPoint != null && endPoint != null) {
                        var distance = Line2D.ptSegDist(startPoint.x, startPoint.y, endPoint.x, endPoint.y, e.getX(), e.getY());
                        if (distance < 3) {
                            System.out.println("pipe: " + pipeView.getField().getId() + " is pressed!");
                            pipeView.pipeClicked();
                        }
                    }
                });
            }
        });
    }


    /**
     * A csovek, jatekosok es a hatter kirajzolasaert felelos fuggveny. A jatekosok amellett a mezo mellett jelennek
     * meg, ahol jelenleg allnak.
     *
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackgroundImage(g, "assets/board/desert.png");
        Graphics2D g2d = (Graphics2D) g;

        for (PipeView pipeView : pipeViews) {
            setPipeEndpoints(pipeView);
            drawPipe(g2d, pipeView);
        }

        drawPlayersOnFields(g);
        drawPlayersOnPipes(g);
    }

    private void drawBackgroundImage(Graphics g, String filePath) {
        try {
            var inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            if (inputStream != null) {
                var image = ImageIO.read(inputStream);
                var scaledImage = image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                g.drawImage(scaledImage, 0, 0, null);
            } else {
                showErrorMessage("Image not found: " + filePath);
            }
        } catch (IOException e) {
            showErrorMessage(e.toString());
        }
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void setPipeEndpoints(PipeView pipeView) {
        var pipe = pipeView.getField();
        var pumps = pipe.getNeighbours();

        if (pumps.size() == 2) {
            setPipeEndpointsForTwoPumps(pipeView, pumps);
        } else if (pumps.size() == 1) {
            setPipeEndpointsForOnePump(pipeView, pumps.get(0));
        }
    }

    private void setPipeEndpointsForTwoPumps(PipeView pipeView, ArrayList<Field> pumps) {
        for (PumpLikeFieldView fieldView : fieldViews) {
            var field = fieldView.getField();
            Dimension buttonSize = fieldView.getSize();

            if (field == pumps.get(0)) {
                pipeView.setStartPoint(getFieldCenter(fieldView));
            }
            if (field == pumps.get(1)) {
                pipeView.setEndPoint(getFieldCenter(fieldView));
            }
        }
    }

    private void setPipeEndpointsForOnePump(PipeView pipeView, Field pump) {
        for (PumpLikeFieldView fieldView : fieldViews) {
            if (fieldView.getField() == pump) {
                var r = new Random();
                Dimension buttonSize = fieldView.getSize();
                pipeView.setStartPoint(getFieldCenter(fieldView));
                var xdif = r.nextInt(100) - 50;
                var ydif = r.nextInt(100) - 50;
                pipeView.setEndPoint(new Point(fieldView.getLocation().x + buttonSize.width / 2 + adjustCoordinate(xdif), fieldView.getLocation().y + buttonSize.height / 2 + adjustCoordinate(ydif)));
            }
        }
    }

    private int adjustCoordinate(int coord) {
        return coord < 0 ? -25 + coord : 25 + coord;
    }

    private Point getFieldCenter(PumpLikeFieldView fieldView) {
        Dimension buttonSize = fieldView.getSize();
        return new Point(fieldView.getLocation().x + buttonSize.width / 2, fieldView.getLocation().y + buttonSize.height / 2);
    }
    //kirajzoljuk a csovet
    private void drawPipe(Graphics2D g2d, PipeView pipeView) {
        var pipe = pipeView.getField();
        //kirajzoljuk a cso ket szelet
        drawPipeSection(g2d, pipeView, pipe.isSticky() ? Color.GREEN : pipe.isSlippery() ? Color.YELLOW : Color.BLACK, 7);
        //kirajzoljuk a cso kozepet
        drawPipeSection(g2d, pipeView, pipe.isWorking() ? (pipe.getCurrentWaterLevel() > 0 ? Color.BLUE : Color.BLACK) : Color.RED, 3);
    }

    private void drawPipeSection(Graphics2D g2d, PipeView pipeView, Color color, int stroke) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(stroke));
        g2d.drawLine(pipeView.getStartPoint().x, pipeView.getStartPoint().y, pipeView.getEndPoint().x, pipeView.getEndPoint().y);
    }

    private void drawPlayersOnFields(Graphics g) {
        for (PumpLikeFieldView fieldView : fieldViews) {
            var players = fieldView.getField().getPlayers();
            if (!players.isEmpty()) {
                drawPlayers(g, fieldView, players);
            }
        }
    }

    private void drawPlayersOnPipes(Graphics g) {
        for (var pipeView : pipeViews) {
            var players = pipeView.getField().getPlayers();
            if (!players.isEmpty()) {
                drawPlayersOnPipe(g, pipeView, players);
            }
        }
    }

    private void drawPlayers(Graphics g, PumpLikeFieldView fieldView, ArrayList<Player> players) {
        var center = new Point(fieldView.getBounds().x, fieldView.getBounds().y);
        var radius = 55;
        var theta = 2 * Math.PI / players.size();
        int c = 1;
        for (Player player : players) {
            drawPlayer(g, center, radius, theta, c, player);
            c++;
        }
    }

    private void drawPlayersOnPipe(Graphics g, PipeView pipeView, ArrayList<Player> players) {
        var center = new Point((pipeView.getStartPoint().x + pipeView.getEndPoint().x) / 2, (pipeView.getStartPoint().y + pipeView.getEndPoint().y) / 2);
        for (var player : players) {
            drawPlayerImage(g, center, player);
        }
    }

    private void drawPlayer(Graphics g, Point center, int radius, double theta, int c, Player player) {
        var angle = c * theta;
        var x = (int) (center.x + radius * Math.cos(angle));
        var y = (int) (center.y + radius * Math.sin(angle));
        drawPlayerImage(g, new Point(x, y), player);
    }

    private void drawPlayerImage(Graphics g, Point position, Player player) {
        Image playerImage;
        var currentPlayer = Game.getInstance().getCurrentPlayer();
        for (PlayerView playerView : playerViews) {
            if (playerView.getPlayer() == player) {
                playerImage = playerView.loadPlayerImage(player == currentPlayer ? "-current" : "");
                g.drawImage(playerImage, position.x, position.y, null);
            }
        }
    }

}
