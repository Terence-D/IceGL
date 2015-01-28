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

import android.graphics.PointF;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * base object for anything that gets drawn to the screen as part of the framework
 */
public class GLObject
{
    private final float DEFAULT_SIZE = 48.0f;

    //object position and size
    protected RectGL dimensions = new RectGL(0, 0, DEFAULT_SIZE, DEFAULT_SIZE);

    //Geometric variables
    protected float vertices[]; //vertices
    protected short indices[]; //indices
    protected float uvs[]; //texture data
    protected FloatBuffer vertexBuffer;
    protected ShortBuffer drawListBuffer;
    protected FloatBuffer uvBuffer;
    protected ByteBuffer bb;

    protected float scale = 1.0f;
    protected PointF translation = new PointF(0f, 0f);

    //Can be overridden to create "clones" for use in tiles
    protected int arraySize = 1;

    protected RectGL textDim = new RectGL(0, 0, 0, 0); //0 texture dimensions

    protected float scaledWidth = 1;
    protected float scaledHeight = 1;

    protected int textureID = 0;

    // How many bytes per float
    protected final int BYTES_PER_FLOAT = 4;

    /**
     * Default constructor that builds the various matrix arrays
     */
    public GLObject()
    {
        buildArrays();
    }

    /**
     * Set where we start grabbing from the texture
     * @param textureU where to horizontally start drawing from in the atlas
     * @param textureV where to vertically start drawing from in the atlas
     */
    public void setTexture (float textureU, float textureV)
    {
        if (textureU >= 0 && textureV >= 0)
        {
            textDim.left = textureU;
            textDim.bottom = textureV;
        }
    }

    /**
     * What percentage, between 0 and 1, we will use of the texture
     * @param textureUOffset width of the section to draw from
     * @param textureVOffset height of the section to draw from
     */
    public void setTextureOffset (float textureUOffset, float textureVOffset)
    {
        if (textureUOffset >= 0 && textureVOffset >= 0)
        {
            textDim.width = textureUOffset;
            textDim.height = textureVOffset;
        }
    }

    /**
     * Set how wide to draw our object is on the screen
     * @param width new width value
     */
    public void setWidth(float width)
    {
        if (width >= 0)
        {
            dimensions.width = width;
            setScale(scale);
        }
    }

    /**
     * Set the height of our object on the screen
     * @param height new height value
     */
    public void setHeight(float height)
    {
        if (height >= 0)
        {
            dimensions.height = height;
            setScale(scale);
        }
    }

    /**
     * Returns the height of the object
     * @return object height
     */
    public float getHeight() {
        return dimensions.height;
    }

    /**
     * Return the width of the object
     * @return object width
     */
    public float getWidth() {
        return dimensions.width;
    }

    /**
     * Return the bottom edge position of the object
     * @return bottom edge of object
     */
    public float getBottom()
    {
        return dimensions.bottom;
    }

    /**
     * Return the left edge position of the object
     * @return left edge of object
     */
    public float getLeft()
    {
        return dimensions.left;
    }

    /**
     * When overridden, the object may in fact be several cloned objects.  this returns the count of the clones
     * @return how many clones of the object we are drawing.  Usually this should return 1
     */
    protected int getCloneCount() {
        return arraySize;
    }

    /**
     * Build the various matrix arrays required to draw our object
     */
    protected void buildArrays()
    {
        //build our opengl array objects
        vertices = new float[arraySize * BYTES_PER_FLOAT * 3]; // 3 vertices (x,y,z) per GLObject
        indices = new short[arraySize * 6]; // 6 indicies per object
        uvs = new float[arraySize * BYTES_PER_FLOAT * 2]; // 2 is the number of values (u, and v) per GLObject
    }

    /**
     * Set the left edge position of the object
     * @param posX left edge position
     */
    public void setLeft(float posX) {
        dimensions.left = posX / scaledWidth;
    }

    /**
     * Set the bottom edge position of the object
     * @param posY bottom edge position
     */
    public void setBottom(float posY) {
        dimensions.bottom = posY / scaledHeight;
    }

    /**
     * Adjust the scaling of the object
     * @param scale scale ratio to use
     */
    public void setScale(float scale)
    {
            this.scale = scale;
//        scaledWidth = dimensions.width * scale;
//        scaledHeight = dimensions.height * scale;
    }

    /**
     * Set the texture we are attaching our object to
     * @param ID the ID of the texture we are using
     */
    public void setTextureID(int ID)
    {
        textureID = ID;
    }

    /**
     * Adjust the position where we begin drawing the object on the screen
     * @param deltaX how far to adjust on the horizontal axis
     * @param deltaY how far to adjust on the vertical axis
     */
    public void setTranslation(float deltaX, float deltaY)
    {
        // Update our location.
        translation.x += deltaX;
        translation.y += deltaY;
    }

    /**
     * Return the adjusted drawing position
     * @return how far from 0,0 we begin drawing from
     */
    public PointF getTranslation()
    {
        return translation;
    }

