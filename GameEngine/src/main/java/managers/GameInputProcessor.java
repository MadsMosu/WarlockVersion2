package managers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

import data.GameData;
import data.GameKeys;
import net.java.games.input.Component;

public class GameInputProcessor extends InputAdapter {

    private final GameData gameData;

    public GameInputProcessor(GameData gameData) {
        this.gameData = gameData;
    }

    public boolean keyDown(int k) {
        if (k == Keys.NUM_1) {
            gameData.getKeys().setKey(GameKeys.NUM_1, true);
        }
        if (k == Keys.NUM_2) {
            gameData.getKeys().setKey(GameKeys.NUM_2, true);
        }
        if (k == Keys.NUM_3) {
            gameData.getKeys().setKey(GameKeys.NUM_3, true);
        }
        if (k == Keys.NUM_4) {
            gameData.getKeys().setKey(GameKeys.NUM_4, true);
        }
        if (k == Keys.Q) {
            gameData.getKeys().setKey(GameKeys.Q, true);
        }

        return true;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Buttons.RIGHT) {
            gameData.getKeys().setKey(GameKeys.RIGHT_MOUSE, true);
            gameData.setScreenX(screenX);
            gameData.setScreenY(screenY);
        }
        if (button == Buttons.LEFT) {
            gameData.getKeys().setKey(GameKeys.LEFT_MOUSE, true);
            gameData.setScreenX(screenX);
            gameData.setScreenY(screenY);
        }
        return true;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Buttons.RIGHT) {
            gameData.getKeys().setKey(GameKeys.RIGHT_MOUSE, false);
        }
        if (button == Buttons.LEFT) {
            gameData.getKeys().setKey(GameKeys.LEFT_MOUSE, false);
        }
        return false;
    }

    public boolean keyUp(int k) {
        if (k == Keys.NUM_1) {
            gameData.getKeys().setKey(GameKeys.NUM_1, false);
        }
        if (k == Keys.NUM_2) {
            gameData.getKeys().setKey(GameKeys.NUM_2, false);
        }
        if (k == Keys.NUM_3) {
            gameData.getKeys().setKey(GameKeys.NUM_3, false);
        }
        if (k == Keys.NUM_4) {
            gameData.getKeys().setKey(GameKeys.NUM_4, false);
        }
        if (k == Keys.Q) {
            gameData.getKeys().setKey(GameKeys.Q, false);
        }
        if (k == Buttons.RIGHT) {
            gameData.getKeys().setKey(GameKeys.RIGHT_MOUSE, false);
        }
        if (k == Buttons.LEFT) {
            gameData.getKeys().setKey(GameKeys.LEFT_MOUSE, false);
        }
        return true;
    }

}
