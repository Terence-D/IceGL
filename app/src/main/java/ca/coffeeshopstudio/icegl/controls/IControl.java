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

public interface IControl
{
    /**
     * Getter for left position
     * @return Returns the left position of the control
     */
    float getLeft();

    /**
     * Getter for bottom position
     * @return Returns the bottom position of the control
     */
    float getBottom();

    /**
     * Getter for width
     * @return Returns the width of the control measured in grid units
     */
    float getWidth();

    /**
     * Getter for height
     * @return Returns the height of the control measured in grid units
     */
    float getHeight();

    /**
     * Set the Exact Width of the Control
     * @param width exact with of the control to use
     */
    void setWidthRaw(float width);

    /**
     * Set the Exact Height of the Control
     * @param height exact width of the control to use
     */
    void setHeightRaw(float height);

    /**
     * Set the Width of the Controls based on Grid units
     * @param width how wide of the control based in Grid units
     */
    void setWidth(int width);

    /**
     * Set the height of the Controls based on Grid units
     * @param height how high of the control based in Grid units
     */
    void setHeight(int height);

    /**
     * Set the Left position of the Control based on Grid units
     * @param left left position based on Grid units
     */
    void setLeft(int left);

    /**
     * Set the bottom position of the Control based on Grid units
     * @param bottom bottom position based on Grid units
     */
    void setBottom(int bottom);

    /**
     * Set the Exact Left position of the Control
     * @param left exact left position
     */
    void setLeftRaw(float left);

    /**
     * Set the Exact Bottom position of the Control
     * @param bottom exact bottom position
     */
    void setBottomRaw(float bottom);

    /**
     * Adjust the scale of the control
     * @param scale What to multiply the scale by
     */
    void setScale(float scale);

    /**
     * Assign our control the specified texture
     * @param textureID index of the texture to use
     */
    void setTextureID(int textureID);

    /**
     * Determine what part of the texture to start drawing from
     * @param x horizontal position inside the texture to start drawing from
     * @param y vertical position inside the texture to start drawing from
     */
    void setTexturePosition(float x, float y);

    /**
     * Set the offset (size) of the texture we "cut out" to draw with.
     * @param u Horizontal offset
     * @param v Vertical offset
     */
    void setTextureOffset(float u, float v);

    /**
     * When we change the size of our surface, readjust the size of the control based on the grid
     */
    void onSurfaceChanged();

    /**
     * Draw the control to our active surface
     * @param mtrxProjView Our generated Projection/View matrix
     */
    void onDraw(float[] mtrxProjView);

    /**
     * Reposition our control based on the new grid coordinates
     * @param newLeft new left most position based on our grid
     * @param newBottom new bottom most position based on our grid
     */
    void move(float newLeft, float newBottom);

    /**
     * Reposition our control using precise coordinates
     * @param newLeft new left most position
     * @param newBottom new bottom most position
     */
    void moveRaw(float newLeft, float newBottom);

    /**
     * Basic onTouch handling to ensure we don't handle touch events for disabled / invisible controls
     * @param event the touch event that occurred
     * @return false if disabled, or the value returned from our callback listener
     */
    boolean onTouch(MotionEvent event);

    /**
     * Set the enabled status of our control
     * @param enabled true to enable the control, false to disable
     */
    void setEnabled(boolean enabled);

    /**
     * Set whether or not to display the control
     * @param visible true to display the control, false to hie
     */
    void setVisibility(boolean visible);

    /**
     * Called when the GL Surface is first created
     */
    void onSurfaceCreated();

    /**
     * Returns whether or not the control is enabled or disabled
     * @return enabled status
     */
    boolean isEnabled();

    /**
     * Returns whether or not we have hidden the control
     * @return whether or not the control is shown
     */
    boolean isVisible();

    /**
     * If the control has been changed, set this to true to force it to redraw
     * @param dirty if true, the control will be rebuilt
     */
    void setDirty(boolean dirty);
}
