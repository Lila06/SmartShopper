package com.example.smartshopper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smartshopper.data.ScannedProductRepository;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.smartshopper.MESSAGE";


    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final int CAMERA_REQUEST = 101;
    private TextView textView;
    private BarcodeDetector detector;

    private ScannedProductRepository scannedProductRepository;
    private boolean isSearchRequestRunning = false;
    private long eanOfSearchRequest = 0L;
    private SearchRequest searchRequest = new SearchRequest(new SearchRequest.ResultCallback() {
        @Override
        public void onResponse(String response) {
            if(isSearchRequestRunning) {
                scannedProductRepository.saveProduct(eanOfSearchRequest, response);
                Intent intent = new Intent(ScanActivity.this, ScanDetailsActivity.class);
                intent.putExtra(ScanDetailsActivity.EXTRA_EAN, eanOfSearchRequest);
                startActivity(intent);
            }

        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        scannedProductRepository = new ScannedProductRepository(this);

        // View inflated = getLayoutInflater().inflate(R.layout.support_simple_spinner_dropdown_item,null);
        // inflated.findViewById(R.id...)

        surfaceView = findViewById(R.id.cameraPreview);
        textView = findViewById(R.id.textView);

        detector = new BarcodeDetector.Builder(ScanActivity.this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        if (!detector.isOperational()) {
            textView.setText("Could not set up the detector!");
            return;
        }

        cameraSource = new CameraSource.Builder(this, detector)
                .setRequestedPreviewSize(1024, 768)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                try {
                    if (ContextCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScanActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                        cameraSource.start(surfaceView.getHolder());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                detector.release();
                cameraSource.stop();
                cameraSource.release();
            }
        });

        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(final Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();

                if (qrCodes.size() > 0) {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            String qrCode = qrCodes.valueAt(0).displayValue;
                            textView.setText(qrCode);

                            if (!isSearchRequestRunning) {
                                eanOfSearchRequest = Long.valueOf(qrCode);
                                searchRequest.search(qrCode);
                                isSearchRequestRunning = true;
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        isSearchRequestRunning = false;
    }
}
