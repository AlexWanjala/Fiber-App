/*
package com.zuku.smartbill.zukufiber.data.services;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;



public class FloatingWindow extends Service {


    public  static WindowManager wm;
    public  static LinearLayout ll;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        ll = new LinearLayout(this);
        ll.setBackgroundColor(Color.BLUE);
        LinearLayout.LayoutParams layoutParameteres = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 400);
        ll.setBackgroundColor(Color.WHITE);
        ll.setLayoutParams(layoutParameteres);
        ll.setOrientation(LinearLayout.VERTICAL);

        final WindowManager.LayoutParams parameters = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_PHONE,

                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        parameters.gravity = Gravity.TOP | Gravity.END;
        parameters.x = 0;
        parameters.y = 0;

        Button stop = new Button(this);
        stop.setBackgroundColor(Color.TRANSPARENT);
        TextView textView = new TextView(this);

        stop.setText("Done");
        stop.setTextColor(Color.parseColor("#183b32"));
        textView.setText("Business No: KUKUKU");
        textView.setTextSize(18);
        textView.setTextColor(Color.BLACK);

        //Copying and pasting
        ClipboardManager clipboard = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clip = ClipData.newPlainText("Business No: ", "88896");
        ClipData clip2 = ClipData.newPlainText("Account No: ", "HAHAHAHA");

        // Set the clipboard's primary clip.
        clipboard.setPrimaryClip(clip2);


        ViewGroup.LayoutParams btnParameters = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        stop.setLayoutParams(btnParameters);

        ViewGroup.LayoutParams txtParameters = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(txtParameters);

        ll.addView(stop);
        ll.addView(textView);
        wm.addView(ll, parameters);




        ll.setOnTouchListener(new View.OnTouchListener() {
            WindowManager.LayoutParams updatedParameters = parameters;
            double x;
            double y;
            double pressedX;
            double pressedY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        x = updatedParameters.x;
                        y = updatedParameters.y;

                        pressedX = event.getRawX();
                        pressedY = event.getRawY();

                        break;

                    case MotionEvent.ACTION_MOVE:
                        updatedParameters.x = (int) (x + (event.getRawX() - pressedX));
                        updatedParameters.y = (int) (y + (event.getRawY() - pressedY));

                        wm.updateViewLayout(ll, updatedParameters);

                    default:
                        break;
                }

                return false;
            }
        });



        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wm.removeView(ll);
                stopSelf();
                //System.exit(0);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }


}*/
