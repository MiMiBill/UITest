package com.example.uitest

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

class WorkerProcess(context: Context, workerParams:WorkerParameters) : Worker(context,workerParams) {

    companion object{
        var index = 0
    }

    override fun doWork(): Result {
        val inputstr = inputData.getString("data");
        index ++
        Log.d("WorkerProcess","WorkerProcess doing input data:${inputstr} index:${index}")
        if (index % 2 == 1){
            return Result.retry()
        }
        val data = Data.Builder().putString("data",inputstr + "来自doWork").build()
        return Result.success(data)
    }

}