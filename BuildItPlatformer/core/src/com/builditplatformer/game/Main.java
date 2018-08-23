package com.builditplatformer.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.builditplatformer.game.Levels.*;

public class Main extends Game {
    SpriteBatch batch;
    OrthographicCamera camera;
    World world;
    Box2DDebugRenderer debugRenderer;
    float scale;
    Level01 level01;
    Level02 level02;
    AssetManager assetManager;
    int screenWidth, screenHeight;
    boolean levelBegin;

    @Override
    public void create() {
//        Gdx.graphics.setDisplayMode(1600, 900, false);
        screenWidth = 1600;
        screenHeight = 900;
        Gdx.graphics.setWindowedMode(screenWidth, screenHeight);
        scale = Gdx.graphics.getWidth() / 1920f;
        batch = new SpriteBatch();

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = .01f * (1f / scale);
        camera.update();
        levelBegin = true;

        world = new World(new Vector2(0, -24f), false);
        debugRenderer = new Box2DDebugRenderer();

        assetManager = new AssetManager();
        LoadAssets();
        level01 = new Level01(world, 16, 6, camera,  scale, assetManager);
//        level02 = new Level02(world, 16, 6, camera, scale, assetManager);
//        camera.zoom = .1f;
//        camera.update();
    }

    @Override
    public void render() {
        Update();
        Draw();
    }

    public void Update() {
////        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
////            camera.position.y++;
////        }
////        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
////            camera.zoom+=.02f;
////        }
//        if(levelBegin)
//        {
//            camera.zoom -= .0005f;
//
//            if(camera.zoom <= .01f * (1f / scale))
//            {
//                camera.zoom = .01f * (1f / scale);
//                levelBegin = false;
//            }
//            camera.update();
////            return;
//        }
        if(!level01.getGoalReached())
        {
            level01.Update(camera, levelBegin);
            if(level01.getGoalReached())
            {
                world.dispose();
                world = new World(new Vector2(0, -24f * (16f/16f)), false);
                level01.end();
                level02 = new Level02(world, 16, 6, camera, scale, assetManager);
            }
        }
        else
        {
            level02.Update(camera, levelBegin);
        }
//        camera.position.x = ground.getPosition().x;
//        camera.position.y = ground.getPosition().y;
        camera.update();

        world.step(1/60f, 6, 2);
//        level01.rayHandler.update();
    }

    public void Draw() {
        if(!level01.getGoalReached())
        {
            Gdx.gl.glClearColor(.6f, 1, 1, 1);
        }
        else
        {
            Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.end();

        if(!level01.getGoalReached())
        {
            level01.Draw(camera);
        }
        else
        {
            level02.Draw(camera);
        }
//        level02.Draw(camera);

//        level01.rayHandler.setCombinedMatrix(camera.combined);
//        level01.rayHandler.render();
//        level01.rayHandler.updateAndRender();
//        debugRenderer.render(world, camera.combined);
    }


    private void LoadAssets() {
        assetManager.load("WhitePixel.png", Texture.class);
        assetManager.finishLoading();
    }

    public Texture GetTextureAsset(String fileName) {
        return assetManager.get(fileName, Texture.class);
    }
}
