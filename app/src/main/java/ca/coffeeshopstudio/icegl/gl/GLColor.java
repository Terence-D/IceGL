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

/**
 * Object to represent RGBA Color
 * Created by terence on 22/09/14.
 */
public class GLColor
{
    //used to internally reference the array
    private static final int RED = 0;
    private static final int GREEN = 1;
    private static final int BLUE = 2;
    private static final int ALPHA = 3;

    //max value allowed for converting to int
    private final int MAX_SIZE = 255;

    //stores the individual RGBA colors
    private float rgba[] = new float[4];

    //Built in colors
    public final static GLColor Red = new GLColor(1.0f, 0.0f, 0.0f, 1.0f);
    public final static GLColor Green = new GLColor(0.0f, 1.0f, 0.0f, 1.0f);
    public final static GLColor Blue = new GLColor(0.0f, 0.0f, 1.0f, 1.0f);
    public final static GLColor White = new GLColor(1.0f, 1.0f, 1.0f, 1.0f);
    public final static GLColor Black = new GLColor(0.0f, 0.0f, 0.0f, 1.0f);

    /**
     * Default constructor, black non transparent
     */
    public GLColor()
    {
        rgba[RED] = 0;
        rgba[GREEN] = 0;
        rgba[BLUE] = 0;
        rgba[ALPHA] = 1;
    }

    /**
     * Constructor to initialize the color to the specified RGBA
     * @param red red value to use, 0-255
     * @param green green value to use, 0-255
     * @param blue blue value to use, 0-255
     * @param alpha control transparency, 0-255
     */
    public GLColor(float red, float green, float blue, float alpha)
    {
        setColor(RED, red);
        setColor(GREEN, green);
        setColor(BLUE, blue);
        setColor(ALPHA, alpha);
    }

    private void setColor(int colorIndex, float newColorValue)
    {
        if (newColorValue > MAX_SIZE)
            newColorValue = 0;
        rgba[colorIndex] = newColorValue;
    }

    /**
     * Return the red value
     * @return the red hue
     */
    public float red()
    {
        return rgba[RED];
    }

    /**
     * Return the green value
     * @return the green hue
     */
    public float green()
    {
        return rgba[GREEN];
    }

    /**
     * Return the blue value
     * @return the blue hue
     */
    public float blue()
    {
        return rgba[BLUE];
    }

    /**
     * Return the transparency value
     * @return the transparency value
     */
    public float alpha()
    {
        return rgba[ALPHA];
    }

    /**
     * Retrieve the red value
     * @return red intensity 0..255
     */
    public int redInt()
    {
        return buildColorInt(red());
    }

    /**
     * Retrieve the green value
     * @return green intensity 0..255
     */
    public int greenInt()
    {
        return buildColorInt(green());
    }

    /**
     * Retrieve the blue value
     * @return blue intensity 0..255
     */
    public int blueInt()
    {
        return buildColorInt(blue());
    }

    /**
     * Retrieve the alpha value
     * @return transparency level 0..255
     */
    public int alphaInt()
    {
        return buildColorInt(alpha());
    }

    /**
     * Retrieve the integer value of the color
     * @param color which color we are checking
     * @return intensity of that color / alpha value
     */
    private int buildColorInt(float color)
    {
        //first covnert it from a ratio (0 - 1.0) to hex style (0 to 255)
        float converted = color * 255;

        return (int) converted;
    }
}
