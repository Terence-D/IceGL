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

import ca.coffeeshopstudio.icegl.gl.GLTexture;

public class Image extends Control
{
    //The attached control manager
    protected IControlManager icm;
    private String imageName;
    private float width = 0.0f;
    private float height = 0.0f;

    /**
     * Default constructor
     * @param icm parent to attach to
     */
    public Image(IControlManager icm)
    {
        super(icm);
        this.icm = icm;
        icm.addControl(this);
        //defaults to full size sprite
        setTexturePosition(0, 0);
        setTextureOffset(1f, 1f);
        enabled = false; //prevents overriding clicks
    }

    /**
     * Assigns the Image field and core information to display on the screen
     *
     * @param imageName image to draw that is stored in the drawable resource folder
     * @param width     width of the image we will be drawing
     * @param height    height of the image we will be drawing
     */
    public void setImage(String imageName, float width, float height) {
        this.imageName = imageName;
        this.width = width;
        this.height = height;
    }

    @Override
    public void onSurfaceCreated() {
        if (imageName != null) {
            GLTexture texBackdrop = new GLTexture(icm.getActivity(), imageName, width, width, height);

            setTextureID(texBackdrop.getTextureID());
        }
        super.onSurfaceCreated();
    }
}
