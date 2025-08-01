package de.joshuagleitze.types;

import lombok.experimental.Delegate;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.util.List;

import static de.joshuagleitze.types.TypeFormatting.appendFormatted;
import static de.joshuagleitze.types.LocatedTypeUses.componentsOf;

/// A type use by a return type of a [Method]. For example, `@Nullable String` in `@Nullable String getName()`.
public record LocatedReturnType(Method method) implements LocatedTypeUse {
	@Delegate
	private AnnotatedType annotatedType() {
		return method.getAnnotatedReturnType();
	}

	/// The [Method] declaring this type use as its return type.
	@Override
	public Method declaration() {
		return method;
	}

	@Override
	public List<LocatedTypeComponent> components() {
		return componentsOf(this, annotatedType());
	}

	@Override
	public @Nullable LocatedOuterType getAnnotatedOwnerType() {
		return LocatedTypeUse.super.getAnnotatedOwnerType();
	}

	@Override
	public String toString() {
		var result = new StringBuilder();
		appendFormatted(result, annotatedType());
		result.append(" returned by ");
		appendFormatted(result, method);

		return result.toString();
	}
}
