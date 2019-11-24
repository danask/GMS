package com.gms.controller;

public interface OnEventListener<T> {

    public static final String SITE = "http://34.205.156.23:8000";
    public static final String ADMIN = "Admin";
    public void onSuccess(T result);
    public void onFailure(Exception e);
}
