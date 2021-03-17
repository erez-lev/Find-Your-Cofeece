package com.cofeece.findyourcofeece

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_timer.*
import java.text.SimpleDateFormat
import java.util.*


private const val TAG = "TimerFragment"

/**
 * A simple [Fragment] subclass.
 */
class TimerFragment : Fragment() {
    /** Attributes: */
    private var isFirstClick: Boolean = false
    private var startTime: Long = 0
    private var timeInMilliseconds: Long = 0
    private var customHandler: Handler = Handler()

    /** Activity Methods: */
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView called")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated called")
        super.onViewCreated(view, savedInstanceState)
        timerButton.setOnLongClickListener {
            Log.d(TAG, "onViewCreated: timerButton clicked long.")
            isFirstClick = true
            start(timeShowETV)
            pressButtonHintTV.text = "Started!"
            pressButtonHintTV.setTextColor(Color.YELLOW)
            true
        }

        timerButton.setOnClickListener {
            if (!isFirstClick) {
                Log.d(TAG, "onViewCreated: timerButton clicked.")
                Snackbar.make(it, "To start the timer, please press long.", Snackbar.LENGTH_LONG).
                setAnchorView(it).show()
            } else {
                Log.d(TAG, "onViewCreated: timerButton clicked after long clicked.")
                stop(timeShowETV)
                pressButtonHintTV.text = "Your time was ${timeShowETV.text}"
                pressButtonHintTV.setTextColor(Color.WHITE)
                timeShowETV.setText(R.string.start_time_timer)
                // Show time and a receipt.
            }
        }
    }

    fun getDateFromMillis(d: Long): String? {
        val df = SimpleDateFormat("HH:mm:ss")
        df.setTimeZone(TimeZone.getTimeZone("GMT"))
        return df.format(d)
    }

    fun start(v: View?) {
        Log.d(TAG, "start: called.")
        startTime = SystemClock.uptimeMillis()
        customHandler.postDelayed(updateTimerThread, 0)
    }

    fun stop(v: View?) {
        Log.d(TAG, "stop: called.")
        customHandler.removeCallbacks(updateTimerThread)
    }

    private val updateTimerThread: Runnable = object : Runnable {
        override fun run() {
            Log.d(TAG, "updateTimerThread: run called.")
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime
            timeShowETV.setText(getDateFromMillis(timeInMilliseconds))
            Log.d(TAG, "updateTimerThread: time of timer is ${timeShowETV.text}.")
            customHandler.postDelayed(this, 1000)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        @JvmStatic
        fun newInstance() =
            TimerFragment().apply {
                return TimerFragment()
            }
    }
}