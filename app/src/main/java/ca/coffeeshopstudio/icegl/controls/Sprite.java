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

import ca.coffeeshopstudio.icegl.gl.GLTexture;

public class Sprite extends Image {
    /**
     * Default constructor to build a sprite from a sprite map (texture atlas)
     *
     * @param icm parent to attach to
     */
    public Sprite(IControlManager icm) {
        super(icm);
    }

    /**
     * Defines the sprite details.  The texture is broken up into a "grid" based on the previously
     * defined sprite size
     *
     * @param texture the Texture atlas containing our sprites image
     * @param xPos    horizontal position of the sprite inside the texture atlas
     * @param yPos    vertical position of the sprite inside the texture atlas
     * @param width   how wide - measured in sprite size - the sprite is
     * @param height  how tall - measured in sprite size - the sprite is
     * @return whether or not the operation was a success
     */
    public boolean setSprite(GLTexture texture, float xPos, float yPos, float width, float height) {
        return buildSprite(texture, xPos, yPos, width, height);
    }

    /**
     * Defines the sprite details.  The texture is broken up into a "grid" based on the previously
     * defined sprite size - this will build a "1x1 sprite"
     *
     * @param texture the Texture atlas containing our sprites image
     * @param xPos    horizontal position of the sprite inside the texture atlas
     * @param yPos    vertical position of the sprite inside the texture atlas
     * @return whether or not the operation was a success
     */
    public boolean setSprite(GLTexture texture, float xPos, float yPos) {
        return buildSprite(texture, xPos, yPos, 1, 1);
    }

    private boolean buildSprite(GLTexture texture, float xPos, float yPos, float width, float height) {
        if (texture.getTextureID() >= 0) {
            setTextureID(texture.getTextureID());
            setTexturePosition(xPos, yPos);
            setTextureOffset(texture.getUratio() * width, texture.getVratio() * height);

            return true;
        }
        return false;
    }
}
