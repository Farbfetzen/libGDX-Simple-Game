package farbfetzen.libgdxsimplegame;

import java.util.Iterator;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import lombok.Getter;

public class Raindrops {

    private static final int SPEED = 200;
    private static final int MILLIS_BETWEEN_DROPS = 1000;
    private final GameScreen screen;
    private final Array<Rectangle> rects = new Array<>();
    private long lastDropTime;
    private final Texture image;
    private final Sound sound;
    @Getter
    private int dropsCollected;

    public Raindrops(final GameScreen screen, final Texture image, final Sound sound) {
        this.screen = screen;
        this.image = image;
        this.sound = sound;
        spawnRaindrop();
    }

    public void update(final float delta) {
        if (TimeUtils.timeSinceMillis(lastDropTime) > MILLIS_BETWEEN_DROPS) {
            spawnRaindrop();
        }

        for (final Iterator<Rectangle> iter = rects.iterator(); iter.hasNext(); ) {
            final Rectangle raindrop = iter.next();
            raindrop.y -= SPEED * delta;
            if (raindrop.y + 64 < 0) {
                iter.remove();
            } else if (raindrop.overlaps(screen.getBucket().getRect())) {
                dropsCollected++;
                sound.play();
                iter.remove();
            }
        }
    }

    public void draw(final SpriteBatch batch) {
        for (final Rectangle raindrop : rects) {
            batch.draw(image, raindrop.x, raindrop.y);
        }
    }

    public void dispose() {
        image.dispose();
        sound.dispose();
    }

    private void spawnRaindrop() {
        final Rectangle raindrop = new Rectangle(MathUtils.random(0, GameScreen.TEXTURE_MAX_X), 480, 64, 64);
        rects.add(raindrop);
        lastDropTime = TimeUtils.millis();
    }
}
