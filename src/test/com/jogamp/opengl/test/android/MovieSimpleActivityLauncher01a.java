/**
 * Copyright 2012 JogAmp Community. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 * 
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 * 
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY JogAmp Community ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JogAmp Community OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of JogAmp Community.
 */
package com.jogamp.opengl.test.android;

import java.util.Arrays;
import java.util.List;

import com.jogamp.opengl.test.android.LauncherUtil.OrderedProperties;

public class MovieSimpleActivityLauncher01a extends LauncherUtil.BaseActivityLauncher {

    static String demo = "com.jogamp.opengl.test.android.MovieSimpleActivity";
    // static String[] pkgs = new String[] { "com.jogamp.common", "javax.media.opengl", "com.jogamp.opengl.test" };
    static String[] pkgs = new String[] { "com.jogamp.opengl.test" };
    
    @Override
    public void init() {
       final OrderedProperties props = getProperties();       
       props.setProperty("jnlp.mplayer.normal", "false");
       props.setProperty("jnlp.mplayer.nozoom", "true");
       props.setProperty("jnlp.mplayer.shared", "true");
       props.setProperty("jnlp.media0_url2", "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_640x360.m4v");
       props.setProperty("jnlp.media0_url1", "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4");
       props.setProperty("jnlp.media0_url0", "file:///mnt/sdcard/Movies/BigBuckBunny_320x180.mp4");
       props.setProperty("jnlp.media1_url0", "http://archive.org/download/ElephantsDream/ed_1024_512kb.mp4");
       // props.setProperty("jogamp.debug.JNILibLoader", "true");
       // props.setProperty("jogamp.debug.NativeLibrary", "true");
       // props.setProperty("jogamp.debug.NativeLibrary.Lookup", "true");
       // props.setProperty("jogamp.debug.IOUtil", "true");       
       // props.setProperty("nativewindow.debug", "all");
       props.setProperty("nativewindow.debug.GraphicsConfiguration", "true");
       // props.setProperty("jogl.debug", "all");
       // props.setProperty("jogl.debug.GLProfile", "true");
       props.setProperty("jogl.debug.GLDrawable", "true");
       props.setProperty("jogl.debug.GLContext", "true");
       props.setProperty("jogl.debug.GLSLCode", "true");
       props.setProperty("jogl.debug.CapabilitiesChooser", "true");       
       // props.setProperty("jogl.debug.GLSLState", "true");
       // props.setProperty("jogl.debug.DebugGL", "true");
       // props.setProperty("jogl.debug.TraceGL", "true");
       // props.setProperty("newt.debug", "all");
       props.setProperty("newt.debug.Window", "true");
       // props.setProperty("newt.debug.Window.MouseEvent", "true");
       // props.setProperty("newt.debug.Window.KeyEvent", "true");
       props.setProperty("jogamp.debug.IOUtil", "true");
    }
    
    @Override
    public String getActivityName() {
        return demo;
    }
    @Override
    public List<String> getPackages() {
        return Arrays.asList(pkgs);
    }
}
