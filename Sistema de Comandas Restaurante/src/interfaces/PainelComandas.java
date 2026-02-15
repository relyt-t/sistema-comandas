package interfaces;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import entidades.*;
import entidades.enums.Cores;

public class PainelComandas extends JPanel {
	private static final long serialVersionUID = 202506011622L;
	private PopUps popup = new PopUps();
	
	private Cardapio card;
	private PainelCardapio painelCard;
	private PainelHistorico painelHist;
	private List<Comanda> comandasAtivas;
	private List<Historico> historico;
	
	private JPanel painelListaHorizontalComandas;
	private JPanel painelLateralDireita;
	private JLabel lblBruto;
	private JLabel lblDescontos;
	private JLabel lblTotal;

	public PainelComandas(Cardapio card, List<Comanda> comandasAtivas, List<Historico> historico, PainelCardapio painelCard, PainelHistorico painelHist) {
		setLayout(new BorderLayout(10, 0));
		setBackground(Cores.CINZA.cor);

		this.card = card;
		this.comandasAtivas = comandasAtivas;
		this.painelCard = painelCard;
		this.historico = historico;
		this.painelHist = painelHist;
		
		painelListaHorizontalComandas = new JPanel();

		painelListaHorizontalComandas.setLayout(new BoxLayout(painelListaHorizontalComandas, BoxLayout.X_AXIS));
		painelListaHorizontalComandas.setBackground(Cores.CINZA.cor);
		painelListaHorizontalComandas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		atualizarVisuComandas();
		
		JScrollPane scrollComandasHorizontal = new JScrollPane(painelListaHorizontalComandas);
		scrollComandasHorizontal.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollComandasHorizontal.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollComandasHorizontal.setBorder(null);

		painelLateralDireita = criarPainelLateralDireita();

		add(scrollComandasHorizontal, BorderLayout.CENTER);
		add(painelLateralDireita, BorderLayout.EAST);

	}

