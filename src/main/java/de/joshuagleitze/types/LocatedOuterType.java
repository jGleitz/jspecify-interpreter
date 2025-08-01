package de.joshuagleitze.types;

import lombok.experimental.Delegate;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.util.List;

import static de.joshuagleitze.types.LocatedTypeUses.componentsOf;
import static de.joshuagleitze.types.TypeFormatting.appendFormatted;

/// Type use by an outer type declaration within a [LocatedTypeUse]. For example, `Outer` in `Outer.@Nullable Inner`.
public record LocatedOuterType(LocatedTypeUse outerType, @Delegate AnnotatedType ownerType) implements LocatedTypeUse {
	/// The [LocatedTypeUse] including this type use as an owner type.
	@Override
	public AnnotatedElement declaration() {
		return outerType;
	}

	@Override
	public List<LocatedTypeComponent> components() {
		return componentsOf(this, ownerType);
	}

	@Override
	public @Nullable LocatedOuterType getAnnotatedOwnerType() {
		return LocatedTypeUse.super.getAnnotatedOwnerType();
	}

	@Override
	public String toString() {
		var result = new StringBuilder();
		appendFormatted(result, ownerType);
		result.append(" in ");
		appendFormatted(result, outerType);
		return result.toString();
	}
}
