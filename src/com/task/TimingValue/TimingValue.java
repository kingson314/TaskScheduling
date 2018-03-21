package com.task.TimingValue;

/**
 * @Description:
 * @date Mar 18, 2014
 * @author:fgq
 */
public class TimingValue {
	private String id;
	private String symbol;
	// 价格类型，0表示开盘价，1表示收盘价，2表示最高价，3表示最低价
	private int priceType;
	// economic_data.id
	private String economicDataId;
	// 1分钟前差价(当前价与开始点价格差)
	private double valueMinuteBefore1;
	private double valueMinuteBefore2;
	private double valueMinuteBefore3;
	private double valueMinuteBefore4;
	private double valueMinuteBefore5;
	// 1分钟后差价(当前价与开始点价格差)
	private double valueMinuteAfter1;
	private double valueMinuteAfter2;
	private double valueMinuteAfter3;
	private double valueMinuteAfter4;
	private double valueMinuteAfter5;
	private double valueMinuteAfter6;
	private double valueMinuteAfter7;
	private double valueMinuteAfter8;
	private double valueMinuteAfter9;
	private double valueMinuteAfter10;
	private double valueMinuteAfter11;
	private double valueMinuteAfter12;
	private double valueMinuteAfter13;
	private double valueMinuteAfter14;
	private double valueMinuteAfter15;
	private double valueMinuteAfter16;
	private double valueMinuteAfter17;
	private double valueMinuteAfter18;
	private double valueMinuteAfter19;
	private double valueMinuteAfter20;
	private double valueMinuteAfter21;
	private double valueMinuteAfter22;
	private double valueMinuteAfter23;
	private double valueMinuteAfter24;
	private double valueMinuteAfter25;
	private double valueMinuteAfter26;
	private double valueMinuteAfter27;
	private double valueMinuteAfter28;
	private double valueMinuteAfter29;
	private double valueMinuteAfter30;
	private double valueMinuteAfter35;
	private double valueMinuteAfter40;
	private double valueMinuteAfter45;
	private double valueMinuteAfter50;
	private double valueMinuteAfter55;
	private double valueMinuteAfter60;
	private double valueMinuteAfter70;
	private double valueMinuteAfter80;
	private double valueMinuteAfter90;
	private double valueMinuteAfter100;
	private double valueMinuteAfter110;
	private double valueMinuteAfter120;

