package com.example.rxjavaexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        //val task = Task("Walk the dog", false, 3)

        val observable  = Observable
            .range(0, 9)
            .subscribeOn(Schedulers.io())
            .map(object : Function<Int, Task> {
                override fun apply(t: Int): Task {
                    Timber.d("apply: ${Thread.currentThread().name}")
                    return Task("this is a task with priority: $t", false, t)
                }
            })
            .takeWhile(object : Predicate<Task> {
                override fun test(t: Task): Boolean {
                    return t.priority < 9
                }
            })
            .observeOn(AndroidSchedulers.mainThread())

        observable.subscribe(object : Observer<Task> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Task) {
                Timber.d("onNext: ${t.priority}")
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }

        })
    }
}