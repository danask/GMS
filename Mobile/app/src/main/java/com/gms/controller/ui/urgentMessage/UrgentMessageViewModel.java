package com.gms.controller.ui.urgentMessage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UrgentMessageViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UrgentMessageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}