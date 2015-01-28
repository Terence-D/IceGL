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

import ca.coffeeshopstudio.icegl.gl.GLScreen;

public class CheckedButton extends Button
{
    protected String buttonText = "New Checked Button";

    private boolean checked = false; //button status

    /**
     * Default Button Constructor
     *
     * @param renderer Game screen to attach to
     */
    public CheckedButton(GLScreen renderer)
    {
        super(renderer);
        setText(buttonText);
    }

    /**
     * Handle touch events for the control
     * @param event the motion event object
     */
    @Override
    protected void setButtonTexture(MotionEvent event)
    {
        if (isEnabled())
        {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_UP:
                    checked = !checked;
                    break;
            }
            if (checked)
            {
                glo[0].setTexture(0, 6);
                glo[0].generateTextures();
            } else
            {
                glo[0].setTexture(0, 7);
                glo[0].generateTextures();
            }
        }
    }

    /**
     * Returns whether or not the control is checked or not
     * @return true if checked, false if not
     */
    public boolean getStatus()
    {
        return checked;
    }
}

