package br.com.locafacil.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Testes unitários para a classe ContratoLocacao.
 * Testa os principais métodos de cálculo e manipulação de contratos.
 */
public class ContratoLocacaoTest {

    private Cliente cliente;
    private Veiculo veiculo;
    private ContratoLocacao contrato;

    @Before
    public void setUp() {
        // Configuração inicial para os testes
        cliente = new Cliente(1L, "Cliente Teste", "123.456.789-00", 
                             "(11) 99999-9999", "cliente@teste.com", "12345678900");

        veiculo = new Veiculo(1L, "ABC-1234", "Modelo Teste", "Categoria Teste", 
                             "Cor Teste", 2020, new BigDecimal("100.00"));

        // Contrato com 5 dias de duração
        contrato = new ContratoLocacao(
            1L, 
            cliente, 
            veiculo, 
            LocalDate.of(2023, 1, 1), 
            LocalDate.of(2023, 1, 6), 
            10000, 
            new BigDecimal("100.00")
        );
    }

    @Test
    public void testCalcularValorParcial() {
        // Teste para um período de 5 dias (01/01 a 06/01)
        BigDecimal valorEsperado = new BigDecimal("500.00");
        BigDecimal valorCalculado = contrato.calcularValorParcial();

        assertEquals("O valor parcial para 5 dias deve ser 500.00", 
                    valorEsperado, valorCalculado);

        // Teste para um período de 1 dia
        ContratoLocacao contratoDiaUnico = new ContratoLocacao(
            2L, cliente, veiculo, 
            LocalDate.of(2023, 1, 1), 
            LocalDate.of(2023, 1, 1), 
            10000, 
            new BigDecimal("100.00")
        );

        valorEsperado = new BigDecimal("100.00");
        valorCalculado = contratoDiaUnico.calcularValorParcial();

        assertEquals("O valor parcial para 1 dia deve ser 100.00", 
                    valorEsperado, valorCalculado);
    }

    @Test
    public void testCalcularMultas() {
        // Adicionar multas ao contrato
        MultaExtra multa1 = new MultaExtra(1L, TipoMulta.ATRASO, "Atraso na devolução", new BigDecimal("50.00"));
        MultaExtra multa2 = new MultaExtra(2L, TipoMulta.DANO, "Arranhão na lateral", new BigDecimal("150.00"));

        contrato.adicionarMultaExtra(multa1);
        contrato.adicionarMultaExtra(multa2);

        // Calcular multas
        contrato.calcularMultas();

        BigDecimal valorEsperado = new BigDecimal("200.00");
        BigDecimal valorCalculado = contrato.getValorMultas();

        assertEquals("O valor total das multas deve ser 200.00", 
                    valorEsperado, valorCalculado);
    }

    @Test
    public void testCalcularValorTotal() {
        // Configurar valores parciais, multas e extras
        // O valor parcial já é calculado no construtor (500.00 para 5 dias)

        // Adicionar multas
        MultaExtra multa = new MultaExtra(1L, TipoMulta.ATRASO, "Atraso na devolução", new BigDecimal("50.00"));
        contrato.adicionarMultaExtra(multa);
        contrato.calcularMultas();

        // Adicionar extras (usando o mesmo mecanismo de multas)
        MultaExtra extra = new MultaExtra(2L, TipoMulta.QUILOMETRAGEM_EXCEDENTE, "Quilometragem excedente", new BigDecimal("75.00"));
        contrato.adicionarMultaExtra(extra);
        contrato.calcularExtras();

        // Calcular valor total
        contrato.calcularValorTotal();

        // Valor esperado: 500 (parcial) + 50 (multa) + 75 (extra) = 625
        BigDecimal valorEsperado = new BigDecimal("625.00");
        BigDecimal valorCalculado = contrato.getValorTotal();

        assertEquals("O valor total deve ser 625.00", 
                    valorEsperado, valorCalculado);
    }

    @Test
    public void testStatusContrato() {
        // Verificar status inicial
        assertEquals("O status inicial deve ser RESERVA", 
                    StatusContrato.RESERVA, contrato.getStatusContrato());

        // Alterar status para ATIVO
        contrato.setStatusContrato(StatusContrato.ATIVO);
        assertEquals("O status deve ser ATIVO após alteração", 
                    StatusContrato.ATIVO, contrato.getStatusContrato());

        // Alterar status para ENCERRADO
        contrato.setStatusContrato(StatusContrato.ENCERRADO);
        assertEquals("O status deve ser ENCERRADO após alteração", 
                    StatusContrato.ENCERRADO, contrato.getStatusContrato());
    }

    @Test
    public void testDatasEQuilometragem() {
        // Testar definição de data de retirada
        LocalDateTime dataRetirada = LocalDateTime.of(2023, 1, 1, 10, 0);
        contrato.setDataRetirada(dataRetirada);
        assertEquals("A data de retirada deve ser a mesma que foi definida", 
                    dataRetirada, contrato.getDataRetirada());

        // Testar definição de data de devolução
        LocalDateTime dataDevolucao = LocalDateTime.of(2023, 1, 6, 18, 0);
        contrato.setDataDevolucao(dataDevolucao);
        assertEquals("A data de devolução deve ser a mesma que foi definida", 
                    dataDevolucao, contrato.getDataDevolucao());

        // Testar definição de quilometragem final
        contrato.setQuilometragemFinal(10500);
        assertEquals("A quilometragem final deve ser a mesma que foi definida", 
                    10500, contrato.getQuilometragemFinal().intValue());
    }
}
