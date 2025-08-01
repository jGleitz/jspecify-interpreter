package de.joshuagleitze.types;

import lombok.experimental.Delegate;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.AnnotatedType;
import java.util.List;

import static de.joshuagleitze.types.LocatedTypeUses.componentsOf;

/// A type use by a declaration of a superinterface of a [Class]. This can either be in the `implements …` clause of a class declaration or
/// the `extends …` clause of an interface declaration. For example, `List<@Nullable T>` in
/// `class NullList<T> implements List<@Nullable T>`.
///
/// @param declaration       The class declaring the superinterface.
/// @param extendedInterface The extended interface.
public record LocatedSuperinterface(Class<?> declaration, @Delegate AnnotatedType extendedInterface) implements LocatedTypeUse {
	@Override
	public List<LocatedTypeComponent> components() {
		return componentsOf(this, extendedInterface);
	}

	@Override
	public @Nullable LocatedOuterType getAnnotatedOwnerType() {
		return LocatedTypeUse.super.getAnnotatedOwnerType();
	}

	@Override
	public String toString() {
		var result = new StringBuilder();
		result.append(declaration.isInterface() ? "extends " : "implements ");
		TypeFormatting.appendFormatted(result, extendedInterface);
		result.append(" in ");
		result.append(declaration.getSimpleName());
		return result.toString();
	}
}
