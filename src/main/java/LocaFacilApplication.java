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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

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

        // Adicionar painéis ao tabbedPane
        tabbedPane.addTab("Clientes", painelClientes);
        tabbedPane.addTab("Veículos", painelVeiculos);
        tabbedPane.addTab("Reservas", painelReservas);

        // Adicionar tabbedPane ao frame
        add(tabbedPane, BorderLayout.CENTER);

        // Configurar listeners
        btnSalvarCliente.addActionListener(e -> salvarCliente());
        btnAtualizarClientes.addActionListener(e -> carregarClientes());
        btnExcluirCliente.addActionListener(e -> excluirCliente());

        btnSalvarVeiculo.addActionListener(e -> salvarVeiculo());
        btnAtualizarVeiculos.addActionListener(e -> carregarVeiculos());
        btnExcluirVeiculo.addActionListener(e -> excluirVeiculo());

        btnCalcularValor.addActionListener(e -> calcularValorReserva());
        btnCriarReserva.addActionListener(e -> criarReserva());
        btnExcluirReserva.addActionListener(e -> excluirReserva());

        // Adicionar listener para atualizar os combos quando mudar de aba
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 2) { // Aba de Reservas
                carregarCombosReserva();
                carregarReservas();
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LocaFacilApplication::new);
    }
}
