/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AssetManager
{
    private static Map<String, Asset> assets = new ConcurrentHashMap<>();

    public static Collection<Asset> assets()
    {
        return assets.values();
    }

    public static void createAsset(String path, boolean repeatImage)
    {
        Asset a = null;
        try
        {
            a = new Asset(new File(path).getCanonicalPath().replace("\\", "/"), repeatImage);
            assets.put(path, a);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public static Asset getAssets(String path)
    {
        return assets.get(path);
    }

    public static void removeAssets(String path)
    {
        assets.remove(path);
    }
}
