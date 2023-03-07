package farbfetzen.libgdxsimplegame;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {

    private static final float TEXTURE_MAX_X = 800 - 64f;
    private final Drop game;
    private final Vector3 touchPos = new Vector3();
    private final OrthographicCamera camera;
    private final Texture bucketImage;
    private final Texture dropImage;
    private final Sound dropSound;
    private final Music rainMusic;
    private final Rectangle bucket;
    private final Array<Rectangle> raindrops;
    private long lastDropTime;
    private int dropsCollected;

    public GameScreen(final Drop game) {
        this.game = game;
        game.setCurrentScreen(this);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        dropImage = new Texture("droplet.png");
        bucketImage = new Texture("bucket.png");

        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        rainMusic.setLooping(true);

        bucket = new Rectangle(800 / 2f - 64 / 2f, 20, 64, 64);

        raindrops = new Array<>();
        spawnRaindrop();
    }

    @Override
    public void render(final float delta) {
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.x = touchPos.x - 64 / 2f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            bucket.x -= 200 * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            bucket.x += 200 * Gdx.graphics.getDeltaTime();
        }
        if (bucket.x < 0) {
            bucket.x = 0;
        } else if (bucket.x > 800 - 64) {
            bucket.x = TEXTURE_MAX_X;
        }

        if (TimeUtils.timeSinceNanos(lastDropTime) > 1_000_000_000) {
            spawnRaindrop();
        }

        for (final Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
            final Rectangle raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if (raindrop.y + 64 < 0) {
                iter.remove();
            } else if (raindrop.overlaps(bucket)) {
                dropsCollected++;
                dropSound.play();
                iter.remove();
            }
        }

        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
        game.getBatch().draw(bucketImage, bucket.x, bucket.y);
        for (final Rectangle raindrop : raindrops) {
            game.getBatch().draw(dropImage, raindrop.x, raindrop.y);
        }
        game.getFont().draw(game.getBatch(), "Drops collected: " + dropsCollected, 0, 480);
        game.getBatch().end();
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
        bucketImage.dispose();
        dropImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
    }

    private void spawnRaindrop() {
        final Rectangle raindrop = new Rectangle(MathUtils.random(0, TEXTURE_MAX_X), 480, 64, 64);
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }
}
