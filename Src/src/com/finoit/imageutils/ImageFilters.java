package com.finoit.imageutils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Matrix;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;

/**
 * This Class defines methods and definations for Image Filters like
 * blurring, masking , hue, saturation, sharpness etc..
 * 
 * @author Ankur Parashar
 *
 */
public class ImageFilters {
	
	/**
	* This Function will Invert the Bitmap
	* @param src
	* @return
	*/
	public static Bitmap createInvertedImage(Bitmap src , boolean recycleOriginal) {

			 	int height = src.getHeight();
			 	int width  = src.getWidth();
			
			    Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

			    int A, R, G, B;
			    int pixelColor;
			  
			    for (int y = 0; y < height; y++)
			    {
			        for (int x = 0; x < width; x++)
			        {
			            pixelColor = src.getPixel(x, y);
			            
			            A = Color.alpha(pixelColor);
			            R = 255 - Color.red(pixelColor);
			            G = 255 - Color.green(pixelColor);
			            B = 255 - Color.blue(pixelColor);
			            
			            bmOut.setPixel(x, y, Color.argb(A, R, G, B));

			        } 
			    }
			    
			    if(recycleOriginal)
			    	 src.recycle();
			    
		    return bmOut;
	}
		
	/**
	 * This Function will create a GrayScale Image from the OriginalColor Image
	 * @param src
	 * @return
	 */
	public static Bitmap createGrayScaleImage(Bitmap src , boolean recycleOriginal){
		
			int height = src.getHeight();
			int width  = src.getWidth();
			
			// constant factors
			final double GS_RED   = 0.299;
			final double GS_GREEN = 0.587;
			final double GS_BLUE  = 0.114;

			Bitmap bmout = Bitmap.createBitmap(width, height, src.getConfig());
			
			// pixel information
			int A, R, G, B;
			int pixel;
			
			for(int i=0; i<width; i++){
			  for(int j=0; j<height; j++){
						
				  pixel = src.getPixel(i, j);
				  A = Color.alpha(pixel);
				  R = Color.red(pixel);
				  G = Color.green(pixel);
				  B = Color.blue(pixel);
				  
				  R = G = B = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);
				  bmout.setPixel(i, j, Color.argb(A, R, G, B));
				  
			  }
			}
			
			if(recycleOriginal)
				  src.recycle();
		
