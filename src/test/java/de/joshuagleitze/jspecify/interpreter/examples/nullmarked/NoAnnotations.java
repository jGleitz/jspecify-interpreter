package de.joshuagleitze.jspecify.interpreter.examples.nullmarked;

import java.util.List;

@SuppressWarnings("unused")
public interface NoAnnotations<T> {
	String nullableString(String input);

	T nullableTypeVariable(T input);

	T[] nullableArray(T[] input);

	interface Components<T> {
		List<String> listOfNullableString(List<String> input);

		List<T> listOfNullableTypeVariable(List<T> input);


		String[] arrayOfNullableString(String[] input);


		T[] arrayOfNullableTypeVariable(T[] input);
	}
}
