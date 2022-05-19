package com.dental.lab.helpers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.dental.lab.exceptions.InvalidArgumentException;
import com.dental.lab.utils.HelperMethods;

public class HelperMethodsTest {
	
	@Test
	public void splitTwoWordsStringTest() {
		assertThat(HelperMethods.splitTwoWordsString("Oscar Manuel "))
			.containsOnly("Oscar", "Manuel");
		
		assertThat(HelperMethods.splitTwoWordsString(" Oscar Manuel"))
			.containsOnly("Oscar", "Manuel");
		
		assertThat(HelperMethods.splitTwoWordsString("   Oscar   Manuel"))
			.containsOnly("Oscar", "Manuel");
		
		assertThat(HelperMethods.splitTwoWordsString("   Oscar  Manuel  "))
		.containsOnly("Oscar", "Manuel");
		
		assertThat(HelperMethods.splitTwoWordsString("Manuel"))
			.containsOnly("Manuel");
		
		assertThat(HelperMethods.splitTwoWordsString(" Manuel"))
			.containsOnly("Manuel");
		
		assertThrows(InvalidArgumentException.class, () -> {
			HelperMethods.splitTwoWordsString("");
		});
		
		assertThrows(InvalidArgumentException.class, () -> {
			HelperMethods.splitTwoWordsString(null);
		});
		
		assertThrows(InvalidArgumentException.class, () -> {
			HelperMethods.splitTwoWordsString("Juan Manuel Gutierrez");
		});
	}

}
