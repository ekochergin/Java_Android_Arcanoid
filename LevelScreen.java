package com.infinite.android.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
//import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.infinite.android.ArkanoidInfinite;
import com.infinite.android.BallContact;
import com.infinite.android.actors.Ball;
import com.infinite.android.actors.Brick;
import com.infinite.android.actors.Hero;


import java.util.Iterator;

import static com.badlogic.gdx.Gdx.input;


public class LevelScreen implements Screen {

    private ArkanoidInfinite game;
    private OrthographicCamera camera;
    private Stage stage;
    private Group bricks;
    private Group balls;
    private Hero hero;
    private Skin skin;
    private int score;
    private Array<Texture> brickImagesArray;
    private boolean bricksMove;
    private int lowestBrickY;


    private Texture ballImage;

    //box2d
    private World world;
    //private Box2DDebugRenderer debugRenderer;
    private float accumulator = 0;
    private static final float TIME_STEP = 1/60f;
    private final float SIZE_MULTIPLICATOR = 0.1f;

    //fonts
    private BitmapFont font;

    public LevelScreen(ArkanoidInfinite game, Skin skin){
        this.game = game;
        this.camera = new OrthographicCamera();
        this.stage = new Stage(new StretchViewport(800*SIZE_MULTIPLICATOR, 480*SIZE_MULTIPLICATOR, this.camera));
        this.camera.setToOrtho(false, 800*SIZE_MULTIPLICATOR, 480*SIZE_MULTIPLICATOR);
        this.skin = skin;
        this.world = this.game.getWorld();
        this.lowestBrickY = 300;
        this.bricksMove = false;
        //create hero
        int livesQty = 5;
        this.hero = new Hero(this.world, livesQty, this.skin.get("hero", Texture.class));
        this.ballImage = this.skin.get("ball", Texture.class);
        this.score = 0;
        this.font = this.skin.getFont("default-font");
        this.font.getData().setScale(Gdx.graphics.getWidth()/800f, Gdx.graphics.getHeight()/480f);

        this.stage.addActor(this.hero);

        /*--- getting all brick images start ---*/
        this.brickImagesArray = new Array<Texture>();
        for(int i = 1; i <= ArkanoidInfinite.MAX_BRICK_LEVEL; i++){
            this.brickImagesArray.add(this.skin.get("brick"+i, Texture.class));
        }
        /*--- getting all brick images end ---*/

        /*--- box2d init starts ---*/
        Box2D.init();
        world.step(1/60f, 6, 2);
        world.setContactListener(new BallContact());
        //this.debugRenderer = new Box2DDebugRenderer();
        /*--- box2d init ends ---*/

        /*--- setting up the boundaries starts ---*/
        BodyDef tempBodyDef = new BodyDef();
        tempBodyDef.type = BodyDef.BodyType.StaticBody;
        PolygonShape rect = new PolygonShape();
        FixtureDef tempFixtureDef = new FixtureDef();
        tempFixtureDef.shape = rect;
        tempFixtureDef.density = 1f;
        tempFixtureDef.friction = 0f;
        tempFixtureDef.restitution = 1f;
        //Left
        tempBodyDef.position.set(1f*SIZE_MULTIPLICATOR, 0f*SIZE_MULTIPLICATOR);
        rect.setAsBox(1f*SIZE_MULTIPLICATOR, 500f*SIZE_MULTIPLICATOR);
        Body leftBorder = world.createBody(tempBodyDef);
        leftBorder.setAngularDamping(0.1f);
        leftBorder.setLinearDamping(0.1f);
        leftBorder.setUserData("leftWall");
        Fixture leftBoundary = leftBorder.createFixture(tempFixtureDef);
        //Right
        tempBodyDef.position.set(800f*SIZE_MULTIPLICATOR, 0f*SIZE_MULTIPLICATOR);
        Body rightBorder = world.createBody(tempBodyDef);
        rightBorder.setAngularDamping(0.1f);
        rightBorder.setLinearDamping(0.1f);
        rightBorder.setUserData("rightWall");
        Fixture rightBoundary = rightBorder.createFixture(tempFixtureDef);
        //Top
        tempBodyDef.position.set(0f*SIZE_MULTIPLICATOR, 480f*SIZE_MULTIPLICATOR);
        rect.setAsBox(900f*SIZE_MULTIPLICATOR, 1f*SIZE_MULTIPLICATOR);
        Body topBorder = world.createBody(tempBodyDef);
        topBorder.setAngularDamping(0.1f);
        topBorder.setLinearDamping(0.1f);
        topBorder.setUserData("topWall");
        Fixture topBoundary = topBorder.createFixture(tempFixtureDef);
        //Bottom
        tempBodyDef.position.set(0f*SIZE_MULTIPLICATOR, -20f*SIZE_MULTIPLICATOR);
        rect.setAsBox(900f*SIZE_MULTIPLICATOR, 1f*SIZE_MULTIPLICATOR);
        Body bottomBorder = world.createBody(tempBodyDef);
        bottomBorder.setAngularDamping(0.1f);
        bottomBorder.setLinearDamping(0.1f);
        bottomBorder.setUserData("bottomWall");
        Fixture bottomBoundary = bottomBorder.createFixture(tempFixtureDef);

        rect.dispose();
        /*--- setting up the boundaries ends ---*/


        this.bricks = new Group();
        this.balls = new Group();

        //place bricks
        float currentY = lowestBrickY;
        int margin = 2;
        while(currentY < 480){
            placeBricksRow(currentY);
            currentY += 2*Brick.height + margin;
        }

        this.balls.addActor(new Ball(this.world, this.ballImage));
        this.stage.addActor(this.balls);
        this.stage.addActor(this.bricks);
    }

