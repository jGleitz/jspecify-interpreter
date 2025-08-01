package de.joshuagleitze.jspecify.interpreter.examples;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

@SuppressWarnings("NullableProblems")
public interface InterfaceWithSuperInterfaces
		extends Comparable<@Nullable String>, @Nullable Iterable<@Nullable List<@Nullable String>>, @NonNull AutoCloseable {}
