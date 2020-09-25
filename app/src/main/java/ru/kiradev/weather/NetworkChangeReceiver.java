package ru.kiradev.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private boolean wasNetInfoNull = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo == null && !wasNetInfoNull) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
            builder.setTitle("Отсутствует сеть")
                    .setMessage("Необходимо подключение к интернету")
                    .setIcon(R.drawable.ic_baseline_info_24)
                    .setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
            wasNetInfoNull = true;
        } else if (wasNetInfoNull && netInfo != null) {
            wasNetInfoNull = false;
            context.startActivity(new Intent(context, MainActivity.class));
        }
    }
}
