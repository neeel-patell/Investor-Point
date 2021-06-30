package in.weareindian.investorapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.view.ContextThemeWrapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.weareindian.investorapp.R;

import static java.lang.Math.PI;

public class CommonUtils {

    public static final String KEY_GCM_REG_ID = "GCM_REG_ID";
    public static String gcmId = "";
    public static String projectGCMiD;
    private static long mLastClickTime = 0;
    private static int timeBetweenClick = 600; //in ns
    //public static CustomProgressDialog progressDialog = null;
    public static ProgressDialog progressDialog = null;

    //distance in miles
    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = (dist * 60 * 1.1515) / 0.621371;
        return (dist);
    }

    public static double getAngleBetweenTwoLatLong(double lat1, double lon1, double lat2, double lon2) {

        //double lat1, double lon1, double lat2, double lon2
        double RLat1 = lat1 * PI / 180;
        double RLon1 = lon1 * PI / 180;
        double RLat2 = lat2 * PI / 180;
        double RLon2 = lon2 * PI / 180;

        double dLon = (RLon2 - RLon1);
        double y = Math.sin(dLon) * Math.cos(RLat2);
        double x = Math.cos(RLat1) * Math.sin(RLat2) - Math.sin(RLat1) * Math.cos(RLat2) * Math.cos(dLon);
        double brng = Math.toDegrees((Math.atan2(y, x)));
        //brng = (360 - ((brng + 360) % 360));
        return brng;
    }

    /*public static double getAngleBetweenTwoLatLong(double lat1, double lon1, double lat2, double lon2) {

        //double lat1, double lon1, double lat2, double lon2
        double RLat1 = lat1 * PI / 180;
        double RLon1 = lon1 * PI / 180;
        double RLat2 = lat2 * PI / 180;
        double RLon2 = lon2 * PI / 180;

        double dLon = (lon2 - lon1);
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
        double brng = Math.toDegrees((Math.atan2(y, x)));
        brng = (360 - ((brng + 360) % 360));
        return brng;
    }*/

    private static double deg2rad(double deg) {
        return (deg * PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / PI);
    }

    public static String formatNumber(long no) {
        return String.format("%02d", no);
    }

    public static void hideKeyboard(Context context, View view) {
        if (view != null) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static long getPreparationTime(String dateStr, String pTime) {
        long diffMinute = 0;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(dateStr);
            long confirmDateTime = date.getTime() + Long.parseLong(pTime) * 60 * 1000;
            long currentDateTime = Calendar.getInstance().getTimeInMillis();

            diffMinute = (confirmDateTime - currentDateTime) / (60000);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return diffMinute;
    }


    /*public static String getDateForTicketList(String dateStr, String InputFormat,String outputFormat) {

        Date date = new Date();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat();
        return dateStr;
    }*/
    public static long getHoursMinuteSecondsFromTwoDate(String dateStr, String pTime, String condition) {
        long diffMinute = 0;
        long confirmDateTime = 0;
        long currentDateTime = 0;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(dateStr);
            confirmDateTime = date.getTime() + Long.parseLong(pTime) * 60 * 1000;
            currentDateTime = Calendar.getInstance().getTimeInMillis();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        long milliSec = confirmDateTime - currentDateTime;
        long totalSecs = (confirmDateTime - currentDateTime) / 1000;
        long hours = (totalSecs / 3600);
        long mins = (totalSecs / 60) % 60;
        long secs = totalSecs % 60;
        String minsString = (mins == 0)
                ? "00"
                : ((mins < 10)
                ? "0" + mins
                : "" + mins);
        String secsString = (secs == 0)
                ? "00"
                : ((secs < 10)
                ? "0" + secs
                : "" + secs);
        if (condition.equalsIgnoreCase("hours")) {
            return hours;
        } else if (condition.equalsIgnoreCase("min")) {
            return mins;
        } else if (condition.equalsIgnoreCase("sec")) {
            return secs;
        } else if (condition.equalsIgnoreCase("milliseconds")) {
            return milliSec;
        } else {
            return secs;
        }
    }

    public static String getCurrentTimeZone() {
        return TimeZone.getDefault().getID();
    }


    public static String UTCtoCurrentTimeZone(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        SimpleDateFormat lastDf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df.setTimeZone(TimeZone.getDefault());
        return lastDf.format(date);
    }

    public static String UTCtoCurrentDate(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        SimpleDateFormat lastDf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df.setTimeZone(TimeZone.getDefault());
        return lastDf.format(date);
    }

    public static String UTCtoCurrentTime(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        SimpleDateFormat lastDf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df.setTimeZone(TimeZone.getDefault());
        return lastDf.format(date);
    }

    public static boolean isPasswordLengthValid(String password) {
        boolean isValid = false;

        if (password.length() < 6)
            isValid = false;
        else
            isValid = true;
        return isValid;
    }

    public static boolean isPasswordContain(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    public static String countRemainingTime(String dateStr, String minute) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df.setTimeZone(TimeZone.getDefault());

        long orderDateLong = date.getTime();
        long orderDeliveryTime = orderDateLong + (Integer.parseInt(minute) * 60 * 1000);
        long currentTimeLong = Calendar.getInstance().getTimeInMillis();

        long diff = orderDeliveryTime - currentTimeLong;

        long mns = diff / 6000;

        return mns + "";
    }

    public boolean isValidYoutubeURL(String url) {

        String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";

        Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = compiledPattern.matcher(url);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static String formatNo(int no) {
        return String.format("%02d", no);
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static boolean isNavigationBarAvailable() {

        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);

        return (!(hasBackKey && hasHomeKey));
    }

    public static int getNavigationBarHeight(Context context) {
        if (!isNavigationBarAvailable()) {
            return 0;
        } else {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return resources.getDimensionPixelSize(resourceId);
            }
        }
        return 0;
    }

    public static int getStatusBarHeight(Context c) {
        int result = 0;
        int resourceId = c.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = c.getResources().getDimensionPixelSize(resourceId);
        }
        return result;

    }

    public static boolean isAlphaBet(String s) {
        String pattern = "^[a-zA-Z]*$";
        if (s.matches(pattern)) {
            return true;
        }
        return false;
    }

    public static String getChatTimeFromStamp(String date) {
        String time = "";

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(Long.parseLong(date));
        time = android.text.format.DateFormat.format("hh:mm a", c).toString();
        return time;
    }

    public static String getDateTimeFormet(String dateString, int pos) {

        String newDateStr = "";
        Date date = null;
        try {
            if (pos != 5) {
                SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = form.parse(dateString);
            }

            SimpleDateFormat postFormater = null;
            if (pos == 1) {
                postFormater = new SimpleDateFormat("dd-MMM-yyyy, hh:mm a");
            } else if (pos == 2) {
                postFormater = new SimpleDateFormat("yyyy-MM-dd");
            } else if (pos == 3) {
                postFormater = new SimpleDateFormat("HH:mm");
            } else if (pos == 4) {
                postFormater = new SimpleDateFormat("dd-MM-yyyy");
            } else if (pos == 5) {
                SimpleDateFormat form1 = new SimpleDateFormat("dd-MM-yyyy");
                date = form1.parse(dateString);
                postFormater = new SimpleDateFormat("yyyy-MM-dd");
            } else if (pos == 6) {
                postFormater = new SimpleDateFormat("MMMM-dd-yyyy");
            }
            newDateStr = postFormater.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDateStr;
    }

    public static boolean isKeyboardOpen(Context context) {
        boolean abc = false;
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            abc = false;
        } else {
            abc = true;
        }
        return abc;
    }

    public static void showSoftKeyboard(Activity context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED,
                0);
    }

    public static void hideSoftKeyboard(Activity activity) {
        final InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            if (activity.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public static String encodeStringToBase64(final String message) {
        byte[] data = null;
        try {
            data = message.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(data, Base64.NO_WRAP);
    }

    public static String decodeStringToBase64(final String message) {
        String decodeString = "";
        try {
            final byte[] data = Base64.decode(message, Base64.DEFAULT);
            decodeString = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodeString;
    }

    public static boolean isNetworkAvailable(Context mContext) {

        /* getting systems Service connectivity manager */
        ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (mConnectivityManager != null) {
            NetworkInfo[] mNetworkInfos = mConnectivityManager
                    .getAllNetworkInfo();
            if (mNetworkInfos != null) {
                for (int i = 0; i < mNetworkInfos.length; i++) {
                    if (mNetworkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static int getAge(int _year, int _month, int _day) {
        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(_year, _month, _day);
        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH)) || ((m == cal.get(Calendar.MONTH)) && (d < cal.get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        if (a < 0)
            throw new IllegalArgumentException("Age < 0");
        return a;
    }

    public static int getActionBarBarHeight(Context Con) {
        int result = 0;
        TypedValue tv = new TypedValue();
        if (Con.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            result = TypedValue.complexToDimensionPixelSize(tv.data, Con.getResources().getDisplayMetrics());
        }

        return result;
    }

    public static boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but
            // all we need
            // to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }

        if (mExternalStorageAvailable == true && mExternalStorageWriteable == true) {
            return true;
        } else {
            return false;
        }
    }

    public static String getDateDifference(Date thenDate) {
        Calendar now = Calendar.getInstance();
        Calendar then = Calendar.getInstance();
        now.setTime(new Date());
        then.setTime(thenDate);

        // Get the represented date in milliseconds
        long nowMs = now.getTimeInMillis();
        long thenMs = then.getTimeInMillis();

        // Calculate difference in milliseconds
        long diff = nowMs - thenMs;

        // Calculate difference in seconds
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);

        if (diffMinutes < 60) {
            if (diffMinutes == 1)
                return diffMinutes + " minute ago";
            else
                return diffMinutes + " minutes ago";
        } else if (diffHours < 24) {
            if (diffHours == 1)
                return diffHours + " hour ago";
            else
                return diffHours + " hours ago";
        } else if (diffDays < 30) {
            if (diffDays == 1)
                return diffDays + " day ago";
            else
                return diffDays + " days ago";
        } else {
            return "a long time ago..";
        }
    }

    public static boolean isEditValid(EditText view) {
        boolean isValid = false;

        EditText mEditText = view;

        if (mEditText.getText().toString().trim().length() > 0) {
            isValid = true;
        }

        return isValid;
    }

    public static boolean isValidMobileNumber(EditText view) {
        boolean isValid = false;

        EditText mEditText = view;

        if (mEditText.getText().toString().trim().length() == 10) {
            isValid = true;
        }

        return isValid;
    }

    public static boolean isValidEmail(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isValidPassword(String password) {
        boolean isValid = false;

        if (password.length() < 6)
            isValid = false;
        else
            isValid = true;
        return isValid;
    }

    public static boolean isValidInternationalMobile(String email) {
        boolean isValid = false;

        String expression = "^\\+?[1-9]\\d{4,14}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public class LimitedRangeDatePickerDialog extends DatePickerDialog {

        private Calendar minDate;
        private Calendar maxDate;
        private DateFormat mTitleDateFormat;

        public LimitedRangeDatePickerDialog(Context context, OnDateSetListener callBack, int year,
                                            int monthOfYear, int dayOfMonth, Calendar minDate, Calendar maxDate) {
            super(context, callBack, year, monthOfYear, dayOfMonth);
            this.minDate = minDate;
            this.maxDate = maxDate;
            mTitleDateFormat = DateFormat.getDateInstance(DateFormat.FULL);
        }

        public void onDateChanged(DatePicker view, int year, int month, int day) {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, month, day);

            if (minDate != null && minDate.after(newDate)) {
                view.init(minDate.get(Calendar.YEAR), minDate.get(Calendar.MONTH), minDate.get(Calendar.DAY_OF_MONTH),
                        this);
                setTitle(mTitleDateFormat.format(minDate.getTime()));
            } else if (maxDate != null && maxDate.before(newDate)) {
                view.init(maxDate.get(Calendar.YEAR), maxDate.get(Calendar.MONTH), maxDate.get(Calendar.DAY_OF_MONTH),
                        this);
                setTitle(mTitleDateFormat.format(maxDate.getTime()));
            } else {
                view.init(year, month, day, this);
                setTitle(mTitleDateFormat.format(newDate.getTime()));
            }
        }
    }

    public static boolean isClickEnable() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < timeBetweenClick)
            return false;
        else {
            mLastClickTime = SystemClock.elapsedRealtime();
            return true;
        }
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    public static String getDeviceOSVersion() {
        //String manufacturer = Build.MANUFACTURER;
        //String model = Build.MODEL;
        //int version = Build.VERSION.SDK_INT;
        String versionRelease = Build.VERSION.RELEASE;

        return versionRelease;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    public static String formatTime(String time) {
        String inputPattern = "HH:mm:ss";
        String outputPattern = "HH:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getAppVersion(Context context) {
        String version = "";
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = info.versionName;
        return version;
    }

    public static String getCountryName(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address result;

            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getCountryName();
            }
            return null;
        } catch (IOException ignored) {
        }
        return null;
    }

    public static String getStateName(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address result;

            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getAdminArea();
            }
            return null;
        } catch (IOException ignored) {
        }
        return null;
    }

    public static String getCityName(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address result;

            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getLocality();
            }
            return null;
        } catch (IOException ignored) {
        }
        return null;
    }

    public static double getSixDigitAfterDecimal(double value) {
        DecimalFormat dFormat = new DecimalFormat(".######");
        Double new_value = Double.valueOf(dFormat.format(value));
        return new_value;
    }

    public static String getDateFromTimeStamp(long timestamp) {
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            Date currentTimeZone = (Date) calendar.getTime();
            return sdf.format(currentTimeZone);
        } catch (Exception e) {
        }
        return "";
    }

    public static String changeDateFormat(String inputDateString, String inputFormat, String outputFormat) {
        String outputDateString = "";

        SimpleDateFormat format = new SimpleDateFormat(inputFormat);
        Date newDate = null;
        try {
            newDate = format.parse(inputDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat(outputFormat);
        outputDateString = format.format(newDate);

        return outputDateString;

    }

    public static void showProgressDialog(Context context) {
        /*progressDialog = new CustomProgressDialog(context);
        progressDialog.show();*/

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    public static void hideProgressDialog() {
        /*if (progressDialog != null) {
            progressDialog.hide();
        }*/
        progressDialog.dismiss();
    }

    public static String removeFractionFromLatLong(String latLong) {

        String d = latLong;
        /*double d = Double.parseDouble(latLong);
        DecimalFormat dFormat = new DecimalFormat("##.######");
        d = Double.valueOf(dFormat.format(d));*/
        if (latLong.length() > 9)
            d = latLong.substring(0, 9);

        return "" + d;
    }

    public static String getJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("countryList.json");
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

    public static String getCurrentVersion(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1.0";
    }

    public static String oneDigitAfterDecimal(Double distance) {
        //return new DecimalFormat("##.##").format(distance);
        return String.format(Locale.US, "%.1f", distance);
    }

    public static String twoDigitAfterDecimal(Double distance) {
        //return new DecimalFormat("##.##").format(distance);
        return String.format(Locale.US, "%.2f", distance);
    }

    public static void refreshGallery(Context context,File file)
    {
        MediaScannerConnection.scanFile(context, new String[]
                        { file.toString() },
                null,
                new MediaScannerConnection.OnScanCompletedListener()
                {
                    public void onScanCompleted(String path, Uri uri)
                    {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }

}
