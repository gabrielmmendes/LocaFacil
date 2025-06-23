import br.com.locafacil.dao.ClienteDAO;
import br.com.locafacil.dao.ContratoLocacaoDAO;
import br.com.locafacil.dao.VeiculoDAO;
import br.com.locafacil.model.Cliente;
import br.com.locafacil.model.ContratoLocacao;
import br.com.locafacil.model.StatusVeiculo;
import br.com.locafacil.model.Veiculo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

public class LocaFacilApplication extends JFrame {
    // Cliente fields
    private JTextField txtNome, txtCPF, txtTelefone, txtEmail, txtCNH;
    private JTable tabelaClientes;
    private DefaultTableModel modeloTabelaClientes;
    private ClienteDAO clienteDAO = new ClienteDAO();

    // Veiculo fields
    private JTextField txtPlaca, txtModelo, txtCategoria, txtCor, txtAno, txtValorDiaria;
    private JComboBox<StatusVeiculo> comboStatus;
    private JTable tabelaVeiculos;
    private DefaultTableModel modeloTabelaVeiculos;
    private VeiculoDAO veiculoDAO = new VeiculoDAO();

    // Contrato de Locação (Reserva) fields
    private JComboBox<Cliente> comboClientes;
    private JComboBox<Veiculo> comboVeiculos;
    private JTextField txtDataInicio, txtDataFim, txtQuilometragemInicial;
    private JLabel lblValorParcial;
    private JTable tabelaReservas;
    private DefaultTableModel modeloTabelaReservas;
    private ContratoLocacaoDAO contratoLocacaoDAO = new ContratoLocacaoDAO();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Tabs
    private JTabbedPane tabbedPane;

    // Relatórios
    private JTable tabelaVeiculosDisponiveis;
    private DefaultTableModel modeloTabelaVeiculosDisponiveis;
    private JTable tabelaVeiculosNaoDisponiveis;
    private DefaultTableModel modeloTabelaVeiculosNaoDisponiveis;
    private JTable tabelaLocacoesAtivas;
    private DefaultTableModel modeloTabelaLocacoesAtivas;
    private JTable tabelaHistoricoCliente;
    private DefaultTableModel modeloTabelaHistoricoCliente;
    private JComboBox<Cliente> comboClientesHistorico;
    private JTextField txtDataInicioFaturamento, txtDataFimFaturamento;
    private JLabel lblValorBruto, lblValorMultas, lblValorExtras, lblValorTotalFaturamento;

