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
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import ca.coffeeshopstudio.icegl.programs.ImageProgram;

/**
 * Game Configuration parameters - stuff that should be loaded from users preferences
 */
public class ScreenConfiguration
{
    //some built in shader programs used by the library
    public static ImageProgram imageProgram = new ImageProgram();
    private static boolean fullScreen = true;

    //used for the grid spacing
    private static int verticalTiles = 12; //default
    private static int horizontalTiles = 12; //default
    private static float tileWidth = 10;
    private static float tileHeight = 10;

    /**
     * Return the current number of tiles we are using horizontally
     * @return tile count along horizontal axis
     */
    public static int getHorizontalTiles()
    {
        return horizontalTiles;
    }

    /**
     * Return the current number of tiles we are using vertically
     * @return tile count along vertical axis
     */
    public static int getVerticalTiles()
    {
        return verticalTiles;
    }

    /**
     * Set the number of tiles we want horizontally
     * @param newHorizontalTiles new tile count
     */
    public static void setHorizontalTiles(int newHorizontalTiles)
    {
        if (newHorizontalTiles > 0)
        {
            //we need to determine what the current screen size is set to for reseting the grid sizes
            float originalWidth = horizontalTiles * tileWidth;
            float originalHeight = verticalTiles * tileHeight;
            horizontalTiles = newHorizontalTiles;
            setTileSize(originalWidth, originalHeight);
        }
    }

    /**
     * Set the number of tiles we want vertically
     * @param newVerticalTiles new tile count
     */
    public static void setVerticalTiles(int newVerticalTiles)
    {
        if (newVerticalTiles > 0)
        {
            //we need to determine what the current screen size is set to for reseting the grid sizes
            float originalWidth = horizontalTiles * tileWidth;
            float originalHeight = verticalTiles * tileHeight;
            verticalTiles = newVerticalTiles;
            setTileSize(originalWidth, originalHeight);
        }
    }

    /**
     * Get the height of each tile
     * @return tile height
     */
    public static float getTileHeight() {
        return tileHeight;
    }

    /**
     * Get the width of each tile
     * @return tile width
     */
    public static float getTileWidth() {
        return tileWidth;
    }

    /**
     * Return if we are full screen or not
     * @return true if full screen, false if not (showing nav bar etc)
     */
    public static boolean getFullScreen()
    {
        return fullScreen;
    }

    /**
     * When the screen size changes (orientation flip etc), we need to reset the tiles based on the new screen size
     * @param screenWidth new screen width
     * @param screenHeight new screen height
     */
    public static void setTileSize(float screenWidth, float screenHeight) {
        if (screenHeight > verticalTiles && screenWidth > horizontalTiles)
        {
            tileHeight = screenHeight / verticalTiles;
            tileWidth = screenWidth / horizontalTiles;
//            Utilities.Log("tile width/height is " + tileHeight + "/" + tileWidth);
        }
    }

    /**
     * Set to true to enable full screen mode
     * @param fullScreen true to enable full screen
     */
    public static void setFullScreen(boolean fullScreen)
    {
        fullScreen = fullScreen;
    }

    /**
     * Determines the size of our screen
     * @param context the current application context
     * @return the width and height of our screen as a PointF
     */
    public static PointF getScreenDimensions(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displaymetrics);

        PointF dimensions = new PointF();
        dimensions.set(displaymetrics.widthPixels, displaymetrics.heightPixels);

        return dimensions;
    }
}
