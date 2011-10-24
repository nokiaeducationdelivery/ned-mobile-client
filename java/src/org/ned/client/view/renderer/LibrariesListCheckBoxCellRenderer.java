package org.ned.client.view.renderer;

import com.sun.lwuit.CheckBox;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.layouts.BoxLayout;
import org.ned.client.library.NedLibrary;
import org.ned.client.view.style.NEDStyleToolbox;

/**
 *
 * @author pawel.polanski
 */
public class LibrariesListCheckBoxCellRenderer extends ListCellRendererBase {

    private CheckBox mCheckBox;

    public LibrariesListCheckBoxCellRenderer() {
        super();
        if( com.sun.lwuit.Display.getInstance().isTouchScreenDevice() ) {
            getStyle().setPadding( 10, 10, 2, 2 );
            getSelectedStyle().setPadding( 10, 10 ,2 ,2 );
        }
        removeAll();
        Container cont = new Container( new BoxLayout( BoxLayout.X_AXIS ) );
        mCheckBox = new CheckBox();
        mCheckBox.getStyle().setBgTransparency( NEDStyleToolbox.TRANSPARENT );
        mCheckBox.setCellRenderer( true );
        mTitle.setFocusable( false );
        mTitle.setWidth( Display.getInstance().getDisplayWidth() );
        setFocusable( true );
        mCheckBox.setWidth( Display.getInstance().getDisplayWidth() );
        mCheckBox.getStyle().setPadding(0, 0, 10, 10);
        cont.addComponent( mCheckBox );
        cont.addComponent( mTitle );
        Label spacer = new Label( " " );
        spacer.setCellRenderer(true);
        spacer.setPreferredH(1);
        spacer.setFocusable(false);
        addComponent( cont );
        addComponent( spacer );
    }

    public Component getListCellRendererComponent( List list, Object value, int index, boolean isSelected ) {
        NedLibrary lib = (NedLibrary)value;
        if ( lib != null ) {
            mTitle.setText( lib.getTitle() );
            mCheckBox.setSelected( lib.getVisible() );

            if ( isSelected && list.hasFocus() ) {
                mTitle.getStyle().setFgColor( NEDStyleToolbox.WHITE );
                getStyle().setBgPainter( mSelectedPainter );
            } else {
                mTitle.getStyle().setFgColor( NEDStyleToolbox.MAIN_FONT_COLOR );
                getStyle().setBgPainter( mUnselectedPainter );
            }
        }
        return this;
    }
}
