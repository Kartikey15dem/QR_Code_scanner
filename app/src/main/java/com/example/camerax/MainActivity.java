package com.example.camerax;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.core.TorchState;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.ZoomSuggestionOptions;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import LOcation.LocationUtils;
import LOcation.NotificationUtils;
import kotlinx.coroutines.flow.Flow;

public class MainActivity extends AppCompatActivity {
    private PreviewView previewView;
    private Camera camera;
    private float maxSupportedZoomRatio;
    private ZoomSuggestionOptions.ZoomCallback zoomCallback;
    private ProcessCameraProvider cameraProvider;
    private CameraSelector cameraSelector;
    private Preview preview;
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    private TextView resultTextView;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    private final double fixedLatitude = 26.8912133; // Example latitude
    private final double fixedLongitude = 80.9263183; // Example longitude
    private final float radiusInMeters = 1000;
    private boolean isInRadius = false;

    private boolean isActivityStarted = false;
    private CameraControl cameraControl;
    private Button buttonFlashlight;
    private boolean isTorchOn =false;

    private BottomSheetFragment bottomSheetFragment;
    private Button res;
    private Boolean hadReason =false;
    private Preview.SurfaceProvider surfaceProvider;
    private CustomImageView customImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        customImageView = findViewById(R.id.myCustomImageView);

        buttonFlashlight = findViewById(R.id.flash_button);

        buttonFlashlight.setOnClickListener(v -> {
            if (cameraControl != null) {
                isTorchOn = !isTorchOn;
                cameraControl.enableTorch(isTorchOn);
            }
        });
        bottomSheetFragment = new BottomSheetFragment();

        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);




         res = findViewById(R.id.button_reason);
        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });





        if (!LocationUtils.isLocationEnabled(this)) {
            NotificationUtils.showLocationOffNotification(this);
        }

        previewView = findViewById(R.id.preview_view);
        surfaceProvider = previewView.getSurfaceProvider();
        //resultTextView = findViewById(R.id.result_text_view);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    float distance = calculateDistance(location.getLatitude(), location.getLongitude());
                    boolean wasInRadius = isInRadius;
                    isInRadius = distance <= radiusInMeters;

                    Log.d("KUTTA", "Distance: " + distance + ", isInRadius: " + isInRadius);

                    if (wasInRadius != isInRadius) {
                        bindCameraUseCases();
                    }
                }
            }
        };

        Button scanit= findViewById(R.id.button_scan);
        scanit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hadReason) {
                    customImageView.setBackgroundColor(Color.TRANSPARENT);
                    if (allPermissionsGranted()) {
                        startLocationUpdates();
                        startCamera();
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Please enter the reason", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void startCamera() {
        ProcessCameraProvider.getInstance(this).addListener(() -> {
            try {
                cameraProvider = ProcessCameraProvider.getInstance(this).get();
                bindCameraUseCases();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindCameraUseCases() {
        if (cameraProvider == null) {
            return;
        }

        preview = new Preview.Builder().build();
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

        try {
            cameraProvider.unbindAll();
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview);
            preview.setSurfaceProvider(surfaceProvider);

            cameraControl = camera.getCameraControl();

            maxSupportedZoomRatio = Objects.requireNonNull(camera.getCameraInfo().getZoomState().getValue()).getMaxZoomRatio();
            zoomCallback = zoomRatio -> {
                if (camera != null) {
                    camera.getCameraControl().setZoomRatio(zoomRatio);
                }
                return false;
            };

            ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().build();
            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), imageProxy -> {
                @SuppressLint("UnsafeOptInUsageError")
                InputImage image = InputImage.fromMediaImage(Objects.requireNonNull(imageProxy.getImage()), imageProxy.getImageInfo().getRotationDegrees());


                Task<List<Barcode>> result = scanBarcodes(image);
                result.addOnCompleteListener(task -> imageProxy.close());


            });

            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);

            Log.d("BINDdd", "Camera use cases bound. isInRadius: " + isInRadius);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Task<List<Barcode>> scanBarcodes(InputImage image){

        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_AZTEC)
                .build();

        BarcodeScanner scanner = BarcodeScanning.getClient();

        return scanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    // Barcode barcode = barcodes.get(0);
                    for (Barcode barcode : barcodes) {
                        if (isInRadius && LocationUtils.isLocationEnabled(this)  &&  !isActivityStarted ){

                            isActivityStarted = true;

                            Rect bounds = barcode.getBoundingBox();
                            Point[] corners = barcode.getCornerPoints();
                            String rawValue = barcode.getRawValue();
                            int valueType = barcode.getValueType();
//                                switch (valueType) {
//                                    case Barcode.TYPE_WIFI:
//                                        String ssid = Objects.requireNonNull(barcode.getWifi()).getSsid();
//                                        String password = barcode.getWifi().getPassword();
//                                        int type = barcode.getWifi().getEncryptionType();
//                                        break;
//                                    case Barcode.TYPE_URL:
//                                        String title = Objects.requireNonNull(barcode.getUrl()).getTitle();
//                                        String url = barcode.getUrl().getUrl();
//
////                                        resultTextView.setAutoLinkMask(Linkify.WEB_URLS);
////                                        resultTextView.setMovementMethod(LinkMovementMethod.getInstance());
//                                        break;
//                                }
                            //resultTextView.setText(rawValue);

                            String RG = "";
                            assert rawValue != null;
                            if (rawValue.length() >= 5) {
                                RG = rawValue.substring(0, 5);
                            } else {
                                RG = rawValue; // Or handle it differently as needed
                            }
                            if (Objects.equals(RG, "RGIPT")) {
                                String[] separatedStrings = rawValue.split(",");



                                Log.d("seper","String is "+separatedStrings[1]);
                                Log.d("seper","Sterring is "+separatedStrings[2]);

                                Intent intent = new Intent(MainActivity.this, OkDoneActivity.class);
                                intent.putExtra("Gate",separatedStrings[1]);
                                intent.putExtra("Et",separatedStrings[2]);

                                startActivity(intent);

                                finish();
                                break;
                            } else {

                                isActivityStarted = false;
                            }

                        }
                    }

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Failed to scan barcode", Toast.LENGTH_SHORT).show();
                    Log.e("ERRRR", "Error: ", e);
                });

    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startLocationUpdates();
                startCamera();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private float calculateDistance(double currentLatitude, double currentLongitude) {
        Location fixedLocation = new Location("");
        fixedLocation.setLatitude(fixedLatitude);
        fixedLocation.setLongitude(fixedLongitude);

        Location currentLocation = new Location("");
        currentLocation.setLatitude(currentLatitude);
        currentLocation.setLongitude(currentLongitude);

        return fixedLocation.distanceTo(currentLocation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityStarted = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        cameraProvider.unbindAll();
    }

    public void onBottomSheetDismissed() {
       res.setText("Edit reason");
       hadReason = true;

    }

    public void EtEmpty() {

        hadReason=false;
        cameraProvider.unbindAll();
        customImageView.setBackgroundColor(getResources().getColor(R.color.black));

    }
}
