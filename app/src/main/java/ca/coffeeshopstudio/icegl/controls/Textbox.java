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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.MotionEvent;
import android.widget.EditText;

public class Textbox extends Label
{
    private String textboxText = "Enter Text Here";

    protected String prompt = "Enter the new Text";

    /**
     * Default constructor
     * @param icm Parent to attach to
     */
    public Textbox(IControlManager icm)
    {
        super(icm);
        text = textboxText;
    }

    @Override
    public void buildGLObjects()
    {
        underline = true;
        setTexturePosition(0, 0);
        setTextureOffset(0, 0);
        setWidthRaw(dimensions.width());
        setHeightRaw(dimensions.height());
        super.buildGLObjects();
    }

    /**
     * Supports opening a text entry dialog when touched
     * @param event the touch event that occurred
     * @return true when text is entered
     */
    @Override
    public boolean onTouch(MotionEvent event)
    {
        if (isEnabled())
        {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_UP:
                    getUserInput();
                    return true;
            }
        }
        return super.onTouch(event);
    }

    /**
     * Sets the text to display inside the prompt
     * @param prompt text to display
     */
    public void setPrompt(String prompt)
    {
        this.prompt = prompt;
    }

    /**
     * Controls entering text for the user via a popup window
     */
    private void getUserInput()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(icm.getActivity());
        final EditText nameEntry = new EditText(icm.getActivity());
        builder.setView(nameEntry);
        builder.setMessage(prompt)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        text = nameEntry.getText().toString();
                        setDirty(true);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

