package com.badlogic.drop;

import com.badlogic.drop.Screens.GameScreen.GameScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Drop extends Game {

	public static float VIEW_WIDTH = 800;
	public static float VIEW_HEIGHT = 480;
	
	private SpriteBatch batch;
	private Screen screen;
	
	@Override
	public void create () {
		this.batch = new SpriteBatch();

		this.screen = new GameScreen(this.batch);
		this.setScreen(this.screen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		this.batch.dispose();
		this.screen.dispose();
	}
}
