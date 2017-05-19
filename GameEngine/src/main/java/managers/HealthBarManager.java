/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import data.Entity;
import static data.EntityType.ENEMY;
import static data.EntityType.PLAYER;
import data.World;
import data.componentdata.Body;
import data.componentdata.Health;
import data.componentdata.Position;

/**
 *
 * @author Aleksander
 */
public class HealthBarManager {

    public void drawHealthBar(ShapeRenderer sr, World world, Camera camera) {
        for (Entity e : world.getEntities(PLAYER, ENEMY)) {
            float x = e.get(Position.class).getX();
            float y = e.get(Position.class).getY();
            int eHeight = e.get(Body.class).getHeight();

            sr.setColor(Color.BLACK);
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.rect(x, y + eHeight + 9, (float) (e.get(Health.class).getMaxHp() / 2), 12);
            sr.setProjectionMatrix(camera.combined);
            sr.end();

            sr.setColor(Color.RED);
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.rect(x, y + eHeight + 10, (float) (e.get(Health.class).getMaxHp() / 2), 10);
            sr.setProjectionMatrix(camera.combined);
            sr.end();

            sr.setColor(Color.GREEN);
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.rect(x, y + eHeight + 10, e.get(Health.class).getHp() / 2, 10);
            sr.setProjectionMatrix(camera.combined);
            sr.end();
        }
    }
}
