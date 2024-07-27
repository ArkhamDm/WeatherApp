package dev.arkhamd.wheatherapp.ui.map.customView

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class TypewriterTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {
    private var mText: CharSequence? = null
    private var mTextSize: Float = 14f
    private var mIndex = 0
    private var mDelay: Long = 100 // Задержка перед отображением следующей буквы

    private val mHandler = Handler()

    private val characterAdder = object : Runnable {
        override fun run() {
            text = mText?.subSequence(0, mIndex++)
            if (mIndex <= (mText?.length ?: 0)) {
                mHandler.postDelayed(this, mDelay)
            }
        }
    }

    init {
        isElegantTextHeight = true
        mText = text
        animateText(mText)
    }

    private fun animateText(text: CharSequence?) {
        mText = text
        mIndex = 0
        text?.let {
            mHandler.removeCallbacks(characterAdder)
            mHandler.postDelayed(characterAdder, mDelay)
        }
    }

    fun setCharacterDelay(millis: Long) {
        mDelay = millis
    }

    fun setTextSizeInSp(size: Float) {
        mTextSize = size
        textSize = size
    }

    fun getTextSizeInSp(): Float {
        return mTextSize
    }
}