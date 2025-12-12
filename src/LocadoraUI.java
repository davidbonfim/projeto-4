import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class LocadoraUI extends JFrame {
    private final SistemaLocadora sistema = new SistemaLocadora();

    private JTextField placaField;
    private JTextField modeloField;
    private JTextField valorField;
    private JTextField extraField;
    private JComboBox<String> tipoCombo;
    private JLabel extraLabel;
    private JTextArea areaVeiculos;

    private JTextField clienteField;
    private JTextField placaLocacaoField;
    private JTextField diasField;
    private JComboBox<Integer> diaCombo;
    private JComboBox<Integer> mesCombo;
    private JComboBox<Integer> anoCombo;
    private JTextArea areaLocacoes;
    private JLabel resumoLocacaoLabel;
    private JTextField idLocacaoRemoverField;

    public LocadoraUI() {
        setTitle("Sistema de Locadora");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sistema.salvarDados();
                dispose();
                System.exit(0);
            }
        });

        sistema.carregarDados();

        JTabbedPane abas = new JTabbedPane();
        abas.add("Veiculos", criarPainelVeiculos());
        abas.add("Locacoes", criarPainelLocacoes());
        add(abas, BorderLayout.CENTER);

        atualizarListas();
    }

    private JPanel criarPainelVeiculos() {
        JPanel painel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        tipoCombo = new JComboBox<>(new String[] { "Carro", "Moto" });
        placaField = new JTextField(10);
        modeloField = new JTextField(10);
        valorField = new JTextField(10);
        extraField = new JTextField(10);
        extraLabel = new JLabel("Numero de portas:");
        tipoCombo.addActionListener(e -> atualizarLabelExtra());

        int row = 0;
        c.gridx = 0;
        c.gridy = row;
        form.add(new JLabel("Tipo:"), c);
        c.gridx = 1;
        form.add(tipoCombo, c);

        row++;
        c.gridx = 0;
        c.gridy = row;
        form.add(new JLabel("Placa:"), c);
        c.gridx = 1;
        form.add(placaField, c);

        row++;
        c.gridx = 0;
        c.gridy = row;
        form.add(new JLabel("Modelo:"), c);
        c.gridx = 1;
        form.add(modeloField, c);

        row++;
        c.gridx = 0;
        c.gridy = row;
        form.add(new JLabel("Valor diaria:"), c);
        c.gridx = 1;
        form.add(valorField, c);

        row++;
        c.gridx = 0;
        c.gridy = row;
        form.add(extraLabel, c);
        c.gridx = 1;
        form.add(extraField, c);

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(e -> cadastrarVeiculo());
        JButton btnRemover = new JButton("Remover por placa");
        btnRemover.addActionListener(e -> removerVeiculo());

        row++;
        c.gridx = 0;
        c.gridy = row;
        form.add(btnCadastrar, c);
        c.gridx = 1;
        form.add(btnRemover, c);

        painel.add(form, BorderLayout.NORTH);

        areaVeiculos = new JTextArea();
        areaVeiculos.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaVeiculos);
        scroll.setBorder(BorderFactory.createTitledBorder("Veiculos cadastrados"));
        painel.add(scroll, BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelLocacoes() {
        JPanel painel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        clienteField = new JTextField(12);
        placaLocacaoField = new JTextField(8);
        diasField = new JTextField(5);
        resumoLocacaoLabel = new JLabel("Resumo: informe dias e data para calcular o fim.");

        LocalDate hoje = LocalDate.now();
        anoCombo = new JComboBox<>();
        int anoAtual = hoje.getYear();
        for (int i = 0; i < 3; i++) {
            anoCombo.addItem(anoAtual + i);
        }
        mesCombo = new JComboBox<>();
        for (int m = 1; m <= 12; m++) {
            mesCombo.addItem(m);
        }
        diaCombo = new JComboBox<>();
        atualizarDiasCombo(hoje.getYear(), hoje.getMonthValue());
        diaCombo.setSelectedItem(hoje.getDayOfMonth());
        mesCombo.setSelectedItem(hoje.getMonthValue());
        anoCombo.setSelectedItem(hoje.getYear());
        mesCombo.addActionListener(e -> atualizarDiasPorSelecao());
        anoCombo.addActionListener(e -> atualizarDiasPorSelecao());

        int row = 0;
        c.gridx = 0;
        c.gridy = row;
        form.add(new JLabel("Cliente:"), c);
        c.gridx = 1;
        form.add(clienteField, c);

        row++;
        c.gridx = 0;
        c.gridy = row;
        form.add(new JLabel("Placa:"), c);
        c.gridx = 1;
        form.add(placaLocacaoField, c);

        row++;
        c.gridx = 0;
        c.gridy = row;
        form.add(new JLabel("Dias:"), c);
        c.gridx = 1;
        form.add(diasField, c);

        row++;
        c.gridx = 0;
        c.gridy = row;
        form.add(new JLabel("Data inicio:"), c);
        JPanel dataPanel = new JPanel();
        dataPanel.add(new JLabel("Dia"));
        dataPanel.add(diaCombo);
        dataPanel.add(new JLabel("Mes"));
        dataPanel.add(mesCombo);
        dataPanel.add(new JLabel("Ano"));
        dataPanel.add(anoCombo);
        c.gridx = 1;
        form.add(dataPanel, c);

        JButton btnLocar = new JButton("Registrar locacao");
        btnLocar.addActionListener(e -> registrarLocacao());
        row++;
        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 2;
        form.add(btnLocar, c);

        row++;
        idLocacaoRemoverField = new JTextField(6);
        JButton btnRemoverLocacao = new JButton("Remover locacao por ID");
        btnRemoverLocacao.addActionListener(e -> removerLocacao());
        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 1;
        form.add(new JLabel("ID para remover:"), c);
        c.gridx = 1;
        form.add(idLocacaoRemoverField, c);
        row++;
        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 2;
        form.add(btnRemoverLocacao, c);

        row++;
        c.gridx = 0;
        c.gridy = row;
        form.add(resumoLocacaoLabel, c);

        painel.add(form, BorderLayout.NORTH);

        areaLocacoes = new JTextArea();
        areaLocacoes.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaLocacoes);
        scroll.setBorder(BorderFactory.createTitledBorder("Locacoes registradas"));
        painel.add(scroll, BorderLayout.CENTER);
        return painel;
    }

    private void atualizarLabelExtra() {
        boolean ehCarro = "Carro".equalsIgnoreCase((String) tipoCombo.getSelectedItem());
        extraLabel.setText(ehCarro ? "Numero de portas:" : "Cilindradas:");
    }

    private void cadastrarVeiculo() {
        String tipo = (String) tipoCombo.getSelectedItem();
        String placa = placaField.getText().trim();
        String modelo = modeloField.getText().trim();
        String valorStr = valorField.getText().trim();
        String extraStr = extraField.getText().trim();

        if (placa.isEmpty() || modelo.isEmpty() || valorStr.isEmpty() || extraStr.isEmpty()) {
            mostrarMensagem("Preencha todos os campos.");
            return;
        }
        if (sistema.buscarVeiculo(placa) != null) {
            mostrarMensagem("Placa ja cadastrada.");
            return;
        }
        double valor;
        int extra;
        try {
            valor = Double.parseDouble(valorStr);
            extra = Integer.parseInt(extraStr);
        } catch (NumberFormatException e) {
            mostrarMensagem("Valor da diaria e campo extra devem ser numericos.");
            return;
        }

        if ("Carro".equalsIgnoreCase(tipo)) {
            sistema.cadastrarVeiculo(new Carro(placa, modelo, valor, extra));
        } else {
            sistema.cadastrarVeiculo(new Moto(placa, modelo, valor, extra));
        }
        sistema.salvarDados();
        limparCamposVeiculo();
        atualizarListas();
        mostrarMensagem("Veiculo cadastrado com sucesso.");
    }

    private void removerVeiculo() {
        String placa = placaField.getText().trim();
        if (placa.isEmpty()) {
            mostrarMensagem("Informe a placa para remover.");
            return;
        }
        boolean removido = sistema.removerVeiculo(placa);
        if (removido) {
            sistema.salvarDados();
            atualizarListas();
            mostrarMensagem("Veiculo removido.");
        } else {
            mostrarMensagem("Placa nao encontrada.");
        }
    }

    private void registrarLocacao() {
        String cliente = clienteField.getText().trim();
        String placa = placaLocacaoField.getText().trim();
        String diasStr = diasField.getText().trim();
        if (cliente.isEmpty() || placa.isEmpty() || diasStr.isEmpty()) {
            mostrarMensagem("Preencha todos os campos da locacao.");
            return;
        }
        int dias;
        try {
            dias = Integer.parseInt(diasStr);
        } catch (NumberFormatException e) {
            mostrarMensagem("Dias deve ser numero inteiro.");
            return;
        }
        if (dias <= 0) {
            mostrarMensagem("Dias deve ser maior que zero.");
            return;
        }
        Integer diaSel = (Integer) diaCombo.getSelectedItem();
        Integer mesSel = (Integer) mesCombo.getSelectedItem();
        Integer anoSel = (Integer) anoCombo.getSelectedItem();
        if (diaSel == null || mesSel == null || anoSel == null) {
            mostrarMensagem("Data invalida. Verifique dia, mes e ano.");
            return;
        }
        LocalDate dataInicio;
        try {
            dataInicio = LocalDate.of(anoSel, mesSel, diaSel);
        } catch (DateTimeException e) {
            mostrarMensagem("Data invalida. Verifique dia, mes e ano.");
            return;
        }

        Veiculo veiculo = sistema.buscarVeiculo(placa);
        if (veiculo == null) {
            mostrarMensagem("Veiculo nao encontrado para a placa informada.");
            return;
        }
        if (!sistema.veiculoDisponivel(placa, dataInicio, dias)) {
            mostrarMensagem("Veiculo indisponivel para o periodo escolhido.");
            return;
        }

        LocalDate dataFim = dataInicio.plusDays(dias - 1L);
        resumoLocacaoLabel.setText("Resumo: Inicio " + dataInicio + " - Fim " + dataFim + " (" + dias + " dias)");

        double totalPrevisto = dias * veiculo.getValorDiaria();
        String resumo = "Confirmar locacao?\n\nCliente: " + cliente + "\nPlaca: " + placa + "\nModelo: "
                + veiculo.getModelo() + "\nPeriodo: " + dataInicio + " ate " + dataFim + " (" + dias + " dias)"
                + "\nTotal: R$ " + String.format("%.2f", totalPrevisto);
        int resp = JOptionPane.showConfirmDialog(this, resumo, "Confirmar locacao", JOptionPane.OK_CANCEL_OPTION);
        if (resp != JOptionPane.OK_OPTION) {
            return;
        }

        Locacao locacao = sistema.cadastrarLocacao(cliente, placa, dias, dataInicio);
        sistema.salvarDados();
        limparCamposLocacao();
        atualizarListas();
        mostrarMensagem("Locacao registrada. Fim previsto: " + dataFim + ". Total: R$ "
                + String.format("%.2f", locacao.calcularTotal()));
    }

    private void removerLocacao() {
        String idStr = idLocacaoRemoverField.getText().trim();
        if (idStr.isEmpty()) {
            mostrarMensagem("Informe o ID da locacao para remover.");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            mostrarMensagem("ID deve ser numero inteiro.");
            return;
        }
        boolean removida = sistema.removerLocacao(id);
        if (removida) {
            sistema.salvarDados();
            atualizarListas();
            mostrarMensagem("Locacao removida.");
        } else {
            mostrarMensagem("Locacao nao encontrada.");
        }
    }

    private void atualizarListas() {
        StringBuilder sbVeiculos = new StringBuilder();
        for (Veiculo v : sistema.getListaVeiculos()) {
            sbVeiculos.append(v).append(System.lineSeparator());
        }
        areaVeiculos.setText(sbVeiculos.toString());

        StringBuilder sbLocacoes = new StringBuilder();
        for (Locacao l : sistema.getListaLocacoes()) {
            sbLocacoes.append(l).append(System.lineSeparator());
        }
        areaLocacoes.setText(sbLocacoes.toString());
    }

    private void limparCamposVeiculo() {
        placaField.setText("");
        modeloField.setText("");
        valorField.setText("");
        extraField.setText("");
    }

    private void limparCamposLocacao() {
        clienteField.setText("");
        placaLocacaoField.setText("");
        diasField.setText("");
        LocalDate hoje = LocalDate.now();
        anoCombo.setSelectedItem(hoje.getYear());
        mesCombo.setSelectedItem(hoje.getMonthValue());
        atualizarDiasCombo(hoje.getYear(), hoje.getMonthValue());
        diaCombo.setSelectedItem(hoje.getDayOfMonth());
        resumoLocacaoLabel.setText("Resumo: informe dias e data para calcular o fim.");
        idLocacaoRemoverField.setText("");
    }

    private void atualizarDiasPorSelecao() {
        Integer ano = (Integer) anoCombo.getSelectedItem();
        Integer mes = (Integer) mesCombo.getSelectedItem();
        if (ano == null || mes == null) {
            return;
        }
        int diaAnterior = diaCombo.getSelectedItem() != null ? (Integer) diaCombo.getSelectedItem() : 1;
        atualizarDiasCombo(ano, mes);
        if (diaAnterior <= diaCombo.getItemCount()) {
            diaCombo.setSelectedItem(diaAnterior);
        }
    }

    private void atualizarDiasCombo(int ano, int mes) {
        diaCombo.removeAllItems();
        int diasMes = YearMonth.of(ano, mes).lengthOfMonth();
        for (int d = 1; d <= diasMes; d++) {
            diaCombo.addItem(d);
        }
    }

    private void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem);
    }

    public static void iniciar() {
        SwingUtilities.invokeLater(() -> {
            LocadoraUI ui = new LocadoraUI();
            ui.setVisible(true);
        });
    }
}
