/*******************************************************************************
 * Copyright (c) 2011 Nokia Corporation
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Comarch team - initial API and implementation
 *******************************************************************************/
package org.ned.client.command;

import org.ned.client.NedResources;
import org.ned.client.view.WaitingScreen;

public abstract class NedCommandAsync extends NedCommand implements Runnable {

    protected AsyncCompletedCallback callback;
    protected Object param;
    private boolean showConnectingDialog;

    public void beginAsync(Object aParam, AsyncCompletedCallback aCallback, boolean showConnectingDialog) {
        this.param = aParam;
        this.callback = aCallback;
        this.showConnectingDialog = showConnectingDialog;
        if (this instanceof Runnable) {
            Thread t = new Thread((Runnable) this);
            t.start();
        }
    }

    public void run() {
        try {
            tryShowConnecting();
            execute(param);
            if (callback != null) {
                callback.onSuccess();
            }
        } catch (Exception ex) {
            if (callback != null) {
                callback.onFailure(ex.getMessage());
            }
        } finally {
            tryDisposeConnecting();
        }
    }

    private void tryShowConnecting() {
        if (showConnectingDialog) {
            WaitingScreen.show(NedResources.CONNECTING);
        }
    }

    private void tryDisposeConnecting() {
        if (showConnectingDialog) {
            WaitingScreen.dispose();
        }
    }

    protected static class AsyncException extends RuntimeException {

        public AsyncException(String message) {
            super(message);
        }
    }
}
