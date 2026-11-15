package com.example.threadhandlerwizard

import android.os.Handler
import android.os.HandlerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WorkViewModel : ViewModel() {

    companion object {
        const val IDLE = "按 Start 開始"
        const val PREPARING = "準備中"
        const val WORKING = "工作中"
        const val DONE = "背景工作結束"
        const val CANCELED = "背景工作中斷"
    }

    // 背景執行緒 + 綁在它 Looper 上的 Handler
    private val handlerThread = HandlerThread("VM-Work").apply { start() }
    private val worker = Handler(handlerThread.looper)

    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> = _progress

    private val _status = MutableLiveData(IDLE)
    val status: LiveData<String> = _status

    @Volatile private var running = false

    fun start() {
        if (running) return
        running = true
        _status.value = PREPARING
        _progress.value = 0
        worker.post {
            Thread.interrupted() // 清掉可能殘留的中斷旗標
            try {
                Thread.sleep(3000) // 準備工作
                _status.postValue(WORKING)
                for (i in 1..100) {
                    Thread.sleep(100) // 背景工作：100 次 × 0.1 秒 = 10 秒
                    _progress.postValue(i)
                }
                _status.postValue(DONE)
            } catch (_: InterruptedException) {
                _status.postValue(CANCELED)
            } finally {
                running = false
            }
        }
    }

    fun cancel() {
        // HandlerThread 本身就是 Thread，interrupt() 會讓 sleep 立刻丟出 InterruptedException
        if (running) handlerThread.interrupt()
    }

    override fun onCleared() {
        cancel()
        handlerThread.quitSafely()
        super.onCleared()
    }
}
