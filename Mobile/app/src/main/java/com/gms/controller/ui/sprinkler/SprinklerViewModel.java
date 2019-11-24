package com.gms.controller.ui.sprinkler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SprinklerViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SprinklerViewModel() {
        mText = new MutableLiveData<>();
//        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}