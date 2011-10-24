package org.ned.client.view.renderer;

import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.List;
import org.ned.client.view.style.NEDStyleToolbox;


public class SimpleListCellRenderer extends ListCellRendererBase {

    public SimpleListCellRenderer() {
        super();
        if( com.sun.lwuit.Display.getInstance().isTouchScreenDevice() ) {
            getStyle().setPadding( 10, 10, 2, 2 );
            getSelectedStyle().setPadding( 10, 10 ,2 ,2 );
        }
        mTitle.setPreferredW( Display.getInstance().getDisplayWidth() );
        mTitle.setAlignment( CENTER );
    }

    public Component getListCellRendererComponent( List list, Object value, int index, boolean isSelected ) {
        mTitle.setText( value.toString() );

        if ( isSelected ) {
            setFocus( true );
            mTitle.getStyle().setFgColor( NEDStyleToolbox.WHITE );
            getStyle().setBgPainter( mSelectedPainter );
        } else {
            mTitle.getStyle().setFgColor( NEDStyleToolbox.MAIN_FONT_COLOR );
            getStyle().setBgPainter( mUnselectedPainter );
            setFocus( false );
        }
        return this;
    }
}
