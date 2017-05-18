/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import data.Entity;
import data.componentdata.Body;
import data.componentdata.Health;
import data.componentdata.Position;

/**
 *
 * @author jonaspedersen
 */
public class HealthBarManager {

    private final float min = 0;
    private float maxHealth = 100;
    private float stepSize = 1;

    private ProgressBar healthBar;

    /**
     * @param entity
     * @param height of the health bar
     */
    public HealthBarManager(Entity entity, int height)
    {
        Position p = entity.get(Position.class);
        Body b = entity.get(Body.class);
        healthBar = new ProgressBar(0f, 100f, 1f, false, new ProgressBarStyle());
        int width = b.getWidth();

        healthBar.getStyle().background = getColoredDrawable(width, height, Color.RED);
        healthBar.getStyle().knob = getColoredDrawable(5, height, Color.GREEN);
        healthBar.getStyle().knobBefore = getColoredDrawable(width, height, Color.GREEN);

        healthBar.setWidth(width);
        healthBar.setHeight(height);
        healthBar.setValue(entity.get(Health.class).getHp());

        healthBar.setAnimateDuration(0.25f);
        healthBar.setPosition(p.getX(), p.getY() + b.getHeight() + 20);
    }

    public ProgressBar getHealthBar()
    {
        return this.healthBar;
    }

    private Drawable getColoredDrawable(int width, int height, Color color)
    {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();

        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

        pixmap.dispose();

        return drawable;
    }

}