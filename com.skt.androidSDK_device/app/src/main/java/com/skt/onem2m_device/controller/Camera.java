package com.skt.onem2m_device.controller;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Camera controller
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public class Camera {

    private static final String TAG = "CAMERA";

    /**
     * type list
     */
    public enum TYPE {
        NONE,
        FRONT,
        BACK
    }

    private Context context;            // context
    private CapturedListener captureListener;    // capture listener

    /**
     * capture result listener
     */
    public interface CapturedListener {
        /**
         * called when complete image capture
         *
         * @param image captured image
         */
        void onCaptured(byte[] image);

        /**
         * called when failed image capture
         */
        void onCaptureFailed();
    }

    /**
     * constructor
     *
     * @param context context
     */
    public Camera(Context context, CapturedListener captureListener) {
        this.context = context;
        this.captureListener = captureListener;
    }

    /**
     * camera command notification
     *
     * @param type camera type
     * @param view preview layout
     * @return running result
     */
    public boolean notifyCommand(TYPE type, final FrameLayout view) {
        if (type == TYPE.NONE) {
            return false;
        }

        boolean ret;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ret = openCamera2(type);
        } else {
            ret = takePictureFromCamera(type, view);
        }

        return true;
    }

    /**
     * take a picture with the Camera API
     *
     * @param view view for camera preview
     * @return calling result
     */
    private boolean takePictureFromCamera(final TYPE type, final FrameLayout view) {
        final SurfaceView preview = new SurfaceView(view.getContext());
        SurfaceHolder holder = preview.getHolder();
        // deprecated setting, but required on Android versions prior to 3.0
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        holder.addCallback(new SurfaceHolder.Callback() {
            android.hardware.Camera camera = null;

            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                Log.e(TAG, "surfaceCreated");
                int cameraType = android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;
                if (type == TYPE.FRONT) {
                    cameraType = android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT;
                }

                // Search for the front facing camera
                int cameraId = -1;
                int numberOfCameras = android.hardware.Camera.getNumberOfCameras();
                for (int loop1 = 0; loop1 < numberOfCameras; loop1++) {
                    android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
                    android.hardware.Camera.getCameraInfo(loop1, info);
                    if (info.facing == cameraType) {
                        cameraId = loop1;
                        break;
                    }
                }
                if (cameraId == -1) {
                    captureListener.onCaptureFailed();
                    return;
                }

                // Orientation
                android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
                android.hardware.Camera.getCameraInfo(cameraId, info);
                WindowManager wm = (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
                int degrees = 0;
                switch (wm.getDefaultDisplay().getRotation()) {
                    case Surface.ROTATION_0:
                        degrees = 0;
                        break;
                    case Surface.ROTATION_90:
                        degrees = 90;
                        break;
                    case Surface.ROTATION_180:
                        degrees = 180;
                        break;
                    case Surface.ROTATION_270:
                        degrees = 270;
                        break;
                }
                int rotate;
                rotate = (info.orientation - degrees + 360) % 360;
                try {
                    camera = android.hardware.Camera.open(cameraId);
                    camera.setPreviewDisplay(surfaceHolder);
                    android.hardware.Camera.Parameters params = camera.getParameters();
                    params.setPictureSize(640, 480);
                    params.setPictureFormat(ImageFormat.JPEG);
                    params.setRotation(rotate);
                    camera.setParameters(params);
                    camera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                    if (camera != null) {
                        camera.release();
                    }
                    captureListener.onCaptureFailed();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                Log.e(TAG, "surfaceChanged");

                camera.takePicture(null, null, new android.hardware.Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes, android.hardware.Camera camera) {
                        Log.e(TAG, "onPictureTaken");
                        captureListener.onCaptured(bytes);
                        camera.release();
                        view.removeView(preview);
                    }
                });

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                Log.e(TAG, "surfaceDestroyed");
            }
        });
        view.addView(preview);
        return true;
    }

    /**
     * open camera with the Camera2 API
     *
     * @return calling result
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean openCamera2(final TYPE type) {
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }

            int cameraType = CameraCharacteristics.LENS_FACING_BACK;
            if (type == TYPE.FRONT) {
                cameraType = CameraCharacteristics.LENS_FACING_FRONT;
            }

            String id = "";
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                int cOrientation = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (cOrientation == cameraType) {
                    id = cameraId;
                }
            }
            if (id.isEmpty()) {
                return false;
            }

            manager.openCamera(id, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(CameraDevice cameraDevice) {
                    takePictureFromCamera2(cameraDevice);
                }

                @Override
                public void onDisconnected(CameraDevice cameraDevice) {
                }

                @Override
                public void onError(CameraDevice cameraDevice, int i) {
                    captureListener.onCaptureFailed();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * take a picture with the Camera2 API
     *
     * @param camera Camera2
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void takePictureFromCamera2(final CameraDevice camera) {
        final ImageReader reader = ImageReader.newInstance(640, 480, ImageFormat.JPEG, 1);
        reader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader imageReader) {
                Image img = imageReader.acquireLatestImage();
                if (img != null) {
                    ByteBuffer buffer = img.getPlanes()[0].getBuffer();
                    byte[] bytes = new byte[buffer.capacity()];
                    buffer.get(bytes);

                    captureListener.onCaptured(bytes);

//                    processImage(img);
                    img.close();
//                    camera.close();
                }
            }
        }, null);

        try {
            camera.createCaptureSession(Arrays.asList(reader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    // Orientation
                    final SparseIntArray ORIENTATIONS = new SparseIntArray();
                    ORIENTATIONS.append(Surface.ROTATION_0, 90);
                    ORIENTATIONS.append(Surface.ROTATION_90, 0);
                    ORIENTATIONS.append(Surface.ROTATION_180, 270);
                    ORIENTATIONS.append(Surface.ROTATION_270, 180);
                    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                    int rotation = wm.getDefaultDisplay().getRotation();

                    try {
                        CaptureRequest.Builder captureBuilder = cameraCaptureSession.getDevice().createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                        captureBuilder.addTarget(reader.getSurface());
                        captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                        captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
                        cameraCaptureSession.capture(captureBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                            @Override
                            public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                                super.onCaptureCompleted(session, request, result);
                                session.getDevice().close();
                            }

                            @Override
                            public void onCaptureFailed(CameraCaptureSession session, CaptureRequest request, CaptureFailure failure) {
                                super.onCaptureFailed(session, request, failure);
                                session.getDevice().close();
                                captureListener.onCaptureFailed();
                            }
                        }, null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                        captureListener.onCaptureFailed();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                    captureListener.onCaptureFailed();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            captureListener.onCaptureFailed();
        }
    }

    /**
     * Process image data as desired.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void processImage(Image image) {
        try {
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.capacity()];
            buffer.get(bytes);
            OutputStream output = null;
            try {
                final File file = new File(Environment.getExternalStorageDirectory() + "/pic.jpg");
                output = new FileOutputStream(file);
                output.write(bytes);
            } finally {
                if (null != output) {
                    output.close();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
