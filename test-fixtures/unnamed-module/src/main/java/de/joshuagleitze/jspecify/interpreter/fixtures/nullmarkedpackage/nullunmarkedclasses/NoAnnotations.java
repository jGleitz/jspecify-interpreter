package de.joshuagleitze.jspecify.interpreter.fixtures.nullmarkedpackage.nullunmarkedclasses;

import org.jspecify.annotations.NullUnmarked;

import java.util.List;

@SuppressWarnings("unused")
@NullUnmarked
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