    /**
     * Build our vertices
     * @param cloneID which cloned object we are adjusting
     * @throws IllegalArgumentException
     */
    protected void assignVerticesObject(int cloneID)
    {
        if (cloneID >= getCloneCount())
            throw new IllegalArgumentException("clone ID " + cloneID + " is out of bounds.");

        // draw sprite
        vertices[(cloneID * 12)] = (dimensions.left) * scaledWidth;
        vertices[(cloneID * 12) + 1] = (dimensions.bottom * scaledHeight) + scaledHeight + dimensions.height;
        vertices[(cloneID * 12) + 2] = 0f;

        vertices[(cloneID * 12) + 3] = vertices[(cloneID * 12)]; //dup of 0
        vertices[(cloneID * 12) + 4] = (dimensions.bottom * scaledHeight);//scaledHeight);
        vertices[(cloneID * 12) + 5] = 0f;

        vertices[(cloneID * 12) + 6] = (dimensions.left * scaledWidth) + scaledWidth + dimensions.width;
        vertices[(cloneID * 12) + 7] = vertices[(cloneID * 12) + 4]; //dup of 4
        vertices[(cloneID * 12) + 8] = 0f;

        vertices[(cloneID * 12) + 9] = vertices[(cloneID * 12) + 6]; //dup of 6
        vertices[(cloneID * 12) + 10] = vertices[(cloneID * 12) + 1]; //dup of 1
        vertices[(cloneID * 12) + 11] = 0f;

    }

    /**
     * Build our vertices for all clones
     */
    public void generateVertices()
    {
        for(int i = 0; i < getCloneCount(); i++)
        {
            assignVerticesObject(i);
        }

        buildVertexBuffer();
    }

    /**
     * Build our Indices for all clones
     */
    public void generateIndices()
    {
        // The indices for all textured quads
        int last = 0;
        for(int i = 0; i < getCloneCount(); i++)
        {
            // We need to set the new indices for the new quad
            indices[(i*6)] = (short) (last);
            indices[(i*6) + 1] = (short) (last + 1);
            indices[(i*6) + 2] = (short) (last + 2);
            indices[(i*6) + 3] = (short) (last);
            indices[(i*6) + 4] = (short) (last + 2);
            indices[(i*6) + 5] = (short) (last + 3);

            // Our indices are connected to the vertices so we need to keep them
            // in the correct order.
            // normal quad = 0,1,2,0,2,3 so the next one will be 4,5,6,4,6,7
            last = last + 4;
        }

        buildIndiciesBuffer();
    }

    /**
     * Generate the textures for all clones
     */
    public void generateTextures()
    {
        for(int i = 0; i < getCloneCount(); i++)
        {
            assignTextureObject(i, textDim);
        }

        buildTextureBuffer();
    }

    /**
     * Assign our texture to the specified clone
     * @param cloneID the clone we are working with
     * @param textDim the texture object we are using
     * @throws IllegalArgumentException
     */
    protected void assignTextureObject(int cloneID, RectGL textDim)
    {
        if (cloneID > getCloneCount())
            throw new IllegalArgumentException("Clone ID " + cloneID + " out of bounds");
        if (textDim == null)
            throw new IllegalArgumentException("texture dimensions is null");

        // Adding the UV's using the offsets
        uvs[(cloneID * 8)] = textDim.left * textDim.width;
        uvs[(cloneID * 8) + 1] = textDim.bottom * textDim.height;
        uvs[(cloneID * 8) + 2] = textDim.left * textDim.width;
        uvs[(cloneID * 8) + 3] = (textDim.bottom+1) * textDim.height;
        uvs[(cloneID * 8) + 4] = (textDim.left+1) * textDim.width;
        uvs[(cloneID * 8) + 5] = (textDim.bottom+1) * textDim.height;
        uvs[(cloneID * 8) + 6] = (textDim.left+1) * textDim.width;
        uvs[(cloneID * 8) + 7] = textDim.bottom * textDim.height;
    }

    /**
     * Build our vertex buffer object
     */
    protected void buildVertexBuffer() {
        bb = ByteBuffer.allocateDirect(vertices.length * BYTES_PER_FLOAT);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }

    /**
     * Build the byte buffer for the draw list
     */
    protected void buildIndiciesBuffer()
    {
        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);
    }

    /**
     * Build the texture buffer object
     */
    protected void buildTextureBuffer() {
        ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * BYTES_PER_FLOAT);
        bb.order(ByteOrder.nativeOrder());
        uvBuffer = bb.asFloatBuffer();
        uvBuffer.put(uvs);
        uvBuffer.position(0);
    }

    /**
     * Render our object to the screen
     * @param matrixProjectionAndView matrix representing our projection and view
     */
    public void onDraw(float[] matrixProjectionAndView)
    {
        GLES20.glUseProgram(ScreenConfiguration.imageProgram.getPositionHandle()); // specify the program to use

        //Vertexes
        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(ScreenConfiguration.imageProgram.getPositionHandle());
        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                ScreenConfiguration.imageProgram.getPositionHandle(),
                3,
                GLES20.GL_FLOAT,
                false,
                0,
                vertexBuffer
        );

        //Textures
        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(ScreenConfiguration.imageProgram.getTextureCoordinateHandle());
        // Prepare the texture coordinates
        GLES20.glVertexAttribPointer(
                ScreenConfiguration.imageProgram.getTextureCoordinateHandle(),
                2,
                GLES20.GL_FLOAT,
                false,
                0,
                uvBuffer
        );

        // Get handle to shape's transformation matrix and add our matrix
        int mtrxhandle = GLES20.glGetUniformLocation(ScreenConfiguration.imageProgram.getProgramID(), "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, matrixProjectionAndView, 0);

        // Get handle to textures locations
        int mSamplerLoc = GLES20.glGetUniformLocation (ScreenConfiguration.imageProgram.getProgramID(), "s_texture" );

        GLES20.glUniform1i ( mSamplerLoc, textureID);

        // Draw the triangles
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES,
                indices.length,
                GLES20.GL_UNSIGNED_SHORT,
                drawListBuffer
        );

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(ScreenConfiguration.imageProgram.getPositionHandle());
        GLES20.glDisableVertexAttribArray(ScreenConfiguration.imageProgram.getTextureCoordinateHandle());
    }
}
