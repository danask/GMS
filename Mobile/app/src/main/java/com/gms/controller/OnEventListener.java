package com.gms.controller;

public interface OnEventListener<T> {

    public static final String SITE = "http://18.207.205.239:8000";
    public static final String ADMIN = "Admin";
    public void onSuccess(T result);
    public void onFailure(Exception e);
}
