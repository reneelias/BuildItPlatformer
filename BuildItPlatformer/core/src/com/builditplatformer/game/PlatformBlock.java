package com.builditplatformer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.w3c.dom.css.Rect;
import sun.security.krb5.internal.crypto.Des;

/**
 * Created by rene__000 on 7/2/2015.
 */
public class PlatformBlock {

    GameButtons platformButton;

    public GameButtons getPlatformButton() {
        return platformButton;
    }

    public Body getPlatformBody() {
        return platformBody;
    }

    public World getWorld() {
        return world;
    }

    public float getScale() {
        return scale;
    }

    public boolean isClicked() {
        return clicked;
    }

    public Vector2 getPlatformBoxSize() {
        return platformBoxSize;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public Rectangle getTopBoundingBox() {
        return topBoundingBox;
    }

    Body platformBody;


    World world;
    float scale;
    boolean clicked;
    Vector2 platformBoxSize;
    boolean destroyed;
    Rectangle topBoundingBox;

    public PlatformBlock(World world, Rectangle destinationRectangle, OrthographicCamera camera, Texture spriteTexture, float scale) {

        PolygonShape platformShape = new PolygonShape();
        platformBoxSize = new Vector2((destinationRectangle.getWidth() * camera.zoom) / 2f, (destinationRectangle.getHeight() * camera.zoom) / 2f);
        platformShape.setAsBox(platformBoxSize.x, platformBoxSize.y);

        FixtureDef fixtureDef;
        BodyDef bodyDef;

        fixtureDef = new FixtureDef();
        fixtureDef.friction = .1f;
        fixtureDef.restitution = 0f;
        fixtureDef.density = .2f;
        fixtureDef.shape = platformShape;

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.x = destinationRectangle.getX() * camera.zoom + camera.position.x - Gdx.graphics.getWidth() / 2f * camera.zoom + platformBoxSize.x;
        bodyDef.position.y = destinationRectangle.getY() * camera.zoom + camera.position.y - Gdx.graphics.getHeight()  / 2f * camera.zoom + platformBoxSize.y;
        if(Gdx.graphics.getWidth() == 1920) {
            bodyDef.position.y -= .1f;
        }

        platformBody = world.createBody(bodyDef);
        platformBody.createFixture(fixtureDef);
        platformShape.dispose();

        this.world = world;
        this.scale = scale;

        platformButton = new GameButtons(spriteTexture, true, new Vector2(destinationRectangle.getX(), destinationRectangle.getY()));
        platformButton.setSize(destinationRectangle.getWidth(), destinationRectangle.getHeight());
        clicked = false;
        destroyed = true;

        topBoundingBox = new Rectangle(platformButton.getX(),
                platformButton.getY() + platformButton.getHeight() - 1f, platformButton.getWidth(), 4f * scale);
    }

    public boolean Clicked() {
        return clicked;
    }

    public void Update() {
        platformButton.Update();
        if(platformButton.Tapped()) {
            Destroy();
            clicked = true;
        } else {
            clicked = false;
        }

        topBoundingBox.setPosition(platformButton.getX(),
                platformButton.getY() + platformButton.getHeight());
    }

    public void Transport(Rectangle destinationRectangle, OrthographicCamera camera) {
        platformBody.setTransform(destinationRectangle.getX() * camera.zoom + camera.position.x - Gdx.graphics.getWidth() / 2f * camera.zoom + platformBoxSize.x,
                destinationRectangle.getY() * camera.zoom + camera.position.y - Gdx.graphics.getHeight()  / 2f * camera.zoom + platformBoxSize.y, 0);
        platformButton.setPosition(destinationRectangle.getX(), destinationRectangle.getY());
        destroyed = false;
    }

    public void Destroy()
    {
        platformBody.setTransform(-100, 0, 0);
        platformButton.setPosition(-1000, 0);
        destroyed = true;
    }


    public void Draw(SpriteBatch batch) {
        if(platformButton != null) {
           platformButton.draw(batch);
        }
    }

}
