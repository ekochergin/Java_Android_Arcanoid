package com.infinite.android;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.infinite.android.actors.Hero;
import com.infinite.android.screens.LoadScreen;

public class ArkanoidInfinite extends Game {
	private SpriteBatch batch;
	private Stage stage;
	private OrthographicCamera camera;
	private Hero hero;
	private World world;
	private int maxScore;
	private Skin skin;

	public static final float SIZE_MULTIPLICATOR = 0.1f;
	public static final int MAX_BRICK_LEVEL = 5;

	@Override
	public void create () {
		this.batch = new SpriteBatch();
		this.world = new World(new Vector2(0, 0), true);
		this.setScreen(new LoadScreen(this));
		this.maxScore = Gdx.app.getPreferences("Score").getInteger("MaxScore", 0);
		Gdx.input.setCatchBackKey(true);

	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		this.batch.dispose();
		if(this.stage != null) {
			this.stage.dispose();
		}
		this.world.dispose();
	}

	/*--- get/set starts ---*/
	public SpriteBatch getBatch(){return this.batch;};

	public Stage getStage(){return this.stage;}

	public OrthographicCamera getCamera(){return this.camera;}

	public World getWorld(){return this.world;}

	public Hero getHero(){return this.hero;}

	public void setHero(Hero hero){this.hero = hero;}

	public int getMaxScore(){return this.maxScore;}

	public void setMaxScore(int newScore){
		this.maxScore = newScore;
		Gdx.app.getPreferences("Score").putInteger("MaxScore", newScore).flush();
	}

	public void setSkin(Skin newSkin){
		this.skin = newSkin;
	}

	public Skin getSkin(){return this.skin;}
	/*--- get/set ends ---*/
}
