package com.infinite.android.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.infinite.android.ArkanoidInfinite;


public class Hero extends Actor {
    private Body body;

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 480;
    private final float SIZE_MULTIPLICATOR = ArkanoidInfinite.SIZE_MULTIPLICATOR;
    private int lives;
    private boolean swiping;

    private TextureRegion image;


    public Hero(World world, int livesQty, Texture texture) {
        this.lives = livesQty;
        this.image = new TextureRegion(texture);

        /*--- Box2D ---*/
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;//DynamicBody;
        bodyDef.position.set(SIZE_MULTIPLICATOR*(800-this.getWidth())/2 , 20*SIZE_MULTIPLICATOR);
        this.body = world.createBody(bodyDef);
        this.body.setUserData(this);
        PolygonShape rect = new PolygonShape();
        rect.setAsBox(50f*SIZE_MULTIPLICATOR, 10f*SIZE_MULTIPLICATOR);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = rect;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 1f;
        Fixture fixture = body.createFixture(fixtureDef);
        System.out.println("x: " + bodyDef.position.x + " y: " + bodyDef.position.y);

        rect.dispose();
        /*--- Box2D ---*/
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.draw(this.image, this.body.getPosition().x - this.getWidth(), this.body.getPosition().y - this.getHeight(), 2*this.getWidth(), 2*this.getHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        final int SPEED = 50;

        //convert stage coords (width is always 800) into screen coords
        float xTouched = ( Gdx.input.getX() * 800/Gdx.graphics.getWidth())*SIZE_MULTIPLICATOR;

        /*--- input handling starts ---*/
        //follow swipe if user swipes
        if(Gdx.input.isTouched() && !this.swiping && (xTouched > (this.getX() - this.getWidth())) && (xTouched < (this.getX() + this.getWidth()))){
            this.swiping = true;
            //this.prevMouseX = xTouched;
        }else if(this.swiping){
            if(xTouched < this.getX()-1){
                this.body.setLinearVelocity(-1*SPEED, 0);
            }else if (xTouched > this.getX()+1){
                this.body.setLinearVelocity(SPEED, 0);
            }else{
                this.body.setLinearVelocity(0, 0);
            }
        }//if user clicks outside of hero
        else if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || (Gdx.input.isTouched() && xTouched < (this.body.getPosition().x - this.getWidth()/2))){
            this.body.setLinearVelocity(-1*SPEED, 0);
        }else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || (Gdx.input.isTouched() && xTouched > (this.body.getPosition().x + this.getWidth()/2))){
            this.body.setLinearVelocity(SPEED, 0);
        }
        //stop if user has button released
        if(!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !Gdx.input.isTouched()){
            this.body.setLinearVelocity(0f, 0f);
            this.swiping = false;
        }
        /*--- input handling ends ---*/

        /*--- check for screen boundaries ---*/
        if((this.body.getPosition().x  < this.getWidth() && this.body.getLinearVelocity().x < 0) ||
           (this.body.getPosition().x  > (799*SIZE_MULTIPLICATOR-this.getWidth()) && this.body.getLinearVelocity().x > 0)){
            this.body.setLinearVelocity(0f, 0f);
        }
        /*--- end boundaries checking ---*/
    }

    @Override
    public boolean remove() {
        return super.remove();
    }

    @Override
    public float getWidth() {
        return 50f*SIZE_MULTIPLICATOR;
    }

    @Override
    public float getHeight() {
        return 10f*SIZE_MULTIPLICATOR;
    }

    @Override
    public float getX(){ return this.body.getPosition().x;}

    @Override
    public float getY(){ return this.body.getPosition().y;}

    /*--- get/set start ---*/
    public int getLives(){return this.lives;}

    public void removeLife(){
        this.lives -= 1;
    }

    public void moveToinitialPosition(){
        this.body.setTransform(SIZE_MULTIPLICATOR*(800-this.getWidth())/2 , 20*SIZE_MULTIPLICATOR, 0);
    }
    /*--- get/set end ---*/
}
