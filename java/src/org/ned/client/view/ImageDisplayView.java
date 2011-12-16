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

import com.sun.lwuit.Command;
import com.sun.lwuit.Display;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import org.ned.client.IContent;
import org.ned.client.NedResources;
import org.ned.client.command.BackImageCommand;

/**
 * @author community  this class it to be thrown
 */
public class ImageDisplayView extends NedFormBase implements ActionListener {

    private static final Command mEnlargeCommand = new Command( NedResources.ENLARGE );
    private static final Command mFitCommand = new Command( NedResources.FIT_TO_SCREEN );

    private Image img = null;
    private Label mImage;

    public ImageDisplayView(IContent content) {
        currentElement = content;
        String pictureFile = content.getMediaFile();
        setLayout( new BorderLayout() );
        setNedTitle( currentElement.getText() );
        setScrollable(true);

        FileConnection fc = null;
        InputStream is = null;
        try {
           fc = (FileConnection)Connector.open(pictureFile, Connector.READ);
           if (fc.exists()) {
               is = fc.openInputStream();
               img = Image.createImage(is);
               mImage = new Label( img.scaledWidth(Display.getInstance().getDisplayWidth()) );
               mImage.setFocusable(false);
               mImage.getStyle().setMargin( 0, 0, 0, 0 );
               mImage.getStyle().setPadding( 0, 0, 0, 0 );
               mImage.setAlignment(Label.CENTER);
               addComponent(BorderLayout.CENTER, mImage);
            }
        } catch (IOException ex) {
            ex.printStackTrace(); // TODO default pic?
        } finally {
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
        addCommand(mEnlargeCommand);
        addCommandListener(this);
        addGameKeyListener(Display.GAME_FIRE, this);
    }

    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        if( src == BackImageCommand.getInstance().getCommand() ) {
            BackImageCommand.getInstance().execute( currentElement.getParentId() );
        } else if ( src == mEnlargeCommand ) {
            mImage.setIcon(img);
            removeCommand( mEnlargeCommand );
            addCommand( mFitCommand );
        } else if ( src == mFitCommand ) {
            mImage.setIcon( img.scaledWidth( Display.getInstance().getDisplayWidth() ) );
            removeCommand( mFitCommand );
            addCommand( mEnlargeCommand );
        }
    }
}
