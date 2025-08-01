package de.joshuagleitze.jspecify.interpreter.examples;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;

@SuppressWarnings({"NullableProblems", "unused"})
public class ClassesWithSuperclasses {
	public static class Superclass extends ArrayList<String> {}

	public static class NullableSuperclass extends @Nullable ArrayList<@Nullable String> {}

	public static class NonNullSuperclass extends @NonNull ArrayList<@Nullable String> {}
}
