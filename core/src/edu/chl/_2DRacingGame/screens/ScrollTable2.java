package edu.chl._2DRacingGame.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;


/**
 * Created by Victor Christoffersson on 2015-05-20.
 */
public class ScrollTable2 {

    private Table table;

    private ArrayList<Image> images;

    private Skin skin;

    private TextButton nextButton;
    private TextButton prevButton;

    private Label currentLabel;

    private int selectedImage;

    public ScrollTable2(ArrayList<Image> images){
        this.images = images;

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        table = new Table();

        selectedImage = images.size() / 2;
        System.out.print(selectedImage);
        setupTable();

    }

    public void setupTable(){
        currentLabel = new Label("Name here", skin);

        nextButton = new TextButton(">", skin);
        nextButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                nextItem();
            }
        });

        prevButton = new TextButton("<", skin);
        prevButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                previousItem();
            }
        });

        drawTable();
    }

    private void drawTable() {
        table.reset();

        table.setDebug(true);

        table.add(currentLabel).colspan(images.size() + 2);
        table.row();

        table.add(prevButton);

        setUpImages();

        table.add(nextButton);
    }

    private void setUpImages() {
        for(Image img : images) {

            Table temp = new Table();
            if(img.equals(images.get(selectedImage))){
                temp.add(img).width(300).height(150);
            } else {
                temp.add(img).width(200).height(100);
            }
            temp.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    changeSelectedImage(event.getButton());
                }

            });
            table.add(temp);
        }
    }

    private void changeSelectedImage(int i) {
        selectedImage = i;
        drawTable();
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public Table getTable() {
        return table;
    }

    public void previousItem(){
        if(selectedImage == 0){
            selectedImage = images.size() - 1;
        } else {
            selectedImage = selectedImage - 1;
        }
        drawTable();
    }

    public void nextItem(){
        if(selectedImage == images.size() - 1){
            selectedImage = 0;
        } else {
            selectedImage = selectedImage + 1;
        }

        drawTable();
    }
}