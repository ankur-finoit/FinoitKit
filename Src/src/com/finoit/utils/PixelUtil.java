package com.finoit.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * This Class contains pixel and dip related functions
 * @author Ankur Parashar
 *
 */
public class PixelUtil {
	
	/**
	* This function will convert the dip into pixel value
	* @param context
	* @param dp
	* @return
	*/
	public static int dpToPx(Context context, int dp) {
		
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float scale = metrics.density;
		return (int)(scale * dp);
		
	}

}
