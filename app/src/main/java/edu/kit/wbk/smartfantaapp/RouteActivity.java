package edu.kit.wbk.smartfantaapp;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;
import edu.kit.wbk.smartfantaapp.data.Order;
import edu.kit.wbk.smartfantaapp.data.OrderItem;
import edu.kit.wbk.smartfantaapp.data.PickingProduct;

import android.content.Intent;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class RouteActivity extends Activity {

    TextView text;
    Order order;
    Iterator itemIterator;
    boolean inDelivery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        text = (TextView) findViewById(R.id.textView);
        text.setText("Everything picked?");
        text.setTextSize(50.f);

        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra(Order.ORDER);
        List<OrderItem> items = order.getPickedProductsDestination();
        itemIterator = items.iterator();
        inDelivery = false;

    }

    private boolean getNextStation () {
       // OrderItem currentItem;
        if (itemIterator.hasNext()) {
            OrderItem currentItem = (OrderItem) itemIterator.next();
            /*text.setText("Go to " + currentItem.getDestination() +
                    "\nto delviver " + String.valueOf(currentItem.getAmount()
                    + " " + currentItem.getName()));*/
            return true;

        } else if (nextOrderAvailable()) {
            text.setText("Go to " + "storage" + " to pick up new order");
        } else {
            text.setText("No incoming order.");
        }

        return false;

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_ENTER) {

           // text.setText(String.valueOf(keyCode));

            text.setText("Get order to " + order.getStation());

            if (inDelivery) {
                finish();
            } else {
                inDelivery = true;
            }

            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    public boolean deliveredToStation(String stationName) {
        return true;
    }

    private boolean nextOrderAvailable() {


        return false;
    }

}
