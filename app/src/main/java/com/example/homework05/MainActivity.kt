package com.example.homework05

import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Thread.sleep

data class ImgItem(val id: Int, val desc: String)

class MainActivity : AppCompatActivity() {
    val tag: String = "mainactivity"
    private lateinit var mainHandler: Handler;
    private lateinit var handlerThread: HandlerThread
    private lateinit var downloadHandler: Handler
    private lateinit var list: List<ImgItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainHandler = Handler(android.os.Looper.getMainLooper())
        initDownloadThread()
        initList()

        start_download.setOnClickListener {
            startDownload()
        }

        stop_download.setOnClickListener {
            if (handlerThread.isAlive) {
                handlerThread.quit()
                download_status.text = "Download stopped"
            }
        }

        restart_download.setOnClickListener {
                initDownloadThread()
                startDownload()
        }
    }

    private fun initList() {
        val item1 = ImgItem(1, "img-1")
        val item2 = ImgItem(2, "img-2")
        val item3 = ImgItem(3, "img-3")
        list = listOf(item1, item2, item3)
    }


    private fun startDownload() {
        list.forEach {
            val message = Message()
            message.what = it.id
            message.obj = it.desc
            downloadHandler.sendMessage(message)
        }
    }

    fun initDownloadThread() {
        handlerThread = HandlerThread("downloadImage")
        handlerThread.start()

        downloadHandler = object : Handler(handlerThread.looper) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                Log.d(tag, msg.what.toString())
                download_status.text = "Downloading ${msg.obj}"
                Thread.sleep(1000)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handlerThread.quit()
    }
}

