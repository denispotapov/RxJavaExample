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
            .range(0, 3)
            .subscribeOn(Schedulers.io())
            .repeat(3)
            .observeOn(AndroidSchedulers.mainThread())

        observable.subscribe(object : Observer<Int> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Int) {
                Timber.d("onNext: $t")
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }

        })
    }
}