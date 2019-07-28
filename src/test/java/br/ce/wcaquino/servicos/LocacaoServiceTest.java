package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.matchers.DiaSemanaMatcher;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.utils.DataUtils;
import static br.ce.wcaquino.matchers.MatchersProprio.caiEm;
import static br.ce.wcaquino.matchers.MatchersProprio.caiNumaSegunda;
import static br.ce.wcaquino.matchers.MatchersProprio.ehHoje;
import static br.ce.wcaquino.matchers.MatchersProprio.ehHojeComDiferencaDias;


public class LocacaoServiceTest {

	//manter variável static não deixa junit zerar conteúdo toda vez que faz @before
	private static int contador; 
	private static List<Filme> filmes = new ArrayList<Filme>();
	
	@Rule 
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	private Usuario usuario;
	private LocacaoService service;
	
	@Before
	public void setup() {
		usuario = new Usuario("Cristina");
		service = new LocacaoService();
		filmes.add(new Filme("filme1", 1, 4.0));
		filmes.add(new Filme("filme2", 1, 4.0));
		filmes.add(new Filme("filme3", 1, 4.0));

	}
	

	@Test
	public void testeLocacao() {
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		Locacao locacao= null;
		try {
			locacao = service.alugarFilme(usuario, filmes);
		} catch (FilmeSemEstoqueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LocadoraException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		//verificação
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.adicionarDias(new Date(), 1)), CoreMatchers.is(true));
		error.checkThat(locacao.getValor(), CoreMatchers.is(12.0));
		error.checkThat(locacao.getDataLocacao(), ehHoje());
		error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
		

		
	}
	
	@Test(expected=Exception.class)
	public void testLocacao_FilmeSemEstoque() throws Exception {
		Assume.assumeTrue(filmes.stream()
				.anyMatch(filme -> filme.getEstoque().equals(0)));
		//ação
		service.alugarFilme(usuario, filmes);
	}

	@Test
	public void testLocacao_FilmeSemEstoque2() {
		try {
			//ação
			service.alugarFilme(usuario, filmes);
		} catch(FilmeSemEstoqueException e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("sem estoque"));
		} catch(LocadoraException e) {
			e.printStackTrace();
		}
	}	

	@Test
	public void testLocacao_FilmeSemEstoque3() throws Exception {
		Assume.assumeTrue(filmes.stream()
					.anyMatch(filme -> filme.getEstoque().equals(0)));
		//cenario
		exception.expect(Exception.class);
		exception.expectMessage("sem estoque");
		
		//ação
		service.alugarFilme(usuario, filmes);
		
	}
	
	@Test
	public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException{
		//ação
		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Usuário vazio"));
		}
		

	}
	
	@Test
	public void testLocacao_filmeVazio() throws FilmeSemEstoqueException{
		//cenario
		
		//acao
		try {
			service.alugarFilme(usuario, null);

		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Filme vazio"));
		}
	}
	
	
	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException, Exception {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));
		
		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		//boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(),  Calendar.MONDAY);
		//Assert.assertTrue(ehSegunda);
		
		assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
		assertThat(retorno.getDataRetorno(), caiEm(Calendar.MONDAY));
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());
	}
}
