package de.joshuagleitze.jspecify.interpreter;

import java.lang.reflect.AnnotatedType;

public class JSpecify implements JSpecifyInterpreter {
    @Override
    public NullnessOperator nullabilityOf(AnnotatedType type) {
        if (isIntrinsicallyNonNull(type)) {
            return NullnessOperator.MINUS_NULL;
        }
        return NullnessOperator.NO_CHANGE;
    }

    /// In [Recognized locations for type-use annotations](https://jspecify.dev/docs/spec/#recognized-type-use):
    /// > However, the type-use annotation is unrecognized in any of the following cases:
    /// >
    /// > * a type usage of a primitive type, since those are intrinsically non-nullable
    /// >
    /// > * any component of a return type in an annotation interface, since those are intrinsically non-nullable
    /// >
    /// >
    /// > \[…\]
    /// >
    /// > some additional intrinsically non-nullable locations:
    /// >
    /// > * supertype in a class declaration
    /// >
    /// > \[…\]
    /// >
    /// > * outer type qualifying an inner type
    private static boolean isIntrinsicallyNonNull(AnnotatedType type) {
        return isPrimitive(type) || isComponentOfAnnotationMethodReturnType(type);
    }

    private static boolean isPrimitive(AnnotatedType type) {
        return type.getType() instanceof Class<?> clazz && clazz.isPrimitive();
    }

    private static boolean isComponentOfAnnotationMethodReturnType(AnnotatedType type) {
        return false;
    }
}
