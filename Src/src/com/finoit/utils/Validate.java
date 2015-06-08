package com.finoit.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validate {

	private static Pattern pattern;
	private static Matcher matcher;

	private static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpeg|jpg|png|gif|bmp))$)";

	public Validate() {
		pattern = Pattern.compile(IMAGE_PATTERN);
	}

	/**
	 * Validate image with regular expression
	 * 
	 * @param image
	 *            image for validation
	 * @return true valid image, false invalid image
	 */
	public static boolean validateImage(final String image) {

		if (pattern == null)
			pattern = Pattern.compile(IMAGE_PATTERN);
		matcher = pattern.matcher(image);

		return matcher.matches();
	}
	
	/**
	 * Email Validation
	 * @param eMail
	 * @return
	 */
	public static boolean validateEmail(final String eMail) {

		final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		if (eMail.matches(EMAIL_PATTERN))
			return true;
		else
			return false;

	}

}