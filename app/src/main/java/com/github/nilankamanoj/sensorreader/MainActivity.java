package com.github.nilankamanoj.sensorreader;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {

    Button btnAction;
    TextView txtX, txtY, txtZ;
    GraphView graph;
    int currentTime = 0;
    LineGraphSeries<DataPoint> seriesX;
    LineGraphSeries<DataPoint> seriesY;
    LineGraphSeries<DataPoint> seriesZ;
    private SensorManager sensorManager;
    private Sensor sensor;
    private boolean started;
    private float last_x, last_y, last_z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Sensor Reader - Accelerometer");
        btnAction = findViewById(R.id.btnAction);
        btnAction.setBackgroundColor(0xFF9e0000);

        txtX = findViewById(R.id.txtX);
        txtY = findViewById(R.id.txtY);
        txtZ = findViewById(R.id.txtZ);

        graph = findViewById(R.id.graph);


        seriesX = new LineGraphSeries<DataPoint>();
        seriesY = new LineGraphSeries<DataPoint>();
        seriesZ = new LineGraphSeries<DataPoint>();

        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);

        seriesX.setColor(0xFFc9a500);
        seriesY.setColor(0xFF009957);
        seriesZ.setColor(0xFF990087);

        btnAction.setOnClickListener(MainActivity.this);
        started = false;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            last_x = sensorEvent.values[0];
            last_y = sensorEvent.values[1];
            last_z = sensorEvent.values[2];

            try {
                txtX.setText("  x: " + last_x);
                txtY.setText("y: " + last_y);
                txtZ.setText("z: " + last_z);
            } catch (NullPointerException ex) {

            } finally {

                seriesX.appendData(new DataPoint(currentTime, last_x), true, 11);
                seriesY.appendData(new DataPoint(currentTime, last_y), true, 11);
                seriesZ.appendData(new DataPoint(currentTime, last_z), true, 11);

                currentTime++;

                graph.addSeries(seriesX);
                graph.addSeries(seriesY);
                graph.addSeries(seriesZ);
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {
        if (!started) {
            btnAction.setBackgroundColor(0xFF009e69);
            btnAction.setText("Stop");
            started = true;
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);


        } else if (started) {
            btnAction.setBackgroundColor(0xFF9e0000);
            btnAction.setText("Start");
            started = false;
            sensorManager.unregisterListener(this);
        }
    }
}