    @Override
    public void render(float delta) {
        /*--- buttons handling start ---*/
        if(input.isKeyPressed(Input.Keys.BACK) || input.isKeyPressed(Input.Keys.ESCAPE)){
            this.stage.clear();
            this.game.getScreen().dispose();
            this.game.setScreen(new MenuScreen(this.game));
        }else if(input.isKeyPressed(Input.Keys.HOME)){
            Gdx.app.exit();
        }
        /*--- buttons handling end ---*/
        /*--- game logic starts ---*/
        if(getLowestBrickY() > lowestBrickY && !bricksMove){
            for (Iterator<Actor> bricksIter = this.bricks.getChildren().iterator(); bricksIter.hasNext();){
              Brick currentBrick = (Brick) bricksIter.next();
              currentBrick.setVSpeed(-0.3f);
            }
            this.bricksMove = true;
            System.out.println("going down start");
        }else if(getLowestBrickY() <= lowestBrickY && bricksMove){
            for (Iterator<Actor> bricksIter = this.bricks.getChildren().iterator(); bricksIter.hasNext();){
                Brick currentBrick = (Brick) bricksIter.next();
                currentBrick.setVSpeed(0);
            }
            this.bricksMove = false;
            System.out.println("going down stop");
        }else if(bricksMove && getHighestBrickY() < (480 - Brick.height*2)){
            placeBricksRow(480);
        }
        /*
        if(getHighestBrickY() < 480){
            placeBricksRow();
        }
        */

        //bricks logic
        for (Iterator<Actor> bricksIter = this.bricks.getChildren().iterator(); bricksIter.hasNext();){
            Brick currentBrick = (Brick) bricksIter.next();
            //removes a brick if it has been marked to be destroy
            if (currentBrick.isToBeDestroyed()){
                this.score += currentBrick.getScore();
                this.world.destroyBody(currentBrick.getBody());
                this.bricks.removeActor(currentBrick);

                /*--- check if score greater than maxscore ---*/
                if(this.score > this.game.getMaxScore()){
                    this.game.setMaxScore(this.score);
                }
                /*--- maxScore check end ---*/
            //game over if a brick is at the same vert-level as hero
            }else if((currentBrick.getY()) <= (this.hero.getY()+4*this.hero.getHeight())/SIZE_MULTIPLICATOR){
                gameOver();
            }
        }

        //if there is no balls left
        if(!this.balls.hasChildren()) {

            if(this.hero.getLives() > 0) {
                //remove life and respawn
                this.hero.moveToinitialPosition();
                this.balls.addActor(new Ball(this.world, this.ballImage));
                this.hero.removeLife();
            }else {
                //game over
                gameOver();
            }
        }else{//check balls if they are to be removed
            for(Iterator<Actor> ballsIter = this.balls.getChildren().iterator(); ballsIter.hasNext(); ){
                Ball currentBall = (Ball) ballsIter.next();
                if(currentBall.isToBeRemoved()){
                    this.world.destroyBody(currentBall.getBody());
                    currentBall.die();
                }
            }
        }
        /*--- game logic ends ---*/

        /*--- object drawing starts---*/
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.stage.act();
        this.stage.draw();
        /*--- object drawing ends ---*/

        /*--- HUD draw starts ---*/
        this.game.getBatch().begin();
        float xPoint = Gdx.graphics.getWidth()/800f;
        float yPoint = Gdx.graphics.getHeight()/480f;
        this.font.draw(this.game.getBatch(), "Lives: " + this.hero.getLives(), 5f*xPoint, 440f*yPoint);
        this.font.draw(this.game.getBatch(), "Score: " + this.score, 5f*xPoint, 415f*yPoint);
        this.font.draw(this.game.getBatch(), "Record: " + this.game.getMaxScore(), 5f*xPoint, 390f*yPoint);
        this.game.getBatch().end();
        /*--- HUD drawing ends ---*/

        /*--- box2D ---*/
        doPhysicsStep(delta);
        //debugRenderer.render(this.world, this.camera.combined);
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
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        /*--- clear world out ---*/
        Array<Body> bodies = new Array<Body>();
        this.world.getBodies(bodies);
        Iterator<Body> i = bodies.iterator();
        while(i.hasNext()){
            Body b = i.next();
            world.destroyBody(b);
        }
        i.remove();
        /*--- done world clearing ---*/
    }

