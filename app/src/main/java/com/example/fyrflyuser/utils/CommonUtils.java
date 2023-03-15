package com.example.fyrflyuser.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.content.CursorLoader;


import com.example.fyrflyuser.R;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class CommonUtils {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    private static AlertDialog alertDialog;

    public static void showDialog(Activity activity, String message) {
        dismissDialog();
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        View mview = activity.getLayoutInflater().inflate(R.layout.progress_dialog, null);
        alert.setView(mview);
        alertDialog = alert.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);

        // txtMessage.setText("" + message);

    }

    public static void dismissDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    public static RequestBody getRequestBodyText(String data) {

        return RequestBody.create(MediaType.parse("text/plain"), data);

    }

    public static void xToastServerNotRespond(Context context) {
        Toast.makeText(context, "Server not responding. Please try after some time", Toast.LENGTH_SHORT).show();
    }

    public static String getRealPath(Context context, Uri fileUri) {
        String realPath;
        // SDK < API11
        if (Build.VERSION.SDK_INT < 11) {
            realPath = CommonUtils.getRealPathFromURI_BelowAPI11(context, fileUri);
        }
        // SDK >= 11 && SDK < 19
        else if (Build.VERSION.SDK_INT < 19) {
            realPath = CommonUtils.getRealPathFromURI_API11to18(context, fileUri);
        }
        // SDK > 19 (Android 4.4) and up
        else {
            realPath = CommonUtils.getRealPathFromURI_API19(context, fileUri);
        }
        return realPath;
    }

    public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = 0;
        String result = "";
        if (cursor != null) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
            cursor.close();
            return result;
        }
        return result;
    }

    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
            cursor.close();
        }
        return result;
    }


    public static String getRealPathFromURI_API19(final Context context, final Uri uri) {

        final boolean isKitKat = true;

        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean phonePatternChecker(String phone) {
        return (!TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches());
    }

    public static boolean emailPatternChecker(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static String getUserId() {
        return App.getPrefManager().getString(AppConstants.USER_ID);


    }

    public static MultipartBody.Part getFileData(String path, String parameter) {

        if (path != null) {

            File file = new File(path);

            final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            return MultipartBody.Part.createFormData(parameter, file.getName(), requestFile);

        } else {

            final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");

            return MultipartBody.Part.createFormData(parameter, "", requestFile);

        }
    }
    public static class RationaleDialog extends DialogFragment {
        private static final String ARGUMENT_PERMISSION_REQUEST_CODE = "requestCode";
        private static final String ARGUMENT_FINISH_ACTIVITY = "finish";
        private boolean mFinishActivity = false;

        public static RationaleDialog newInstance(int requestCode, boolean finishActivity) {
            Bundle arguments = new Bundle();
            arguments.putInt(ARGUMENT_PERMISSION_REQUEST_CODE, requestCode);
            arguments.putBoolean(ARGUMENT_FINISH_ACTIVITY, finishActivity);
            RationaleDialog dialog = new RationaleDialog();
            dialog.setArguments(arguments);
            return dialog;
        }


    }
    public static void requestPermission(FragmentActivity activity, int requestId, String permission, boolean finishActivity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            // Display a dialog with rationale.
            CommonUtils.RationaleDialog.newInstance(requestId, finishActivity).show(activity.getSupportFragmentManager(), "dialog");
        } else {
            // Location permission has not been granted yet, request it.
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestId);
        }


    }


    /**
     * Round a float value to a specific decimal place
     * @param amount - the value to round
     * @param decimalPlace - to what decimal place to round the amount to
     * @return rounded number
     */
    public BigDecimal round(float amount, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(amount));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }


    /**
     * Returns array list with all of the driver rides available for this
     * application.
     * @param activity - activity that called this function
     * @return typeArrayList - array list with all the driver types
     */
    public static ArrayList<TypeObject> getTypeList(Activity activity){
        ArrayList<TypeObject> typeArrayList = new ArrayList<>();


        typeArrayList.add(new TypeObject("type_1", activity.getResources().getString(R.string.type_1), activity.getResources().getDrawable(R.drawable.car1), 4));
        typeArrayList.add(new TypeObject("type_2", activity.getResources().getString(R.string.type_2), activity.getResources().getDrawable(R.drawable.suv), 7));
        typeArrayList.add(new TypeObject("type_3", activity.getResources().getString(R.string.type_3), activity.getResources().getDrawable(R.drawable.car1), 4));
        typeArrayList.add(new TypeObject("type_4", activity.getResources().getString(R.string.type_4), activity.getResources().getDrawable(R.drawable.group__34_), 1));

        return  typeArrayList;
    }


    /**
     * Calculate Ride cost estimate
     */
    public static int rideCostEstimate(double distance, double duration){
        double price;
        price = 36 + distance * 26 + duration * 0.001;
        return (int) price;
    }

    /**
     * get type object that is in the arrayList with a certain id
     * @param activity - activity that called this function
     * @param id - id of the object to find
     * @return - type object
     */
    public static TypeObject getTypeById(Activity activity, String id){
        ArrayList<TypeObject> typeArrayList = getTypeList(activity);
        for(TypeObject mType : typeArrayList){
            if(mType.getId().equals(id)){
                return mType;
            }
        }
        return null;
    }
}
