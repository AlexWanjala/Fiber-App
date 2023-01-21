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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zuku.smartbill.zukufiber.R;


public class FloatingWindow2 extends Service {

    ImageView copy_ImageView;
    WindowManager windowManager;
    WindowManager.LayoutParams layoutParams;

    @Override
    public IBinder onBind(Intent arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override

    public void onCreate()
    {
        windowManager=(WindowManager)getSystemService(WINDOW_SERVICE);

        copy_ImageView=new ImageView(getApplicationContext());
        copy_ImageView.setImageResource(R.drawable.arrow_down_black);
        copy_ImageView.setAlpha(245);
        copy_ImageView.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                showCustomPopupMenu();
            }
        });

        layoutParams=new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        layoutParams.gravity=Gravity.TOP|Gravity.CENTER;
        layoutParams.x=0;
        layoutParams.y=100;

        windowManager.addView(copy_ImageView, layoutParams);

    }

    private void showCustomPopupMenu()
    {
        WindowManager  windowManager2 = (WindowManager)getSystemService(WINDOW_SERVICE);
        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.radio_group, null);
            layoutParams= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        layoutParams.gravity=Gravity.CENTER|Gravity.CENTER;
        layoutParams.x=0;
        layoutParams.y=0;
        windowManager2.addView(view, layoutParams);
    }
}
