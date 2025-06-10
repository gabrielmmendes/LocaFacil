import br.com.locafacil.dao.ClienteDAO;
import br.com.locafacil.dao.VeiculoDAO;
import br.com.locafacil.model.Cliente;
import br.com.locafacil.model.Veiculo;
import br.com.locafacil.model.StatusVeiculo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
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

        // Criar o painel com abas
        tabbedPane = new JTabbedPane();

        // Painel de Clientes
        JPanel painelClientes = new JPanel(new BorderLayout());

        // Formulário de Clientes
        JPanel painelFormularioCliente = new JPanel(new GridLayout(6, 2));
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

        painelClientes.add(painelFormularioCliente, BorderLayout.NORTH);

        // Tabela de Clientes
        modeloTabelaClientes = new DefaultTableModel(new String[]{"ID", "Nome", "CPF", "Telefone", "Email", "CNH"}, 0);
        tabelaClientes = new JTable(modeloTabelaClientes);
        painelClientes.add(new JScrollPane(tabelaClientes), BorderLayout.CENTER);

        // Painel de Veículos
        JPanel painelVeiculos = new JPanel(new BorderLayout());

        // Formulário de Veículos
        JPanel painelFormularioVeiculo = new JPanel(new GridLayout(8, 2));
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

        painelVeiculos.add(painelFormularioVeiculo, BorderLayout.NORTH);

        // Tabela de Veículos
        modeloTabelaVeiculos = new DefaultTableModel(
            new String[]{"ID", "Placa", "Modelo", "Categoria", "Cor", "Ano", "Valor Diária", "Status"}, 0);
        tabelaVeiculos = new JTable(modeloTabelaVeiculos);
        painelVeiculos.add(new JScrollPane(tabelaVeiculos), BorderLayout.CENTER);

        // Adicionar painéis ao tabbedPane
        tabbedPane.addTab("Clientes", painelClientes);
        tabbedPane.addTab("Veículos", painelVeiculos);

        // Adicionar tabbedPane ao frame
        add(tabbedPane, BorderLayout.CENTER);

        // Configurar listeners
        btnSalvarCliente.addActionListener(e -> salvarCliente());
        btnAtualizarClientes.addActionListener(e -> carregarClientes());
        btnSalvarVeiculo.addActionListener(e -> salvarVeiculo());
        btnAtualizarVeiculos.addActionListener(e -> carregarVeiculos());

        // Carregar dados iniciais
        carregarClientes();
        carregarVeiculos();

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LocaFacilApplication::new);
    }
}
