package com.example.rxjavaexample

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.example.rxjavaexample.databinding.ActivityMainBinding
import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
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

        var timeSinceLastRequest = System.currentTimeMillis()

        val observableQueryText = Observable
            .create(object : ObservableOnSubscribe<String> {
                override fun subscribe(emitter: ObservableEmitter<String>) {

                    binding.searchView.setOnQueryTextListener(object :
                        SearchView.OnQueryTextListener {

                        override fun onQueryTextSubmit(p0: String?): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(p0: String?): Boolean {
                            if (!emitter.isDisposed) {
                                if (p0 != null && p0 != "") {
                                    emitter.onNext(p0)
                                }
                            }
                            return false
                        }
                    })
                }
            })
            .debounce(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())

        observableQueryText.subscribe(object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
                disposables.add(d)
            }

            override fun onNext(t: String) {
                Timber.d("onNext: time last since request ${System.currentTimeMillis() - timeSinceLastRequest}")
                Timber.d("onNext: search query $t")
                timeSinceLastRequest = System.currentTimeMillis()
                sendRequestToServer(t)
            }

            override fun onError(e: Throwable) {
            }

            override fun onComplete() {
            }
        })
    }


    private fun sendRequestToServer(query: String){
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}