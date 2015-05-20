package edu.chl._2DRacingGame.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


/**
 * Created by Victor Christoffersson on 2015-05-20.
 */
public class ScrollTable2 {

    private Table table;

    private Image[] images;

    private Skin skin;

    private TextButton nextButton;
    private TextButton prevButton;

    private Label currentLabel;

    public ScrollTable2(Image[] images){
        this.images = images;

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        table = new Table();

        tableSetup();

    }

    public void tableSetup(){
        currentLabel = new Label("Vehicles name here", skin);

        nextButton = new TextButton(">", skin);
        nextButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("next");
            }
        });

        prevButton = new TextButton("<", skin);
        prevButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("prev");
            }
        });

        drawTable();
    }

    private void drawTable() {
        table.setDebug(true);
        table.add(currentLabel).colspan(images.length + 2);
        table.row();
        table.add(prevButton);
        setUpImages();
        table.add(nextButton);
    }

    private void setUpImages() {
        for(int i = 0; i <= images.length - 1; i++){

            Table temp = new Table();
            temp.add(images[i]);
            table.add(temp);
        }
    }

    public Image[] getImages() {
        return images;
    }

    public Table getTable() {
        return table;
    }
}
