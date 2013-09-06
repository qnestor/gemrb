package net.sourceforge.gemrb;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

public class GameVerifier {

	public static final String CHITINKEY_FILENAME = "CHITIN.KEY";
	public static final String CACHE_FOLDERNAME = "cache";
	
	public static FilenameFilter CHITIN_FILTER = new FilenameFilter(){

		@Override
		public boolean accept(File dir, String filename) {
			
			return filename.equals(CHITINKEY_FILENAME);
		}
	};
	public static FilenameFilter CACHE_FILTER = new FilenameFilter(){

		@Override
		public boolean accept(File dir, String filename) {
			
			return filename.equals(CHITINKEY_FILENAME);
		}
	};
	
	
	
	
	
	
	public static boolean verifyIWD2(File gamepath) {
		
		boolean chitin =chitinOK(gamepath);
		boolean cacheOK = cacheOK(gamepath);
		
		return cacheOK&chitin;
	}
	
	public static boolean verifyBG2(File gamepath) {
		
		boolean chitin =chitinOK(gamepath);
		boolean cacheOK = cacheOK(gamepath);
		
		return cacheOK&chitin;
	}
	
	public static boolean verifyBG1(File gamepath) {
		
		boolean chitin =chitinOK(gamepath);
		boolean cacheOK = cacheOK(gamepath);
		
		return cacheOK&chitin;
	}
	
	public static boolean verifyIWD(File gamepath) {
		
		boolean chitin =chitinOK(gamepath);
		boolean cacheOK = cacheOK(gamepath);
		
		return cacheOK&chitin;
	}
	
	public static boolean verifyPST(File gamepath) {
		
		boolean chitin =chitinOK(gamepath);
		boolean cacheOK = cacheOK(gamepath);
		
		return cacheOK&chitin;
	}
	
	public static boolean verifyHOW(File gamepath) {
		
		boolean chitin =chitinOK(gamepath);
		boolean cacheOK = cacheOK(gamepath);
		
		return cacheOK&chitin;
	}
	
	/*
	 * There is a way of checking if the files in cache are "alien", or no good. Appearantly
	 *  empty is always ok
	 */
	private static boolean cacheOK(File gamepath) {
		
		String[] listGamepath = gamepath.list(null);
		return Arrays.asList(listGamepath).contains(CACHE_FOLDERNAME);
	}
	
	private static boolean chitinOK(File gamepath) {
		
		File[] listFiles = gamepath.listFiles(CHITIN_FILTER);
		if(listFiles != null && listFiles.length == 1) {
			return true;
		}
		else {
			//setup message
			return false;
		}
	}
}
