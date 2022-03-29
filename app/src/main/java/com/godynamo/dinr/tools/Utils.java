package com.godynamo.dinr.tools;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Pair;
import android.view.ViewGroup;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by dankovassev on 2015-02-05.
 */
public class Utils {
    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (;;) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    /**
     * Take screenshot of the activity including the action bar
     *
     * @param activity
     * @return The screenshot of the activity including the action bar
     */
    public static Bitmap takeScreenshot(Activity activity) {
        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        decorChild.setDrawingCacheEnabled(true);
        decorChild.buildDrawingCache();
        Bitmap drawingCache = decorChild.getDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(drawingCache);
        decorChild.setDrawingCacheEnabled(false);
        return bitmap;
    }


    /**
     * Build alert dialog with properties and data
     *
     * @param pairs
     * @return {@link android.app.AlertDialog}
     */
    public static AlertDialog buildProfileResultDialog(Activity activity,
                                                       Pair<String, String>... pairs) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Pair<String, String> pair : pairs) {
            stringBuilder.append(String.format("<h3>%s</h3>", pair.first));
            stringBuilder.append(String.valueOf(pair.second));
            stringBuilder.append("<br><br>");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(Html.fromHtml(stringBuilder.toString()))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }

    /**
     * This method will return true if the list of date contain the month of the
     * date we pass and false otherwise.
     *
     * @param srcList
     * @param date
     * @return
     */
    public static boolean monthContains(List<Date> srcList, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(new Date());

        for (Date dateBcl : srcList) {
            if (date != null && dateBcl != null) {
                cal.setTime(dateBcl);
                cal2.setTime(date);
                if (cal.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Format on two digit after the comma the Amount
     */
    public static String formatAmount(Double amount) {

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.applyPattern("0.00");
        df.format(amount);
        return df.format(amount);
    }

    /**
     * Return a localized url
     */
    public static String returnLocalizedUrl(String url) {
        String locale = returnLocale();

        if (locale.equals("fr") || locale.equals("fra")) {
            url = url.replace("%@", "fr");
        } else {
            url = url.replace("%@", "en");
        }

        return url;
    }

    /**
     * Return language of the device
     */
    public static String returnLocale() {
        String retour = Locale.getDefault().getDisplayLanguage();

        if (retour.toLowerCase().contains("fr")) {
            retour = "fr";
        } else if (retour.equals("Spanish")) {
            retour = "es";
        } else {
            retour = "en";
        }
        return retour;
    }

    /**
     * This method can used to know if a locale is managed by Zweet App.
     *
     * @param testedLocale
     * @return true is the locale is used in the application false otherwise
     */
    public static Boolean isValidLocale(String testedLocale) {
        List<String> languageInApp = new ArrayList<String>();
        languageInApp.add("es");
        languageInApp.add("fr");
        languageInApp.add("en");
        if (testedLocale != null && !testedLocale.isEmpty()
                && languageInApp.contains(testedLocale)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
    /***
     * To
     */
    public static String toCapitalize(String enter)
    {
        String retour = enter.substring(0, 1).toUpperCase(Locale.US)
                + enter.substring(1);
        return retour;
    }

    public static String getCountryCarrier(Context context)
    {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String carrierName = manager.getNetworkCountryIso();
        return carrierName;
    }
}
