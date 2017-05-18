/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import gameengine.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import data.Entity;
import data.GameData;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author mads1
 */
public class AnimationHandler {

    private Texture texture;
    private Animation spellAnimation;
    private float stateTime;
    private Map<Entity, Animation> anRunningRight;
    private Map<Entity, Animation> anRunningLeft;
    private Map<Entity, Animation> anRunningUp;
    private Map<Entity, Animation> anRunningDown;
    private Map<Entity, TextureRegion> standing;
    private Array<TextureRegion> frames;


    public AnimationHandler() {
        anRunningRight = new ConcurrentHashMap<>();
        anRunningLeft = new ConcurrentHashMap<>();
        anRunningUp = new ConcurrentHashMap<>();
        anRunningDown = new ConcurrentHashMap<>();
        standing = new ConcurrentHashMap<>();
        frames = new Array<>();
    }


    public void initializeCharacters(Texture imageFile, Entity e, GameData gameData) {
        texture = imageFile;
        int spriteHeight = 50;
        int spriteWidth = 50;
        stateTime = 0;
       
        //run right
        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(texture, 700 + i * 50, 0, 50, 50));
        }
        anRunningRight.put(e, new Animation(0.18f, frames));
        frames.clear();

        //run left
        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(texture, 300 + i * 50, 0, 50, 50));
        }
        anRunningLeft.put(e, new Animation(0.18f, frames));
        frames.clear();

        //run down
        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(texture, i * 50, 0, 50, 50));
        }
        anRunningDown.put(e, new Animation(0.18f, frames));
        standing.put(e, frames.first());
        frames.clear();

        //run up
        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(texture, 1100 + i * 50, 0, 50, 50));
        }
        anRunningUp.put(e, new Animation(0.18f, frames));
        frames.clear();

    }

    public Texture getTexture() {
        return texture;
    }

    public void initializeSpell(Texture imageFile) {
                for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(imageFile, i * 63, 0, 63, 19));
        }
        spellAnimation = new Animation(0.18f, frames);
        frames.clear();
    }

    public TextureRegion getSpellAnimation() {
        return (TextureRegion) spellAnimation.getKeyFrame(stateTime, true);
    }

    public TextureRegion getFrame(Entity entity) {
        TextureRegion region = null;

        switch (entity.getMoveState()) {
            case RUNNINGRIGHT:
                region = (TextureRegion) anRunningRight.get(entity).getKeyFrame(stateTime, true);
                standing.get(entity).setRegion(region);
                break;
            case RUNNINGLEFT:
                region = (TextureRegion) anRunningLeft.get(entity).getKeyFrame(stateTime, true);
                standing.get(entity).setRegion(region);
                break;
            case RUNNINGUP:
                region = (TextureRegion) anRunningUp.get(entity).getKeyFrame(stateTime, true);
                standing.get(entity).setRegion(region);
                break;
            case RUNNINGDOWN:
                region = (TextureRegion) anRunningDown.get(entity).getKeyFrame(stateTime, true);
                standing.get(entity).setRegion(region);
                break;
            case STANDING:
                region = standing.get(entity);
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
