package gregtech.api;

public class RecipeDebugConstants {
	public static final int ASSUMED_MAX_INPUT_STACK_SIZE = 16;
	public static final int ASSUMED_MAX_INPUT_FLUID_SIZE = 8;
	public static final int ASSUMED_MAX_OUTPUT_STACK_SIZE = 9;
	public static final int ASSUMED_MAX_OUTPUT_FLUID_SIZE = 8;
	public static final String CSV_HEADER;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("\"RecipeMapIdentifier\",\"old EU/t\",\"old duration\",\"old special value\"");
		for (int i = 0; i < ASSUMED_MAX_INPUT_STACK_SIZE; i++) {
			add(sb, "old input stack ", i, " name");
			add(sb, "old input stack ", i, " meta");
			add(sb, "old input stack ", i, " size");
		}
		for (int i = 0; i < ASSUMED_MAX_INPUT_FLUID_SIZE; i++) {
			add(sb, "old input fluid ", i, " name");
			add(sb, "old input fluid ", i, " size");
		}
		for (int i = 0; i < ASSUMED_MAX_OUTPUT_STACK_SIZE; i++) {
			add(sb, "old output stack ", i, " name");
			add(sb, "old output stack ", i, " meta");
			add(sb, "old output stack ", i, " size");
		}
		for (int i = 0; i < ASSUMED_MAX_OUTPUT_FLUID_SIZE; i++) {
			add(sb, "old outut fluid ", i, " name");
			add(sb, "old outut fluid ", i, " size");
		}
		sb.append(",\"new EU/t\",\"new duration\",\"new special value\"");
		for (int i = 0; i < ASSUMED_MAX_INPUT_STACK_SIZE; i++) {
			add(sb, "new input stack ", i, " name");
			add(sb, "new input stack ", i, " meta");
			add(sb, "new input stack ", i, " size");
		}
		for (int i = 0; i < ASSUMED_MAX_INPUT_FLUID_SIZE; i++) {
			add(sb, "new input fluid ", i, " name");
			add(sb, "new input fluid ", i, " size");
		}
		for (int i = 0; i < ASSUMED_MAX_OUTPUT_STACK_SIZE; i++) {
			add(sb, "new output stack ", i, " name");
			add(sb, "new output stack ", i, " meta");
			add(sb, "new output stack ", i, " size");
		}
		for (int i = 0; i < ASSUMED_MAX_OUTPUT_FLUID_SIZE; i++) {
			add(sb, "new outut fluid ", i, " name");
			add(sb, "new outut fluid ", i, " size");
		}
		sb.append(",\"new recipe adder stack trace with # as delimiter\",\"old recipe adder stack trace with # as delimiter\"");
		CSV_HEADER = sb.toString();
	}

	private static void add(StringBuilder sb, String prefix, int index, String suffix) {
		sb.append(",\"").append(prefix).append(index).append(suffix).append('"');
	}
}
