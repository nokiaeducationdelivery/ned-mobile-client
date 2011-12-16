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
package org.ned.client.view.customComponents;

import com.sun.lwuit.Command;
import com.sun.lwuit.Form;
import com.sun.lwuit.TextField;

public class ClearTextField extends TextField {
    
    protected Command installCommands(Command clear, Command t9) {
        Form f = getComponentForm();
        Command[] originalCommands = new Command[f.getCommandCount()];
        for (int iter = 0; iter < originalCommands.length; iter++) {
            originalCommands[iter] = f.getCommand(iter);
        }
        f.removeAllCommands();
        f.addCommand(clear);
        f.addCommand(t9);
        for (int iter = originalCommands.length - 1; iter >= 0; iter--) {
            f.addCommand(originalCommands[iter]);
        }
        return clear;
    }

    protected void removeCommands(Command clear, Command t9, Command originalClear) {
        //when context menu is shown, text field is hidden and deinitialize() is called on it, which calls removeCommands
        //this is a workaround
        if (isInitialized()) {
            super.removeCommands(clear, t9, originalClear);
        }
    }
}
