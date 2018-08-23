package com.builditplatformer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Random;

/**
 * Created by rene__000 on 7/7/2015.
 */
public class Goal extends Sprite {
    Body goalBody;

    World world;
    float scale;
    boolean clicked;
    Vector2 goalBoxSize;
    boolean destroyed;
    Rectangle topBoundingBox;
    float alpha;
    float alphaIncrement;
    Random random;
    Color originalColor;
    public void revertColor()
    {
        setColor(originalColor);
    }

    public void setOriginalColor(Color color)
    {
        originalColor = color;
    }


    public Goal(Texture texture, Rectangle destinationRectangle, World world, OrthographicCamera camera, float scale, Color color){
        super(texture);
        setPosition(destinationRectangle.x, destinationRectangle.y);
        setSize(destinationRectangle.width, destinationRectangle.height);
        setOriginCenter();

        setColor(color);
        originalColor = color;

//        PolygonShape goalShape = new PolygonShape();
//        goalBoxSize = new Vector2((destinationRectangle.getWidth() * camera.zoom) / 2f, (destinationRectangle.getHeight() * camera.zoom) / 2f);
//        goalShape.setAsBox(goalBoxSize.x, goalBoxSize.y);
//
//        FixtureDef fixtureDef;
//        BodyDef bodyDef;
//
//        fixtureDef = new FixtureDef();
//        fixtureDef.friction = .6f;
//        fixtureDef.restitution = 0f;
//        fixtureDef.density = .2f;
//        fixtureDef.shape = goalShape;
//
//        bodyDef = new BodyDef();
//        bodyDef.type = BodyDef.BodyType.StaticBody;
//        bodyDef.position.x = destinationRectangle.getX() * camera.zoom + camera.position.x - Gdx.graphics.getWidth() / 2f * camera.zoom + goalBoxSize.x;
//        bodyDef.position.y = destinationRectangle.getY() * camera.zoom + camera.position.y - Gdx.graphics.getHeight()  / 2f * camera.zoom + goalBoxSize.y;
//        if(Gdx.graphics.getWidth() == 1920) {
//            bodyDef.position.y -= .1f;
//        }
//
//        goalBody = world.createBody(bodyDef);
//        goalBody.createFixture(fixtureDef);
//        goalShape.dispose();
        random = new Random();
        this.world = world;
        this.scale = scale;
        alphaIncrement = -.005f;
        alpha = .8f;
    }

    public void Update(OrthographicCamera camera){
        rotate(2f);
        if(alpha <= .1f || alpha >= 1f) {
            alphaIncrement = -alphaIncrement;
//            setColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), alpha));
        }
        alpha += alphaIncrement;
        setAlpha(alpha);
    }

    public void Draw(SpriteBatch batch) {
        draw(batch);
    }
}
