package br.com.locafacil.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Testes unitários para a classe Cliente.
 * Testa as funcionalidades básicas de um cliente no sistema.
 */
public class ClienteTest {
    
    private Cliente cliente;
    
    @Before
    public void setUp() {
        // Configuração inicial para os testes
        cliente = new Cliente(
            1L, 
            "João da Silva", 
            "123.456.789-00", 
            "(11) 99999-9999", 
            "joao@email.com", 
            "12345678900"
        );
    }
    
    @Test
    public void testConstrutorEGetters() {
        // Verificar se os valores iniciais estão corretos
        assertEquals("O ID deve ser 1", 1L, cliente.getId().longValue());
        assertEquals("O nome deve ser João da Silva", "João da Silva", cliente.getNome());
        assertEquals("O CPF/CNPJ deve ser 123.456.789-00", "123.456.789-00", cliente.getCpfCnpj());
        assertEquals("O telefone deve ser (11) 99999-9999", "(11) 99999-9999", cliente.getTelefone());
        assertEquals("O email deve ser joao@email.com", "joao@email.com", cliente.getEmail());
        assertEquals("O número da CNH deve ser 12345678900", "12345678900", cliente.getNumeroCNH());
    }
    
    @Test
    public void testSetters() {
        // Testar alteração de nome
        cliente.setNome("João Silva Souza");
        assertEquals("O nome deve ser atualizado para João Silva Souza", 
                    "João Silva Souza", cliente.getNome());
        
        // Testar alteração de CPF/CNPJ
        cliente.setCpfCnpj("987.654.321-00");
        assertEquals("O CPF/CNPJ deve ser atualizado para 987.654.321-00", 
                    "987.654.321-00", cliente.getCpfCnpj());
        
        // Testar alteração de telefone
        cliente.setTelefone("(11) 88888-8888");
        assertEquals("O telefone deve ser atualizado para (11) 88888-8888", 
                    "(11) 88888-8888", cliente.getTelefone());
        
        // Testar alteração de email
        cliente.setEmail("joao.novo@email.com");
        assertEquals("O email deve ser atualizado para joao.novo@email.com", 
                    "joao.novo@email.com", cliente.getEmail());
        
        // Testar alteração de número da CNH
        cliente.setNumeroCNH("98765432100");
        assertEquals("O número da CNH deve ser atualizado para 98765432100", 
                    "98765432100", cliente.getNumeroCNH());
    }
    
    @Test
    public void testToString() {
        // Verificar o formato da representação em string
        String esperado = "João da Silva (123.456.789-00)";
        String resultado = cliente.toString();
        
        assertEquals("O toString deve retornar 'nome (cpfCnpj)'", esperado, resultado);
    }
    
    @Test
    public void testClientePessoaJuridica() {
        // Testar cliente pessoa jurídica (CNPJ)
        Cliente clientePJ = new Cliente(
            2L, 
            "Empresa XYZ Ltda", 
            "12.345.678/0001-90", 
            "(11) 3333-3333", 
            "contato@xyz.com", 
            "98765432100" // CNH do responsável
        );
        
        assertEquals("O ID deve ser 2", 2L, clientePJ.getId().longValue());
        assertEquals("O nome deve ser Empresa XYZ Ltda", "Empresa XYZ Ltda", clientePJ.getNome());
        assertEquals("O CNPJ deve ser 12.345.678/0001-90", "12.345.678/0001-90", clientePJ.getCpfCnpj());
        
        // Testar toString para pessoa jurídica
        String esperadoPJ = "Empresa XYZ Ltda (12.345.678/0001-90)";
        String resultadoPJ = clientePJ.toString();
        
        assertEquals("O toString para PJ deve retornar 'nome (cnpj)'", esperadoPJ, resultadoPJ);
    }
}