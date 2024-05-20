package org.bme.mit.iet.view.playerview;

import org.bme.mit.iet.player.Player;
import org.bme.mit.iet.player.Saboteur;

import java.awt.*;
import java.io.Serializable;

/**
 * Egy játékos figurát megjelenítő osztály.
 */
public abstract class PlayerView implements Serializable {
    /**
     * A PlayerView-hoz tartozo jatekos
     */
    protected final transient Player player;

    public PlayerView(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public abstract Image loadPlayerImage(String name);
}
