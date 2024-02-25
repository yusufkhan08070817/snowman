package com.example.snowtheam

import android.animation.Animator
import android.animation.AnimatorInflater
import android.graphics.Rect
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.TouchDelegate
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.snowtheam.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        window.decorView.apply {
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
         mediaPlayer = MediaPlayer.create(this, R.raw.music)

        // Set looping to true
        mediaPlayer.isLooping = true

        // Start playing the music
        mediaPlayer.start()
        // Adjust touch delegate for b.snowman to expand touch area
        b.snowman.post {
            val rect = Rect()
            b.snowman.getHitRect(rect)
            val expandTouchArea = 50 // Adjust as needed
            rect.top -= expandTouchArea
            rect.bottom += expandTouchArea
            rect.left -= expandTouchArea
            rect.right += expandTouchArea
            b.snowman.setTouchDelegate(TouchDelegate(rect, b.snowman))
        }

        // Load walking animation for b.snowman
        val walkingAnimator = AnimatorInflater.loadAnimator(this, R.animator.snowmanwalking)
        walkingAnimator.setTarget(b.snowman)
        walkingAnimator.start()

        // Set OnClickListener for b.snowman
        b.snowman.setOnClickListener {
            // Start animation for b.msg
            b.msg.startAnimation(AnimationUtils.loadAnimation(this, R.anim.msg))
            // Show a toast message
            Toast.makeText(this, "I will upload", Toast.LENGTH_SHORT).show()
        }

        // Load snow falling animator and apply it to each child view of b.snow
        val snowAnimatorResId = R.animator.snowfalling
        for (i in 0 until b.snow.childCount) {
            val child = b.snow.getChildAt(i)
            val snowAnimator = AnimatorInflater.loadAnimator(this, snowAnimatorResId).clone() as Animator
            val randomDelay = Random.nextLong(10000)
            snowAnimator.startDelay = randomDelay
            snowAnimator.setTarget(child)
            snowAnimator.start()
        }
    }
    override fun onDestroy() {
        super.onDestroy()

        // Release the MediaPlayer when the activity is destroyed
        mediaPlayer.release()
    }
}
