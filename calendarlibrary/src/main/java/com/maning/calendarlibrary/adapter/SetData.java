package com.maning.calendarlibrary.adapter;

/**
 * Created by chenlipeng on 2018/5/9 0009.
 * describe :
 */

public class SetData {

    private String currentState = "1";
    private boolean isChooseStartData = false;

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public boolean isChooseStartData() {
        return isChooseStartData;
    }

    public void setChooseStartData(boolean chooseStartData) {
        isChooseStartData = chooseStartData;
    }
}
