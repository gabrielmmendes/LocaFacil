import br.com.locafacil.dao.ClienteDAO;
import br.com.locafacil.model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.util.List;

public class LocaFacilApplication extends JFrame {
    private JTextField txtNome, txtCPF, txtTelefone, txtEmail, txtCNH;
    private JTable tabelaClientes;
    private DefaultTableModel modeloTabela;
    private ClienteDAO clienteDAO = new ClienteDAO();

    public LocaFacilApplication() {
        super("Cadastro de Clientes");
        setSize(700, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        clienteDAO.criarTabela();

        JPanel painelFormulario = new JPanel(new GridLayout(6, 2));
        painelFormulario.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        painelFormulario.add(txtNome);

        painelFormulario.add(new JLabel("CPF/CNPJ:"));
        txtCPF = new JTextField();
        painelFormulario.add(txtCPF);

        painelFormulario.add(new JLabel("Telefone:"));
        txtTelefone = new JTextField();
        painelFormulario.add(txtTelefone);

        painelFormulario.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        painelFormulario.add(txtEmail);

        painelFormulario.add(new JLabel("CNH:"));
        txtCNH = new JTextField();
        painelFormulario.add(txtCNH);

        JButton btnSalvar = new JButton("Salvar");
        painelFormulario.add(btnSalvar);

        JButton btnAtualizar = new JButton("Atualizar Lista");
        painelFormulario.add(btnAtualizar);

        add(painelFormulario, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(new String[]{"ID", "Nome", "CPF", "Telefone", "Email", "CNH"}, 0);
        tabelaClientes = new JTable(modeloTabela);
        add(new JScrollPane(tabelaClientes), BorderLayout.CENTER);

        btnSalvar.addActionListener(e -> salvarCliente());
        btnAtualizar.addActionListener(e -> carregarClientes());

        carregarClientes();
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
        modeloTabela.setRowCount(0);
        List<Cliente> clientes = clienteDAO.listarTodos();
        for (Cliente c : clientes) {
            modeloTabela.addRow(new Object[]{
                    c.getId(), c.getNome(), c.getCpfCnpj(),
                    c.getTelefone(), c.getEmail(), c.getNumeroCNH()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LocaFacilApplication::new);
    }
}
