package de.joshuagleitze.jspecify.interpreter.fixtures.nullmarkedmodule.nullunmarkedpackage;

@SuppressWarnings("unused")
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
