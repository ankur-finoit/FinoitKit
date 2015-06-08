package com.finoit.imageutils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

/**
 * This class Consists of Basic Image handling functions
 * @author Ankur Parashar
 *
 */
public class ImageUtil {
	
	  /**
	   * This function returns the Path of the Image using it's URI
	   * @param paramContext
	   * @param paramUri
	   * @return
	   */
	  public static String GetPathFromUri(Context paramContext, Uri paramUri)
	  {
		   String str;
		   try
		   {
			  	if (paramUri.toString().startsWith("file:")){
			  			str = paramUri.getPath();
			  	}
			  	else
			  	{
			  			str = null;
			  			String[] arrayOfString = new String[1];
			  			arrayOfString[0] = "_data";
			  			Cursor localCursor = paramContext.getContentResolver().query(paramUri, arrayOfString, null, null, null);
			  			if (localCursor != null)
			  			{
			  					localCursor.moveToFirst();
			  					int i = localCursor.getColumnIndex(arrayOfString[0]);
			  					if ((localCursor.getCount() >= 1) && (localCursor.getColumnCount() >= i + 1))
			  						str = localCursor.getString(i);
			  					localCursor.close();
			  			}
			  	}
			  	
		  }
		  catch (Exception localException){
			  str = null;
		  }
		  
	      return str;
	  }
	
	  @SuppressWarnings({ "rawtypes" })
	public static boolean ExifNeedsRotate(String paramString){
			
	      if (android.os.Build.VERSION.SDK_INT >= 5){
	    	
	          try
	          {
	    	  
					Class<?> localClass = Class.forName("android.media.ExifInterface");
					Class[] arrayOfClass1 = new Class[1];
	        		arrayOfClass1[0] = String.class;
	        		Constructor localConstructor = localClass.getConstructor(arrayOfClass1);
	        		Class[] arrayOfClass2 = new Class[1];
	        		arrayOfClass2[0] = String.class;
	        		Method localMethod = localClass.getMethod("getAttribute", arrayOfClass2);
	        		Object[] arrayOfObject1 = new Object[1];
	        		arrayOfObject1[0] = paramString;
	        		Object localObject1 = localConstructor.newInstance(arrayOfObject1);
	        		Object[] arrayOfObject2 = new Object[1];
	        		arrayOfObject2[0] = "Orientation";
	        		Object localObject2 = localMethod.invoke(localObject1, arrayOfObject2);
	        		if (localObject2 != null){
	        			  boolean bool = localObject2.equals("6");
	        			  if (bool)
	        				 return true;
	        		}
	       	
	      	  }
	      	  catch (Exception localException){
	      		  return false;
	      	  }
	     
	    }
	      
		return false;
	    
	}
	
	public static Bitmap loadBitmap(String paramString){
		
	    BitmapFactory.Options localOptions = new BitmapFactory.Options();
	    localOptions.inDither = false;
	    return BitmapFactory.decodeFile(paramString, localOptions);
	    
	}
	
	public static Bitmap loadPicture( int maxWidth ,int maxHeight  ){
		
		return null;
	}
	
