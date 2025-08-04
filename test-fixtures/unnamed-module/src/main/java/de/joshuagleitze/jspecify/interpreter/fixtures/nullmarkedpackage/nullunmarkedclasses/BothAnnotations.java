package de.joshuagleitze.jspecify.interpreter.fixtures.nullmarkedpackage.nullunmarkedclasses;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@SuppressWarnings({"unused", "NullableProblems"})
@NullUnmarked
public interface BothAnnotations<T> {
	@NonNull
	@Nullable
	String nullableString(@NonNull @Nullable String input);

	@NonNull
	@Nullable
	T nullableTypeVariable(@NonNull @Nullable T input);

	T @NonNull @Nullable [] nullableArray(T @NonNull @Nullable [] input);

	interface Components<T> {
		List<@NonNull @Nullable String> listOfNullableString(List<@NonNull @Nullable String> input);

		List<@NonNull @Nullable T> listOfNullableTypeVariable(List<@NonNull @Nullable T> input);

		@NonNull
		@Nullable
		String[] arrayOfNullableString(@NonNull @Nullable String[] input);

		@NonNull
		@Nullable
		T[] arrayOfNullableTypeVariable(@NonNull @Nullable T[] input);
	}
}
