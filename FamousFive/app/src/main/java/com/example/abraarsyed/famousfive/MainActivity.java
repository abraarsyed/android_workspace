package com.example.abraarsyed.famousfive;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;


public class MainActivity extends Activity implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    Button btn1;
    ImageView imageView;
    private static final String TAG2 = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    Button btn2;
    TextView tv;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    Button btnSendSMS;
    Button btnfl;

    private boolean isLighOn = false;

    public Camera camera;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG2, "onCreate ...............................");
        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        setContentView(R.layout.activity_main);
        btn1 = (Button) findViewById(R.id.btn1);
        imageView = (ImageView) findViewById(R.id.imageView);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int REQUEST_IMAGE_CAPTURE = 1;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        tv = (TextView) findViewById(R.id.tv);
        tv.setText("At Time: "  + "\n" +
                "Latitude: "  + "\n" +
                "Longitude: "  + "\n" +
                "Accuracy: "  + "\n" +
                "Provider: ");
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (null != mCurrentLocation) {
                    String lat = String.valueOf(mCurrentLocation.getLatitude());
                    String lng = String.valueOf(mCurrentLocation.getLongitude());
                    mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                    tv.setText("At Time: " + mLastUpdateTime + "\n" +
                            "Latitude: " + lat + "\n" +
                            "Longitude: " + lng + "\n" +
                            "Accuracy: " + mCurrentLocation.getAccuracy() + "\n" +
                            "Provider: " + mCurrentLocation.getProvider());
                    updateUI();
                } else {
                    Log.d(TAG2, "location is null ...............");
                }
            }
        });

        btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMSMessage();

                Notify("You've received new message","Specified Location");

            }
        });

        btnfl = (Button) findViewById(R.id.btnfl);
        Context context = this;
        PackageManager pm = context.getPackageManager();
        // if device support camera?
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Log.e("err", "Device has no camera!");
            return;
        }


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bp = (Bitmap) data.getExtras().get("data");
        imageView.setImageBitmap(bp);
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG2, "onStart fired ..............");
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG2, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG2, "isConnected ...............: " + mGoogleApiClient.isConnected());
        //if (camera != null) {
        //    camera.release();
        //}
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG2, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG2, "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG2, "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG2, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    private void updateUI() {
        Log.d(TAG2, "UI update initiated .............");

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
        if (camera != null) {
            camera.release();
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG2, "Location update stopped .......................");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG2, "Location update resumed .....................");
        }

        camera = Camera.open();
        final Camera.Parameters p = camera.getParameters();
        btnfl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isLighOn) {

                    Log.i("info", "torch is turn off!");

                    p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(p);
                    camera.stopPreview();
                    isLighOn = false;

                } else {

                    Log.i("info", "torch is turn on!");

                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

                    camera.setParameters(p);
                    camera.startPreview();
                    isLighOn = true;

                }

            }
        });

    }


    protected void sendSMSMessage() {
        Log.i("Send SMS", "");
        String phoneNo = "8105256913";
        String message = tv.getText().toString();

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
        }

        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void Notify(String notificationTitle, String notificationMessage){

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
        notification.setSmallIcon(R.drawable.user_profile);
        notification.setContentTitle(notificationTitle);
        notification.setContentText(notificationMessage);
        notification.setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String SMS_MIME_TYPE = "vnd.android-dir/mms-sms";
        Intent resultIntent = new Intent(Intent.ACTION_MAIN);
        resultIntent.setType(SMS_MIME_TYPE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this , 0 , resultIntent,0);
        notification.setContentIntent(pendingIntent);
        notificationManager.notify(9999, notification.build());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
