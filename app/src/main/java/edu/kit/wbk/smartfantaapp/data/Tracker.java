package edu.kit.wbk.smartfantaapp.data;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;

public class Tracker {

    public Tracker() {
        new NetworkTask().execute();
    }

    public static String json_crazy_stuff(String answer) {
        int index  = answer.indexOf("x");
        int indexz  = answer.indexOf("z");
        String xstring = answer.substring(index +2, index +8);
        String zstring = answer.substring(indexz +2, indexz +8);

        double x = 0;
        double z = 0;
        x = Double.valueOf(xstring);
        z = Double.valueOf(zstring);
        /*try { x = Double.valueOf(xs);
        z = Double.valueOf(zs);
            JSONArray arr = answer.getJSONArray("position");
            String xs = arr.getJSONObject(0).getString("x");
            String zs = arr.getJSONObject(2).getString("z");
            x = Double.valueOf(xs);
            z = Double.valueOf(zs);
        } catch (JSONException j) {
            System.out.println(j.toString());
        }
*/
        if (z <= -9 && z > -10) {
            if (x <= 10.8 && x > 11.8) {
                return ("Regal 1");
            } else if (x <= 11.8 && x > 13.1) {
                return ("Regal 2");
            } else if (x <= 13.1 && x >= 14.0) {
                return ("Regal 3");
            }
        } else if (z <= -5 && x >= -6) {
            if (x <= 10) {
                return (" Presse 4");
            } else {
                return ("Presse 2");
            }
        } else return "somewhere";

        return "somewhere";
    }


    private static class NetworkTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {

            StringBuilder builder = new StringBuilder();
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpGet request = new HttpGet("http://192.168.43.26:8090/kinexon/data/current/all/");
                HttpResponse response = client.execute(request);
                BufferedReader bufReader = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent()));

                String line;

                 while ((line = bufReader.readLine()) != null) {
                    builder.append(line);
                    builder.append(System.lineSeparator());
                }

                Log.e("Tracker", builder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String s = json_crazy_stuff(builder.toString());
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("net",s);
        }
    }
}

