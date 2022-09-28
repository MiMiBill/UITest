package com.example.uitest;

import android.os.Build;
import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.internal.io.FileSystem;

public class JavaTest{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void test(){
        Cache cache = new Cache(new File(""),123,FileSystem.SYSTEM);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().cache(cache).build();


        ArrayList<JavaTest> arrayList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            List<JavaTest> javaTests = arrayList.stream().filter(e ->{
                return TextUtils.equals(e.getName(), "bill");
            }).distinct().collect(Collectors.toList());;

            
        }


    }



}