			return null;
		
	}
	
	/**
	 * This Function will Increase or Decrease the brightness of Image by level amount
	 * @param src
	 * @param level
	 * @param recycleOriginal
	 * @return
	 */
	public static Bitmap createBrightfilterImage(Bitmap src , int level , boolean recycleOriginal){
		
		int height = src.getHeight();
		int width  = src.getWidth();
		
		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
		
		int A, R, G, B;
		int pixel;

		for(int i=0; i<width; i++){
		  for(int j=0; j<height; j++){
			  
			  pixel = src.getPixel(i, j);
			  A = Color.alpha(pixel);
			  R = Color.red(pixel);
			  G = Color.green(pixel);
			  B = Color.blue(pixel);
			  
			  // increase/decrease each channel
			  R += level;
			  R = R > 255 ? 255 : 0;
		  	  G += level;
		  	  G = G > 255 ? 255 : 0;
		 	  B += level;
		 	  B = B > 255 ? 255 : 0;
      
			  bmOut.setPixel(i, j, Color.argb(A, R, G, B));
					
		  }
		}
		
		return bmOut;
	}
	
	/**
	 * This Function will create the Sepia Tunning Effect Image
	 * @param src
	 * @param depth
	 * @param red
	 * @param green
	 * @param blue
	 * @return
	 */
	public static Bitmap createSepiaToningEffect(Bitmap src, int depth, double red, double green, double blue) {
		
		    // image size
		    int width = src.getWidth();
		    int height = src.getHeight();

		    // create output bitmap
		    Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

		    // constant grayscale
		    final double GS_RED = 0.3;
		    final double GS_GREEN = 0.59;
		    final double GS_BLUE = 0.11;

		    // color information
		    int A, R, G, B;
		    int pixel;

		    // scan through all pixels
		    for(int x = 0; x < width; ++x) {
		        for(int y = 0; y < height; ++y) {
		            // get pixel color
		            pixel = src.getPixel(x, y);

		            // get color on each channel
		            A = Color.alpha(pixel);
		            R = Color.red(pixel);
		            G = Color.green(pixel);
		            B = Color.blue(pixel);

		            B = G = R = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);

		            R += (depth * red);
		            R = R > 255 ? 255 : 0;
		            G += (depth * green);
		            G = G > 255 ? 255 : 0;
		            B += (depth * blue);
		            B = B > 255 ? 255 : 0;

		            // set new pixel color to output image
		            bmOut.setPixel(x, y, Color.argb(A, R, G, B));

		        }
		    }

		    return bmOut;

	}

	/**
	 * This Function will Increase the Intensity of single Color Channel
	 * @param src
	 * @param type
	 * @param percent
	 * @return
	 */
	public static Bitmap boost(Bitmap src, int type, float percent){
		
		    int width = src.getWidth();
		    int height = src.getHeight();

		    Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
		    int A, R, G, B;
		    int pixel;

		    for(int x = 0; x < width; ++x) {
		        for(int y = 0; y < height; ++y) {
		        	
		            pixel = src.getPixel(x, y);
		
		            A = Color.alpha(pixel);
		            R = Color.red(pixel);
		            G = Color.green(pixel);
		            B = Color.blue(pixel);
		
		            if(type == 1) {
		
		                R = (int)(R * (1 + percent));
		                if(R > 255) R = 255;
		
		            }
		            else if(type == 2) {
		
		                G = (int)(G * (1 + percent));
		                if(G > 255) G = 255;
		
		            }
		            else if(type == 3) {
	
		                B = (int)(B * (1 + percent));
		                if(B > 255) B = 255;
	
		            }
	
		            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	
		        }
		    }

		  return bmOut;

	}
	
	/**
	 * 
	 * @param src
	 * @param type
	 * @return
	 */
	public static Bitmap flip(Bitmap src, int type) {
		
		final int FLIP_VERTICAL = 1;
		final int FLIP_HORIZONTAL = 2;
		
	    // create new matrix for transformation
	    Matrix matrix = new Matrix();
	
	    // if vertical
	    if(type == FLIP_VERTICAL) {
	        // y = y * -1
	        matrix.preScale(1.0f, -1.0f);
	    }
	    // if horizonal
	    else if(type == FLIP_HORIZONTAL) {
	        // x = x * -1
	        matrix.preScale(-1.0f, 1.0f);

	    }
	    else {
	        return null;

	    }

	    return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);

	}
	
	//GrayColorMatrix
	public static float[] grayColorFilter(){
		
		float matrix[] = {0.5f, 0.5f, 0.5f, 0, 0,
				          0.5f, 0.5f, 0.5f, 0, 0,
				          0.5f, 0.5f, 0.5f, 0, 0,
				          0f,   0f,   0f,  1f, 0};
		return matrix;
	}
	
	//Negative Image Filter
	public static float[] negativeImageFilter(){
		
		float matrix[] = {-1f,0f, 0f, 0, 255,
				          0f, -1f,0f, 0, 255,
				          0f, 0f, -1f, 0, 255,
				          0f, 0f, 0f, 1f, 0};
		return matrix;
	}
	
	//BrightnessMatrix
	public static float[] brightnessFilter(float offset){
		
		float matrix[] = {1f, 0f, 0f, 0f, offset,
		                  0f, 1f, 0f, 0f, offset,
		                  0f, 0f, 1f, 0f, offset,
		                  0f, 0f, 0f, 1f,  0};
		
		return matrix;
	}
	
	//ContrastFilter
	public static float[] contrastFilter(float contrast){
		
		float scale = contrast + 1.f;
		float translate = (-.5f*scale + .5f)*255.f;
		
		float matrix[] = {scale, 0f,   0f,   0, translate,
		                  0f,   scale, 0f,   0, translate,
		                  0f,   0f,   scale, 0, translate,
		                  0f,   0f,   0f,   1f , 0};
		
		return matrix;
	}
	
	//Contast Translate Filter
	public static float[] setContrastTranslateOnly(float contrast){
        
		float scale = contrast + 1.f;
        float translate = (-.5f * scale + .5f) * 255.f;
        
        float matrix[] =  new float[]{
               1, 0, 0, 0, translate,
               0, 1, 0, 0, translate,
               0, 0, 1, 0, translate,
               0, 0, 0, 1, 0 };
        
       return matrix;       
    }
	
	//SaturationFilter
	public static float[] saturationFilter(float saturation){
		
		float p_val = cleanValue(saturation,100);
		float x = 1+((p_val > 0) ? 3*p_val/100 : p_val/100);
		float lumR = 0.3086f;
	    float lumG = 0.6094f;
	    float lumB = 0.0820f;
		float matrix[] = {lumR*(1-x)+x, lumG*(1-x),  lumB*(1-x), 0, 0, 
						  lumR*(1-x),   lumG*(1-x)+x, lumB*(1-x), 0, 0,
						  lumR*(1-x),   lumG*(1-x),   lumB*(1-x)+x, 0, 0,
		                  0f, 0f, 0f, 1f, 0f,
		                  0f ,0f, 0f, 0f ,1f};
		
		return matrix;
	}
	
	//Hue Filter
	public static float[] hueFilter(float value){
	   
		value = cleanValue(value, 180f)/180f*(float)Math.PI;
	   
	    float cosVal = (float)Math.cos(value);
	    float sinVal = (float)Math.sin(value);
	    float lumR   = 0.213f;
	    float lumG   = 0.715f;
	    float lumB   = 0.072f;
	    
	    float[] fmatrix = new float[] { lumR + cosVal * (1 - lumR) + sinVal * (-lumR),
	            				        lumG + cosVal * (-lumG) + sinVal * (-lumG),
	            				        lumB + cosVal * (-lumB) + sinVal * (1 - lumB), 0, 0,
	            				        lumR + cosVal * (-lumR) + sinVal * (0.143f),
	            				        lumG + cosVal * (1 - lumG) + sinVal * (0.140f),
	            				        lumB + cosVal * (-lumB) + sinVal * (-0.283f), 0, 0,
	            				        lumR + cosVal * (-lumR) + sinVal * (-(1 - lumR)),
	            				        lumG + cosVal * (-lumG) + sinVal * (lumG),
	            				        lumB + cosVal * (1 - lumB) + sinVal * (lumB), 0, 0, 
	            				        0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f,0f, 1f };
	    
	    return fmatrix;
	    
	}
	
	//Brightness filter
	public static PorterDuffColorFilter applyLightness(int progress){

	    if(progress>0){
	        	int value = (int)progress*255/100;
	        	return new PorterDuffColorFilter(Color.argb(value, 255, 255, 255), Mode.SRC_OVER);
	    } 
	    else{
	        	int value = (int) (progress*-1)*255/100;
	        	return new PorterDuffColorFilter(Color.argb(value, 0, 0, 0), Mode.SRC_ATOP);
	    }

	}
	
	//Sepia Filter
	public static void applySepiaFilter(ColorMatrix colorMatrix){
		
		colorMatrix.setSaturation(0);
		final ColorMatrix matrixB = new ColorMatrix();
		// applying scales for RGB color values
		matrixB.setScale(1f, .95f, .82f, 1.0f);
		colorMatrix.setConcat(matrixB, colorMatrix);
		
	}

	private static float cleanValue(float p_val, float p_limit){
	    	 return Math.min(p_limit, Math.max(-p_limit, p_val));
	}
	
}
