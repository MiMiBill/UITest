package com.example.uitest

import android.os.Parcel
import android.os.Parcelable
import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference

class MyWeakReference<T>(var ref: T,var queue: ReferenceQueue<T> ,var name: String) : WeakReference<T>(ref,queue) {

}