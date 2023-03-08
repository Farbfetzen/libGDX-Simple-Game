package farbfetzen.libgdxsimplegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen extends ScreenAdapter {

    private final Drop game;
    private final OrthographicCamera camera;

    public MainMenuScreen(final Drop game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render(final float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
        game.getFont().draw(game.getBatch(), "Welcome to Drop!", 100, 150);
        game.getFont().draw(game.getBatch(), "Tap anywhere to begin.", 100, 100);
        game.getBatch().end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }
}
