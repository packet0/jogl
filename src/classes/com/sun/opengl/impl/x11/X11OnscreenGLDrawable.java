/*
 * Copyright (c) 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN
 * MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR
 * ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR
 * DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE
 * DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF
 * SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed or intended for use
 * in the design, construction, operation or maintenance of any nuclear
 * facility.
 * 
 * Sun gratefully acknowledges that this software was originally authored
 * and developed by Kenneth Bradley Russell and Christopher John Kline.
 */

package com.sun.opengl.impl.x11;

import javax.media.opengl.*;
import com.sun.opengl.impl.*;

public class X11OnscreenGLDrawable extends X11GLDrawable {
  protected NativeWindow component;

  // Indicates whether the component (if an onscreen context) has been
  // realized. Plausibly, before the component is realized the JAWT
  // should return an error or NULL object from some of its
  // operations; this appears to be the case on Win32 but is not true
  // at least with Sun's current X11 implementation (1.4.x), which
  // crashes with no other error reported if the DrawingSurfaceInfo is
  // fetched from a locked DrawingSurface during the validation as a
  // result of calling show() on the main thread. To work around this
  // we prevent any JAWT or OpenGL operations from being done until
  // addNotify() is called on the component.
  protected boolean realized;

  public X11OnscreenGLDrawable(NativeWindow component) {
    super(null, null);
    this.component = component;
  }

  public GLContext createContext(GLContext shareWith) {
    return new X11OnscreenGLContext(this, shareWith);
  }

  public void setRealized(boolean realized) {
    this.realized = realized;
  }

  public void setSize(int width, int height) {
    component.setSize(width, height);
  }

  public int getWidth() {
    return component.getWidth();
  }

  public int getHeight() {
    return component.getHeight();
  }

  public void swapBuffers() throws GLException {
    lockToolkit();
    try {    
      boolean didLock = false;
      
      if (component.getWindowHandle() == 0) {
        if (lockSurface() == LOCK_SURFACE_NOT_READY) {
          return;
        }

        didLock = true;
      }

      GLX.glXSwapBuffers(component.getDisplayHandle(), component.getWindowHandle());

      if (didLock) {
        unlockSurface();
      }
    } finally {
      unlockToolkit();
    }
  }

  public int lockSurface() throws GLException {
    if (!realized) {
      return LOCK_SURFACE_NOT_READY;
    }
    return component.lockSurface();
  }

  public void unlockSurface() {
    return component.lockSurface();
  }
}
