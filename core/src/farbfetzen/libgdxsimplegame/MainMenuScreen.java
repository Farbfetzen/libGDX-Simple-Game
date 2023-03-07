package farbfetzen.libgdxsimplegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    final Drop game;
    final OrthographicCamera camera;

    public MainMenuScreen(final Drop game) {
        this.game = game;
        game.currentScreen = this;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render(final float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.draw(game.batch, "Welcome to Drop!", 100, 150);
        game.font.draw(game.batch, "Tap anywhere to begin.", 100, 100);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void show() {
        // Method is empty because it's unused in this tutorial.
    }

    @Override
    public void resize(final int width, final int height) {
        // Method is empty because it's unused in this tutorial.
    }

    @Override
    public void pause() {
        // Method is empty because it's unused in this tutorial.
    }

    @Override
    public void resume() {
        // Method is empty because it's unused in this tutorial.
    }

    @Override
    public void hide() {
        // Method is empty because it's unused in this tutorial.
    }

    @Override
    public void dispose() {
        // Method is empty because it's unused in this tutorial.
    }
}
