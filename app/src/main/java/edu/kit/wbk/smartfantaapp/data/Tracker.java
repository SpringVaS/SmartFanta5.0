package edu.kit.wbk.smartfantaapp.data;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;

public class Tracker {
    public static String json_crazy_stuff(String answer) {

        double x = 0;
        double z = 0;
        /*try {
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
        } else return null;

    return null;
    }
    public static void main() {
        StringBuilder builder = new StringBuilder();
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet("http://129.13.10.241:8090/kinexon/data/current/all/");
            HttpResponse response = client.execute(request);

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            String line;

            while ((line = bufReader.readLine()) != null) {
                builder.append(line);
                builder.append(System.lineSeparator());
            }

            Log.d("Tracker", builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        json_crazy_stuff(builder.toString());
    }
}
