package org.ned.client.lwuitExtended;

import com.sun.lwuit.html.DocumentInfo;
import com.sun.lwuit.html.DocumentRequestHandler;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import org.ned.client.NedConsts;
import org.ned.client.NedMidlet;


public class NedRequestHandler implements DocumentRequestHandler {

    public InputStream resourceRequested(DocumentInfo docInfo) {

        String fileName = docInfo.getUrl();
        FileConnection fc = null;
        InputStream is = null;
        try {
           String path = NedMidlet.getSettingsManager().getLibraryManager().getCurrentLibrary().getDirUri()
                       + "/"
                       + NedConsts.NedLocalConst.VIDEOSDIR
                       + fileName;
            fc = (FileConnection)Connector.open(path, Connector.READ);
            if (fc.exists()) {
                is = fc.openInputStream();
            }
        }
        catch (Exception e) {
        } finally {
            try {
                fc.close();
            } catch (Exception ex) {
            }
        }
        return is;
    }
}

