package com.builditplatformer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;

/**
 * Created by rene__000 on 7/4/2015.
 */
public class Hero extends Sprite {

    World world;
    Body heroBody;
    Vector2 heroBoxSize;
    Vector2 startingPositionStatic;
    Vector2 startingPositionWorld;

    public World getWorld() {
        return world;
    }

    public Body getHeroBody() {
        return heroBody;
    }

    public Vector2 getHeroBoxSize() {
        return heroBoxSize;
    }

    public Vector2 getStartingPositionStatic() {
        return startingPositionStatic;
    }

    public Vector2 getStartingPositionWorld() {
        return startingPositionWorld;
    }

    public Rectangle getBottomBoundingBox() {
        return bottomBoundingBox;
    }

    public float getScale() {
        return scale;
    }

    public float getMovementForce() {
        return movementForce;
    }

    public float getJumpForce() {
        return jumpForce;
    }

    public float getOriginalWidth() {
        return originalWidth;
    }

    public float getOriginalHeight() {
        return originalHeight;
    }

    Rectangle bottomBoundingBox;
    float scale;
    float movementForce, jumpForce;
    public float originalWidth, originalHeight;
    float jumpVelocity;

    public Hero(Texture texture, Rectangle destinationRectangle, World world, OrthographicCamera camera, float scale, float gridSize) {
        super(texture);

        this.scale = scale;

        setPosition(destinationRectangle.x, destinationRectangle.y);
        setSize(destinationRectangle.width - 7f * scale, destinationRectangle.height - 7f * scale);
        originalWidth = getWidth();
        originalHeight = getHeight();
        setOriginCenter();
        startingPositionStatic = new Vector2(getX(), getY());
        bottomBoundingBox = new Rectangle(getX(), getY(), getWidth(), 4f * scale);


        PolygonShape platformShape = new PolygonShape();
        heroBoxSize = new Vector2(((destinationRectangle.getWidth() - 7f * scale) * camera.zoom) / 2f, ((destinationRectangle.getHeight() - 7f * scale) * camera.zoom) / 2f);
        platformShape.setAsBox(heroBoxSize.x, heroBoxSize.y);


        FixtureDef fixtureDef;
        BodyDef bodyDef;

        float worldScale = 1280f / Gdx.graphics.getWidth();

        fixtureDef = new FixtureDef();
        fixtureDef.friction = .6f;
        fixtureDef.restitution = 0f;
        fixtureDef.density = .0725f;
        fixtureDef.shape = platformShape;

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.x = destinationRectangle.getX() * camera.zoom + camera.position.x - Gdx.graphics.getWidth() / 2f * camera.zoom + heroBoxSize.x;
        bodyDef.position.y = destinationRectangle.getY() * camera.zoom + camera.position.y - Gdx.graphics.getHeight() / 2f * camera.zoom + heroBoxSize.y;
        if (Gdx.graphics.getWidth() == 1920) {
            bodyDef.position.y -= .1f;
        }

        startingPositionWorld = new Vector2(bodyDef.position.x, bodyDef.position.y);

        heroBody = world.createBody(bodyDef);
        heroBody.createFixture(fixtureDef);
        platformShape.dispose();

        this.world = world;

        float screenWidth = Gdx.graphics.getWidth();

//        if(screenWidth == 1280) {
        float hello = heroBody.getMass();

        jumpVelocity = (float)Math.sqrt(-2f * world.getGravity().y * ((destinationRectangle.getHeight() * 2f  + 16f * scale) * camera.zoom));
        float movementScale = heroBody.getMass() / .0661218f;
        movementForce = 1.7f * movementScale;
        jumpForce = 41f * movementScale;

    }

    public void Update(OrthographicCamera camera, boolean canJump, boolean leftPressed, boolean rightPressed, boolean jumpPressed, Sound jumpSound) {
        //PC Controls
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            heroBody.applyForceToCenter(movementForce, 0, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            heroBody.applyForceToCenter(-movementForce, 0, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && Math.abs(heroBody.getLinearVelocity().y) < .5f && canJump) {
//            heroBody.applyForceToCenter(0, jumpForce, true);
            heroBody.setLinearVelocity(heroBody.getLinearVelocity().x, jumpVelocity);
            jumpSound.play(.75f);
        }

        //Mobile Controls
        if (rightPressed) {
            heroBody.applyForceToCenter(movementForce, 0, true);
        }
        if (leftPressed) {
            heroBody.applyForceToCenter(-movementForce, 0, true);
        }
        if (jumpPressed && Math.abs(heroBody.getLinearVelocity().y) < .5f && canJump) {
            heroBody.applyForceToCenter(0, jumpForce, true);
        }


        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            setPosition(startingPositionStatic.x, startingPositionStatic.y);
            setRotation(0);
            heroBody.setTransform(startingPositionWorld.x, startingPositionWorld.y, 0);
            heroBody.setLinearVelocity(0, 0);

            heroBody.setAngularVelocity(0);
        }
        heroBody.setAngularVelocity(0);
        heroBody.setTransform(heroBody.getPosition().x, heroBody.getPosition().y, 0);


        bottomBoundingBox.setPosition(getX(), getY() - 2f * scale);

        float addition;
//        if(scale == 1f) {
//            addition = 15f;
//        } else {
        addition = 0;
//        }
        setPosition((heroBody.getPosition().x - camera.position.x + Gdx.graphics.getWidth() * camera.zoom / 2f) / camera.zoom - getWidth() / 2f,
                (heroBody.getPosition().y - camera.position.y + Gdx.graphics.getHeight() * camera.zoom / 2f) / camera.zoom - getHeight() / 2f + addition);
        setRotation(heroBody.getAngle() * MathUtils.radiansToDegrees);
    }

    public void Draw(SpriteBatch batch) {
        draw(batch);
    }
}
