package de.joshuagleitze.types;

import lombok.EqualsAndHashCode;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;

import static de.joshuagleitze.types.TypeFormatting.appendFormatted;

/// A type use by a return type of a [Method]. For example, `@Nullable String` in `@Nullable String getName()`.
@EqualsAndHashCode(callSuper = false)
public final class LocatedReturnType extends BaseLocatedTypeUse {
	private final Method method;
	@EqualsAndHashCode.Exclude
	private final AnnotatedType returnType;

	public LocatedReturnType(Method method) {
		this.method = method;
		this.returnType = method.getAnnotatedReturnType();
	}

	/// The [Method] declaring this type use as its return type.
	@Override
	public Method getDeclaration() {
		return method;
	}

	@Override
	protected AnnotatedType underlyingType() {
		return returnType;
	}

	@Override
	void appendTo(StringBuilder result) {
		appendFormatted(result, returnType);
		result.append(" returned by ");
		appendFormatted(result, method);
	}
}
