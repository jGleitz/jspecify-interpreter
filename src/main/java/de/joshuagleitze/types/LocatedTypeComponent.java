package de.joshuagleitze.types;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;

import static de.joshuagleitze.types.TypeFormatting.appendFormatted;

/// A type use by a component of an [AnnotatedParameterizedType] or an [AnnotatedArrayType]. For example, `@Nullable String` in
/// `List<@Nullable String>` or `@Nullable String[]`.
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class LocatedTypeComponent extends BaseLocatedTypeUse {
	private final LocatedTypeUse outerType;
	private final AnnotatedType component;

	/// The type use this is a component of.
	@Override
	public LocatedTypeUse getDeclaration() {
		return outerType;
	}

	@Override
	protected AnnotatedType underlyingType() {
		return component;
	}

	@Override
	void appendTo(StringBuilder result) {
		appendFormatted(result, component);
		result.append(" in ");
		appendFormatted(result, outerType);
	}
}
