package com.gms.controller;

public interface OnEventListener<T> {

    public static final String SITE = "http://3.95.189.155:8000";
    public static final String ADMIN = "Admin";
    public void onSuccess(T result);
    public void onFailure(Exception e);
}
