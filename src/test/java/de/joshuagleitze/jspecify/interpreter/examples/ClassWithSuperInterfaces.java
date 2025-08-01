package de.joshuagleitze.jspecify.interpreter.examples;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.*;

@SuppressWarnings("NullableProblems")
public abstract class ClassWithSuperInterfaces
		implements Comparable<@Nullable String>, @Nullable Iterable<@Nullable List<@Nullable String>>, @NonNull AutoCloseable {}
