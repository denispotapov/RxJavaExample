package com.example.rxjavaexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        val taskObservable: Observable<Task> = Observable
            .fromIterable(DataSource.createTasksList())
            .subscribeOn(Schedulers.io())
            .filter(object : Predicate<Task> {
                override fun test(t: Task): Boolean {
                    Timber.d("test: ${Thread.currentThread().name}")
                    try {
                        Thread.sleep(1000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    return t.isComplete
                }
            })
            .observeOn(AndroidSchedulers.mainThread())

        taskObservable.subscribe(object : Observer<Task> {
            override fun onSubscribe(d: Disposable) {
                Timber.d("onSubscribe: called")
            }

            override fun onNext(task: Task) {
                Timber.d("onNext: called ${Thread.currentThread().name}")
                Timber.d("onNext: ${task.description}")
            }

            override fun onError(e: Throwable) {
                Timber.d("onError: $e")
            }

            override fun onComplete() {
                Timber.d("onComplete: called")
            }
        })
    }
}