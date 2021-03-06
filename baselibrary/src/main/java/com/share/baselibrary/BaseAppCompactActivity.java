package com.share.baselibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Created by qlooit-9 on 20/12/17.
 */

public class BaseAppCompactActivity extends AppCompatActivity {

    public SharedPreferences preferencesVal;
    public SharedPreferences.Editor editor;
    private ProgressDialog mProgressDialog;
    private String FILE_NAME;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferencesVal = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

    }

    //For update string prefrence
    public void nbUpdatePref(String key, String val) {
        editor = preferencesVal.edit();
        editor.putString(key, val);
        editor.commit();
    }

    //For get string prefrence
    public String nbGetPref(String key, String defVal) {
        return preferencesVal.getString(key, defVal);
    }

    //For update int prefrence
    public void nbUpdatePrefInt(String key, int val) {
        editor = preferencesVal.edit();
        editor.putInt(key, val);
        editor.commit();
    }

    //For get int prefrence
    public Integer nbGetPrefInt(String key, int defVal) {
        return preferencesVal.getInt(key, defVal);
    }

    //For clear all prefrence
    public void nbClearPref() {
        editor.clear();
        editor.apply();
    }

    //For Toast your message
    public void nbToast(String val) {
        Toast customToast = new Toast(getBaseContext());
        customToast = Toast.makeText(getBaseContext(), val, Toast.LENGTH_SHORT);
        customToast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
        customToast.show();
    }

    //For Get the screen dimensions
    public static int[] nbGetScreenSize(Activity activity) {
        Point size = new Point();
        WindowManager w = activity.getWindowManager();

        w.getDefaultDisplay().getSize(size);
        return new int[]{size.x, size.y};
    }

    //For Alert your message
    public void nbAlertBox(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name))
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .show();
    }

    //For setActionbar title with back button
    public void nbSetUpActionBar(String Actiontitle) {
//        getSupportActionBar().setTitle((Html.fromHtml("<ba <font color='#ffffff'>" + Actiontitle + "</font>")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.actionbar_titleview, null);
        ((TextView) v.findViewById(R.id.title)).setText(Actiontitle);
        getSupportActionBar().setCustomView(v);

    }

    //For setActionbar title
    public void nbSetUpActionBarHome(String Actiontitle) {
        getSupportActionBar().setTitle((Html.fromHtml("<ba <font color='#ffffff'>" + Actiontitle + "</font>")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ;
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.actionbar_titleview, null);
        ((TextView) v.findViewById(R.id.title)).setText(Actiontitle);
        getSupportActionBar().setCustomView(v);
    }

    //For hide Actionbar
    public static void nbHideActionBar(Activity activity) {
        // Call before calling setContentView();
        if (activity != null) {
            activity.getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
            if (activity.getActionBar() != null) {
                activity.getActionBar().hide();
            }
        }
    }

    //For FullScreen
    public static void nbSetFullScreen(Activity activity) {
        // Call before calling setContentView();
        activity.getWindow()
                .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //For hide keyborad
    public void nbHideKeyboard() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //For check internet connection available or not
    public boolean nbIsNetworkAvailable(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //For check GPS Enabled or not
    public static boolean nbIsGpsEnabled(Context context) {
        LocationManager lm = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    //For check Wifi connected or not
    public static boolean nbIsWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    //For check 3G service running or not
    public static boolean nbIs3G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    //For check 4G service running or not
    public static boolean nbIs4G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.isConnectedOrConnecting()) {
            if (activeNetInfo.getType() == TelephonyManager.NETWORK_TYPE_LTE) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            nbHideKeyboard();
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    //For check valid Email
    public final static boolean nbIsValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    //For debug
    public void deBug(String string) {
        Log.e("DEBUG : ", string);
    }

    //For start progrss dialog
    public void nbStartDialog(Context applicationContext, String message, boolean isCancelable) {
        mProgressDialog = new ProgressDialog(applicationContext);
        mProgressDialog.setCancelable(isCancelable);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();

//        TextView dialog_title = (TextView) dialog.findViewById(R.id.text_dialog_title);
//        dialog_title.setText(mVal);
    }

    //For close dialog
    public void nbCloseDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    //For check hash map null or not
    public Map<String, String> nbCheckParams(Map<String, String> map) {
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
            if (pairs.getValue() == null) {
                map.put(pairs.getKey(), "");
            }
        }
        return map;
    }

    //For check edittext is empty
    public boolean nbIsValidString(EditText data) {
        if (data.getText().toString().trim().equals(""))
            return false;
        else
            return true;
    }

    //For load json from assest folder
    public String nbLoadJSONFromAsset(String file_name) {
        String json = null;
        try {
            InputStream is = getAssets().open(file_name);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    //For play sound
    //raw_data = R.raw.click_sound
    public void nbPlaySound(Context context, int raw_data) {

        MediaPlayer mp = MediaPlayer.create(context, raw_data);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.reset();
                mp.release();
                mp = null;
            }

        });
        try {
            if (mp.isPlaying()) {
                mp.stop();
                mp.release();
                mp = MediaPlayer.create(context, raw_data);
            }
            mp.setVolume(0.5f, 0.5f);
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //For hide status bar
    public void nbHideStatusBar() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);

        }
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        getWindow().setAttributes(params);
    }

    //For check device is mobile ot tablet
    public boolean nbIsPhoneDevice() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
    }

    //For check DB existance
    protected boolean nbCheckDataBase(String db_name) {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = "/data/data/" + getPackageName() + "/databases/" + db_name;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {
            //database does't exist yet.
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    //For get BaseEncode64 String from image
    public String nbGetEncoded64ImageStringFromBitmap(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String temp = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return temp;
    }

    //For check app install or not
    public boolean nbCheckAppInstall(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    //For get image uri
    public Uri nbGetImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, UUID.randomUUID().toString() + ".png", "drawing");
        return Uri.parse(path);
    }

    //For save image- handle permission yourself
    public String nbSaveImageBitmap(Bitmap bitmap, String name, String path) {
        File file = null;
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + path);
            myDir.mkdirs();
            file = new File(myDir, name);
            if (file.exists()) {
                return file.toString();
            }
            FileOutputStream out = new FileOutputStream(file);
            //UPDATE IMAGE QUALITY NIKHIL
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.toString();
    }

    //For send mail
    public void nbSendMail(String string, String sub) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{string});
        i.putExtra(Intent.EXTRA_SUBJECT, sub);
        // getResources().getString(R.string.mail_subject));
        i.putExtra(Intent.EXTRA_TEXT, "");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    //For dial number
    public void nbDialNumber(String s) {

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
            nbToast("Can not support calling functionality");
        } else {
            try {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + s));
                startActivity(callIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //For message
    public static void sendMessage(Context activity, String phoneNumber,
                                   String smsContent) {
        if (phoneNumber == null || phoneNumber.length() < 4) {
            return;
        }
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", smsContent);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(it);

    }

    //For date conversion
    //  ptn1="given date format"
    //  ptn2="desired date format"
    public String nbConvertDate(String ptn1, String ptn2, String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(ptn1, Locale.US);
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat(ptn2, Locale.US);
        return formatter.format(testDate);
    }

    //For get date difference
    public String nbDateDifference(Date startDate, Date endDate) {
        try {
            long different = endDate.getTime() - startDate.getTime();
            if (different <= 0) {
                return "0";
            }
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            String mSec = "";
            if (elapsedSeconds <= 9)
                mSec = "0";
            return " 0" + elapsedMinutes + " : " + mSec + elapsedSeconds + "";
            // return elapsedMinutes + " min " + elapsedSeconds + " Sec";

        } catch (Exception e) {
            return "0";
        }
    }

    //For go to next activity without trace
    public void nbToNextActivityWithoutTrack(Class temp) {

        Intent intent = new Intent(getApplicationContext(), temp);
        startActivity(intent);
        finish();
    }

    //For go to next activity with trace
    public void nbToNextActivity(Class temp) {

        Intent intent = new Intent(getApplicationContext(), temp);
        startActivity(intent);
    }

    //For copy file
    public static void nbCopyFile(File from, File to) throws IOException {
        InputStream in = new BufferedInputStream(new FileInputStream(from));
        try {
            OutputStream out = new BufferedOutputStream(new FileOutputStream(to));
            try {
                copyAllBytes(in, out);
            } finally {
                safeClose(out);
            }
        } finally {
            safeClose(in);
        }
    }

    private static int copyAllBytes(InputStream in, OutputStream out) throws IOException {
        int byteCount = 0;
        byte[] buffer = new byte[8192];
        while (true) {
            int read = in.read(buffer);
            if (read == -1) {
                break;
            }
            out.write(buffer, 0, read);
            byteCount += read;
        }
        return byteCount;
    }

    private static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // Silent
            }
        }
    }

    //For encode string
    public static String nbEncodeUrl(String stringToEncode) {
        try {
            return URLEncoder.encode(stringToEncode, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            throw new RuntimeException(e1);
        }
    }

    //For decode string
    public static String nbDecodeUrl(String stringToDecode) {
        try {
            return URLDecoder.decode(stringToDecode, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            throw new RuntimeException(e1);
        }
    }

    //For check device is rooted
    public static boolean nbIsDeviceRooted() {
        String su = "su";
        String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
                "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/"};
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }

    //For get sdk version
    public static int nbGetSDKVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    //For get Android ID
    public String nbGetAndroidID() {
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    //For get device manufacture
    public static String nbGetDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    //For get device model
    public static String nbGetDeviceModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    //For image Download in Async
    public void nbDownloadFromUrl(Context context, String imageURL, String fileName) {  //this is the downloader method

        Context mContext = context;
        FILE_NAME = fileName;
        new DownloadFromURL().execute(imageURL);


    }

    class DownloadFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nbToast("Image is Downloading. Please wait...");
        }

        @Override
        protected String doInBackground(String... fileUrl) {
            int count;
            try {

                File file = new File("/sdcard/" + FILE_NAME + ".jpg");
                if (file.exists())
                    file.delete();

                URL url = new URL(fileUrl[0]);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                // show progress bar 0-100%
                int fileLength = urlConnection.getContentLength();
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                OutputStream outputStream = new FileOutputStream(file);

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / fileLength));
                    outputStream.write(data, 0, count);
                }
                // flushing output
                outputStream.flush();
                // closing streams
                outputStream.close();
                inputStream.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }


        @Override
        protected void onPostExecute(String file_url) {
            nbToast("Image saved on this device");
        }
    }

    //For download image from url
    public void nbShowNoInternet() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getApplicationContext());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.no_internet_dialog, null);

        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCancelable(false);

        dialogView.findViewById(R.id.txtOkay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }

    //For Install apk(File & Uri)
    public static void nbInstallApk(Context context, Uri file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(file, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    //For Uninstall apk
    public static void nbUninstallApk(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        context.startActivity(intent);
    }

    //For Checking Service running or not
    public static boolean nbIsServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> servicesList = activityManager
                .getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo si : servicesList) {
            if (className.equals(si.service.getClassName())) {
                isRunning = true;
            }
        }
        return isRunning;
    }

    //For Stop Service
    public static boolean nbStopRunningService(Context context, String className) {
        Intent intent_service = null;
        boolean ret = false;
        try {
            intent_service = new Intent(context, Class.forName(className));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (intent_service != null) {
            ret = context.stopService(intent_service);
        }
        return ret;
    }

    //For check App is on Background or not
    public static boolean nbIsApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null
                    && !topActivity.getPackageName().equals(
                    context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    //For getting Bitmap from any view
    public static Bitmap nbGetBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        view.layout(view.getLeft(), view.getTop(), view.getRight(),
                view.getBottom());
        view.draw(canvas);

        return bitmap;
    }

    //For has SdCard or not
    public static boolean nbHasSDCard() {

        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    //For get file size
    public static String nbGetFileSize(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            int length = fis.available();
            if (length >= 1073741824) {
                return String.format("%.2f GB", length * 1.0 / 1073741824);
            } else if (length >= 1048576) {
                return String.format("%.2f MB", length * 1.0 / 1048576);
            } else {
                return String.format("%.2f KB", length * 1.0 / 1024);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "none";
    }

    //For getting file from Asset folder
    public static String nbGetFileFromAssets(Context context, String fileName) {
        if (context == null || TextUtils.isEmpty(fileName)) {
            return null;
        }

        StringBuilder s = new StringBuilder("");
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //For getting file from Raw folder
    public static String nbGeFileFromRaw(Context context, int resId) {
        if (context == null) {
            return null;
        }

        StringBuilder s = new StringBuilder();
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().openRawResource(resId));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //For text to Html
    public static String nbAddTextToHtml(String string) {
        return "<font color=\"red\">" + string + "</font>";
    }

    //For check Screen Oriented
    public static boolean nbIsLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    //For check Screen Oriented
    public static boolean nbIsPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    //For Share Image
    public static void nbShareImage(Context context, Uri uri, String title) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg");
        context.startActivity(Intent.createChooser(shareIntent, title));
    }

    //For Share Text
    public static void nbShareText(Context context, String extraText) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Text");
        intent.putExtra(Intent.EXTRA_TEXT, extraText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(
                Intent.createChooser(intent, "Choose"));
    }

    //For take screenshot with status bar return Bitmap
    public static Bitmap nbGetScreenShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = nbGetScreenSize(activity)[0];
        int height = nbGetScreenSize(activity)[1];
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;
    }

    //For take screenshot without status bar return Bitmap
    public static Bitmap nbGetScreenShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = nbGetScreenSize(activity)[0];
        int height = nbGetScreenSize(activity)[1];
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }

    //For Disable screen capture
    public static void nbDisableScreenshotFunctionality(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }



}
