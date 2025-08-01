package de.joshuagleitze.types;

import lombok.experimental.Delegate;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Parameter;
import java.util.List;

import static de.joshuagleitze.types.TypeFormatting.appendFormatted;
import static de.joshuagleitze.types.LocatedTypeUses.componentsOf;

/// A type use by a [Parameter]. For example, `@Nullable String` in `void isFriend(@Nullable String name)`.
public record LocatedParameterType(Parameter parameter) implements LocatedTypeUse {
	@Delegate
	private AnnotatedType annotatedType() {
		return parameter.getAnnotatedType();
	}

	/// The [Parameter] declaring this type use.
	@Override
	public Parameter declaration() {
		return parameter;
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
		result.append(' ');
		result.append(parameter.getName());
		result.append(" in ");
		appendFormatted(result, parameter.getDeclaringExecutable());
		return result.toString();
	}
}
