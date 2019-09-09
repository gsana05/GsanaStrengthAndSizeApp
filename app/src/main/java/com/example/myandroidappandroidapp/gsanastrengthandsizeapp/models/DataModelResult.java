package com.example.myandroidappandroidapp.gsanastrengthandsizeapp.models;

public interface DataModelResult<T> {
    void onComplete(T data, Exception exception);
}
