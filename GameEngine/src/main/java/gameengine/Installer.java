/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameengine;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import java.awt.Dimension;
import java.awt.Toolkit;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

    public GameEngine gameEngine;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    @Override
    public void restored()
    {
        // TODO
        LwjglApplicationConfiguration cfg
                = new LwjglApplicationConfiguration();
        cfg.title = "Warlock";
        cfg.width = (int) screenSize.getWidth();
        cfg.height = (int) screenSize.getHeight();
        cfg.useGL30 = false;
        cfg.resizable = true;

         gameEngine= new GameEngine();
        new LwjglApplication(gameEngine, cfg);
    }

}
