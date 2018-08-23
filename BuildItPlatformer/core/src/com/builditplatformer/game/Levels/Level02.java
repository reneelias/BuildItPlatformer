package com.builditplatformer.game.Levels;

import box2dLight.DirectionalLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.builditplatformer.game.Flame;
import com.builditplatformer.game.PlatformBlock;

import java.awt.*;
import java.util.ArrayList;

public class Level02 extends Level {

    DirectionalLight moon;
    Sprite clouds;
    Vector2[] cloudLocations;
    float originalCloudWidth, originalCloudHeight;
    PointLight goalLight, heroLight;
    float goalLightLength, goalLightIncrememnt;
    ArrayList<PointLight> boxLights;
    ArrayList<PointLight> flameLights;
    Music nightTimeSong;
    Flame[] flame;
    BitmapFont font;
    boolean firstFlame;


    public Level02(World world, int gridSize, int allotedBlocks, OrthographicCamera camera, float scale, AssetManager assetManager)
    {
        super(world, gridSize, allotedBlocks, camera, scale, assetManager);


        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(1, 1, 1, .1123f);
        moon = new DirectionalLight(rayHandler, 2000, new Color(1, 1,1, .125f), -90);
        moon.setSoftnessLength(3.5f);

//        goalLight = new PointLight(rayHandler, 50, Color.GOLD, 2f, goal.getX() - camera.position.x + Gdx.graphics.getWidth() goal.getX() * camera.zoom / 2f, goal.getY() - camera.position.y + Gdx.graphics.getWidth() * camera.zoom / 2f);
        goalLight = new PointLight(rayHandler, 50, Color.GOLD, 1f, 7.65f, 2.95f);
        goalLight.setColor(Color.GOLD.r, Color.GOLD.g, Color.GOLD.b, .85f);
        goalLightIncrememnt = .005f;
        goalLightLength = 1f;


        float x = goal.getX();
        float y = goal.getY();

        platformSprite = new Sprite(GetTextureAsset("MetalPlatform.png"));
        platformSprite.setSize(17f / camera.zoom, 2f / camera.zoom);
        platformSprite.setColor(new Color(.5f, .5f, .5f, 1));
        originalPlatformWidth = platformSprite.getWidth();
        originalPlatformHeight = platformSprite.getHeight();
        platformSprite.setPosition((ground.getPosition().x - camera.position.x + Gdx.graphics.getWidth() * camera.zoom / 2f) / camera.zoom - platformSprite.getWidth() / 2f,
                (ground.getPosition().y - camera.position.y + Gdx.graphics.getHeight() * camera.zoom / 2f) / camera.zoom);

        goal.setOriginalColor(Color.GOLD);

        clouds = new Sprite(GetTextureAsset("Clouds1.png"));
        clouds.setSize(50f / camera.zoom, 27f / camera.zoom);
        originalCloudWidth = clouds.getWidth();
        originalCloudHeight = clouds.getHeight();
        clouds.setPosition(Gdx.graphics.getWidth() * camera.zoom - camera.position.x, Gdx.graphics.getHeight() * camera.zoom / 2f);

        cloudLocations = new Vector2[2];
        cloudLocations[0] = new Vector2(Gdx.graphics.getWidth() * camera.zoom - camera.position.x, Gdx.graphics.getHeight() * camera.zoom / 2f);
        cloudLocations[1] = new Vector2(cloudLocations[0].x + clouds.getWidth(), Gdx.graphics.getHeight() * camera.zoom / 2f);

        boxLights = new ArrayList<PointLight>();
        for(PlatformBlock pb : platformBlocks)
        {
            pb.getPlatformButton().setTexture(GetTextureAsset("ClearBlockMetallic.png"));
//            pb.getPlatformButton().setColor(Color.LIGHT_GRAY);
            boxLights.add(new PointLight(rayHandler, 50,new Color(Color.CYAN.r, Color.CYAN.g, Color.CYAN.b, .9f), 1.5f, pb.getPlatformButton().getX(), pb.getPlatformButton().getX()));
        }

        heroLight = new PointLight(rayHandler, 50, Color.WHITE, .9f, hero.getHeroBody().getPosition().x, hero.getHeroBody().getPosition().y);
        heroLight.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, .75f);

        nightTimeSong = assetManager.get("Nightime.mp3");

        nightTimeSong.setVolume(.65f);
        nightTimeSong.setLooping(true);
        //nightTimeSong.play();

        flame = new Flame[3];
        for(int i = 0; i < flame.length; i++)
        {
            if(i == 0 || i == 2)
            {
                flame[i] = new Flame(GetTextureAsset("Fireball.png"), -9.8f, 16f, 2f);
            }
            else
            {
                flame[i] = new Flame(GetTextureAsset("Fireball.png"), -9.8f, 16f, 4f);
            }
            flame[i].setSize(hero.getWidth(), hero.getWidth() * 1.69f);
            flame[i].setOriginCenter();
//        flame.setPosition(Gdx.graphics.getWidth() / 2f - flame.getOriginX(), Gdx.graphics.getHeight() / 2f - flame.getOriginY());

        }

        flame[0].setStartingPosition(Gdx.graphics.getWidth() / 3f, -flame[0].getHeight());
        flame[1].setStartingPosition(Gdx.graphics.getWidth() / 2f, -flame[0].getHeight());
        flame[2].setStartingPosition(Gdx.graphics.getWidth() * 2f / 3f, -flame[0].getHeight());
//        flame[1].setCycleComplete(true);

