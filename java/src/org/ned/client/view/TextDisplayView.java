/*******************************************************************************
* Copyright (c) 2011 Nokia Corporation
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Comarch team - initial API and implementation
*******************************************************************************/
package org.ned.client.view;

import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import org.ned.client.IContent;
import org.ned.client.command.BackImageCommand;
import com.sun.lwuit.html.HTMLComponent;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import org.ned.client.NedResources;
import org.ned.client.lwuitExtended.NedRequestHandler;

public class TextDisplayView extends NedFormBase implements ActionListener {

    private HTMLComponent comp = null;

    public TextDisplayView(IContent content) {
        currentElement = content;
        String fileName = content.getMediaFile();
        setLayout( new BorderLayout() );
        setNedTitle( currentElement.getText() );
        setScrollable(false);

        FileConnection fc = null;
        InputStream is = null;
        try {
           fc = (FileConnection)Connector.open(fileName, Connector.READ);
           if (fc.exists()) {
               is = fc.openInputStream();
                StringBuffer sb = new StringBuffer();
                int chars = 0;
                while ((chars = is.read()) != -1) {
                    sb.append((char) chars);
                }
               comp = new HTMLComponent( new NedRequestHandler() );
               addComponent(BorderLayout.CENTER, comp);
               comp.setShowImages(true);
               comp.setScrollableX(false);
               comp.setScrollableY(true);
               if( fileName.toUpperCase().endsWith("RTF")) {
                   comp.setBodyText( sb.toString() );
               } else {
                   comp.setHTML( sb.toString(), null, currentElement.getText(), true );
               }

            }
        } catch (IOException ex) {
            ex.printStackTrace();  // TODO
        } catch (OutOfMemoryError ex) {
            GeneralAlert.show(NedResources.MEMORY_OUT, GeneralAlert.WARNING);
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
        finally {
            try {
                is.close();
            } catch (Exception ex) {
            }
            try {
                fc.close();
            } catch (Exception ex) {
            }
        }

        addCommand(BackImageCommand.getInstance().getCommand());
        addCommandListener(this);
    }

    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        if (src == BackImageCommand.getInstance().getCommand()) {
            comp.cancel();
            BackImageCommand.getInstance().execute(currentElement.getParentId());
        }
    }
}
