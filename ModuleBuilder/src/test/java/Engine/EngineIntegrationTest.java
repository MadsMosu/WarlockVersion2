package Engine;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import gameengine.Installer;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openide.util.Exceptions;
import services.IGamePluginService;

/**
 *
 * @author Frederik
 */
public class EngineIntegrationTest
{

    public EngineIntegrationTest()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of restored method, of class Installer.
     */
//    @Test
//    public void testStoppingAndStarting()
//    {
//        Installer instance = new Installer();
//
//        instance.restored(); // Starts a game that loads all plugins.
//        try
//        {
//            Thread.sleep(1000); // Waits for libgdx
//        }
//        catch (InterruptedException ex)
//        {
//            Exceptions.printStackTrace(ex);
//        }
//        World world = instance.gameEngine;
//        Set<IGamePluginService> gamePlugins = instance.gameEngine.entityPlugins;
//        MapSPI map = instance.gameEngine.getMap();
//        assertTrue(world.entities().size() > 0);
//        // It is expected that the dependencies such as map has added something to the game.
//        for (IGamePluginService plugin : gamePlugins)
//        {
//            plugin.stop();
//        }
//        map.unloadMap();
//        assertTrue(world.entities().isEmpty());
//
//        map.generateMap(world);
//        // It is expected that the dependencies such as map has added something to the game.
//        assertTrue(world.entities().size() > 0);
//        int size;
//        int sizeBeforeStart;
//        for (GamePluginSPI plugin : gamePlugins)
//        {
//            sizeBeforeStart = world.entities().size();
//            plugin.start(world);
//            size = world.entities().size();
//            if (size > sizeBeforeStart) // If added any entities
//            {
//                plugin.stop();
//                assertTrue(world.entities().size() == sizeBeforeStart);
//            }
//
//        }
//        map.unloadMap();
//        assertTrue(world.entities().isEmpty());
//    }

}
