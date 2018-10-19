package com.adrian.tecmilenio.broadcast;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
    private Bundle bundle;
    private SmsMessage currentSMS;
    private String message;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdu_Objects = (Object[]) bundle.get("pdus");
                if (pdu_Objects != null) {

                    for (Object aObject : pdu_Objects) {

                        String format = bundle.getString("format");
                        currentSMS = SmsMessage.createFromPdu((byte[]) aObject, format);

                        String senderNo = currentSMS.getDisplayOriginatingAddress();

                        message = currentSMS.getDisplayMessageBody();
                        Toast.makeText(context, "senderNum: " + senderNo + " :\n message: " + message, Toast.LENGTH_LONG).show();
                        Log.d("SMS DEBUG", "senderNum: " + senderNo + " :\n message: " + message);
                        if (!message.startsWith("http://") && !message.startsWith("https://"))
                            message = "http://" + message;
                        try {
                            Uri webpage = Uri.parse(message);
                            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
                            context.startActivity(myIntent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(context, "No hay ninguna aplicación para abrir el link.",  Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    }
                    this.abortBroadcast();
                }
            }
        }
    }
}
