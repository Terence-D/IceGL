/*
 * Copyright 2015 Terence Doerksen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ca.coffeeshopstudio.icegl.gl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.IntBuffer;

/**
 * Texture used by the game
 */
public class GLTexture
{
    private float targetWidth = 0;

    private float targetHeight = 0;
    private static final int MAX_TEXTURES = 1; //per atlas we allow, for future proofing maybe

    private IntBuffer t;

    private int textureID = -1;

    private float spriteSize = -1;

    private float atlasWidth = -1;
    private float atlasHeight = -1;
    private float atlasUratio = -1;
    private float atlasVratio = -1;

    /**
     * Constructor for the texture atlas we are loading
     * @param context active activity context
     * @param atlasName file name of the atlas stored under drawable
     * @param spriteSize ideal size of each sprite
     * @param targetWidth width, in pixels, that the atlas was drawn in
     * @param targetHeight height, in pixels, that the atlas was drawn in
     */
    public GLTexture(Context context, String atlasName, float spriteSize, float targetWidth, float targetHeight)
    {
        if (context == null)
            throw new IllegalArgumentException("NULL Context passed in for texture atlas");
        if (spriteSize < 0)
            throw new IllegalArgumentException("Sprite size must be positive value");
        if (targetHeight < 0)
            throw new IllegalArgumentException("target height must be a positive value");
        if (targetWidth < 0)
            throw new IllegalArgumentException("target width must be positive value");

        this.targetHeight = targetHeight;
        this.targetWidth = targetWidth;
        this.spriteSize = spriteSize;

        t = IntBuffer.allocate(MAX_TEXTURES);
        init();
        Bitmap bmp = getBitmap(context, atlasName);
        generateTexture(bmp);
    }

    /**
     * Constructor for the texture atlas we are loading
     * @param bmp Bitmap we are using for the texture
     * @param spriteSize ideal size of each sprite
     * @param targetWidth width, in pixels, that the atlas was drawn in
     * @param targetHeight height, in pixels, that the atlas was drawn in
     */
    public GLTexture(Bitmap bmp, float spriteSize, float targetWidth, float targetHeight) {
        if (spriteSize < 0)
            throw new IllegalArgumentException("Sprite size must be positive value");
        if (targetHeight < 0)
            throw new IllegalArgumentException("target height must be a positive value");
        if (targetWidth < 0)
            throw new IllegalArgumentException("target width must be positive value");

        this.targetHeight = targetHeight;
        this.targetWidth = targetWidth;
        this.spriteSize = spriteSize;
        t = IntBuffer.allocate(MAX_TEXTURES);
        init();
        generateTexture(bmp);
    }

    /**
     * Build our bitmap and get it's resource ID
     * @param context application context
     * @param atlasName name of the atlas to load in
     * @return A valid Bitmap object
     */
    private Bitmap getBitmap(Context context, String atlasName) {
        //now create a bitmap from the drawable resource
        atlasName = "drawable/" + atlasName;
        // Retrieve our image from resources.
        int id = context.getResources().getIdentifier(atlasName, null, context.getPackageName());
        // Temporary create a bitmap
        return BitmapFactory.decodeResource(context.getResources(), id);
    }

    /**
     * Initialize our texture object
     */
    private void init()
    {
        //add our texture and initialize the buffer
        GLES20.glGenTextures(1, t);
        textureID = t.get(0);
    }

    /**
     * Regenerate our texture for the passed in bitmap object
     * @param bmp bitmap we will be loading in
     */
    public void rebuildTexture(Bitmap bmp) {
        generateTexture(bmp);
    }

    /**
     * Generate our texture for the passed in bitmap object
     * @param bmp bitmap we will be loading in
     */
    private void generateTexture(Bitmap bmp) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureID);  // Set the active texture unit to texture unit 0
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID); // Bind the texture to this unit

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        // Set wrapping mode
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

        atlasWidth = bmp.getWidth();
        atlasHeight = bmp.getHeight();

        atlasUratio = spriteSize / atlasWidth;
        atlasVratio = spriteSize / atlasHeight;

        // We are done using the bitmap so we should recycle it.
        bmp.recycle();
    }

    /**
     * The texture ID of this atlas
     * @return valid texture ID
     */
    public int getTextureID() {
        return textureID;
    }

    /**
     * How wide each sprite is, measured between 0 to 1
     * @return ratio of the sprites width
     */
    public float getUratio() {
        return atlasUratio;
    }

    /**
     * How high each sprite is, measured between 0 to 1
     * @return ratio of the sprites height
     */
    public float getVratio() {
        return atlasVratio;
    }

    /**
     * The height of the texture that we designed for
     * @return target height
     */
    public float getTargetHeight()
    {
        return targetHeight;
    }

    /**
     * The width of the texture that we designed for
     * @return target width
     */
    public float getTargetWidth()
    {
        return targetWidth;
    }
}
