package farbfetzen.libgdxsimplegame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import lombok.Getter;

public class Bucket {

    private static final int SPEED = 400;
    @Getter
    private final Rectangle rect;
    private final Texture image;

    public Bucket(final Rectangle rect, final Texture image) {
        this.rect = rect;
        this.image = image;
    }

    public void update() {
        if (rect.x < 0) {
            rect.x = 0;
        } else if (rect.x > 800 - 64) {
            rect.x = GameScreen.TEXTURE_MAX_X;
        }
    }

    public void moveWithTouch(final Vector3 pos) {
        rect.x = pos.x - 64 / 2f;
    }

    public void moveWithKeyboard(final boolean right, final float delta) {
        final float deltaX = SPEED * delta;
        rect.x = right ? rect.x + deltaX : rect.x - deltaX;
    }

    public void draw(final SpriteBatch batch) {
        batch.draw(image, rect.x, rect.y);
    }

    public void dispose() {
        image.dispose();
    }
}
