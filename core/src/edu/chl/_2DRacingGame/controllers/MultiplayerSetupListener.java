package edu.chl._2DRacingGame.controllers;

import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import edu.chl._2DRacingGame.models.Player;

import java.util.List;

/**
 * @author Daniel Sunnerberg
 */
interface MultiplayerSetupListener {
    void raceReady(String roomId, WarpClient client, List<Player> players);
    void connectionError(String message);
}
