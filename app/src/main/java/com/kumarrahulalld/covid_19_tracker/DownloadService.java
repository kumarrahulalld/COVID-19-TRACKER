package com.kumarrahulalld.covid_19_tracker;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class DownloadService extends IntentService {
    public DownloadService() {
        super("Download");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(Intent intent) {
        String str = BuildConfig.FLAVOR;
        String con = intent.getStringExtra("country");
        ArrayList<String> res = new ArrayList<>();
        try {
            Document document = Jsoup.connect("https://www.worldometers.info/coronavirus/#countries").get();
            String head = str;
            String str2 = "@";
            String str3 = "Country, Other";
            String str4 = "tr";
            if (con.equals("All Countries")) {
                Iterator it = document.select(str4).iterator();
                while (it.hasNext()) {
                    Element e = (Element) it.next();
                    String head2 = str;
                    int j = 0;
                    while (true) {
                        if (j >= 11) {
                            break;
                        } else if (e.child(j).text().equals(str3)) {
                            break;
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append(head2);
                            sb.append(e.child(j).text());
                            sb.append(str2);
                            head2 = sb.toString();
                            j++;
                        }
                    }
                    res.add(head2);
                }
            } else {
                Iterator it2 = document.select(str4).iterator();
                while (it2.hasNext()) {
                    Element e2 = (Element) it2.next();
                    head = str;
                    if (e2.child(0).text().equals(con)) {
                        int j2 = 0;
                        while (true) {
                            if (j2 >= 11) {
                                break;
                            } else if (e2.child(j2).text().equals(str3)) {
                                break;
                            } else {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append(head);
                                sb2.append(e2.child(j2).text());
                                sb2.append(str2);
                                head = sb2.toString();
                                j2++;
                            }
                        }
                        res.add(head);
                    }
                }
                res.add(head);
            }
        } catch (IOException e3) {
            Log.d("Exception Covid-9", e3.getStackTrace().toString());
        }
        intent.setAction("Get");
        stopSelf();
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent.putStringArrayListExtra("bMessage", res));
    }

    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
