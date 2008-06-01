/*
 * Copyright (c) 2008 Sun Microsystems, Inc. All Rights Reserved.
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
 */

package com.sun.javafx.newt.x11;

import com.sun.javafx.newt.*;
import com.sun.opengl.impl.*;

public class X11Window extends Window {
    private static final String WINDOW_CLASS_NAME = "NewtWindow";
    // Default width and height -- will likely be re-set immediately by user
    private int width  = 100;
    private int height = 100;
    private int x=0;
    private int y=0;
    // non fullscreen dimensions ..
    private boolean fullscreen, visible;
    private int nfs_width, nfs_height, nfs_x, nfs_y;

    static {
        NativeLibLoader.loadCore();

        if (!initIDs()) {
            throw new RuntimeException("Failed to initialize jmethodIDs");
        }
    }

    public X11Window() {
    }

    public void initNative() {
        fullscreen=false;
        visible=false;
        long w = CreateWindow(getDisplayHandle(), getScreenHandle(), getScreenIndex(), visualID, x, y, width, height);
        if (w == 0 || w!=windowHandle) {
            throw new RuntimeException("Error creating window: "+w);
        }
    }

    public void setVisible(boolean visible) {
        if(this.visible!=visible) {
            this.visible=visible;
            setVisible0(getDisplayHandle(), windowHandle, visible);
        }
    }

    public void setSize(int width, int height) {
        setSize0(getDisplayHandle(), windowHandle, width, height);
    }

    public void setPosition(int x, int y) {
        setPosition0(getDisplayHandle(), windowHandle, x, y);
    }

    public boolean isVisible() {
        return visible;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean setFullscreen(boolean fullscreen) {
        if(this.fullscreen!=fullscreen) {
            int x,y,w,h;
            this.fullscreen=fullscreen;
            if(this.fullscreen) {
                x = 0; y = 0;
                w = getDisplayWidth0(getDisplayHandle(), getScreenIndex())/2;
                h = getDisplayHeight0(getDisplayHandle(), getScreenIndex())/2;
            } else {
                x = nfs_x;
                y = nfs_y;
                w = nfs_width;
                h = nfs_height;
            }
            setPosition0(getDisplayHandle(), windowHandle, x, y);
            setSize0(getDisplayHandle(), windowHandle, w, h);
        }
        return true;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void pumpMessages() {
        DispatchMessages(getDisplayHandle(), windowHandle);
    }

    public int getDisplayWidth() {
        return getDisplayWidth0(getDisplayHandle(), getScreenIndex());
    }

    public int getDisplayHeight() {
        return getDisplayHeight0(getDisplayHandle(), getScreenIndex());
    }

    //----------------------------------------------------------------------
    // Internals only
    //

    private static native boolean initIDs();
    private        native long CreateWindow(long display, long screen, int screen_index, 
                                            long visualID, int x, int y, int width, int height);
    private        native void setVisible0(long display, long windowHandle, boolean visible);
    private        native void DispatchMessages(long display, long windowHandle);
    private        native void setSize0(long display, long windowHandle, int width, int height);
    private        native void setPosition0(long display, long windowHandle, int x, int y);
    private        native int  getDisplayWidth0(long display, int scrn_idx);
    private        native int  getDisplayHeight0(long display, int scrn_idx);

    private void sizeChanged(int newWidth, int newHeight) {
        width = newWidth;
        height = newHeight;
        if(!fullscreen) {
            nfs_width=width;
            nfs_height=height;
        }
    }

    private void positionChanged(int newX, int newY) {
        x = newX;
        y = newY;
        if(!fullscreen) {
            nfs_x=x;
            nfs_y=y;
        }
    }

    private void windowCreated(long visualID, long windowHandle) {
        this.visualID = visualID;
        this.windowHandle = windowHandle;
    }

    private void windowClosed() {
    }

    private void windowDestroyed() {
    }

}
