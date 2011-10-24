package org.ned.client.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import org.kxml2.io.KXmlParser;
import org.kxml2.io.KXmlSerializer;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParserException;
import org.ned.client.NedConsts;

public class NedXmlUtils {

    private static String currentFile = null;
    private static byte[] buffer = null;
    private static Document docCache = null;

    public static Document getDocFile(String file) {
        Document doc = null;
        KXmlParser parser = null;
        FileConnection fc = null;
        InputStream in = null;
        InputStreamReader is = null;

        if(docCache != null && currentFile != null && file.equals(currentFile))
        {
            return docCache;
        }

        try {
            if ( currentFile == null || !currentFile.equals(file) || buffer == null ) {
                buffer = null;
                buffer = NedIOUtils.bytesFromFile(file);
                if(buffer == null) {
                 return doc;
                }
                currentFile = file;
            }
            in = new ByteArrayInputStream(buffer);
            is = new InputStreamReader(in, "UTF-8");
            parser = new KXmlParser();
            try {
                parser.setInput(is);
                doc = new Document();
                doc.parse(parser);
            } catch (XmlPullParserException ex) {
                ex.printStackTrace();
                doc = null;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            doc = null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (in != null) {
                    in.close();
                }
                if (fc != null) {
                    fc.close();
                }
                parser = null;
            } catch (IOException ex) {
                ex.printStackTrace();
                doc = null;
            }
        }
        docCache = doc;
        if(doc != null)
        {
            buffer = null;
        }
        return doc;
    }

    public static Element findElement(Document doc, String tagName, String tagValue) {
        Element rootElement = doc.getRootElement();
        Element result = null;

        for (int i = 0; i < rootElement.getChildCount();
                ++i) {
            if (rootElement.getType(i) != Node.ELEMENT) {
                continue;
            }
            Element element = rootElement.getElement(i);

            if (element.getName().equals(tagName)) {
                String text = element.getText(0);
                if ((text != null) && (text.equals(tagValue))) {
                    result = element;
                    break;
                }
            }
        }
        return result;
    }

    public static boolean entryExists(Document doc, String id) {
        boolean entryFound = false;

        Element rootElement = doc.getRootElement();
        String currentId = null;

        for (int i = 0; i
                < rootElement.getChildCount(); i++) {
            if (rootElement.getType(i) != Node.ELEMENT) {
                continue;
            }

            Element element = rootElement.getElement(i);

            if (element.getName().equals("entry")) {
                currentId = element.getAttributeValue("", "id");
                if ((currentId != null) && (currentId.equals(id))) {
                    entryFound = true;
                    break;
                }
            }
        }
        return entryFound;
    }

    public synchronized static void writeXmlFile(String file, Document doc) {

        FileConnection fc = null;
        KXmlSerializer serializer = new KXmlSerializer();
        OutputStream os = null;
        try {
            NedIOUtils.removeFile(file);
            fc = (FileConnection) Connector.open(file, Connector.READ_WRITE);
            if (!fc.exists()) {
                fc.create();
            }
            fc.setWritable(true);

            os = fc.openOutputStream(0);
            serializer.setOutput(os, "UTF-8");
            doc.write(serializer);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (fc != null) {
                    fc.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static Element findElement(Document doc, String tagName, String currentMediaId, int statType) {
        Element retval = null;

        for (int i = 0; i
                < doc.getChildCount(); i++) {
            if (doc.getType(i) == Node.ELEMENT) {
                Element statsElement = doc.getElement(i);
                if (statsElement.getAttributeValue("", NedConsts.NedXmlAttribute.MEDIA_ID).equals(currentMediaId)
                        && statsElement.getAttributeValue("", NedConsts.NedXmlAttribute.STAT_TYPE).equals(String.valueOf(statType))) {
                    retval = statsElement;
                }
            }
        }
        return retval;
    }

    public static void cleanDocCache() {
        docCache = null;
    }
}
