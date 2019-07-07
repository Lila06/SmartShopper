package com.example.smartshopper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.smartshopper.data.LocalProductRepository;
import com.example.smartshopper.data.ScannedProductRepository;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanActivity extends AppCompatActivity implements SearchRequest.ResultCallback {

    private static final String TAG = "ScanActivity";

    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private TextView textView;
    private BarcodeDetector detector;
    private ScannedProductRepository scannedProductRepository;
    private LocalProductRepository localProductRepository = new LocalProductRepository();
    private boolean isSearchRequestRunning = false;
    private long eanOfSearchRequest = 0L;
    private SearchRequest searchRequest = new SearchRequest(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        scannedProductRepository = new ScannedProductRepository(this);

        surfaceView = findViewById(R.id.cameraPreview);
        textView = findViewById(R.id.textView);

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    initDetectorAndCamera();
                    if (ContextCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Start camera source failed: ", e);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                // do nothing
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
                cameraSource.release();
                detector.release();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        resetSearch();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initDetectorAndCamera() {
        detector = new BarcodeDetector.Builder(ScanActivity.this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        if (!detector.isOperational()) {
            textView.setText(R.string.barcode_scanner_error);
        }

        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Log.i(TAG, "To prevent memory leaks barcode scanner has been stopped");
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

        cameraSource = new CameraSource.Builder(this, detector)
                .setRequestedPreviewSize(1024, 768)
                .setAutoFocusEnabled(true)
                .build();
    }

    @Override
    public void onResponse(String response) {
        if (isSearchRequestRunning) {
            scannedProductRepository.saveProduct(eanOfSearchRequest, response);
            Intent intent = new Intent(ScanActivity.this, ScanDetailsActivity.class);
            intent.putExtra(ScanDetailsActivity.EXTRA_EAN, eanOfSearchRequest);
            startActivity(intent);
        }
    }

    @Override
    public void onResponseError(SearchRequest.ErrorCode code, String response) {
        Log.e(TAG, "Search request failed - error code: " + code + " >> " + response);

        if (code == SearchRequest.ErrorCode.CODE_5) {
            Toast.makeText(this, code.description + " - Nutzung der lokalen Datenbank", Toast.LENGTH_LONG).show();
            String localResponse = localProductRepository.findProduct(eanOfSearchRequest);
            if (localResponse != null) {
                this.onResponse(localResponse);
            } else {
                Toast toast = Toast.makeText(this, "EAN nicht in der lokalen Datenbank gefunden", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                resetSearch();
            }
        } else {
            Toast.makeText(this, "Suche fehlgeschlagen: " + code.description, Toast.LENGTH_LONG).show();
            resetSearch();
        }
    }

    private void resetSearch() {
        isSearchRequestRunning = false;
        textView.setText(R.string.barcode_scanner);
    }
}
