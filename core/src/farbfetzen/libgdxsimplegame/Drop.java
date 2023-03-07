package farbfetzen.libgdxsimplegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Drop extends Game {

    private SpriteBatch batch;
    private BitmapFont font;
    @Setter
    private Screen currentScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        if (currentScreen != null) {
            currentScreen.dispose();
        }
    }
}
