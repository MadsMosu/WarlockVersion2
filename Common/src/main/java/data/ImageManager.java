/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import com.badlogic.gdx.audio.Sound;
import data.componentdata.Image;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImageManager
{
    private static Map<String, Image> images = new ConcurrentHashMap<>();
    private static Map<SpellType, Image> spellImages = new ConcurrentHashMap<>();
    private static Map<String, Sound> sounds = new ConcurrentHashMap<>();

    public static Collection<Image> images()
    {
        return images.values();
    }

    public static void createImage(String path, boolean repeatImage)
    {
        Image image = null;
        try
        {
            image = new Image(new File(path).getCanonicalPath().replace("\\", "/"), repeatImage);
            images.put(path, image);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(1);
        }
    }
    
//    public static void createSound(String path){
//        Sound sound = null;
//         try
//        {
//            sound = (new File(path).getCanonicalPath().replace("\\", "/"));
//            sounds.put(path, sound);
//        }
//        catch (IOException ex)
//        {
//            ex.printStackTrace();
//            System.exit(1);
//        }
//    }

    public static Image getImage(String path)
    {
        return images.get(path);
    }

    public static void removeImage(String path)
    {
        images.remove(path);
    }
}
