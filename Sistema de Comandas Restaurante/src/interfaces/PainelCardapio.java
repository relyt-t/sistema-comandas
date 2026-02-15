package interfaces;

import java.awt.*;
import java.awt.event.*;
import java.util.Comparator;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;

import entidades.*;
import entidades.enums.Cores;

public class PainelCardapio extends JPanel {
    private static final long serialVersionUID = 202506011621L;
    private PopUps popup = new PopUps();
    
    JPanel painelListagem;
    Cardapio card;
    List<Comanda> comandasAtivas;

    public PainelCardapio(Cardapio card, List<Comanda> comandasAtivas) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Cores.CINZA.cor);

        this.card = card;
        this.comandasAtivas = comandasAtivas;
        
        JPanel botoesAdiciona = new JPanel(new GridLayout(0, 2));
        
        JButton adicionarNoCardapio = new JButton("Adicionar Novo Produto ao Cardápio");
        adicionarNoCardapio.setBackground(Cores.VERDE.cor);
        adicionarNoCardapio.setForeground(Cores.BRANCO.cor);
        adicionarNoCardapio.setFont(new Font("Arial", Font.BOLD, 16));
        adicionarNoCardapio.setPreferredSize(new Dimension(0, 60));
        adicionarNoCardapio.addActionListener(e -> popup.abrirAdicaoDeProduto(card, this));
        botoesAdiciona.add(adicionarNoCardapio);
        
        JButton adicionarCategoria = new JButton("Adicionar Nova Categoria");
        adicionarCategoria.setBackground(Cores.AZUL.cor);
        adicionarCategoria.setForeground(Cores.BRANCO.cor);
        adicionarCategoria.setFont(new Font("Arial", Font.BOLD, 16));
        adicionarCategoria.setPreferredSize(new Dimension(0, 60));
        adicionarCategoria.addActionListener(e -> popup.abrirAdicaoDeCategoria(card, this));
        botoesAdiciona.add(adicionarCategoria);
        
        add(botoesAdiciona, BorderLayout.NORTH);
        
        painelListagem = new JPanel();
        painelListagem.setLayout(new BoxLayout(painelListagem, BoxLayout.Y_AXIS));
        painelListagem.setBackground(Cores.CINZA.cor);
        painelListagem.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        carregarProdutos();
        
        JScrollPane scroll = new JScrollPane(painelListagem);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(BorderFactory.createLineBorder(Cores.CINZA.cor));
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);
    }

    @SuppressWarnings("serial")
	private JPanel criarItemProduto(Pedido pedido, Cardapio card, int x) {
        JPanel painelItem = new JPanel(new BorderLayout(10, 0));
        painelItem.setBackground(Cores.BRANCO.cor);
        Border bordaItem = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Cores.CINZA.cor),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        );
        painelItem.setBorder(bordaItem);
        painelItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        painelItem.setPreferredSize(new Dimension(400, 90));

        JPanel painelInfo = new JPanel();
        painelInfo.setLayout(new BoxLayout(painelInfo, BoxLayout.Y_AXIS));
        painelInfo.setOpaque(false);
        painelInfo.add(new JLabel(x + ": " + pedido.getNome()) {{ setFont(new Font("Arial", Font.BOLD, 16)); }});
        painelInfo.add(Box.createRigidArea(new Dimension(0, 3)));
        painelInfo.add(new JLabel(pedido.getCategoria()) {{ setFont(new Font("Arial", Font.ITALIC, 12)); }});
        painelInfo.add(Box.createRigidArea(new Dimension(0, 3)));
        painelInfo.add(new JLabel(String.format("R$ %.2f", pedido.getPreco())) {{ setFont(new Font("Arial", Font.PLAIN, 14)); }});
        painelItem.add(painelInfo, BorderLayout.WEST);

        JButton statusButton = new JButton();
        statusButton.setFont(new Font("Arial", Font.BOLD, 12));
        statusButton.setForeground(Cores.BRANCO.cor);
        statusButton.setFocusPainted(false);
        statusButton.setPreferredSize(new Dimension(140, 70));
        statusButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		pedido.alterarNoCardapio();
        		alterarSeEstaNoCardapio(pedido, statusButton);
        		revalidate();
        		repaint();
        	}
        });
        alterarSeEstaNoCardapio(pedido, statusButton);
        JPanel painelStatusWrapper = new JPanel(new GridBagLayout());
        painelStatusWrapper.setOpaque(false);
        painelStatusWrapper.add(statusButton);

        JPanel painelBotoesAcao = new JPanel();
        painelBotoesAcao.setLayout(new BoxLayout(painelBotoesAcao, BoxLayout.Y_AXIS));
        painelBotoesAcao.setOpaque(false);
        JButton editarButton = new JButton("Editar");
        JButton excluirButton = new JButton("Excluir");
        
        editarButton.addActionListener(e -> {
        	popup.abrirEdicaoDeComandas(card, pedido, comandasAtivas, this);
        });
        excluirButton.addActionListener(e -> popup.abrirExcluirItem(card, pedido, comandasAtivas, this));
        
        Dimension tamanhoBotaoAcao = new Dimension(100, 35);
        configurarBotaoAcao(editarButton, Cores.AZUL.cor, Cores.BRANCO.cor, tamanhoBotaoAcao);
        if(pedido.getJaFoiPedido()) {
        	configurarBotaoAcao(excluirButton, Cores.CINZA_ESCURO.cor, Cores.CINZA.cor, tamanhoBotaoAcao);
        } else {
        	configurarBotaoAcao(excluirButton, Cores.VERMELHO.cor, Cores.BRANCO.cor, tamanhoBotaoAcao);
        }
        painelBotoesAcao.add(editarButton);
        painelBotoesAcao.add(Box.createRigidArea(new Dimension(0, 5)));
        painelBotoesAcao.add(excluirButton);
        
        JPanel painelTodosOsBotoes = new JPanel(new GridBagLayout());
        painelTodosOsBotoes.add(painelStatusWrapper);
        painelTodosOsBotoes.add(Box.createRigidArea(new Dimension(5, 0)));
        painelTodosOsBotoes.add(painelBotoesAcao);
        painelTodosOsBotoes.setBackground(Cores.BRANCO.cor);
        painelItem.add(painelTodosOsBotoes, BorderLayout.EAST);

        return painelItem;
    }

	private void alterarSeEstaNoCardapio(Pedido pedido, JButton statusButton) {
		if (pedido.getEstaNoCardapio()) {
            statusButton.setText("<html><center>Está no<br>Cardápio</center></html>");
            statusButton.setBackground(Cores.VERDE.cor);
        } else {
            statusButton.setText("<html><center>Não está<br>no Cardápio</center></html>");
            statusButton.setBackground(Cores.VERMELHO.cor);
            removerOPedidoDasComandas(pedido);
        }
	}

	void removerOPedidoDasComandas(Pedido pedido) {
		for(Comanda comanda : comandasAtivas) {
			comanda.removerTodosPedidos(pedido);
		}
	}

    private void configurarBotaoAcao(JButton botao, Color corFundo, Color corTexto, Dimension tamanho) {
        botao.setBackground(corFundo);
        botao.setForeground(corTexto);
        botao.setFont(new Font("Arial", Font.BOLD, 12));
        botao.setPreferredSize(tamanho);
        botao.setMinimumSize(tamanho);
        botao.setMaximumSize(tamanho);
        botao.setFocusPainted(false);
    }

	void atualizarVisuCardapio() {
		painelListagem.removeAll();
		carregarProdutos();
		revalidate();
		repaint();
	}

	private void carregarProdutos() {
		card.getListaItensCardapio().sort(Comparator.comparing(Pedido::getCategoria).thenComparing(Pedido::getNome));
		int x = 1;
		String ultimaCategoria = "";
		for(Pedido pedidou : card.getListaItensCardapio()) {
			if(pedidou.getCategoria() != ultimaCategoria) {
				painelListagem.add(Box.createRigidArea(new Dimension(0, 8)));
				ultimaCategoria = pedidou.getCategoria();
				JLabel lblCatAtual = new JLabel(ultimaCategoria);
				lblCatAtual.setForeground(Cores.BRANCO.cor);
				lblCatAtual.setFont(new Font("Arial", Font.BOLD, 20));
				JPanel painelItem = new JPanel(new BorderLayout(10, 0));
				painelItem.setBackground(Cores.CINZA_ESCURO.cor);
		        Border bordaItem = BorderFactory.createCompoundBorder(
		                BorderFactory.createMatteBorder(0, 0, 1, 0, Cores.CINZA.cor),
		                BorderFactory.createEmptyBorder(10, 15, 10, 15)
		        );
		        painelItem.setBorder(bordaItem);
				painelItem.add(lblCatAtual);
				painelListagem.add(painelItem);
			}
			painelListagem.add(criarItemProduto(pedidou, card, x));
			x++;
			
		}
	}

}
