package de.joshuagleitze.jspecify.interpreter.examples;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.lang.annotation.Annotation;

public @interface TestAnnotation {
    String noAnnotationString();

    @NonNull String nonNullString();

    @Nullable String nullableString();

    Class<? extends Annotation>[] noAnnotationClassArray();

    @Nullable Class<? extends @Nullable Annotation> @Nullable [] nullableClassArray();

    @NonNull Class<? extends @NonNull Annotation> @NonNull [] nonNullClassArray();
}
