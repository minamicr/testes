package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertThat;

import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	private LocacaoService service;
	//manter vari�vel static n�o deixa junit zerar conte�do toda vez que faz @before
	private static int contador; 
	
	@Rule 
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setup() {
		System.out.println("before");
		service = new LocacaoService();
		contador ++;
		System.out.println(contador + " vezes");
	}
	
	@After
	public void tearDown() {
		System.out.println("after");
	}
	
	@BeforeClass
	public static void  setupClass() {
		System.out.println("beforeClass");
	}
	
	@AfterClass
	public static void tearDownClass() {
		System.out.println("afterClass");
	}
	
	@Test
	public void testeLocacao() throws Exception{

		System.out.println("Teste loca��o");
		
		//cenario
		Filme filme = new Filme("filme", 2, 5.0);
		Usuario usuario = new Usuario("Cristina");
		
		
		//a��o
		Locacao locacao = service.alugarFilme(usuario, filme);
		
		//verifica��o
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.adicionarDias(new Date(), 2)), CoreMatchers.is(false));
		error.checkThat(locacao.getValor(), CoreMatchers.is(5.0));
		
	}
	
	@Test(expected=Exception.class)
	public void testLocacao_FilmeSemEstoque() throws Exception {
		//cenario
		Filme filme = new Filme("filme", 0, 5.0);
		Usuario usuario = new Usuario("Cristina");
		LocacaoService service = new LocacaoService();
		
		//a��o
		service.alugarFilme(usuario, filme);
	}

	@Test(expected=Exception.class)
	public void testLocacao_FilmeSemEstoque2() {
		//cenario
		Filme filme = new Filme("filme", 0, 5.0);
		Usuario usuario = new Usuario("Cristina");
		LocacaoService service = new LocacaoService();
		
		try {
			//a��o
			service.alugarFilme(usuario, filme);
		} catch(FilmeSemEstoqueException e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("sem estoque"));
		} catch(LocadoraException e) {
			e.printStackTrace();
		}
	}	

	@Test(expected=Exception.class)
	public void testLocacao_FilmeSemEstoque3() throws Exception {
		//cenario
		Filme filme = new Filme("filme", 1, 5.0);
		Usuario usuario = new Usuario("Cristina");
		LocacaoService service = new LocacaoService();

		exception.expect(Exception.class);
		exception.expectMessage("filme sem estoque");
		
		//a��o
		service.alugarFilme(usuario, filme);
		
	}
	
	@Test
	public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException{
		//cenario
		Filme filme = new Filme("filme", 0, 5.0);
		LocacaoService service = new LocacaoService();
		
		//a��o

		try {
			service.alugarFilme(null, filme);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Usu�rio vazio"));
		}
		

	}
	
	@Test
	public void testLocacao_filmeVazio() throws FilmeSemEstoqueException{
		//cenario
		Usuario usuario = new Usuario("cris");
		LocacaoService service = new LocacaoService();
		
		//acao
		try {
			service.alugarFilme(usuario, null);

		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Filme vazio"));
		}
	}
}
