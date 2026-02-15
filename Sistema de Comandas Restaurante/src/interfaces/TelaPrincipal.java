package interfaces;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import entidades.*;
import entidades.enums.Cores;

public class TelaPrincipal extends JFrame {
	private static final long serialVersionUID = 202506011620L;
	
	private static final String PAINEL_CARDAPIO = "Cardapio";
	private static final String PAINEL_COMANDAS = "Comandas";
	private static final String PAINEL_HISTORICO = "Historico";
	private static final String PASTA = "C:/salvamento";
	private static final String ARQUIVO = PASTA + "/dados.bin";
	private static final File DIRETORIO = new File(PASTA);
	
	private JPanel painelConteudo;
	private CardLayout cardLayout;

	private JButton gerenciarCardapio;
	private JButton comandasDasMesas;
	private JButton historicoDePedidos;

	private Cardapio card = new Cardapio();
	private List<Historico> historico = new ArrayList<>();
	private List<Comanda> comandasAtivas = new ArrayList<>();
	
	PainelComandas painelCom;
	PainelCardapio painelCard;
	PainelHistorico painelHist;

	public TelaPrincipal() {
		super("Sistema de Comandas");
		carregarArquivos();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(1000, 700));
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLayout(new BorderLayout());

		JPanel botoesPrincipais = new JPanel(new GridLayout(1, 3));
		botoesPrincipais.setBackground(Cores.CINZA.cor);

		gerenciarCardapio = new JButton("Gerenciar Cardápio");
		comandasDasMesas = new JButton("Comandas das Mesas");
		historicoDePedidos = new JButton("Histórico de Pedidos");

		botoesPrincipais.add(gerenciarCardapio);
		botoesPrincipais.add(comandasDasMesas);
		botoesPrincipais.add(historicoDePedidos);
		add(botoesPrincipais, BorderLayout.NORTH);

		cardLayout = new CardLayout();
		painelConteudo = new JPanel(cardLayout);

		painelCard = new PainelCardapio(card, comandasAtivas);
		painelHist = new PainelHistorico(historico, card);
		painelCom  = new PainelComandas(card, comandasAtivas, historico, painelCard, painelHist);
		
		painelConteudo.add(painelCard, PAINEL_CARDAPIO);
		painelConteudo.add(painelCom, PAINEL_COMANDAS);
		painelConteudo.add(painelHist, PAINEL_HISTORICO);

		add(painelConteudo, BorderLayout.CENTER);

		gerenciarCardapio.addActionListener(e -> mostrarPainel(PAINEL_CARDAPIO));
		comandasDasMesas.addActionListener(e -> mostrarPainel(PAINEL_COMANDAS));
		historicoDePedidos.addActionListener(e -> mostrarPainel(PAINEL_HISTORICO));

		mostrarPainel(PAINEL_CARDAPIO);

		configurarAutoSave();

		setVisible(true);
	}

	private void mostrarPainel(String nomePainel) {
		painelCom.atualizarVisuComandas();
		cardLayout.show(painelConteudo, nomePainel);
		atribuirCoresBotao(gerenciarCardapio, nomePainel.equals(PAINEL_CARDAPIO));
		atribuirCoresBotao(comandasDasMesas, nomePainel.equals(PAINEL_COMANDAS));
		atribuirCoresBotao(historicoDePedidos, nomePainel.equals(PAINEL_HISTORICO));
	}

	private void atribuirCoresBotao(JButton botao, boolean ativo) {
		botao.setFocusPainted(false);
		botao.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		botao.setFont(new Font("Arial", Font.BOLD, 14));
		if (ativo) {
			botao.setBackground(Cores.CINZA_ESCURO.cor);
			botao.setForeground(Cores.BRANCO.cor);
		} else {
			// Alterado de cinzaClaro() para cinza()
			botao.setBackground(Cores.BRANCO.cor);
			botao.setForeground(Cores.PRETO.cor);
		}
	}

	private void configurarAutoSave() {
		int minutos = 5;
		Timer autoSave = new Timer(minutos * 60000, e -> {
			Calendar calendar = Calendar.getInstance();
			super.setTitle(getTitle() + " (Auto-save executado às: " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ")");
			salvarArquivos();
		});
		autoSave.setRepeats(true);
		autoSave.start();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Janela fechando, parando auto-save.");
				salvarArquivos();
				autoSave.stop();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	private void carregarArquivos() {
		if (!DIRETORIO.exists()) {
		    DIRETORIO.mkdirs();
		}

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO))) {
		    Object lido = ois.readObject();
		    if (lido instanceof Map) {
				Map<String, Object> dadosLidos = (Map<String, Object>) lido;

		        if (dadosLidos.containsKey("cardapio"))
		            this.card = (Cardapio) dadosLidos.get("cardapio");
		        
		        if (dadosLidos.containsKey("comandasAtivas")) {
		            this.comandasAtivas = (List<Comanda>) dadosLidos.get("comandasAtivas");
		        }

		        if (dadosLidos.containsKey("historico")) {
		            this.historico = (List<Historico>) dadosLidos.get("historico");
		        }
		    }
		} catch (IOException | ClassNotFoundException e) {
		    System.out.println("Arquivo não encontrado ou corrompido, usando dados padrão.");
		}
	}
	
	private void salvarArquivos() {
		Map<String, Object> dadosParaSalvar = new HashMap<>();
		dadosParaSalvar.put("cardapio", card);
		dadosParaSalvar.put("comandasAtivas", comandasAtivas);
		dadosParaSalvar.put("historico", historico);

		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
		    oos.writeObject(dadosParaSalvar);
		    System.out.println("Dados salvos com sucesso!");
		} catch (IOException e) {
		    System.out.println("Erro ao salvar os dados.");
		    e.printStackTrace();
		}
	}
}
