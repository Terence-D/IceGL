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

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.opengl.GLES20;

import ca.coffeeshopstudio.icegl.gl.GLColor;
import ca.coffeeshopstudio.icegl.gl.GLText;
import ca.coffeeshopstudio.icegl.gl.GLTexture;
import ca.coffeeshopstudio.icegl.gl.ScreenConfiguration;

public class Label extends Control
{
    //Set the justification of the text
    public static final int LEFT_JUSTIFY = 0;
    public static final int CENTER_JUSTIFY = 1;
    public static final int RIGHT_JUSTIFY = 2;

    //The attached control manager
    protected IControlManager icm;

    //Defaults
    protected final String DEFAULT_FONT = "font.ttf";
    protected String text = "New Label";
    protected GLColor color = GLColor.White;
    protected final int DEFAULT_SIZE = 36;
    protected Rect dimensions = new Rect(0,0,0,0);

    private int fontSize = DEFAULT_SIZE;

    private String font = DEFAULT_FONT;
    private int textJustification = LEFT_JUSTIFY;

    private GLTexture fontTexture;

    protected float textLeft = 0;
    protected float textBottom = 0;
    private int textMessageHeight = 0;
    protected boolean underline = false;

    /**
     * Default constructor
     * @param icm parent to attach to
     */
    public Label(IControlManager icm)
    {
        super(icm);
        this.icm = icm;
        setGloCount(2); //add an object to account for the text
        setLeftRaw(1);
        setBottomRaw(1);
        setWidthRaw(ScreenConfiguration.getScreenDimensions(icm.getContext()).length());
    }

    @Override
    public void buildGLObjects()
    {
        //Build a texture with the font and string message we want to use
        try
        {
            Bitmap bmp = GLText.buildTextBitmap(icm.getContext(), getText(), font, fontSize, getColor(), underline);
            //get the dimensions of the generated font texture
            dimensions = GLText.getDimensions();

            textMessageHeight = dimensions.height();

            textBottom = glo[0].getBottom();

            switch (getTextJustification())
            {
                case CENTER_JUSTIFY:
                    textLeft = glo[0].getLeft() + (getWidth() / 2) - (dimensions.width() / 2);
                    textBottom = glo[0].getBottom() + (glo[0].getHeight() / 2) - (dimensions.height() / 2);
                    break;
                case LEFT_JUSTIFY:
                    textLeft = glo[0].getLeft();//+ (glo[0].getWidth() / 2) - (dimensions.width() / 2);
                    break;
                case RIGHT_JUSTIFY:
                    textLeft = glo[0].getLeft() + glo[0].getWidth() - dimensions.width();
                    break;
            }

            //Delete any existing textures before updating the text
            if (fontTexture != null)
                if (fontTexture.getTextureID() > 0)
                {
                    GLES20.glDeleteTextures(1, new int[] {fontTexture.getTextureID()}, 0);
                }
            //Create the texture and build our globject based on it
            fontTexture = new GLTexture(bmp, 0, 0, 0);
            setGlObjectDimensions(glo[1], textLeft, textBottom, dimensions.width(), dimensions.height(), getScale());
            glo[1].setTextureID(fontTexture.getTextureID());
            glo[1].setTexture(0, 0);
            glo[1].setTextureOffset(1, 1);
            bmp.recycle(); //clean up our bitmap
        } catch (Exception e)
        {
            dimensions = new Rect(0,0,0,0);
        }
        super.buildGLObjects();
    }

    /**
     * Returns the height of the text string
     * @return height of the text
     */
    public int getTextHeight()
    {
        return dimensions.height();
    }

    /**
     * returns what the text is set to
     * @return the text displayed in the label
     */
    public String getText()
    {
        return text;
    }

    /**
     * Set the text to display in the label
     * @param text text to show
     */
    public void setText(String text)
    {
        if (text == null)
            text = "";
        this.text = text;
        setDirty(true);
    }

    /**
     * Returns the color of the font
     * @return the current font color
     */
    public GLColor getColor()
    {
        return color;
    }

    /**
     * Sets the font to the passed in GLColor object
     * @param color the color to use
     */
    public void setColor(GLColor color)
    {
        if (color == null)
            color = GLColor.White;
        this.color = color;
        //glText.setColor(color);
    }

    /**
     * Returns the currently set justification
     * @return type of text justification
     */
    public int getTextJustification()
    {
        return textJustification;
    }

    /**
     * Sets the justification type of the text
     * @param justification One of the three justification types
     */
    public void setTextJustification(int justification)
    {
        if (justification == LEFT_JUSTIFY ||
                justification == CENTER_JUSTIFY ||
                justification == RIGHT_JUSTIFY)
        {
            textJustification = justification;
        }
    }

    /**
     * Set the font size
     * @param fontSize size of the font
     */
    public void setFontSize(int fontSize)
    {
        if (fontSize > 0)
            this.fontSize = fontSize;
    }

    /**
     * Returns the font size of the control
     * @return current font size
     */
    public int getFontSize()
    {
        return fontSize;
    }

    /**
     * Returns the font file we are set to use
     * @return current font being used
     */
    public String getFont()
    {
        return font;
    }

    /**
     * Changes the fotn we are using from the default one
     * @param font font file we are to use
     */
    public void setFont(String font)
    {
        this.font = font;
    }

    public int getTextMessageHeight()
    {
        return textMessageHeight;
    }
}
