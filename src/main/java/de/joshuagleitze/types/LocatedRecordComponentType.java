package de.joshuagleitze.types;

import lombok.EqualsAndHashCode;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.RecordComponent;

import static de.joshuagleitze.types.TypeFormatting.appendFormattedMarked;

/// A type use by a [RecordComponent]. For example, `@Nullable String` in `record Person(@Nullable String name)`.
@EqualsAndHashCode(callSuper = false)
public final class LocatedRecordComponentType extends BaseLocatedTypeUse {
	private final RecordComponent recordComponent;
	@EqualsAndHashCode.Exclude
	private final AnnotatedType recordComponentType;

	public LocatedRecordComponentType(RecordComponent recordComponent) {
		this.recordComponent = recordComponent;
		this.recordComponentType = recordComponent.getAnnotatedType();
	}

	/// The [RecordComponent] declaring this type use.
	@Override
	public RecordComponent getDeclaration() {
		return recordComponent;
	}

	@Override
	protected AnnotatedType underlyingType() {
		return recordComponentType;
	}

	@Override
	void appendTo(StringBuilder result) {
		appendFormattedMarked(result, recordComponentType);
		result.append(' ')
				.append(recordComponent.getName())
				.append(" of ")
				.append(recordComponent.getDeclaringRecord().getSimpleName());
	}
}
