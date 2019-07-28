package br.ce.wcaquino.matchers;

import java.util.Calendar;

public class MatchersProprio {

	public static DiaSemanaMatcher caiEm(Integer diaSemana) {
		return new DiaSemanaMatcher(diaSemana);
	}
	
	public static DiaSemanaMatcher caiNumaSegunda() {
		return new DiaSemanaMatcher(Calendar.MONDAY);
	}
	
	public static DataMatcher ehHojeComDiferencaDias(Integer dias) {
		return new DataMatcher(dias);
	}
	
	public static DataMatcher ehHoje() {
		return new DataMatcher(0);
	}
}
