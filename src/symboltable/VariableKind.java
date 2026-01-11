public enum VariableKind {
    LOCAL(0),
    GLOBAL(1),
    CLASS_FIELD(2),
	PARAMETER(3);

	private final int value;
	private VariableKind(int value) {
		this.value = value;
	}
}