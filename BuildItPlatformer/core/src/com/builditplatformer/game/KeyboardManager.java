package com.builditplatformer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.ArrayList;

/**
 * Created by ReneElias on 4/2/2015.
 */
public class KeyboardManager {
    
    private ArrayList<Integer> keyIntValues;
    private ArrayList<Integer> numberKeyIntValues;
    
    public KeyboardManager() {
        keyIntValues = new ArrayList<Integer>();
        numberKeyIntValues = new ArrayList<Integer>();
        keyIntValues.add(Input.Keys.A);
        keyIntValues.add(Input.Keys.B);
        keyIntValues.add(Input.Keys.C);
        keyIntValues.add(Input.Keys.D);
        keyIntValues.add(Input.Keys.E);
        keyIntValues.add(Input.Keys.F);
        keyIntValues.add(Input.Keys.G);
        keyIntValues.add(Input.Keys.H);
        keyIntValues.add(Input.Keys.I);
        keyIntValues.add(Input.Keys.J);
        keyIntValues.add(Input.Keys.K);
        keyIntValues.add(Input.Keys.L);
        keyIntValues.add(Input.Keys.M);
        keyIntValues.add(Input.Keys.N);
        keyIntValues.add(Input.Keys.O);
        keyIntValues.add(Input.Keys.P);
        keyIntValues.add(Input.Keys.Q);
        keyIntValues.add(Input.Keys.R);
        keyIntValues.add(Input.Keys.S);
        keyIntValues.add(Input.Keys.T);
        keyIntValues.add(Input.Keys.U);
        keyIntValues.add(Input.Keys.V);
        keyIntValues.add(Input.Keys.W);
        keyIntValues.add(Input.Keys.X);
        keyIntValues.add(Input.Keys.Y);
        keyIntValues.add(Input.Keys.Z);
        keyIntValues.add(Input.Keys.NUM_0);
        keyIntValues.add(Input.Keys.NUM_1);
        keyIntValues.add(Input.Keys.NUM_2);
        keyIntValues.add(Input.Keys.NUM_3);
        keyIntValues.add(Input.Keys.NUM_4);
        keyIntValues.add(Input.Keys.NUM_5);
        keyIntValues.add(Input.Keys.NUM_6);
        keyIntValues.add(Input.Keys.NUM_7);
        keyIntValues.add(Input.Keys.NUM_8);
        keyIntValues.add(Input.Keys.NUM_9);

        numberKeyIntValues.add(Input.Keys.NUM_0);
        numberKeyIntValues.add(Input.Keys.NUM_1);
        numberKeyIntValues.add(Input.Keys.NUM_2);
        numberKeyIntValues.add(Input.Keys.NUM_3);
        numberKeyIntValues.add(Input.Keys.NUM_4);
        numberKeyIntValues.add(Input.Keys.NUM_5);
        numberKeyIntValues.add(Input.Keys.NUM_6);
        numberKeyIntValues.add(Input.Keys.NUM_7);
        numberKeyIntValues.add(Input.Keys.NUM_8);
        numberKeyIntValues.add(Input.Keys.NUM_9);

        keyIntValues.add(Input.Keys.NUMPAD_0);
        keyIntValues.add(Input.Keys.NUMPAD_1);
        keyIntValues.add(Input.Keys.NUMPAD_2);
        keyIntValues.add(Input.Keys.NUMPAD_3);
        keyIntValues.add(Input.Keys.NUMPAD_4);
        keyIntValues.add(Input.Keys.NUMPAD_5);
        keyIntValues.add(Input.Keys.NUMPAD_6);
        keyIntValues.add(Input.Keys.NUMPAD_7);
        keyIntValues.add(Input.Keys.NUMPAD_8);
        keyIntValues.add(Input.Keys.NUMPAD_9);

        numberKeyIntValues.add(Input.Keys.NUMPAD_0);
        numberKeyIntValues.add(Input.Keys.NUMPAD_1);
        numberKeyIntValues.add(Input.Keys.NUMPAD_2);
        numberKeyIntValues.add(Input.Keys.NUMPAD_3);
        numberKeyIntValues.add(Input.Keys.NUMPAD_4);
        numberKeyIntValues.add(Input.Keys.NUMPAD_5);
        numberKeyIntValues.add(Input.Keys.NUMPAD_6);
        numberKeyIntValues.add(Input.Keys.NUMPAD_7);
        numberKeyIntValues.add(Input.Keys.NUMPAD_8);
        numberKeyIntValues.add(Input.Keys.NUMPAD_9);
        //index 46
        keyIntValues.add(Input.Keys.MINUS);
        //index 47
        keyIntValues.add(Input.Keys.PLUS);
        //index 48
        keyIntValues.add(Input.Keys.PERIOD);

        numberKeyIntValues.add(Input.Keys.PERIOD);
    }

    public int[] GetKeyIntsArray() {
        int[] allowedKeysArray = new int[keyIntValues.size()];
        for(int i = 0; i < allowedKeysArray.length; i++){
            allowedKeysArray[i] = keyIntValues.get(i);
        }
        return allowedKeysArray;
    }

    public char GetTypedKey() {
        char tempChar = '\0';
        for (int i = 0; i < keyIntValues.size(); i++) {
            if (Gdx.input.isKeyJustPressed((keyIntValues.get(i)))) {
                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
                    tempChar = Input.Keys.toString(keyIntValues.get(i)).toCharArray()[0];
                } else {
                    tempChar = Input.Keys.toString(keyIntValues.get(i)).toLowerCase().toCharArray()[0];
                }
            }
        }
        return tempChar;
    }

    public char GetTypedNumberKey() {
        char tempChar = '\0';
        for (int i = 0; i < numberKeyIntValues.size(); i++) {
            if (Gdx.input.isKeyJustPressed((numberKeyIntValues.get(i)))) {
                    tempChar = Input.Keys.toString(numberKeyIntValues.get(i)).toCharArray()[0];
            }
        }
        return tempChar;
    }
}
