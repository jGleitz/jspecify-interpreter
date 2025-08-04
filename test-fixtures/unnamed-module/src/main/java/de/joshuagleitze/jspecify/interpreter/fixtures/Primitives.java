package de.joshuagleitze.jspecify.interpreter.fixtures;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@SuppressWarnings("NullableProblems")
public record Primitives(
		byte noAnnotationByte, @NonNull int nonNullByte, @Nullable int nullableByte,
		short noAnnotationShort, @NonNull short nonNullShort, @Nullable short nullableShort,
		int noAnnotationInt, @NonNull int nonNullInt, @Nullable int nullableInt,
		long noAnnotationLong, @NonNull long nonNullLong, @Nullable long nullableLong,
		float noAnnotationFloat, @NonNull float nonNullFloat, @Nullable float nullableFloat,
		double noAnnotationDouble, @NonNull double nonNullDouble, @Nullable double nullableDouble,
		boolean noAnnotationBoolean, @NonNull boolean nonNullBoolean, @Nullable boolean nullableBoolean,
		char noAnnotationChar, @NonNull char nonNullChar, @Nullable char nullableChar
) {
}
