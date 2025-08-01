package de.joshuagleitze.jspecify.interpreter;

import de.joshuagleitze.types.LocatedTypeUse;

public interface JSpecifyInterpreter {
    /// Determines the [nullability][NullnessOperator] of `type`.
    ///
    /// Using the terminology of [JSpecify’s Nullness Specification](https://jspecify.dev/docs/spec/), this method
    /// returns the [Nullness Operator](https://jspecify.dev/docs/spec/#nullness-operator) of the
    /// [augmented type](https://jspecify.dev/docs/spec/#augmented-type) represented by `type`.
    ///
    /// This method assumes that JSpecify’s annotations have been applied correctly. Its results may be wrong if this
    /// is not the case. For example, this method may return [`UNION_NULL`][NullnessOperator#UNION_NULL] for a
    /// `@Nullable int` because [`@Nullable`][org.jspecify.annotations.Nullable] is not
    /// [recognized](https://jspecify.dev/docs/spec/#recognized-type-use) on primitive type uses.
    NullnessOperator nullabilityOf(LocatedTypeUse type);
}
