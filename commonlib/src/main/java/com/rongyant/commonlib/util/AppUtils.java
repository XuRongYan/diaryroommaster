package com.rongyant.commonlib.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by XRY on 2016/8/31.
 */
public class AppUtils {
    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getWidth(Activity context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getHeight(Activity context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * convert dip to px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dpToPx(Context context, float dpValue){
        final  float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue*scale+0.5f);
    }

    /**
     * convert px to dip
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int pxToDip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * convert sp to px
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int spToPx(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }

    /**
     * 外置SD卡是否存在
     *
     * @return
     */
    public static boolean isStorageExists(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return true;
        }
        return false;
    }

    /**
     * 隐藏键盘
     *
     * @param activity
     */
    public static void hideKeyboard(Activity activity){
        try {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context){
        if (context == null){
            return false;
        }
        // 获取手机所有连接管理对象（包括wifi，net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null){
            //获取网络连接管理对象
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                //判断当前网络已连接
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 没有网络
     */
    public static final int NETWORKTYPE_INVALID = 0;
    /**
     * wap网络
     */
    public static final int NETWORKTYPE_WAP = 1;
    /**
     * 2G网络
     */
    public static final int NETWORKTYPE_2G = 2;
    /**
     * 3G和3G以上网络，或统称为快速网络
     */
    public static final int NETWORKTYPE_3G = 3;
    /**
     * wifi网络
     */
    public static final int NETWORKTYPE_WIFI = 4;

    public static int getNetWorkType(Context context){
        int mNetWorkType = NETWORKTYPE_INVALID;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                mNetWorkType = NETWORKTYPE_WIFI;
            }else if (type.equalsIgnoreCase("MOBILE")){
                String proxyHost = android.net.Proxy.getDefaultHost();
                mNetWorkType = TextUtils.isEmpty(proxyHost)
                        ? (isFastMobileNetwork(context) ? NETWORKTYPE_3G : NETWORKTYPE_2G)
                        : NETWORKTYPE_WAP;
            }
        }else {
            mNetWorkType = NETWORKTYPE_INVALID;
        }
        return mNetWorkType;
    }

    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }

    /**
     * 显示是否进行网络设置的对话框
     *
     * @param context
     */
    public static void setNetworkMethod(final Context context) {
        // 提示对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        try {
            builder.setTitle("网络设置提示")
                    .setMessage("网络连接不可用,是否进行设置?")
                    .setPositiveButton("设置",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Intent intent = null;
                                    // 判断手机系统的版本 即API大于10 就是3.0或以上版本
                                    if (android.os.Build.VERSION.SDK_INT > 10) {
                                        intent = new Intent(
                                                android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                                    } else {
                                        intent = new Intent();
                                        ComponentName component = new ComponentName(
                                                "com.android.settings",
                                                "com.android.settings.WirelessSettings");
                                        intent.setComponent(component);
                                        intent.setAction("android.intent.action.VIEW");
                                    }
                                    context.startActivity(intent);
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            }).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * return if str is empty
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0 || str.equalsIgnoreCase("null") || str.isEmpty() || str.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * get format date
     *
     * @param timemillis
     * @return
     */
    public static String getFormatDate(long timemillis) {
        return new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA).format(new Date(timemillis));
    }

    /**
     * convert time str
     *
     * @param time
     * @return
     */
    public static String convertTime(int time) {

        time /= 1000;
        int minute = time / 60;
        int second = time % 60;
        minute %= 60;
        return String.format(Locale.CHINA,"%02d:%02d", minute, second);
    }

    /**
     * url is usable
     *
     * @param url
     * @return
     */
    public static boolean isUrlUsable(String url) {
        if (AppUtils.isEmpty(url)) {
            return false;
        }

        URL urlTemp = null;
        HttpURLConnection connt = null;
        try {
            urlTemp = new URL(url);
            connt = (HttpURLConnection) urlTemp.openConnection();
            connt.setRequestMethod("HEAD");
            int returnCode = connt.getResponseCode();
            if (returnCode == HttpURLConnection.HTTP_OK) {
                return true;
            }
        } catch (Exception e) {
            return false;
        } finally {
            connt.disconnect();
        }
        return false;
    }

    /**
     * is url
     *
     * @param url
     * @return
     */
    public static boolean isUrl(String url) {
        Pattern pattern = Pattern.compile(
                        "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)" +
                                "(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
        return pattern.matcher(url).matches();
    }

    /**
     * get toolbar height
     *
     * @param context
     * @return
     */
    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

    /**
     * 判断是否为手机号
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        String expression = "((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(phoneNumber);
        if (matcher.matches() ) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 日期是不是匹配
     * @param date
     * @return
     */
    public static boolean isDateValid(String date){
        boolean isValid = false;
        String expression = "^\\d{4}(\\.)\\d{1,2}\\1\\d{1,2}$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(date);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isEmailValid(String email){
        boolean isValid = false;
        String expression = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 获取手机的状态栏高度(单位为px像素)
     */
    public static int getStatusBarHeight(Context ctx) {
        int height = 0;
        int resId = ctx.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            height = ctx.getResources().getDimensionPixelSize(resId);
        }
        return height;
    }

}
