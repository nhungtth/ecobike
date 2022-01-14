package utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * @author nhungtth Contains helper functions
 */
public class Utils {

	public static DateFormat DATE_FORMATER = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private static Logger LOGGER = getLogger(Utils.class.getName());
	static {
		System.setProperty("java.util.logging.SimpleFormatter.format", "[%4$-4s] [%1$tF %1$tT] [%2$-7s] %5$s %n");
	}

	public static Logger getLogger(String className) {
		return Logger.getLogger(className);
	}

	/**
	 * Return a {@link java.lang.String String} that represents the current time.
	 * 
	 * @author NhungTTH
	 * @return the current time as {@link java.sql.Timestamp Timestamp}.
	 */
	public static Timestamp getToday() {
	    return new Timestamp(Calendar.getInstance().getTimeInMillis());
	}
	
	public static long calculateTime(Timestamp start) {
		Timestamp end = getToday();
		return (end.getTime() - start.getTime())/60000;
	}

	/**
	 * Return a {@link java.lang.String String} that represents the cipher text
	 * encrypted by md5 algorithm.
	 * 
	 * @author nhungtth vnpay
	 * @param message - plain text as {@link java.lang.String String}.
	 * @return cipher text as {@link java.lang.String String}.
	 */
	public static String md5(String message) {
		String digest = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hash = md.digest(message.getBytes("UTF-8"));
			// converting byte array to Hexadecimal String
			StringBuilder sb = new StringBuilder(2 * hash.length);
			for (byte b : hash) {
				sb.append(String.format("%02x", b & 0xff));
			}
			digest = sb.toString();
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
			Utils.getLogger(Utils.class.getName());
			digest = "";
		}
		return digest;
	}

}