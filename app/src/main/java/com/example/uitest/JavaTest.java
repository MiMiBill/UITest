package com.example.uitest;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.internal.io.FileSystem;

public class JavaTest{

    private void test(){
        Cache cache = new Cache(new File(""),123,FileSystem.SYSTEM);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().cache(cache).build();


    }



}
