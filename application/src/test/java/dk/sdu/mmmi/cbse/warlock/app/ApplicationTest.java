package dk.sdu.mmmi.cbse.warlock.app;

import services.IGamePluginService;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import junit.framework.Test;
import static junit.framework.TestCase.assertTrue;
import org.netbeans.junit.NbModuleSuite;
import org.netbeans.junit.NbTestCase;
import org.netbeans.modules.autoupdate.silentupdate.UpdateHandler;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ApplicationTest extends NbTestCase
{
    private final Lookup lookup = Lookup.getDefault();
    private String filepath = "C:/Users/aleksander/Documents/NetBeansProjects/WarlockVersion2/ModuleBuilder/target/netbeans_site/updates.xml";
    private Document doc;
    private Lookup.Result<IGamePluginService> result;
    private Set<IGamePluginService> gamePlugins; // public so it can be tested_lookup.lookupResult(GamePluginSPI.class);

    public static Test suite()
    {
        return NbModuleSuite.createConfiguration(ApplicationTest.class).
                gui(false).
                failOnMessage(Level.WARNING). // works at least in RELEASE71
                failOnException(Level.INFO).
                enableClasspathModules(false).
                clusters(".*").
                suite(); // RELEASE71+, else use NbModuleSuite.create(NbModuleSuite.createConfiguration(...))
    }

    public ApplicationTest(String n)
    {
        super(n);
    }

//    public void reset()
//    {
//        UpdateHandler.checkAndHandleUpdates();
//        try
//        {
//            Thread.sleep(500);
//        }
//        catch (InterruptedException ex)
//        {
//            Exceptions.printStackTrace(ex);
//        }
//        gamePlugins.clear();
//        gamePlugins.addAll(result.allInstances());
//    }
//
    public void testApplication()
    {
//        result = lookup.lookupResult(IGamePluginService.class);
//        gamePlugins = ConcurrentHashMap.newKeySet();
//        gamePlugins.addAll(result.allInstances());
//        //No plugins should be added before updatehandle is called.
//        assertTrue(gamePlugins.isEmpty());
//
//        reset();
//        int original = gamePlugins.size();
//        //Plugins should be added after updatehandler is called.
//        assertTrue(original > 0);
//
//        createDoc();
//        Node removedPlayer = removePlayerModule();
//        reset();
//        //Should be one less plugin, because one player is removed.
//        assertTrue(gamePlugins.size() == original - 1);
//
//        addModule(removedPlayer);
//        reset();
//        //Should be the same as before because player is added back.
//        assertTrue(gamePlugins.size() == original);
//
//    }
//
//    private void createDoc()
//    {
//        try
//        {
//            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder docBuilder;
//
//            docBuilder = docFactory.newDocumentBuilder();
//
//            doc = docBuilder.parse(filepath);
//        }
//        catch (ParserConfigurationException | SAXException | IOException ex)
//        {
//            Exceptions.printStackTrace(ex);
//        }
//    }
//
//    private void addModule(Node node)
//    {
//        Node sibling = doc.getElementsByTagName("module").item(0);
//        sibling.getParentNode().insertBefore(node, sibling);
//        updateDoc();
//    }
//
//    private void updateDoc()
//    {
//        try
//        {
//            TransformerFactory transformerFactory = TransformerFactory.newInstance();
//            Transformer transformer = transformerFactory.newTransformer();
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
//            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
//            DOMImplementation domImpl = doc.getImplementation();
//            DocumentType doctype = domImpl.createDocumentType("module_updates",
//                    "-//NetBeans//DTD Autoupdate Catalog 2.5//EN",
//                    "http://www.netbeans.org/dtds/autoupdate-catalog-2_5.dtd");
//            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
//            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
//            DOMSource source = new DOMSource(doc);
//            StreamResult result = new StreamResult(new File(filepath));
//
//            transformer.transform(source, result);
//        }
//        catch (TransformerException ex)
//        {
//            Exceptions.printStackTrace(ex);
//        }
//    }
//
//    private Node removePlayerModule()
//    {
//        Node retval = null;
//
//        NodeList list = doc.getElementsByTagName("module");
//
//        for (int i = 0; i < list.getLength(); i++)
//        {
//            Node module = list.item(i);
//            NamedNodeMap attr = module.getAttributes();
//            Node nodeAttr = attr.getNamedItem("codenamebase");
//            if (nodeAttr.getTextContent().equals("dk.sdu.mmmi.cbse.Player"))
//            {
//                retval = module.cloneNode(true);
//                module.getParentNode().removeChild(module);
//                break;
//            }
//        }
//        updateDoc();
//
//        return retval;
    }

}