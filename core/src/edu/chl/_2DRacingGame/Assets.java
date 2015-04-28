package edu.chl._2DRacingGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Anton on 2015-04-27.
 */
public class Assets {

    public static Texture mainMenuBackground;
    public static Texture mainMenuText;
    public static Texture pauseMenu;
    public static Sound carHorn;

    public static Texture loadTexture (String file) {
        return new Texture(Gdx.files.internal(file));
    }

    public static void load(){
        mainMenuBackground = loadTexture("mainMenuBackground.jpg");
        mainMenuText = loadTexture("mainMenuText.png");
        pauseMenu = loadTexture("pauseMenu.png");
        carHorn = Gdx.audio.newSound(Gdx.files.internal("carHorn.wav"));
    }

    public static void carHorn(){
        carHorn.play(1);
    }
}
