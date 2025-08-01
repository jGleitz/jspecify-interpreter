package de.joshuagleitze.jspecify.interpreter;

import java.lang.reflect.AnnotatedType;

public interface JSpecifyInterpreter {
    /// Determines the [nullability][NullnessOperator] of `type`.
    ///
    /// Using the terminology of [JSpecify’s Nullness Specification](https://jspecify.dev/docs/spec/), this method
    /// returns the [Nullness Operator](https://jspecify.dev/docs/spec/#nullness-operator) of the
    /// [augmented type](https://jspecify.dev/docs/spec/#augmented-type) represented by `type`.
    NullnessOperator nullabilityOf(AnnotatedType type);
}