	private JPanel criarPainelComandaIndividual(Comanda comanda) {
		JPanel painelComanda = new JPanel(new BorderLayout(5, 5));
		painelComanda.setPreferredSize(new Dimension(400, 300));
		painelComanda.setMinimumSize(new Dimension(350, 300));
		painelComanda.setMaximumSize(new Dimension(450, Integer.MAX_VALUE));
		painelComanda.setBorder(BorderFactory.createLineBorder(Cores.CINZA_ESCURO.cor, 1));
		painelComanda.setBackground(Cores.BRANCO.cor);

		JLabel lblNomeMesa = new JLabel(comanda.getNome(), SwingConstants.CENTER);
		lblNomeMesa.setFont(new Font("Arial", Font.BOLD, 18));
		lblNomeMesa.setOpaque(true);
		lblNomeMesa.setBackground(Cores.CINZA_ESCURO.cor);
		lblNomeMesa.setForeground(Cores.BRANCO.cor);
		lblNomeMesa.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));
		painelComanda.add(lblNomeMesa, BorderLayout.NORTH);

		JPanel painelItensPedido = new JPanel();
		painelItensPedido.setLayout(new BoxLayout(painelItensPedido, BoxLayout.Y_AXIS));
		painelItensPedido.setBackground(Cores.BRANCO.cor);
		painelItensPedido.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		int x = 1;
		for (Pedido pedido : comanda.getListaDePedidos()) {
			painelItensPedido.add(criarItemPedido(x, pedido, comanda));
			painelItensPedido.add(Box.createRigidArea(new Dimension(0, 5)));
			x++;
		}
		JScrollPane scrollItensPedido = new JScrollPane(painelItensPedido);
		scrollItensPedido.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollItensPedido.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollItensPedido.setBorder(BorderFactory.createTitledBorder("Lista de Pedidos"));
		painelComanda.add(scrollItensPedido, BorderLayout.CENTER);

		JPanel painelSulComanda = new JPanel(new GridLayout(0, 1, 0, 5));
		painelSulComanda.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		painelSulComanda.setBackground(Cores.CINZA_ESCURO.cor);
		JLabel lblBruto = new JLabel(String.format("R$ %.2f", comanda.getPrecoBruto()));
		JLabel lblDescontos = new JLabel(String.format("%.0f %%", comanda.getDesconto()*100));
		JLabel lblTotal = new JLabel(String.format("R$ %.2f", (comanda.getPrecoBruto() - (comanda.getPrecoBruto()*comanda.getDesconto()))));
		painelSulComanda.add(criarLinhaInfo("Valor:", lblBruto));
		if(comanda.getDesconto() != 0.0f)
			painelSulComanda.add(criarLinhaInfo("Desconto:", lblDescontos));
		painelSulComanda.add(criarLinhaInfo("Total:", lblTotal));
		
		JPanel painelBotoesComanda = new JPanel(new GridLayout(4,1,0,0));
		painelBotoesComanda.setOpaque(false);
		
		JButton btnEditarDesconto = new JButton("Editar Desconto");
		btnEditarDesconto.setBackground(Cores.BRANCO.cor);
		btnEditarDesconto.setForeground(Cores.PRETO.cor);
		btnEditarDesconto.addActionListener(e -> popup.abrirEdicaoDeDesconto(comanda, this));
		
		JButton btnAdicionarPedido = new JButton("Adicionar Pedido");
		btnAdicionarPedido.setBackground(Cores.AZUL.cor);
		btnAdicionarPedido.setForeground(Cores.BRANCO.cor);
		btnAdicionarPedido.addActionListener(e -> popup.abrirAdicaoDeProdutoNaComanda(comanda, card, this));
		
		JButton btnCancelarComanda = new JButton("Cancelar Comanda");
		btnCancelarComanda.setBackground(Cores.VERMELHO.cor);
		btnCancelarComanda.setForeground(Cores.BRANCO.cor);
		if(!(comanda instanceof Mesa)) {
			btnCancelarComanda.addActionListener(e -> popup.abrirExclusaoDeComandaSemMesa(comandasAtivas, comanda, this));
		} else {
			btnCancelarComanda.addActionListener(e -> popup.abrirCancelarComandaMesa(comanda, this));
		}
		
		JButton btnFinalizarComanda = new JButton("Finalizar Comanda");
		btnFinalizarComanda.setBackground(Cores.VERDE.cor);
		btnFinalizarComanda.setForeground(Cores.BRANCO.cor);
		if(comanda instanceof Mesa) {
			btnFinalizarComanda.addActionListener(e -> popup.abrirConcluirComandaMesa(comanda, historico, this, painelCard, painelHist));
		} else {
			btnFinalizarComanda.addActionListener(e -> popup.abrirConcluirComandaComum(comanda, comandasAtivas, historico, this, painelCard, painelHist));
		}
		
		painelSulComanda.add(btnEditarDesconto);
		painelSulComanda.add(btnAdicionarPedido);
		painelSulComanda.add(btnCancelarComanda);
		painelSulComanda.add(btnFinalizarComanda);
		
		painelComanda.add(painelSulComanda, BorderLayout.SOUTH);

		return painelComanda;
	}

	private JPanel criarItemPedido(int x, Pedido pedido, Comanda comanda) {
		JPanel itemPanel = new JPanel(new BorderLayout(5,0));
		itemPanel.setBackground(new Color(240,240,240)); // Mantido um cinza fixo para contraste
		itemPanel.setBorder(BorderFactory.createEmptyBorder(3,5,3,5));
		itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		itemPanel.add(new JLabel(String.format("%d - %s R$ %.2f", x, pedido.getNome(), pedido.getPreco())), BorderLayout.CENTER);
		JPanel painelIcones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 0));
		painelIcones.setOpaque(false);

		JButton btnExcluirItem = new JButton("Excluir");
		btnExcluirItem.setMargin(new Insets(1,1,1,1));
		btnExcluirItem.setBackground(Cores.VERMELHO.cor);
		btnExcluirItem.setForeground(Cores.BRANCO.cor);
		btnExcluirItem.addActionListener(e -> {
			comanda.removerPedido(pedido);
			atualizarVisuComandas();
		});
		
		painelIcones.add(btnExcluirItem);
		itemPanel.add(painelIcones, BorderLayout.EAST);
		return itemPanel;
	}

	private JPanel criarLinhaInfo(String titulo, JLabel valorLabel) {
		JPanel linha = new JPanel(new BorderLayout());
		linha.setOpaque(false);

		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setForeground(Cores.BRANCO.cor);
		lblTitulo.setFont(new Font("Arial", Font.PLAIN, 13));

		if (valorLabel == null) {
			valorLabel = new JLabel("N/A");
		}

		valorLabel.setForeground(Cores.BRANCO.cor);
		valorLabel.setFont(new Font("Arial", Font.BOLD, 13));
		valorLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		linha.add(lblTitulo, BorderLayout.WEST);
		linha.add(valorLabel, BorderLayout.EAST);

		return linha;
	}

	private JPanel criarPainelLateralDireita() {
		JPanel painelLateral = new JPanel();
		painelLateral.setLayout(new BoxLayout(painelLateral, BoxLayout.Y_AXIS));
		painelLateral.setPreferredSize(new Dimension(280, 0));
		// Alterado de cinzaMedio() para cinzaEscuro()
		painelLateral.setBackground(Cores.CINZA_ESCURO.cor);
		painelLateral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JButton btnAddMesas = new JButton("Adicionar Mesas");
		configurarBotaoLateral(btnAddMesas, Cores.VERDE.cor);
		btnAddMesas.addActionListener(e -> popup.abrirAdicaoDeMesas(comandasAtivas, this));
		JButton btnRemoveMesas = new JButton("Remover Mesas");
		configurarBotaoLateral(btnRemoveMesas, Cores.VERMELHO.cor);
		btnRemoveMesas.addActionListener(e -> popup.abrirExcluirMesas(comandasAtivas, this));
		JButton btnAddComandaSemMesa = new JButton("Adicionar Comanda sem Mesa");
		configurarBotaoLateral(btnAddComandaSemMesa, Cores.AZUL.cor);
		btnAddComandaSemMesa.addActionListener(e -> popup.abrirAdicaoDeComandaSemMesa(comandasAtivas, this));
		
		painelLateral.add(btnAddMesas);
		painelLateral.add(Box.createRigidArea(new Dimension(0, 10)));
		painelLateral.add(btnRemoveMesas);
		painelLateral.add(Box.createRigidArea(new Dimension(0, 10)));
		painelLateral.add(btnAddComandaSemMesa);
		painelLateral.add(Box.createVerticalStrut(30));
		JPanel painelResumoDia = new JPanel(new GridLayout(0,1,0,5));
		painelResumoDia.setOpaque(false);
		painelResumoDia.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Cores.CINZA.cor), "Resumo do Dia", // Borda mais clara
				javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP,
				new Font("Arial", Font.BOLD, 14), Cores.BRANCO.cor));
				((javax.swing.border.TitledBorder) painelResumoDia.getBorder()).setTitleColor(Cores.BRANCO.cor);
		painelResumoDia.add(criarLinhaInfo("Valor:", lblBruto));
		painelResumoDia.add(criarLinhaInfo("Desconto:", lblDescontos));
		painelResumoDia.add(criarLinhaInfo("Total:", lblTotal));
		painelLateral.add(criarResumoDoDia());
		painelLateral.add(Box.createVerticalGlue());
		return painelLateral;
	}

	private void configurarBotaoLateral(JButton botao, Color corFundo) {
		botao.setBackground(corFundo);
		botao.setForeground(Cores.BRANCO.cor);
		botao.setAlignmentX(Component.CENTER_ALIGNMENT);
		botao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
	}

	private void adicionarComanda(Comanda comanda) {
		painelListaHorizontalComandas.add(criarPainelComandaIndividual(comanda));
		painelListaHorizontalComandas.add(Box.createRigidArea(new Dimension(10, 0)));
		revalidate();
		repaint();
	}
	
	private JPanel criarResumoDoDia() {
		JPanel painelResumo = new JPanel(new GridLayout(0, 1, 0, 5));
		painelResumo.setOpaque(false);

		TitledBorder borda = BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Cores.CINZA.cor),
				"Resumo do Dia",
				TitledBorder.CENTER,
				TitledBorder.TOP,
				new Font("Arial", Font.BOLD, 14),
				Cores.BRANCO.cor
		);
		painelResumo.setBorder(borda);

		// Inicializa os JLabels
		lblBruto = new JLabel("R$ 0,00");
		lblDescontos = new JLabel("R$ 0,00");
		lblTotal = new JLabel("R$ 0,00");
		
		double valorBruto = 0.0;
		double valorDescontos = 0.0;
		double valorTotal = 0.0;
		
		for(Historico historico : historico) {
			Date data = historico.getDataDoPedido();
			Calendar calPedido = Calendar.getInstance();
			calPedido.setTime(data);
			
			Calendar calHoje = Calendar.getInstance();
			
			boolean mesmoDia = calPedido.get(Calendar.DAY_OF_MONTH) == calHoje.get(Calendar.DAY_OF_MONTH) &&
							   calPedido.get(Calendar.MONTH) == calHoje.get(Calendar.MONTH) &&
							   calPedido.get(Calendar.YEAR) == calHoje.get(Calendar.YEAR);
			
			if(mesmoDia) {
				Double precoBruto = historico.getComanda().getPrecoBruto();
				Float desconto = historico.getComanda().getDesconto();
				
				valorBruto += precoBruto;
				valorDescontos += precoBruto * desconto * -1;
				valorTotal += precoBruto + valorDescontos;
			}
		}
		
		lblBruto.setText(String.format("R$ %.2f", valorBruto));
		lblDescontos.setText(String.format("R$ %.2f", valorDescontos));
		lblTotal.setText(String.format("R$ %.2f", valorTotal));

		// Adiciona as linhas dinâmicas
		painelResumo.add(criarLinhaInfo("Bruto do Dia:", lblBruto));
		painelResumo.add(criarLinhaInfo("Descontos do Dia:", lblDescontos));
		painelResumo.add(criarLinhaInfo("Total do Dia:", lblTotal));

		return painelResumo;
	}

	public void atualizarResumoDoDia(double bruto, double descontos) {
		lblBruto.setText(String.format("R$ %.2f", bruto));
		lblDescontos.setText(String.format("R$ -%.2f", descontos));
		double total = bruto - descontos;
		lblTotal.setText(String.format("R$ %.2f", total));
	}
	
	void substituirComanda(int numMesa, Comanda comanda) {
		Comanda comandaNova = new Mesa(numMesa);
		int index = comandasAtivas.indexOf(comanda);
		comandasAtivas.remove(comanda);
		comandasAtivas.add(index, comandaNova);
	}
	
	void atualizarVisuComandas() {
		comandasAtivas.sort((a, b) -> {
			if (a instanceof Mesa && b instanceof Mesa) {
				return Integer.compare(((Mesa) a).getNumeroMesa(), ((Mesa) b).getNumeroMesa());
			}
			if (a instanceof Mesa) return -1; // Mesa vem antes
			if (b instanceof Mesa) return 1;  // Mesa vem depois
			return 0; // Ambos são Comanda sem mesa
		});

		
		painelListaHorizontalComandas.removeAll();
		for(Comanda comanda : comandasAtivas) {
			if(comanda instanceof Mesa)
				adicionarComanda(comanda);
		}
		for(Comanda comanda : comandasAtivas) {
			if(!(comanda instanceof Mesa))
				adicionarComanda(comanda);
		}
		revalidate();
		repaint();
	}
	
	public void atualizarVisuResultadosDoDia() {
		double valorBruto = 0.0;
		double valorDescontos = 0.0;

		Calendar hoje = Calendar.getInstance();

		for (Historico h : historico) {
			Calendar dataHist = Calendar.getInstance();
			dataHist.setTime(h.getDataDoPedido());

			boolean mesmoDia = dataHist.get(Calendar.DAY_OF_MONTH) == hoje.get(Calendar.DAY_OF_MONTH)
					&& dataHist.get(Calendar.MONTH) == hoje.get(Calendar.MONTH)
					&& dataHist.get(Calendar.YEAR) == hoje.get(Calendar.YEAR);

			if (mesmoDia) {
				double preco = h.getComanda().getPrecoBruto();
				float desc = h.getComanda().getDesconto();
				valorBruto += preco;
				valorDescontos += preco * desc;
			}
		}

		atualizarResumoDoDia(valorBruto, valorDescontos);
	}

}
