package org.bme.mit.iet.view;

import org.bme.mit.iet.Game;
import org.bme.mit.iet.field.Field;
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

    /**
     * Konstruktor, beallitja a panel szeleit, feltolti a fieldViews, pipeViews, playerViews listakat. A button-oket
     * random pozicioba helyezi el. Hozzaad egy MouseListenert a panelhez, amely ellenorzi, hogy egy csohoz tartozo
     * vonalra kattintottunk-e.
     *
     * @param gameView
     * @param btnGroup
     */
    public BoardView(GameView gameView, ActionButtonGroup btnGroup) {
        this.fieldViews = new ArrayList<>();
        this.pipeViews = new ArrayList<>();
        this.playerViews = new ArrayList<>();

        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(4, 8, 4, 8, Color.BLACK), // Thick left border
                BorderFactory.createEmptyBorder(0, 0, 0, 0) // Empty border for top, right, and bottom
        ));

        var board = Game.getInstance().getBoard();
        setLayout(null);
        var random = new Random();

        int x = 60 + random.nextInt(10), y = 30;
        int temp = 1;
        for (Field source : board.getSrcs()) {
            var sv = new SourceView(gameView, btnGroup, source);
            sv.setBounds(x, y, sv.getPreferredSize().width, sv.getPreferredSize().height);
            x += (gameView.getWidth() / board.getSrcs().size()) + temp * random.nextInt(15);
            y += temp * 15;
            temp = -temp;

            add(sv);
            fieldViews.add(sv);
        }

        x = 50 + random.nextInt(10);
        y = gameView.getHeight() / 3;
        for (Field pump : board.getPumps()) {
            String fileNameIfWorkingOrNot;
            if (pump.isWorking()) fileNameIfWorkingOrNot = "pump";
            else fileNameIfWorkingOrNot = "pump-not-working";
            var pv = new PumpView(gameView, btnGroup, pump, fileNameIfWorkingOrNot);
            pv.setBounds(x, y, pv.getPreferredSize().width, pv.getPreferredSize().height);
            x += (gameView.getWidth() / board.getPumps().size()) + temp * random.nextInt(15);
            y += temp * new Random().nextInt(150);
            temp = -temp;
            add(pv);
            fieldViews.add(pv);
        }


        x = 60 + random.nextInt(10);
        y = (2 * gameView.getHeight()) / 3;

        for (Field destination : board.getDests()) {
            var dv = new DestView(gameView, btnGroup, destination);
            dv.setBounds(x, y, dv.getPreferredSize().width, dv.getPreferredSize().height);
            x += (gameView.getWidth() / board.getDests().size()) + temp * 5 + temp * random.nextInt(15);
            y += temp * 15;
            temp = -temp;
            add(dv);
            fieldViews.add(dv);
        }

        for (Field pipe : board.getPipes()) {
            var pv = new PipeView(gameView, btnGroup, pipe);
            pipeViews.add(pv);
        }

        for (Player plumber : board.getPlums()) {
            playerViews.add(new PlumberView(plumber));
        }

        for (Player saboteur : board.getSabs()) {
            playerViews.add(new SaboteurView(saboteur));
        }

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
        var filePath = "assets/board/desert.png";
        try {
            var inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            if (inputStream != null) {
                var image = ImageIO.read(inputStream);
                var scaledImage = image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                g.drawImage(scaledImage, 0, 0, null);
            } else {
                JOptionPane.showMessageDialog(null, "Image not found: " + filePath, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
        var g2d = (Graphics2D) g;

        Dimension buttonSize;

        for (PipeView pipeView : pipeViews) {
            var pipe = pipeView.getField();
            var pumps = pipe.getNeighbours();

            if (pumps.size() == 2) {
                for (PumpLikeFieldView fieldView : fieldViews) {
                    var field = fieldView.getField();
                    buttonSize = fieldView.getSize();

                    if (field == pumps.get(0)) {
                        pipeView.setStartPoint(new Point(fieldView.getLocation().x + buttonSize.width / 2, fieldView.getLocation().y + buttonSize.height / 2));
                    }
                    if (field == pumps.get(1)) {
                        pipeView.setEndPoint(new Point(fieldView.getLocation().x + buttonSize.width / 2, fieldView.getLocation().y + buttonSize.height / 2));
                    }
                }

                //kirajzoljuk a cso ket szelet
                if (pipe.isSticky()) {
                    g2d.setColor(Color.GREEN);
                } else if (pipe.isSlippery()) {
                    g2d.setColor(Color.YELLOW);
                } else g2d.setColor(Color.BLACK);

                g2d.setStroke(new BasicStroke(7));
                g2d.drawLine(pipeView.getStartPoint().x, pipeView.getStartPoint().y, pipeView.getEndPoint().x, pipeView.getEndPoint().y);

                //kirajzoljuk a cso kozepet
                if (!pipe.isWorking()) {
                    g2d.setColor(Color.RED);
                } else if (pipe.getCurrentWaterLevel() > 0) {
                    g2d.setColor(Color.BLUE);
                } else g2d.setColor(Color.BLACK);

                g2d.setStroke(new BasicStroke(3));
                g2d.drawLine(pipeView.getStartPoint().x, pipeView.getStartPoint().y, pipeView.getEndPoint().x, pipeView.getEndPoint().y);

            } else if (pumps.size() == 1) {
                for (PumpLikeFieldView fieldView : fieldViews) {
                    if (fieldView.getField() == pumps.get(0)) {
                        var r = new Random();
                        buttonSize = fieldView.getSize();
                        pipeView.setStartPoint(new Point(fieldView.getLocation().x + buttonSize.width / 2, fieldView.getLocation().y + buttonSize.height / 2));
                        var xdif = r.nextInt(100) - 50;
                        var ydif = r.nextInt(100) - 50;
                        pipeView.setEndPoint(new Point(fieldView.getLocation().x + buttonSize.width / 2 + (xdif < 0 ? -25 + xdif : 25 + xdif), fieldView.getLocation().y + buttonSize.height / 2 + (ydif < 0 ? -25 + ydif : 25 + ydif)));
                    }
                }

                //kirajzoljuk a cso ket szelet
                if (pipe.isSticky()) {
                    g2d.setColor(Color.GREEN);
                } else if (pipe.isSlippery()) {
                    g2d.setColor(Color.YELLOW);
                } else g2d.setColor(Color.BLACK);

                g2d.setStroke(new BasicStroke(7));
                g2d.drawLine(pipeView.getStartPoint().x, pipeView.getStartPoint().y, pipeView.getEndPoint().x, pipeView.getEndPoint().y);

                //kirajzoljuk a cso kozepet
                if (!pipe.isWorking()) {
                    g2d.setColor(Color.RED);
                } else if (pipe.getCurrentWaterLevel() > 0) {
                    g2d.setColor(Color.BLUE);
                } else g2d.setColor(Color.BLACK);

                g2d.setStroke(new BasicStroke(3));
                g2d.drawLine(pipeView.getStartPoint().x, pipeView.getStartPoint().y, pipeView.getEndPoint().x, pipeView.getEndPoint().y);

            } else {
                System.out.println(pipeView.getField().getId());
            }
        }

        Image playerImage;

        for (PumpLikeFieldView fieldView : fieldViews) {
            var player = fieldView.getField().getPlayers();
            if (player.size() != 0) {
                for (PlayerView playerView : playerViews) {
                    var p = playerView.getPlayer();
                    var center = new Point(fieldView.getBounds().x, fieldView.getBounds().y);
                    var radius = 55;
                    var theta = 2 * Math.PI / player.size();
                    var c = 1;
                    for (Player pl : player) {
                        if (pl == p) {
                            if (pl == Game.getInstance().getCurrentPlayer()) {
                                playerImage = playerView.loadPlayerImage("-current");
                            } else {
                                playerImage = playerView.loadPlayerImage("");
                            }
                            var angle = c * theta;
                            var x = (int) (center.x + radius * Math.cos(angle));
                            var y = (int) (center.y + radius * Math.sin(angle));
                            g.drawImage(playerImage, x, y, null);
                        }
                        c++;
                    }
                }
            }
        }

        for (var pipeView : pipeViews) {
            var player = pipeView.getField().getPlayers();
            if (player.size() != 0) {
                for (var playerView : playerViews) {
                    var p = playerView.getPlayer();
                    var center = new Point((pipeView.getStartPoint().x + pipeView.getEndPoint().x) / 2, (pipeView.getStartPoint().y + pipeView.getEndPoint().y) / 2);
                    for (var pl : player) {
                        if (pl == p) {
                            if (pl == Game.getInstance().getCurrentPlayer()) {
                                playerImage = playerView.loadPlayerImage("-current");
                            } else {
                                playerImage = playerView.loadPlayerImage("");
                            }
                            g.drawImage(playerImage, center.x, center.y, null);
                        }
                    }
                }
            }
        }
    }
}