    public LocaFacilApplication() {
        super("Sistema Loca Fácil");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Inicializar DAOs e criar tabelas
        clienteDAO.criarTabela();
        veiculoDAO.criarTabela();
        contratoLocacaoDAO.criarTabela();

        // Criar o painel com abas
        tabbedPane = new JTabbedPane();

        // Painel de Clientes
        JPanel painelClientes = new JPanel(new BorderLayout());

        // Formulário de Clientes
        JPanel painelFormularioCliente = new JPanel(new GridLayout(7, 2));
        painelFormularioCliente.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        painelFormularioCliente.add(txtNome);

        painelFormularioCliente.add(new JLabel("CPF/CNPJ:"));
        txtCPF = new JTextField();
        painelFormularioCliente.add(txtCPF);

        painelFormularioCliente.add(new JLabel("Telefone:"));
        txtTelefone = new JTextField();
        painelFormularioCliente.add(txtTelefone);

        painelFormularioCliente.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        painelFormularioCliente.add(txtEmail);

        painelFormularioCliente.add(new JLabel("CNH:"));
        txtCNH = new JTextField();
        painelFormularioCliente.add(txtCNH);

        JButton btnSalvarCliente = new JButton("Salvar");
        painelFormularioCliente.add(btnSalvarCliente);

        JButton btnAtualizarClientes = new JButton("Atualizar Lista");
        painelFormularioCliente.add(btnAtualizarClientes);

        JButton btnExcluirCliente = new JButton("Excluir Selecionado");
        painelFormularioCliente.add(btnExcluirCliente);

        JLabel lblInstrucaoCliente = new JLabel("Selecione um cliente na tabela para excluir");
        painelFormularioCliente.add(lblInstrucaoCliente);

        painelClientes.add(painelFormularioCliente, BorderLayout.NORTH);

        // Tabela de Clientes
        modeloTabelaClientes = new DefaultTableModel(new String[]{"ID", "Nome", "CPF", "Telefone", "Email", "CNH"}, 0);
        tabelaClientes = new JTable(modeloTabelaClientes);
        painelClientes.add(new JScrollPane(tabelaClientes), BorderLayout.CENTER);

        // Painel de Veículos
        JPanel painelVeiculos = new JPanel(new BorderLayout());

        // Formulário de Veículos
        JPanel painelFormularioVeiculo = new JPanel(new GridLayout(9, 2));
        painelFormularioVeiculo.add(new JLabel("Placa:"));
        txtPlaca = new JTextField();
        painelFormularioVeiculo.add(txtPlaca);

        painelFormularioVeiculo.add(new JLabel("Modelo:"));
        txtModelo = new JTextField();
        painelFormularioVeiculo.add(txtModelo);

        painelFormularioVeiculo.add(new JLabel("Categoria:"));
        txtCategoria = new JTextField();
        painelFormularioVeiculo.add(txtCategoria);

        painelFormularioVeiculo.add(new JLabel("Cor:"));
        txtCor = new JTextField();
        painelFormularioVeiculo.add(txtCor);

        painelFormularioVeiculo.add(new JLabel("Ano:"));
        txtAno = new JTextField();
        painelFormularioVeiculo.add(txtAno);

        painelFormularioVeiculo.add(new JLabel("Valor Diária:"));
        txtValorDiaria = new JTextField();
        painelFormularioVeiculo.add(txtValorDiaria);

        painelFormularioVeiculo.add(new JLabel("Status:"));
        comboStatus = new JComboBox<>(StatusVeiculo.values());
        painelFormularioVeiculo.add(comboStatus);

        JButton btnSalvarVeiculo = new JButton("Salvar");
        painelFormularioVeiculo.add(btnSalvarVeiculo);

        JButton btnAtualizarVeiculos = new JButton("Atualizar Lista");
        painelFormularioVeiculo.add(btnAtualizarVeiculos);

        JButton btnExcluirVeiculo = new JButton("Excluir Selecionado");
        painelFormularioVeiculo.add(btnExcluirVeiculo);

        JLabel lblInstrucaoVeiculo = new JLabel("Selecione um veículo na tabela para excluir");
        painelFormularioVeiculo.add(lblInstrucaoVeiculo);

        painelVeiculos.add(painelFormularioVeiculo, BorderLayout.NORTH);

        // Tabela de Veículos
        modeloTabelaVeiculos = new DefaultTableModel(
            new String[]{"ID", "Placa", "Modelo", "Categoria", "Cor", "Ano", "Valor Diária", "Status"}, 0);
        tabelaVeiculos = new JTable(modeloTabelaVeiculos);
        painelVeiculos.add(new JScrollPane(tabelaVeiculos), BorderLayout.CENTER);

        // Painel de botões para gerenciamento de status de veículos
        JPanel painelBotoesStatus = new JPanel();
        JButton btnManutencao = new JButton("Marcar Em Manutenção");
        JButton btnForaFrota = new JButton("Marcar Fora de Frota");
        JButton btnDisponivel = new JButton("Marcar Disponível");
        painelBotoesStatus.add(btnManutencao);
        painelBotoesStatus.add(btnForaFrota);
        painelBotoesStatus.add(btnDisponivel);
        painelVeiculos.add(painelBotoesStatus, BorderLayout.SOUTH);

        // Painel de Reservas (Contratos de Locação)
        JPanel painelReservas = new JPanel(new BorderLayout());

        // Formulário de Reservas
        JPanel painelFormularioReserva = new JPanel(new GridLayout(8, 2));

        painelFormularioReserva.add(new JLabel("Cliente:"));
        comboClientes = new JComboBox<>();
        painelFormularioReserva.add(comboClientes);

        painelFormularioReserva.add(new JLabel("Veículo:"));
        comboVeiculos = new JComboBox<>();
        painelFormularioReserva.add(comboVeiculos);

        painelFormularioReserva.add(new JLabel("Data Início (dd/mm/aaaa):"));
        txtDataInicio = new JTextField();
        painelFormularioReserva.add(txtDataInicio);

        painelFormularioReserva.add(new JLabel("Data Fim (dd/mm/aaaa):"));
        txtDataFim = new JTextField();
        painelFormularioReserva.add(txtDataFim);

        painelFormularioReserva.add(new JLabel("Quilometragem Inicial:"));
        txtQuilometragemInicial = new JTextField();
        painelFormularioReserva.add(txtQuilometragemInicial);

        painelFormularioReserva.add(new JLabel("Valor Parcial:"));
        lblValorParcial = new JLabel("R$ 0,00");
        painelFormularioReserva.add(lblValorParcial);

        JButton btnCalcularValor = new JButton("Calcular Valor");
        painelFormularioReserva.add(btnCalcularValor);

        JButton btnCriarReserva = new JButton("Criar Reserva");
        painelFormularioReserva.add(btnCriarReserva);

        JButton btnExcluirReserva = new JButton("Excluir Selecionado");
        painelFormularioReserva.add(btnExcluirReserva);

        JLabel lblInstrucaoReserva = new JLabel("Selecione uma reserva na tabela para excluir");
        painelFormularioReserva.add(lblInstrucaoReserva);

        painelReservas.add(painelFormularioReserva, BorderLayout.NORTH);

        // Tabela de Reservas
        modeloTabelaReservas = new DefaultTableModel(
            new String[]{"ID", "Cliente", "Veículo", "Data Início", "Data Fim", "Quilometragem", "Valor", "Status"}, 0);
        tabelaReservas = new JTable(modeloTabelaReservas);
        painelReservas.add(new JScrollPane(tabelaReservas), BorderLayout.CENTER);

        // Painel de botões para operações com reservas
        JPanel painelBotoesReserva = new JPanel();
        JButton btnRegistrarRetirada = new JButton("Registrar Retirada");
        JButton btnRegistrarDevolucao = new JButton("Registrar Devolução");
        JButton btnCancelarReserva = new JButton("Cancelar Reserva");
        JButton btnCancelarContratoAtivo = new JButton("Cancelar Contrato Ativo");
        painelBotoesReserva.add(btnRegistrarRetirada);
        painelBotoesReserva.add(btnRegistrarDevolucao);
        painelBotoesReserva.add(btnCancelarReserva);
        painelBotoesReserva.add(btnCancelarContratoAtivo);
        painelReservas.add(painelBotoesReserva, BorderLayout.SOUTH);

        // Configurar listeners para os novos botões
        btnRegistrarRetirada.addActionListener(e -> registrarRetiradaVeiculo());
        btnRegistrarDevolucao.addActionListener(e -> registrarDevolucaoVeiculo());
        btnCancelarReserva.addActionListener(e -> cancelarReserva());
        btnCancelarContratoAtivo.addActionListener(e -> cancelarContratoAtivo());

        // Painel de Relatórios
        JPanel painelRelatorios = new JPanel(new BorderLayout());

        // Criar um painel com abas para os diferentes tipos de relatórios
        JTabbedPane tabbedPaneRelatorios = new JTabbedPane();

        // 1. Relatório de Veículos Disponíveis
        JPanel painelVeiculosDisponiveis = new JPanel(new BorderLayout());
        JButton btnAtualizarVeiculosDisponiveis = new JButton("Atualizar Relatório");
        painelVeiculosDisponiveis.add(btnAtualizarVeiculosDisponiveis, BorderLayout.NORTH);

        modeloTabelaVeiculosDisponiveis = new DefaultTableModel(
            new String[]{"ID", "Placa", "Modelo", "Categoria", "Cor", "Ano", "Valor Diária"}, 0);
        tabelaVeiculosDisponiveis = new JTable(modeloTabelaVeiculosDisponiveis);
        painelVeiculosDisponiveis.add(new JScrollPane(tabelaVeiculosDisponiveis), BorderLayout.CENTER);

        // 2. Relatório de Veículos Não Disponíveis
        JPanel painelVeiculosNaoDisponiveis = new JPanel(new BorderLayout());
        JButton btnAtualizarVeiculosNaoDisponiveis = new JButton("Atualizar Relatório");
        painelVeiculosNaoDisponiveis.add(btnAtualizarVeiculosNaoDisponiveis, BorderLayout.NORTH);

        modeloTabelaVeiculosNaoDisponiveis = new DefaultTableModel(
            new String[]{"ID", "Placa", "Modelo", "Categoria", "Cor", "Ano", "Valor Diária", "Status"}, 0);
        tabelaVeiculosNaoDisponiveis = new JTable(modeloTabelaVeiculosNaoDisponiveis);
        painelVeiculosNaoDisponiveis.add(new JScrollPane(tabelaVeiculosNaoDisponiveis), BorderLayout.CENTER);

        // 3. Relatório de Locações Ativas
        JPanel painelLocacoesAtivas = new JPanel(new BorderLayout());
        JButton btnAtualizarLocacoesAtivas = new JButton("Atualizar Relatório");
        painelLocacoesAtivas.add(btnAtualizarLocacoesAtivas, BorderLayout.NORTH);

        modeloTabelaLocacoesAtivas = new DefaultTableModel(
            new String[]{"ID", "Cliente", "Veículo", "Data Início", "Data Fim", "Status"}, 0);
        tabelaLocacoesAtivas = new JTable(modeloTabelaLocacoesAtivas);
        painelLocacoesAtivas.add(new JScrollPane(tabelaLocacoesAtivas), BorderLayout.CENTER);

        // 4. Histórico de Locações por Cliente
        JPanel painelHistoricoCliente = new JPanel(new BorderLayout());
        JPanel painelFiltroHistorico = new JPanel(new FlowLayout());

        painelFiltroHistorico.add(new JLabel("Cliente:"));
        comboClientesHistorico = new JComboBox<>();
        painelFiltroHistorico.add(comboClientesHistorico);

        JButton btnConsultarHistorico = new JButton("Consultar Histórico");
        painelFiltroHistorico.add(btnConsultarHistorico);

        painelHistoricoCliente.add(painelFiltroHistorico, BorderLayout.NORTH);

        modeloTabelaHistoricoCliente = new DefaultTableModel(
            new String[]{"ID", "Veículo", "Data Início", "Data Fim", "Valor", "Status"}, 0);
        tabelaHistoricoCliente = new JTable(modeloTabelaHistoricoCliente);
        painelHistoricoCliente.add(new JScrollPane(tabelaHistoricoCliente), BorderLayout.CENTER);

        // 5. Relatório de Faturamento
        JPanel painelFaturamento = new JPanel(new BorderLayout());
        JPanel painelFiltroFaturamento = new JPanel(new GridLayout(3, 2));

        painelFiltroFaturamento.add(new JLabel("Data Início (dd/mm/aaaa):"));
        txtDataInicioFaturamento = new JTextField();
        painelFiltroFaturamento.add(txtDataInicioFaturamento);

        painelFiltroFaturamento.add(new JLabel("Data Fim (dd/mm/aaaa):"));
        txtDataFimFaturamento = new JTextField();
        painelFiltroFaturamento.add(txtDataFimFaturamento);

        JButton btnGerarRelatorioFaturamento = new JButton("Gerar Relatório");
        painelFiltroFaturamento.add(btnGerarRelatorioFaturamento);

        painelFaturamento.add(painelFiltroFaturamento, BorderLayout.NORTH);

        JPanel painelResultadoFaturamento = new JPanel(new GridLayout(4, 2));
        painelResultadoFaturamento.add(new JLabel("Valor Bruto:"));
        lblValorBruto = new JLabel("R$ 0,00");
        painelResultadoFaturamento.add(lblValorBruto);

        painelResultadoFaturamento.add(new JLabel("Valor Multas:"));
        lblValorMultas = new JLabel("R$ 0,00");
        painelResultadoFaturamento.add(lblValorMultas);

        painelResultadoFaturamento.add(new JLabel("Valor Extras:"));
        lblValorExtras = new JLabel("R$ 0,00");
        painelResultadoFaturamento.add(lblValorExtras);

        painelResultadoFaturamento.add(new JLabel("Valor Total:"));
        lblValorTotalFaturamento = new JLabel("R$ 0,00");
        painelResultadoFaturamento.add(lblValorTotalFaturamento);

        painelFaturamento.add(painelResultadoFaturamento, BorderLayout.CENTER);

        // Adicionar os painéis de relatórios ao tabbedPane de relatórios
        tabbedPaneRelatorios.addTab("Veículos Disponíveis", painelVeiculosDisponiveis);
        tabbedPaneRelatorios.addTab("Veículos Não Disponíveis", painelVeiculosNaoDisponiveis);
        tabbedPaneRelatorios.addTab("Locações Ativas", painelLocacoesAtivas);
        tabbedPaneRelatorios.addTab("Histórico por Cliente", painelHistoricoCliente);
        tabbedPaneRelatorios.addTab("Faturamento", painelFaturamento);

        // Adicionar o tabbedPane de relatórios ao painel principal de relatórios
        painelRelatorios.add(tabbedPaneRelatorios, BorderLayout.CENTER);

        // Configurar listeners para os botões de relatórios
        btnAtualizarVeiculosDisponiveis.addActionListener(e -> carregarRelatorioVeiculosDisponiveis());
        btnAtualizarVeiculosNaoDisponiveis.addActionListener(e -> carregarRelatorioVeiculosNaoDisponiveis());
        btnAtualizarLocacoesAtivas.addActionListener(e -> carregarRelatorioLocacoesAtivas());
        btnConsultarHistorico.addActionListener(e -> consultarHistoricoCliente());
        btnGerarRelatorioFaturamento.addActionListener(e -> gerarRelatorioFaturamento());

        // Adicionar painéis ao tabbedPane principal
        tabbedPane.addTab("Clientes", painelClientes);
        tabbedPane.addTab("Veículos", painelVeiculos);
        tabbedPane.addTab("Reservas", painelReservas);
        tabbedPane.addTab("Relatórios", painelRelatorios);

        // Adicionar tabbedPane ao frame
        add(tabbedPane, BorderLayout.CENTER);

        // Configurar listeners
        btnSalvarCliente.addActionListener(e -> salvarCliente());
        btnAtualizarClientes.addActionListener(e -> carregarClientes());
        btnExcluirCliente.addActionListener(e -> excluirCliente());

        btnSalvarVeiculo.addActionListener(e -> salvarVeiculo());
        btnAtualizarVeiculos.addActionListener(e -> carregarVeiculos());
        btnExcluirVeiculo.addActionListener(e -> excluirVeiculo());

        // Configurar listeners para os botões de status de veículos
        btnManutencao.addActionListener(e -> alterarStatusVeiculo(StatusVeiculo.EM_MANUTENCAO));
        btnForaFrota.addActionListener(e -> alterarStatusVeiculo(StatusVeiculo.FORA_FROTA));
        btnDisponivel.addActionListener(e -> alterarStatusVeiculo(StatusVeiculo.DISPONIVEL));

        btnCalcularValor.addActionListener(e -> calcularValorReserva());
        btnCriarReserva.addActionListener(e -> criarReserva());
        btnExcluirReserva.addActionListener(e -> excluirReserva());

        // Adicionar listener para atualizar os combos quando mudar de aba
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 2) { // Aba de Reservas
                carregarCombosReserva();
                carregarReservas();
            } else if (tabbedPane.getSelectedIndex() == 3) { // Aba de Relatórios
                carregarComboClientesHistorico();
                carregarRelatorioVeiculosDisponiveis();
                carregarRelatorioVeiculosNaoDisponiveis();
                carregarRelatorioLocacoesAtivas();
            }
        });

        // Carregar dados iniciais
        carregarClientes();
        carregarVeiculos();
        carregarCombosReserva();
        carregarReservas();

        setVisible(true);
    }

    private void salvarCliente() {
        Cliente cliente = new Cliente(
                null,
                txtNome.getText(),
                txtCPF.getText(),
                txtTelefone.getText(),
                txtEmail.getText(),
                txtCNH.getText()
        );
        clienteDAO.inserir(cliente);
        limparCampos();
        carregarClientes();
    }

    private void limparCampos() {
        txtNome.setText("");
        txtCPF.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtCNH.setText("");
    }

    private void carregarClientes() {
        modeloTabelaClientes.setRowCount(0);
        List<Cliente> clientes = clienteDAO.listarTodos();
        for (Cliente c : clientes) {
            modeloTabelaClientes.addRow(new Object[]{
                    c.getId(), c.getNome(), c.getCpfCnpj(),
                    c.getTelefone(), c.getEmail(), c.getNumeroCNH()
            });
        }
    }

    private void salvarVeiculo() {
        try {
            BigDecimal valorDiaria = new BigDecimal(txtValorDiaria.getText());
            int ano = Integer.parseInt(txtAno.getText());

            Veiculo veiculo = new Veiculo(
                    null,
                    txtPlaca.getText(),
                    txtModelo.getText(),
                    txtCategoria.getText(),
                    txtCor.getText(),
                    ano,
                    valorDiaria
            );
            veiculo.setStatus((StatusVeiculo) comboStatus.getSelectedItem());
            veiculoDAO.inserir(veiculo);
            limparCamposVeiculo();
            carregarVeiculos();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao converter valores numéricos. Verifique o Ano e Valor da Diária.", 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCamposVeiculo() {
        txtPlaca.setText("");
        txtModelo.setText("");
        txtCategoria.setText("");
        txtCor.setText("");
        txtAno.setText("");
        txtValorDiaria.setText("");
        comboStatus.setSelectedIndex(0);
    }

    private void carregarVeiculos() {
        modeloTabelaVeiculos.setRowCount(0);
        List<Veiculo> veiculos = veiculoDAO.listarTodos();
        for (Veiculo v : veiculos) {
            modeloTabelaVeiculos.addRow(new Object[]{
                    v.getId(), v.getPlaca(), v.getModelo(),
                    v.getCategoria(), v.getCor(), v.getAnoFabricacao(),
                    v.getValorDiaria(), v.getStatus()
            });
        }
    }

    // Métodos para a aba de Reservas
    private void carregarCombosReserva() {
        // Limpar combos
        comboClientes.removeAllItems();
        comboVeiculos.removeAllItems();

        // Carregar clientes
        List<Cliente> clientes = clienteDAO.listarTodos();
        for (Cliente cliente : clientes) {
            comboClientes.addItem(cliente);
        }

        // Carregar apenas veículos disponíveis
        List<Veiculo> veiculos = veiculoDAO.listarTodos();
        for (Veiculo veiculo : veiculos) {
            if (veiculo.getStatus() == StatusVeiculo.DISPONIVEL) {
                comboVeiculos.addItem(veiculo);
            }
        }
    }

    private void calcularValorReserva() {
        try {
            // Obter datas
            LocalDate dataInicio = LocalDate.parse(txtDataInicio.getText(), dateFormatter);
            LocalDate dataFim = LocalDate.parse(txtDataFim.getText(), dateFormatter);

            // Validar datas
            if (dataFim.isBefore(dataInicio)) {
                JOptionPane.showMessageDialog(this, 
                    "A data de término deve ser posterior à data de início.", 
                    "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obter veículo selecionado
            Veiculo veiculo = (Veiculo) comboVeiculos.getSelectedItem();
            if (veiculo == null) {
                JOptionPane.showMessageDialog(this, 
                    "Selecione um veículo.", 
                    "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Calcular valor parcial (dias * valor diária)
            long dias = java.time.temporal.ChronoUnit.DAYS.between(dataInicio, dataFim);
            if (dias == 0) dias = 1; // Mínimo de 1 dia
            BigDecimal valorParcial = veiculo.getValorDiaria().multiply(BigDecimal.valueOf(dias));

            // Exibir valor parcial
            lblValorParcial.setText("R$ " + valorParcial.toString());

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, 
                "Formato de data inválido. Use dd/mm/aaaa.", 
                "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao calcular valor: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void criarReserva() {
        try {
            // Obter cliente e veículo selecionados
            Cliente cliente = (Cliente) comboClientes.getSelectedItem();
            Veiculo veiculo = (Veiculo) comboVeiculos.getSelectedItem();

            if (cliente == null || veiculo == null) {
                JOptionPane.showMessageDialog(this, 
                    "Selecione um cliente e um veículo.", 
                    "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obter datas
            LocalDate dataInicio = LocalDate.parse(txtDataInicio.getText(), dateFormatter);
            LocalDate dataFim = LocalDate.parse(txtDataFim.getText(), dateFormatter);

            // Validar datas
            if (dataFim.isBefore(dataInicio)) {
                JOptionPane.showMessageDialog(this, 
                    "A data de término deve ser posterior à data de início.", 
                    "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obter quilometragem inicial
            int quilometragemInicial;
            try {
                quilometragemInicial = Integer.parseInt(txtQuilometragemInicial.getText());
                if (quilometragemInicial < 0) {
                    throw new NumberFormatException("Quilometragem não pode ser negativa");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, 
                    "Quilometragem inicial inválida. Informe um número inteiro positivo.", 
                    "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar disponibilidade do veículo
            if (!contratoLocacaoDAO.verificarDisponibilidadeVeiculo(veiculo.getId(), dataInicio, dataFim)) {
                JOptionPane.showMessageDialog(this, 
                    "Veículo não disponível para o período selecionado.", 
                    "Indisponível", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Calcular valor parcial
            long dias = java.time.temporal.ChronoUnit.DAYS.between(dataInicio, dataFim);
            if (dias == 0) dias = 1; // Mínimo de 1 dia
            BigDecimal valorDiaria = veiculo.getValorDiaria();

            // Criar contrato
            ContratoLocacao contrato = new ContratoLocacao(
                null, // ID será gerado pelo banco
                cliente,
                veiculo,
                dataInicio,
                dataFim,
                quilometragemInicial,
                valorDiaria
            );

            // Salvar contrato
            contratoLocacaoDAO.inserir(contrato);

            // Limpar campos e atualizar listas
            limparCamposReserva();
            carregarReservas();
            carregarVeiculos(); // Atualizar lista de veículos (status alterado)
            carregarCombosReserva(); // Atualizar combos (veículo não mais disponível)

            JOptionPane.showMessageDialog(this, 
                "Reserva criada com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, 
                "Formato de data inválido. Use dd/mm/aaaa.", 
                "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao criar reserva: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void limparCamposReserva() {
        txtDataInicio.setText("");
        txtDataFim.setText("");
        txtQuilometragemInicial.setText("");
        lblValorParcial.setText("R$ 0,00");
        if (comboClientes.getItemCount() > 0) {
            comboClientes.setSelectedIndex(0);
        }
        if (comboVeiculos.getItemCount() > 0) {
            comboVeiculos.setSelectedIndex(0);
        }
    }

    private void carregarReservas() {
        modeloTabelaReservas.setRowCount(0);
        List<ContratoLocacao> contratos = contratoLocacaoDAO.listarTodos();
        for (ContratoLocacao c : contratos) {
            modeloTabelaReservas.addRow(new Object[]{
                c.getId(),
                c.getCliente().getNome(),
                c.getVeiculo().getModelo() + " (" + c.getVeiculo().getPlaca() + ")",
                c.getDataInicioPrevista().format(dateFormatter),
                c.getDataFimPrevista().format(dateFormatter),
                c.getQuilometragemInicial(),
                c.getValorParcial(),
                c.getStatusContrato()
            });
        }
    }

    private void excluirCliente() {
        int linhaSelecionada = tabelaClientes.getSelectedRow();
        if (linhaSelecionada >= 0) {
            // Confirmar exclusão
            int resposta = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o cliente selecionado?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

            if (resposta == JOptionPane.YES_OPTION) {
                // Obter ID do cliente selecionado
                Long idCliente = (Long) modeloTabelaClientes.getValueAt(linhaSelecionada, 0);

                try {
                    // Excluir cliente
                    clienteDAO.excluir(idCliente);

                    // Atualizar tabela
                    carregarClientes();

                    JOptionPane.showMessageDialog(this, 
                        "Cliente excluído com sucesso!", 
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, 
                        "Erro ao excluir cliente: " + e.getMessage(), 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Selecione um cliente na tabela para excluir.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void excluirVeiculo() {
        int linhaSelecionada = tabelaVeiculos.getSelectedRow();
        if (linhaSelecionada >= 0) {
            // Obter status do veículo selecionado
            StatusVeiculo status = (StatusVeiculo) modeloTabelaVeiculos.getValueAt(linhaSelecionada, 7);

            // Verificar se o veículo está alugado ou reservado
            if (status == StatusVeiculo.ALUGADO || status == StatusVeiculo.RESERVADO) {
                JOptionPane.showMessageDialog(this, 
                    "Não é possível excluir um veículo que está alugado ou reservado.", 
                    "Operação não permitida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Confirmar exclusão
            int resposta = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o veículo selecionado?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

            if (resposta == JOptionPane.YES_OPTION) {
                // Obter ID do veículo selecionado
                Long idVeiculo = (Long) modeloTabelaVeiculos.getValueAt(linhaSelecionada, 0);

                try {
                    // Excluir veículo
                    veiculoDAO.excluir(idVeiculo);

                    // Atualizar tabela
                    carregarVeiculos();

                    JOptionPane.showMessageDialog(this, 
                        "Veículo excluído com sucesso!", 
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, 
                        "Erro ao excluir veículo: " + e.getMessage(), 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Selecione um veículo na tabela para excluir.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void excluirReserva() {
        int linhaSelecionada = tabelaReservas.getSelectedRow();
        if (linhaSelecionada >= 0) {
            // Confirmar exclusão
            int resposta = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir a reserva selecionada?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

            if (resposta == JOptionPane.YES_OPTION) {
                // Obter ID da reserva selecionada
                Long idReserva = (Long) modeloTabelaReservas.getValueAt(linhaSelecionada, 0);

                try {
                    // Excluir reserva
                    contratoLocacaoDAO.excluir(idReserva);

                    // Atualizar tabelas e combos
                    carregarReservas();
                    carregarVeiculos(); // Atualizar lista de veículos (status pode ter mudado)
                    carregarCombosReserva(); // Atualizar combos (veículo pode estar disponível novamente)

                    JOptionPane.showMessageDialog(this, 
                        "Reserva excluída com sucesso!", 
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, 
                        "Erro ao excluir reserva: " + e.getMessage(), 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Selecione uma reserva na tabela para excluir.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Registra a retirada de um veículo pelo cliente, atualizando a quilometragem inicial,
     * a data de retirada, e alterando o status do contrato para ATIVO e do veículo para ALUGADO.
     */
    private void registrarRetiradaVeiculo() {
        int linhaSelecionada = tabelaReservas.getSelectedRow();
        if (linhaSelecionada >= 0) {
            // Verificar se o contrato está no status RESERVA
            String statusContrato = modeloTabelaReservas.getValueAt(linhaSelecionada, 7).toString();
            if (!statusContrato.equals("RESERVA")) {
                JOptionPane.showMessageDialog(this, 
                    "Apenas contratos com status RESERVA podem ter a retirada registrada.", 
                    "Operação não permitida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Obter ID do contrato selecionado
            Long idContrato = (Long) modeloTabelaReservas.getValueAt(linhaSelecionada, 0);

            // Solicitar a quilometragem inicial
            String quilometragemStr = JOptionPane.showInputDialog(this, 
                "Informe a quilometragem atual do veículo:", 
                "Registrar Retirada", JOptionPane.QUESTION_MESSAGE);

            if (quilometragemStr == null || quilometragemStr.trim().isEmpty()) {
                // Usuário cancelou ou não informou a quilometragem
                return;
            }

            try {
                // Converter e validar a quilometragem
                int quilometragemInicial = Integer.parseInt(quilometragemStr);
                if (quilometragemInicial < 0) {
                    throw new NumberFormatException("Quilometragem não pode ser negativa");
                }

                // Confirmar a operação
                int resposta = JOptionPane.showConfirmDialog(this,
                    "Confirma a retirada do veículo com quilometragem " + quilometragemInicial + "?",
                    "Confirmar Retirada", JOptionPane.YES_NO_OPTION);

                if (resposta == JOptionPane.YES_OPTION) {
                    // Registrar a retirada
                    boolean sucesso = contratoLocacaoDAO.registrarRetirada(idContrato, quilometragemInicial);

                    if (sucesso) {
                        // Atualizar tabelas e combos
                        carregarReservas();
                        carregarVeiculos(); // Atualizar lista de veículos (status alterado para ALUGADO)
                        carregarCombosReserva();

                        JOptionPane.showMessageDialog(this, 
                            "Retirada do veículo registrada com sucesso!", 
                            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "Não foi possível registrar a retirada do veículo.", 
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, 
                    "Quilometragem inválida. Informe um número inteiro positivo.", 
                    "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Selecione uma reserva na tabela para registrar a retirada.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Cancela uma reserva, alterando o status do contrato para CANCELADO
     * e o status do veículo para DISPONIVEL.
     */
    private void cancelarReserva() {
        int linhaSelecionada = tabelaReservas.getSelectedRow();
        if (linhaSelecionada >= 0) {
            // Verificar se o contrato está no status RESERVA
            String statusContrato = modeloTabelaReservas.getValueAt(linhaSelecionada, 7).toString();
            if (!statusContrato.equals("RESERVA")) {
                JOptionPane.showMessageDialog(this, 
                    "Apenas contratos com status RESERVA podem ser cancelados.", 
                    "Operação não permitida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Obter ID do contrato selecionado
            Long idContrato = (Long) modeloTabelaReservas.getValueAt(linhaSelecionada, 0);

            // Confirmar o cancelamento
            int resposta = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja cancelar a reserva selecionada?",
                "Confirmar Cancelamento", JOptionPane.YES_NO_OPTION);

            if (resposta == JOptionPane.YES_OPTION) {
                // Cancelar a reserva
                boolean sucesso = contratoLocacaoDAO.cancelarReserva(idContrato);

                if (sucesso) {
                    // Atualizar tabelas e combos
                    carregarReservas();
                    carregarVeiculos(); // Atualizar lista de veículos (status alterado para DISPONIVEL)
                    carregarCombosReserva(); // Atualizar combos (veículo disponível novamente)

                    JOptionPane.showMessageDialog(this, 
                        "Reserva cancelada com sucesso!", 
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Não foi possível cancelar a reserva.", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Selecione uma reserva na tabela para cancelar.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Cancela um contrato ativo (devolução antecipada), calculando multa se aplicável,
     * alterando o status do contrato para CANCELADO e o status do veículo para DISPONIVEL.
     */
    private void cancelarContratoAtivo() {
        int linhaSelecionada = tabelaReservas.getSelectedRow();
        if (linhaSelecionada >= 0) {
            // Verificar se o contrato está no status ATIVO
            String statusContrato = modeloTabelaReservas.getValueAt(linhaSelecionada, 7).toString();
            if (!statusContrato.equals("ATIVO")) {
                JOptionPane.showMessageDialog(this, 
                    "Apenas contratos com status ATIVO podem ser cancelados.", 
                    "Operação não permitida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Obter ID do contrato selecionado
            Long idContrato = (Long) modeloTabelaReservas.getValueAt(linhaSelecionada, 0);

            // Criar um painel para coletar as informações de cancelamento
            JPanel panel = new JPanel(new GridLayout(0, 1));

            // Campo para quilometragem final
            JTextField quilometragemField = new JTextField();
            panel.add(new JLabel("Quilometragem Final:"));
            panel.add(quilometragemField);

            // Campo para observações de danos
            JTextArea observacoesArea = new JTextArea(3, 20);
            observacoesArea.setLineWrap(true);
            JScrollPane scrollPane = new JScrollPane(observacoesArea);
            panel.add(new JLabel("Observações de Danos (opcional):"));
            panel.add(scrollPane);

            // Campo para valor da multa por cancelamento antecipado
            JTextField valorMultaField = new JTextField("100.00");  // Valor padrão
            panel.add(new JLabel("Valor da Multa por Cancelamento Antecipado (R$):"));
            panel.add(valorMultaField);

            // Mostrar o diálogo
            int resultado = JOptionPane.showConfirmDialog(this, panel, 
                    "Cancelar Contrato Ativo", JOptionPane.OK_CANCEL_OPTION);

            if (resultado == JOptionPane.OK_OPTION) {
                try {
                    // Validar e converter os valores
                    int quilometragemFinal = Integer.parseInt(quilometragemField.getText().trim());
                    if (quilometragemFinal < 0) {
                        throw new NumberFormatException("Quilometragem não pode ser negativa");
                    }

                    BigDecimal valorMulta = new BigDecimal(valorMultaField.getText().trim());
                    String observacoesDanos = observacoesArea.getText().trim();

                    // Confirmar a operação
                    int resposta = JOptionPane.showConfirmDialog(this,
                        "Confirma o cancelamento do contrato com quilometragem " + quilometragemFinal + "?",
                        "Confirmar Cancelamento", JOptionPane.YES_NO_OPTION);

                    if (resposta == JOptionPane.YES_OPTION) {
                        // Cancelar o contrato
                        boolean sucesso = contratoLocacaoDAO.cancelarContratoAtivo(
                            idContrato, quilometragemFinal, observacoesDanos, valorMulta);

                        if (sucesso) {
                            // Atualizar tabelas e combos
                            carregarReservas();
                            carregarVeiculos(); // Atualizar lista de veículos (status alterado para DISPONIVEL)
                            carregarCombosReserva(); // Atualizar combos (veículo disponível novamente)

                            JOptionPane.showMessageDialog(this, 
                                "Contrato cancelado com sucesso!", 
                                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, 
                                "Não foi possível cancelar o contrato.", 
                                "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, 
                        "Por favor, insira valores numéricos válidos para quilometragem e multa.", 
                        "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Selecione um contrato na tabela para cancelar.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Registra a devolução de um veículo pelo cliente, atualizando a quilometragem final,
     * registrando observações de danos, calculando multas e extras, e alterando o status
     * do contrato para ENCERRADO e do veículo para DISPONIVEL.
     */
    private void registrarDevolucaoVeiculo() {
        int linhaSelecionada = tabelaReservas.getSelectedRow();
        if (linhaSelecionada >= 0) {
            // Verificar se o contrato está no status ATIVO
            String statusContrato = modeloTabelaReservas.getValueAt(linhaSelecionada, 7).toString();
            if (!statusContrato.equals("ATIVO")) {
                JOptionPane.showMessageDialog(this, 
                    "Apenas contratos com status ATIVO podem ter a devolução registrada.", 
                    "Operação não permitida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Obter ID do contrato selecionado
            Long idContrato = (Long) modeloTabelaReservas.getValueAt(linhaSelecionada, 0);

            // Criar um painel para coletar as informações de devolução
            JPanel panel = new JPanel(new GridLayout(0, 1));

            // Campo para quilometragem final
            JTextField quilometragemField = new JTextField();
            panel.add(new JLabel("Quilometragem Final:"));
            panel.add(quilometragemField);

            // Campo para observações de danos
            JTextArea observacoesArea = new JTextArea(3, 20);
            observacoesArea.setLineWrap(true);
            JScrollPane scrollPane = new JScrollPane(observacoesArea);
            panel.add(new JLabel("Observações de Danos (opcional):"));
            panel.add(scrollPane);

            // Campos para valores de multas
            JTextField valorMultaAtrasoField = new JTextField("50.00");  // Valor padrão
            panel.add(new JLabel("Valor Diário de Multa por Atraso (R$):"));
            panel.add(valorMultaAtrasoField);

            JTextField valorKmExcedenteField = new JTextField("1.50");  // Valor padrão
            panel.add(new JLabel("Valor por Km Excedente (R$):"));
            panel.add(valorKmExcedenteField);

            // Mostrar o diálogo
            int resultado = JOptionPane.showConfirmDialog(this, panel, 
                    "Registrar Devolução de Veículo", JOptionPane.OK_CANCEL_OPTION);

            if (resultado == JOptionPane.OK_OPTION) {
                try {
                    // Validar e converter os valores
                    int quilometragemFinal = Integer.parseInt(quilometragemField.getText().trim());
                    if (quilometragemFinal < 0) {
                        throw new NumberFormatException("Quilometragem não pode ser negativa");
                    }

                    BigDecimal valorDiarioMultaAtraso = new BigDecimal(valorMultaAtrasoField.getText().trim());
                    BigDecimal valorPorKmExcedente = new BigDecimal(valorKmExcedenteField.getText().trim());

                    String observacoesDanos = observacoesArea.getText().trim();

                    // Confirmar a operação
                    int resposta = JOptionPane.showConfirmDialog(this,
                        "Confirma a devolução do veículo com quilometragem " + quilometragemFinal + "?",
                        "Confirmar Devolução", JOptionPane.YES_NO_OPTION);

                    if (resposta == JOptionPane.YES_OPTION) {
                        // Registrar a devolução
                        boolean sucesso = contratoLocacaoDAO.registrarDevolucao(
                            idContrato, quilometragemFinal, observacoesDanos, 
                            valorDiarioMultaAtraso, valorPorKmExcedente);

                        if (sucesso) {
                            // Atualizar tabelas e combos
                            carregarReservas();
                            carregarVeiculos(); // Atualizar lista de veículos (status alterado para DISPONIVEL)
                            carregarCombosReserva(); // Atualizar combos (veículo disponível novamente)

                            JOptionPane.showMessageDialog(this, 
                                "Devolução do veículo registrada com sucesso!", 
                                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, 
                                "Não foi possível registrar a devolução do veículo.", 
                                "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, 
                        "Valores inválidos. Verifique os campos numéricos.", 
                        "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Selecione um contrato na tabela para registrar a devolução.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Carrega o relatório de veículos disponíveis.
     */
    private void carregarRelatorioVeiculosDisponiveis() {
        modeloTabelaVeiculosDisponiveis.setRowCount(0);
        List<Veiculo> veiculos = veiculoDAO.listarVeiculosDisponiveis();
        for (Veiculo v : veiculos) {
            modeloTabelaVeiculosDisponiveis.addRow(new Object[]{
                    v.getId(), v.getPlaca(), v.getModelo(),
                    v.getCategoria(), v.getCor(), v.getAnoFabricacao(),
                    v.getValorDiaria()
            });
        }
    }

    /**
     * Carrega o relatório de veículos não disponíveis.
     */
    private void carregarRelatorioVeiculosNaoDisponiveis() {
        modeloTabelaVeiculosNaoDisponiveis.setRowCount(0);
        List<Veiculo> veiculos = veiculoDAO.listarVeiculosNaoDisponiveis();
        for (Veiculo v : veiculos) {
            modeloTabelaVeiculosNaoDisponiveis.addRow(new Object[]{
                    v.getId(), v.getPlaca(), v.getModelo(),
                    v.getCategoria(), v.getCor(), v.getAnoFabricacao(),
                    v.getValorDiaria(), v.getStatus()
            });
        }
    }

    /**
     * Carrega o relatório de locações ativas.
     */
    private void carregarRelatorioLocacoesAtivas() {
        modeloTabelaLocacoesAtivas.setRowCount(0);
        List<ContratoLocacao> contratos = contratoLocacaoDAO.listarLocacoesAtivas();
        for (ContratoLocacao c : contratos) {
            modeloTabelaLocacoesAtivas.addRow(new Object[]{
                    c.getId(),
                    c.getCliente().getNome(),
                    c.getVeiculo().getModelo() + " (" + c.getVeiculo().getPlaca() + ")",
                    c.getDataInicioPrevista().format(dateFormatter),
                    c.getDataFimPrevista().format(dateFormatter),
                    c.getStatusContrato()
            });
        }
    }

    /**
     * Carrega o combo de clientes para o histórico e atualiza o combo quando a aba de relatórios é selecionada.
     */
    private void carregarComboClientesHistorico() {
        comboClientesHistorico.removeAllItems();
        List<Cliente> clientes = clienteDAO.listarTodos();
        for (Cliente cliente : clientes) {
            comboClientesHistorico.addItem(cliente);
        }
    }

    /**
     * Consulta o histórico de locações de um cliente específico.
     */
    private void consultarHistoricoCliente() {
        Cliente cliente = (Cliente) comboClientesHistorico.getSelectedItem();
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um cliente para consultar o histórico.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        modeloTabelaHistoricoCliente.setRowCount(0);
        List<ContratoLocacao> contratos = contratoLocacaoDAO.listarHistoricoCliente(cliente.getId());
        for (ContratoLocacao c : contratos) {
            modeloTabelaHistoricoCliente.addRow(new Object[]{
                    c.getId(),
                    c.getVeiculo().getModelo() + " (" + c.getVeiculo().getPlaca() + ")",
                    c.getDataInicioPrevista().format(dateFormatter),
                    c.getDataFimPrevista().format(dateFormatter),
                    c.getValorParcial(),
                    c.getStatusContrato()
            });
        }
    }

    /**
     * Gera o relatório de faturamento para um período específico.
     */
    private void gerarRelatorioFaturamento() {
        try {
            // Obter datas
            LocalDate dataInicio = LocalDate.parse(txtDataInicioFaturamento.getText(), dateFormatter);
            LocalDate dataFim = LocalDate.parse(txtDataFimFaturamento.getText(), dateFormatter);

            // Validar datas
            if (dataFim.isBefore(dataInicio)) {
                JOptionPane.showMessageDialog(this, 
                    "A data de término deve ser posterior à data de início.", 
                    "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Gerar relatório
            Map<String, BigDecimal> resultado = contratoLocacaoDAO.gerarRelatorioFaturamento(dataInicio, dataFim);

            // Exibir resultados
            lblValorBruto.setText("R$ " + resultado.get("valorBruto").toString());
            lblValorMultas.setText("R$ " + resultado.get("valorMultas").toString());
            lblValorExtras.setText("R$ " + resultado.get("valorExtras").toString());
            lblValorTotalFaturamento.setText("R$ " + resultado.get("valorTotal").toString());

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, 
                "Formato de data inválido. Use dd/mm/aaaa.", 
                "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao gerar relatório: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Altera o status de um veículo selecionado na tabela.
     * 
     * @param novoStatus O novo status a ser atribuído ao veículo
     */
    private void alterarStatusVeiculo(StatusVeiculo novoStatus) {
        int linhaSelecionada = tabelaVeiculos.getSelectedRow();
        if (linhaSelecionada >= 0) {
            // Obter ID e status atual do veículo selecionado
            Long idVeiculo = (Long) modeloTabelaVeiculos.getValueAt(linhaSelecionada, 0);
            StatusVeiculo statusAtual = (StatusVeiculo) modeloTabelaVeiculos.getValueAt(linhaSelecionada, 7);

            // Verificar se o veículo está alugado ou reservado
            if (statusAtual == StatusVeiculo.ALUGADO || statusAtual == StatusVeiculo.RESERVADO) {
                JOptionPane.showMessageDialog(this, 
                    "Não é possível alterar o status de um veículo que está alugado ou reservado.", 
                    "Operação não permitida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Confirmar a alteração de status
            int resposta = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja alterar o status do veículo para " + novoStatus + "?",
                "Confirmar Alteração de Status", JOptionPane.YES_NO_OPTION);

            if (resposta == JOptionPane.YES_OPTION) {
                try {
                    // Obter o veículo completo do banco de dados
                    List<Veiculo> veiculos = veiculoDAO.listarTodos();
                    Veiculo veiculo = veiculos.stream()
                        .filter(v -> v.getId().equals(idVeiculo))
                        .findFirst()
                        .orElse(null);

                    if (veiculo != null) {
                        // Atualizar o status do veículo
                        veiculo.setStatus(novoStatus);
                        veiculoDAO.atualizar(veiculo);

                        // Atualizar a tabela
                        carregarVeiculos();

                        JOptionPane.showMessageDialog(this, 
                            "Status do veículo alterado com sucesso para " + novoStatus + "!", 
                            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, 
                        "Erro ao alterar status do veículo: " + e.getMessage(), 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Selecione um veículo na tabela para alterar seu status.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LocaFacilApplication::new);
    }
}
