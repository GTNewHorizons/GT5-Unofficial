package gtPlusPlus.api.objects.random;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;
import java.util.UUID;

/**
 * 
 * Implement modified version of Apache's OpenJPA UUID generator.
 * This UUID generator is paired with a Blum-Blum-Shub random number generator
 * which in itself is seeded by custom SecureRandom.
 * 
 * The UUID generator class has been converted from a static factory to an instanced factory.
 * 
 */

//========================================= APACHE BLOCK =========================================

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */

/**
 * UUID value generator.  Type 1 generator is based on the time-based generator
 * in the Apache Commons Id project:  http://jakarta.apache.org/commons/sandbox
 * /id/uuid.html  The type 4 generator uses the standard Java UUID generator.
 *
 * The type 1 code has been vastly simplified and modified to replace the 
 * ethernet address of the host machine with the IP, since we do not want to 
 * require native libs and Java cannot access the MAC address directly.
 *
 * In spirit, implements the IETF UUID draft specification, found here:<br />
 * http://www1.ics.uci.edu/~ejw/authoring/uuid-guid/draft-leach-uuids-guids-01
 * .txt
 *
 * @author Abe White, Kevin Sutter
 * @since 0.3.3
 */
public class UUIDGenerator {

	// supported UUID types
	public static final int TYPE1 = 1;
	public static final int TYPE4 = 4;
	// indexes within the uuid array for certain boundaries
	private static final byte IDX_TIME_HI = 6;
	private static final byte IDX_TYPE = 6; // multiplexed
	private static final byte IDX_TIME_MID = 4;
	private static final byte IDX_TIME_LO = 0;
	private static final byte IDX_TIME_SEQ = 8;
	private static final byte IDX_VARIATION = 8; // multiplexed
	// indexes and lengths within the timestamp for certain boundaries
	private static final byte TS_TIME_LO_IDX = 4;
	private static final byte TS_TIME_LO_LEN = 4;
	private static final byte TS_TIME_MID_IDX = 2;
	private static final byte TS_TIME_MID_LEN = 2;
	private static final byte TS_TIME_HI_IDX = 0;
	private static final byte TS_TIME_HI_LEN = 2;
	// offset to move from 1/1/1970, which is 0-time for Java, to gregorian
	// 0-time 10/15/1582, and multiplier to go from 100nsec to msec units
	private static final long GREG_OFFSET = 0xB1D069B5400L;
	private static final long MILLI_MULT = 10000L;
	// type of UUID -- time based
	private final static byte TYPE_TIME_BASED = 0x10;
	// random number generator used to reduce conflicts with other JVMs, and
	// hasher for strings.  
	private Random RANDOM;
	// 4-byte IP address + 2 random bytes to compensate for the fact that
	// the MAC address is usually 6 bytes
	private byte[] IP;
	// counter is initialized to 0 and is incremented for each uuid request
	// within the same timestamp window.
	private int _counter;
	// current timestamp (used to detect multiple uuid requests within same
	// timestamp)
	private long _currentMillis;
	// last used millis time, and a semi-random sequence that gets reset
	// when it overflows
	private long _lastMillis = 0L;
	private static final int MAX_14BIT = 0x3FFF;
	private short _seq = 0;
	private boolean type1Initialized = false;	    /*
	 * Initializer for type 1 UUIDs.  Creates random generator and genenerates
	 * the node portion of the UUID using the IP address.
	 */
	private synchronized void initializeForType1() {
		if (type1Initialized == true) {
			return;
		}
		// note that secure random is very slow the first time
		// it is used; consider switching to a standard random
		RANDOM = CSPRNG_DO_NOT_USE.generate();
		_seq = (short) RANDOM.nextInt(MAX_14BIT);

		byte[] ip = null;
		try {
			ip = InetAddress.getLocalHost().getAddress();
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
		IP = new byte[6];
		RANDOM.nextBytes(IP);
		//OPENJPA-2055: account for the fact that 'getAddress'
		//may return an IPv6 address which is 16 bytes wide.
		for( int i = 0 ; i < ip.length; ++i ) {
			IP[2+(i%4)] ^= ip[i];
		}	        
		type1Initialized = true;
	}
	/**
	 * Return a unique UUID value.
	 */
	public byte[] next(int type) {
		if (type == TYPE4) {
			return createType4();
		}
		return createType1();
	}	      
	/*
	 * Creates a type 1 UUID 
	 */
	public byte[] createType1() {
		if (type1Initialized == false) {
			initializeForType1();
		}
		// set ip addr
		byte[] uuid = new byte[16];
		System.arraycopy(IP, 0, uuid, 10, IP.length);
		// Set time info.  Have to do this processing within a synchronized
		// block because of the statics...
		long now = 0;
		synchronized (UUIDGenerator.class) {
			// Get the time to use for this uuid.  This method has the side
			// effect of modifying the clock sequence, as well.
			now = getTime();
			// Insert the resulting clock sequence into the uuid
			uuid[IDX_TIME_SEQ] = (byte) ((_seq & 0x3F00) >>> 8);
			uuid[IDX_VARIATION] |= 0x80;
			uuid[IDX_TIME_SEQ+1] = (byte) (_seq & 0xFF);
		}
		// have to break up time because bytes are spread through uuid
		byte[] timeBytes = Bytes.toBytes(now);
		// Copy time low
		System.arraycopy(timeBytes, TS_TIME_LO_IDX, uuid, IDX_TIME_LO,
				TS_TIME_LO_LEN);
		// Copy time mid
		System.arraycopy(timeBytes, TS_TIME_MID_IDX, uuid, IDX_TIME_MID,
				TS_TIME_MID_LEN);
		// Copy time hi
		System.arraycopy(timeBytes, TS_TIME_HI_IDX, uuid, IDX_TIME_HI,
				TS_TIME_HI_LEN);
		//Set version (time-based)
		uuid[IDX_TYPE] |= TYPE_TIME_BASED; // 0001 0000
		return uuid;
	}
	/*
	 * Creates a type 4 UUID
	 */
	private byte[] createType4() {
		UUID type4 = UUID.randomUUID();
		byte[] uuid = new byte[16];
		longToBytes(type4.getMostSignificantBits(), uuid, 0);
		longToBytes(type4.getLeastSignificantBits(), uuid, 8);
		return uuid;
	}	    
	/*
	 * Converts a long to byte values, setting them in a byte array
	 * at a given starting position.
	 */
	private void longToBytes(long longVal, byte[] buf, int sPos) {
		sPos += 7;
		for(int i = 0; i < 8; i++)         
			buf[sPos-i] = (byte)(longVal >>> (i * 8));
	}

	/**
	 * Return the next unique uuid value as a 16-character string.
	 */
	public String nextString(int type) {
		byte[] bytes = next(type);
		try {
			return new String(bytes, "ISO-8859-1");
		} catch (Exception e) {
			return new String(bytes);
		}
	}
	/**
	 * Return the next unique uuid value as a 32-character hex string.
	 */
	public String nextHex(int type) {
		return Base16Encoder.encode(next(type));
	}
	/**
	 * Get the timestamp to be used for this uuid.  Must be called from
	 * a synchronized block.
	 *
	 * @return long timestamp
	 */
	// package-visibility for testing
	private long getTime() {
		if (RANDOM == null)
			initializeForType1();
		long newTime = getUUIDTime();
		if (newTime <= _lastMillis) {
			incrementSequence();
			newTime = getUUIDTime();
		}
		_lastMillis = newTime;
		return newTime;
	}
	/**
	 * Gets the appropriately modified timestamep for the UUID.  Must be called
	 * from a synchronized block.
	 *
	 * @return long timestamp in 100ns intervals since the Gregorian change
	 * offset
	 */
	private long getUUIDTime() {
		if (_currentMillis != System.currentTimeMillis()) {
			_currentMillis = System.currentTimeMillis();
			_counter = 0;  // reset counter
		}
		// check to see if we have created too many uuid's for this timestamp
		if (_counter + 1 >= MILLI_MULT) {
			// Original algorithm threw exception.  Seemed like overkill.
			// Let's just increment the timestamp instead and start over...
			_currentMillis++;
			_counter = 0;
		}
		// calculate time as current millis plus offset times 100 ns ticks
		long currentTime = (_currentMillis + GREG_OFFSET) * MILLI_MULT;
		// return the uuid time plus the artificial tick counter incremented
		return currentTime + _counter++;
	}
	/**
	 * Increments the clock sequence for this uuid.  Must be called from a
	 * synchronized block.
	 */
	private void incrementSequence() {
		// increment, but if it's greater than its 14-bits, reset it
		if (++_seq > MAX_14BIT) {
			_seq = (short) RANDOM.nextInt(MAX_14BIT);  // semi-random
		}
	}

	//Add Dependant classes internally

	/**
	 * This class came from the Apache Commons Id sandbox project in support
	 * of the UUIDGenerator implementation.
	 *
	 * <p>Static methods for managing byte arrays (all methods follow Big
	 * Endian order where most significant bits are in front).</p>
	 */
	public static final class Bytes {
		/**
		 * <p>Hide constructor in utility class.</p>
		 */
		private Bytes() {
		}
		/**
		 * Appends two bytes array into one.
		 *
		 * @param a A byte[].
		 * @param b A byte[].
		 * @return A byte[].
		 */
		public static byte[] append(byte[] a, byte[] b) {
			byte[] z = new byte[a.length + b.length];
			System.arraycopy(a, 0, z, 0, a.length);
			System.arraycopy(b, 0, z, a.length, b.length);
			return z;
		}
		/**
		 * Returns a 8-byte array built from a long.
		 *
		 * @param n The number to convert.
		 * @return A byte[].
		 */
		public static byte[] toBytes(long n) {
			return toBytes(n, new byte[8]);
		}
		/**
		 * Build a 8-byte array from a long.  No check is performed on the
		 * array length.
		 *
		 * @param n The number to convert.
		 * @param b The array to fill.
		 * @return A byte[].
		 */
		public static byte[] toBytes(long n, byte[] b) {
			b[7] = (byte) (n);
			n >>>= 8;
			b[6] = (byte) (n);
			n >>>= 8;
			b[5] = (byte) (n);
			n >>>= 8;
			b[4] = (byte) (n);
			n >>>= 8;
			b[3] = (byte) (n);
			n >>>= 8;
			b[2] = (byte) (n);
			n >>>= 8;
			b[1] = (byte) (n);
			n >>>= 8;
			b[0] = (byte) (n);

			return b;
		}
		/**
		 * Build a long from first 8 bytes of the array.
		 *
		 * @param b The byte[] to convert.
		 * @return A long.
		 */
		public static long toLong(byte[] b) {
			return ((((long) b[7]) & 0xFF)
					+ ((((long) b[6]) & 0xFF) << 8)
					+ ((((long) b[5]) & 0xFF) << 16)
					+ ((((long) b[4]) & 0xFF) << 24)
					+ ((((long) b[3]) & 0xFF) << 32)
					+ ((((long) b[2]) & 0xFF) << 40)
					+ ((((long) b[1]) & 0xFF) << 48)
					+ ((((long) b[0]) & 0xFF) << 56));
		}
		/**
		 * Compares two byte arrays for equality.
		 *
		 * @param a A byte[].
		 * @param b A byte[].
		 * @return True if the arrays have identical contents.
		 */
		public static boolean areEqual(byte[] a, byte[] b) {
			int aLength = a.length;
			if (aLength != b.length) {
				return false;
			}
			for (int i = 0; i < aLength; i++) {
				if (a[i] != b[i]) {
					return false;
				}
			}
			return true;
		}
		/**
		 * <p>Compares two byte arrays as specified by <code>Comparable</code>.
		 *
		 * @param lhs - left hand value in the comparison operation.
		 * @param rhs - right hand value in the comparison operation.
		 * @return  a negative integer, zero, or a positive integer as 
		 * <code>lhs</code> is less than, equal to, or greater than 
		 * <code>rhs</code>.
		 */
		public static int compareTo(byte[] lhs, byte[] rhs) {
			if (lhs == rhs) {
				return 0;
			}
			if (lhs == null) {
				return -1;
			}
			if (rhs == null) {
				return +1;
			}
			if (lhs.length != rhs.length) {
				return ((lhs.length < rhs.length) ? -1 : +1);
			}
			for (int i = 0; i < lhs.length; i++) {
				if (lhs[i] < rhs[i]) {
					return -1;
				} else if (lhs[i] > rhs[i]) {
					return 1;
				}
			}
			return 0;
		}
		/**
		 * Build a short from first 2 bytes of the array.
		 *
		 * @param b The byte[] to convert.
		 * @return A short.
		 */
		public static short toShort(byte[] b) {
			return  (short) ((b[1] & 0xFF) + ((b[0] & 0xFF) << 8));
		}
	}
	/**
	 * Base 16 encoder.
	 *
	 * @author Marc Prud'hommeaux
	 */
	public static final class Base16Encoder {

		private final static char[] HEX = new char[]{
				'0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		/**
		 * Convert bytes to a base16 string.
		 */
		public static String encode(byte[] byteArray) {
			StringBuilder hexBuffer = new StringBuilder(byteArray.length * 2);
			for (int i = 0; i < byteArray.length; i++)
				for (int j = 1; j >= 0; j--)
					hexBuffer.append(HEX[(byteArray[i] >> (j * 4)) & 0xF]);
			return hexBuffer.toString();
		}
		/**
		 * Convert a base16 string into a byte array.
		 */
		public static byte[] decode(String s) {
			int len = s.length();
			byte[] r = new byte[len / 2];
			for (int i = 0; i < r.length; i++) {
				int digit1 = s.charAt(i * 2), digit2 = s.charAt(i * 2 + 1);
				if (digit1 >= '0' && digit1 <= '9')
					digit1 -= '0';
				else if (digit1 >= 'A' && digit1 <= 'F')
					digit1 -= 'A' - 10;
				if (digit2 >= '0' && digit2 <= '9')
					digit2 -= '0';
				else if (digit2 >= 'A' && digit2 <= 'F')
					digit2 -= 'A' - 10;

				r[i] = (byte) ((digit1 << 4) + digit2);
			}
			return r;
		}
	}



}

//========================================= APACHE BLOCK =========================================

