/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enemy;

import services.IGamePluginService;
import org.openide.modules.ModuleInstall;

/**
 *
 * @author frede
 */
public class Installer extends ModuleInstall
{
    public static IGamePluginService Plugin = null;

    @Override
    public void restored()
    {
        // TODO
    }

    @Override
    public void uninstalled()
    {
        if (Plugin != null)
        {
            Plugin.stop();
        }
    }
}
