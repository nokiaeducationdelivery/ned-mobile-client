package org.ned.client.view;

import org.ned.client.view.renderer.CategoryListCellRenderer;
import com.sun.lwuit.Display;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import org.ned.client.Content;
import org.ned.client.command.BackCategoryCommand;
import org.ned.client.command.BackToMainScreenCommand;
import org.ned.client.command.BrowseCategoryCommand;
import org.ned.client.command.DeleteContentCommand;
import org.ned.client.command.DownloadAllCategoryScreenCommand;
import org.ned.client.command.HelpCommand;
import org.ned.client.command.SearchDialogCommand;
import org.ned.client.view.customComponents.NedList;

public class CategoryScreen extends NedFormBase implements ActionListener{

    private NedList mCategoryList;

    public CategoryScreen(String id) {
        super(id);
        setNedTitle( currentElement!= null ? currentElement.getText() : " " );

        mCategoryList = new NedList( listModel );
        mCategoryList.setContextMenu( new CategoryContextMenu( mCategoryList, 2 ) );
        mCategoryList.setListCellRenderer(new CategoryListCellRenderer() );
        mCategoryList.setSelectedIndex(0);
        mCategoryList.setPreferredW(Display.getInstance().getDisplayWidth());
        addComponent( mCategoryList );

        addCommand( BackCategoryCommand.getInstance().getCommand() );
        addCommand( HelpCommand.getInstance().getCommand() );
        addCommand( DeleteContentCommand.getInstance().getCommand() );
        addCommand( DownloadAllCategoryScreenCommand.getInstance().getCommand() );
        addCommand( SearchDialogCommand.getInstance().getCommand() );

        mCategoryList.addActionListener(this);
        addGameKeyListener(Display.GAME_LEFT, this);
        addGameKeyListener(Display.GAME_RIGHT, this);
        addCommandListener(this);
    }

    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();

        if ( src == BackCategoryCommand.getInstance().getCommand()
                || evt.getKeyEvent() == Display.GAME_LEFT ) {
            if ( currentElement!= null  ) {
                BackCategoryCommand.getInstance().execute( currentElement.getParentId() );
            } else {
                BackToMainScreenCommand.getInstance().execute( null );
            }
        } else if ( src instanceof List || evt.getKeyEvent() == Display.GAME_RIGHT ) {
            Content content = (Content) mCategoryList.getSelectedItem();
            if ( content != null) {
                BrowseCategoryCommand.getInstance().execute( content.getId() );
            }
        } else if ( src == DeleteContentCommand.getInstance().getCommand() ) {
            DeleteContentCommand.getInstance().execute(mCategoryList);
        } else if ( src == DownloadAllCategoryScreenCommand.getInstance().getCommand() ) {
            DownloadAllCategoryScreenCommand.getInstance().execute(listModel);
        } else if ( src == SearchDialogCommand.getInstance().getCommand() ) {
            Content content = (Content) mCategoryList.getSelectedItem();
            if ( content != null ) {
                SearchDialogCommand.getInstance().execute(content.getId());
            }
        } else if (src == showFreeMem) {
            GeneralAlert.show( String.valueOf(Runtime.getRuntime().freeMemory()), GeneralAlert.INFO );
        } else if ( src == HelpCommand.getInstance().getCommand() ) {
            Object[] params = {this.getClass(), currentElement.getId() };
            HelpCommand.getInstance().execute( params );
        }
    }
}
