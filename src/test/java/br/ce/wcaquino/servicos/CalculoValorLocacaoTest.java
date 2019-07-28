package br.ce.wcaquino.servicos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {
	private LocacaoService locacaoService;
	
	@Parameter
	public List<Filme> filmes;
	@Parameter(value=1)
	public Double valor;
	@Parameter(value=2)
	public String cenario;
	
	
	@Before
	public void setup() {
		locacaoService = new LocacaoService();
	}
	
	private static Filme filme1 = new Filme("Filme1", 2, 4.0);
	private static Filme filme2 = new Filme("Filme2", 2, 4.0);
	private static Filme filme3 = new Filme("Filme3", 2, 4.0);
	private static Filme filme4 = new Filme("Filme4", 2, 4.0);
	private static Filme filme5 = new Filme("Filme5", 2, 4.0);
	private static Filme filme6 = new Filme("Filme6", 2, 4.0);
	private static Filme filme7 = new Filme("Filme7", 2, 4.0);

	@Parameters(name="{2}")
	public static Collection<Object[]> getParametros(){
		return Arrays.asList(new Object[][] {
			{Arrays.asList(filme1, filme2), 8.0, "2 filmes: sem desconto"},
			{Arrays.asList(filme1, filme2, filme3), 11.0, "3 filmes: 25%"},
			{Arrays.asList(filme1, filme2, filme3, filme4), 13.0, "4 filmes: 50%"},
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14.0, "5 filmes: 75%"},
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 18.0, "7 filmes: sem desconto"}
		});
	}
	
	@Test
	public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = new Usuario("Cristina");
			
		//acao
		Locacao locacao = locacaoService.alugarFilme(usuario, filmes);
		
		Assert.assertEquals(locacao.getValorComDesconto(), valor);
	}
}
