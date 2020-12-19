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

        val taskObservable: Observable<Task> = Observable
            .create(object : ObservableOnSubscribe<Task> {
                override fun subscribe(emitter: ObservableEmitter<Task>) {

                    for (task in DataSource.createTasksList())
                        if (!emitter.isDisposed) {
                            emitter.onNext(task)
                        }

                    if (!emitter.isDisposed) {
                        emitter.onComplete()
                    }
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        taskObservable.subscribe(object : Observer<Task> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Task) {
                Timber.d("onNext: ${t.description}")
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        })
    }
}