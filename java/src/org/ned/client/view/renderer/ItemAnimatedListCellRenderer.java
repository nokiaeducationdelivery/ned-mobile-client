package org.ned.client.view.renderer;

import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.layouts.BoxLayout;
import java.util.Vector;
import org.ned.client.Content;
import org.ned.client.IContent;
import org.ned.client.utils.MediaTypeResolver;
import org.ned.client.utils.NedIOUtils;
import org.ned.client.view.style.NEDStyleToolbox;

/**
 *
 * @author damian.janicki
 */
public class ItemAnimatedListCellRenderer extends ListCellRendererBase{

    private final int PADDING = 5;
    private int position = 0;

    private String text = null;
    private String animatedText = "";

    private static final int ICON_FIT_SIZE = 36;
    private static final int ICON_WIDTH = 32;
    private Label mMediaType;
    private int displayW;
    private Vector fileLists = null;

    public ItemAnimatedListCellRenderer(){
        super();
        if (com.sun.lwuit.Display.getInstance().isTouchScreenDevice()) {
            getStyle().setPadding(10, 10, 2, 2);
            getSelectedStyle().setPadding(10, 10, 2, 2);
        }
        if (getPreferredH() < ICON_FIT_SIZE) {
            setPreferredH(ICON_FIT_SIZE);
        }
        displayW = Display.getInstance().getDisplayWidth();

        setLayout(new BoxLayout(BoxLayout.X_AXIS));
        setWidth(displayW);
        setFocusable(true);
        mMediaType = new Label(" ");//must set some text to render properly
        mMediaType.getStyle().setPadding(0, 0, 0, 0);
        mMediaType.getStyle().setMargin(0, 0, 0, 0);
        mMediaType.getSelectedStyle().setPadding(0, 0, 0, 0);
        mMediaType.getSelectedStyle().setMargin(0, 0, 0, 0);
        mMediaType.setAlignment(Label.LEFT);
        mMediaType.getStyle().setBgTransparency(0);
        mMediaType.setPreferredW(ICON_WIDTH);
        mMediaType.setCellRenderer(true);

        mTitle.setPreferredW(displayW - 2 * ICON_WIDTH);
        removeComponent(mTitle);

        addComponent(mMediaType);
        addComponent(mTitle);

    }

    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {

        IContent content = (IContent) value;

        mMediaType.setText("");
        mMediaType.setIcon(null);
        mMediaType.setIcon(MediaTypeResolver.getTypeIcon(content.getType()));

        if(text == null || !text.equals(content.getText())){
            text = content.getText();
            mTitle.setText(text);
        }

        if (content instanceof Content) {
            if (fileLists == null) {
                fileLists = NedIOUtils.directoryListing(((Content) content).getMediaFilePath());
            }
        }

        if (isSelected) {
            setFocus(true);
            mTitle.getStyle().setFgColor(NEDStyleToolbox.WHITE);
            getStyle().setBgPainter(mSelectedPainter);
        } else {
            setFocus(false);
            mTitle.getStyle().setFgColor(NEDStyleToolbox.MAIN_FONT_COLOR);
            getStyle().setBgPainter(mUnselectedPainter);
        }
        return this;

    }


    public void setAnimatedText() {

        int width = mTitle.getStyle().getFont().stringWidth(text);

        if (hasFocus() && (mTitle.getWidth() <= width)) {
            animatedText = text + "         ";
            if(position < animatedText.length()){
                
                String currentText = animatedText.substring(position);
                if(mTitle.getStyle().getFont().stringWidth(currentText) < mTitle.getWidth()){
                    currentText += text;
                }
                mTitle.setText(currentText);
                mTitle.setEndsWith3Points(false);
            }
        }else{
            mTitle.setEndsWith3Points(true);
        }

    }

    public void paint(Graphics g){
        setAnimatedText();
        super.paint(g);
    }

    public void resetPosition() {
        position = 0;
    }

    public void incrementPosition() {
        
        if ( position >= animatedText.length()  ) {
            resetPosition();
        }
        position += 1;
    }

}