        flameLights = new ArrayList<PointLight>();
        for(int i = 0; i < flame.length; i++)
        {
            flameLights.add(i, new PointLight(rayHandler, 50, Color.RED, 1.25f, flame[i].getX() - Gdx.graphics.getWidth() / 2f + flame[i].getOriginX(), flame[i].getY() - Gdx.graphics.getHeight() / 2f + flame[i].getOriginY()));
//            flameLight.setPosition(0, 0);
        }

        font = new BitmapFont();
        firstFlame = false;
    }

    @Override
    public void Update(OrthographicCamera camera, boolean levelBegin) {
        super.Update(camera, levelBegin);

        if(goalLightLength >= 1f || goalLightLength <= .6f)
        {
            goalLightIncrememnt = -goalLightIncrememnt;
        }
        goalLightLength += goalLightIncrememnt;
        goalLight.setDistance(goalLightLength);
//        goalLight.update();
        for(int i = 0, j = 1; i < cloudLocations.length; i++, j--)
        {
            cloudLocations[i].x -= 2;
            if(cloudLocations[i].x < -clouds.getWidth())
            {
                cloudLocations[i].x = cloudLocations[j].x + clouds.getWidth();
            }
        }

        for(int i = 0; i < platformBlocks.size(); i++)
        {
            if(boxLights.get(i).getX() != platformBlocks.get(i).getPlatformBody().getPosition().x)
            {
                boxLights.get(i).setPosition(platformBlocks.get(i).getPlatformBody().getPosition().x, platformBlocks.get(i).getPlatformBody().getPosition().y);
            }
        }

        for(int i = 0; i < flame.length; i++)
        {
//            if(i == 0 || i == 2)
//            {
                flame[i].Update(flameLights.get(i), camera);
//            }
//            else
//            {
//                    flame[i].Update(flameLights.get(i), camera);
//            }
            if(hero.getBoundingRectangle().overlaps(flame[i].getBoundingRectangle()))
            {
                hero.setPosition(hero.getStartingPositionStatic().x, hero.getStartingPositionStatic().y);
                hero.setRotation(0);
                hero.getHeroBody().setTransform(hero.getStartingPositionWorld().x, hero.getStartingPositionWorld().y, 0);
                hero.getHeroBody().setLinearVelocity(0, 0);
                hero.getHeroBody().setAngularVelocity(0);
            }
        }

        if(goalCollision)
        {
            heroLight.setPosition(1000, 1000);
        }
        else
        {
            heroLight.setPosition(hero.getHeroBody().getPosition().x, hero.getHeroBody().getPosition().y);
        }


    }

    @Override
    public void Draw(OrthographicCamera camera) {
        staticBatch.begin();
        clouds.setX(cloudLocations[0].x);
        clouds.draw(staticBatch);
        clouds.setX(cloudLocations[1].x);
        clouds.draw(staticBatch);

        if (showGrid) {
            int index = 0;
            for (Rectangle gridBox : gridBoxes) {
                gridSprite.setPosition(gridBox.getX(), gridBox.getY());
                gridSprite.setSize(gridBox.width, gridBox.height);
//            if(index == selectedIndex) {
//                gridSprite.setColor(Color.RED);
//            } else if(gridSprite.getColor() != Color.WHITE) {
//                gridSprite.setColor(Color.WHITE);
//            }
                gridSprite.draw(staticBatch);
                index++;
            }
        }
        for (PlatformBlock platformBlock : platformBlocks) {
            platformBlock.Draw(staticBatch);
        }

        platformSprite.draw(staticBatch);

        goal.draw(staticBatch);
        for(Flame f : flame)
        {
            f.draw(staticBatch);
        }
        hero.draw(staticBatch);

        staticBatch.end();

        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();

        staticBatch.begin();
        playButton.draw(staticBatch);
//        font.draw(staticBatch, String.format("FlameTime: %f", flameTimeElapsed), 0, 10);
//        font.draw(staticBatch, String.format("Row: %d, Col: %d", (int)rowCol.x, (int)rowCol.y),
//                textLocation.x, textLocation.y + font.getLineHeight());
//        font.draw(staticBatch, String.format("FPS: %d", Gdx.graphics.getFramesPerSecond()),
//                Gdx.graphics.getWidth() - 350f * scale, Gdx.graphics.getHeight() - 50f * scale);
//        font.draw(staticBatch, String.format("X: %f, Y: %f", GestureManager.InputLocation().x, GestureManager.InputLocation().y), GestureManager.InputLocation().x, GestureManager.InputLocation().y);
//        leftButton.draw(staticBatch);
//        rightButton.draw(staticBatch);
//        leftJumpButton.draw(staticBatch);
//        rightJumpButton.draw(staticBatch);
        staticBatch.end();
    }

    @Override
    protected void LoadAssets() {
        super.LoadAssets();
        assetManager.load("Clouds1.png", Texture.class);
        assetManager.load("Nightime.mp3", Music.class);
        assetManager.load("ClearBlockMetallic.png", Texture.class);
        assetManager.load("MetalPlatform.png", Texture.class);
        assetManager.load("Fireball.png", Texture.class);
        assetManager.finishLoading();
    }

}
