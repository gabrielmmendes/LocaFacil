package br.com.locafacil.dao;

import br.com.locafacil.config.ConexaoSQLite;
import br.com.locafacil.model.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Testes unitários para a classe ContratoLocacaoDAO.
 * Testa as operações de persistência e regras de negócio relacionadas aos contratos de locação.
 * 
 * Nota: Estes testes utilizam um banco de dados SQLite em memória para testes.
 */
public class ContratoLocacaoDAOTest {

    private ContratoLocacaoDAO contratoDAO;
    private ClienteDAO clienteDAO;
    private VeiculoDAO veiculoDAO;

    private Cliente cliente1;
    private Cliente cliente2;
    private Veiculo veiculo1;
    private Veiculo veiculo2;
    private ContratoLocacao contrato;

    @Before
    public void setUp() {
        // Inicializar DAOs
        contratoDAO = new ContratoLocacaoDAO();
        clienteDAO = new ClienteDAO();
        veiculoDAO = new VeiculoDAO();

        // Criar tabelas no banco de dados
        clienteDAO.criarTabela();
        veiculoDAO.criarTabela();
        contratoDAO.criarTabela();

        // Criar clientes de teste
        cliente1 = new Cliente(1L, "Cliente Teste 1", "111.222.333-44", 
                              "(11) 99999-9999", "cliente1@teste.com", "11122233344");
        cliente2 = new Cliente(2L, "Cliente Teste 2", "555.666.777-88", 
                              "(11) 88888-8888", "cliente2@teste.com", "55566677788");

        // Criar veículos de teste
        veiculo1 = new Veiculo(1L, "ABC-1234", "Modelo Teste 1", "SUV", 
                              "Preto", 2022, new BigDecimal("150.00"));
        veiculo2 = new Veiculo(2L, "XYZ-9876", "Modelo Teste 2", "Sedan", 
                              "Branco", 2023, new BigDecimal("200.00"));

        // Inserir clientes e veículos no banco
        clienteDAO.inserir(cliente1);
        clienteDAO.inserir(cliente2);
        veiculoDAO.inserir(veiculo1);
        veiculoDAO.inserir(veiculo2);

        // Criar contrato de teste
        contrato = new ContratoLocacao(
            1L, 
            cliente1, 
            veiculo1, 
            LocalDate.now(), 
            LocalDate.now().plusDays(5), 
            10000, 
            new BigDecimal("150.00")
        );
    }

    @After
    public void tearDown() {
        // Limpar dados de teste usando SQL direto para evitar restrições dos métodos DAO
        try (Connection conn = ConexaoSQLite.conectar()) {
            assert conn != null;

            // Primeiro, excluir todos os contratos
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("DELETE FROM contrato_locacao");
            }

            // Depois, resetar o status de todos os veículos para DISPONIVEL
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("UPDATE veiculo SET status = '" + StatusVeiculo.DISPONIVEL.name() + "'");
            }

            // Opcional: excluir todos os veículos e clientes
            // Comentado para manter os dados básicos entre testes
            // try (Statement stmt = conn.createStatement()) {
            //     stmt.execute("DELETE FROM veiculo");
            //     stmt.execute("DELETE FROM cliente");
            // }

