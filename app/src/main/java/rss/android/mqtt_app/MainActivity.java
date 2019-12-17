package rss.android.mqtt_app;

import android.content.pm.PackageManager;
import android.location.Location;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity  {
    DatabaseSchedule db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseSchedule(this);
        db.getContext(this);
        db.addRow(new Session1("Lab4", "IOT!", "Tuesday", "00:00:00", "22:00:00"));


    }






    public void onClickBtn(View v) {
        //Toast.makeText(this, "Clicked on Button", Toast.LENGTH_LONG).show();
        Date currentTime = Calendar.getInstance().getTime();


        int hh = currentTime.getHours();
        String hhString = null;
        if (hh < 10) {
            hhString = "0" + hh;
        } else
            hhString = hh + "";
        int mm = currentTime.getMinutes();
        String mmString = null;
        if (mm < 10) {
            mmString = "0" + mm;
        } else
            mmString = mm + "";

        int ss = currentTime.getMinutes();
        String ssString = null;
        if (ss < 10) {
            ssString = "0" + ss;
        } else
            ssString = ss + "";


        String finalTime = hhString + ":" + mmString + ":" + ssString;

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);

        if (db.CheckTime(dayOfTheWeek, finalTime)){
            //Get the location
            FusedLocationProviderClient client;
            requestPermission();

            client= LocationServices.getFusedLocationProviderClient(MainActivity.this);

            if (android.support.v4.app.ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return ;
            }
            client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Toast.makeText(MainActivity.this,"Location: "+location.toString(),Toast.LENGTH_SHORT).show();
                        //after getting the location
                        //we publish the location to the server
                        Publish(location);

                    }
                }
            });
        }
        else{
            Toast.makeText(this, "No Course at this time "+ finalTime, Toast.LENGTH_LONG).show();
        }
    }
    public void requestPermission(){
        android.support.v4.app.ActivityCompat.requestPermissions(MainActivity.this,new String[]{ACCESS_FINE_LOCATION},1);
    }


    MqttAndroidClient client;
    Location internLoc;
    public void Publish(Location location)
    {
        Toast.makeText(this, "Publishing", Toast.LENGTH_LONG).show();
        internLoc=location;
        String clientId = MqttClient.generateClientId();
         client =
                new MqttAndroidClient(this, "tcp://broker.hivemq.com:1883",
                        clientId);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(MainActivity.this,"Connected!",Toast.LENGTH_SHORT).show();

                    String topic = "smart-campus/test";

                    double lal=internLoc.getLatitude();
                    double lon=internLoc.getLongitude();
                    String payload = lal+"/"+lon;
                    byte[] encodedPayload;
                    Toast.makeText(MainActivity.this,"Message: "+payload,Toast.LENGTH_SHORT).show();
                    try {
                        encodedPayload = payload.getBytes("UTF-8");
                        MqttMessage message = new MqttMessage(encodedPayload);
                        client.publish(topic, message);
                        Toast.makeText(MainActivity.this,"Message Published Correctly!",Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException  | MqttException e ) {
                        Toast.makeText(MainActivity.this,"Publishing Error!",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(MainActivity.this,"Not Connected!",Toast.LENGTH_SHORT).show();


                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }


}
