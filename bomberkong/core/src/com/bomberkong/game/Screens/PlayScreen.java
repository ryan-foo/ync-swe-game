package com.bomberkong.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bomberkong.game.BomberKong;

import static com.badlogic.gdx.Gdx.*;

public class PlayScreen implements Screen {

    private BomberKong game;
    Texture texture;
    private OrthographicCamera gamecam;
    private Viewport gamePort;

    public PlayScreen(BomberKong game) {
        this.game = game;
        texture = new Texture("badlogic.jpg");
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(BomberKong.V_WIDTH, BomberKong.V_HEIGHT, gamecam);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        game.batch.draw(texture, 0, 0);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
