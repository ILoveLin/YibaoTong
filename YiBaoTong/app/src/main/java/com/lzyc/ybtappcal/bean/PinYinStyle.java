package com.lzyc.ybtappcal.bean;

/**
 * Created by yang on 2017/01/06.
 */
public class PinYinStyle {
	public String briefnessSpell = "";//简拼
	public String completeSpell = "";//全拼

	public String getBriefnessSpell() {
		return briefnessSpell;
	}

	public void setBriefnessSpell(String briefnessSpell) {
		this.briefnessSpell = briefnessSpell;
	}

	public String getCompleteSpell() {
		return completeSpell;
	}

	public void setCompleteSpell(String completeSpell) {
		this.completeSpell = completeSpell;
	}


	@Override
	public String toString() {
		return "PinYinStyle{" +
				"briefnessSpell='" + briefnessSpell + '\'' +
				", completeSpell='" + completeSpell + '\'' +
				'}';
	}
}
