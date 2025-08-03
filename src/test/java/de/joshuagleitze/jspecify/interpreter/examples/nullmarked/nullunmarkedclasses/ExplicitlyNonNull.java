package de.joshuagleitze.jspecify.interpreter.examples.nullmarked.nullunmarkedclasses;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullUnmarked;

import java.util.List;

@SuppressWarnings("unused")
@NullUnmarked
public interface ExplicitlyNonNull<T> {
	@NonNull
	String nullableString(@NonNull String input);

	@NonNull
	T nullableTypeVariable(@NonNull T input);

	T @NonNull [] nullableArray(T @NonNull [] input);

	interface Components<T> {
		List<@NonNull String> listOfNullableString(List<@NonNull String> input);

		List<@NonNull T> listOfNullableTypeVariable(List<@NonNull T> input);

		@NonNull
		String[] arrayOfNullableString(@NonNull String[] input);

		@NonNull
		T[] arrayOfNullableTypeVariable(@NonNull T[] input);
	}
}
