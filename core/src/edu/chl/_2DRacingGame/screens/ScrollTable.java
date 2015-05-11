package edu.chl._2DRacingGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by Anton on 2015-05-10.
 */
public class ScrollTable {


    private Skin skin;
    private Table table;
    private TextButton previousItemButton;
    private TextButton nextItemButton;
    private Label itemName;
    private TextArea itemDesc;
    private Image [] imageArray;
    private int imageIndex = 0;

    public ScrollTable(Image[] images) {
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        table = new Table();
        imageArray = images;
        setupSelectorTable();
    }

    public Table getTable(){
        return table;
    }

    private void setupSelectorTable(){
        itemName = new Label("Name!", skin, "default");
        previousItemButton = new TextButton("<", skin, "default");
        nextItemButton = new TextButton(">", skin, "default");
        itemDesc = new TextArea(" Description stuff",skin,"default");

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

    private void redrawTable(){
        table.reset();
        table.padRight(50);
        table.add(previousItemButton);
        table.add(imageArray[imageIndex]).width(300).height(200);//värde för width och Height är bara för test
        table.add(nextItemButton);
        table.row();
        table.add(itemName).colspan(3).center();
        table.row();
        table.add(itemDesc).colspan(3);
        table.setBounds(0, 0, table.getMinWidth(), table.getMinHeight());
        table.debug();
    }

    private void previousItem() {
        if(imageIndex==0)
            imageIndex= imageArray.length-1;
        else
            imageIndex--;
        System.out.println("Previous!");
        redrawTable();
    }

    private void nextItem() {
        if(imageIndex==imageArray.length-1)
            imageIndex=0;
        else
            imageIndex++;
        System.out.println("NExt!");
        redrawTable();

    }


}
