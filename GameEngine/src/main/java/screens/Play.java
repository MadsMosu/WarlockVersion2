/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 *
 * @author jonaspedersen
 */
public class Play implements Screen {

    private TiledMap map;
    private IsometricTiledMapRenderer renderer;
    private OrthographicCamera camera;

    @Override
    public void show()
    {
        map = new TmxMapLoader().load("assets/warlockmap.tmx");
        renderer = new IsometricTiledMapRenderer(map, 2f);
        camera = new OrthographicCamera();
        camera.translate(1875, 50);
    }

    Vector3 getMousePosInGameWorld()
    {
        return camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }

    @Override
    public void render(float f)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.setView(camera);
        renderer.render();
        getMousePosInGameWorld();

    }

    @Override
    public void resize(int width, int height)
    {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void pause()
    {
    }

    @Override
    public void resume()
    {
    }

    @Override
    public void hide()
    {
        dispose();
    }

    @Override
    public void dispose()
    {
        map.dispose();
        renderer.dispose();
    }

}
