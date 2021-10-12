package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

public class GameObject extends Rectangle implements Disposable {

    public final Texture texture;

    /**
     * 
     * @param texture: The texture of the object to draw to the screen
     */
    public GameObject(Texture texture) {
        this.texture = texture;
    }

    /**
     * Dispose of object resources
     */
    @Override
    public void dispose() {
        this.texture.dispose();        
    }
 
}
