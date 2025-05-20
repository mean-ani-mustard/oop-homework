import java.security.*;
import java.util.concurrent.CountDownLatch;

public class Cracker {
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();
	static CountDownLatch latch;
	static volatile boolean found = false;

	/*
	 * Converts a byte array to a hex string representation.
	 * Each byte is converted to two hex characters, padded with '0' if needed.
	 */

	public static String hexToString(byte[] bytes) {
		StringBuilder buff = new StringBuilder();
		for (byte b : bytes) {
			int val = b & 0xff;
			if (val < 16) buff.append('0');
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}


	/*
	 * Converts a hex string into a byte array.
	 * Each pair of hex characters is parsed into a single byte.
	 */

	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length(); i += 2) {
			result[i / 2] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
		}
		return result;
	}

	/*
	 * Computes the SHA hash of a given input string.
	 * Returns the hash as a hex string.
	 * Throws RuntimeException if SHA algorithm is not supported.
	 */

	public static String hashCode(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			md.update(input.getBytes());
			return hexToString(md.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	/*
	 * Worker thread class responsible for brute-forcing passwords starting
	 * with a range of initial characters.
	 * Recursively tries all character combinations up to maxLength.
	 * Uses MessageDigest to hash candidates and compares with target hash.
	 */

	static class Worker extends Thread {
		private final String targetHash;
		private final int maxLength;
		private final int startIndex, endIndex;
		private final MessageDigest md;

		public Worker(String targetHash, int maxLength, int startIndex, int endIndex) throws NoSuchAlgorithmException {
			this.targetHash = targetHash;
			this.maxLength = maxLength;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
			this.md = MessageDigest.getInstance("SHA");
		}
		/*
		 * Starts the brute-force process on assigned character subset.
		 * Calls the recursive bruteForce method on each starting character.
		 * Signals completion by counting down the latch.
		 */

		public void run() {
			try {
				char[] current = new char[maxLength];
				for (int i = startIndex; i <= endIndex && !found; i++) {
					current[0] = CHARS[i];
					bruteForce(current, 1);
				}
			} finally {
				latch.countDown();
			}
		}
		/*
		 * Recursive method that attempts all character combinations at each depth.
		 * Stops early if password is found or max length reached.
		 * Hashes each candidate string and compares to target.
		 */

		private void bruteForce(char[] current, int depth) {
			if (found) return;

			String candidate = new String(current, 0, depth);
			md.reset();
			md.update(candidate.getBytes());
			String hash = hexToString(md.digest());

			if (hash.equals(targetHash)) {
				System.out.println(candidate);
				found = true;
				return;
			}
			if (depth == maxLength) return;

			for (char ch : CHARS) {
				if (found) return;
				current[depth] = ch;
				bruteForce(current, depth + 1);
			}
		}
	}
	/*
	 * Main method parses command-line arguments and either:
	 * - Prints the SHA hash of a given string, or
	 * - Launches multithreaded brute-force cracking to find a password matching a target hash.
	 * Divides the character set evenly among worker threads for concurrent searching.
	 */

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Args: target length [workers]");
			return;
		}

		if (args.length < 3) {
			System.out.println(hashCode(args[0]));
		} else {
			String targetHash = args[0];
			int maxLength = Integer.parseInt(args[1]);
			int numThreads = Integer.parseInt(args[2]);
			int totalChars = CHARS.length;
			int charsPerThread = totalChars / numThreads;
			int remainder = totalChars % numThreads;
			latch = new CountDownLatch(numThreads);
			int start = 0;
			for (int i = 0; i < numThreads; i++) {
				int end = start + charsPerThread - 1;
				if (remainder > 0) {
					end++;
					remainder--;
				}
				try {
					new Worker(targetHash, maxLength, start, end).start();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
				start = end + 1;}

			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!found) {
				System.out.println("Password not found.");}
			System.out.println("All done.");}
	}
}