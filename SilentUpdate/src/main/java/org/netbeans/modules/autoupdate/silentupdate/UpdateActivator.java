package org.netbeans.modules.autoupdate.silentupdate;

/**
 *
 */
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.openide.modules.ModuleInstall;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class UpdateActivator extends ModuleInstall
{

    private final ScheduledExecutorService exector = Executors.newScheduledThreadPool(1);

    @Override
    public void restored()
    {
        exector.scheduleAtFixedRate(doCheck, 5000, 5000, TimeUnit.MILLISECONDS);
    }

    public static final Runnable doCheck = ()
            ->
            {
                if (UpdateHandler.timeToCheck())
                {
                    UpdateHandler.checkAndHandleUpdates();
                }
    };

}
