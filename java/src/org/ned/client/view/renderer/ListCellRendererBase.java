package org.ned.client.view.renderer;

import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Font;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.list.ListCellRenderer;


abstract public class ListCellRendererBase extends Container implements ListCellRenderer {

    protected static ListSelectedBGPainter mSelectedPainter = new ListSelectedBGPainter();
    protected static UnselectedBGPainter mUnselectedPainter = new UnselectedBGPainter();

    protected static final Font mFontSmall = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL );
    protected static final Font mFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM );

    protected Label mTitle;
    protected Label mQuanity;
    protected Label mTransitionLabel;

    public ListCellRendererBase() {
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        mTitle = new Label( " " );
        mTitle.setCellRenderer(true);
        mTitle.getStyle().setBgTransparency(0);
        mTitle.getStyle().setFont(mFont);
        mTitle.getSelectedStyle().setFont(mFont);
        mQuanity = new Label();
        mQuanity.setCellRenderer(true);
        mQuanity.getStyle().setBgTransparency(0);
        mQuanity.getStyle().setFont(mFontSmall);
        mQuanity.getSelectedStyle().setFont(mFontSmall);
        addComponent(mTitle);
        addComponent(mQuanity);
        setCellRenderer(true);
        mTransitionLabel = new Label();
        mTransitionLabel.setCellRenderer(true);
        mTransitionLabel.setFocus(true);
        mTransitionLabel.getStyle().setMargin(0, 0, 0, 0);
        mTransitionLabel.getStyle().setBgPainter(mSelectedPainter);
    }

    public Component getListFocusComponent(List list) {
        if (list.hasFocus()) {
            return mTransitionLabel;
        } else {
            return null;
        }
    }
}