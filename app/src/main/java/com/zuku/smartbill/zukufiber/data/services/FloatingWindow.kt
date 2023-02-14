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
import android.os.Handler
import android.os.IBinder
import android.view.*
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.marginStart


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

        val parameters = WindowManager.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        parameters.gravity = Gravity.TOP or Gravity.END
        parameters.x = 0
        parameters.y = 0

        val stop = Button(application)
        stop.setBackgroundColor(Color.TRANSPARENT)
        stop.text = "Close"
        stop.setTextColor(Color.parseColor("#183b32"))
        stop.setOnClickListener {
            wm!!.removeView(ll)
            stopSelf()
            //System.exit(0);
        }
        val textView0 = TextView(application)
        textView0.text = "Tap to copy paste"
        textView0.textSize = 11f
        textView0.setTextColor(Color.RED)

        val textView = TextView(application)
        textView.text = "Business No: "+ getValue(this,"paybill").toString()
        textView.textSize = 18f
        textView.setTextColor(Color.BLACK)
        textView.setOnClickListener {

            Toast.makeText(application,getValue(this,"paybill").toString(),Toast.LENGTH_LONG).show()

            val clipboard2 = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip2 =ClipData.newPlainText("PAYBILL: ", getValue(this,"paybill").toString())
            clipboard2.setPrimaryClip(clip2)

        }

        val textView2 = TextView(application)
        textView2.text = "AccNo: "+ getValue(this,"subid").toString();
        textView2.textSize = 18f
        textView2.setTextColor(Color.BLACK)
        textView2.setOnClickListener {

            Toast.makeText(application,getValue(this,"subid").toString(),Toast.LENGTH_LONG).show()

            val clipboard3 = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip3 =ClipData.newPlainText("SUBID", getValue(this,"subid").toString())
            clipboard3.setPrimaryClip(clip3)

        }


        val btnParameters = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val txtParameters = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        stop.layoutParams = btnParameters
        textView0.layoutParams = txtParameters
        textView.layoutParams = txtParameters
        textView2.layoutParams = txtParameters
        ll!!.addView(stop)
        ll!!.addView(textView0)
        ll!!.addView(textView)
        ll!!.addView(textView2)
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

    }
    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
    }
}