package com.builditplatformer.game;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Flame extends Sprite {

    float scale;
    float flameGravity;
    float flameSpeed;
    float flameTimeElapsed, flameWaitTime;
    float startingX, startingY;

    boolean waiting;
    boolean cycleCompleted, previousCycleCompleted;

    public boolean isCycleCompleted() {

        return cycleCompleted;
    }

    public void setCycleComplete(boolean cycleCompleted){
        this.cycleCompleted = cycleCompleted;
    }

    public boolean isWaiting()
    {
        return waiting;
    }
    public void setWaiting(boolean waiting)
    {
        this.waiting = waiting;
    }


    public void setStartingPosition(float startX, float startY)
    {
        startingX = startX;
        startingY = startY;
        setPosition(startingX, startingY);
    }

    public Flame(Texture texture, float flameGravity, float flameSpeed, float flameWaitTime)
    {
        super(texture);
        this.flameGravity = flameGravity;
        this.flameSpeed = flameSpeed;
        flameTimeElapsed = 0;
        cycleCompleted = false;
        this.flameWaitTime = flameWaitTime;
    }

     public void Update(PointLight flameLight, OrthographicCamera camera)
     {
         setY(getY() + flameSpeed);
//        flameLight.setPosition(flameLight.getX(), flame.getY() - Gdx.graphics.getHeight() / 2f + flame.getOriginY() * camera.zoom);
         flameLight.setPosition((getX() - Gdx.graphics.getWidth() / 2f) * camera.zoom + getOriginX() * camera.zoom, (getY() - Gdx.graphics.getHeight() / 2f) * camera.zoom + getOriginY() * camera.zoom);

         flameGravity = -9.8f * (Gdx.graphics.getDeltaTime() / 1f);
         flameSpeed += flameGravity;


         if(getY() > Gdx.graphics.getHeight() / 3f )
         {
             if(getRotation() < 180f)
             {
                 setX(getX() - .65f);
                 rotate(1.5f);
                 if(getRotation() > 180f)
                 {
                     setRotation(180);
                 }
             }
         }
         if(getY() < 0)
         {
             flameTimeElapsed += Gdx.graphics.getDeltaTime();
             waiting = true;
             cycleCompleted = true;
         }

         if(flameTimeElapsed >  flameWaitTime)
         {
             flameTimeElapsed = 0;
             setRotation(0);
             setY(startingY);
             setX(startingX);
             flameSpeed = 16f;
             cycleCompleted = false;
         }

         previousCycleCompleted = cycleCompleted;
     }


}
