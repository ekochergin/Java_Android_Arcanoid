package com.infinite.android.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.infinite.android.ArkanoidInfinite;

public class Brick extends Actor {

    TextureRegion currentImage;

    private Body body;
    private boolean flagDestroy;
    private int score;
    private int hitPoints;

    private final float SIZE_MULTIPLICATOR = ArkanoidInfinite.SIZE_MULTIPLICATOR;

    public static float width = 38f;
    public static float height = 16f;

    private float vSpeed;
    private float hSpeed;

    private float x;
    private float y;

    private TextureRegion image;
    private Array<Texture> imagesArray;

    //public Brick(World world, float x, float y, Texture image){
    public Brick(World world, float x, float y, Array<Texture> imagesArray){

        this.flagDestroy = false;
        //this.vSpeed = -0.5f;
        this.vSpeed = 0;
        this.hSpeed = 0;
        this.x = x;
        this.y = y;

        this.hitPoints = MathUtils.random(1, ArkanoidInfinite.MAX_BRICK_LEVEL);
        this.imagesArray = imagesArray;
        this.image = new TextureRegion(imagesArray.get(this.hitPoints - 1));
        this.score = 100 * this.hitPoints;

        /*--- Box2D ---*/
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x*SIZE_MULTIPLICATOR, y*SIZE_MULTIPLICATOR);
        this.body = world.createBody(bodyDef);
        this.body.setUserData(this);
        this.body.setLinearVelocity(this.hSpeed, this.vSpeed);
        PolygonShape rect = new PolygonShape();
        rect.setAsBox(this.width*SIZE_MULTIPLICATOR, this.height*SIZE_MULTIPLICATOR);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = rect;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 1f;


        Fixture fixture = body.createFixture(fixtureDef);
        body.setLinearDamping(0.1f);
        body.setAngularDamping(0.1f);
        rect.dispose();
        /*--- Box2D ---*/
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        float width = 2 * this.getWidth() * SIZE_MULTIPLICATOR;
        float height = 2 * this.getHeight() * SIZE_MULTIPLICATOR;
        float imageX = this.body.getPosition().x - width/2;
        float imageY = this.body.getPosition().y - height/2;

        batch.draw(this.image, imageX, imageY, width, height);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        //reset world coords based on new box2d coords
        this.x = this.body.getPosition().x / this.SIZE_MULTIPLICATOR;
        this.y = this.body.getPosition().y / this.SIZE_MULTIPLICATOR;

    }

    /*--- get/set start ---*/
    public Body getBody(){return this.body;}

    public void markDestroy(){
        this.flagDestroy = true;
    }

    public boolean isToBeDestroyed(){return this.flagDestroy;}

    @Override
    public float getWidth(){return this.width;}

    @Override
    public float getHeight(){return this.height;}

    @Override
    public float getX() {
        return x;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    public int getScore(){return this.score;}

    public int getHitPoints(){return this.hitPoints;}

    public void setHitPoints(int newHitPoints){
        this.hitPoints = newHitPoints;
        this.image = new TextureRegion(this.imagesArray.get(this.hitPoints-1));
    }

    public void setVSpeed(float newVSpeed){this.vSpeed = newVSpeed; this.body.setLinearVelocity(this.hSpeed, this.vSpeed);}
    /*--- get/set end ---*/
}
