package de.joshuagleitze.types;

import lombok.experimental.Delegate;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.AnnotatedType;
import java.util.List;

import static de.joshuagleitze.types.TypeFormatting.appendFormatted;
import static de.joshuagleitze.types.LocatedTypeUses.componentsOf;

/// A type use by a declaration of a superclass of a [Class]. For example, `ArrayList<@Nullable T>` in
/// `class NullList<T> extends ArrayList<@Nullable T>`.
///
/// @param declaration   The class declaring the superclass.
/// @param extendedClass The extended class.
public record LocatedSuperclass(Class<?> declaration, @Delegate AnnotatedType extendedClass) implements LocatedTypeUse {

	public LocatedSuperclass(Class<?> declaration) {
		this(declaration, validateExtendedClass(declaration));
	}

	@Override
	public List<LocatedTypeComponent> components() {
		return componentsOf(this, extendedClass);
	}

	private static AnnotatedType validateExtendedClass(Class<?> declaration) {
		var superclass = declaration.getAnnotatedSuperclass();
		if (superclass == null) {
			throw new IllegalArgumentException(declaration.getName() + " does not have a superclass!");
		}
		return superclass;
	}

	@Override
	public @Nullable LocatedOuterType getAnnotatedOwnerType() {
		return LocatedTypeUse.super.getAnnotatedOwnerType();
	}

	@Override
	public String toString() {
		var result = new StringBuilder();
		result.append("extends ");
		appendFormatted(result, extendedClass);
		result.append(" in ");
		result.append(declaration.getSimpleName());
		return result.toString();
	}
}
