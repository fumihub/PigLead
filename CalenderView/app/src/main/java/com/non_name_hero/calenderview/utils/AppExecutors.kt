package com.non_name_hero.calenderview.utils

import android.os.Handler
import android.os.Looper
import androidx.annotation.VisibleForTesting
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors
/**
 * コンストラクタ(オーバーロード)
 *
 * @param diskIO ローカルで使用するスレッドを管理するExecutor
 * @param networkIO ローカルで使用するスレッドを管理するExecutor
 * @param mainThread 主にUI処理を行うメインスレッドを管理するExecutor
 */ @VisibleForTesting internal constructor(private val diskIO: Executor, private val networkIO: Executor, private val mainThread: Executor) {
    constructor() : this(
            DiskIOThreadExecutor(),  //ローカル用のスレッドを管理するExecutorをインスタンス化
            Executors.newFixedThreadPool(THREAD_COUNT),  //ここでは3つのスレッドを作成して使い回す
            MainThreadExecutor() //UI用のスレッドを管理するExecutorをインスタンス化
    ) {
    }

    /**
     * 処理を実行するスレッドのExecutorを返却
     * 使用するときは 任意のExecutor().execute(new Runnable {run(){行いたい処理}})
     * @return Executor
     */
    fun diskIO(): Executor {
        return diskIO //diskIO.execute(new Runnable{....
    }

    fun networkIO(): Executor { //diskIO.execute(new Runnable{....
        return networkIO
    }

    fun mainThread(): Executor { //mainThread().execute(new Runnable{....
        return mainThread
    }

    /**
     * Executorsからスレッドを作成するのではなく、現在のメインスレッドを利用するため、Handlerを作成してlooperを登録する
     * 1.スレッドを作成する必要がないため
     * 2.他の全てのRunnableタスクと同期的に実行されるので管理しやすい
     * 3.メインハンドラでは
     * https://academy.realm.io/jp/posts/android-thread-looper-handler/
     */
    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            //処理をハンドラ(looper)に投稿する
            mainThreadHandler.post(command)
        }
    }

    companion object {
        private const val THREAD_COUNT = 3
    }
}