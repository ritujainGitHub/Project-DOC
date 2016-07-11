import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class PDFParser {

	private final String PDF_FILE_PATH = "E:\\home\\RK.pdf";
	public static List<String> accountCodeList;

	public static void createAccountMap() {
		accountCodeList = new ArrayList<String>();
		accountCodeList.add("8Q5V");
		accountCodeList.add("8Q2L");
		accountCodeList.add("8Q5S");
		accountCodeList.add("8Q5R");
		accountCodeList.add("8Q1V");
		accountCodeList.add("8Q5N");
		accountCodeList.add("8Q5Q");
		accountCodeList.add("8Q5A");
		accountCodeList.add("8Q5B");
		accountCodeList.add("8Q5D");
		accountCodeList.add("8Q5E");
		accountCodeList.add("8Q5F");
		accountCodeList.add("8Q5G");
		accountCodeList.add("8Q5H");
		accountCodeList.add("8Q5J");
		accountCodeList.add("8Q5L");
		accountCodeList.add("8Q5M");
		accountCodeList.add("8Q5Y");
		accountCodeList.add("8Q5Z");
		accountCodeList.add("8Q5X");
		accountCodeList.add("8Q5U");
		accountCodeList.add("8Q5I");

	}

	static {
		createAccountMap();
	}

	public static void main(String[] args) {
		PDFParser pdfParser = new PDFParser();
		pdfParser.parsePDF();
	}

	private void parsePDF() {

		String pageText = "";
		String ExtractedText = null;
		PdfReader reader = null;
		PdfReaderContentParser parser = null;
		TextExtractionStrategy strategy = null;

		try {
			reader = new PdfReader(PDF_FILE_PATH);
			parser = new PdfReaderContentParser(reader);

			int pages = reader.getNumberOfPages();
			String reportDate = "";
			List<FundValue> fundValueList = new ArrayList<FundValue>();

			for (int pageCount = 1; pageCount <= pages; pageCount++) {
				strategy = parser.processContent(pageCount,
						new SimpleTextExtractionStrategy());
				ExtractedText = strategy.getResultantText().toString();
				pageText = ExtractedText;
				if (pageCount == 1) {
					reportDate = getReportDate(pageText);
				}
				prepareReport(fundValueList, pageText);
				pageText = "";
			}

			System.out.println("Report date --->" + reportDate);

			System.out.println("Fund Values --->\n");
			for (FundValue fundValue : fundValueList) {
				System.out.println("Fund Code : " + fundValue.getFundCode()
						+ ", Fund Value : " + fundValue.getFundValue());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			parser = null;
			if (reader != null) {
				reader.close();
			}
		}
	}

	private String getReportDate(String pageText) {
		String date = null;

		String[] pageLines = pageText.toString().split(
				PDFConstant.NEW_LINE_CHAR);

		for (String line : pageLines) {
			int lineIndex = line.indexOf(PDFConstant.DATE_LABEL);
			if (lineIndex > -1) {
				date = line.substring(lineIndex + 9);
			}
		}

		return date;

	}

	public void prepareReport(List<FundValue> fundValueList, String pageText) {

		String[] pageLines = pageText.toString().split("\n");
		String fundCodeLine = "";
		String fundUnitsLine = "";

		boolean isDataCaptured = false;
		int fundCodeIndex = -1;
		int fundUnitIndex = -1;
		for (String line : pageLines) {
			fundCodeIndex = line.indexOf(PDFConstant.FUND_CODE_PREDECESSOR);

			if (fundCodeIndex > -1) {
				fundCodeLine = line;
			}

			fundUnitIndex = line.indexOf(PDFConstant.FUND_UNIT_PREDECESSOR);

			if (fundUnitIndex > -1) {
				fundUnitsLine = line;
			}

			if (!"".equals(fundCodeLine) && !"".equals(fundUnitsLine)) {
				isDataCaptured = true;
				break;
			}
		}

		if (isDataCaptured) {
			List<String> parsedFunds = new ArrayList<String>();

			String processedFundLine = PDFUtil.trimLeft(fundCodeLine
					.substring(fundCodeIndex
							+ PDFConstant.FUND_CODE_PREDECESSOR.length()));
			for (String code : accountCodeList) {
				if (processedFundLine.indexOf(code) > -1) {
					parsedFunds.add(code);
				}
			}

			int fundUnitStartIndex = fundUnitIndex
					+ PDFConstant.FUND_UNIT_PREDECESSOR.length();
			for (String fundCode : parsedFunds) {
				int fundUnitLastIndex = fundCodeLine.indexOf(fundCode)
						+ fundCode.length();
				String fundUnit = "";
				if (fundUnitsLine.length() >= fundUnitLastIndex) {
					fundUnit = PDFUtil.trimLeft(fundUnitsLine.substring(
							fundUnitStartIndex, fundUnitLastIndex));
				}
				FundValue fundValue = new FundValue(fundCode, fundUnit);
				fundValueList.add(fundValue);

				fundUnitStartIndex = fundUnitLastIndex + 1;
			}
		}
	}

}
