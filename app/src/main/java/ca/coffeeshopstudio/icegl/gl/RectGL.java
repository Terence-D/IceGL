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

public class RectGL
{
    // Left position of the object
    public float left;

    // Bottom position of the object
    public float bottom;

    // Width of the object
    public float width;

    // Height of the object
    public float height;

    /**
     * Initialize the dimensions for used with GL Objects
     * @param left left most position
     * @param bottom bottom most position
     * @param width width of the object
     * @param height height of the object
     */
    public RectGL(float left, float bottom, float width, float height)
    {
        this.left = left;
        this.bottom = bottom;
        this.width = width;
        this.height = height;
    }
}
