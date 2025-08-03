package de.joshuagleitze.types;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.AnnotatedType;

import static de.joshuagleitze.types.TypeFormatting.appendFormatted;

/// Type use by an outer type declaration within a [LocatedTypeUse]. For example, `Outer` in `Outer.@Nullable Inner`.
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class LocatedOuterType extends BaseLocatedTypeUse {
	private final LocatedTypeUse fullType;
	@EqualsAndHashCode.Exclude
	private final AnnotatedType outerType;

	/// The [LocatedTypeUse] containing this type use as an outer type qualifying an inner type.
	@Override
	public LocatedTypeUse getDeclaration() {
		return fullType;
	}

	@Override
	protected AnnotatedType underlyingType() {
		return outerType;
	}

	@Override
	void appendTo(StringBuilder result) {
		appendFormatted(result, outerType);
		result.append(" in ");
		appendFormatted(result, fullType);
	}
}