            System.out.println("[DEBUG_LOG] Banco de dados limpo após o teste");
        } catch (SQLException e) {
            System.err.println("[DEBUG_LOG] Erro ao limpar banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testInserirEListarContrato() {
        // Inserir contrato
        contratoDAO.inserir(contrato);

        // Listar todos os contratos
        List<ContratoLocacao> contratos = contratoDAO.listarTodos();

        // Verificar se o contrato foi inserido corretamente
        assertFalse("A lista de contratos não deve estar vazia", contratos.isEmpty());
        assertEquals("Deve haver 1 contrato na lista", 1, contratos.size());

        // Verificar se o veículo foi marcado como RESERVADO
        Veiculo veiculoAtualizado = veiculoDAO.listarTodos().stream()
                .filter(v -> v.getId().equals(veiculo1.getId()))
                .findFirst()
                .orElse(null);
        assertEquals("O status do veículo deve ser RESERVADO", 
                    StatusVeiculo.RESERVADO, veiculoAtualizado.getStatus());
    }

    @Test
    public void testVerificarDisponibilidadeVeiculo() {
        // Verificar disponibilidade antes de inserir contrato
        boolean disponivel = contratoDAO.verificarDisponibilidadeVeiculo(
            veiculo1.getId(),
            LocalDate.now(),
            LocalDate.now().plusDays(5)
        );

        assertTrue("O veículo deve estar disponível antes da reserva", disponivel);

        // Inserir contrato
        contratoDAO.inserir(contrato);

        // Verificar disponibilidade para o mesmo período (deve estar indisponível)
        disponivel = contratoDAO.verificarDisponibilidadeVeiculo(
            veiculo1.getId(),
            LocalDate.now(),
            LocalDate.now().plusDays(5)
        );

        assertFalse("O veículo não deve estar disponível após a reserva", disponivel);

        // Verificar disponibilidade para um período diferente (deve estar disponível)
        disponivel = contratoDAO.verificarDisponibilidadeVeiculo(
            veiculo1.getId(),
            LocalDate.now().plusDays(10),
            LocalDate.now().plusDays(15)
        );

        assertFalse("O veículo não deve estar disponível mesmo para outro período, pois seu status é RESERVADO", disponivel);

        // Verificar disponibilidade de outro veículo (deve estar disponível)
        disponivel = contratoDAO.verificarDisponibilidadeVeiculo(
            veiculo2.getId(),
            LocalDate.now(),
            LocalDate.now().plusDays(5)
        );

        assertTrue("O segundo veículo deve estar disponível", disponivel);
    }

    @Test
    public void testRegistrarRetirada() {
        // Inserir contrato
        contratoDAO.inserir(contrato);

        // Registrar retirada
        boolean sucesso = contratoDAO.registrarRetirada(contrato.getId(), 10100);

        assertTrue("A retirada deve ser registrada com sucesso", sucesso);

        // Verificar se o status do contrato foi alterado para ATIVO
        List<ContratoLocacao> contratos = contratoDAO.listarTodos();
        ContratoLocacao contratoAtualizado = contratos.get(0);

        assertEquals("O status do contrato deve ser ATIVO", 
                    StatusContrato.ATIVO, contratoAtualizado.getStatusContrato());

        // Verificar se o status do veículo foi alterado para ALUGADO
        Veiculo veiculoAtualizado = veiculoDAO.listarTodos().stream()
                .filter(v -> v.getId().equals(veiculo1.getId()))
                .findFirst()
                .orElse(null);
        assertEquals("O status do veículo deve ser ALUGADO", 
                    StatusVeiculo.ALUGADO, veiculoAtualizado.getStatus());

        // Verificar se a quilometragem foi atualizada
        assertEquals("A quilometragem deve ser atualizada", 
                    10100, contratoAtualizado.getQuilometragemInicial());
    }

    @Test
    public void testRegistrarDevolucao() {
        // Inserir contrato
        contratoDAO.inserir(contrato);

        // Registrar retirada
        contratoDAO.registrarRetirada(contrato.getId(), 10100);

        // Registrar devolução
        boolean sucesso = contratoDAO.registrarDevolucao(
            contrato.getId(), 
            10600, 
            "Sem danos", 
            new BigDecimal("50.00"), // Valor diário de multa por atraso
            new BigDecimal("2.00")   // Valor por km excedente
        );

        assertTrue("A devolução deve ser registrada com sucesso", sucesso);

        // Verificar se o status do contrato foi alterado para ENCERRADO
        List<ContratoLocacao> contratos = contratoDAO.listarTodos();
        ContratoLocacao contratoAtualizado = contratos.get(0);

        assertEquals("O status do contrato deve ser ENCERRADO", 
                    StatusContrato.ENCERRADO, contratoAtualizado.getStatusContrato());

        // Verificar se o status do veículo foi alterado para DISPONIVEL
        Veiculo veiculoAtualizado = veiculoDAO.listarTodos().stream()
                .filter(v -> v.getId().equals(veiculo1.getId()))
                .findFirst()
                .orElse(null);
        assertEquals("O status do veículo deve ser DISPONIVEL", 
                    StatusVeiculo.DISPONIVEL, veiculoAtualizado.getStatus());

        // Verificar se a quilometragem foi atualizada
        assertEquals("A quilometragem final deve ser atualizada", 
                    10600, contratoAtualizado.getQuilometragemFinal().intValue());
    }

    @Test
    public void testCancelarReserva() {
        // Inserir contrato
        contratoDAO.inserir(contrato);

        // Cancelar reserva
        boolean sucesso = contratoDAO.cancelarReserva(contrato.getId());

        assertTrue("A reserva deve ser cancelada com sucesso", sucesso);

        // Verificar se o status do contrato foi alterado para CANCELADO
        List<ContratoLocacao> contratos = contratoDAO.listarTodos();
        ContratoLocacao contratoAtualizado = contratos.get(0);

        assertEquals("O status do contrato deve ser CANCELADO", 
                    StatusContrato.CANCELADO, contratoAtualizado.getStatusContrato());

        // Verificar se o status do veículo foi alterado para DISPONIVEL
        Veiculo veiculoAtualizado = veiculoDAO.listarTodos().stream()
                .filter(v -> v.getId().equals(veiculo1.getId()))
                .findFirst()
                .orElse(null);
        assertEquals("O status do veículo deve ser DISPONIVEL", 
                    StatusVeiculo.DISPONIVEL, veiculoAtualizado.getStatus());
    }

    @Test
    public void testGerarRelatorioFaturamento() {
        // Inserir contrato
        contratoDAO.inserir(contrato);

        // Registrar retirada
        contratoDAO.registrarRetirada(contrato.getId(), 10100);

        // Registrar devolução
        contratoDAO.registrarDevolucao(
            contrato.getId(), 
            10600, 
            "Sem danos", 
            new BigDecimal("50.00"), // Valor diário de multa por atraso
            new BigDecimal("2.00")   // Valor por km excedente
        );

        // Gerar relatório de faturamento
        Map<String, BigDecimal> relatorio = contratoDAO.gerarRelatorioFaturamento(
            LocalDate.now().minusDays(1),
            LocalDate.now().plusDays(10)
        );

        // Verificar se o relatório contém os valores esperados
        assertNotNull("O relatório não deve ser nulo", relatorio);
        assertTrue("O relatório deve conter a chave 'valorBruto'", relatorio.containsKey("valorBruto"));
        assertTrue("O relatório deve conter a chave 'valorMultas'", relatorio.containsKey("valorMultas"));
        assertTrue("O relatório deve conter a chave 'valorExtras'", relatorio.containsKey("valorExtras"));
        assertTrue("O relatório deve conter a chave 'valorTotal'", relatorio.containsKey("valorTotal"));
    }
}
