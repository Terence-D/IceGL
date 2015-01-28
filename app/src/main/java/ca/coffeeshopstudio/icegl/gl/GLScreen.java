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
import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ca.coffeeshopstudio.icegl.controls.IControl;
import ca.coffeeshopstudio.icegl.controls.IControlManager;

/**
 * Abstract Renderer that each screens screen is based on
 */
public abstract class GLScreen implements GLSurfaceView.Renderer, IControlManager
{
    private float controlSpriteSize = 32.0f; //size of each tile in the default controls texture atlas

    // Our matrices
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxView = new float[16];
    protected final float[] mtrxProjectionAndView = new float[16];

    // Our screen resolution
    private float screenWidth;
    private float screenHeight;

    // Misc
    protected Context context;
    protected GLActivity activity;
    private long mLastTime; //timer for checking performance

    //scaling
    private float scale = 1.0f; //scale unit

    private GLTexture controlTexture; //used strictly for the controls

    //active controls assigned from the current screen
    private List<IControl> controls = new ArrayList<IControl>();

    //move the view port to adjust the viewable part of the screen by this much
    private PointF screenAdjustment = new PointF(0, 0);

    /**
     * initialize the screen
     * @param activity the current activity
     * @throws IllegalArgumentException
     */
    public GLScreen(GLActivity activity)
    {
        if (activity  == null)
            throw new IllegalArgumentException("Invalid activity constructing the screen");
        this.activity = activity;
        this.context = this.activity.getApplicationContext();

        //init the FPS control
        mLastTime = System.currentTimeMillis() + 100;

        PointF dimensions = ScreenConfiguration.getScreenDimensions(context);
        screenHeight = dimensions.y;
        screenWidth = dimensions.x;

        ScreenConfiguration.setTileSize(screenWidth, screenHeight);
    }

    /**
     * Called when control is returned to the application
     */
    public void onResume()
    {
        mLastTime = System.currentTimeMillis();
    }

    /**
     * called when the screen is first created, sets up the screen and controls
     * @param unused GL object
     * @param config GL configuration
     */
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config)
    {
        // Set the clear color to black
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);

        //setup desired blending
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        ScreenConfiguration.imageProgram.buildShaders();

        buildControlTexture();
        for (IControl control: controls)
            control.onSurfaceCreated();
    }

    /**
     * Create the textures used by our control library
     */
    private void buildControlTexture()
    {
        float targetWidth = 320.0f;
        float targetHeight = 480.0f;
        controlTexture = new GLTexture(context, "controls", controlSpriteSize, targetWidth, targetHeight);
        for (IControl control: controls)
            control.setTextureID(controlTexture.getTextureID());
    }

    /**
     * called when the screen changes due to orientation change etc
     * @param unused not used
     * @param width new screen width
     * @param height new screen height
     */
    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height)
    {
        // We need to know the new width and height of our device.
        screenWidth = width;
        screenHeight = height;
        ScreenConfiguration.setTileSize(screenWidth, screenHeight);

        //adjust the controls
        for (IControl control: controls)
            control.onSurfaceChanged();

        // Redo the Viewport, making it fill the available screen.
        GLES20.glViewport(0, 0, (int) screenWidth, (int) screenHeight);

        //build our view with the coordinates allowing full screen viewing
        buildView();

        setupScaling();
    }

    /**
     * Renders a frame
     * @param unused not used
     */
    @Override
    public void onDrawFrame(GL10 unused)
    {
        // Get the current time
        long now = System.currentTimeMillis();

        // In case something funky goin down
        if (mLastTime > now) return;

        // fill the screen with black and clear the Buffers
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        drawControls();

        // Get the amount of time the last frame took.
        //long elapsed = now - mLastTime;

        // Save the current time to see how long it took
        mLastTime = now;
    }

    /**
     * Draws our controls and sets us up using the image program
     */
    private void drawControls()
    {
        // Apply the projection and view transformation for our controls
        GLES20.glUniformMatrix4fv(ScreenConfiguration.imageProgram.getMatrixHandle(), 1, false, mtrxProjectionAndView, 0);

        // Set our shaderprogram to image shader for our controls
        GLES20.glUseProgram(ScreenConfiguration.imageProgram.getProgramID());

        for (IControl control: controls)
            control.onDraw(mtrxProjectionAndView);
    }

    /**
     * Setup the Ortho View and adjust it based on the users feedback
     */
    protected void buildView()
    {
        // Clear our matrices
        for(int i=0;i<16;i++)
        {
            mtrxProjection[i] = 0.0f;
            mtrxView[i] = 0.0f;
            mtrxProjectionAndView[i] = 0.0f;
        }

        // Setup our screen width and height for normal sprite translation.
        Matrix.orthoM(
                mtrxProjection, //matrix
                0, //matrix offset
                0f - screenAdjustment.x, //left
                screenWidth - screenAdjustment.x, //right
                0.0f - screenAdjustment.y, //bottom
                screenHeight - screenAdjustment.y, //top
                0, //near
                50 //far
        );

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);
    }

    /**
     * When a touch event occurs, this determines which control if any is called
     * and passes control down the onTouch chain to the active screen etc
     * @param event our motion event
     * @return true if we handled it, false if not
     */
    public boolean onTouchEvent(MotionEvent event)
    {
        float posX = event.getX();
        float posY = screenHeight - event.getY();

        for (IControl control : controls)
        {
            if (posX > control.getLeft() &&
                posX < control.getLeft() + control.getWidth() &&
                posY < control.getBottom() + control.getHeight() &&
                posY > control.getBottom()
                ) {
                    control.onTouch(event);
                    return true;
                }
        }
        return false;
    }

    /**
     * adjust the scaling of the screen and controls
     */
    private void setupScaling()
    {
        // The screen resolutions
        float targetScreenWidth = (context.getResources().getDisplayMetrics().widthPixels);
        float targetScreenHeight = (context.getResources().getDisplayMetrics().heightPixels);

        // Orientation is assumed portrait
        float ssx = targetScreenWidth / controlTexture.getTargetWidth();
        float ssy = targetScreenHeight / controlTexture.getTargetHeight();

        // Get our uniform scaler
        if(ssx > ssy)
            scale = ssy;
        else
            scale = ssx;

        for (IControl control: controls)
            control.setScale(scale);
    }

    /**
     * Called when the control temporarily leaves the screen
     */
    public void onPause()
    {

    }

    /**
     * Add a control to the screen - called when the active screen changes
     * @param control the control we are adding
     */
    @Override
    public void addControl(IControl control)
    {
        if (control != null)
            controls.add(control);
    }

    /**
     * adjust the view port for the screen
     * @param screenAdjustment how far to adjust based on the 2D x and y axis
     */
    public void setScreenAdjustment(PointF screenAdjustment)
    {
        this.screenAdjustment = screenAdjustment;
    }

    /**
     * Current active context
     * @return context attached to this screen
     */
    public Context getContext()
    {
        return context;
    }

    /**
     * Return the parent activity
     * @return current activity
     */
    @Override
    public Activity getActivity()
    {
        return activity;
    }

    /**
     * Returns the texture used by the Controls
     * @return GLTexture object
     */
    public GLTexture getControlTexture()
    {
        return controlTexture;
    }
}
