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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class GLText
{
    private static final int FONT_BUFFER = 8; //extra breathing room for the font
    private static Rect dimensions; //dimensions of the generated bmp

    /**
     * Generate a Bitmap based on the text message
     * @param context active context
     * @param text text to display
     * @param fontFile font being used (from assets folder)
     * @param size font size
     * @param color color of the font
     * @return Bitmap containing the desired text
     */
    public static Bitmap buildTextBitmap(Context context, String text, String fontFile, int size, GLColor color)
    {
        return buildTextBitmap(context, text, fontFile, size, color, false);
    }

    /**
     * Generate a Bitmap based on the text message
     * @param context active context
     * @param text text to display
     * @param fontFile font being used (from assets folder)
     * @param size font size
     * @param color color of the font
     * @param underline whether or not to underline the text
     * @return Bitmap containing the desired text
     */
    public static Bitmap buildTextBitmap(Context context, String text, String fontFile, int size, GLColor color, boolean underline)
    {
        if (context == null)
            throw new NullPointerException("null context");
        if (fontFile == null || fontFile.isEmpty())
            throw new NullPointerException("null font file");

        Typeface tf = Typeface.createFromAsset(context.getAssets(), fontFile);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(size);
        paint.setUnderlineText(underline);
        paint.setARGB(color.alphaInt(),
                color.redInt(),
                color.greenInt(),
                color.blueInt());
        paint.setTypeface(tf);

        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        dimensions = new Rect(textBounds);

        Bitmap bitmap = Bitmap.createBitmap(textBounds.width() + FONT_BUFFER, textBounds.height() + FONT_BUFFER, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        bitmap.eraseColor(0);

        canvas.drawText(text, 0, dimensions.height(), paint);

        dimensions = new Rect(0, 0, textBounds.width(), textBounds.height());
        return bitmap;
    }

    /**
     * Returns the dimensions of the most recently generated text
     * TODO there should be a better way of doing this, possibly pass the information back in buildTextBitmap
     * @return Rect of the dimensions
     */
    public static Rect getDimensions()
    {
        return dimensions;
    }
}
