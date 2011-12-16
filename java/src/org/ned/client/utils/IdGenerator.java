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
package org.ned.client.utils;

import java.util.Random;

public class IdGenerator {
    private static Random rng = new Random();

    private IdGenerator(){};

    public static String getId() {
        int id = 1000000 + rng.nextInt(8999999);
        String sid = String.valueOf(id);
        return ("id" + sid);
    }
}
