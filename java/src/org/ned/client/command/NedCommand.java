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
