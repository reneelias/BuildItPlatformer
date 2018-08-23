package com.builditplatformer.game.Levels;

import box2dLight.DirectionalLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.builditplatformer.game.PlatformBlock;


public class Level01 extends Level {

    Sprite clouds;
    DirectionalLight sun;
    Vector2[] cloudLocations;
    Music wind, birds, song;

    float originalCloudWidth, originalCloudHeight;

    public Level01(World world, int gridSize, int allotedBlocks, OrthographicCamera camera, float scale, AssetManager assetManager)
    {
        super(world, gridSize, allotedBlocks, camera, scale, assetManager);

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(1, 1, 1, .24f);
        sun = new DirectionalLight(rayHandler, 2000, new Color(1, 1,1, .45f), -90);
        sun.setSoftnessLength(3.5f);

        LoadAssets();

        platformSprite = new Sprite(GetTextureAsset("Platform.png"));
        platformSprite.setSize(17f / camera.zoom, 2f / camera.zoom);
        originalPlatformWidth = platformSprite.getWidth();
        originalPlatformHeight = platformSprite.getHeight();
        platformSprite.setPosition((ground.getPosition().x - camera.position.x + Gdx.graphics.getWidth() * camera.zoom / 2f) / camera.zoom - platformSprite.getWidth() / 2f,
                (ground.getPosition().y - camera.position.y + Gdx.graphics.getHeight() * camera.zoom / 2f) / camera.zoom);


        clouds = new Sprite(GetTextureAsset("Clouds1.png"));
        clouds.setSize(50f / camera.zoom, 27f / camera.zoom);
        originalCloudWidth = clouds.getWidth();
        originalCloudHeight = clouds.getHeight();
        clouds.setPosition(Gdx.graphics.getWidth() * camera.zoom - camera.position.x, Gdx.graphics.getHeight() * camera.zoom / 2f);

        cloudLocations = new Vector2[2];
        cloudLocations[0] = new Vector2(Gdx.graphics.getWidth() * camera.zoom - camera.position.x, Gdx.graphics.getHeight() * camera.zoom / 2f);
        cloudLocations[1] = new Vector2(cloudLocations[0].x + clouds.getWidth(), Gdx.graphics.getHeight() * camera.zoom / 2f);


        wind = assetManager.get("Windy Desert.mp3");
        birds = assetManager.get("Bird Ambience.mp3");
        song = assetManager.get("Equal_Soul.mp3");

        wind.setVolume(.75f);
        wind.setLooping(true);
        wind.play();
        birds.setVolume(.75f);
        birds.setLooping(true);
        birds.play();
        song.setVolume(.5f);
        song.setLooping(true);
        //song.play();
    }

    @Override
    public void Update(OrthographicCamera camera, boolean levelBegin) {
        super.Update(camera, levelBegin);

        for(int i = 0, j = 1; i < cloudLocations.length; i++, j--)
        {
            cloudLocations[i].x -= 2;
            if(cloudLocations[i].x < -clouds.getWidth())
            {
                cloudLocations[i].x = cloudLocations[j].x + clouds.getWidth();
            }
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
        hero.draw(staticBatch);

        staticBatch.end();

        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();

        staticBatch.begin();
        playButton.draw(staticBatch);
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

    public void end()
    {
        wind.stop();
        birds.stop();
        song.stop();
    }


    @Override
    protected void LoadAssets() {
        super.LoadAssets();
        assetManager.load("Clouds1.png", Texture.class);
        assetManager.load("Windy Desert.mp3", Music.class);
        assetManager.load("Equal_Soul.mp3", Music.class);
        assetManager.load("Bird Ambience.mp3", Music.class);
        assetManager.finishLoading();
    }

    protected void UnloadAssets()
    {
        assetManager.unload("Clouds1.png");
        assetManager.unload("Windy Desert.mp3");
        assetManager.unload("Equal_Soul.mp3");
        assetManager.unload("Bird Ambience.mp3");
    }

}
