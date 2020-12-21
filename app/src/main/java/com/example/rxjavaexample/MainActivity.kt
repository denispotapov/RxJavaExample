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

        val taskObservable  = Observable
            .fromIterable(DataSource.createTasksList())
            .subscribeOn(Schedulers.io())
            .buffer(2)
            .observeOn(AndroidSchedulers.mainThread())

        taskObservable.subscribe(object : Observer<List<Task>> {
            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: List<Task>) {
                Timber.d("onNext: bundle results: -------------------")
                for (task in t) {
                    Timber.d("onNext: ${task.description}")
                }
            }

            override fun onError(e: Throwable) {
            }

            override fun onComplete() {
            }
        })
    }
}