package com.infinite.android;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.infinite.android.actors.Ball;
import com.infinite.android.actors.Brick;

public class BallContact implements ContactListener {

    private Brick brick;
    private Ball ball;
    private boolean bottomWall;

    public BallContact(){
        resetParameters();
    }

    @Override
    public void beginContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    @Override
    public void endContact(Contact contact) {

        getObject(contact.getFixtureA().getBody());
        getObject(contact.getFixtureB().getBody());

        //ball & brick
        if(this.brick != null && this.ball != null){
            if(this.brick.getHitPoints() - this.ball.getPower() > 0){
                this.brick.setHitPoints(this.brick.getHitPoints() - this.ball.getPower());
            }else {
                this.brick.markDestroy();
            }
        //ball and wall to the bottom
        }else if(this.bottomWall && this.ball !=null){
            this.ball.markToRemove();
        }

        resetParameters();
    }

    /*--- helpers start ---*/
    private void getObject(Body body){
        String className = body.getUserData().getClass().getCanonicalName();

        if(className.equals("com.infinite.android.actors.Brick")){
            this.brick = (Brick) body.getUserData();
        }else if(className.equals("com.infinite.android.actors.Ball")){
            this.ball = (Ball) body.getUserData();
        }else if(className.equals("java.lang.String")){//ball hits a wall
            if(body.getUserData().equals("bottomWall")){
                this.bottomWall = true;
            }
        }
    }

    private void resetParameters(){
        this.ball = null;
        this.brick = null;
        this.bottomWall = false;
    }
    /*--- helpers end ---*/
}
