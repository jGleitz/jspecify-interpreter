package de.joshuagleitze.jspecify.interpreter;

import de.joshuagleitze.types.*;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;

import static de.joshuagleitze.jspecify.interpreter.NullnessOperator.*;

public class JSpecify implements JSpecifyInterpreter {
	@Override
	public NullnessOperator nullabilityOf(LocatedTypeUse type) {
		// following along [Augmented type of a type usage appearing in code](https://jspecify.dev/docs/spec/#augmented-type-of-usage):

		// > To determine the nullness operator in a _recognized_ location, apply the following rules in order.
		// > Once one condition is met, skip the remaining conditions.

		// (non-normative):
		// > If the type usage is in an intrinsically non-null location listed earlier, its nullness operator is MINUS_NULL. For other
		// > unrecognized locations, no nullness operator is applicable.
		if (isIntrinsicallyNonNullable(type)) return MINUS_NULL;
			// > If the type usage is annotated with @Nullable and not with @NonNull, its nullness operator is UNION_NULL.
		else if (isExplicitlyNullable(type)) return UNION_NULL;
			// > If the type usage is annotated with @NonNull and not with @Nullable, its nullness operator is MINUS_NULL.
		else if (isExplicitlyNonNull(type)) return MINUS_NULL;
			// > If the type usage is the parameter of equals(Object) in a subclass of java.lang.Record, then its nullness operator is UNION_NULL.
		else if (isParameterTypeOfRecordEquals(type)) return UNION_NULL;
			// > If the type usage appears in a null-marked scope, its nullness operator is NO_CHANGE.
		else if (isInNullMarkedScope(type)) return NO_CHANGE;
			// > Its nullness operator is UNSPECIFIED.
		else return UNSPECIFIED;
	}

	private static boolean isIntrinsicallyNonNullable(LocatedTypeUse type) {
		// following along [Recognized locations for type-use annotations](https://jspecify.dev/docs/spec/#recognized-type-use):

		// > However, the type-use annotation is unrecognized in any of the following cases:
		return
				// > * a type usage of a primitive type, since those are intrinsically non-nullable
				isPrimitive(type)
						// > * any component of a return type in an annotation interface, since those are intrinsically non-nullable
						|| isComponentOfReturnTypeInAnnotationInterface(type)
						// > some additional intrinsically non-nullable locations:
						// > * supertype in a class declaration
						|| type instanceof LocatedSuperclass
						|| type instanceof LocatedSuperinterface
						// > * outer type qualifying an inner type
						|| type instanceof LocatedOuterType;
	}

	private static boolean isPrimitive(AnnotatedType type) {
		return type.getType() instanceof Class<?> clazz && clazz.isPrimitive();
	}

	private static boolean isComponentOfReturnTypeInAnnotationInterface(LocatedTypeUse type) {
		return (
				type instanceof LocatedTypeComponent typeComponent
						&& isComponentOfReturnTypeInAnnotationInterface(typeComponent.outerType())
		) || (
				type instanceof LocatedReturnType returnType
						&& returnType.declaration().getDeclaringClass().isAnnotation()
		);
	}

	private static boolean isExplicitlyNullable(AnnotatedType type) {
		return type.isAnnotationPresent(Nullable.class) && !type.isAnnotationPresent(NonNull.class);
	}

	private static boolean isExplicitlyNonNull(AnnotatedType type) {
		return type.isAnnotationPresent(NonNull.class) && !type.isAnnotationPresent(Nullable.class);
	}

	private static boolean isParameterTypeOfRecordEquals(LocatedTypeUse type) {
		return type instanceof LocatedParameterType parameterType
				&& parameterType.declaration().getDeclaringExecutable() instanceof Method declaringMethod
				&& declaringMethod.getDeclaringClass().isRecord()
				&& declaringMethod.getName().equals("equals")
				&& declaringMethod.getParameterCount() == 1
				&& type.getType().equals(Object.class);
	}

	private boolean isInNullMarkedScope(LocatedTypeUse type) {
		return false;
	}
}
