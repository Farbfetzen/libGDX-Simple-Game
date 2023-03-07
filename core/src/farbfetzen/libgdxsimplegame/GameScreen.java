package farbfetzen.libgdxsimplegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import lombok.Getter;

public class GameScreen implements Screen {

    public static final float TEXTURE_MAX_X = 800 - 64f;
    private final Drop game;
    private final Vector3 touchPos = new Vector3();
    private final OrthographicCamera camera;
    private final Music rainMusic;
    @Getter
    private final Bucket bucket;
    private final Raindrops raindrops;

    public GameScreen(final Drop game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        rainMusic.setLooping(true);

        bucket = new Bucket(new Rectangle(800 / 2f - 64 / 2f, 20, 64, 64), new Texture("bucket.png"));
        raindrops = new Raindrops(this, new Texture("droplet.png"), Gdx.audio.newSound(Gdx.files.internal("drop.wav")));
    }

    @Override
    public void render(final float delta) {
        handleInput(delta);
        update(delta);
        draw();
    }

    @Override
    public void show() {
        rainMusic.play();
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
        bucket.dispose();
        raindrops.dispose();
        rainMusic.dispose();
    }

    private void handleInput(final float delta) {
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.moveWithTouch(touchPos);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            bucket.moveWithKeyboard(false, delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            bucket.moveWithKeyboard(true, delta);
        }
    }

    private void update(final float delta) {
        bucket.update();
        raindrops.update(delta);
    }

    private void draw() {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
        bucket.draw(game.getBatch());
        raindrops.draw(game.getBatch());
        game.getFont().draw(game.getBatch(), "Drops collected: " + raindrops.getDropsCollected(), 0, 480);
        game.getBatch().end();
    }
}
