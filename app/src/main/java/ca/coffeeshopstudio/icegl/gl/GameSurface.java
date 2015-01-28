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
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * Surface used to draw the OpenGL screens on
 */
class GameSurface extends GLSurfaceView {

    private GLScreen mRenderer = null;

    /**
     * Initialize our surface and assign a screen object
     * @param context our current activities context
     * @param screen screen to attach to the surface
     */
    public GameSurface(Context context, GLScreen screen) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        this.mRenderer = screen;
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    /**
     * pause the screen when we lose control of the devices screen
     */
    @Override
    public void onPause() {
        super.onPause();
        mRenderer.onPause();
    }

    /**
     * Resumes the screen when we regain control of the devices screen
     */
    @Override
    public void onResume() {
        super.onResume();
        mRenderer.onResume();
    }

    /**
     * When the surface receives a touch action, we pass it along to the screen
     * @param e motion event received
     * @return true if the screen handles the touch event, otherwise false
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return mRenderer.onTouchEvent(e);
    }

}
