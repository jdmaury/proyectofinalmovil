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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
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


public class MainActivity extends ActionBarActivity {

    ListView lv;
    Spinner sp;
    ArrayList categorias;
    ArrayAdapter<String> adapter;
    int group_id;
    JSONArray array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        categorias = new ArrayList<String>();
        group_id=0;
        sp = (Spinner)findViewById(R.id.spinner);
        adapter= new ArrayAdapter<String>(this,R.layout.spinner_item,categorias);


        new HttpAsyncTask().execute("https://dynamicformapi.herokuapp.com/groups.json");

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
                array = new JSONArray(result);

                for(int i=0;i<array.length();i++)
                {
                    JSONObject categoria = array.getJSONObject(i);

                    String name =""+categoria.getString("name");
                    categorias.add(name);

                }
                sp.setAdapter(adapter);





            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    int next(Spinner sd, JSONArray jarray)
    {
       String name = sd.getSelectedItem().toString();
       int id=0;
        for(int i=0;i<jarray.length();i++)
        {
            try {
                JSONObject m = jarray.getJSONObject(i);
                if(m.getString("name").equals(name))
                {
                    id = m.getInt("group_id");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return id;
    }




    public void siguiente(View v)
    {
        int b = next(sp,array);
        String g_id = b+"";
        //Toast.makeText(this,b+"",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,procedures.class);
        intent.putExtra("grupoid",g_id);
        startActivity(intent);

    }
}
