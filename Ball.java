package com.infinite.android.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.infinite.android.ArkanoidInfinite;

import static com.badlogic.gdx.Gdx.input;

public class Ball extends Actor {

    private final float SIZE_MULTIPLICATOR = ArkanoidInfinite.SIZE_MULTIPLICATOR;

    private Body body;
    private boolean stickToPlatform;
    private boolean readyToStart;
    private final float startVertSpeed = 30f;
    private final float maxSpeed = 60f;
    private final float minSpeed = 15f;
    private boolean toBeRemoved;
    private TextureRegion image;
    private float ballRadius = 10f*SIZE_MULTIPLICATOR;
    private int power;

    public Ball(World world, Texture image){

        this.toBeRemoved = false;
        this.image = new TextureRegion(image);
        this.power = 1;

        /*--- Box2D ---*/
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(375f*SIZE_MULTIPLICATOR, 40f*SIZE_MULTIPLICATOR);
        this.body = world.createBody(bodyDef);
        this.body.setUserData(this);
        CircleShape ball = new CircleShape();
        ball.setRadius(ballRadius);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = ball;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 1f;
        body.setAngularDamping(0);
        body.setLinearDamping(0);
        body.setBullet(true);


        Fixture fixture = body.createFixture(fixtureDef);
        body.setLinearDamping(0);
        body.setAngularDamping(0);
        ball.dispose();
        /*--- Box2D ---*/

        this.stickToPlatform = true;
        this.readyToStart = false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        float x = this.body.getPosition().x - this.ballRadius;
        float y = this.body.getPosition().y - this.ballRadius;
        float heightWidth = 2 * this.ballRadius;
        float rotation = this.body.getAngle() * (180f/3.1415f);

        batch.draw(this.image, x, y, this.ballRadius, this.ballRadius, heightWidth, heightWidth, 1, 1, rotation);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        /*--- slow the ball down if it is too fast ---*/
        if(this.body.getLinearVelocity().x > this.maxSpeed){
            this.body.setLinearVelocity(maxSpeed, this.body.getLinearVelocity().y);
        }
        if(this.body.getLinearVelocity().y > this.maxSpeed){
            this.body.setLinearVelocity(this.body.getLinearVelocity().x, maxSpeed);
        }
        /*--- end of slowing down the ball ---*/

        /*--- speed ball up if it is too slow ---*/
        if(this.body.getLinearVelocity().x < this.minSpeed && this.body.getLinearVelocity().x > 0 && !this.stickToPlatform){
            this.body.setLinearVelocity(minSpeed, this.body.getLinearVelocity().y);
        }
        if(this.body.getLinearVelocity().y < this.minSpeed && this.body.getLinearVelocity().y > 0){
            this.body.setLinearVelocity(this.body.getLinearVelocity().x, minSpeed);
        }
        /*--- end of speeding up the ball ---*/

        /*--- moving with hero if sticked to it ---*/
        if(stickToPlatform){
            Hero hero = (Hero) this.getStage().getActors().get(0);
            this.body.setTransform(hero.getX(), this.body.getPosition().y, 0);
        }
        /*--- end of moving with hero ---*/

        /*--- start on click released ---*/
        if(stickToPlatform){
            if(Gdx.input.isTouched()) {
                this.readyToStart = true;
            }else{
                if(this.readyToStart){
                    this.stickToPlatform = false;
                    this.body.setLinearVelocity(0f, startVertSpeed);
                }
            }
            if(input.isKeyPressed(Input.Keys.SPACE)){
                this.stickToPlatform = false;
                this.body.setLinearVelocity(0f, startVertSpeed);
            }
        }
        /*----------------------*/
    }

    /*--- get/set start ---*/
    public boolean isStickedToPlatform(){return this.stickToPlatform;}

    public void stickToPlatform(){this.stickToPlatform =true;}

    public void markToRemove(){this.toBeRemoved = true;}

    public boolean isToBeRemoved(){return this.toBeRemoved;}

    public Body getBody(){return this.body;}

    public int getPower(){return this.power;}

    public void setPower(int newPower){this.power = newPower;}
    /*--- get/set end ---*/

    public void die(){
        this.body = null;
        this.remove();
    }
}
