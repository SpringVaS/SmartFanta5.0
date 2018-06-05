package edu.kit.wbk.smartfantaapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;
import edu.kit.wbk.smartfantaapp.data.Order;
import android.content.Intent;


public class RouteActivity extends Activity {

    TextView text;
    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        text = (TextView) findViewById(R.id.textView);
        text.setText("Go to Press 4");
        text.setTextSize(50.f);

        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra(Order.ORDER);

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_ENTER) {
           // text.setText(String.valueOf(keyCode));
            finish();
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    public boolean deliveredToStation(String stationName) {
        return true;
    }

}
