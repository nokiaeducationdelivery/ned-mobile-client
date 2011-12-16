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

import com.sun.lwuit.Command;


public abstract class NedCommand {

    protected Command command;

    public Command getCommand() {
        return command;
    }

    public String toString() {
        return command.toString();
    }

    public void execute( Object aParam ) {
        doLog( aParam );
        doAction( aParam );
    }

    protected void doLog( Object aParam ) {
    }

    protected abstract void doAction( Object aParam );
}
