package com.dental.lab.utils;

import com.dental.lab.exceptions.InvalidArgumentException;

public class HelperMethods {
	
	/**
	 * Split the passed string and returns an Array according with the rules:<br><br>
	 * - Words in the string are defined by blank spaces<br>
	 * - If the string is a single word, an Array of size one, with such word as its single element is returned<br>
	 * - If the string contains two words, an Array of size 2, with both words, is returned<br>
	 * - If the string is null or has length zero, an Exception is thrown<br>
	 * - If the string contains more than two words, an Exception is thrown<br>
	 * 
	 * @param fullLastName - string to be split into at most two words
	 * @return
	 * @throws InvalidArgumentException
	 */
	public static String[] splitTwoWordsString(String fullLastName) 
			throws InvalidArgumentException {
		
		if(fullLastName == null)
			throw new InvalidArgumentException("Parameter can not be null");
		
		fullLastName = fullLastName.replaceFirst("^\s*", "");
		fullLastName = fullLastName.replaceAll("\s+", " ");
		
		if(fullLastName.length() == 0)
			throw new InvalidArgumentException("Parameter can not be blank");
		
		String[] lastNames = fullLastName.split(" ");
		
		if(lastNames.length > 2)
			throw new InvalidArgumentException("Full last name can contain at most two words");
		
		return lastNames;
	}

}
