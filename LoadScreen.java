package com.infinite.android.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.infinite.android.ArkanoidInfinite;

public class LoadScreen implements Screen {

    private ArkanoidInfinite game;
    private OrthographicCamera camera;
    private Stage stage;
    private AssetManager assetManager;
    private Texture progressBar;
    private Skin skin;
    private Batch batch;

    public LoadScreen(ArkanoidInfinite game){
        this.game = game;
        this.camera = this.game.getCamera();
        this.stage = this.game.getStage();
        this.batch = this.game.getBatch();

        this.assetManager = new AssetManager();

        // assets loading
        this.assetManager.load("skin.json", Skin.class);
        this.assetManager.load("img/hero.png", Texture.class);
        this.assetManager.load("img/ball.png", Texture.class);
        for(int i = 1; i <= ArkanoidInfinite.MAX_BRICK_LEVEL; i++){
            this.assetManager.load("img/brick" + i + ".png", Texture.class);
        }

        //preparing progress bar
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawLine(0, 0, 1, 1);

        progressBar = new Texture(pixmap);
        pixmap.dispose();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(31/255f, 40/255f, 45/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Call MenuScreen when loading is finished
        if(assetManager.update()){

            //load from asset to a Skin
            this.skin = assetManager.get("skin.json", Skin.class);
            this.skin.add("hero", assetManager.get("img/hero.png", Texture.class));
            this.skin.add("ball", assetManager.get("img/ball.png", Texture.class));
            for(int i = 1; i <= ArkanoidInfinite.MAX_BRICK_LEVEL; i++){
                this.skin.add("brick" + i, assetManager.get("img/brick" + i + ".png", Texture.class));
            }

            this.game.setSkin(this.skin);
            //Going to next screen
            this.game.setScreen(new MenuScreen(this.game));

        }else{ //draw Loading indicator
            this.batch.begin();
            this.batch.draw(this.progressBar, 100, 100, 10, 100*assetManager.getProgress());
            this.batch.end();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
