package org.ned.client.view;

import com.sun.lwuit.Display;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BoxLayout;
import org.ned.client.Content;
import org.ned.client.NedMidlet;
import org.ned.client.command.BackCatalogCommand;
import org.ned.client.command.BrowseCatalogCommand;
import org.ned.client.command.DeleteContentCommand;
import org.ned.client.command.DownloadAllCatalogScreenCommand;
import org.ned.client.command.HelpCommand;
import org.ned.client.command.SearchDialogCommand;
import org.ned.client.command.UpdateLibraryCommand;
import org.ned.client.view.customComponents.NedList;
import org.ned.client.view.renderer.CatalogListCellRenderer;

public class CatalogScreen extends NedFormBase implements ActionListener {

    private NedList mCatalogList;

    public CatalogScreen(String id) {
        super(id);
        setNedTitle(currentElement.getText());

        mCatalogList = new NedList(listModel);
        mCatalogList.setContextMenu( new CatalogContextMenu( mCatalogList, 2 ) );
        mCatalogList.setListCellRenderer(new CatalogListCellRenderer());
        mCatalogList.setSelectedIndex(0);
        mCatalogList.addActionListener(this);

        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        setPreferredW(Display.getInstance().getDisplayWidth());
        addComponent(mCatalogList);

        addCommand(BackCatalogCommand.getInstance().getCommand());
        addCommand(HelpCommand.getInstance().getCommand());
        addCommand(DeleteContentCommand.getInstance().getCommand());
        addCommand(DownloadAllCatalogScreenCommand.getInstance().getCommand());
        addCommand(SearchDialogCommand.getInstance().getCommand());
        addCommand(UpdateLibraryCommand.getInstance().getCommand());

        addCommandListener(this);
        addGameKeyListener(Display.GAME_LEFT, this);
        addGameKeyListener(Display.GAME_RIGHT, this);
    }

    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();

        if (src == BackCatalogCommand.getInstance().getCommand() || evt.getKeyEvent() == Display.GAME_LEFT) {
            BackCatalogCommand.getInstance().execute( currentElement.getId() );
        } else if (src == DeleteContentCommand.getInstance().getCommand()) {
            DeleteContentCommand.getInstance().execute(mCatalogList);
        } else if (src instanceof List || evt.getKeyEvent() == Display.GAME_RIGHT) {
            Content content = (Content) mCatalogList.getSelectedItem();
            if ( content != null ) {
                BrowseCatalogCommand.getInstance().execute( content.getId() );
            }
        } else if (src == DownloadAllCatalogScreenCommand.getInstance().getCommand()) {
            DownloadAllCatalogScreenCommand.getInstance().execute(listModel);
        } else if (src == SearchDialogCommand.getInstance().getCommand()) {
            Content content = (Content) mCatalogList.getSelectedItem();
            if ( content != null ) {
                SearchDialogCommand.getInstance().execute(content.getId());
            }
        } else if (src == showFreeMem) {
            GeneralAlert.show(String.valueOf(Runtime.getRuntime().freeMemory()), GeneralAlert.INFO);
        }  else if ( src == HelpCommand.getInstance().getCommand() ) {
            Object[] params = {this.getClass(), currentElement.getId() };
            HelpCommand.getInstance().execute( params );
        } else if ( src == UpdateLibraryCommand.getInstance().getCommand() ) {
            UpdateLibraryCommand.getInstance().execute( NedMidlet.getSettingsManager().getLibraryManager().getCurrentLibrary() );
        }
    }
}
