package com.assist.tutorial.damian.gravitytest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager sensorManager;
    private Sensor gravity,linearAcc,rotationVector,magneticField;
    private TextView zAxis,yAxis,xAxis,
            zAcc,yAcc,xAcc,
            azimuth,pitch,roll,
            worldAcc1,worldAcc2,worldAcc3,
            projection1,projection2,projection3;
    private float[] deviceAcceleration,magneticFieldMatrix,
            gravityMatrix,tempWorldAcc,
            rMatrix,orientation,temp;
    private static final float standardGravity = SensorManager.STANDARD_GRAVITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorManager.registerListener(this,gravity,SensorManager.SENSOR_DELAY_NORMAL);
        linearAcc = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(this,linearAcc,SensorManager.SENSOR_DELAY_NORMAL);
        rotationVector = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorManager.registerListener(this,rotationVector,SensorManager.SENSOR_DELAY_NORMAL);
        magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this,magneticField,SensorManager.SENSOR_DELAY_NORMAL);

        zAxis = (TextView) findViewById(R.id.z_Axis);
        yAxis = (TextView) findViewById(R.id.y_Axis);
        xAxis = (TextView) findViewById(R.id.x_Axis);

        zAcc = (TextView) findViewById(R.id.z_ACC);
        yAcc = (TextView) findViewById(R.id.y_ACC);
        xAcc = (TextView) findViewById(R.id.x_ACC);

        azimuth = (TextView) findViewById(R.id.azimuth);
        pitch = (TextView) findViewById(R.id.pitch);
        roll = (TextView) findViewById(R.id.roll);

        worldAcc1 = (TextView) findViewById(R.id.worldAcc1);
        worldAcc2 = (TextView) findViewById(R.id.worldAcc2);
        worldAcc3 = (TextView) findViewById(R.id.worldAcc3);

        projection1 = (TextView) findViewById(R.id.projection1);
        projection2 = (TextView) findViewById(R.id.projection2);
        projection3 = (TextView) findViewById(R.id.projection3);


        rMatrix = new float[9];
        orientation = new float[3];
        temp = new float[9];

        deviceAcceleration = new float[4];
        magneticFieldMatrix = new float[3];
        gravityMatrix = new float[3];
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()){
            case Sensor.TYPE_GRAVITY:
                System.arraycopy(event.values,0,gravityMatrix,0,event.values.length);
                String tempZ ="Z-Axis = "+ String.format(Locale.getDefault(),"%.02f",event.values[2]);
                String tempY = "Y-Axis = "+ String.format(Locale.getDefault(),"%.02f",event.values[1]);
                String tempX = "X-Axis = " + String.format(Locale.getDefault(),"%.02f",event.values[0]);
                zAxis.setText(tempZ);
                yAxis.setText(tempY);
                xAxis.setText(tempX);

                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                String tempZAcc = "Z-Acc = " + String.format(Locale.getDefault(),"%.02f",event.values[2]) ;
                String tempYAcc = "Y-Acc = " + String.format(Locale.getDefault(),"%.02f",event.values[1]) ;
                String tempXAcc = "X-Acc = " + String.format(Locale.getDefault(),"%.02f",event.values[0]) ;
                zAcc.setText(tempZAcc);
                yAcc.setText(tempYAcc);
                xAcc.setText(tempXAcc);
                if(deviceAcceleration != null && magneticFieldMatrix != null && gravityMatrix != null){
                    float[] rotation = new float[16];
                    float[] invRotation = new float[16];
                    float[] inclination = new float[16];
                    // Magnitude of the projection of A onto a unit vector in direction of G
                    // Other words : how much of A is in the direction of G
//                    float[] gUnit = new float[3];
//                    float temp = magneticFieldMatrix[0]*magneticFieldMatrix[0]+
//                            magneticFieldMatrix[1]*magneticFieldMatrix[1]+
//                            magneticFieldMatrix[2]*magneticFieldMatrix[2];
//                    double temp1 =event.values[0]*magneticFieldMatrix[0]/(Math.sqrt(temp));
//                    gUnit[0] = (float)temp1;
//                    double temp2 = event.values[1]*magneticFieldMatrix[1]/(Math.sqrt(temp));
//                    gUnit[1] = (float)temp2;
//                    double temp3 = event.values[2]*magneticFieldMatrix[2]/(Math.sqrt(temp));
//                    gUnit[2] = (float)temp3;
//                    String tempProjection1 = "PROJECTION_1 ="+String.format(Locale.getDefault(),"%.02f",gUnit[0]);
//                    String tempProjection2 = "PROJECTION_2 ="+String.format(Locale.getDefault(),"%.02f",gUnit[1]);
//                    String tempProjection3 = "PROJECTION_3 ="+String.format(Locale.getDefault(),"%.02f",gUnit[2]);
//                    projection1.setText(tempProjection1);
//                    projection2.setText(tempProjection2);
//                    projection3.setText(tempProjection3);
                    /////////////////
                    tempWorldAcc = new float[4];
                    SensorManager.getRotationMatrix(rotation,inclination,gravityMatrix,magneticFieldMatrix);
//                    Matrix.transposeM(invRotation,0,rotation,0);
//                    Matrix.multiplyMV(tempWorldAcc,0,invRotation,0,deviceAcceleration,0);
                    Matrix.invertM(invRotation,0,rotation,0);
                    Matrix.multiplyMV(tempWorldAcc,0,invRotation,0,deviceAcceleration,0);
                    //worldAcc = new List(tempWorldAcc);
                    String tempText = "WORLD_ACC_1 = "+String.format(Locale.getDefault(),"%.02f",tempWorldAcc[0]);
                    worldAcc1.setText(tempText);
                    tempText = "WORLD_ACC_2 = "+String.format(Locale.getDefault(),"%.02f",tempWorldAcc[1]);
                    worldAcc2.setText(tempText);
                    tempText = "WORLD_ACC_3 = "+String.format(Locale.getDefault(),"%.02f",tempWorldAcc[2]);
                    worldAcc3.setText(tempText);
                }
                for(int i=0; i<3;i++){
                    deviceAcceleration[i]=event.values[i];
                }
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                SensorManager.getRotationMatrixFromVector(rMatrix,event.values);
                //SensorManager.remapCoordinateSystem(rMatrix,SensorManager.AXIS_X,SensorManager.AXIS_Z,temp);
                //SensorManager.getOrientation(temp,orientation);
                SensorManager.getOrientation(rMatrix,orientation);
                String azimuthTemp = "AZIMUTH = "+ String.format(Locale.getDefault(),"%.02f",Math.toDegrees(orientation[0]));
                String pitchTemp = "PITCH = "+ String.format(Locale.getDefault(),"%.02f",Math.toDegrees(orientation[1]));
                String rollTemp = "ROLL = "+ String.format(Locale.getDefault(),"%.02f",Math.toDegrees(orientation[2]));
                azimuth.setText(azimuthTemp);
                pitch.setText(pitchTemp);
                roll.setText(rollTemp);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                System.arraycopy(event.values,0,magneticFieldMatrix,0,event.values.length);
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this,gravity);
        sensorManager.unregisterListener(this,linearAcc);
        sensorManager.unregisterListener(this,rotationVector);
        sensorManager.unregisterListener(this,magneticField);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,gravity,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,linearAcc,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,rotationVector,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,magneticField,SensorManager.SENSOR_DELAY_NORMAL);
    }
}
