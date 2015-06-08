package com.finoitkit.helper;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import com.finoitkit.R;

/**
 * Helper Class Maintaining the Application Instance
 * 
 * @author Ankur Parashar
 */
public class ActivityHelper {

	public static void startIntent(Activity activity, Class<?> activityClass) {
		Intent intent = new Intent(activity, activityClass);
		activity.startActivity(intent);
	}

	public static void startIntent(Activity activity, Class<?> activityClass, Bundle bundle) {
			
		Intent intent = new Intent(activity, activityClass);
		intent.putExtras(bundle);
		activity.startActivity(intent);
		
	}

	public static void startIntentForResult(Activity activity, Class<?> activityClass, int requestCode) {
			
		Intent intent = new Intent(activity, activityClass);
		activity.startActivityForResult(intent, requestCode);
		
	}

	public static void startIntentForResultWithBundle(Activity activity,
			Class<?> activityClass, int requestCode, Bundle bundle) {
		
		Intent intent = new Intent(activity, activityClass);
		intent.putExtras(bundle);
		activity.startActivityForResult(intent, requestCode);
		
	}
	
	public static void startGalleryView(Activity activity, int requestCode) {
		
		Intent photoPickerIntent1 = new Intent(Intent.ACTION_PICK);
		photoPickerIntent1.setType("image/*");
		activity.startActivityForResult(photoPickerIntent1, requestCode); 
		
	}
	
	public static void startCameraView(Activity activity, int requestCode, Uri mImageCaptureUri) {
		
		if( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ) {
			
				Intent photoPickerIntent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				mImageCaptureUri = Uri.fromFile( new File(Environment.getExternalStorageDirectory(),
												 "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg") );
				photoPickerIntent2.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);					
				activity.startActivityForResult(photoPickerIntent2, requestCode);
			
		}
		else{
			
				new AlertDialog.Builder(activity)
				.setMessage("External Storeage (SD Card) is required.\n\nCurrent state: " + Environment.getExternalStorageState())
				.setCancelable(true).create().show();
				
		}
		
	}
	
	public static void startCropIntent( Activity activity,Class<?> activityClass, int requestCode,
			 							Uri mImageCaptureUri, int outputX, int outputY ) {
		
			Intent intent = new Intent(activity , activityClass);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", outputX);
			intent.putExtra("outputY", outputY);
			intent.setData(mImageCaptureUri);
			activity.startActivityForResult(intent, requestCode);
		
	}
	
	public static void startActivityWithLefttoRightAnim(Activity activity){
		activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}
	
	public static void startActivityWithRighttoLeftAnim(Activity activity){
		activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

}
