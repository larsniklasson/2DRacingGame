package edu.chl._2DRacingGame.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;


/**
 * Takes an array of images and set's it up to select between them
 *
 * @author Victor Christoffersson
 */
public class ScrollTable2 {

    private final Table table;

    private final ArrayList<Image> images;

    private final Skin skin;

    private TextButton nextButton;
    private TextButton prevButton;

    private Label currentLabel;
    private Label chooseTypeLabel;

    private final String chooseType;
    private int selectedImage;

    /**
     * Creates an instance of ScrollTable2 and will set up the table
     *
     * @param images an array of images to be selected from
     * @param chooseType the type of images
     */
    public ScrollTable2(ArrayList<Image> images, String chooseType){
        this.images = images;
        this.chooseType = chooseType;

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        table = new Table();

        selectedImage = images.size() / 2;

        setupTable();

    }

    private void setupTable(){
        currentLabel = new Label("Name here", skin);
        chooseTypeLabel = new Label("Choose a " + chooseType, skin);

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

        table.add(chooseTypeLabel).colspan(images.size() + 2);
        table.row();

        table.add(prevButton);

        setUpImages();

        table.add(nextButton);
        table.row();

        currentLabel.setText(images.get(selectedImage).getName());
        table.add(currentLabel).colspan(images.size() + 2);
        table.row();
    }

    private void setUpImages() {
        for(Image img : images) {

            Table temp = new Table();
            if(img.equals(images.get(selectedImage))){
                temp.add(img).width(250).height(125);
            } else {
                temp.add(img).width(200).height(100);
            }

            table.add(temp);
        }
    }

    /**
     * returns an array of images
     *
     * @return an arraylist of images
     */
    public ArrayList<Image> getImages() {
        return images;
    }

    /**
     * Returnsthe table which all of the components are drawn upon
     *
     * @return table which all components are drawn upon
     */
    public Table getTable() {
        return table;
    }

    private void previousItem(){
        if(selectedImage == 0){
            selectedImage = images.size() - 1;
        } else {
            selectedImage = selectedImage - 1;
        }
        drawTable();
    }

    private void nextItem(){
        if(selectedImage == images.size() - 1){
            selectedImage = 0;
        } else {
            selectedImage = selectedImage + 1;
        }

        drawTable();
    }

    /**
     * Returns the name of the selected image
     *
     * @return name of the selected image
     */
    public String getImageName() {
        return images.get(selectedImage).getName();
    }

}