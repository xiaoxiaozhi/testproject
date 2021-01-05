package com.myapplication.activity


import android.content.res.AssetManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.myapplication.R
import java.io.IOException

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test2)

    }

    override fun onResume() {
        super.onResume()
        window.decorView.rootView.postDelayed({
            Thread(){
                playaudio()
            }.start()

        }, 2000)
    }

    //    fun test(vararg args:){
//        System.out.print()
//    }
    fun playaudio() {
        val assetManager: AssetManager
        var player: MediaPlayer? = null
        player = MediaPlayer()
        assetManager = resources.assets
        try {
            val fileDescriptor = assetManager.openFd("camera_click.ogg")
            player.setDataSource(fileDescriptor.fileDescriptor, fileDescriptor.startOffset, fileDescriptor.startOffset)
            player.prepare()
            player.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
