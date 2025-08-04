package de.joshuagleitze.types;

import lombok.EqualsAndHashCode;

import java.lang.reflect.AnnotatedType;

import static de.joshuagleitze.types.TypeFormatting.appendFormatted;
import static de.joshuagleitze.types.TypeFormatting.appendFormattedMarked;

/// A type use by a declaration of a superclass of a [Class]. For example, `ArrayList<@Nullable T>` in `class NullList<T> extends
/// ArrayList<@Nullable T>`.
@EqualsAndHashCode(callSuper = false)
public final class LocatedSuperclass extends BaseLocatedTypeUse {
	private final Class<?> declaringClass;
	@EqualsAndHashCode.Exclude
	private final AnnotatedType superClassType;

	public LocatedSuperclass(Class<?> declaringClass) {
		var superClass = declaringClass.getAnnotatedSuperclass();
		if (superClass == null) {
			throw new IllegalArgumentException(declaringClass.getName() + " does not have a superclass!");
		}
		this.declaringClass = declaringClass;
		this.superClassType = superClass;
	}

	/// The class declaring the superclass.
	@Override
	public Class<?> getDeclaration() {
		return declaringClass;
	}

	@Override
	protected AnnotatedType underlyingType() {
		return superClassType;
	}

	@Override
	void appendTo(StringBuilder result) {
		result.append("extends ");
		appendFormattedMarked(result, superClassType);
		result.append(" of ");
		result.append(declaringClass.getSimpleName());
	}
}
