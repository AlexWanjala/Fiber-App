package com.zuku.smartbill.zukufiber.ui

import PackageItems
import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RemoteViews
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.youtube.player.YouTubeStandalonePlayer
import com.zuku.smartbill.zukufiber.R
import com.zuku.smartbill.zukufiber.data.services.*
import com.zuku.smartbill.zukufiber.ui.adapter.PackageAdapter
import com.zuku.smartbill.zukufiber.ui.adapter.PackagesAdapter
import com.zuku.smartbill.zukufiber.ui.adapter.PaymentMethodsAdapter
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login2.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_transactions.*
import kotlinx.android.synthetic.main.bottom_sheet_plans.*
import kotlinx.android.synthetic.main.dialog_layout.*
import kotlinx.android.synthetic.main.notification.*
import kotlinx.android.synthetic.main.radio_group.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener{

    private var accounts: MutableList<String> = ArrayList()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var accNo: String = ""
    private var subdb: String =""
    lateinit var adapter : PackageAdapter

    // declaring variables
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"




    private fun notification(){

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // RemoteViews are used to use the content of
        // some different layout apart from the current activity layout
        val contentView = RemoteViews(packageName, R.layout.notification)

        // checking if android version is greater than oreo(API 26) or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, channelId)
                .setContent(contentView)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                .setContentIntent(pendingIntent)
        } else {

            builder = Notification.Builder(this)
                .setContent(contentView)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                .setContentIntent(pendingIntent)
        }
        notificationManager.notify(1234, builder.build())
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    bottomSheet.animate()
                        .translationY(0f)
                }
                if (newState == BottomSheetBehavior.STATE_HALF_EXPANDED) {
                    bottomSheet.animate()
                        .setDuration(200)
                        .translationY(20f)
                }
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheet.animate()
                        .setDuration(200)
                        .translationY(20f)
                }
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheet.animate()
                        .setDuration(200)
                        .translationY(-20f)
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        tv_change_plan.setOnClickListener {
            startActivity(Intent(this@MainActivity, PackagesActivity::class.java))
        }

        tv_pay.setOnClickListener {

            getpaymentmethods()

            if (android.os.Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
                startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")),1)
                Toast.makeText(this,"Please Allow",Toast.LENGTH_LONG).show()
                return@setOnClickListener;
            }else{
                toggleBottomSheet()
            }

        }
        tv_pay.setOnLongClickListener(object: View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                notification()
                return true
            }

        })
        image_statement.setOnClickListener {
            startActivity(Intent(this, Transactions::class.java)
            .putExtra("subdb",subdb)
            .putExtra("subid",accNo)
        ) }

        tv_top_up.setOnClickListener {

            if (android.os.Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
                startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")),1)
                Toast.makeText(this,"Please Allow",Toast.LENGTH_LONG).show()
                return@setOnClickListener;
            }else{
                startActivity(Intent(this, PaymentsActivity::class.java))
            }


        }
        chat.setOnClickListener {
            save(this,"address","")
            startActivity(Intent(this, ShiftRequest::class.java))
        }
        profile.setOnClickListener {
           // Toast.makeText(this,"coming soon",Toast.LENGTH_LONG).show()
            startActivity(Intent(this, Profile::class.java))
        }
        if(isOnline(this)){
            getSubscriber()
        }else{
            Toast.makeText(this,"You are offline",Toast.LENGTH_LONG).show()
        }



        tvName.setOnClickListener { logout() }

    }


    private fun adverts(){
        val imageList = ArrayList<SlideModel>() // Create image list

// imageList.add(SlideModel("String Url" or R.drawable)
// imageList.add(SlideModel("String Url" or R.drawable, "title") You can add title


         if(getValue(this,"subdb").toString().contains("Sat")){
             imageList.add(SlideModel("https://victormatara.com/wp-content/uploads/2018/06/Zuku-Satellite-TV-Packages-2018-1024x452.jpg", "Satellite"))
             imageList.add(SlideModel("https://www.jitimu.com/wp-jtech/wp-content/uploads/2022/12/Full-Zuku-Satellite-Kit.jpg", "Satellite"))
             imageList.add(SlideModel("https://www.jitimu.com/wp-jtech/wp-content/uploads/2020/10/Zuku-satellite-TV-Decoder-packages-and-channels.jpg", "Satellite"))
         }else{
             imageList.add(SlideModel("https://www.dignited.com/wp-content/uploads/2018/10/zuku-fiber.jpg", "Efficient"))
             imageList.add(SlideModel("https://kisiifinest.co.ke/wp-content/uploads/2021/06/List-of-best-Zuku-internet-packages-2021-Kenya-prices-fiber-WiFi-satellite-TV-rates.jpg", "Fast reliable"))
             imageList.add(SlideModel("https://arkafrica.com/wp-content/uploads/2021/03/ark-zuku-tv-ident.jpg", "Creating Africa’s first triple play brand."))
             imageList.add(SlideModel("https://pbs.twimg.com/media/DBix2lvXYAI-u0K.jpg", "Zuku Reward"))
         }


        val imageSlider = findViewById<ImageSlider>(R.id.image_slider)
        imageSlider.setImageList(imageList)

        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                var intent: Intent? = null
                if (this@MainActivity as Activity? != null) {
                    intent = YouTubeStandalonePlayer.createVideoIntent(
                        this@MainActivity as Activity?,
                        "AIzaSyC_L1ygZnDwoAcd8C23aL6BkqcNCifVMX4",
                        "ANMmYoLVx_M",
                        0,
                        false,
                        true
                    )
                }
                startActivity(intent)
            }
        })
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        try {
          //  startActivityForResult(intent, PERMISSION_REQUEST_CODE)
        } catch (e: Exception) {
         /*   showDialog(
                getString(R.string.permission_error_title),
                getString(R.string.permission_error_text)
            )*/
        }

        notification()
    }

     fun initRecyclerView(packageItems: List<PackageItems>){
        //Packages
        val adapter = PackagesAdapter(this,packageItems)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
    }
