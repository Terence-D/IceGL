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

package ca.coffeeshopstudio.icegl.controls;

import android.view.MotionEvent;

import ca.coffeeshopstudio.icegl.gl.GLObject;
import ca.coffeeshopstudio.icegl.gl.ScreenConfiguration;

public abstract class Control implements IControl, OnTouchListener
{
    protected GLObject[] glo;

    protected boolean enabled = true;
    protected boolean visible = true;

    //stores where in the grid we are positioning ourselves.  -1 indicates not used
    private int gridLeft = -1;
    private int gridBottom = -1;
    private int gridWidth = -1;
    private int gridHeight = -1;

    private boolean builtTexture = false; //determines if there is something to draw or not

    private float width;
    private float height;
    private float left;
    private float bottom;
    private float scale;

    private int gloCount = 1; //by default we only have 1 item to draw.  some objects such as Label may override this.

    private boolean dirty = false; //if true, will rebuild the GLObjects

    /**
     * Callback Function to allow custom event handling when touched
     */
    public OnTouchListener onTouchListener = null;

    /**
     * Default constructor
     * @param icm what we are attaching to
     * @throws IllegalArgumentException
     */
    public Control(IControlManager icm)
    {
        if (icm == null)
        {
            throw new IllegalArgumentException("no valid control manager to assign control to");
        }
        setGloCount(gloCount);
        icm.addControl(this);
    }

    /**
     * Configure the GL Objects and prepare them for drawing to our surface
     */
    protected void buildGLObjects()
    {
        setGlObjectDimensions(glo[0], left, bottom, width, height, scale);
        for (GLObject glInstance : glo)
        {
            glInstance.generateIndices();
            glInstance.generateVertices();
            glInstance.generateTextures();
        }
        builtTexture = true;
    }

    /**
     * Set the basic dimensions of the passed on GL Object
     * @param glInstance GL Object we are setting the size of
     * @param left left position of the object on the surface
     * @param bottom bottom position of the object on the surface
     * @param width how wide the object is to draw
     * @param height how high the object is to draw
     * @param scale what scale we are using for drawing
     */
    protected void setGlObjectDimensions(GLObject glInstance, float left, float bottom, float width, float height, float scale)
    {
        glInstance.setLeft(left);
        glInstance.setBottom(bottom);
        glInstance.setWidth(width);
        glInstance.setHeight(height);
        glInstance.setScale(scale);
    }

    /**
     * Simple getter to ensure we never set it < 1
     * @param gloCount how many glo's we want to create
     */
    protected void setGloCount(int gloCount)
    {
        if (gloCount > 0)
            this.gloCount = gloCount;
        glo = new GLObject[gloCount];
        for (int i=0; i < gloCount; i++)
                glo[i] = new GLObject();
    }

    /**
     * Get the precise location of the controls left most position
     * @return the exact left most position of the control
     */
    @Override
    public float getLeft()
    {
        return left;
    }

    /**
     * Get the precise location of the controls bottom most position
     * @return the exact bottom most position of the control
     */
    @Override
    public float getBottom()
    {
        return bottom;
    }

    /**
     * Get the precise width of the control
     * @return width of the control
     */
    @Override
    public float getWidth()
    {
        return width;
    }

    /**
     * Get the precise height of the control
     * @return height of the control
     */
    @Override
    public float getHeight()
    {
        return height;
    }

    /**
     * Set the Exact Width of the Control
     * @param width exact with of the control to use
     */
    @Override
    public void setWidthRaw(float width)
    {
        this.width = width;
    }

    /**
     * Set the Exact Height of the control
     * @param height exact width of the control to use
     */
    @Override
    public void setHeightRaw(float height)
    {
        this.height = height;
    }

    /**
     * Set the height of the Controls based on Grid units
     * @param height how high of the control based in Grid units
     */
    @Override
    public void setHeight(int height)
    {
        if (height < 0) height = 0;
        gridHeight = height;
        setHeightRaw(gridHeight * ScreenConfiguration.getTileHeight());
    }

    /**
     * Set the Width of the Controls based on Grid units
     * @param width how wide of the control based in Grid units
     */
    @Override
    public void setWidth(int width)
    {
        if (width < 0) width = 0;
        gridWidth = width;
        setWidthRaw(gridWidth * ScreenConfiguration.getTileWidth());
    }

    /**
     * Set the Left position of the Control based on Grid units
     * @param left left position based on Grid units
     */
    @Override
    public void setLeft(int left)
    {
        gridLeft = left;
        setLeftRaw(gridLeft * ScreenConfiguration.getTileWidth());
    }

