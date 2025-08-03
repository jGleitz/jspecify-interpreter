package de.joshuagleitze.jspecify.interpreter.examples.nullmarked;

import org.jspecify.annotations.Nullable;

import java.util.List;

@SuppressWarnings("unused")
public interface ExplicitlyNullable<T> {
	@Nullable
	String nullableString(@Nullable String input);

	@Nullable
	T nullableTypeVariable(@Nullable T input);

	T @Nullable [] nullableArray(T @Nullable [] input);

	interface Components<T> {

		List<@Nullable String> listOfNullableString(List<@Nullable String> input);

		List<@Nullable T> listOfNullableTypeVariable(List<@Nullable T> input);

		@Nullable
		String[] arrayOfNullableString(@Nullable String[] input);

		@Nullable
		T[] arrayOfNullableTypeVariable(@Nullable T[] input);
	}
}
