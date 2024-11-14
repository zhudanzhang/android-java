package com.example.demo34;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {
    private MutableLiveData<String> text = new MutableLiveData<>();

    public LiveData<String> getText() {
        return text;
    }

    public void updateText(String newText) {
        text.postValue(newText);  // 使用 postValue 在后台线程更新数据
    }
}