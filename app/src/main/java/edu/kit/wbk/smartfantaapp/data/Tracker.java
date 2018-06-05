package edu.kit.wbk.smartfantaapp.data;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import edu.kit.wbk.smartfantaapp.MainActivity;

public class Tracker {
    private MainActivity activity;
    public static boolean orderPlaced = false;
    public static String SHELF = "shelf";

    public Tracker(MainActivity activity) {
        this.activity = activity;
        new NetworkTask(activity).execute();
    }

    public static String json_crazy_stuff(String answer) {
        String[] a = answer.split("\\}\\}");
        String answera = "";
        for (int i = 0; i < a.length; i++) {
            if (a[i].contains("26636")) {
                answera = a[i];
            }
        }
        int index = answera.indexOf("x");
        int indexz = answera.indexOf("z");
        String xstring = answera.substring(index + 3, index + 8);
        String zstring = answera.substring(indexz + 3, indexz + 8);
        int indexy = answera.indexOf("y");
        String ystring = answera.substring(indexy + 3, indexy + 8);
        Log.e("test",xstring);
        //asldkjf
        Log.e("test", zstring);
        double x = 0;
        double y = 0;
        double z = 0;
        x = Double.valueOf(xstring);
        z = Double.valueOf(zstring);
        y = Double.valueOf(ystring);//person

        if (z <= -8 && z > -12) {
            Log.e("test",Tracker.SHELF);

            return (Tracker.SHELF);
            /*if (x <= 10.8 && x > 11.8) {
                Log.e("test", "Regal 1");
                return ("Regal 1");

            } else if (x <= 11.8 && x > 13.1) {
                Log.e("test", "Regal 2");
                return ("Regal 2");
            } else if (x <= 13.1 && x >= 14.0) {
                Log.e("test", "Regal 3");
                return ("Regal 3");
            }*/
        } else if (z <= -4.5 && z >= -6) {
            if (x <= 10) {
                Log.e("test","Presse4");

                return ("Presse 4");
            } else {
                Log.e("test","Presse2");

                return ("Presse 2");
            }
        } else return "somewhere";


        //return "somewhere";
    }

    public static boolean json_crazy_stuff_lager(String answer) {
        String[] a = answer.split("\\}\\}");
        String answerb = "";
        for (int i = 0; i < a.length; i++) {
            if (a[i].contains("26627")) {
                answerb = a[i];
            }
        }
        int index = answerb.indexOf("x");
        int indexz = answerb.indexOf("z");
        String xstring = answerb.substring(index + 3, index + 8);
        String zstring = answerb.substring(indexz + 3, indexz + 8);
        int indexy = answerb.indexOf("y");
        String ystring = answerb.substring(indexy + 3, indexy + 8);
        double x = 0;
        double y = 0;
        double z = 0;
        x = Double.valueOf(xstring);
        z = Double.valueOf(zstring);
        y = Double.valueOf(ystring);
        if (z <= -5 && y > 1.1) {
            Log.e("test", "False");
        } else if (z > -5 && y <= 1.1) {
            Log.e("test", "True");
            return true;
        }
        //Lager

        return false;
    }


    // }


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

    private class NetworkTask extends AsyncTask<Void, Void, String> {

        MainActivity activity;

        public NetworkTask(MainActivity pActivity) {
            this.activity = pActivity;
        }

        @Override
        protected String doInBackground(Void... voids) {

            StringBuilder builder = new StringBuilder();
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpGet request = new HttpGet("http://192.168.43.26:8090/kinexon/data/current/all/position");
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
            try {
                String s = json_crazy_stuff(builder.toString());
                if (json_crazy_stuff_lager(builder.toString())) {
                    if (!(Tracker.orderPlaced)) {
                        Tracker.orderPlaced = true;
                        Log.e("Tracker", "placed Order once");
                        return "place order";

                    }
                } else {
                    Tracker.orderPlaced = false;
                }
                return s;
            } catch (StringIndexOutOfBoundsException e) {
            } catch (ArrayIndexOutOfBoundsException a) {
            }

            return "Somewhere";
        }

        @Override
        protected void onPostExecute(String s) {
            activity.receivedTrackerInfo(s);
            // Log.e("net",s);
        }
    }
}

