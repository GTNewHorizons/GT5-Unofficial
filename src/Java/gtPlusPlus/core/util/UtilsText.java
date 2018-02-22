package gtPlusPlus.core.util;

public enum UtilsText {

	blue('1'), green('2'), teal('3'), maroon('4'), purple('5'), orange('6'), lightGray('7'), darkGray('8'), lightBlue(
			'9'), black('0'), lime('a'), aqua('b'), red('c'), pink('d'), yellow('e'), white('f');

	private char colourValue;

	private UtilsText(final char value) {
		this.colourValue = value;
	}

	public String colour() {
		return "§" + this.colourValue;
	}

}
