package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

/**
 * A class creating the
 * @author Anton Ingvarsson
 */
class ScrollTable {


    final private Skin skin;
    final private Table table;
    private TextButton previousItemButton;
    private TextButton nextItemButton;
    private Label itemName;
    final private String s;
    final private ArrayList <Image> imageArray;
    private int imageIndex = 0;

    /**
     *
     * @param images     The list of images used
     * @param typeOfSelector    the name of item to choose from
     */
    public ScrollTable(ArrayList <Image> images, String typeOfSelector) {
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        table = new Table();
        imageArray = images;
        this.s = "Choose a " + typeOfSelector;
        setupSelectorTable();
    }

    /**
     *
     * @return the table contaning the different components
     */
    public Table getTable(){
        return table;
    }

    /**
     * @return the name of the image currently displayed
     */
    public String getImageName(){return imageArray.get(imageIndex).getName();}

    /**
     * Sets up the table with buttons, labels and draws the table
     */
    private void setupSelectorTable(){
        itemName = new Label(s, skin, "default");
        previousItemButton = new TextButton("<", skin, "default");
        nextItemButton = new TextButton(">", skin, "default");

        previousItemButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                previousItem();
            }
        });

        nextItemButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nextItem();
            }
        });
        redrawTable();

    }

    /**
     * Redraws the table with the current image
     */
    private void redrawTable(){
        table.reset();
        table.add(itemName).colspan(3).center();
        table.row();
        table.padRight(50);
        table.add(previousItemButton);
        table.add(imageArray.get(imageIndex)).width(300).height(200);//värde för width och Height är bara för test
        table.add(nextItemButton);
        table.row();
        table.row();
        table.setBounds(0, 0, table.getMinWidth(), table.getMinHeight());
    }

    private void previousItem() {
        decreaseImageIndex();
        redrawTable();
    }

    private void nextItem() {
        increaseImageIndex();
        redrawTable();

    }

    private void increaseImageIndex(){
        if(imageIndex==imageArray.size()-1)
            imageIndex=0;
        else
            imageIndex++;
    }

    private void decreaseImageIndex(){
        if(imageIndex==0)
            imageIndex= imageArray.size()-1;
        else
            imageIndex--;
    }
}