    /*--- box2d helpers start ---*/
    private void doPhysicsStep(float deltaTime) {
        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= TIME_STEP) {
            this.world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }
    }

    private void placeBricksRow(){
        //parameters
        int margin = 7;
        float brickFrequency = 0.5f; //0 to 1. 0 - no brics, 1 - no blank spaces

        for(int col=1; col <= 10; col++) {
            if(MathUtils.random(0f, 1f) < brickFrequency){//place brick or not. Based on "brickFrequency" parameter
                bricks.addActor(new Brick(this.world, margin + col * Brick.width + 41 * (col-1), 480, this.brickImagesArray));
            }
        };
    }

    private void placeBricksRow(float y){
        //parameters
        int margin = 7;
        float brickFrequency = 0.5f; //0 to 1. 0 - no brics, 1 - no blank spaces

        for(int col=1; col <= 10; col++) {
            if(MathUtils.random(0f, 1f) < brickFrequency){//place brick or not. Based on "brickFrequency" parameter
                bricks.addActor(new Brick(this.world, margin + col * Brick.width + 41 * (col-1), y, this.brickImagesArray));
            }
        };
    }

    private float getHighestBrickY(){

        float res = 0;
        for(Iterator<Actor> iter=this.bricks.getChildren().iterator(); iter.hasNext();){
            Brick currentBrick = (Brick) iter.next();
            if(currentBrick.getY() > res){
                res = currentBrick.getY();
            }
        }
        return res;
    }

    private float getLowestBrickY(){

        float res = 480;
        for(Iterator<Actor> iter=this.bricks.getChildren().iterator(); iter.hasNext();){
            Brick currentBrick = (Brick) iter.next();
            if(currentBrick.getY() < res){
                res = currentBrick.getY();
            }
        }
        return res;
    }

    private void gameOver(){
        this.stage.clear();
        /*--- clear world out ---*/
        Array<Body> bodies = new Array<Body>();
        this.world.getBodies(bodies);
        Iterator<Body> i = bodies.iterator();
        while(i.hasNext()){
            Body b = i.next();
            world.destroyBody(b);
        }
        i.remove();
        /*--- done world clearing ---*/
        this.game.setScreen(new GameOverScreen(this.game, this.score));
    }
    /*--- box2d helpers end ---*/
}
