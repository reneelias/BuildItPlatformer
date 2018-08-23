package com.builditplatformer.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.OrthographicCamera;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by rene__000 on 8/18/2014.
 */
public class GestureManager{

    public GestureManager() {
    }

    //Returns the current number of fingers currently touching the screen in the form of a Gestures Enum

    public static Gestures GetCurrentTouchState()
    {
        if(GetNumberOfFingersDown() == 0) {
            return Gestures.None;
        }
        else if(GetNumberOfFingersDown() == 1) {
            return Gestures.OneFinger;
        }
        else if(GetNumberOfFingersDown() == 2) {
            return Gestures.TwoFingers;
        }
        else {
            return Gestures.ThreeFingers;
        }
    }

    //Returns the total number of fingers currently touching the screen in the form of an int

    public static int GetNumberOfFingersDown()
    {
        int fingersTouching = 0;
        for (int i = 0; i < 3; i++)
        {
            if(Gdx.input.isTouched(i))
                fingersTouching++;
        }
        return fingersTouching;
    }

    //Returns an array of ints corresponding to the indexes of the fingers currently touching the screen

    public static int[] CurrentFingersDown()
    {
        ArrayList<Integer> fingersDown = new ArrayList<Integer>();
        for(int i = 0; i < 3; i++)
        {
            if(Gdx.input.isTouched(i)) {
                fingersDown.add(i);
            }
        }
        int[] fingersDownArray = new int[fingersDown.size()];
        for(int i = 0; i < fingersDownArray.length; i++)
        {
            fingersDownArray[i] = fingersDown.get(i);
        }
        return fingersDownArray;
    }

    //Returns true if the specified rectangle is currently being touched, accounts for camera zoom

