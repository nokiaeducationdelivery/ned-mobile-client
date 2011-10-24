package org.ned.client.view.renderer;

import com.sun.lwuit.Component;
import com.sun.lwuit.List;
import java.util.Vector;
import org.ned.client.Content;
import org.ned.client.NedResources;
import org.ned.client.XmlManager;
import org.ned.client.utils.NedIOUtils;
import org.ned.client.view.style.NEDStyleToolbox;

public class CategoryListCellRenderer extends ListCellRendererBase {

    private static final int INIT_LENGTH = 35;
    private Vector fileLists = null;
    

    public Component getListCellRendererComponent( List list, Object value, int index, boolean isSelected ) {

        Content content = (Content)value;
        mTitle.setText( content.getText() );
        Vector contents = XmlManager.getContentChilds(content.getId(), null);

        if (fileLists == null) {
            fileLists = NedIOUtils.directoryListing(((Content) content).getMediaFilePath());
        }

        int local = 0;
        int remote = 0;
        if (fileLists != null) {
            for (int i = 0; i < contents.size(); i++) {

                if (((Content) contents.elementAt(i)).isDownloaded(fileLists)) {
                    ++local;
                } else {
                    ++remote;
                }
            }
        } else {
            for (int i = 0; i < contents.size(); i++) {
                if (((Content) contents.elementAt(i)).isDownloaded()) {
                    ++local;
                } else {
                    ++remote;
                }
            }
        }

        StringBuffer text =  new StringBuffer( INIT_LENGTH );
        text.append( contents.size() ).append( " ").append( NedResources.MEDIA_ITEMS )
              .append( ": " ).append( local ).append( " " ).append( NedResources.LOCAL )
                .append( ", " ).append( remote ).append( " " ).append( NedResources.REMOTE );

        mQuanity.setText( text.toString() );

        if ( isSelected) {
            setFocus( true );
            mTitle.getStyle().setFgColor( NEDStyleToolbox.WHITE );
            mQuanity.getStyle().setFgColor( NEDStyleToolbox.WHITE );
            getStyle().setBgPainter( mSelectedPainter );
        } else {
            mTitle.getStyle().setFgColor( NEDStyleToolbox.MAIN_FONT_COLOR );
            mQuanity.getStyle().setFgColor( NEDStyleToolbox.MAIN_FONT_COLOR );
            getStyle().setBgPainter( mUnselectedPainter );
            setFocus( false );
        }
        return this;
    }
}
