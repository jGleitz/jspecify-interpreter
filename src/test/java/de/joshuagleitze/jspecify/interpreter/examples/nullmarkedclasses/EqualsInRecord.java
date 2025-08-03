package de.joshuagleitze.jspecify.interpreter.examples.nullmarkedclasses;

import org.jspecify.annotations.NullMarked;

@SuppressWarnings("unused")
@NullMarked
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
