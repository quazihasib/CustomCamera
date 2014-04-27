package com.example.customcamera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends Activity 
{

	private Camera mCamera;
	private CameraPreview mPreview;
	public static Bitmap imageBitmap;
	// a bitmap to display the captured image
	private Bitmap bitmap;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mCamera = getCameraInstance();

		mPreview = new CameraPreview(this, mCamera);
		FrameLayout preview = (FrameLayout) findViewById(R.id.fl);
		preview.addView(mPreview);
		
		ImageView im = (ImageView) findViewById(R.id.imageView1);
		im.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				takePicture();
			}
		});
		
	}

	@SuppressLint("NewApi")
	public static Camera getCameraInstance()
	{
		Camera c = null;
		try
		{
			c = Camera.open(1);
		}
		catch (Exception e) 
		{
			
		}
		return c;
	}
	
	public void takePicture()
	{
		Camera.PictureCallback mCall = new Camera.PictureCallback()
		{

			public void onPictureTaken(byte[] data, Camera camera)
			{
				// decode the data obtained by the camera into a Bitmap
				bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

				Matrix matrix = new Matrix();

				//matrix.postRotate(180);
//				 float[] mirrorY = { 1, 0, 0, 0, 1, 0, 0, 0, -1};
//		         Matrix matrixMirrorY = new Matrix();
//		         matrixMirrorY.setValues(mirrorY);
//
//		        matrix.postConcat(matrixMirrorY);
		        		
				Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 226,
						223, true);
				imageBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
						scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
				
			        
				
				writeFile();
				// closing the activity after taking picture
				finish();

			}
		};
		mCamera.takePicture(null, null, mCall);
	}
	
	public static void writeFile()
	{
		File folder = new File(Environment.getExternalStorageDirectory() + "/PhonicsAppTest2");
		boolean success = true;
		if (!folder.exists())
		{
		    success = folder.mkdirs();
		}
		if (success) 
		{
		    // Do something on success
			try 
			{
				FileOutputStream out = new FileOutputStream("/sdcard/PhonicsAppTest2/"+2+".jpg");
				imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
				
				//Log.d(DEBUG_TAG, "camerafile: " + camerafile);
			}
			catch (FileNotFoundException e) 
			{
				Log.d("CAMERA", e.getMessage());
			} 
			catch (IOException e) 
			{
				Log.d("CAMERA", e.getMessage());
			}
		}
		else 
		{
		    // Do something else on failure 
			if(folder.exists()==true)
			{
				try 
				{
					FileOutputStream out = new FileOutputStream("/sdcard/PhonicsApp/AccountPic/1.jpg");
					imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
					
					//Log.d(DEBUG_TAG, "camerafile: " + camerafile);
				}
				catch (FileNotFoundException e) 
				{
					Log.d("CAMERA", e.getMessage());
				} 
				catch (IOException e) 
				{
					Log.d("CAMERA", e.getMessage());
				}
			}
		}
	}
}
