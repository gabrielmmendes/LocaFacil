package br.com.locafacil.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;

/**
 * Testes unitários para a classe Veiculo.
 * Testa as funcionalidades básicas de um veículo no sistema.
 */
public class VeiculoTest {

    private Veiculo veiculo;

    @Before
    public void setUp() {
        // Configuração inicial para os testes
        veiculo = new Veiculo(
            1L, 
            "ABC-1234", 
            "Modelo Teste", 
            "SUV", 
            "Preto", 
            2022, 
            new BigDecimal("150.00")
        );
    }

    @Test
    public void testConstrutorEGetters() {
        // Verificar se os valores iniciais estão corretos
        assertEquals("O ID deve ser 1", 1L, veiculo.getId().longValue());
        assertEquals("A placa deve ser ABC-1234", "ABC-1234", veiculo.getPlaca());
        assertEquals("O modelo deve ser Modelo Teste", "Modelo Teste", veiculo.getModelo());
        assertEquals("A categoria deve ser SUV", "SUV", veiculo.getCategoria());
        assertEquals("A cor deve ser Preto", "Preto", veiculo.getCor());
        assertEquals("O ano deve ser 2022", 2022, veiculo.getAnoFabricacao());
        assertEquals("O valor da diária deve ser 150.00", new BigDecimal("150.00"), veiculo.getValorDiaria());
        assertEquals("O status inicial deve ser DISPONIVEL", StatusVeiculo.DISPONIVEL, veiculo.getStatus());
    }

    @Test
    public void testSetters() {
        // Testar alteração de placa
        veiculo.setPlaca("XYZ-9876");
        assertEquals("A placa deve ser atualizada para XYZ-9876", "XYZ-9876", veiculo.getPlaca());

        // Testar alteração de modelo
        veiculo.setModelo("Novo Modelo");
        assertEquals("O modelo deve ser atualizado para Novo Modelo", "Novo Modelo", veiculo.getModelo());

        // Testar alteração de categoria
        veiculo.setCategoria("Sedan");
        assertEquals("A categoria deve ser atualizada para Sedan", "Sedan", veiculo.getCategoria());

        // Testar alteração de cor
        veiculo.setCor("Branco");
        assertEquals("A cor deve ser atualizada para Branco", "Branco", veiculo.getCor());

        // Testar alteração de ano
        veiculo.setAnoFabricacao(2023);
        assertEquals("O ano deve ser atualizado para 2023", 2023, veiculo.getAnoFabricacao());

        // Testar alteração de valor da diária
        veiculo.setValorDiaria(new BigDecimal("200.00"));
        assertEquals("O valor da diária deve ser atualizado para 200.00", new BigDecimal("200.00"), veiculo.getValorDiaria());
    }

    @Test
    public void testAlteracaoStatus() {
        // Verificar status inicial
        assertEquals("O status inicial deve ser DISPONIVEL", StatusVeiculo.DISPONIVEL, veiculo.getStatus());

        // Alterar para RESERVADO
        veiculo.setStatus(StatusVeiculo.RESERVADO);
        assertEquals("O status deve ser alterado para RESERVADO", StatusVeiculo.RESERVADO, veiculo.getStatus());

        // Alterar para ALUGADO
        veiculo.setStatus(StatusVeiculo.ALUGADO);
        assertEquals("O status deve ser alterado para ALUGADO", StatusVeiculo.ALUGADO, veiculo.getStatus());

        // Alterar para EM_MANUTENCAO
        veiculo.setStatus(StatusVeiculo.EM_MANUTENCAO);
        assertEquals("O status deve ser alterado para EM_MANUTENCAO", StatusVeiculo.EM_MANUTENCAO, veiculo.getStatus());

        // Voltar para DISPONIVEL
        veiculo.setStatus(StatusVeiculo.DISPONIVEL);
        assertEquals("O status deve ser alterado de volta para DISPONIVEL", StatusVeiculo.DISPONIVEL, veiculo.getStatus());
    }

    @Test
    public void testToString() {
        // Verificar o formato da representação em string
        String esperado = "Modelo Teste - ABC-1234";
        String resultado = veiculo.toString();

        assertEquals("O toString deve retornar 'Modelo - Placa'", esperado, resultado);
    }
}
