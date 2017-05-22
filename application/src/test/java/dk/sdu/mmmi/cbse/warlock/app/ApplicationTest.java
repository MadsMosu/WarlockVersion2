package dk.sdu.mmmi.cbse.warlock.app;

import services.IGamePluginService;
import java.io.IOException;
import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import junit.framework.Test;
import static junit.framework.TestCase.assertTrue;
import org.netbeans.junit.NbModuleSuite;
import org.netbeans.junit.NbTestCase;
//import org.netbeans.modules.autoupdate.silentupdate.UpdateHandler;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.w3c.dom.Document;
import services.IEntityProcessingService;


public class ApplicationTest extends NbTestCase {

    private final Lookup lookup = Lookup.getDefault();
    private String filepath = "C:/Users/aleksander/Documents/NetBeansProjects/WarlockVersion2/ModuleBuilder/target/netbeans_site/updates.xml";
    private final String remPlayerFilePath = "C:/Users/Aleksander/Documents/NetBeansProjects/WarlockVersion2/application/src/test/resources/RemovedPlayer/updates.xml";
    private final String playerFilePath = "C:/Users/Aleksander/Documents/NetBeansProjects/WarlockVersion2/application/src/test/resources/Player/updates.xml";
    private Document doc;
    private Lookup.Result<IGamePluginService> lookupResult;
    private List<IGamePluginService> entityList; 
    private List<IEntityProcessingService> processorList;

    public static Test suite() {
        return NbModuleSuite.createConfiguration(ApplicationTest.class).
                gui(false).
                failOnMessage(Level.WARNING). // works at least in RELEASE71
                failOnException(Level.INFO).
                enableClasspathModules(false).
                clusters(".*").
                suite(); // RELEASE71+, else use NbModuleSuite.create(NbModuleSuite.createConfiguration(...))
    }

    public ApplicationTest(String n) {
        super(n);
    }

    public void WaitForUpdate() throws InterruptedException {

        //UpdateHandler.checkAndHandleUpdates();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }

        entityList.clear();

        entityList.addAll(lookup.lookupAll(IGamePluginService.class));
        processorList.clear();

        processorList.addAll(lookup.lookupAll(IEntityProcessingService.class));
    }
    
    
    public void testApplication() throws InterruptedException, IOException {
//        entityList = new CopyOnWriteArrayList<>();
//        processorList = new CopyOnWriteArrayList<>();
//        WaitForUpdate();
//        int originalEntityList = entityList.size();
//        int originalProcessorList = processorList.size();
//        assertTrue(originalEntityList > 0);
//        assertTrue(originalProcessorList > 0);
//
//        copy(get(remPlayerFilePath), get(filepath), REPLACE_EXISTING);
//        WaitForUpdate();
//        assertTrue(entityList.size() == originalEntityList - 1);
//        assertTrue(processorList.size() == originalProcessorList - 1);
//
//        copy(get(remPlayerFilePath), get(filepath), REPLACE_EXISTING);
//        WaitForUpdate();
//        assertTrue(originalEntityList == originalEntityList);
//        assertTrue(originalProcessorList == originalProcessorList);
    }
}
