package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

public class GameObject extends Rectangle implements Disposable {

    public final Texture texture;

    public GameObject(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void dispose() {
        this.texture.dispose();        
    }
 
}
