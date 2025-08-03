package de.joshuagleitze.jspecify.interpreter.examples;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

@SuppressWarnings({"NullableProblems", "unused"})
public abstract class OuterClass<T extends @Nullable Object> {
	public static class InnerStaticClass {}

	@SuppressWarnings("InnerClassMayBeStatic")
	public class InnerClass {}

	public abstract OuterClass.InnerStaticClass innerStaticClass();

	public abstract OuterClass<@Nullable List<@Nullable String>>.InnerClass innerClass();

	public abstract @Nullable OuterClass<@Nullable List<@Nullable String>>.InnerClass innerClassWithNullableOuter();

	public abstract @NonNull OuterClass<@Nullable List<@Nullable String>>.InnerClass innerClassWithNonNullOuter();
}
