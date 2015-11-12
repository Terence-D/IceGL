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

import java.util.ArrayList;
import java.util.List;

import ca.coffeeshopstudio.icegl.gl.ScreenConfiguration;

/**
 * Modal style window that overlaps all other controls and prevents interaction with other controls while visible
 */
public class Dialog extends Label {
    final int MARGIN = 16;

    private List<IControl> controls = new ArrayList<>();

    private Button positive;
    private Button negative;

    /**
     * Default constructor
     *
     * @param icm what we are attaching to
     * @throws IllegalArgumentException
     */
    public Dialog(IControlManager icm) {
        super(icm);

        setLeft(1);
        setWidth(ScreenConfiguration.getHorizontalTiles() - 2);
        setBottom(1);
        setHeight(ScreenConfiguration.getVerticalTiles() - 2);

        positive = new Button(icm);
        positive.setLeftRaw(getLeft() + MARGIN);
        positive.setBottomRaw(getBottom() + MARGIN);
        positive.setWidthRaw((getWidth() / 2) - (MARGIN * 2));
        positive.setText("OK");
        positive.onTouchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    close();
                    return true;
                } else
                    return false;
            }
        };

        negative = new Button(icm);
        negative.setLeftRaw(positive.getLeft() + positive.getWidth() + MARGIN);
        negative.setBottomRaw(getBottom() + MARGIN);
        negative.setWidthRaw((getWidth() / 2) - (MARGIN * 2));
        negative.setText("Cancel");
        negative.onTouchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    close();
                    return true;
                } else
                    return false;
            }
        };

        controls.add(positive);
        controls.add(negative);

        setTextJustification(CENTER_JUSTIFY);
        setText("Are you sure?");
    }

    /**
     * Overrides the default action when the positive button is touched
     *
     * @param event type of event triggered
     */
    public void setPositiveTouchListener(OnTouchListener event) {
        positive.onTouchListener = event;
    }

    /**
     * Changes the text of the positive button
     *
     * @param text Text to use
     */
    public void setPositiveText(String text) {
        positive.setText(text);
    }

    /**
     * Overrides the default action when the negative button is touched
     *
     * @param event type of event triggered
     */
    public void setNegativeTouchListener(OnTouchListener event) {
        negative.onTouchListener = event;
    }

    /**
     * Changes the text of the negataive button
     *
     * @param text Text to use
     */
    public void setNegativeText(String text) {
        negative.setText(text);
    }

    private void close() {
        setEnabled(false);
        setVisibility(false);
        for (IControl control : controls) {
            control.setEnabled(false);
            control.setVisibility(false);
        }
    }

    @Override
    public void buildGLObjects() {
        setTexturePosition(0, 7);
        setTextureOffset(1, icm.getControlTexture().getVratio());
        super.buildGLObjects();
    }

    /**
     * onTouch event that takes in coordinates from the calling GLScreen onTouch
     *
     * @param event type of touch event
     * @param posX  horizontal position of touch
     * @param posY  vertical position of touch
     * @return true on completion
     */
    public boolean onTouch(MotionEvent event, float posX, float posY) {
        for (IControl control : controls) {
            if (posX > control.getLeft() &&
                    posX < control.getLeft() + control.getWidth() &&
                    posY < control.getBottom() + control.getHeight() &&
                    posY > control.getBottom()
                    && control.isEnabled()) {
                control.onTouch(event);
            }
        }
        return true;
    }
}
