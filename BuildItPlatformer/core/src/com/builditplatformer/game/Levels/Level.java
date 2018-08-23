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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.builditplatformer.game.*;

import java.util.ArrayList;

/**
 * Created by rene__000 on 7/2/2015.
 */
public class Level {

    int gridSize;
    Body ground;
    FixtureDef groundDef;
    BodyDef groundBodyDef;
    World world;
    Sprite gridSprite;
    Sprite platformSprite;
    float scale;
    AssetManager assetManager;
    SpriteBatch staticBatch;
    Vector2 viewport;
    float viewportDifference;
    ArrayList<Rectangle> gridBoxes;
    boolean currentlyPressed;
    boolean previousPressed;
//    FreeTypeFontGenerator ftfGen;
//    FreeTypeFontGenerator.FreeTypeFontParameter ftfpar;
    BitmapFont font;
    Vector2 textLocation;
    Vector2 rowCol;
    int selectedIndex;
    GameButtons playButton, leftButton, rightButton, leftJumpButton, rightJumpButton;
    ArrayList<PlatformBlock> platformBlocks;
    int allotedBlocks;
    RayHandler rayHandler;
    Hero hero;
    boolean showGrid;
    Goal goal;
    PlatformBlock currentPlatformBlock;
    Sound boxClickSound, jumpSound, goalReachedSound;
    boolean goalCollision, previousGoalCollision;
    float originalZoom;
    boolean previousLevelBegin;
    float goalTimer;
    float originalPlatformWidth, originalPlatformHeight;

    public boolean getGoalReached()
    {
        return goalTimer >= 1f;
    }


    public Level(World world, int gridSize, int allotedBlocks, OrthographicCamera camera, float scale, AssetManager assetManager) {
        this.gridSize = gridSize;

        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(8.5f, 2f);

        float worldScale = 1280f / Gdx.graphics.getWidth();

        groundDef = new FixtureDef();
        groundDef.friction = .6f;
        groundDef.restitution = 0f;
        groundDef.density = .2f;
        groundDef.shape = groundShape;

        groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.x = camera.position.x;
        groundBodyDef.position.y = camera.position.y - 5.7f;

        ground = world.createBody(groundBodyDef);
        ground.createFixture(groundDef);
        groundShape.dispose();

        this.world = world;
        this.scale = scale;
        this.assetManager = assetManager;
        LoadAssets();

        gridSprite = new Sprite(GetTextureAsset("BorderedBox.png"));

        viewport = new Vector2(1640f * scale, 800f * scale);
        viewportDifference = Gdx.graphics.getWidth() - viewport.x;

        staticBatch = new SpriteBatch();

        gridBoxes = new ArrayList<Rectangle>();

        int xIndex = 0;
        for (float x = viewportDifference / 2f; x < viewportDifference / 2f + viewport.x; x += viewport.x / gridSize) {
            for (float y = 170f * scale; y < viewport.y; y += viewport.x / gridSize) {
                gridBoxes.add(new Rectangle(x, y, viewport.x / gridSize, viewport.x / gridSize));
            }
            xIndex++;
            if (xIndex == gridSize) {
                break;
            }
        }

//        ftfGen = new FreeTypeFontGenerator(Gdx.files.internal("goodtimesrg.ttf"));
//        ftfpar = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        ftfpar.size = 40;
//        font = ftfGen.generateFont(ftfpar);
        textLocation = new Vector2(-1, -1);
        rowCol = new Vector2();
        selectedIndex = -1;

        playButton = new GameButtons(GetTextureAsset("PlayButton.png"), true, new Vector2(20f * scale, Gdx.graphics.getHeight() - 80f * scale));
        playButton.setSize(60f * scale, 60f * scale);
        playButton.setColor(Color.GREEN);

        leftButton = new GameButtons(GetTextureAsset("LeftRightButton.png"), true, new Vector2(20f * scale, 20f * scale));
        leftButton.setSize(150f * scale, 150f * scale);
        leftButton.setFlip(true, false);
//        leftButton.setColor(Color.LIGHT_GRAY);

        rightButton = new GameButtons(GetTextureAsset("LeftRightButton.png"), true, new Vector2(Gdx.graphics.getWidth() - 170f * scale, 20f * scale));
        rightButton.setSize(150f * scale, 150f * scale);
//        rightButton.setColor(Color.LIGHT_GRAY);

        leftJumpButton = new GameButtons(GetTextureAsset("JumpButton.png"), true, new Vector2(leftButton.getX() + leftButton.getWidth() + 20f * scale, leftButton.getY()));
        leftJumpButton.setSize(150f * scale, 150f * scale);

        rightJumpButton = new GameButtons(GetTextureAsset("JumpButton.png"), true, new Vector2(rightButton.getX() - rightButton.getWidth() - 20f * scale, rightButton.getY()));
        rightJumpButton.setSize(150f * scale, 150f * scale);

        this.allotedBlocks = allotedBlocks;
        platformBlocks = new ArrayList<PlatformBlock>(allotedBlocks);
        for (int i = 0; i < allotedBlocks; i++) {
            platformBlocks.add(new PlatformBlock(world, gridBoxes.get(0), camera, GetTextureAsset("SinglePlatform.png"), scale));
            platformBlocks.get(i).getPlatformBody().setTransform(-1000, 0, 0);
            platformBlocks.get(i).getPlatformButton().setPosition(-1000, 0);
        }




        hero = new Hero(GetTextureAsset("Hero.png"), gridBoxes.get(0), world, camera, scale, gridSize);
        showGrid = true;

        goal = new Goal(GetTextureAsset("Goal.png"), gridBoxes.get(gridBoxes.size() - 1), world, camera, scale, Color.BLACK);


//        wind = Gdx.audio.newMusic(Gdx.files.local("Windy Desert.mp3"));
//        birds = Gdx.audio.newMusic(Gdx.files.local("Bird Ambience.mp3"));

        boxClickSound = assetManager.get("Click.mp3");
        jumpSound = assetManager.get("Jump.mp3");
        goalReachedSound = assetManager.get("OOT_Secret.wav");


        goalCollision = false;
        previousGoalCollision = false;

        originalZoom = camera.zoom;
//        camera.zoom = .25f;

        previousLevelBegin = false;

        goalTimer = 0;
    }



