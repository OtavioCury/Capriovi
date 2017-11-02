package br.ufpi.capriovi.suporte.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Data {

	private static Calendar c = Calendar.getInstance();

	private static void clearTime(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	public static Date diaSeguinte(Date d) {
		c.setTime(d);
		clearTime(c);
		c.add(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}

	public static Calendar calendarHoje() {
		Calendar calendar = Calendar.getInstance();
		clearTime(calendar);
		return calendar;
	}

	public static Calendar calendarAmanha() {
		Calendar calendar = calendarHoje();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return calendar;
	}

	public static Calendar calendarOntem() {
		Calendar calendar = calendarHoje();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return calendar;
	}

	public static Date hoje() {
		c.setTime(new Date());
		clearTime(c);
		return c.getTime();
	}

	public static Date amanha() {
		c.setTime(hoje());
		c.add(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}

	public static Date ontem() {
		c.setTime(hoje());
		c.add(Calendar.DAY_OF_MONTH, -1);
		return c.getTime();
	}

	public static Date proximoAno() {
		c.setTime(hoje());
		c.add(Calendar.YEAR, 1);
		return c.getTime();
	}

	public static int getAno(Date date) {
		GregorianCalendar calendario = new GregorianCalendar();
		calendario.setTime(date);
		return calendario.get(GregorianCalendar.YEAR);
	}
	
	public static int getMes(Date date) {
		GregorianCalendar calendario = new GregorianCalendar();
		calendario.setTime(date);
		return calendario.get(GregorianCalendar.MONTH);
	}
	
	public static int getDia(Date date) {
		GregorianCalendar calendario = new GregorianCalendar();
		calendario.setTime(date);
		return calendario.get(GregorianCalendar.DAY_OF_MONTH);
	}

	public static Date anoPassado() {
		c.setTime(hoje());
		c.add(Calendar.YEAR, -1);
		return c.getTime();
	}

	public static Date inicioAno(Date data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, 0);
		return calendar.getTime();
	}

	public static Date fimAno(Date data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		calendar.set(Calendar.MONTH, 11);
		return calendar.getTime();
	}

	/**
	 * Diferença em dias de {@link java.util.Date}
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static int subtract(Date a, Date b) {
		long ms = a.getTime() - b.getTime();
		return (int) (ms / 86400000L); // 1 dia = 24h * 60m * 60s * 1000ms =
										// 86400000 ms
	}

	public static int diferencaDatas(Date dataFinal, Date dataInicial) {
		long diferenca = dataFinal.getTime() - dataInicial.getTime();
		long diferencaEmDias = ((diferenca / 1000) / 60 / 60 / 24);
		return (int) (diferencaEmDias >= 0 ? diferencaEmDias : -diferencaEmDias);
	}

	public static Date getNovaData(Date date, int n) throws ParseException {

		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = new GregorianCalendar();

		c.setTime(date);
		c.add(Calendar.DAY_OF_YEAR, n);

		String data = sd.format(c.getTime());

		return sd.parse(data);
	}

	public static Date mesAnterior(Date competencia) {
		c.setTime(competencia);
		c.add(Calendar.MONTH, -1);
		return c.getTime();
	}
}