	public TimingValue(String symbol, String economicDataId,int priceType) {
		this.symbol = symbol;
		this.economicDataId = economicDataId;
		this.priceType = priceType;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public int getPriceType() {
		return priceType;
	}

	public void setPriceType(int priceType) {
		this.priceType = priceType;
	}
	public double getValueMinuteAfter1() {
		return valueMinuteAfter1;
	}

	public void setValueMinuteAfter1(double valueMinuteAfter1) {
		this.valueMinuteAfter1 = valueMinuteAfter1;
	}

	public double getValueMinuteAfter2() {
		return valueMinuteAfter2;
	}

	public void setValueMinuteAfter2(double valueMinuteAfter2) {
		this.valueMinuteAfter2 = valueMinuteAfter2;
	}

	public double getValueMinuteAfter3() {
		return valueMinuteAfter3;
	}

	public void setValueMinuteAfter3(double valueMinuteAfter3) {
		this.valueMinuteAfter3 = valueMinuteAfter3;
	}

	public double getValueMinuteAfter4() {
		return valueMinuteAfter4;
	}

	public void setValueMinuteAfter4(double valueMinuteAfter4) {
		this.valueMinuteAfter4 = valueMinuteAfter4;
	}

	public double getValueMinuteAfter5() {
		return valueMinuteAfter5;
	}

	public void setValueMinuteAfter5(double valueMinuteAfter5) {
		this.valueMinuteAfter5 = valueMinuteAfter5;
	}

	public double getValueMinuteAfter6() {
		return valueMinuteAfter6;
	}

	public void setValueMinuteAfter6(double valueMinuteAfter6) {
		this.valueMinuteAfter6 = valueMinuteAfter6;
	}

	public double getValueMinuteAfter7() {
		return valueMinuteAfter7;
	}

	public void setValueMinuteAfter7(double valueMinuteAfter7) {
		this.valueMinuteAfter7 = valueMinuteAfter7;
	}

	public double getValueMinuteAfter8() {
		return valueMinuteAfter8;
	}

	public void setValueMinuteAfter8(double valueMinuteAfter8) {
		this.valueMinuteAfter8 = valueMinuteAfter8;
	}

	public double getValueMinuteAfter9() {
		return valueMinuteAfter9;
	}

	public void setValueMinuteAfter9(double valueMinuteAfter9) {
		this.valueMinuteAfter9 = valueMinuteAfter9;
	}

	public double getValueMinuteAfter10() {
		return valueMinuteAfter10;
	}

	public void setValueMinuteAfter10(double valueMinuteAfter10) {
		this.valueMinuteAfter10 = valueMinuteAfter10;
	}

	public double getValueMinuteAfter11() {
		return valueMinuteAfter11;
	}

	public void setValueMinuteAfter11(double valueMinuteAfter11) {
		this.valueMinuteAfter11 = valueMinuteAfter11;
	}

	public double getValueMinuteAfter12() {
		return valueMinuteAfter12;
	}

	public void setValueMinuteAfter12(double valueMinuteAfter12) {
		this.valueMinuteAfter12 = valueMinuteAfter12;
	}

	public double getValueMinuteAfter13() {
		return valueMinuteAfter13;
	}

	public void setValueMinuteAfter13(double valueMinuteAfter13) {
		this.valueMinuteAfter13 = valueMinuteAfter13;
	}

	public double getValueMinuteAfter14() {
		return valueMinuteAfter14;
	}

	public void setValueMinuteAfter14(double valueMinuteAfter14) {
		this.valueMinuteAfter14 = valueMinuteAfter14;
	}

	public double getValueMinuteAfter15() {
		return valueMinuteAfter15;
	}

	public void setValueMinuteAfter15(double valueMinuteAfter15) {
		this.valueMinuteAfter15 = valueMinuteAfter15;
	}

	public double getValueMinuteAfter16() {
		return valueMinuteAfter16;
	}

	public void setValueMinuteAfter16(double valueMinuteAfter16) {
		this.valueMinuteAfter16 = valueMinuteAfter16;
	}

	public double getValueMinuteAfter17() {
		return valueMinuteAfter17;
	}

	public void setValueMinuteAfter17(double valueMinuteAfter17) {
		this.valueMinuteAfter17 = valueMinuteAfter17;
	}

	public double getValueMinuteAfter18() {
		return valueMinuteAfter18;
	}

	public void setValueMinuteAfter18(double valueMinuteAfter18) {
		this.valueMinuteAfter18 = valueMinuteAfter18;
	}

	public double getValueMinuteAfter19() {
		return valueMinuteAfter19;
	}

	public void setValueMinuteAfter19(double valueMinuteAfter19) {
		this.valueMinuteAfter19 = valueMinuteAfter19;
	}

	public double getValueMinuteAfter20() {
		return valueMinuteAfter20;
	}

	public void setValueMinuteAfter20(double valueMinuteAfter20) {
		this.valueMinuteAfter20 = valueMinuteAfter20;
	}

	public double getValueMinuteAfter21() {
		return valueMinuteAfter21;
	}

	public void setValueMinuteAfter21(double valueMinuteAfter21) {
		this.valueMinuteAfter21 = valueMinuteAfter21;
	}

	public double getValueMinuteAfter22() {
		return valueMinuteAfter22;
	}

	public void setValueMinuteAfter22(double valueMinuteAfter22) {
		this.valueMinuteAfter22 = valueMinuteAfter22;
	}

	public double getValueMinuteAfter23() {
		return valueMinuteAfter23;
	}

	public void setValueMinuteAfter23(double valueMinuteAfter23) {
		this.valueMinuteAfter23 = valueMinuteAfter23;
	}

	public double getValueMinuteAfter24() {
		return valueMinuteAfter24;
	}

	public void setValueMinuteAfter24(double valueMinuteAfter24) {
		this.valueMinuteAfter24 = valueMinuteAfter24;
	}

	public double getValueMinuteAfter25() {
		return valueMinuteAfter25;
	}

	public void setValueMinuteAfter25(double valueMinuteAfter25) {
		this.valueMinuteAfter25 = valueMinuteAfter25;
	}

	public double getValueMinuteAfter26() {
		return valueMinuteAfter26;
	}

	public void setValueMinuteAfter26(double valueMinuteAfter26) {
		this.valueMinuteAfter26 = valueMinuteAfter26;
	}

	public double getValueMinuteAfter27() {
		return valueMinuteAfter27;
	}

	public void setValueMinuteAfter27(double valueMinuteAfter27) {
		this.valueMinuteAfter27 = valueMinuteAfter27;
	}

	public double getValueMinuteAfter28() {
		return valueMinuteAfter28;
	}

	public void setValueMinuteAfter28(double valueMinuteAfter28) {
		this.valueMinuteAfter28 = valueMinuteAfter28;
	}

	public double getValueMinuteAfter29() {
		return valueMinuteAfter29;
	}

	public void setValueMinuteAfter29(double valueMinuteAfter29) {
		this.valueMinuteAfter29 = valueMinuteAfter29;
	}

	public double getValueMinuteAfter30() {
		return valueMinuteAfter30;
	}

	public void setValueMinuteAfter30(double valueMinuteAfter30) {
		this.valueMinuteAfter30 = valueMinuteAfter30;
	}

	public double getValueMinuteAfter35() {
		return valueMinuteAfter35;
	}

	public void setValueMinuteAfter35(double valueMinuteAfter35) {
		this.valueMinuteAfter35 = valueMinuteAfter35;
	}

	public double getValueMinuteAfter40() {
		return valueMinuteAfter40;
	}

	public void setValueMinuteAfter40(double valueMinuteAfter40) {
		this.valueMinuteAfter40 = valueMinuteAfter40;
	}

	public double getValueMinuteAfter45() {
		return valueMinuteAfter45;
	}

	public void setValueMinuteAfter45(double valueMinuteAfter45) {
		this.valueMinuteAfter45 = valueMinuteAfter45;
	}

	public double getValueMinuteAfter50() {
		return valueMinuteAfter50;
	}

	public void setValueMinuteAfter50(double valueMinuteAfter50) {
		this.valueMinuteAfter50 = valueMinuteAfter50;
	}

	public double getValueMinuteAfter55() {
		return valueMinuteAfter55;
	}

	public void setValueMinuteAfter55(double valueMinuteAfter55) {
		this.valueMinuteAfter55 = valueMinuteAfter55;
	}

	public double getValueMinuteAfter60() {
		return valueMinuteAfter60;
	}

	public void setValueMinuteAfter60(double valueMinuteAfter60) {
		this.valueMinuteAfter60 = valueMinuteAfter60;
	}

	public double getValueMinuteAfter70() {
		return valueMinuteAfter70;
	}

	public void setValueMinuteAfter70(double valueMinuteAfter70) {
		this.valueMinuteAfter70 = valueMinuteAfter70;
	}

	public double getValueMinuteAfter80() {
		return valueMinuteAfter80;
	}

	public void setValueMinuteAfter80(double valueMinuteAfter80) {
		this.valueMinuteAfter80 = valueMinuteAfter80;
	}

	public double getValueMinuteAfter90() {
		return valueMinuteAfter90;
	}

	public void setValueMinuteAfter90(double valueMinuteAfter90) {
		this.valueMinuteAfter90 = valueMinuteAfter90;
	}

	public double getValueMinuteAfter100() {
		return valueMinuteAfter100;
	}

	public void setValueMinuteAfter100(double valueMinuteAfter100) {
		this.valueMinuteAfter100 = valueMinuteAfter100;
	}

	public double getValueMinuteAfter110() {
		return valueMinuteAfter110;
	}

	public void setValueMinuteAfter110(double valueMinuteAfter110) {
		this.valueMinuteAfter110 = valueMinuteAfter110;
	}

	public double getValueMinuteAfter120() {
		return valueMinuteAfter120;
	}

	public void setValueMinuteAfter120(double valueMinuteAfter120) {
		this.valueMinuteAfter120 = valueMinuteAfter120;
	}

	public double getValueMinuteBefore1() {
		return valueMinuteBefore1;
	}

	public void setValueMinuteBefore1(double valueMinuteBefore1) {
		this.valueMinuteBefore1 = valueMinuteBefore1;
	}

	public double getValueMinuteBefore2() {
		return valueMinuteBefore2;
	}

	public void setValueMinuteBefore2(double valueMinuteBefore2) {
		this.valueMinuteBefore2 = valueMinuteBefore2;
	}

	public double getValueMinuteBefore3() {
		return valueMinuteBefore3;
	}

	public void setValueMinuteBefore3(double valueMinuteBefore3) {
		this.valueMinuteBefore3 = valueMinuteBefore3;
	}

	public double getValueMinuteBefore4() {
		return valueMinuteBefore4;
	}

	public void setValueMinuteBefore4(double valueMinuteBefore4) {
		this.valueMinuteBefore4 = valueMinuteBefore4;
	}

	public double getValueMinuteBefore5() {
		return valueMinuteBefore5;
	}

	public void setValueMinuteBefore5(double valueMinuteBefore5) {
		this.valueMinuteBefore5 = valueMinuteBefore5;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEconomicDataId() {
		return economicDataId;
	}

	public void setEconomicDataId(String economicDataId) {
		this.economicDataId = economicDataId;
	}

}
