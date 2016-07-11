public class FundValue {

	private String fundCode;
	private String fundValue;

	public FundValue(String fundCode, String fundValue) {
		super();
		this.fundCode = fundCode;
		this.fundValue = fundValue;
	}

	@Override
	public String toString() {
		return "FundValue [fundCode=" + fundCode + ", fundValue=" + fundValue
				+ "]\n";
	}

	public String getFundCode() {
		return fundCode;
	}

	public String getFundValue() {
		return fundValue;
	}

}