	/**
	 * This function will decode a Bitmap from resource
	 * @param context
	 * @param source : resource name 
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeBitmapFromDrawable(Context context, int source, int reqWidth, int reqHeight){
		 	
		 	final BitmapFactory.Options options;
		 	Bitmap result = null;
		 	try{
		 			//First decode with inJustDecodeBounds = true to check dimensions
		 			options = new BitmapFactory.Options();
		 			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		 			options.inScaled = false;
		 			options.inDither = false;
		 			options.inJustDecodeBounds = true;
		 			BitmapFactory.decodeResource(context.getResources(), source, options);
		 			options.inSampleSize = 1;
		 			options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		 			options.inJustDecodeBounds = false;
		 			result = BitmapFactory.decodeResource(context.getResources(), source, options);
		 			System.out.println("SampleSize::"+options.inSampleSize+" "+result.getWidth()+" "+result.getHeight());
		 	 }
		 	 catch(Exception e){
		 	 	Log.d("Image Compress Error", e.getMessage());
		 	 }
		 	
		     return result;
		    
	 }
	 
	 /**
	  * This will be used for retrieving the Bitmap from Uri
	  * @param context
	  * @param selectedImage
	  * @param reqWidth
	  * @param reqHeight
	  * @return
	  */
	 public static Bitmap decodeBitmapFromStream( Context context , Uri selectedImage , int reqWidth , int reqHeight ){
	  	        
		 	final BitmapFactory.Options options;
		 	
		 	Object localObject = null;
	 	 	try{
		 		  options = new BitmapFactory.Options();
		 		  options.inPreferredConfig=Bitmap.Config.ARGB_8888;
		 		  options.inJustDecodeBounds = true;
		 		  options.inScaled = false;
		 		  options.inDither = false;
		 		  options.inSampleSize = 1;
		 		  BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, options);
		 		  options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		 		  options.inJustDecodeBounds = false;
		 		  localObject = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, options);
		 	}
		 	catch(Exception e){
		 		Log.d("Image Compress Error", e.getMessage());
		 	}
		 	
		 	if(ExifNeedsRotate(GetPathFromUri(context, selectedImage))){
			       Matrix localMatrix = new Matrix();
			       localMatrix.postRotate(90.0F);
			       Bitmap localBitmap = Bitmap.createBitmap((Bitmap)localObject, 0, 0, ((Bitmap)localObject).getWidth(), ((Bitmap)localObject).getHeight(), localMatrix, true);
			       ((Bitmap)localObject).recycle();
			       localObject = localBitmap;
			 }
		 	
		    return (Bitmap)localObject;
		    
	 }
	 
		/**
		 * 
		 * @param name
		 *            -- FileName
		 * @return File
		 * @throws IOException
		 *             -- Exception
		 */
		public static byte[] imagedata(Bitmap image, boolean isRecycle) {

			if (image != null) {

				ByteArrayOutputStream blob = new ByteArrayOutputStream();
				Bitmap test = image;
				test.compress(CompressFormat.JPEG, 70, blob);
				byte[] bitmapdata = blob.toByteArray();
				if (isRecycle)
					image.recycle();

				return bitmapdata;
			}

			return null;

		}
	
	 /**
	  * 
	  * @param options
	  * @param reqWidth
	  * @param reqHeight
	  * @return
	  */
	 public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
		  
		// Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {
	    	
	    	   // Calculate ratios of height and width to requested height and width
	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);

	          // Choose the smallest ratio as inSampleSize value, this will guarantee
	         // a final image with both dimensions larger than or equal to the
	        // requested height and width.
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	        
	    }
	    
	    return inSampleSize;
	}
	 
	/**
	 * This function will scale the image 
	 * @param currentBitmap
	 * @param newWidth
	 * @return
	 */
	 public static Bitmap scaleImage(Bitmap currentBitmap , int newWidth){
	 	    
	        int width  = currentBitmap.getWidth();
	        int height = currentBitmap.getHeight();
	        
	        float scaleWidth = ((float)newWidth)/width;
	        float ratio = ((float)width)/newWidth;
	        int newHeight = (int)(height/ratio);
	        
	        float scaleHeight = ((float)newHeight)/height;
	        Matrix matrix = new Matrix();
	        matrix.postScale(scaleWidth, scaleHeight);
	        return Bitmap.createBitmap(currentBitmap, 0, 0, width, height, matrix, true);

	 } 
	 
	/**
	 * This function will Scaledown the OriginalBitmap and recycle the original bitmap
	 * @param original
	 * @param recycleOriginal
	 * @return Bitmap
	 */
	 public static Bitmap scaleDownBitmap(Bitmap original, boolean recycleOriginal, int newmaxWidth , int newmaxHeight){
	    	
			if(original == null)
				 return null;
			
			Bitmap rtr = null;
			try{
				
					int origWidth  = original.getWidth();
					int origHeight = original.getHeight();
					
					if(origWidth <= newmaxWidth && origHeight <= newmaxWidth){
							Bitmap b = Bitmap.createBitmap(original);
							if (recycleOriginal && (original != b))
								original.recycle();
							return b;
					}
	    	
					int newWidth = 0;
					int newHeight = 0;
						    	
					float ratio;
	    	
					if(origWidth > origHeight){
							ratio = (float)origWidth/(float)origHeight;
							newWidth = newmaxWidth;
							newHeight = (int)((float)newWidth/ratio);
							
					} 
					else{
							ratio = (float)origHeight/(float)origWidth;
							newHeight = newmaxHeight;
							newWidth = (int)((float)newHeight/ratio);
					}
				
			        rtr =  CreateScaledBitmap(original, newWidth, newHeight);
			        
			        System.out.println("Dimesions: "+newWidth+" "+" "+newHeight);
				
			        if(recycleOriginal && original != rtr)
							original.recycle();
				
			}
			catch (Exception e) {
				Log.d("Image Compress Error", e.getMessage());
			}
	    	
	    	return rtr;
	    	
	 }
	 
	/**
	 * 
	 * @param paramBitmap
	 * @param paramInt1
	 * @param paramInt2
	 * @return
	 */
	 public static Bitmap CreateScaledBitmap(Bitmap paramBitmap, int paramInt1, int paramInt2)
	 {
		 	Bitmap localBitmap = Bitmap.createBitmap(paramInt1, paramInt2, paramBitmap.getConfig());
		 	Canvas localCanvas = new Canvas(localBitmap);
		 	localCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, 2));
		 	localCanvas.drawBitmap(paramBitmap, new Rect(0, 0, paramBitmap.getWidth(), paramBitmap.getHeight()),
		 						   new Rect(0, 0, paramInt1, paramInt2), null);
		 	
	    return localBitmap;
	}
	 
	 /**
	  * 
	  *  @param c
	  *  @param image
	  *  @param recycleOriginal
	  *  @return
	  */
	  public static Uri putBitmapIntoGalleryAndGetUri(Context c, Bitmap image, boolean recycleOriginal) {
		       
			 if(image != null){
				 
		            Uri dataUri = Uri.parse(MediaStore.Images.Media.insertImage(c.getContentResolver(), image, null, null));
		            if (dataUri != null) {
		            	ContentValues cv = new ContentValues();
		            	cv.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
		            	cv.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
		            	c.getContentResolver().update(dataUri, cv, null, null);
		            }
		            
		            if (recycleOriginal)
		                image.recycle();
		            
		            return dataUri; 
		    }
			 
		        return null;
		        
     }

}
