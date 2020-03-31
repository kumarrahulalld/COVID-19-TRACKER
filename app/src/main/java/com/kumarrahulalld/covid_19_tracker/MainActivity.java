package com.kumarrahulalld.covid_19_tracker;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    String[] countries = {"Please Select A Country", "All Countries", "Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Anguilla", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Brazil", "British Virgin Islands", "Brunei", "Bulgaria", "Burkina Faso", "Cabo Verde", "Cambodia", "Cameroon", "Canada", "CAR", "Cayman Islands", "Chad", "Channel Islands", "Chile", "China", "Colombia", "Congo", "Costa Rica", "Croatia", "Cuba", "Curaçao", "Cyprus", "Czechia", "Denmark", "Diamond Princess", "Djibouti", "Dominica", "Dominican Republic", "DRC", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Eswatini", "Ethiopia", "Faeroe Islands", "Fiji", "Finland", "France", "French Guiana", "French Polynesia", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Isle of Man", "Israel", "Italy", "Ivory Coast", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macao", "Madagascar", "Malaysia", "Maldives", "Mali", "Malta", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Moldova", "Monaco", "Mongolia", "Montenegro", "Montserrat", "Morocco", "Mozambique", "MS Zaandam", "Myanmar", "Namibia", "Nepal", "Netherlands", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "North Macedonia", "Norway", "Oman", "Pakistan", "Palestine", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland", "Portugal", "Qatar", "Romania", "Russia", "Rwanda", "Réunion", "S. Korea", "Saint Kitts and Nevis", "Saint Lucia", "Saint Martin", "San Marino", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Singapore", "Sint Maarten", "Slovakia", "Slovenia", "Somalia", "South Africa", "Spain", "Sri Lanka", "St. Barth", "St. Vincent Grenadines", "Sudan", "Suriname", "Sweden", "Switzerland", "Syria", "Taiwan", "Tanzania", "Thailand", "Timor-Leste", "Togo", "Trinidad and Tobago", "Tunisia", "Turkey", "Turks and Caicos", "UAE", "Uganda", "UK", "Ukraine", "Uruguay", "USA", "Uzbekistan", "Vatican City", "Venezuela", "Vietnam", "World", "Zambia", "Zimbabwe"};
    private LocalBroadcastManager lbman;
    MessageR myReceiver;

    /* renamed from: pd */
    ProgressDialog f32pd;

    public class MessageR extends BroadcastReceiver {
        public MessageR() {
        }

        public void onReceive(Context context, Intent intent) {
            MainActivity.this.addata(intent.getStringArrayListExtra("bMessage"));
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        Spinner con = (Spinner) findViewById(R.id.country);
        ArrayAdapter<String> a = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, this.countries);
        a.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        con.setAdapter(a);
        con.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View selectedItemView, int position, long id) {
                MainActivity.this.CountryChanged();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
    }

    public void CountryChanged() {
        Spinner con = (Spinner) findViewById(R.id.country);
        if (con.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "No Country Selected , Please Select A Country.", Toast.LENGTH_LONG).show();
            return;
        }
        this.f32pd = new ProgressDialog(this);
        this.f32pd.setTitle("Please Wait , Fetching COVID Data.");
        this.f32pd.show();
        ((TableLayout) findViewById(R.id.content)).removeAllViewsInLayout();
        this.myReceiver = new MessageR();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Get");
        LocalBroadcastManager.getInstance(this).registerReceiver(this.myReceiver, intentFilter);
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra("country", con.getSelectedItem().toString());
        startService(intent);
    }

    public void addata(ArrayList<String> e) {
        String str;
        String str2;
        int done = 0;
        String[] strArr = new String[1];
        Collections.sort(e);
        e.add(0, "Conutry, Other@Total Cases@New Cases@Total Deaths@New Deaths@Total Recovered@Active Cases@Serious , Critical@Total Cases Per Million Population@Total Deaths Per Million Population@ First Case Reported On");
        TableLayout gv = (TableLayout) findViewById(R.id.content);
        gv.removeAllViewsInLayout();
        int i = 0;
        while (true) {
            str = "-NA-";
            str2 = "@";
            if (i >= e.size()) {
                break;
            }
            TableRow tr = new TableRow(this);
            tr.setBackgroundResource(R.drawable.row_border);
            TextView[] a = new TextView[1000];
            String[] val = ((String) e.get(i)).split(str2);
            for (int j = 0; j < val.length; j++) {
                if (val[0].equals("Total:")) {
                    done = i;
                } else {
                    a[j] = new TextView(this);
                    a[j].setBackgroundResource(R.drawable.row_border);
                    if (val[j].isEmpty()) {
                        val[j] = str;
                    }
                    a[j].setText(val[j]);
                    a[j].setPadding(10, 10, 10, 10);
                    tr.addView(a[j]);
                }
            }
            gv.addView(tr);
            i++;
        }
        if (done != 0) {
            TableRow tr2 = new TableRow(this);
            tr2.setBackgroundResource(R.drawable.row_border);
            TextView[] a2 = new TextView[1000];
            String[] val2 = ((String) e.get(done)).split(str2);
            for (int j2 = 0; j2 < val2.length; j2++) {
                a2[j2] = new TextView(this);
                a2[j2].setBackgroundResource(R.drawable.row_border);
                if (val2[j2].isEmpty()) {
                    val2[j2] = str;
                }
                a2[j2].setText(val2[j2]);
                a2[j2].setPadding(10, 10, 10, 10);
                tr2.addView(a2[j2]);
            }
            gv.addView(tr2);
        }
        this.f32pd.dismiss();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.myReceiver);
        super.onStop();
    }
}
