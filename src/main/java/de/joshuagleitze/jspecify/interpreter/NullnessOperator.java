package de.joshuagleitze.jspecify.interpreter;

/// The possibilities for nullness of a type, as [introduced by JSpecify’s Nullness Specification](https://jspecify.dev/docs/spec/#nullness-operator).
public enum NullnessOperator {
    /// The type can be `null`.
    ///
    /// > This is the operator produced by putting [`@Nullable`][org.jspecify.annotations.Nullable] on a type usage.
    /// >
    /// > * The type usage `String UNION_NULL` includes `"a"`, `"b"`, `"ab"`, etc., plus `null`.
    /// > * The type-variable usage `T UNION_NULL` includes all members of the type argument substituted in for `T``, plus `null` if it was not already included.
    UNION_NULL,
    ///  The type cannot be `null`.
    ///
    /// > This is the operator produced by putting [`@NonNull`][org.jspecify.annotations.NonNull] on a type usage.
    /// >
    /// > * The type usage `String MINUS_NULL` includes `"a"`, `"b"`, `"ab"`, etc., without including `null`.
    /// > * The type-variable usage `T MINUS_NULL` includes all members of the type argument substituted in for `T` except that it does not include `null` even when the type argument does.
    MINUS_NULL,
    /// Whether the type can be `null` depends on how it is used.
    ///
    /// > This operator is important on type-variable usages, where it means that the nullness of the type comes from the type argument.
    /// >
    /// > * The type usage `String NO_CHANGE` includes `"a"`, `"b"`, `"ab"`, etc., without including `null`. (This is equivalent to `String MINUS_NULL`.)
    /// > * The type-variable usage `T NO_CHANGE` includes exactly the members of the type argument substituted in for `T`: If `null` was a member of the type argument, then it is a member of `T NO_CHANGE`. If it was not a member of the type argument, then it is not a member of `T NO_CHANGE`.
    NO_CHANGE,
    /// It is not specified whether the type can be `null` or not.
    ///
    /// >  This is the operator produced by "completely unannotated code"—outside a [null-marked scope](https://jspecify.dev/docs/spec/#null-marked-scope) and with no annotation on the type.
    /// >
    /// > * The type usage `String UNSPECIFIED` includes `"a"`, `"b"`, `"ab"`, etc., but whether `null` should be included is not specified.
    /// > * The type-variable usage `T UNSPECIFIED` includes all members of `T`, except that there is no specification of whether `null` should be added to the set (if it is not already a member), removed (if it is already a member), or included only when the substituted type argument includes it.
    UNSPECIFIED
}
