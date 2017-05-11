/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameengine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import data.Entity;
import data.GameData;

/**
 *
 * @author mads1
 */
public class Animator {

    private Texture texture;
    private Texture spellTexture;
    private float stateTime;

    private TextureRegion chStandingRight, chStandingLeft, chStandingUp, chStandingDown;

    private Animation chRunningRight, chRunningLeft, chRunningUp, chRunningDown;

    public void Animator() {

    }

    public void render(GameData gameData) {
        
        //batch.setProjectionMatrix();
//        batch.begin();
//        batch.draw(getFrame(entity), entity.getX(), entity.getY());
//        batch.end();
    }

    public void initializeSprite(Texture imageFile, GameData gameData) {
        texture = imageFile;
        int spriteHeight = 50;
        int spriteWidth = 50;
        stateTime = 0;

        chStandingRight = new TextureRegion(texture, 700, 0, spriteWidth, spriteHeight);
        chStandingLeft = new TextureRegion(texture, 300, 0, spriteWidth, spriteHeight);
        chStandingUp = new TextureRegion(texture, 0, 0, spriteWidth, spriteHeight);
        chStandingDown = new TextureRegion(texture, 1100, 0, spriteWidth, spriteHeight);

        Array<TextureRegion> frames = new Array<>();

        //run right
        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(texture, 700 + i * 50, 0, 50, 50));
        }
        chRunningRight = new Animation(0.18f, frames);
        frames.clear();

        //run left
        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(texture, 300 + i * 50, 0, 50, 50));
        }
        chRunningLeft = new Animation(0.18f, frames);
        frames.clear();

        //run down
        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(texture, i * 50, 0, 50, 50));
        }
        chRunningDown = new Animation(0.18f, frames);
        frames.clear();

        //run up
        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(texture, 1100 + i * 50, 0, 50, 50));
        }
        chRunningUp = new Animation(0.18f, frames);
        frames.clear();

    }

    public Texture getTexture() {
        return texture;
    }

    public void initializeSpell(Texture imageFile) {
        spellTexture = imageFile;
    }

    public Texture getSpellTexture() {
        return spellTexture;
    }

    public TextureRegion getFrame(Entity entity) {
        TextureRegion region = null;

        switch (entity.getMoveState()) {
            case RUNNINGRIGHT:
                region = (TextureRegion) chRunningRight.getKeyFrame(stateTime, true);
                chStandingRight = region;
                break;
            case RUNNINGLEFT:
                region = (TextureRegion) chRunningLeft.getKeyFrame(stateTime, true);
                chStandingLeft = region;
                break;
            case RUNNINGUP:
                region = (TextureRegion) chRunningUp.getKeyFrame(stateTime, true);
                chStandingUp = region;
                break;
            case RUNNINGDOWN:
                region = (TextureRegion) chRunningDown.getKeyFrame(stateTime, true);
                chStandingDown = region;
                break;
            case STANDINGRIGHT:
                region = chStandingRight;
                break;
            case STANDINGLEFT:
                region = chStandingLeft;
                break;
            case STANDINGUP:
                region = chStandingUp;
                break;
            case STANDINGDOWN:
                region = chStandingDown;
                break;
        }
        return region;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void updateStateTime(float dt) {
        stateTime += dt;

    }

}
