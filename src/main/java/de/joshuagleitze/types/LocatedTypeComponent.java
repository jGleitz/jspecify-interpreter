package de.joshuagleitze.types;

import lombok.experimental.Delegate;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.util.List;

import static de.joshuagleitze.types.TypeFormatting.appendFormatted;
import static de.joshuagleitze.types.LocatedTypeUses.componentsOf;

/// A type use by a component of an [AnnotatedParameterizedType] or an [AnnotatedArrayType]. For example, `@Nullable String` in
/// `List<@Nullable String>` or `@Nullable String[]`.
public record LocatedTypeComponent(
		LocatedTypeUse outerType,
		@Delegate AnnotatedType component
) implements LocatedTypeUse {
	/// The type use this is a component of.
	@Override
	public LocatedTypeUse declaration() {
		return outerType;
	}

	@Override
	public List<LocatedTypeComponent> components() {
		return componentsOf(this, component);
	}

	@Override
	public @Nullable LocatedOuterType getAnnotatedOwnerType() {
		return LocatedTypeUse.super.getAnnotatedOwnerType();
	}

	@Override
	public String toString() {
		var result = new StringBuilder();
		appendFormatted(result, component);
		result.append(" in ");
		result.append(outerType);
		return result.toString();
	}
}
