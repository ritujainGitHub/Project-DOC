public class PDFUtil {

	

	public static String trimLeft(String string) {
		return trimLeft(string, PDFConstant.DEFAULT_TRIM_WHITESPACE);
	}

	public static String trimLeft(final String string, final char trimChar) {
		final int stringLength = string.length();
		int i;

		for (i = 0; i < stringLength && string.charAt(i) == trimChar; i++) {
		}

		if (i == 0) {
			return string;
		} else {
			return string.substring(i);
		}
	}

}
