package com.example.user.proyectofinalmovil;

import android.app.Activity;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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


public class steps extends ActionBarActivity {

    TextView tv,tv2,tv5,tv6;

    int generador;

    String carrera;
    int activar;

    String prueba;
    RelativeLayout rl;
    RelativeLayout rl2;
    int margin;
    int verificar;
    String procedure_id;

    int actualstep;
    ScrollView scroll;

    String concatenado;

    int habilitar;
    int step_id;
    int pasoactual;
    int borrarlayout;

    int dibujar;
    String value;
    String named;

    EditText et;
    CheckBox cb;
    int finalizar;
    String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        tv=(TextView)findViewById(R.id.textView);
        tv2=(TextView)findViewById(R.id.textView2);
        carrera="biologia";
        activar=0;
        temp="";



        et=(EditText)findViewById(R.id.editText);
        cb=(CheckBox)findViewById(R.id.checkBox);
        cb.setVisibility(View.INVISIBLE);
        et.setVisibility(View.INVISIBLE);

        prueba="";
        rl = (RelativeLayout)findViewById(R.id.layoutid);
        rl2 = (RelativeLayout)findViewById(R.id.rl2);
        margin=14;
        dibujar=1;
        habilitar=0;
        concatenado="f";
        verificar=0;
        step_id=1;
        pasoactual=0;
        value="";
        borrarlayout=1;
        procedure_id = getIntent().getExtras().getString("processid");
        generador=0;


        named = getIntent().getExtras().getString("nombre");
        tv.setText(""+named);


        finalizar=0;


        new HttpAsyncTask().execute("https://dynamicformapi.herokuapp.com/steps.json");
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





            if(verificar==0)

            {

                try {
                    JSONArray pasos = new JSONArray(result);

                    int w = previousid(pasos);

                    for (int a = w; a < pasos.length(); a++) {

                            JSONObject pasoid = pasos.getJSONObject(a);

                            if (pasoid.getString("procedure_id").equals(procedure_id+"")&& pasoid.getString("step_id").equals(""+step_id))
                            {

                                prueba = pasoid.getString("content");
                                JSONObject content = new JSONObject(prueba);
                                JSONArray Fields = content.getJSONArray("Fields");
                                JSONArray Decisions = content.getJSONArray("Decisions");


                                for (int b = 0; b < Fields.length(); b++) {
                                    String caption = Fields.getJSONObject(b).getString("caption");
                                    int field_type = Integer.parseInt(Fields.getJSONObject(b).getString("field_type"));
                                    if (field_type != 3) {
                                        JSONArray possiblevalues = Fields.getJSONObject(b).getJSONArray("possible_values");


                                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.FILL_PARENT);


                                        TextView tvv = new TextView(getApplicationContext());


                                        if(dibujar==1)
                                        {    tvv.setText(caption);

                                            tvv.setTextSize(15);

                                            tvv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                        }




                                        else
                                        {
                                            verificarcolor();

                                        }
                                        tvv.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                                        //tvv.setPadding(0,10,0,-100);
                                        //lp.setMargins(0,margin,0,0);



                                        RadioGroup rgg = new RadioGroup(getApplicationContext());

                                        rgg.setPadding(0,30 , 0, 0);


                                        for (int m = 0; m < possiblevalues.length(); m++) {
                                            RadioButton rb = new RadioButton(getApplicationContext());
                                            rb.setText(possiblevalues.get(m) + "");
                                            rb.setTextColor(getResources().getColor(android.R.color.black));

                                            if(dibujar==1){
                                                rgg.addView(rb);

                                            }




                                        }


                                        rl2.addView(tvv, lp);


                                        rl2.addView(rgg, lp);
                                        lp.setMargins(0, margin, 0, 0);




                                        margin = 180;



                                    } else {
                                        if (verificar == 0) {
                                            if (b < pasoactual)
                                                tv2.setText(caption);//en caso tal que sea 3 el fieldtype

                                        }


                                    }



                                }




                                for (int b = 0; b < Decisions.length(); b++) {
                                    JSONObject num = Decisions.getJSONObject(b);

                                    if(num.getJSONArray("branch").length()>0)
                                    {
                                        JSONObject branch = num.getJSONArray("branch").getJSONObject(0);
                                        String proof = branch.getString("value")+"";

                                        if(num.getJSONArray("branch").length()>1)
                                        {
                                            JSONObject branch2 = num.getJSONArray("branch").getJSONObject(1);
                                            value = "" + branch.getString("value") + branch2.getString("value");


                                        }

                                        if (value.equals(concatenado)||proof.equals(concatenado)) {
                                            String gotostep = num.getString("go_to_step");

                                            step_id = Integer.parseInt(gotostep);
                                            String mmm = "";
                                            dibujar=1;
                                            a=w;

                                        }

                                    }

                                    if(num.getJSONArray("branch").length()==0)
                                    {


                                        if(pasoactual== Fields.length())
                                        {        String gotostep = num.getString("go_to_step");
                                            step_id = Integer.parseInt(gotostep);
                                            a=w;
                                            String mmm = "";
                                            dibujar=1;
                                            borrarlayout=0;
                                            pasoactual=0;

                                        }

                                    }





                                }


                            }





                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }




            else//para obtener el valor de los radiobuttons seleccionados
            {
                concatenado=childs(rl2);
                if(concatenado.equals("Ingenierias"))
                    generador=-1;
                activar=1;
               // Toast.makeText(getApplicationContext(), concatenado, Toast.LENGTH_SHORT).show();
                if(!concatenado.equals(""))
                {
                     temp = concatenado;
                    //generar(temp);

                }


                verificar=0;

                new HttpAsyncTask().execute("https://dynamicformapi.herokuapp.com/steps.json");
                int aa=rl2.getChildCount();
                for(int m=0;m<aa;m++)
                {
                    if(rl2.getChildAt(m)instanceof RadioButton)
                    {
                        rl2.removeView(rl2.getChildAt(m));

                    }
                }
                for(int m=0;m<aa;m++)
                {
                    if(rl2.getChildAt(m)instanceof RadioGroup)
                    {
                        rl2.removeView(rl2.getChildAt(m));

                    }
                }

            }


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_steps, menu);
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

