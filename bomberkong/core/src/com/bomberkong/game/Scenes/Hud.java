package com.bomberkong.game.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bomberkong.game.BomberKong;

public class Hud {
    public Stage stage;
    public Viewport viewport;

    private Integer worldTimer;
    private float timeCount;
    private Integer scoreA;
    private Integer scoreB;
    private Integer livesA;
    private Integer livesB;

    public Hud(SpriteBatch sb){
        worldTimer = 90;
        timeCount = 0;
        scoreA = 0;
        scoreB = 0;
        livesA = 0;
        livesB = 0;

        viewport = new FitViewport(BomberKong.V_WIDTH, BomberKong.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Label countdownLabel = new Label(String.format("%02d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label scoreAHeaderLabel = new Label("SCORE", new Label.LabelStyle(new BitmapFont(), Color.RED));
        Label livesAHeaderLabel = new Label("LIVES", new Label.LabelStyle(new BitmapFont(), Color.RED));
        Label scoreBHeaderLabel = new Label("SCORE", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        Label timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label livesBHeaderLabel = new Label("LIVES", new Label.LabelStyle(new BitmapFont(), Color.BLUE));

        Label livesALabel = new Label(String.format("%02d", livesA), new Label.LabelStyle(new BitmapFont(), Color.RED));
        Label scoreALabel = new Label(String.format("%03d", scoreA), new Label.LabelStyle(new BitmapFont(), Color.RED));
        Label scoreBLabel = new Label(String.format("%03d", scoreB), new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        Label livesBLabel = new Label(String.format("%02d", livesB), new Label.LabelStyle(new BitmapFont(), Color.BLUE));

        Table table = new Table();
        table.top(); // Puts table at the top of the stage
        table.setFillParent(true); // it is the size of the stage, which is the parent of the stage
        table.add(livesAHeaderLabel).expandX().padTop(2);
        table.add(scoreAHeaderLabel).expandX().padTop(2);
        table.add(timeLabel).expandX().padTop(2);
        table.add(scoreBHeaderLabel).expandX().padTop(2);
        table.add(livesBHeaderLabel).expandX().padTop(2);

        table.row();
        table.add(livesALabel);
        table.add(scoreALabel);
        table.add(countdownLabel).expandX();
        table.add(scoreBLabel);
        table.add(livesBLabel);



        stage.addActor(table);
    }
}
