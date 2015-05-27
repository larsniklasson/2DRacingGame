package edu.chl._2DRacingGame.controllers;

import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import edu.chl._2DRacingGame.models.Player;

import java.util.List;

/**
 * A class able to find multiplayer opponents.
 *
 * @author Daniel Sunnerberg
 */
interface MultiplayerRaceFinderListener {

    /**
     * Should be called when enough opponents are found and we have joined a room with them.
     *
     * @param roomId id of the joined room
     * @param client AppWarp-client for their API
     * @param players list of opponents
     */
    void raceReady(String roomId, WarpClient client, List<Player> players);

    /**
     * Called if a connection error occurs.
     *
     * @param message error details/message
     */
    void connectionError(String message);

}
