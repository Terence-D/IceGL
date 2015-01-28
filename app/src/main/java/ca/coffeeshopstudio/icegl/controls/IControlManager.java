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

import android.content.Context;

import ca.coffeeshopstudio.icegl.gl.GLTexture;

public interface IControlManager
{
    /**
     * Add a control to the parent object
     * @param control the control we are adding
     */
    void addControl(IControl control);

    /**
     * Retrieve the active context
     */
    Context getContext();

    /**
     * Get the texture atlas used for controls
     * @return text atlas for control
     */
    GLTexture getControlTexture();

    /**
     * Retrieves the attached activity
     * @return current activity
     */
    Context getActivity();
}
