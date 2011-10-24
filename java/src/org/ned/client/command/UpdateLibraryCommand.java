package org.ned.client.command;

import com.sun.lwuit.Command;
import com.sun.lwuit.Display;
import org.ned.client.MotdManager;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.library.NedLibrary;
import org.ned.client.statistics.StatisticsManager;
import org.ned.client.utils.ContentNotExistException;
import org.ned.client.utils.NedConnectionUtils;
import org.ned.client.utils.NedXmlUtils;
import org.ned.client.utils.UnauthorizedLibraryUsageException;
import org.ned.client.view.CatalogScreen;
import org.ned.client.view.GeneralAlert;
import org.ned.client.view.LibraryManagerScreen;
import org.ned.client.view.LoginOnLineScreen;
import org.ned.client.view.WaitingScreen;

/**
 *
 * @author damian.janicki
 */
public class UpdateLibraryCommand extends NedCommand{

    private static UpdateLibraryCommand instance;

    public static UpdateLibraryCommand getInstance(){
        if(instance == null){
            instance = new UpdateLibraryCommand();
        }
        return instance;
    }

    public  UpdateLibraryCommand(){
        command = new Command( NedResources.CHECK_FOR_UPDATE );
    }

    protected void doAction(Object param) {
        NedLibrary lib = (NedLibrary)param;
        UpdateLibraryRunnable ulr = new UpdateLibraryRunnable(lib);

        Thread t = new Thread(ulr);
        t.start();
    }

    public class UpdateLibraryRunnable implements Runnable {

        private NedLibrary library;

        private UpdateLibraryRunnable(NedLibrary selected) {
            this.library = selected;
        }

        public void run() {
            
            try {
                WaitingScreen.show(NedResources.CONNECTING);
                NedLibrary newLibraryInfo = NedConnectionUtils.getLibraryInfo(library.getId());
                WaitingScreen.dispose();
                
                if (newLibraryInfo != null) {
                    if (NedMidlet.getSettingsManager().getAutoStatSend()) {
                        StatisticsManager.uploadStats( true );
                    }
                    MotdManager.getInstance().updateMotd();
                    if (library.getVersionInt() < newLibraryInfo.getVersionInt()) {
                        if (GeneralAlert.RESULT_YES == GeneralAlert.showQuestion(NedResources.DOWNLOAD_NEW_LIBRARY)) {
                            downloadLibrary();
                        }
                        
                    } else {
                        if(GeneralAlert.RESULT_YES == GeneralAlert.showQuestion(NedResources.LIBRARY_UPTODATE)){//TODO change text
                            downloadLibrary();
                        }
                    }
                }
            } catch ( UnauthorizedLibraryUsageException ex ) {
                WaitingScreen.dispose();//to get main view not a "Connecting..." dialog by Diaplay.getCurrent
                if ( GeneralAlert.showQuestion(NedResources.LOGIN_AGAIN ) == GeneralAlert.RESULT_YES ) {
                    new LoginOnLineScreen( LibraryManagerScreen.class ).show();
                }
            } catch (ContentNotExistException ex){
                WaitingScreen.dispose();
                GeneralAlert.show(NedResources.LIB_NOT_EXIST_ANY_MORE, GeneralAlert.WARNING);
            }
        }

        private void downloadLibrary() throws SecurityException, UnauthorizedLibraryUsageException, ContentNotExistException{
            WaitingScreen.show(NedResources.GLOBAL_CONNECTING);
            if (!NedMidlet.getInstance().getDownloadManager().getViaServlet(
                    NedMidlet.getAccountManager().getContentServletUri(), library)) {
                GeneralAlert.show(NedResources.DLM_CONNECTION_FAILED, GeneralAlert.WARNING);
            } else {
                NedXmlUtils.cleanDocCache();
                library.setCatalogCount();
                NedMidlet.getSettingsManager().resetServer();
                WaitingScreen.dispose();
                try {
                    Thread.sleep( 250 );
                } catch ( InterruptedException ex ) {
                }
                if ( Display.getInstance().getCurrent() instanceof CatalogScreen ) {//this is workaround for UpdateLibrary form CatalogScreen
                    new CatalogScreen( library.getId() ).show();
                }
            }
        }
    }
}
