package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;

public class CalculadoraTest {
	private Calculadora calculadora;
	
	@Before
	public void setup() {
		calculadora = new Calculadora();
	}
	
	@Test
	public void deveSomarDoisValores() {
		//cenario
		int a = -30;
		int b = -10;
		//ação
		int resultado = calculadora.somar(a, b);
		//verificação
		Assert.assertEquals(-40, resultado);
	}
	
	@Test
	public void deveSubtrairDoisValores () {
		int a = 48;
		int b = 27;
		
		int resultado = calculadora.subtrair(a, b);
		Assert.assertEquals(21, resultado);
	}
	
	@Test
	public void deveMultiplicarDoisValores () {
		int a = 5;
		int b = 18;
		
		int resultado = calculadora.multiplicar(a, b);
		Assert.assertEquals(90, resultado);
	}
	
	@Test 
	public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
		int a = 6;
		int b = 2;
		
		int resultado = calculadora.dividir(a, b);
		Assert.assertEquals(3, resultado);
	}
	
	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarExcecaoDividirPorZero() throws NaoPodeDividirPorZeroException {
		int a = 10;
		int b = 0;
		
		Calculadora calc = new Calculadora();
		calc.dividir(a, b);
		
	}
	
}