/*
    private fun initRecyclerViewRadio(arrayList: List<Packages>){
        //Packages
        runOnUiThread {
            adapter = PackageAdapter(this,arrayList)
            recycler_view_radio.adapter = adapter
            recycler_view_radio.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        }

    }*/

     fun stkPayments(desc: String){

        startActivity(Intent(this, Amount::class.java)
            .putExtra("amountDue",tvAmountDue.text)
            .putExtra("accNo",accNo)
            .putExtra("speed",  tvSPeed.text)
            .putExtra("desc",  desc))

     }

    private fun requestOverlayPermission() {
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.SYSTEM_ALERT_WINDOW)

        if (permission != PackageManager.PERMISSION_GRANTED) {
           // Log.i(TAG, "Permission to record denied")
        }
    }



    private fun getpaymentmethods (){
        Log.e("#######","######")
       lifecycleScope.launch(Dispatchers.IO){
           val result = api.getpaymentmethods("getpaymentmethods", getValue(this@MainActivity,"subdb").toString())
           if(result.success){
               Log.e("#######",result.toString())
               runOnUiThread {
                   recycler_view2.adapter = null
                   recycler_view2?.adapter?.notifyDataSetChanged()

                   val adapter = PaymentMethodsAdapter(this@MainActivity,result.data.paymethods)
                   recycler_view2.adapter = adapter
                   recycler_view2.layoutManager = LinearLayoutManager(this@MainActivity)
               }

           }else{
               runOnUiThread { Toast.makeText(this@MainActivity,result.message,Toast.LENGTH_LONG).show()}
           }

       }

    }

    override fun onResume() {
        requestOverlayPermission()
        currentTheme()
        super.onResume()
    }

    private fun currentTheme(){
        when (application.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                runOnUiThread {//Night Mode

                }
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                //Light Mode
                layoutMain.background = resources.getDrawable(R.drawable.bg_long)
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                // Toast.makeText(this,"NOT DEFINED",Toast.LENGTH_LONG).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
         getPackageInfo( p0?.getItemAtPosition(p2).toString());
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
       // Toast.makeText(this, p0?.getItemAtPosition(p2).toString(),Toast.LENGTH_LONG).show()

    }
    override fun onNothingSelected(p0: AdapterView<*>?) {
      //  Toast.makeText(this, p0?.getItemAtPosition(1).toString(),Toast.LENGTH_LONG).show()

    }
    private fun toggleBottomSheet(){
        runOnUiThread {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSubscriber(){

        lifecycleScope.launch(Dispatchers.IO) {

            val response = api.getSubscriber("getSubscriber", getValue(this@MainActivity,"phoneNumber").toString())
            runOnUiThread {
                if(response.success){
                    Const.ConstHolder.INSTANCE.setJson4Kotlin_Base(response)

                    for (item in response.data.subDetailsResponse){
                        getPackageInfo(response.data.subDetailsResponse[0].packageinfo.subid.toString())
                        accounts.add(item.packageinfo.subid.toString())
                    }
                    runOnUiThread {
                        val aa = ArrayAdapter(this@MainActivity, R.layout.spinner_right_aligned, accounts)
                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        with(mySpinner)
                        {
                            adapter = aa
                            setSelection(0, false)
                            onItemSelectedListener = this@MainActivity
                            prompt = R.string.select_acc.toString()
                            gravity = Gravity.CENTER

                        }
                    }

                }else{

                }
            }
        }



    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPackageInfo(subid :String){
        accNo = subid
        try {
            var currentPackage = ""
            for (item in Const.ConstHolder.INSTANCE.getJson4Kotlin_Base()?.data!!.subDetailsResponse){
                if(subid==item.packageinfo.subid.toString()){
                    if(item.packageinfo.subdb.contains("ZukuSat")){
                        runOnUiThread {
                            layoutSpeed.visibility = View.GONE
                            tvPackageName.visibility = View.VISIBLE
                            tvPackageName.text = item.packageinfo.currentpack
                            tvDes.text = item.packageinfo.des
                            currentPackage = item.packageinfo.currentpack
                        }

                        if(item.packageinfo.currentpack == null || item.packageinfo.currentpack.isEmpty()|| item.packageinfo.currentpack==""){
                            runOnUiThread {
                                layoutSpeed.visibility = View.GONE
                                tvPackageName.visibility = View.VISIBLE
                                tvPackageName.text = "inactive"
                                tvDes.text = "Pay to activate"
                            }
                        }
                        getPackages(currentPackage,"",item.packageinfo.subdb)
                    }
                    else{
                        runOnUiThread {
                            layoutSpeed.visibility = View.VISIBLE
                            tvPackageName.visibility = View.GONE

                            tvPackageName.text = item.packageinfo.currentpack
                            tvDes.text = item.packageinfo.lastpack
                            currentPackage =""

                        }
                    }
                    subdb = item.subdetails.subdb
                    save(this,"subdb",subdb)
                    save(this,"subName",item.subdetails.subname)
                    save(this,"subapt",item.subdetails.subapt)
                    save(this,"subid",item.subdetails.subid.toString())
                    save(this,"cid", item.subcontacts.cid)
                    save(this,"cellcont", item.subcontacts.cellcont)
                    save(this,"emcont", item.subcontacts.emcont)
                    save(this,"taxpin", item.subcontacts.taxpin)

                    updatedate.text = item.packageinfo.billthru
                    tvName.text = item.subdetails.subname
                    tvBalance.text =  kotlin.math.abs(item.packageinfo.buckamt).toString()
                    tvAmountDue.text = item.packageinfo.dueamt.toString()
                    if(item.packageinfo.billthru == null){
                        tvDays.text = "Due";
                    }else{
                        tvDays.text = dateDiff(item.packageinfo.billthru)
                    }

                    tvUntil.text = "Until "+ item.packageinfo.billthru
                    save(this,"currentPackage",item.packageinfo.lastpack)
                    var speed = item.packageinfo.lastpack.filter { it.isDigit() }
                    if(speed.length > 3){
                        speed = speed.drop(1);
                    }
                    tvSPeed.text =  speed
                    getPackages(currentPackage,speed,item.packageinfo.subdb)


                   if(item.packageinfo.currentpack == null || item.packageinfo.currentpack.isEmpty()|| item.packageinfo.currentpack==""){

                       runOnUiThread {
                            layoutSpeed.visibility = View.GONE
                            tvPackageName.visibility = View.VISIBLE
                            tvPackageName.text = "inactive"
                            tvDes.text = "Pay to activate"
                        }
                    }


                }
            }
        }catch (ex: NullPointerException){

        }

        adverts()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateDiff(finalDate: String): String {
        try {
            if(finalDate==null){
                return "Due";
            }
            else{
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val currentDate = LocalDateTime.now().format(formatter);

                val date1: Date
                val date2: Date
                val dates = SimpleDateFormat("yyyy-MM-dd")
                date1 = dates.parse(currentDate)
                date2 = dates.parse(finalDate)
                val difference: Long = abs(date1.time - date2.time)
                val differenceDates = difference / (24 * 60 * 60 * 1000)
                val dayDifference = differenceDates.toString();
                if(differenceDates>30){
                    return "Due";
                }else{
                    return dayDifference;
                }

            }
        }catch (NullPointerException : NullPointerException){
            return "Due";
        }
    }

    private fun getPackages(currentPackage:String, mbps:String, subid :String){
        lifecycleScope.launch(Dispatchers.IO){
            val response =  api.getPackages("getPackages",currentPackage,mbps,subid)
            if(response.success){
                Const.ConstHolder.INSTANCE.setPackages(response.data.packages)
             //   initRecyclerViewRadio(response.data.packages)

            }else{
                Toast.makeText(this@MainActivity,response.message,Toast.LENGTH_LONG).show()
            }

        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    private fun logout(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Would you like to logout? or Just to exit?")


        builder.setPositiveButton("Cancel") { dialog, which ->
           dialog.cancel()
        }

        builder.setNegativeButton("Logout") { dialog, which ->
            startActivity(Intent(this,Login ::class.java))
            save(this,"login","false")
            finishAffinity()
        }

        builder.setNeutralButton("Exit") { dialog, which ->
           finishAffinity()
        }
        builder.show()
    }

}