package View.PlayerView;

import Player.Player;

import java.awt.*;

/**
 * Egy játékos figurát megjelenítő osztály.
 */
public abstract class PlayerView {
    /**
     * A PlayerView-hoz tartozo jatekos
     */
    protected final Player player;

    public PlayerView(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public abstract Image loadPlayerImage(String name);
}
