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
package com.jogamp.opengl.av;

import java.io.IOException;
import java.net.URLConnection;

import javax.media.opengl.GL;
import javax.media.opengl.GLException;

import jogamp.opengl.Debug;

import com.jogamp.opengl.util.texture.Texture;

/**
 * Lifecycle of an GLMediaPlayer:
 * <table border="1">
 *   <tr><th>action</th>                             <th>state before</th>        <th>state after</th></tr>
 *   <tr><td>{@link #initStream(URLConnection)}</td> <td>UninitializedStream</td> <td>UninitializedGL</td></tr>
 *   <tr><td>{@link #initGL(GL)}</td>                <td>UninitializedGL</td>     <td>Stopped</td></tr>
 *   <tr><td>{@link #start()}</td>                   <td>Stopped, Paused</td>     <td>Playing</td></tr>
 *   <tr><td>{@link #stop()}</td>                    <td>Playing, Paused</td>     <td>Stopped</td></tr>
 *   <tr><td>{@link #pause()}</td>                   <td>Playing</td>             <td>Paused</td></tr>
 *   <tr><td>{@link #destroy(GL)}</td>               <td>ANY</td>                 <td>UninitializedStream</td></tr>
 * </table>
 */
public interface GLMediaPlayer {
    public static final boolean DEBUG = Debug.debug("GLMediaPlayer");
    
    public enum State {
        UninitializedStream(0), UninitializedGL(1), Stopped(2), Playing(3), Paused(4); 
        
        public final int id;

        State(int id){
            this.id = id;
        }
    }
    
    public static class TextureFrame {
        public TextureFrame(Texture t) {
            texture = t;
            // stMatrix = new float[4*4];
            // ProjectFloat.makeIdentityf(stMatrix, 0);
        }
        
        public final Texture getTexture() { return texture; }
        // public final float[] getSTMatrix() { return stMatrix; }
        
        public String toString() {
            return "TextureFrame[" + texture + "]";
        }
        protected final Texture texture;
        // protected final float[] stMatrix;
    }
    
    public int getTextureCount();
    
    public int getTextureTarget();
    
    /** Sets the texture min-mag filter, defaults to {@link GL#GL_NEAREST}. */
    public void setTextureMinMagFilter(int[] minMagFilter);
    public int[] getTextureMinMagFilter();
    
    /** Sets the texture min-mag filter, defaults to {@link GL#GL_CLAMP_TO_EDGE}. */
    public void setTextureWrapST(int[] wrapST);
    public int[] getTextureWrapST();
    
    /** 
     * Sets the stream to be used. Initializes all stream related states.
     * <p>
     * UninitializedStream -> UninitializedGL
     * </p>
     * @throws IOException in case of difficulties to open or process the stream
     * @throws IllegalStateException if not invoked in state UninitializedStream 
     */
    public State initStream(URLConnection urlConn) throws IllegalStateException, IOException;

    /** 
     * Initializes all GL related resources.
     * <p>
     * UninitializedGL -> Stopped
     * </p>
     * @throws GLException in case of difficulties to initialize the GL resources
     * @throws IllegalStateException if not invoked in state UninitializedGL 
     */
    public State initGL(GL gl) throws IllegalStateException, GLException;
    
    /**
     * Releases the GL and stream resources.
     * <p>
     * <code>ANY</code> -> Uninitialized
     * </p>
     */
    public State destroy(GL gl);

    public void setPlaySpeed(float rate);

    public float getPlaySpeed();

    /**
     * Stopped/Paused -> Playing
     */
    public State start();

    /**
     * Playing -> Paused
     */
    public State pause();

    /**
     * Playing/Paused -> Stopped
     */
    public State stop();
    
    /**
     * @return the current state, either Uninitialized, Stopped, Playing, Paused
     */
    public State getState();
    
    /**
     * @return time current position in milliseconds 
     **/
    public long getCurrentPosition();

    /**
     * Allowed in state Stopped, Playing and Paused, otherwise ignored.
     * 
     * @param msec absolute desired time position in milliseconds 
     * @return time current position in milliseconds, after seeking to the desired position  
     **/
    public long seek(long msec);

    /**
     * @return the last updated texture. Not blocking. 
     */
    public TextureFrame getLastTexture();
    
    /**
     * @return the next texture, which should be rendered. May block, depending on implementation.
     * 
     * @see #addEventListener(GLMediaEventListener)
     * @see GLMediaEventListener#newFrameAvailable(GLMediaPlayer, TextureFrame)
     */
    public TextureFrame getNextTexture();
    
    public URLConnection getURLConnection();

    /**
     * <i>Warning:</i> Optional information, may not be supported by implementation.
     * @return the code of the video stream, if available 
     */
    public String getVideoCodec();

    /**
     * <i>Warning:</i> Optional information, may not be supported by implementation.
     * @return the code of the audio stream, if available 
     */
    public String getAudioCodec();

    /**
     * <i>Warning:</i> Optional information, may not be supported by implementation.
     * @return the total number of video frames
     */
    public long getTotalFrames();

    /**
     * @return total duration of stream in msec.
     */
    public long getDuration();
    
    /**
     * <i>Warning:</i> Optional information, may not be supported by implementation.
     * @return the overall bitrate of the stream.  
     */
    public long getBitrate();

    /**
     * <i>Warning:</i> Optional information, may not be supported by implementation.
     * @return the framerate of the video
     */
    public int getFramerate();

    public int getWidth();

    public int getHeight();

    public String toString();

    public void addEventListener(GLMediaEventListener l);

    public void removeEventListener(GLMediaEventListener l);

    public GLMediaEventListener[] getEventListeners();
}
