package farbfetzen.libgdxsimplegame;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class SimpleGame extends ApplicationAdapter {

    private static final float TEXTURE_MAX_X = 800 - 64f;
    private final Vector3 touchPos = new Vector3();
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture bucketImage;
    private Texture dropImage;
    private Sound dropSound;
    private Music rainMusic;
    private Rectangle bucket;
    private Array<Rectangle> raindrops;
    private long lastDropTime;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        dropImage = new Texture("droplet.png");
        bucketImage = new Texture("bucket.png");
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

        rainMusic.setLooping(true);
        rainMusic.play();

        bucket = new Rectangle(800 / 2f - 64 / 2f, 20, 64, 64);

        raindrops = new Array<>();
        spawnRaindrop();
    }

    @Override
    public void render() {
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
                dropSound.play();
                iter.remove();
            }
        }

        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(bucketImage, bucket.x, bucket.y);
        for (final Rectangle raindrop : raindrops) {
            batch.draw(dropImage, raindrop.x, raindrop.y);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
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
