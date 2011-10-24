package org.ned.client.view;

import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Font;
import com.sun.lwuit.Label;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Border;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;
import org.ned.client.NedConsts;
import org.ned.client.NedResources;
import org.ned.client.command.SearchCommand;
import org.ned.client.view.customComponents.ClearTextField;
import org.ned.client.view.renderer.DialogTitlePainter;
import org.ned.client.view.renderer.MenuCellRenderer;


public class SearchDialog implements ActionListener{

    private static SearchDialog instance;
    private static DialogTitlePainter mDialogTitlePainer = new DialogTitlePainter();
    private static final Command okSearchCommand = new Command(NedResources.MID_OK_SEARCH_COMMAND);
    private static final Command cancelSearchCommand = new Command(NedResources.MID_CANCEL_SEARCH_COMMAND);

    private Dialog dialog;
    private ClearTextField searchTextField;
    private static String contentId = null;
    private final boolean isReversed;

    private SearchDialog() {
        isReversed = UIManager.getInstance().getLookAndFeel().isReverseSoftButtons();
        dialog = new Dialog( " " );
        dialog.getTitleComponent().setPreferredH( mDialogTitlePainer.getFontSize()
                                                + dialog.getTitleStyle().getPadding( Component.TOP )
                                                + dialog.getTitleStyle().getPadding( Component.BOTTOM )
                                                + dialog.getTitleStyle().getMargin( Component.TOP )
                                                + dialog.getTitleStyle().getMargin( Component.BOTTOM ) );
        mDialogTitlePainer.setTitle( NedResources.MID_SEARCH_TITLE );
        dialog.getTitleStyle().setBgPainter( mDialogTitlePainer );

        searchTextField = new ClearTextField();
        Label l = new Label( NedResources.SEARCH_FOR );

        dialog.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        dialog.addCommand(cancelSearchCommand);
        dialog.addCommand(okSearchCommand);
        dialog.addComponent(l);
        dialog.addComponent(searchTextField);
        dialog.getDialogStyle().setBorder(Border.createLineBorder(3, 0xFFFFFF));
        dialog.setMenuCellRenderer(new MenuCellRenderer());
        dialog.addCommandListener(this);
        if(com.sun.lwuit.Display.getInstance().isTouchScreenDevice()) {
            Style style = dialog.getSoftButtonStyle();
            style.setFont( Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE ) );
            style.setPadding( 10, 10, 0, 0 );
            dialog.setSoftButtonStyle(style);
        }
    }


    static public void show(Object params) {
        instance = new SearchDialog();
        instance.dialog.showPacked(BorderLayout.CENTER, false);
        contentId = (String)params;
    }

    public void actionPerformed(ActionEvent evt) {
        Command cmd = evt.getCommand();

        if ( cmd == okSearchCommand ) {
            String code = searchTextField.getText();
            if (code != null && !code.equals("")) {
                SearchDialog.dispose();
                String[] params = new String[2];
                params[0] = code;
                params[1] = contentId;
                SearchCommand.getInstance().execute(params);
            }
        } else if ( cmd == cancelSearchCommand ) {
            SearchDialog.dispose();
        }
    }


    public static void dispose() {
        if( instance != null  && instance.dialog != null ) {
            instance.dialog.dispose();
        }
        UIManager.getInstance().getLookAndFeel().setDefaultDialogTransitionIn(CommonTransitions.createSlide(
                CommonTransitions.SLIDE_VERTICAL, false,NedConsts.NedTransitions.TRANSITION_TIME));
        UIManager.getInstance().getLookAndFeel().setDefaultDialogTransitionOut(CommonTransitions.createSlide(
                CommonTransitions.SLIDE_VERTICAL, true, NedConsts.NedTransitions.TRANSITION_TIME));
        UIManager.getInstance().getLookAndFeel().setReverseSoftButtons( instance.isReversed );
    }
}