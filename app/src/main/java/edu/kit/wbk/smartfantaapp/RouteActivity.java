package edu.kit.wbk.smartfantaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

public class RouteActivity extends AppCompatActivity {

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        text = (TextView) findViewById(R.id.textView);
        text.setText("Go to Press 4");
        text.setTextSize(50.f);
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

}
