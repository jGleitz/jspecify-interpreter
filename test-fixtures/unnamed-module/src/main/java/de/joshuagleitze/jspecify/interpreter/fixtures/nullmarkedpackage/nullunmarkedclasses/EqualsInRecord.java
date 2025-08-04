package de.joshuagleitze.jspecify.interpreter.fixtures.nullmarkedpackage.nullunmarkedclasses;

import org.jspecify.annotations.NullUnmarked;

@SuppressWarnings("unused")
@NullUnmarked
public record EqualsInRecord() {
	public boolean equals(Object a, Object b) {
		return true;
	}

	public boolean equals(String c) {
		return false;
	}

	public boolean equals() {
		return false;
	}
}
