package edu.chl._2DRacingGame.controllers;

import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import edu.chl._2DRacingGame.models.Player;

import java.util.List;

/**
 * @author Daniel Sunnerberg
 */
public interface MultiplayerSetupListener {
    void raceReady(WarpClient client, List<Player> opponents);
}
