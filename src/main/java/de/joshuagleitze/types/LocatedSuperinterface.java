package de.joshuagleitze.types;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.AnnotatedType;

import static de.joshuagleitze.types.TypeFormatting.appendFormatted;

/// A type use by a declaration of a superinterface of a [Class]. This can either be in the `implements …` clause of a class declaration or
/// the `extends …` clause of an interface declaration. For example, `List<@Nullable T>` in `class NullList<T> implements List<@Nullable
/// T>`.
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class LocatedSuperinterface extends BaseLocatedTypeUse {
	private final Class<?> declaringClass;
	private final AnnotatedType superInterfaceType;

	/// The class or interface declaring the superinterface.
	@Override
	public Class<?> getDeclaration() {
		return declaringClass;
	}

	@Override
	protected AnnotatedType underlyingType() {
		return superInterfaceType;
	}

	@Override
	void appendTo(StringBuilder result) {
		result.append(declaringClass.isInterface() ? "extends " : "implements ");
		appendFormatted(result, superInterfaceType);
		result.append(" of ");
		appendFormatted(result, declaringClass);
	}
}
