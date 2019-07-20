package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;


public class LocacaoService {
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {
		if (usuario == null) {
			throw new LocadoraException("Usu·rio vazio");
		}
		
		if (filmes == null) {
			throw new LocadoraException("Filme vazio");
		}
		
		for (Filme filme: filmes) {
			if (filme.getEstoque() == 0) {
				throw new FilmeSemEstoqueException("sem estoque");
			}
		}

		Locacao locacao = new Locacao();
		locacao.setFilme(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(getPrecoLocacaoAllFilmes(filmes));
		locacao.setValorComDesconto(getPrecoLocacaoComDesconto(filmes));
		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar m√©todo para salvar
		
		return locacao;
	}

	private Double getPrecoLocacaoAllFilmes(List<Filme> filmes) {
		return filmes.stream()
				.map(filme -> filme.getPrecoLocacao())
				.reduce(0.0, (a,b) -> a + b);
		
	}
	
	private Double getPrecoLocacaoComDesconto(List<Filme> filmes) {
		int quantidadeFilmes = filmes.size();
		double valorFilme = 0;
		double valorTotal = 0;
		int filmeAtual = 0;
		
		for (Filme filme:filmes) {
			filmeAtual ++;
			valorFilme = filme.getPrecoLocacao();
			
			if (quantidadeFilmes == 3 && filmeAtual == 3) {
				valorFilme = valorFilme * (1 - 0.25);
			} else if (quantidadeFilmes == 4 && filmeAtual == 4) {
				valorFilme = valorFilme * (1 - 0.50);
			}else if (quantidadeFilmes == 5 && filmeAtual == 5) {
				valorFilme = valorFilme * (1 - 0.75);
			}else if (quantidadeFilmes == 6 && filmeAtual == 6) {
				valorFilme = valorFilme * (1 - 1);
			}
			
			valorTotal += valorFilme;
		}
		
		return valorTotal;
	}
	//preco locacao 3.o filme 25% | 4.o filme 50% | 5.o filme 75% | 6.o filme 100 %
	

	
}