package br.ce.wcaquino.matchers;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import br.ce.wcaquino.utils.DataUtils;

public class DataMatcher extends TypeSafeMatcher<Date> {
	private Integer dias;
	
	public DataMatcher(Integer dias) {
		this.dias = dias;
	}

	@Override
	public void describeTo(Description desc) {
		Date data = DataUtils.obterDataComDiferencaDias(this.dias);
		Calendar dataFormat = Calendar.getInstance();
		dataFormat.setTime(data);
		String dataExtenso = dataFormat.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("pt", "BR"));
		desc.appendText(dataExtenso);
	}

	@Override
	protected boolean matchesSafely(Date data) {
		return DataUtils.isMesmaData(data, DataUtils.obterDataComDiferencaDias(dias));
	}

}