    public void pasar(View v)
    {



        int hijossrgroup = hijos(rl2);



        if(hijossrgroup==1)//si hay radiogroups en el layout
        {
            verificar=1;//para obtener el valor de los radiobuttons seleccionados
            /**/

        }
        else
        {
            verificar=0;//para ejecutar la consulta al servidor
            concatenado="f";


        }
        if(activar==1)
        {
            generador++;
            //generar(temp);
        }


        pasoactual++;

        new HttpAsyncTask().execute("https://dynamicformapi.herokuapp.com/steps.json");
        margin=14;



        dibujar=0;




    }




    String childs(RelativeLayout rlp)

    {
        int count = rlp.getChildCount();
        String pruebad="";
        for(int i=0;i<count;i++)
        {

            if(rlp.getChildAt(i)instanceof RadioGroup)
            {
                RadioGroup rp = (RadioGroup)rlp.getChildAt(i);
                int itemslength = rp.getChildCount();
                for(int b=0;b<itemslength;b++) {
                    RadioButton rbm = (RadioButton) rp.getChildAt(b);
                    if (rbm.isChecked())
                    {
                        pruebad=pruebad+""+rbm.getText();
                    }
                }
            }
        }
        return pruebad;
    }

    public void concatenar(View v)
    {
        String jejejejeje=""+childs(rl2);
        Toast.makeText(getApplicationContext(),jejejejeje,Toast.LENGTH_SHORT).show();
    }

    int hijos(RelativeLayout rlm)
    {
        int count = rlm.getChildCount();
        int total=0;

        for(int i=0;i<count;i++)
        {

            if(rlm.getChildAt(i)instanceof RadioGroup)
            {

                total =1;
                //rlm.removeView(rlm.getChildAt(i));
//                margin=18;

            }

        }

        return total;

    }


    public void verificarcolor()
    {
        int ad = rl2.getChildCount();
        int conta=0;
        int textview=0;
        for(int db=0;db<ad;db++)
        {
            if(rl2.getChildAt(db)instanceof TextView)
            {
                textview++;
                if(((TextView) rl2.getChildAt(db)).getCurrentTextColor()==getResources().getColor(android.R.color.holo_red_dark))
                {
                    rl2.getChildAt(db).setVisibility(View.INVISIBLE);
                    conta++;
                    //Toast.makeText(getApplicationContext(),conta+"",Toast.LENGTH_LONG).show();
                }
            }

        }


    }

    int previousid(JSONArray array)
    {
        int a=0;
        for(int i=0;i<array.length();i++)
        {
            try {
                JSONObject jsonObject = array.getJSONObject(i);
                if(jsonObject.getString("step_id").equals("1")&&jsonObject.getString("procedure_id").equals(procedure_id))
                {
                    a = i;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return a;//retorna la posicion donde step_id es igual a 1 y procedure_id es igual al solicitado previamente x el usuario
    }}










