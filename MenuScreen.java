package com.infinite.android.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.infinite.android.ArkanoidInfinite;

class MenuScreen implements Screen {

    private Stage stage;
    private ArkanoidInfinite game;
    private OrthographicCamera camera;
    private Skin skin;

    MenuScreen(ArkanoidInfinite game){
        this.game = game;
        this.camera = new OrthographicCamera();
        this.stage = new Stage(new StretchViewport(800, 480, this.camera));
        Gdx.input.setInputProcessor(stage);
        this.camera.setToOrtho(false, 800, 480);
        this.skin = this.game.getSkin();

        /*--- labels start ---*/
        Label titleLabel = new Label("Never ending arkanoid", this.skin, "menu-font", new Color(39, 14f, 175f, 1f));
        titleLabel.setPosition(141, 410);
        this.stage.addActor(titleLabel);

        Label startGameLabel = new Label("Start Game!", this.skin, "menu-font", Color.YELLOW);
        startGameLabel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                startGame();
            }
        });
        Label quitGameLabel = new Label("Quit Game", this.skin, "menu-font", Color.YELLOW);
        quitGameLabel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                quitGame();
            }
        });
        /*--- start & quit labels end ---*/

        Table table = new Table();
        table.setFillParent(true);
        table.add(startGameLabel).height(100);
        table.row();
        table.add(quitGameLabel).height(100);
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.stage.draw();
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

    @Override
    public void show() {

    }

    /*--- helpers start ---*/
    private void startGame(){
        this.stage.clear();
        this.stage = null;
        this.game.setScreen(new LevelScreen(this.game, this.skin));
        dispose();
    }

    private void quitGame(){
        Gdx.app.exit();
    }
    /*---helpers end ---*/
}
