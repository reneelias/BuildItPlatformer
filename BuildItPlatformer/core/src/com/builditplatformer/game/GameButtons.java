package com.builditplatformer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by rene__000 on 8/24/2014.
 */
public class GameButtons extends Sprite {
    private boolean _tapped;
    public boolean Tapped()
    {
        return _tapped;
    }
    public void setTapped(boolean tapped){_tapped = tapped;}
    private boolean _isStatic;
    private boolean _touched;
    private int _currentFingerTouching;
    private int _previousFingerTouching;
    Gestures _currentTouchState;
    Gestures _previousTouchState;
    protected float _originalWidth;
    protected float _originalHeight;
    public void SetOriginalWidthHeight(float width, float height)
    {
        _originalWidth = width;
        _originalHeight = height;
    }
    private Vector2 _originalPosition;
    public Vector2 GetOriginalPosition()
    {
        return _originalPosition;
    }
    public void SetOriginalPosition(Vector2 originalPosition)
    {
        _originalPosition = originalPosition;
    }
    private boolean pressed;
    public boolean Pressed() {
        return pressed;
    }

    public GameButtons(Texture texture, boolean isStatic, Vector2 position)
    {
        super(texture);
        _currentTouchState = Gestures.None;
        _previousTouchState = _currentTouchState;
        _isStatic = isStatic;
        this.setPosition(position.x, position.y);
        if(_isStatic)
        {
            _originalWidth = this.getWidth();
            _originalHeight = this.getHeight();
            _originalPosition = new Vector2(position.x, position.y);
        }
        _touched = false;
    }
    public GameButtons(TextureRegion textureRegion, boolean isStatic, Vector2 position)
    {
        super(textureRegion);
        _currentTouchState = Gestures.None;
        _previousTouchState = _currentTouchState;
        _isStatic = isStatic;
        this.setPosition(position.x, position.y);
        if(_isStatic)
        {
            _originalWidth = this.getWidth();
            _originalHeight = this.getHeight();
            _originalPosition = new Vector2(position.x, position.y);
        }
        _touched = false;
    }
    public void Update()
    {
        _currentTouchState = GestureManager.GetCurrentTouchState();
        FingerCurrentlyTouching();

        _tapped = CheckTap();
        if(GestureManager.IsCurrentlyBeingTouched(getBoundingRectangle()) && Gdx.input.isTouched()) {
            pressed = true;
        } else {
            pressed = false;
        }

        _touched = Gdx.input.isTouched(_currentFingerTouching);
        _previousTouchState = _currentTouchState;
        _previousFingerTouching = _currentFingerTouching;
    }

    public void Update(float cameraZoom)
    {
        _currentTouchState = GestureManager.GetCurrentTouchState();
        FingerCurrentlyTouching(cameraZoom);
        if(_isStatic)
        {
            KeepScale(cameraZoom);
        }

        _tapped = CheckTap(cameraZoom);

        _touched = Gdx.input.isTouched(_currentFingerTouching);
        _previousTouchState = _currentTouchState;
        _previousFingerTouching = _currentFingerTouching;
    }

    private boolean CheckTap()
    {
        if(GestureManager.IsCurrentlyBeingTouchedIndex(this.getBoundingRectangle(), 1, _currentFingerTouching) && !Gdx.input.isTouched(_currentFingerTouching) && _touched) {

            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean CheckTap(float cameraZoom)
    {
        if(GestureManager.IsCurrentlyBeingTouchedIndex(this.getBoundingRectangle(), cameraZoom, _currentFingerTouching) && !Gdx.input.isTouched(_currentFingerTouching) && _touched) {

            return true;
        }
        else
        {
            return false;
        }
    }

    private void KeepScale(float cameraZoom)
    {
        this.setSize(_originalWidth * cameraZoom, _originalHeight * cameraZoom);


    }

    public void KeepOriginalPosition(float cameraZoom)
    {
        this.setPosition(_originalPosition.x * cameraZoom + (Gdx.graphics.getWidth() - Gdx.graphics.getWidth() * cameraZoom) / 2, _originalPosition.y * cameraZoom + (Gdx.graphics.getHeight() - Gdx.graphics.getHeight() * cameraZoom) / 2);
    }

    public void FingerCurrentlyTouching() {
        int[] currentFingersDown = GestureManager.CurrentFingersDown();
        if(currentFingersDown.length > 0) {
            for (int i = 0; i < currentFingersDown.length; i++) {
                if (GestureManager.IsCurrentlyBeingTouchedIndex(this.getBoundingRectangle(), 1, currentFingersDown[i])) {
                    _currentFingerTouching = currentFingersDown[i];
                    break;
                }
            }
        }
    }

    public void FingerCurrentlyTouching(float cameraZoom) {
        int[] currentFingersDown = GestureManager.CurrentFingersDown();
        if(currentFingersDown.length > 0) {
            for (int i = 0; i < currentFingersDown.length; i++) {
                if (GestureManager.IsCurrentlyBeingTouchedIndex(this.getBoundingRectangle(), cameraZoom, currentFingersDown[i])) {
                    _currentFingerTouching = currentFingersDown[i];
                    break;
                }
            }
        }
    }
}
