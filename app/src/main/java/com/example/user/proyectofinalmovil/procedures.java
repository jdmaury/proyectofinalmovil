package com.example.user.proyectofinalmovil;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class procedures extends ActionBarActivity {

    String grupoid;
    ListView lv;
    ArrayList procesos;
    SimpleAdapter sap;
    String itemname;
    String description;


    List<Map<String, String>> data;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procedures);

        data =  new ArrayList<Map<String, String>>();
        itemname="";
        description="";
        grupoid = getIntent().getExtras().getString("grupoid");
        lv = (ListView)findViewById(R.id.listView);









       // Toast.makeText(this,grupoid+"",Toast.LENGTH_SHORT).show();

        new HttpAsyncTask().execute("https://dynamicformapi.herokuapp.com/procedures.json");
    }


    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {


            HttpClient httpclient = new DefaultHttpClient();


            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));


            inputStream = httpResponse.getEntity().getContent();


            if(inputStream != null)
                result = convertInputStreamToString(inputStream);


        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {


            try {
                final JSONArray array = new JSONArray(result);

                for(int i=0;i<array.length();i++)
                {
                    JSONObject proceso = array.getJSONObject(i);
                    if(proceso.getString("group_id").equals(grupoid+""))
                    {
                        HashMap<String,String> itemslista = new HashMap<>(2);;

                        itemname = proceso.getString("name")+"";
                        description = proceso.getString("description")+"";
                        itemslista.put("title",itemname);
                        itemslista.put("desc",description);

                        data.add(itemslista);


                    }

                    String jejej="";







                }
                sap = new SimpleAdapter(getApplicationContext(), data,
                        R.layout.milistview,
                        new String[] {"title", "desc"},
                        new int[] {android.R.id.text1,
                                android.R.id.text2});



                    lv.setAdapter(sap);




                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String prueba = lv.getItemAtPosition(position).toString();

                        for(int i=0;i<array.length();i++)
                        {
                            try {
                                JSONObject obj = array.getJSONObject(i);
                                if(prueba.contains(obj.getString("name")))
                                {
                                    String processid = ""+obj.getString("procedure_id");
                                    String name = ""+obj.getString("name");
                                   // Toast.makeText(getApplicationContext(),processid+"",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),steps.class);
                                    intent.putExtra("processid",processid);
                                    intent.putExtra("nombre",name);
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }






                    }
                });




            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_procedures, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }






}
