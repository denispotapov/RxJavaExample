package com.example.rxjavaexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rxjavaexample.databinding.ActivityMainBinding
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
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

        val taskObservable = Observable
            .fromIterable(DataSource.createTasksList())
            .map(object : Function<Task, String> {
                override fun apply(t: Task): String {
                    return t.description
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        taskObservable.subscribe(object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: String) {
                Timber.d("onNext: $t")
            }

            override fun onError(e: Throwable) {
            }

            override fun onComplete() {
            }
        })
    }
}