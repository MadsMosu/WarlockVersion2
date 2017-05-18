/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.componentdata;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import data.Entity;

/**
 *
 * @author jonaspedersen
 */
public class HealthBar {

    private final float min = 0;
    private float maxHealth = 100;
    private float stepSize = 1;

    private ProgressBar healthBar;

    /**
     * @param entity
     * @param height of the health bar
     */
    public HealthBar(Entity entity, int height)
    {
        healthBar = new ProgressBar(0f, 100f, 1f, false, new ProgressBarStyle());
        int width = entity.get(Body.class).getWidth();

        healthBar.getStyle().background = getColoredDrawable(width, height, Color.RED);
        healthBar.getStyle().knob = getColoredDrawable(0, height, Color.GREEN);
        healthBar.getStyle().knobBefore = getColoredDrawable(width, height, Color.GREEN);

        healthBar.setWidth(width);
        healthBar.setHeight(height);

        healthBar.setAnimateDuration(0.0f);
        healthBar.setValue(1f);

        healthBar.setAnimateDuration(0.25f);
        healthBar.setPosition(entity.get(Position.class).getX(), entity.get(Position.class).getY() + entity.get(Body.class).getHeight() + 20);
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
