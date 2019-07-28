package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;


public class AssertTest {

	@Test
	public void Test() {
		Assert.assertTrue(!false);
		Assert.assertFalse(false);
		Assert.assertEquals("Erro de comparação", 1, 1);
		Assert.assertEquals(Math.PI, 3.14, 0.01);
		Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
		Assert.assertTrue("bola".startsWith("bo"));
		Assert.assertNotEquals("bola", "casa");
		
		Usuario usuario1 = new Usuario("usuario1");
		Usuario usuario2 = new Usuario("usuario1");
		Usuario usuario3 = null;
		
		Assert.assertEquals(usuario1, usuario2);
		
		Assert.assertSame(usuario2, usuario2);
		Assert.assertNotSame(usuario1, usuario2);
		
		Assert.assertTrue(usuario3 == null);
		
		Assert.assertNull(usuario3);
		Assert.assertNotNull(usuario1);
	}
}
