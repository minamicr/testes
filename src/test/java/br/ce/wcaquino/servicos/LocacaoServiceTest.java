package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
		filmes.add(new Filme("filme1", 1, 10.0));
		filmes.add(new Filme("filme2", 1, 10.0));
		filmes.add(new Filme("filme3", 1, 10.0));
		//filmes.add(new Filme("filme4", 1, 10.0));
		//filmes.add(new Filme("filme5", 1, 10.0));
		//filmes.add(new Filme("filme6", 1, 10.0));

	}
	

	@Test
	public void testeLocacao() throws Exception{

		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		//verificação
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.adicionarDias(new Date(), 2)), CoreMatchers.is(false));
		error.checkThat(locacao.getValor(), CoreMatchers.is(15.0));
		
		System.out.println("Valor locação sem desconto: " + locacao.getValor());
		System.out.println("Valor locação com desconto: " + locacao.getValorComDesconto());
		
	}
	
	@Test(expected=Exception.class)
	public void testLocacao_FilmeSemEstoque() throws Exception {

		
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
}
