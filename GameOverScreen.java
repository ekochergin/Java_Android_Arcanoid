package com.infinite.android.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.infinite.android.ArkanoidInfinite;


public class GameOverScreen implements Screen {

    private ArkanoidInfinite game;
    private int score;
    private BitmapFont topText;
    private BitmapFont scoresText;
    private SpriteBatch batch;
    private Stage stage;
    private OrthographicCamera camera;

    public GameOverScreen(ArkanoidInfinite game, int score){
        this.game = game;
        this.batch = game.getBatch();
        this.score = score;
        this.scoresText = this.game.getSkin().getFont("default-font");
        this.topText = this.game.getSkin().getFont("menu-font");

        this.camera = new OrthographicCamera();
        this.stage = new Stage(new StretchViewport(800, 480, this.camera));
        Gdx.input.setInputProcessor(stage);

        Label titleLabel = new Label("Game over but you did well!", this.game.getSkin(), "menu-font", new Color(39, 14f, 175f, 1f));
        titleLabel.setPosition(50, 390);
        this.stage.addActor(titleLabel);

        Label startGameLabel = new Label("Restart Game!", this.game.getSkin(), "menu-font", Color.YELLOW);
        startGameLabel.setPosition(200, 200);
        this.stage.addActor(startGameLabel);
        startGameLabel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                restart();
            }
        });
        Label quitGameLabel = new Label("Quit Game", this.game.getSkin(), "menu-font", Color.YELLOW);
        quitGameLabel.setPosition(250, 130);
        this.stage.addActor(quitGameLabel);
        quitGameLabel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.stage.draw();

        this.batch.begin();
        float xPoint = Gdx.graphics.getWidth()/800f;
        float yPoint = Gdx.graphics.getHeight()/480f;
        //this.topText.draw(this.batch, "Game over but you did well!", 70*xPoint, 400*yPoint);
        this.scoresText.draw(this.batch, "Your final score is: " + this.score, 250*xPoint, 350*yPoint);
        this.batch.end();
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

    /*--- helpers start ---*/
    private void restart(){
        this.stage.clear();
        this.stage = null;
        this.game.setScreen(new LevelScreen(this.game, this.game.getSkin()));
        dispose();
    }
    /*--- helpers end ---*/
}
