package miscutil.core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import cpw.mods.fml.common.FMLLog;

@SuppressWarnings("unused")
public class Benchmark {

	public void math() throws ParseException{
		Random r = new Random();

		FMLLog.info("Looking at the stars in the sky");
		
		// generate some random boolean values
		boolean[] booleans = new boolean[10];
		for (int i = 0; i < booleans.length; i++) {
			booleans[i] = r.nextBoolean();
		}

		//FMLLog.info(getSha256(booleans.toString()));

		/*for (boolean b : booleans) {
    	FMLLog.info(b + ", ");
    }*/

		// generate a uniformly distributed int random numbers
		int[] integers = new int[10];
		for (int i = 0; i < integers.length; i++) {
			integers[i] = r.nextInt();
		}

		FMLLog.info(getSha256(integers.toString()));

		/*for (int i : integers) {s
			FMLLog.info(i + ", ");
		}*/

		// generate a uniformly distributed float random numbers
		float[] floats = new float[10];
		for (int i = 0; i < floats.length; i++) {
			floats[i] = r.nextFloat();
		}

		FMLLog.info(getSha256(floats.toString()));

		/*for (float f : floats) {
			FMLLog.info(f + ", ");
		}*/

		// generate a Gaussian normally distributed random numbers
		double[] gaussians = new double[10];
		for (int i = 0; i < gaussians.length; i++) {
			gaussians[i] = r.nextGaussian();
		}

		FMLLog.info(getSha256(gaussians.toString()));
	}
	
	private String dateTime(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss");		  
		   //get current date time with Calendar()
		   Calendar cal = Calendar.getInstance();
		   return dateFormat.format(cal.getTime());
	}
	
	public String superhash(String a){
		FMLLog.info("Calculating the cost of life & the universe");
		int i = 1;
		String b = a;
		while (i < 3358 && i > 0){
			if (!b.equals(a)){
				b = a;
			}
			getSha256(b);
			a = b;
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				FMLLog.info("Hashbrown order failed");
				e.printStackTrace();
			}
			if (i == 500 || i == 1000 || i == 1500 || i == 2000 || i == 2500 || i == 3000 || i == 3500 || i == 4000 || i == 5000){
				//FMLLog.info("Calculating orbits around the sun: "+i);
			}
			i++;
		}
		return b;
	}

	private String getSha256(String message) {
		if (message == null || message.isEmpty()) {
			return "";
		}
		String chiper = generateString("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-=!@#$%^&*()_+`~[];',./{}:<>?|'", 32);
		String key = generateString("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-=!@#$%^&*()_+`~[];',./{}:<>?|'", 16); // key is used to construct a new SHA-256 key/salt

		// Initialize SHA-256
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			System.err.println(e.getMessage());
		}

		// Hashing entered key to construct a new key/salt
		byte[] keyAsSHA256 = digest.digest(key.getBytes());

		// Encoding the message with CBC
		char[] messageAsChars = message.toCharArray();
		messageAsChars[0] ^= keyAsSHA256[0]; // Avoiding buffer underflow
		for (int i = 1; i < messageAsChars.length; i++) {
			messageAsChars[i] ^= messageAsChars[i - 1]; // XOR with previous character
			messageAsChars[i] ^= keyAsSHA256[i % keyAsSHA256.length]; // XOR with keys hash
		}
		// build cipher from the chars
		chiper = new String(messageAsChars);
		String cipher = MD5(chiper);
		return chiper + "|" + cipher;
	}

	public String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}

	public static String generateString(String characters, int length)
	{
		Random r = new Random();
		char[] text = new char[length];
		for (int i = 0; i < length; i++)
		{
			text[i] = characters.charAt(r.nextInt(characters.length()));
		}
		return new String(text);
	}

}