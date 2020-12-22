package com.example.rxjavaexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rxjavaexample.databinding.ActivityMainBinding
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val disposables = CompositeDisposable()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        val task = arrayOf(
            Task("Take out the trash", true, 3),
            Task("Walk the dog", false, 2),
            Task("Make my bed", true, 1),
            Task("Unload the dishwasher", false, 0),
            Task("Make dinner", true, 5))

        val taskObservable = Observable
            .fromArray(task)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        taskObservable.subscribe(object : Observer<Array<Task>> {
            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: Array<Task>) {
                Timber.d("onNext: ${t[0].description}")
                Timber.d("onNext: ${t[1].description}")
                Timber.d("onNext: ${t[2].description}")
                Timber.d("onNext: ${t[3].description}")
                Timber.d("onNext: ${t[4].description}")
            }

            override fun onError(e: Throwable) {
            }

            override fun onComplete() {
            }
        })

    }

}