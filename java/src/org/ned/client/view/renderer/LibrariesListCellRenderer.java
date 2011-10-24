package org.ned.client.view.renderer;

import com.sun.lwuit.Component;
import com.sun.lwuit.List;
import org.ned.client.NedResources;
import org.ned.client.library.NedLibrary;
import org.ned.client.view.style.NEDStyleToolbox;

/**
 *
 * @author pawel.polanski
 */
public class LibrariesListCellRenderer extends ListCellRendererBase {

    public Component getListCellRendererComponent( List list, Object value, int index, boolean isSelected ) {
        NedLibrary lib = (NedLibrary)value;
        if ( lib != null ) {
            mTitle.setText( lib.getTitle() );
            if( lib.isDownloaded() ) {
                mQuanity.setText( " " + lib.getCatalogCount() + " " + NedResources.CATALOGS );
            } else {
                mQuanity.setText( " " + NedResources.CATALOGS_NO_UNKNOWN );
            }

            if ( isSelected ) {
                setFocus( true );
                mTitle.getStyle().setFgColor(  NEDStyleToolbox.WHITE );
                mQuanity.getStyle().setFgColor(  NEDStyleToolbox.WHITE );
                getStyle().setBgPainter( mSelectedPainter );
            } else {
                mTitle.getStyle().setFgColor( NEDStyleToolbox.MAIN_FONT_COLOR );
                mQuanity.getStyle().setFgColor(  NEDStyleToolbox.MAIN_FONT_COLOR );
                getStyle().setBgPainter( mUnselectedPainter );
                setFocus( false );
            }
        }
        return this;
    }
}
