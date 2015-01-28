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

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.util.Stack;

import ca.coffeeshopstudio.icegl.R;

/**
 * Parent shell for each game screens activities
 */
public abstract class GLActivity extends Activity
{
    protected GLSurfaceView glSurfaceView;

    protected Stack<GLScreen> screenStack = new Stack<GLScreen>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Super
        super.onCreate(savedInstanceState);

        // Turn off the window's title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Fullscreen mode
        if (ScreenConfiguration.getFullScreen())
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * when paused also pause the surface view
     */
    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    /**
     * when resumed also resume the surface view
     */
    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    /**
     * starts a new activity based on the next screen / screen we are calling
     * @param newScreen the ID of the screen used in the GLActivity's renderLoader
     */
    public void changeScreen(GLScreen newScreen)
    {
        screenStack.push(newScreen);
        updateActiveScreen();
    }


    /**
     * starts a new activity based on the next screen / screen we are calling
     */
    public void updateActiveScreen()
    {
        glSurfaceView = new GameSurface(this, screenStack.peek());

        // Set our view.
        setContentView(R.layout.activity_game);

        // Retrieve our Relative layout from our main layout we just set to our view.
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.gamelayout);

        // Attach our surfaces view to our relative layout from our main layout.
        RelativeLayout.LayoutParams glParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.removeAllViews();
        layout.addView(glSurfaceView, 0, glParams);
    }

    @Override
    public void onBackPressed()
    {
        screenStack.pop();
        if (!screenStack.isEmpty())
            updateActiveScreen();
        else
            super.onBackPressed();
    }
}
