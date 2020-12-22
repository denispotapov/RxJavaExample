package com.example.rxjavaexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rxjavaexample.databinding.ActivityMainBinding
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val disposables = CompositeDisposable()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        val intervalObservable = Observable
            .interval(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .takeWhile(object : Predicate<Long> {
                override fun test(t: Long): Boolean {
                    Timber.d("test: $t thread: ${Thread.currentThread().name}")
                    return t <= 5
                }
            })
            .observeOn(AndroidSchedulers.mainThread())

        intervalObservable.subscribe(object : Observer<Long> {
            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: Long) {
                Timber.d("onNext: $t")
            }

            override fun onError(e: Throwable) {
            }

            override fun onComplete() {
            }
        })
    }
}