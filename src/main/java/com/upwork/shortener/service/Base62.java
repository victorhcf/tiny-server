package com.upwork.shortener.service;

public class Base62 {

	/**
	   * Base62 characters table sorted to quickly calculate decimal equivalency by compensating.
	   */
	private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
	
	
	/**
	   * Returns the base 62 string of an integer.
	   *
	   * @return the base 62 string of an integer
	   */
	public static String encodeBase62(Long value) {
	  final StringBuilder sb = new StringBuilder(1);
	  do {
	    sb.append(BASE62[(int) (value % 62)]);
	    value /= 62;
	  } while (value > 0);
	  return sb.reverse().toString();
	}
}
