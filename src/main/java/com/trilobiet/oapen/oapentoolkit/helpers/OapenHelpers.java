package com.trilobiet.oapen.oapentoolkit.helpers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.text.WordUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.trilobiet.graphqlweb.helpers.ViewHelpers;

@Component
public class OapenHelpers extends ViewHelpers {

	@Bean(name = "viewhelpers")
	public OapenHelpers thymeleafHelpers() {
	    return new OapenHelpers();
	}	

	public static String abbreviate(String input, int length) {
		
		return WordUtils.abbreviate(input, length, -1, "...");
	}
	
	/**
	 * https://stackoverflow.com/questions/31868404/how-to-abbreviate-string-at-the-middle-without-cutting-words
	 * 
	 * @param input
	 * @param middle
	 * @param length
	 * @return Abbreviated String
	 */
	public static String abbreviateMiddle(String input, String middle, int length) {
		
	    if (input != null && input.length() > length) {
	        int half = (length - middle.length()) / 2;

	        Pattern pattern = Pattern.compile(
	                "^(.{" + half + ",}?)" + "\\b.*\\b" + "(.{" + half + ",}?)$");
	        Matcher matcher = pattern.matcher(input);

	        if (matcher.matches()) {
	            return matcher.group(1) + middle + matcher.group(2);
	        }
	    }

	    return input;
	}	
	
	
	public static List<String> string2list(String in) {
		
		if (null==in) return Collections.emptyList();
		
		String[] a = in.split(",");
		List<String> l = Arrays.asList(a)
			.stream()
			.filter(k -> !k.isEmpty())
			.map(k->k.trim())
			.collect(Collectors.toList());
		Collections.sort(l,String.CASE_INSENSITIVE_ORDER);
		return l;
	}
	
}
