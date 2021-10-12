package com.badlogic.drop.Screens.GameScreen;

import com.badlogic.drop.Drop;
import com.badlogic.drop.GameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {

	//Game object dimensions
    public static int BUCKET_WIDTH = 64;
	public static int BUCKET_HEIGHT = 64;
    public static int DROP_WIDTH = 64;
    public static int DROP_HEIGHT = 64;
	
	//Game Object Textures
	private Texture dropTexture;

	private Array<GameObject> droplets;

	//Game Audio
	private Sound dropSound;
	private Music rainMusic;

	//Score
	private int score;

    //Game objects
	private GameObject bucket;
	private long lastDrop;

    private SpriteBatch batch;
	private OrthographicCamera camera;

	/**
	 * 
	 * @param batch: The SpriteBatch instance of the game
	 */
    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

	/**
	 * 
	 */
    @Override
    public void show() {
        //Camera
        this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, Drop.VIEW_WIDTH, Drop.VIEW_HEIGHT);

		// Main sprites for the game
		dropTexture = new Texture(Gdx.files.internal("drop.png"));

		//Audio for the game
		dropSound = Gdx.audio.newSound(Gdx.files.internal("dropsound.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rainmusic.mp3"));

		// Rain music settings and play
		rainMusic.setLooping(true);
		rainMusic.play();

		//Bucket rectangle
		bucket = new GameObject(new Texture(Gdx.files.internal("bucket.png")));
		bucket.x = Drop.VIEW_WIDTH/2 - BUCKET_WIDTH/2;
		bucket.y = 20;
		bucket.width = BUCKET_WIDTH;
		bucket.height = BUCKET_HEIGHT;

        //Droplets control
		droplets = new Array<GameObject>();
		spawnRaindrop();
    }

    /**
     * Renders the game on the screen. Draws onto the screen once every frame
     * 
     * @param delta: The time between this frame and the previous one
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        //Camera configuration with window
		this.camera.update();
        batch.setProjectionMatrix(this.camera.combined);

		//Take in user input
		takeInput(delta);

		// Keep bucket within the screen
		if (bucket.x < 0) 
            bucket.x = 0;
            
		if (bucket.x > Drop.VIEW_WIDTH - BUCKET_WIDTH) 
            bucket.x = Drop.VIEW_WIDTH - BUCKET_WIDTH;

		// Has a second passed since the last drop?
		if (TimeUtils.nanoTime() - lastDrop > 1000000000-(score*10)) spawnRaindrop();


		for (ArrayIterator<GameObject> iter = droplets.iterator(); iter.hasNext(); ) {
			GameObject raindrop = iter.next();
			raindrop.y -= 200 * delta *  (Drop.VIEW_HEIGHT/480);
            if(raindrop.overlaps(bucket)) {
				dropSound.play();
				iter.remove();

				score++;
			 }
			if(raindrop.y + 64 < 0) iter.remove();
		}

		batch.begin();
		batch.draw(bucket.texture, bucket.x, bucket.y, BUCKET_WIDTH, BUCKET_HEIGHT);
		for (GameObject drop : droplets) {
			batch.draw(drop.texture, drop.x, drop.y, DROP_WIDTH, DROP_HEIGHT);
		}
		batch.end();
    }

	/**
	 * 
	 * @param width: The new window width
	 * @param height: The new window height
	 */
    @Override
    public void resize(int width, int height) {
        Drop.VIEW_WIDTH = width;
        Drop.VIEW_HEIGHT = height;

        this.camera.setToOrtho(false, Drop.VIEW_WIDTH, Drop.VIEW_HEIGHT);
		this.camera.update();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

	/**
	 * 
	 */
    @Override
    public void dispose() {
        dropTexture.dispose();
		bucket.dispose();
		dropSound.dispose();
		rainMusic.dispose();
    }

    /**
     * Spawns a new raindrop
     */
    private void spawnRaindrop() {
		GameObject raindrop = new GameObject(dropTexture);

		raindrop.x = MathUtils.random(0, Drop.VIEW_WIDTH-bucket.width);
		raindrop.y = Drop.VIEW_HEIGHT;

		raindrop.width = DROP_WIDTH;
		raindrop.height = DROP_HEIGHT;
		droplets.add(raindrop);
		lastDrop = TimeUtils.nanoTime();
	}

    /**
	 * 
     * @param delta: The time between the current frame and the previous frame
     */
    private void takeInput(float delta) {
        if (Gdx.input.isTouched()) {
			//Touch information
			Vector3 touch = new Vector3();
			touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			this.camera.unproject(touch);

			//Set bucket x-coordinate
			bucket.x = touch.x - BUCKET_WIDTH/2;
		}

		// Key to move left?
		if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) 
			bucket.x -= 400 * delta;

		// Key to move right?
		if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) 
			bucket.x += 400 * delta;
    }
    
}
