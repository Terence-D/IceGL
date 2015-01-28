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

import ca.coffeeshopstudio.icegl.gl.GLColor;

public class Button extends Label
{
    protected String buttonText = "New Button"; //default text

    /**
     * Default Button Constructor
     * @param icm Parent to attach to
     */
    public Button(IControlManager icm)
    {
        super(icm);
        setText(buttonText);
        setFontSize(DEFAULT_SIZE);
        setColor(GLColor.White);
        setLeftRaw(1);
        setBottomRaw(1);
        setTextJustification(CENTER_JUSTIFY);
    }

    @Override
    public void buildGLObjects()
    {
        setHeightRaw(getFontSize() + 36);
        setTexturePosition(0, 7);
        setTextureOffset(1, icm.getControlTexture().getVratio());
        super.buildGLObjects();
    }

    @Override
    public void setFontSize(int fontSize)
    {
        super.setFontSize(fontSize);
    }

    @Override
    public boolean onTouch(MotionEvent event)
    {
        //Update the visibility of the button status
        if (isEnabled())
            setButtonTexture(event);
        return super.onTouch(event);
    }

    /**
     * Alternate the texture based on touch event
     * @param event Motion event
     */
    protected void setButtonTexture(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_UP:
                glo[0].setTexture(0, 7);
                glo[0].generateTextures();
                break;
            case MotionEvent.ACTION_DOWN:
                glo[0].setTexture(0, 6);
                glo[0].generateTextures();
                break;
        }
    }
}