    boolean platformClicked;
    boolean canJump;
    Array<Contact> contactList;

    public void Update(OrthographicCamera camera, boolean levelBegin) {
//        if(levelBegin)
//        {
//            camera.zoom -= .0005f;
//
//            if(camera.zoom <= originalZoom)
//            {
//                camera.zoom = originalZoom;
//                levelBegin = false;
//            }
////            camera.update();
////            return;
//        }
//        if(levelBegin)
//        {
//            showGrid = false;
//            hero.setSize(hero.originalWidth * originalZoom / camera.zoom, hero.originalHeight * originalZoom / camera.zoom);
//            clouds.setSize(originalCloudWidth * originalZoom / camera.zoom, originalCloudHeight * originalZoom / camera.zoom);
//            platformSprite.setSize(originalPlatformWidth * originalZoom / camera.zoom, originalPlatformHeight * originalZoom / camera.zoom);
////            platformSprite.setPosition(ground.getPosition().x, ground.getPosition().y);
//            platformSprite.setPosition((ground.getPosition().x - camera.position.x + Gdx.graphics.getWidth() * camera.zoom / 2f) / camera.zoom - platformSprite.getWidth() / 2f,
//                    (ground.getPosition().y - camera.position.y + Gdx.graphics.getHeight() * camera.zoom / 2f) / camera.zoom);
//        }
//        else if(previousLevelBegin && !levelBegin){
//            showGrid = true;
//        }
        previousLevelBegin = levelBegin;
        currentlyPressed = Gdx.input.isTouched();
        contactList = world.getContactList();
        playButton.Update();
        platformSprite.setPosition((ground.getPosition().x - camera.position.x + Gdx.graphics.getWidth() * camera.zoom / 2f) / camera.zoom - platformSprite.getWidth() / 2f,
                (ground.getPosition().y - camera.position.y + Gdx.graphics.getHeight() * camera.zoom / 2f) / camera.zoom);
//        leftButton.Update();
//        rightButton.Update();
//        leftJumpButton.Update();
//        rightJumpButton.Update();
//        clouds.setX(clouds.getX() - 2);
//        if(clouds.getX() < - clouds.getWidth())
//        {
//            clouds.setX(clouds.getWidth());
//        }



        if (playButton.Tapped()) {
            showGrid = !showGrid;
            if (showGrid) {
                hero.setPosition(hero.getStartingPositionStatic().x, hero.getStartingPositionStatic().y);
                hero.setRotation(0);
                hero.getHeroBody().setTransform(hero.getStartingPositionWorld().x, hero.getStartingPositionWorld().y, 0);
                hero.getHeroBody().setLinearVelocity(0, 0);
                hero.getHeroBody().setAngularVelocity(0);
            }
        }

        canJump = false;
        //boolean
        for (int i = 0; i < world.getContactList().size; i++) {
            if (contactList.get(i).getFixtureA().getBody() == hero.getHeroBody() || contactList.get(i).getFixtureB().getBody() == hero.getHeroBody()) {
                if (contactList.get(i).getFixtureA().getBody() == ground || contactList.get(i).getFixtureB().getBody() == ground) {
                    canJump = true;
                    break;
                }
                for (int j = 0; j < platformBlocks.size(); j++) {
                    if (contactList.get(i).getFixtureA().getBody() == platformBlocks.get(j).getPlatformBody() || contactList.get(i).getFixtureB().getBody() == platformBlocks.get(j).getPlatformBody()) {
                        if (hero.getBottomBoundingBox().overlaps(platformBlocks.get(j).getTopBoundingBox())) {
                            canJump = true;
                            currentPlatformBlock = platformBlocks.get(j);
                            currentPlatformBlock.getPlatformBody().getFixtureList().get(0).setFriction(.6f);
                            break;
                        } else if(j == platformBlocks.size() - 1 && currentPlatformBlock != null) {
                            currentPlatformBlock.getPlatformBody().getFixtureList().get(0).setFriction(0);
                            currentPlatformBlock = null;
                        }
                    }
                }
//                if (canJump) {
//                    break;
//                }

//                if(currentPlatformBlock != null && contactList.get(i).getFixtureA().getBody() != currentPlatformBlock.platformBody &&
//                        contactList.get(i).getFixtureB().getBody() != currentPlatformBlock.platformBody){
//                    currentPlatformBlock.platformBody.getFixtureList().get(0).setFriction(0);
//                    currentPlatformBlock = null;
//                }
            }
        }

        if(contactList.size == 0 && currentPlatformBlock != null){
            currentPlatformBlock.getPlatformBody().getFixtureList().get(0).setFriction(0);
            currentPlatformBlock = null;
        }

        hero.Update(camera, canJump, leftButton.Pressed(), rightButton.Pressed(), (leftJumpButton.Tapped() || rightJumpButton.Tapped()), jumpSound);
        goal.Update(camera);
        goalCollision = hero.getBoundingRectangle().overlaps(goal.getBoundingRectangle());
        if(goalCollision)
        {
            goal.setColor(Color.SALMON);
            if(!previousGoalCollision)
            {
                goalReachedSound.play(.75f);
            }
            goalTimer += Gdx.graphics.getDeltaTime();
        }
        else
        {
            goal.revertColor();
            goalTimer = 0;
        }
        if (showGrid) {
            platformClicked = false;
            for (PlatformBlock platformBlock : platformBlocks) {
                platformBlock.Update();
                if (platformBlock.Clicked()) {
                    platformClicked = true;
                    boxClickSound.play();
                }
            }

            if (!platformClicked) {
                int index = 0;
                for (Rectangle gridBox : gridBoxes) {
                    if (GestureManager.IsCurrentlyBeingTouched(gridBox) && !currentlyPressed && previousPressed) {
                        textLocation = new Vector2(gridBox.x, gridBox.y);
                        rowCol.y = (gridBox.x - viewportDifference / 2f) / (viewport.x / (float) gridSize) + .1f;
                        rowCol.x = gridBox.y / (viewport.x / gridSize);
                        selectedIndex = index;
                        for (PlatformBlock platformBlock : platformBlocks) {
                            if (platformBlock.isDestroyed()) {
                                platformBlock.Transport(gridBox, camera);
                                boxClickSound.play();
                                break;
                            }
                        }
                    }
                    index++;
                }
            }
        }
        previousPressed = currentlyPressed;
        previousGoalCollision = goalCollision;
    }

    public void Draw(OrthographicCamera camera) {

    }

    protected void LoadAssets() {
        assetManager.load("BorderedBox.png", Texture.class);
        assetManager.load("PlayButton.png", Texture.class);
//        assetManager.load("CrateTexture.jpg", Texture.class);
        assetManager.load("Platform.png", Texture.class);
        assetManager.load("SinglePlatform.png", Texture.class);
        assetManager.load("Hero.png", Texture.class);
        assetManager.load("HeroTextured.png", Texture.class);
        assetManager.load("LeftRightButton.png", Texture.class);
        assetManager.load("JumpButton.png", Texture.class);
        assetManager.load("Goal.png", Texture.class);
        assetManager.load("Click.mp3", Sound.class);
        assetManager.load("Jump.mp3", Sound.class);
        assetManager.load("OOT_Secret.wav", Sound.class);
        assetManager.finishLoading();
    }

    public Texture GetTextureAsset(String fileName) {
        return assetManager.get(fileName, Texture.class);
    }
}
