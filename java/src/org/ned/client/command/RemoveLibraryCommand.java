package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.library.LibraryManager;
import org.ned.client.library.NedLibrary;
import org.ned.client.statistics.StatType;
import org.ned.client.statistics.StatisticsManager;
import org.ned.client.utils.NedIOUtils;
import org.ned.client.utils.NedXmlUtils;
import org.ned.client.view.GeneralAlert;

public class RemoveLibraryCommand extends NedCommand {
    private static RemoveLibraryCommand instance;

    public static RemoveLibraryCommand getInstance() {
        if( instance == null ) {
            instance = new RemoveLibraryCommand();
        }
        return instance;
    }

    private RemoveLibraryCommand() {
        command = new Command(NedResources.MID_REMOVE_LIB_COMMAND);
    }

    protected void doAction(Object param) {
        if ( GeneralAlert.RESULT_YES == GeneralAlert.showQuestion( NedResources.QUESTION_REMOVE_LIBRARY ) ) {
            LibraryManager manager = NedMidlet.getSettingsManager().getLibraryManager();
            int selected = manager.getSelectedIndex();
            NedLibrary library = manager.getCurrentLibrary();
            NedIOUtils.removeFile( library.getFileUri() );
            manager.removeItem( selected );
            NedMidlet.getSettingsManager().saveSettings();
            NedXmlUtils.cleanDocCache();
            StatisticsManager.logEvent( StatType.LIBRARY_REMOVED, "Id=" + library.getId()
                                                                 +";Title=" + library.getTitle()
                                                                 +";Ver=" + library.getVersion() + ";" );
        }
    }
}
