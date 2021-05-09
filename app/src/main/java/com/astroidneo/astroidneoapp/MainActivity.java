package com.astroidneo.astroidneoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.MailTo;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.astroidneo.astroidneoapp.Adapter.AstroidAdapter;
import com.astroidneo.astroidneoapp.Planet.AstroidPlanet;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView textViewdate, textViewavg, textViewenddate;
    RecyclerView recyclerView;
    LinearLayout linearLayout;
    Button button;
    String start_date, id, magnitude, velocity, distance, end_date, close_approach_date;
    final Calendar myCalendar = Calendar.getInstance();
    private int day,month,year;
    private ProgressDialog progress;
    ServiceHandler shh;
    List<AstroidPlanet> mPlanetlist = new ArrayList<>();
    AstroidAdapter adapter;
    ArrayList<String> arrayList = new ArrayList();
    ArrayList<String> arrayid = new ArrayList();
    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntriesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewdate = (TextView) findViewById(R.id.tv_date);
        textViewenddate = (TextView) findViewById(R.id.tv_end_date);
        textViewavg = (TextView) findViewById(R.id.tv_total_avg);

        linearLayout = (LinearLayout) findViewById(R.id.layout_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));

        button = (Button) findViewById(R.id.btn_submit);

        barChart = findViewById(R.id.idBarChart);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }

        };

        textViewdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        textViewenddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(MainActivity.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                start_date = textViewdate.getText().toString().trim();
                end_date = textViewenddate.getText().toString().trim();

                if (CheckNetwork.isInternetAvailable(MainActivity.this))
                {
                    mPlanetlist.clear();
                    new GetProductData().execute();
                }
                else
                {
                    isNetworkOnline(MainActivity.this);
                }

            }
        });

    }

    private void getBarEntries() {
        // creating a new array list
        barEntriesArrayList = new ArrayList<>();

        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.

        barEntriesArrayList.add(new BarEntry(1f, 20));
        barEntriesArrayList.add(new BarEntry(2f, 20));
        barEntriesArrayList.add(new BarEntry(3f, 21));
        barEntriesArrayList.add(new BarEntry(4f, 20));
        barEntriesArrayList.add(new BarEntry(5f, 19));
        barEntriesArrayList.add(new BarEntry(6f, 29));
        barEntriesArrayList.add(new BarEntry(7f, 24));
    }

    public static boolean isNetworkOnline(Context con)
    {
        boolean status = false;
        try
        {
            ConnectivityManager cm = (ConnectivityManager) con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);

            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);

                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                    status = true;
                } else {
                    status = false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return status;
    }

    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        textViewdate.setText(sdf.format(myCalendar.getTime()));

    }

    private void updateLabel1() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        textViewenddate.setText(sdf.format(myCalendar.getTime()));

    }

    class GetProductData extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {

            shh = new ServiceHandler();
            String queryUrl = "https://api.nasa.gov/neo/rest/v1/feed?";
            String queryKey = "api_key=rtg73OHSKTlFdr1cqMAumYl9aWfoQReBFlrV6izc";
            String queryDate = "startdate=" + start_date + "&end_date=" + end_date + "&";
            String url = queryUrl + queryDate + queryKey;
            Log.d("Url:",">"+url);

            try {

                List<NameValuePair> params2 = new ArrayList<>();
                String jsonStr = shh.makeServiceCall(url, ServiceHandler.GET, null);

                if (jsonStr != null) {

                    JSONObject c1 = new JSONObject(jsonStr);
                    JSONObject a1 = c1.getJSONObject("near_earth_objects");
                    JSONArray classArray = a1.getJSONArray("2021-05-09");
                    for(int i = 0; i < classArray.length(); i++)
                    {
                        JSONObject b1 = classArray.getJSONObject(i);
                        id = b1.getString("neo_reference_id");
                        String mang = b1.getString("absolute_magnitude_h");
                        JSONObject magnitute = b1.getJSONObject("estimated_diameter");
                        JSONObject kilometer = magnitute.getJSONObject("kilometers");
                        magnitude = kilometer.getString("estimated_diameter_max");
                        JSONArray arr = b1.getJSONArray("close_approach_data");
                        for(int j = 0; j < arr.length(); j++)
                        {
                            JSONObject vc_obj = arr.getJSONObject(j);
                            close_approach_date = vc_obj.getString("close_approach_date");
                            JSONObject j_obj = vc_obj.getJSONObject("relative_velocity");
                            velocity = j_obj.getString("kilometers_per_hour");
                            JSONObject j_obj1 = vc_obj.getJSONObject("miss_distance");
                            distance = j_obj1.getString("kilometers");

                            AstroidPlanet planet = new AstroidPlanet(id, magnitude, velocity, distance, close_approach_date, mang);
                            mPlanetlist.add(planet);

                            arrayid.add(id);

                        }
                    }

                } else {
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("ServiceHandler","Json Error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    arrayList.addAll(arrayid);
                    linearLayout.setVisibility(View.VISIBLE);

                    adapter = new AstroidAdapter(MainActivity.this, mPlanetlist);
                    recyclerView.setAdapter(adapter);

                    double total = 0;
                    double avg = 0;
                    int size = mPlanetlist.size();
                    for(int i = 0; i < mPlanetlist.size(); i++)
                    {
                        AstroidPlanet planet = mPlanetlist.get(i);
                        double dist = Double.parseDouble(planet.getDistance());
                        total = total+dist;
                    }
                    avg = total/size;
                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(2);
                    textViewavg.setText(df.format(avg));

                    getBarEntries();
                    barDataSet = new BarDataSet(barEntriesArrayList, "Astroid Neo App");
                    barData = new BarData(barDataSet);
                    barChart.setData(barData);
                    barDataSet.setValueTextColor(Color.BLACK);
                    barDataSet.setValueTextSize(16f);
                    barChart.getDescription().setEnabled(false);

                }
            });

        }

    }

}