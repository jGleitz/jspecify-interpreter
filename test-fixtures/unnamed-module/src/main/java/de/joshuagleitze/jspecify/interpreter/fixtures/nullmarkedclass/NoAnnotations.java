package de.joshuagleitze.jspecify.interpreter.fixtures.nullmarkedclass;

import java.util.List;

@SuppressWarnings("unused")
@org.jspecify.annotations.NullMarked
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