    /**
     * Set the bottom position of the Control based on Grid units
     * @param bottom bottom position based on Grid units
     */
    @Override
    public void setBottom(int bottom)
    {
        gridBottom = bottom;
        setBottomRaw(gridBottom * ScreenConfiguration.getTileHeight());
    }

    /**
     * Set the Exact Left position of the Control
     * @param left exact left position
     */
    @Override
    public void setLeftRaw(float left)
    {
        this.left = left;
    }

    /**
     * Set the Exact Bottom position of the Control
     * @param bottom exact bottom position
     */
    @Override
    public void setBottomRaw(float bottom)
    {
        this.bottom = bottom;
    }

    /**
     * Adjust the scale of the control
     * @param scale What to multiply the scale by
     */
    @Override
    public void setScale(float scale)
    {
        this.scale = scale;
    }

    /**
     * Assign our control the specified texture
     * @param textureID index of the texture to use
     */
    @Override
    public void setTextureID(int textureID)
    {
        glo[0].setTextureID(textureID);
    }

    /**
     * Determine what part of the texture to start drawing from
     * @param x horizontal position inside the texture to start drawing from
     * @param y vertical position inside the texture to start drawing from
     */
    @Override
    public void setTexturePosition(float x, float y)
    {
        glo[0].setTexture(x, y);
    }

    /**
     * Set the offset (size) of the texture we "cut out" to draw with.
     * @param u Horizontal offset
     * @param v Vertical offset
     */
    @Override
    public void setTextureOffset(float u, float v)
    {
        glo[0].setTextureOffset(u, v);
    }

    /**
     * Draw the control to our active surface
     * @param mtrxProjView Our generated Projection/View matrix
     */
    @Override
    public void onDraw(float[] mtrxProjView)
    {
        if (dirty)
        {
            buildGLObjects();
            dirty = false;
        }
        if (visible && builtTexture)
        {
            for (GLObject glInstance : glo)
                glInstance.onDraw(mtrxProjView);
        }
    }

    /**
     * When we change the size of our surface, readjust the size of the control based on the grid
     */
    @Override
    public void onSurfaceChanged()
    {
        if (gridBottom >= 0 &&
                gridLeft >= 0 &&
                gridWidth >= 0 &&
                gridHeight >= 0)
        {
            setWidth(gridWidth);
            setHeight(gridHeight);
            setLeft(gridLeft);
            setBottom(gridBottom);
        }
        buildGLObjects();
    }


    /**
     * Called when the GL Surface is first created
     */
    @Override
    public void onSurfaceCreated()
    {
        buildGLObjects();
    }

    /**
     * Reposition our control based on the new grid coordinates
     * @param newLeft new left most position based on our grid
     * @param newBottom new bottom most position based on our grid
     */
    @Override
    public void move(float newLeft, float newBottom)
    {
        setLeftRaw(newLeft * ScreenConfiguration.getTileWidth());
        setBottomRaw(newBottom * ScreenConfiguration.getTileHeight());
        buildGLObjects();
    }

    /**
     * Reposition our control using precise coordinates
     * @param newLeft new left most position
     * @param newBottom new bottom most position
     */
    @Override
    public void moveRaw(float newLeft, float newBottom)
    {
        setLeftRaw(newLeft);
        setBottomRaw(newBottom);
        buildGLObjects();
    }

    /**
     * Basic onTouch handling to ensure we don't handle touch events for disabled / invisible controls
     * @param event the touch event that occurred
     * @return false if disabled, or the value returned from our callback listener
     */
    @Override
    public boolean onTouch(MotionEvent event)
    {
        if (enabled && visible && onTouchListener != null)
        {
            return onTouchListener.onTouch(event);
        }
        return false;
    }

    /**
     * Set the enabled status of our control
     * @param enabled true to enable the control, false to disable
     */
    @Override
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    /**
     * Set whether or not to display the control
     * @param visible true to display the control, false to hie
     */
    @Override
    public void setVisibility(boolean visible)
    {
        this.visible = visible;
    }

    /**
     * Returns whether or not the control is enabled or disabled
     * @return enabled status
     */
    @Override
    public boolean isEnabled()
    {
        return enabled;
    }

    /**
     * Returns whether or not we have hidden the control
     * @return whether or not the control is shown
     */
    @Override
    public boolean isVisible()
    {
        return visible;
    }

    /**
     * What scale we are using for drawing the control
     * @return scale of the control
     */
    public float getScale()
    {
        return scale;
    }

    /**
     * If the control has been changed, set this to true to force it to redraw
     * @param dirty if true, the control will be rebuilt
     */
    @Override
    public void setDirty(boolean dirty)
    {
        this.dirty = dirty;
    }

}