    public static boolean IsCurrentlyBeingTouched(Rectangle boundingArea, float cameraZoom)
    {
        if(boundingArea.contains(((Gdx.input.getX()) * cameraZoom  + (Gdx.graphics.getWidth() - Gdx.graphics.getWidth() * cameraZoom) / 2),
                ((-Gdx.input.getY()  + Gdx.graphics.getHeight()) * cameraZoom) + (Gdx.graphics.getHeight() - Gdx.graphics.getHeight() * cameraZoom) / 2)) {
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean IsCurrentlyBeingTouched(Rectangle boundingArea, float cameraZoom, Vector2 cameraPosition)
    {
        if(boundingArea.contains(new Vector2(Gdx.input.getX() * cameraZoom + cameraPosition.x - (Gdx.graphics.getWidth() * cameraZoom) / 2 ,
                (-Gdx.input.getY()  + Gdx.graphics.getHeight()) * cameraZoom + cameraPosition.y - (Gdx.graphics.getHeight() * cameraZoom) / 2))) {
            return true;
        }
        else {
            return false;
        }
    }

    //Returns true if the specified rectangle is currently being touched

    public static boolean IsCurrentlyBeingTouched(Rectangle boundingArea)
    {
        if(boundingArea.contains(Gdx.input.getX(), -Gdx.input.getY()  + Gdx.graphics.getHeight())) {
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean IsCurrentlyBeingTouchedIndex(Rectangle boundingArea, float cameraZoom, int fingerIndex)
    {
        if(boundingArea.contains(((Gdx.input.getX(fingerIndex)) * cameraZoom  + (Gdx.graphics.getWidth() - Gdx.graphics.getWidth() * cameraZoom) / 2),
                ((-Gdx.input.getY(fingerIndex)  + Gdx.graphics.getHeight())* cameraZoom) + (Gdx.graphics.getHeight() - Gdx.graphics.getHeight() * cameraZoom) / 2)) {
            return true;
        }
        else {
            return false;
        }
    }

    //Returns the location of the input
    public static Vector2 InputLocation() {
        return new Vector2(Gdx.input.getX(), -Gdx.input.getY() + Gdx.graphics.getHeight());
    }

    public static Vector2 InputLocation(int index) {
        return new Vector2(Gdx.input.getX(index), -Gdx.input.getY(index) + Gdx.graphics.getHeight());
    }
    public static Vector2 InputLocation(int index, float cameraZoom, Vector2 cameraPosition) {
        return new Vector2(Gdx.input.getX(index) * cameraZoom + cameraPosition.x - (Gdx.graphics.getWidth() * cameraZoom) / 2,
                (-Gdx.input.getY(index) + Gdx.graphics.getHeight()) * cameraZoom + cameraPosition.y - (Gdx.graphics.getHeight() * cameraZoom) / 2);
    }

    //Returns the new Vector2 location for an object according to its center origin

    public static Vector2 DragLocation(float width, float height, float cameraZoom)
    {
        Vector2 dragLocation = new Vector2(Gdx.input.getX() * cameraZoom + ((Gdx.graphics.getWidth() - Gdx.graphics.getWidth() * cameraZoom) / 2)  - width / 2,
                (-Gdx.input.getY()  + Gdx.graphics.getHeight()) * cameraZoom + ((Gdx.graphics.getHeight() - Gdx.graphics.getHeight() * cameraZoom) / 2) - height / 2);

        return  dragLocation;
    }

    public static Vector2 DragLocation(float width, float height, float cameraZoom, Vector2 cameraPosition)
    {
        Vector2 dragLocation = new Vector2(Gdx.input.getX() * cameraZoom + cameraPosition.x - (Gdx.graphics.getWidth() * cameraZoom) / 2 - width / 2,
                (-Gdx.input.getY()  + Gdx.graphics.getHeight()) * cameraZoom + cameraPosition.y - (Gdx.graphics.getHeight() * cameraZoom) / 2 - height / 2);

        return  dragLocation;
    }

    public static Vector2 DragLocation(float width, float height, float cameraZoom, int fingerIndex)
    {
        Vector2 dragLocation = new Vector2(Gdx.input.getX(fingerIndex) * cameraZoom + ((Gdx.graphics.getWidth() - Gdx.graphics.getWidth() * cameraZoom) / 2)  - width / 2,
                (-Gdx.input.getY(fingerIndex)  + Gdx.graphics.getHeight()) * cameraZoom + ((Gdx.graphics.getHeight() - Gdx.graphics.getHeight() * cameraZoom) / 2) - height / 2);

        return  dragLocation;
    }

    //Returns the distance between two fingers in the form of a Vector2

    public static Vector2 DistanceBetweenTwoFingers(int fingerIndexOne, int fingerIndexTwo)
    {
        Vector2 distanceBetweenFingers = new Vector2(Gdx.input.getX(fingerIndexTwo) - Gdx.input.getX(fingerIndexOne), Gdx.input.getY(fingerIndexTwo) - Gdx.input.getY(fingerIndexOne));
        return distanceBetweenFingers;
    }

    /*Returns a new camera zoom value in the form of float,
    amount of zoom is calculated by taking the difference in the current distance between fingers and the previous one*/

    public static float PinchCameraZoom(Vector2 currentDistanceBetweenFingers, Vector2 previousDistanceBetweenFingers, float cameraZoom, float zoomSpeed)
    {
        if(currentDistanceBetweenFingers.len2() > previousDistanceBetweenFingers.len2())
        {
            cameraZoom -= zoomSpeed;
        }
        else if(currentDistanceBetweenFingers.len2() < previousDistanceBetweenFingers.len2())
        {
            cameraZoom += zoomSpeed;
        }
        return cameraZoom;
    }

    public static Vector2 CurrentPreviousFingerPositionDifference(Vector2 currentDistanceBetweenFingers, Vector2 previousDistanceBetweenFingers)
    {
        Vector2 fingerPositionDifference = new Vector2(currentDistanceBetweenFingers.x - previousDistanceBetweenFingers.x, currentDistanceBetweenFingers.y - previousDistanceBetweenFingers.y);
        return  fingerPositionDifference;
    }


}
