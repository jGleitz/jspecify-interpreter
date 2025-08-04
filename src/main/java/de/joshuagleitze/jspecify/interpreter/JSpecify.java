package de.joshuagleitze.jspecify.interpreter;

import de.joshuagleitze.types.*;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Set;

import static de.joshuagleitze.jspecify.interpreter.NullnessOperator.*;
import static java.util.Collections.emptySet;

public class JSpecify implements JSpecifyInterpreter {
	private static final @Nullable Class<? extends Annotation> KOTLIN_METADATA = tryFindKotlinMetadataAnnotationClass();

	@Override
	public NullnessOperator nullabilityOf(LocatedTypeUse type) {
		// following along [Augmented type of a type usage appearing in code](https://jspecify.dev/docs/spec/#augmented-type-of-usage):

		// > To determine the nullness operator in a _recognized_ location, apply the following rules in order.
		// > Once one condition is met, skip the remaining conditions.

		// we don’t explicitly check for whether @NonNull and @NotNull are in recognized locations because we couldn’t find an unrecognised
		// location that was possible to express in a LocatedTypeUse and was not covered by isIntrinsicallyNonNullable.

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
						&& isComponentOfReturnTypeInAnnotationInterface(typeComponent.getDeclaration())
		) || (
				type instanceof LocatedReturnType returnType
						&& returnType.getDeclaration().getDeclaringClass().isAnnotation()
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
				&& parameterType.getDeclaration().getDeclaringExecutable() instanceof Method declaringMethod
				&& declaringMethod.getDeclaringClass().isRecord()
				&& declaringMethod.getName().equals("equals")
				&& declaringMethod.getParameterCount() == 1
				&& type.getType().equals(Object.class);
	}

	private static boolean isInNullMarkedScope(AnnotatedElement element) {
		// following along [Null-marked scope](https://jspecify.dev/docs/spec/#null-marked-scope):
		// At each declaration that is a recognized location, check the following rules in order:

		var recognisedAnnotations = recognisedDeclarationAnnotations(element);

		// > * If the declaration is annotated with @NullMarked and not with @NullUnmarked, the type usage is in a null-marked scope.
		if (isExplicitlyNullMarked(element, recognisedAnnotations)) {
			return true;
		} else if (
			// > * If the declaration is annotated with @NullUnmarked and not with @NullMarked, the type usage is not in a null-marked scope.
				isExplicitlyNullUnmarked(element, recognisedAnnotations)
						// > * If the declaration is a top-level class annotated with @kotlin.Metadata, then the type usage is not in a null-marked scope.
						|| isTopLevelClassWithKotlinMetadata(element)) {
			return false;
		}

		var enclosingElement = getEnclosingElement(element);
		if (enclosingElement != null) {
			// Iterate over all the declarations that enclose the type usage, starting from the innermost.
			return isInNullMarkedScope(enclosingElement);
		}

		// If none of the enclosing declarations meet any rule, then the type usage is not in a null-marked scope.
		return false;
	}

	private static @Nullable AnnotatedElement getEnclosingElement(AnnotatedElement element) {
		// following along [Null-marked scope](https://jspecify.dev/docs/spec/#null-marked-scope):
		// > "Enclosing" is defined as follows:

		// if I only could use a switch…
		if (element instanceof LocatedTypeUse locatedTypeUse) {
			return locatedTypeUse.getDeclaration();
		} else if (element instanceof Member classMember) {
			// > * Each class member is enclosed by a class.
			return classMember.getDeclaringClass();
		} else if (element instanceof Parameter parameter) {
			return parameter.getDeclaringExecutable();
		} else if (element instanceof Class<?> clazz) {
			// > * Each non-top-level class is enclosed by a class or class member.
			var outerClass = clazz.getEnclosingClass();
			if (outerClass != null) {
				return outerClass;
			}
			// > * Each top-level class is enclosed by a package.
			return new PackageInModule(clazz.getPackage(), clazz.getModule());
		} else if (element instanceof PackageInModule pkg) {
			// > * Each package may be enclosed by a module.
			var enclosingModule = pkg.getModule();
			if (enclosingModule.isNamed()) {
				return enclosingModule;
			}
			return null;
		} else if (element instanceof Module) {
			// > * Modules are not enclosed by anything.
			return null;
		}

		throw new UnsupportedOperationException(
				"Cannot determine the enclosing element of this unsupported element type: %s <%s>"
						.formatted(element.getClass().getSimpleName(), element)
		);
	}

	private static boolean isExplicitlyNullMarked(AnnotatedElement element, Set<Class<? extends Annotation>> recognisedAnnotations) {
		return isRecognisedAndPresent(element, NullMarked.class, recognisedAnnotations)
				&& !(isRecognisedAndPresent(element, NullUnmarked.class, recognisedAnnotations));
	}

	private static boolean isExplicitlyNullUnmarked(AnnotatedElement element, Set<Class<? extends Annotation>> recognisedAnnotations) {
		return isRecognisedAndPresent(element, NullUnmarked.class, recognisedAnnotations)
				&& !(isRecognisedAndPresent(element, NullMarked.class, recognisedAnnotations));
	}

	private static boolean isTopLevelClassWithKotlinMetadata(AnnotatedElement element) {
		return KOTLIN_METADATA != null
				&& element instanceof Class<?> clazz
				&& clazz.getDeclaringClass() != null
				&& element.isAnnotationPresent(KOTLIN_METADATA);
	}

	private static boolean isRecognisedAndPresent(
			AnnotatedElement element,
			Class<? extends Annotation> annotation,
			Set<Class<? extends Annotation>> recognisedAnnotations
	) {
		return recognisedAnnotations.contains(annotation) && element.isAnnotationPresent(annotation);
	}

	private static Set<Class<? extends Annotation>> recognisedDeclarationAnnotations(AnnotatedElement element) {
		// Following along [Recognized locations for declaration annotations](https://jspecify.dev/docs/spec/#recognized-declaration):

		// > Our declaration annotations are specified to be recognized when applied to the locations listed below:
		// > * A named class declaration.
		// > * A package declaration.
		// > * A module (for @NullMarked only, not @NullUnmarked) declaration.
		// > * A method or constructor declaration.

		// TODO: can we receive a Class<?> of an unnamed class?
		if (element instanceof Class<?>
				|| element instanceof PackageInModule
				|| element instanceof Executable) {
			return Set.of(NullMarked.class, NullUnmarked.class);
		} else if (element instanceof Module) {
			return Set.of(NullMarked.class);
		} else {
			return emptySet();
		}
	}

	@SuppressWarnings("unchecked")
	private static @Nullable Class<? extends Annotation> tryFindKotlinMetadataAnnotationClass() {
		try {
			var availableClass = JSpecify.class.getClassLoader().loadClass("kotlin.Metadata");
			if (availableClass.isAnnotation()) {
				return (Class<? extends Annotation>) availableClass;
			}
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
}
