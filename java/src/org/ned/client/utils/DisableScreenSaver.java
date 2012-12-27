/*******************************************************************************
* Copyright (c) 2012 Nokia Corporation
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Comarch team - initial API and implementation
*******************************************************************************/
package org.ned.client.utils;

import com.nokia.mid.ui.DeviceControl;

public class DisableScreenSaver extends Thread {
  public void run() {
    while(true) {
            DeviceControl.setLights(0, 100);
      try {
            Thread.sleep(4500);   // minimum screen saver timeout in UI is 5 seconds
      } catch (InterruptedException e) {
      }
    }
  }
}
