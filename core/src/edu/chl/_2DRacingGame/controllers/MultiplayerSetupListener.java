package edu.chl._2DRacingGame.controllers;

import com.shephertz.app42.gaming.multiplayer.client.WarpClient;

/**
 * @author Daniel Sunnerberg
 */
public interface MultiplayerSetupListener {
    void startMultiplayerGame(WarpClient client);
}
