package com.zuku.smartbill.zukufiber.data.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi


class FloatingWindow : Service() {


    var wm: WindowManager? = null
    var ll: LinearLayout? = null

    override fun onBind(intent: Intent?): IBinder? {
        // TODO Auto-generated method stub
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate() {
        // TODO Auto-generated method stub
        super.onCreate()
        wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        ll = LinearLayout(application)
        ll!!.setBackgroundColor(Color.BLUE)
        val layoutParameteres = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 400
        )
        ll!!.setBackgroundColor(Color.WHITE)
        ll!!.layoutParams = layoutParameteres
        ll!!.orientation = LinearLayout.VERTICAL

        val LAYOUT_FLAG: Int
        LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val parameters = WindowManager.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        parameters.gravity = Gravity.TOP or Gravity.END
        parameters.x = 0
        parameters.y = 0
        val stop = Button(application)
        stop.setBackgroundColor(Color.TRANSPARENT)
        val textView = TextView(application)
        stop.text = "Done"
        stop.setTextColor(Color.parseColor("#183b32"))
        textView.text = "Business No: KUKUKU"
        textView.textSize = 18f
        textView.setTextColor(Color.BLACK)

        //Copying and pasting
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Business No: ", "88896")
        val clip2 = ClipData.newPlainText("Account No: ", "HAHAHAHA")

        // Set the clipboard's primary clip.
        clipboard.setPrimaryClip(clip2)
        val btnParameters = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        stop.layoutParams = btnParameters
        val txtParameters = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        textView.layoutParams = txtParameters
        ll!!.addView(stop)
        ll!!.addView(textView)
        wm!!.addView(ll, parameters)
        ll!!.setOnTouchListener(object : OnTouchListener {
            var updatedParameters = parameters
            var x = 0.0
            var y = 0.0
            var pressedX = 0.0
            var pressedY = 0.0
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        x = updatedParameters.x.toDouble()
                        y = updatedParameters.y.toDouble()
                        pressedX = event.rawX.toDouble()
                        pressedY = event.rawY.toDouble()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        updatedParameters.x = (x + (event.rawX - pressedX)).toInt()
                        updatedParameters.y = (y + (event.rawY - pressedY)).toInt()
                        wm!!.updateViewLayout(ll, updatedParameters)
                    }
                    else -> {}
                }
                return false
            }
        })
        stop.setOnClickListener {
            wm!!.removeView(ll)
            stopSelf()
            //System.exit(0);
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
    }
}