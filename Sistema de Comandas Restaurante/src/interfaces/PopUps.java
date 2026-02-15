package interfaces;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import entidades.Cardapio;
import entidades.Comanda;
import entidades.Historico;
import entidades.Mesa;
import entidades.Pedido;
import entidades.enums.Cores;

public class PopUps {
	
	void abrirAdicaoDeProduto(Cardapio card, PainelCardapio painelCard) {
        JDialog popup = new JDialog(); // modal
        popup.setSize(400, 300);
        popup.setTitle("Adicionar Produto");
        popup.setLocationRelativeTo(painelCard);
        popup.setModal(true);

        if(card.getCategorias().isEmpty()) {
    		JOptionPane.showMessageDialog(popup, "Não é possível criar um produto sem categorias!", "Erro", JOptionPane.ERROR_MESSAGE);
    		return;
    	}
        
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel labelProduto = new JLabel("Nome do Produto:");
        labelProduto.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(labelProduto);

        JTextField campoProduto = new JTextField();
        campoProduto.setMaximumSize(new Dimension(350, 30));
        campoProduto.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(campoProduto);

        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel labelCategoria = new JLabel("Categoria:");
        labelCategoria.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(labelCategoria);

        String[] categoriasExistentes = card.getCategorias().toArray(new String[0]);

        JComboBox<String> categoria = new JComboBox<>(categoriasExistentes);
        categoria.setMaximumSize(new Dimension(350, 30));
        categoria.setAlignmentX(Component.CENTER_ALIGNMENT);
        categoria.setBackground(Cores.BRANCO.cor);
        painel.add(categoria);

        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel labelPreco = new JLabel("Preço (R$):");
        labelPreco.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(labelPreco);

        JTextField precoProduto = new JTextField();
        precoProduto.setMaximumSize(new Dimension(350, 30));
        precoProduto.setAlignmentX(Component.CENTER_ALIGNMENT);

        // aplicação do filtro que permite apenas números e ponto
        ((AbstractDocument) precoProduto.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null && string.matches("[0-9,]*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String novoTexto = fb.getDocument().getText(0, fb.getDocument().getLength());
                novoTexto = novoTexto.substring(0, offset) + (text != null ? text : "") + novoTexto.substring(offset + length);

                // permite apenas uma vírgula como separador decimal
                if (novoTexto.matches("[0-9]*[,]?[0-9]*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        painel.add(precoProduto);

        painel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton botaoSalvar = new JButton("Salvar");
        botaoSalvar.setBackground(Cores.VERDE.cor);
        botaoSalvar.setForeground(Cores.BRANCO.cor);
        botaoSalvar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoSalvar.addActionListener(e -> {
            String produto = campoProduto.getText();
            if(produto.length() < 3) {
            	JOptionPane.showMessageDialog(popup, "Insira um nome maior!", "Erro", JOptionPane.ERROR_MESSAGE);
            	return;
            }
            String categoriaSelecionada = (String) categoria.getSelectedItem();
            String precoTexto = precoProduto.getText().replace(",", ".");
            
            for(Pedido pedido : card.getListaItensCardapio()) {
            	if(pedido.getNome().equals(produto) && pedido.getCategoria().equals(categoriaSelecionada)) {
            		JOptionPane.showMessageDialog(popup, "Já há um produto com este nome nesta categoria!", "Erro", JOptionPane.ERROR_MESSAGE);
            		return;
            	}
            }
            
            try {
                double preco = Double.parseDouble(precoTexto);
                System.out.println("Pedido adicionado: " + produto + " - " + categoriaSelecionada + " - R$" + preco);
                Pedido pedido = new Pedido(produto, preco, categoriaSelecionada);
                card.adicionarItemNoCardapio(pedido);
                popup.dispose();
                painelCard.atualizarVisuCardapio();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(popup, "Preço inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        painel.add(botaoSalvar);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        popup.add(painel);
        popup.setVisible(true);
    }
	
	void abrirEdicaoDeComandas(Cardapio card, Pedido pedido, List<Comanda> comandasAtivas, PainelCardapio painelCard) {
        JDialog popup = new JDialog(); // modal
        popup.setSize(400, 300);
        popup.setTitle("Editar Produto");
        popup.setLocationRelativeTo(painelCard);
        popup.setModal(true);

        for(Comanda comanda : comandasAtivas) {
        	for(Pedido pedidoA : comanda.getListaDePedidos()) {
        		if(pedidoA.equals(pedido)) {
        			JOptionPane.showMessageDialog(popup, "Este Produto está em uma comanda! Retire-o para editar depois!", "Erro", JOptionPane.ERROR_MESSAGE);
            		return;
        		}
        	}
        }
        
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel labelProduto = new JLabel("Nome do Produto: " + pedido.getNome());
        labelProduto.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(labelProduto);

        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel labelCategoria = new JLabel("Categoria: " + pedido.getCategoria());
        labelCategoria.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(labelCategoria);

        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel labelPreco = new JLabel("Preço (R$):");
        labelPreco.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(labelPreco);

        JTextField precoProduto = new JTextField((""+pedido.getPreco()).replace(".", ","));
        precoProduto.setMaximumSize(new Dimension(350, 30));
        precoProduto.setAlignmentX(Component.CENTER_ALIGNMENT);

        // aplicação do filtro que permite apenas números e ponto
        ((AbstractDocument) precoProduto.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null && string.matches("[0-9,]*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String novoTexto = fb.getDocument().getText(0, fb.getDocument().getLength());
                novoTexto = novoTexto.substring(0, offset) + (text != null ? text : "") + novoTexto.substring(offset + length);

                // permite apenas uma vírgula como separador decimal
                if (novoTexto.matches("[0-9]*[,]?[0-9]*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        painel.add(precoProduto);

        painel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton botaoSalvar = new JButton("Salvar");
        botaoSalvar.setBackground(Cores.VERDE.cor);
        botaoSalvar.setForeground(Cores.BRANCO.cor);
        botaoSalvar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoSalvar.addActionListener(e -> {
            String precoTexto = precoProduto.getText().replace(",", ".");
            
            try {
                double precoNum = Double.parseDouble(precoTexto);
                //card.getListaItensCardapio().get(x);
                pedido.alterarPreco(precoNum);
                popup.dispose();
                for(Comanda comanda : comandasAtivas) {
            		comanda.calculaPreco();
            	}
                painelCard.atualizarVisuCardapio();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(popup, "Preço inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        painel.add(botaoSalvar);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        popup.add(painel);
        popup.setVisible(true);
    }
	
	void abrirExcluirItem(Cardapio card, Pedido pedido, List<Comanda> comandasAtivas, PainelCardapio painelCard) {
    	JDialog popup = new JDialog(); // modal
        popup.setSize(400, 300);
        popup.setTitle("Excluir Produto");
        popup.setLocationRelativeTo(painelCard);
        popup.setModal(true);

        if(pedido.getJaFoiPedido()) {
    		JOptionPane.showMessageDialog(popup, "Este Produto já foi pedido! Ele não pode ser excluído!", "Erro", JOptionPane.ERROR_MESSAGE);
    		return;
    	}
        
        for(Comanda comanda : comandasAtivas) {
        	for(Pedido pedidoA : comanda.getListaDePedidos()) {
        		if(pedidoA.equals(pedido)) {
        			JOptionPane.showMessageDialog(popup, "Este Produto está em uma comanda! Retire-o para excluir depois!", "Erro", JOptionPane.ERROR_MESSAGE);
            		return;
        		}
        	}
        }
        
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel labelProduto = new JLabel("Deseja excluir o item " + pedido.getNome() + "?");
        labelProduto.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(labelProduto);

        painel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton botaoSalvar = new JButton("Sim");
        botaoSalvar.setBackground(Cores.VERDE.cor);
        botaoSalvar.setForeground(Cores.BRANCO.cor);
        botaoSalvar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoSalvar.addActionListener(e -> {
        	card.removerItemNoCardapio(pedido);
        	painelCard.removerOPedidoDasComandas(pedido);
        	popup.dispose();
        	painelCard.atualizarVisuCardapio();
        });
        
        JButton botaoSair = new JButton("Não");
        botaoSair.setBackground(Cores.VERMELHO.cor);
        botaoSair.setForeground(Cores.BRANCO.cor);
        botaoSair.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoSair.addActionListener(e -> popup.dispose());

        painel.add(botaoSalvar);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));
        painel.add(botaoSair);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        popup.add(painel);
        popup.setVisible(true);
    }
	
	void abrirAdicaoDeCategoria(Cardapio card, PainelCardapio painelCard) {
    	JDialog popup = new JDialog(); // modal
        popup.setSize(400, 300);
        popup.setTitle("Adicionar Categoria");
        popup.setLocationRelativeTo(painelCard);
        popup.setModal(true);

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        String textoCategorias = " - Categorias Existentes - \n";
        
        int x = 1;
        for(String categoria : card.getCategorias()) {
        	textoCategorias += x + " - " + categoria + "\n";
        	x++;
        }
        
        JTextPane categoriasExistentes = new JTextPane();
        categoriasExistentes.setMaximumSize(new Dimension(350, x*15));
        categoriasExistentes.setAlignmentX(Component.CENTER_ALIGNMENT);
        categoriasExistentes.setText(textoCategorias);
        StyledDocument doc = categoriasExistentes.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        categoriasExistentes.setEditable(false);
        painel.add(new JScrollPane(categoriasExistentes));
        
        painel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel labelCategoria = new JLabel("Categoria:");
        labelCategoria.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(labelCategoria);

        JTextField fieldCategoria = new JTextField("");
        fieldCategoria.setMaximumSize(new Dimension(350, 30));
        fieldCategoria.setAlignmentX(Component.CENTER_ALIGNMENT);

        painel.add(fieldCategoria);

        painel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton botaoSalvar = new JButton("Salvar");
        botaoSalvar.setBackground(Cores.VERDE.cor);
        botaoSalvar.setForeground(Cores.BRANCO.cor);
        botaoSalvar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoSalvar.addActionListener(e -> {
            String categoriaTexto = fieldCategoria.getText();
            
            if(categoriaTexto.length() < 3) {
                JOptionPane.showMessageDialog(popup, "Insira um nome maior para a categoria!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            card.adicionarCategoria(categoriaTexto);
            popup.dispose();
        });

        painel.add(botaoSalvar);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        popup.add(painel);
        popup.setVisible(true);
    }
	
	void abrirAdicaoDeMesas(List<Comanda> comandasAtivas, PainelComandas painelCom) {
    	JDialog popup = new JDialog(); // modal
        popup.setSize(400, 300);
        popup.setTitle("Adicionar Mesa");
        popup.setLocationRelativeTo(painelCom);
        popup.setModal(true);

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        
        painel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel labelMesa = new JLabel("Número pra Mesa:");
        labelMesa.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(labelMesa);

        JTextField fieldMesa = new JTextField("");
        fieldMesa.setMaximumSize(new Dimension(350, 30));
        fieldMesa.setAlignmentX(Component.CENTER_ALIGNMENT);

        ((AbstractDocument) fieldMesa.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null && string.matches("[0-9]*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }
            
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String novoTexto = fb.getDocument().getText(0, fb.getDocument().getLength());
                novoTexto = novoTexto.substring(0, offset) + (text != null ? text : "") + novoTexto.substring(offset + length);

                // permite apenas uma vírgula como separador decimal
                if (novoTexto.matches("[0-9]*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        
        painel.add(fieldMesa);

        painel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton botaoSalvar = new JButton("Salvar");
        botaoSalvar.setBackground(Cores.VERDE.cor);
        botaoSalvar.setForeground(Cores.BRANCO.cor);
        botaoSalvar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoSalvar.addActionListener(e -> {
            String numeroMesa = fieldMesa.getText();
            
            try {
                int numMesa = Integer.parseInt(numeroMesa);
                for(Comanda comanda : comandasAtivas) {
                	if(comanda instanceof Mesa) {
                		Mesa mesa = (Mesa) comanda;
                		if(mesa.getNumeroMesa() == numMesa)
                			numMesa = 0;
                	}
                }
                if(numMesa > 0) {
                	Comanda mesa = new Mesa(numMesa);
                	comandasAtivas.add(mesa);
                	painelCom.atualizarVisuComandas();
                	popup.dispose();
                } else {
                	JOptionPane.showMessageDialog(popup, "Insira um outro número de Mesa!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(popup, "Número inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        painel.add(botaoSalvar);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        popup.add(painel);
        popup.setVisible(true);
    }
	
	void abrirExcluirMesas(List<Comanda> comandasAtivas, PainelComandas painelCom) {
    	JDialog popup = new JDialog(); // modal
        popup.setSize(400, 300);
        popup.setTitle("Remover Mesa");
        popup.setLocationRelativeTo(painelCom);
        popup.setModal(true);

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        
        painel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel labelMesa = new JLabel("Insira um Número de uma Mesa Existente:");
        labelMesa.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(labelMesa);

        JTextField fieldMesa = new JTextField("");
        fieldMesa.setMaximumSize(new Dimension(350, 30));
        fieldMesa.setAlignmentX(Component.CENTER_ALIGNMENT);

        ((AbstractDocument) fieldMesa.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null && string.matches("[0-9]*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }
        });
        
        painel.add(fieldMesa);

        painel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton botaoExcluir = new JButton("Excluir");
        botaoExcluir.setBackground(Cores.VERMELHO.cor);
        botaoExcluir.setForeground(Cores.BRANCO.cor);
        botaoExcluir.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoExcluir.addActionListener(e -> {
            String numeroMesa = fieldMesa.getText();
            
            Comanda comandaParaExcluir = null;
            try {
                int numMesa = Integer.parseInt(numeroMesa);
                for(Comanda comanda : comandasAtivas) {
                	if(comanda instanceof Mesa) {
                		Mesa mesa = (Mesa) comanda;
                		if(mesa.getNumeroMesa() == numMesa) {
                			comandaParaExcluir = mesa;
                			break;
                		}
                	}
                }
                if(comandaParaExcluir != null) {
                	comandasAtivas.remove(comandaParaExcluir);
                	painelCom.atualizarVisuComandas();
                	popup.dispose();
                } else {
                	JOptionPane.showMessageDialog(popup, "Insira um outro número de Mesa!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(popup, "Número inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        painel.add(botaoExcluir);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        popup.add(painel);
        popup.setVisible(true);
    }
	
	void abrirAdicaoDeComandaSemMesa(List<Comanda> comandasAtivas, PainelComandas painelCom) {
    	JDialog popup = new JDialog(); // modal
        popup.setSize(400, 300);
        popup.setTitle("Adicionar Mesa");
        popup.setLocationRelativeTo(painelCom);
        popup.setModal(true);

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        
        painel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel labelComandaNome = new JLabel("Nome do Cliente para a Comanda:");
        labelComandaNome.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(labelComandaNome);

        JTextField fieldMesa = new JTextField("");
        fieldMesa.setMaximumSize(new Dimension(350, 30));
        fieldMesa.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        painel.add(fieldMesa);

        painel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton botaoSalvar = new JButton("Salvar");
        botaoSalvar.setBackground(Cores.VERDE.cor);
        botaoSalvar.setForeground(Cores.BRANCO.cor);
        botaoSalvar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoSalvar.addActionListener(e -> {
            String nomeCliente = fieldMesa.getText();
            
            try {
                for(Comanda comanda : comandasAtivas) {
                	if(!(comanda instanceof Mesa)) {
                		if(comanda.getNome().equals(nomeCliente))
                			nomeCliente = "";
                	}
                }
                if(nomeCliente.length() > 2) {
                	Comanda comanda = new Comanda(nomeCliente);
                	comandasAtivas.add(comanda);
                	painelCom.atualizarVisuComandas();
                	popup.dispose();
                } else {
                	JOptionPane.showMessageDialog(popup, "Insira um nome diferente para essa comanda!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(popup, "Nome inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        painel.add(botaoSalvar);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        popup.add(painel);
        popup.setVisible(true);
    }
	
	void abrirExclusaoDeComandaSemMesa(List<Comanda> comandasAtivas, Comanda comanda, PainelComandas painelCom) {
		JDialog popup = new JDialog(); // modal
        popup.setSize(400, 300);
        popup.setTitle("Excluir Comanda");
        popup.setLocationRelativeTo(painelCom);
        popup.setModal(true);

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel labelProduto = new JLabel("Deseja excluir a Comanda de " + comanda.getNome() + "?");
        labelProduto.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(labelProduto);

        painel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton botaoSalvar = new JButton("Sim");
        botaoSalvar.setBackground(Cores.VERDE.cor);
        botaoSalvar.setForeground(Cores.BRANCO.cor);
        botaoSalvar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoSalvar.addActionListener(e -> {
        	comandasAtivas.remove(comanda);
        	popup.dispose();
        	painelCom.atualizarVisuComandas();
        });
        
        JButton botaoSair = new JButton("Não");
        botaoSair.setBackground(Cores.VERMELHO.cor);
        botaoSair.setForeground(Cores.BRANCO.cor);
        botaoSair.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoSair.addActionListener(e -> popup.dispose());

        painel.add(botaoSalvar);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));
        painel.add(botaoSair);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        popup.add(painel);
        popup.setVisible(true);
	}
	
	void abrirCancelarComandaMesa(Comanda comanda, PainelComandas painelCom) {
		JDialog popup = new JDialog(); // modal
        popup.setSize(400, 300);
        popup.setTitle("Excluir Comanda");
        popup.setLocationRelativeTo(painelCom);
        popup.setModal(true);

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel labelProduto = new JLabel("Deseja excluir a Comanda de " + comanda.getNome() + "?");
        labelProduto.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(labelProduto);

        painel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton botaoSalvar = new JButton("Sim");
        botaoSalvar.setBackground(Cores.VERDE.cor);
        botaoSalvar.setForeground(Cores.BRANCO.cor);
        botaoSalvar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoSalvar.addActionListener(e -> {
        	comanda.cancelarComanda();
        	popup.dispose();
        	painelCom.atualizarVisuComandas();
        });
        
        JButton botaoSair = new JButton("Não");
        botaoSair.setBackground(Cores.VERMELHO.cor);
        botaoSair.setForeground(Cores.BRANCO.cor);
        botaoSair.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoSair.addActionListener(e -> popup.dispose());

        painel.add(botaoSalvar);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));
        painel.add(botaoSair);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        popup.add(painel);
        popup.setVisible(true);
	}
	
	void abrirAdicaoDeProdutoNaComanda(Comanda comanda, Cardapio card, PainelComandas painelCom) {
		JDialog popup = new JDialog(); // modal
        popup.setSize(400, 300);
        popup.setTitle("Adicionar Produto na Comanda");
        popup.setLocationRelativeTo(painelCom);
        popup.setModal(true);

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel labelCategorias = new JLabel("Categoria do Produto:");
        labelCategorias.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(labelCategorias);

        String[] categoriasExistentes = card.getCategorias().toArray(new String[0]);

        JComboBox<String> categoria = new JComboBox<>(categoriasExistentes);
        categoria.setMaximumSize(new Dimension(350, 30));
        categoria.setAlignmentX(Component.CENTER_ALIGNMENT);
        categoria.setBackground(Cores.BRANCO.cor);
        
        List<String> produtos = new ArrayList<>();

        String categoriaSelecionadaA = (String) categoria.getSelectedItem();
    	
    	for(Pedido pedido : card.getListaItensCardapio()) {
        	if(pedido.getCategoria().equals(categoriaSelecionadaA) && pedido.getEstaNoCardapio()) {
        		produtos.add(pedido.getNome());
        	}
        }
        
        JLabel labelProduto = new JLabel("Categoria:");
        labelProduto.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(labelProduto);
        
        String[] produtosExistentes = produtos.toArray(new String[0]);
        JComboBox<String> produto = new JComboBox<>(produtosExistentes);
        produto.setMaximumSize(new Dimension(350, 30));
        produto.setAlignmentX(Component.CENTER_ALIGNMENT);
        produto.setBackground(Cores.BRANCO.cor);
        
        categoria.addActionListener(e -> {
        	produtos.removeAll(produtos);
        	String categoriaSelecionada = (String) categoria.getSelectedItem();
        	
        	for(Pedido pedido : card.getListaItensCardapio()) {
            	if(pedido.getCategoria().equals(categoriaSelecionada) && pedido.getEstaNoCardapio()) {
            		produtos.add(pedido.getNome());
            	}
            }
        	
        	DefaultComboBoxModel<String> novoModelo = new DefaultComboBoxModel<>(produtos.toArray(new String[0]));
        	produto.setModel(novoModelo);
        });
        
        painel.add(categoria);

        painel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        painel.add(produto);

        painel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton botaoSalvar = new JButton("Salvar");
        botaoSalvar.setBackground(Cores.VERDE.cor);
        botaoSalvar.setForeground(Cores.BRANCO.cor);
        botaoSalvar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoSalvar.addActionListener(e -> {
            String produtoSelecionado = (String) produto.getSelectedItem();
            String categoriaSelecionada = (String) categoria.getSelectedItem();
            
            try {
            	Pedido pedidoEscolhido = null;
            	
                for(Pedido pedido : card.getListaItensCardapio()) {
                	if(pedido.getNome().equals(produtoSelecionado) && pedido.getCategoria().equals(categoriaSelecionada))
                		pedidoEscolhido = pedido;
                }
            	
                if(pedidoEscolhido != null) {
                	System.out.println(pedidoEscolhido.getNome());
                	comanda.adicionarPedido(pedidoEscolhido);
                	popup.dispose();
                    painelCom.atualizarVisuComandas();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(popup, "Algo deu Errado!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        painel.add(botaoSalvar);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        popup.add(painel);
        popup.setVisible(true);
    }
	
	void abrirEdicaoDeDesconto(Comanda comanda, PainelComandas painelCom) {
    	JDialog popup = new JDialog(); // modal
        popup.setSize(400, 300);
        popup.setTitle("Editar Desconto");
        popup.setLocationRelativeTo(painelCom);
        popup.setModal(true);

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        
        painel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel labelMesa = new JLabel("Editar o Desconto de " + comanda.getNome());
        labelMesa.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(labelMesa);

        String descontoAtual = ((comanda.getDesconto()*100)+"%").replace(".0", "");
        
        JTextField fieldMesa = new JTextField(descontoAtual);
        fieldMesa.setMaximumSize(new Dimension(350, 30));
        fieldMesa.setAlignmentX(Component.CENTER_ALIGNMENT);

        ((AbstractDocument) fieldMesa.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null && string.matches("[0-9%]*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }
            
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String novoTexto = fb.getDocument().getText(0, fb.getDocument().getLength());
                novoTexto = novoTexto.substring(0, offset) + (text != null ? text : "") + novoTexto.substring(offset + length);

                // permite apenas uma vírgula como separador decimal
                if (novoTexto.matches("[0-9]*[%]")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        
        painel.add(fieldMesa);

        painel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton botaoSalvar = new JButton("Salvar");
        botaoSalvar.setBackground(Cores.VERDE.cor);
        botaoSalvar.setForeground(Cores.BRANCO.cor);
        botaoSalvar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoSalvar.addActionListener(e -> {
            String numeroMesa = fieldMesa.getText().replace("%", ".0f");
            
            try {
                float numMesa = Float.parseFloat(numeroMesa);
                
                if(numMesa >= 0 && numMesa <= 100) {
                	comanda.editarDesconto(numMesa/100);
                	painelCom.atualizarVisuComandas();
                	popup.dispose();
                } else {
                	JOptionPane.showMessageDialog(popup, "Insira um Desconto Válido!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(popup, "Número inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        painel.add(botaoSalvar);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        popup.add(painel);
        popup.setVisible(true);
    }

	void abrirConcluirComandaMesa(Comanda comanda, List<Historico> historico, PainelComandas painelCom, PainelCardapio painelCard, PainelHistorico painelHist) {
		JDialog popup = new JDialog(); // modal
        popup.setSize(400, 300);
        popup.setTitle("Finalizar Comanda de Mesa");
        popup.setLocationRelativeTo(painelCom);
        popup.setModal(true);

        if(comanda.getListaDePedidos().size() == 0) {
    		JOptionPane.showMessageDialog(popup, "A Comanda deve conter algum produto!", "Erro", JOptionPane.ERROR_MESSAGE);
    		return;
    	}
        
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel labelComandaNome = new JLabel("Nome do Cliente para a essa Mesa (Opcional):");
        labelComandaNome.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(labelComandaNome);

        JTextField fieldMesa = new JTextField("");
        fieldMesa.setMaximumSize(new Dimension(350, 30));
        fieldMesa.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        painel.add(fieldMesa);
        
        painel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel labelProduto = new JLabel("Deseja finalizar a Comanda da " + comanda.getNome() + "?");
        labelProduto.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(labelProduto);

        JLabel labelInfo = new JLabel("Ao finalizar a mesa, ela será enviada para o histórico de vendas!");
        labelInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(labelInfo);
        
        painel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton botaoSalvar = new JButton("Sim");
        botaoSalvar.setBackground(Cores.VERDE.cor);
        botaoSalvar.setForeground(Cores.BRANCO.cor);
        botaoSalvar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoSalvar.addActionListener(e -> {
        	if(comanda.getListaDePedidos().size() == 0) {
        		JOptionPane.showMessageDialog(popup, "A Comanda deve conter algum produto!", "Erro", JOptionPane.ERROR_MESSAGE);
        	}
        	String nome = fieldMesa.getText();
        	Mesa mesa = (Mesa) comanda;
        	int numMesa = mesa.getNumeroMesa();
        	if(nome.length() > 2) {
        		comanda.setNome(nome);
        	} else if (nome.length() > 0) {
        		JOptionPane.showMessageDialog(popup, "Insira um Nome maior!", "Erro", JOptionPane.ERROR_MESSAGE);
        		return;
        	}
        	comanda.concluirComanda();
        	Historico historicoCom = new Historico(comanda);
        	historico.add(historicoCom);
        	painelCom.substituirComanda(numMesa, comanda);
        	popup.dispose();
        	painelCom.atualizarVisuComandas();
        	painelCom.atualizarVisuResultadosDoDia();
        	painelCard.atualizarVisuCardapio();
        	painelHist.atualizaHistorico();
        });
        
        JButton botaoSair = new JButton("Não");
        botaoSair.setBackground(Cores.VERMELHO.cor);
        botaoSair.setForeground(Cores.BRANCO.cor);
        botaoSair.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoSair.addActionListener(e -> popup.dispose());

        painel.add(botaoSalvar);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));
        painel.add(botaoSair);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        popup.add(painel);
        popup.setVisible(true);
	}
	
	void abrirConcluirComandaComum(Comanda comanda, List<Comanda> comandasAtivas, List<Historico> historico, PainelComandas painelCom, PainelCardapio painelCard, PainelHistorico painelHist) {
		JDialog popup = new JDialog(); // modal
        popup.setSize(400, 300);
        popup.setTitle("Finalizar Comanda");
        popup.setLocationRelativeTo(painelCom);
        popup.setModal(true);

        if(comanda.getListaDePedidos().size() == 0) {
    		JOptionPane.showMessageDialog(popup, "A Comanda deve conter algum produto!", "Erro", JOptionPane.ERROR_MESSAGE);
    		return;
    	}
        
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        painel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel labelProduto = new JLabel("Deseja finalizar a Comanda de " + comanda.getNome() + "?");
        labelProduto.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(labelProduto);

        JLabel labelInfo = new JLabel("Ao finalizar a mesa, ela será enviada para o histórico de vendas!");
        labelInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(labelInfo);
        
        painel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton botaoSalvar = new JButton("Sim");
        botaoSalvar.setBackground(Cores.VERDE.cor);
        botaoSalvar.setForeground(Cores.BRANCO.cor);
        botaoSalvar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoSalvar.addActionListener(e -> {
        	if(comanda.getListaDePedidos().size() == 0) {
        		JOptionPane.showMessageDialog(popup, "A Comanda deve conter algum produto!", "Erro", JOptionPane.ERROR_MESSAGE);
        	}
        	comanda.concluirComanda();
        	Historico historicoCom = new Historico(comanda);
        	historico.add(historicoCom);
        	comandasAtivas.remove(comanda);
        	popup.dispose();
        	painelCom.atualizarVisuComandas();
        	painelCom.atualizarVisuResultadosDoDia();
        	painelCard.atualizarVisuCardapio();
        	painelHist.atualizaHistorico();
        });
        
        JButton botaoSair = new JButton("Não");
        botaoSair.setBackground(Cores.VERMELHO.cor);
        botaoSair.setForeground(Cores.BRANCO.cor);
        botaoSair.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoSair.addActionListener(e -> popup.dispose());

        painel.add(botaoSalvar);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));
        painel.add(botaoSair);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        popup.add(painel);
        popup.setVisible(true);
	}
}
