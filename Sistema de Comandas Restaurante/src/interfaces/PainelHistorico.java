package interfaces;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import entidades.Historico;
import entidades.Pedido;
import entidades.enums.Cores;
import entidades.Cardapio;

public class PainelHistorico extends JPanel {
    private static final long serialVersionUID = 202506011623L;
    
    JPanel painelListaHistoricos;
    JPanel painelResumoPedidos;
    JPanel painelResumoCategorias;
    JPanel painelResumoSemanas;
    JPanel painelResumoMaioresDias;
    JPanel painelResumos;
    
    private List<Historico> historico;
    private Cardapio card;
    
    private Calendar dataAgora = Calendar.getInstance();

    public PainelHistorico(List<Historico> historico, Cardapio card) {
        setLayout(new BorderLayout(10, 0));
        setBackground(Cores.CINZA.cor);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.historico = historico;
        this.card = card;
        
        int larguraHistorico = 500;
        int larguraResumo = 350;

        // PAINEL DA ESQUERDA - HISTÓRICO
        painelListaHistoricos = new JPanel();
        painelListaHistoricos.setLayout(new BoxLayout(painelListaHistoricos, BoxLayout.Y_AXIS));
        painelListaHistoricos.setBackground(Cores.CINZA_ESCURO.cor);

        carregarHistorico();

        JScrollPane scrollHistoricos = new JScrollPane(painelListaHistoricos);
        scrollHistoricos.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollHistoricos.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollHistoricos.setBorder(BorderFactory.createLineBorder(Cores.CINZA.cor));
        scrollHistoricos.setPreferredSize(new Dimension(larguraHistorico, 0));
        scrollHistoricos.setMinimumSize(new Dimension(larguraHistorico, 0));
        scrollHistoricos.setMaximumSize(new Dimension(larguraHistorico, Integer.MAX_VALUE));

        // PAINEL DA DIREITA - RESUMO/ESTATÍSTICAS
        JPanel painelDaDireita = new JPanel();
        painelDaDireita.setLayout(new BoxLayout(painelDaDireita, BoxLayout.Y_AXIS));
        painelDaDireita.setBackground(Cores.CINZA.cor);
        painelDaDireita.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel painelMoverMeses = new JPanel(new GridLayout(0, 4));
        painelMoverMeses.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Criando painel para o mês centralizado
        JPanel panelMes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblMesAtual = new JLabel(retornaMes(dataAgora.get(Calendar.MONTH)));
        lblMesAtual.setFont(new Font("Arial", Font.BOLD, 16));
        panelMes.add(lblMesAtual);

        // Criando painel para o ano centralizado
        JPanel panelAno = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblAnoAtual = new JLabel("" + dataAgora.get(Calendar.YEAR));
        lblAnoAtual.setFont(new Font("Arial", Font.BOLD, 16));
        panelAno.add(lblAnoAtual);

        // Botões
        JButton btnAnteriorMes = new JButton("Anterior");
        btnAnteriorMes.setBackground(Cores.VERMELHO.cor);
        btnAnteriorMes.setForeground(Cores.BRANCO.cor);
        btnAnteriorMes.addActionListener(e -> irAnteriorMes(lblMesAtual, lblAnoAtual));
        JButton btnProximoMes = new JButton("Próximo");
        btnProximoMes.setBackground(Cores.VERDE.cor);
        btnProximoMes.setForeground(Cores.BRANCO.cor);
        btnProximoMes.addActionListener(e -> irProximoMes(lblMesAtual, lblAnoAtual));

        // Adicionando na ordem desejada (exemplo)
        painelMoverMeses.add(btnAnteriorMes);
        painelMoverMeses.add(panelMes);
        painelMoverMeses.add(panelAno);
        painelMoverMeses.add(btnProximoMes);
        
        painelDaDireita.add(painelMoverMeses);
        
        painelDaDireita.add(Box.createRigidArea(new Dimension(0, 20)));
        
        painelResumos = new JPanel();
        painelResumos.setLayout(new BoxLayout(painelResumos, BoxLayout.Y_AXIS));
        painelResumos.setBackground(Cores.CINZA.cor);
        painelResumos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        carregarResumos();
		
		JScrollPane scrollResumo = new JScrollPane(painelResumos);
        scrollResumo.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollResumo.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollResumo.setBorder(BorderFactory.createLineBorder(Cores.CINZA.cor));
        scrollResumo.setPreferredSize(new Dimension(larguraResumo, 0));
        scrollResumo.setMinimumSize(new Dimension(larguraResumo, 0));
        scrollResumo.setMaximumSize(new Dimension(larguraResumo, Integer.MAX_VALUE));

        painelDaDireita.add(scrollResumo);
        
        // ADICIONA OS PAINÉIS NO PAINEL PRINCIPAL
        add(scrollHistoricos, BorderLayout.CENTER);
        add(painelDaDireita, BorderLayout.EAST);
    }

	private void carregarResumos() {
		List<Integer[]> quantidade = new ArrayList<>();
        List<Integer[]> categFav = new ArrayList<>();
        List<Integer[]> semanasList = new ArrayList<>();
        List<Integer[]> diasDoMesList = new ArrayList<>();
        
        for(Pedido item : card.getListaItensCardapio()) {
        	Integer[] arrayItem = {card.getListaItensCardapio().indexOf(item), 0};
        	quantidade.add(arrayItem);
	        for(Historico historicoInd : historico) {
	        	Calendar dataHist = Calendar.getInstance();
	        	dataHist.setTime(historicoInd.getDataDoPedido());
	        	
	        	if(dataHist.get(Calendar.MONTH) == dataAgora.get(Calendar.MONTH) &&
	        	   dataHist.get(Calendar.YEAR) == dataAgora.get(Calendar.YEAR)) {
		        	List<Pedido> individualItems = historicoInd.getComanda().getListaDePedidos();
		        	for(Pedido individual : individualItems) {
		        		if(arrayItem[0] == card.getListaItensCardapio().indexOf(individual)) {
		        			arrayItem[1]++;
		        		}
		        	}
	        	}
	        }
        }
        
        for(String item : card.getCategorias()) {
        	Integer[] arrayItem = {card.getCategorias().indexOf(item), 0};
        	categFav.add(arrayItem);
	        for(Historico historicoInd : historico) {
	        	Calendar dataHist = Calendar.getInstance();
	        	dataHist.setTime(historicoInd.getDataDoPedido());
	        	
	        	if(dataHist.get(Calendar.MONTH) == dataAgora.get(Calendar.MONTH) &&
	        	   dataHist.get(Calendar.YEAR) == dataAgora.get(Calendar.YEAR)) {
		        	List<Pedido> individualItems = historicoInd.getComanda().getListaDePedidos();
		        	for(Pedido individual : individualItems) {
		        		if(arrayItem[0] == card.getCategorias().indexOf(individual.getCategoria())) {
		        			arrayItem[1]++;
		        		}
		        	}
	        	}
	        }
        }
        
        for(int x = 0; x < 7; x++) {
        	Integer[] semana = {x, 0};
        	for(Historico historicoInd : historico) {
        		Calendar dataHist = Calendar.getInstance();
            	dataHist.setTime(historicoInd.getDataDoPedido());
            	
            	if(dataHist.get(Calendar.MONTH) == dataAgora.get(Calendar.MONTH) &&
            	   dataHist.get(Calendar.YEAR) == dataAgora.get(Calendar.YEAR)) 
	            	if(dataHist.get(Calendar.DAY_OF_WEEK) == x) 
	            		semana[1]++;
	            	
            	
        	}
        	semanasList.add(semana);
        }
        
        for(int x = 1; x <= dataAgora.getActualMaximum(Calendar.DAY_OF_MONTH); x++) {
        	Integer[] diaDoMes = {x, 0};
        	for(Historico historicoInd : historico) {
        		Calendar dataHist = Calendar.getInstance();
            	dataHist.setTime(historicoInd.getDataDoPedido());
            	
            	if(dataHist.get(Calendar.MONTH) == dataAgora.get(Calendar.MONTH) &&
            	   dataHist.get(Calendar.YEAR) == dataAgora.get(Calendar.YEAR)) 
	            	if(dataHist.get(Calendar.DAY_OF_MONTH) == x) 
	            		diaDoMes[1]++;
	            	
            	
        	}
        	diasDoMesList.add(diaDoMes);
        }
        
        quantidade.sort((a, b) -> Integer.compare(b[1], a[1]));
        
        if (quantidade.size() > 5) {
            quantidade = quantidade.subList(0, 5);
        }
        
        List<String> pedidosPopulares = new ArrayList<>();
        for (Integer[] q : quantidade) {
	        String item = card.getListaItensCardapio().get(q[0]).getNome();
	        pedidosPopulares.add(item + " (" + q[1] + ")");
        }
        
        categFav.sort((a, b) -> Integer.compare(b[1], a[1]));
        
        List<String> categoriasPopulares = new ArrayList<>();
        for (Integer[] c : categFav) {
	        String item = card.getCategorias().get(c[0]);
	        categoriasPopulares.add(item + " (" + c[1] + ")");
        }
        
        semanasList.sort((a, b) -> Integer.compare(b[1], a[1]));
        
        List<String> diasDeSemanaPopulares = new ArrayList<>();
        for (Integer[] d : semanasList) {
	        diasDeSemanaPopulares.add(retornaSemana(d[0]) + " (" + d[1] + ")");
        }
        
        diasDoMesList.sort((a, b) -> Integer.compare(b[1], a[1]));
        diasDoMesList = diasDoMesList.subList(0, 5);
        
        List<String> diasDoMesPopulares = new ArrayList<>();
        for(Integer[] d : diasDoMesList) {
        	if(d[1] > 0)
        		diasDoMesPopulares.add("Dia " + d[0] + " (" + d[1] + ")");
        }
        
        painelResumoPedidos = criarSecaoEstatistica("Pedidos Populares:", pedidosPopulares.toArray(new String[0]));
        painelResumoCategorias = criarSecaoEstatistica("Categorias Populares:", categoriasPopulares.toArray(new String[0]));
        painelResumoSemanas = criarSecaoEstatistica("Dias de Semana Populares:", diasDeSemanaPopulares.toArray(new String[0]));
        painelResumoMaioresDias = criarSecaoEstatistica("Dias Populares do Mês:", diasDoMesPopulares.toArray(new String[0]));
	
        JLabel lblTituloResumo = new JLabel("Resumo do Mês:");
        lblTituloResumo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTituloResumo.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelResumos.add(lblTituloResumo);
        painelResumos.add(Box.createRigidArea(new Dimension(0, 15)));
        painelResumos.add(painelResumoPedidos);
        painelResumos.add(Box.createRigidArea(new Dimension(0, 10)));
        painelResumos.add(painelResumoCategorias);
		painelResumos.add(Box.createRigidArea(new Dimension(0,10)));
		painelResumos.add(painelResumoSemanas);

		painelResumos.add(Box.createRigidArea(new Dimension(0,10)));
		painelResumos.add(painelResumoMaioresDias);
	}

    void irProximoMes(JLabel mes, JLabel ano) {
    	dataAgora.set(dataAgora.get(Calendar.YEAR), dataAgora.get(Calendar.MONTH)+1, dataAgora.get(Calendar.DAY_OF_MONTH));
    	if(dataAgora.get(Calendar.MONTH) == 0) {
    		ano.setText(""+dataAgora.get(Calendar.YEAR));
    	}
    	mes.setText(retornaMes(dataAgora.get(Calendar.MONTH)));
    	atualizaHistorico();
    	revalidate();
    	repaint();
    }
    
    void irAnteriorMes(JLabel mes, JLabel ano) {
    	dataAgora.set(dataAgora.get(Calendar.YEAR), dataAgora.get(Calendar.MONTH)-1, dataAgora.get(Calendar.DAY_OF_MONTH));
    	if(dataAgora.get(Calendar.MONTH) == 11) {
    		ano.setText(""+dataAgora.get(Calendar.YEAR));
    	}
    	mes.setText(retornaMes(dataAgora.get(Calendar.MONTH)));
    	atualizaHistorico();
    	revalidate();
    	repaint();
    }
    
    // Métodos fictícios usados no exemplo original
	private JPanel criarItemHistorico(Historico historico) {
		JPanel item = new JPanel();
		item.setLayout(new BoxLayout(item, BoxLayout.Y_AXIS));
		item.setBackground(Cores.BRANCO.cor);
		item.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.LIGHT_GRAY),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)
		));
		item.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Permite ocupar toda a largura disponível
		item.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

		Font fonteTextoComum = new Font("Arial", Font.PLAIN, 16);
		Font fonteTextoMaiorzinho = new Font("Arial", Font.PLAIN, 20);
		Font fonteTextoGrandao = new Font("Arial", Font.PLAIN, 26);

		Calendar horas = Calendar.getInstance();
		horas.setTime(historico.getDataDoPedido());
		
		item.add(criarLabel(String.format("Hora: " + horas.get(Calendar.HOUR_OF_DAY) + ":%02d", horas.get(Calendar.MINUTE)), fonteTextoMaiorzinho));
		item.add(criarLabel(historico.obterNomeDaComanda(), fonteTextoGrandao));
		item.add(criarLabel("Pedidos:", fonteTextoMaiorzinho));
		
		String[] produtos = historico.getDadosProdutos().split("\n");
		
		for (String pedido : produtos) {
			item.add(criarLabel("  \u2022 " + pedido.replace("- ", ""), fonteTextoComum));
		}

		if(historico.obterDesconto() != 0.0f) {
			item.add(criarLabel(String.format("Valor: R$ %.2f", historico.obterPrecoBruto()), fonteTextoMaiorzinho));
			item.add(criarLabel(String.format("Desconto: %.2f %%", historico.obterDesconto()*100), fonteTextoMaiorzinho));
		}
		item.add(criarLabel(String.format("Total: R$ %.2f", historico.obterPreco()), fonteTextoGrandao));

		return item;
	}


	private JLabel criarLabel(String texto, Font fonte) {
		JLabel label = new JLabel(texto);
		label.setFont(fonte);
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		label.setMaximumSize(new Dimension(Integer.MAX_VALUE, label.getPreferredSize().height));
		return label;
	}

    private JPanel criarSecaoEstatistica(String titulo, String[] itens) {
        JPanel secao = new JPanel();
        secao.setLayout(new BoxLayout(secao, BoxLayout.Y_AXIS));
        secao.setAlignmentX(Component.LEFT_ALIGNMENT);
        secao.setBackground(Cores.CINZA.cor);
        secao.add(new JLabel(titulo));
        for (String item : itens) {
            secao.add(new JLabel("- " + item));
        }
        return secao;
    }
    
    private String retornaMes(int mes) {
    	switch(mes+1) {
    		case 1:
    			return "Janeiro";
    		case 2:
    			return "Fevereiro";
    		case 3:
    			return "Março";
    		case 4:
    			return "Abril";
    		case 5:
    			return "Maio";
    		case 6:
    			return "Junho";
    		case 7:
    			return "Julho";
    		case 8:
    			return "Agosto";
    		case 9:
    			return "Setembro";
    		case 10:
    			return "Outubro";
    		case 11:
    			return "Novembro";
    		case 12:
    			return "Dezembro";
    		default:
    			return "mesInvalido";
    	}
    }
    
    private String retornaSemana(int dia) {
    	switch(dia+1) {
    		case 1:
    			return "Sábado";
    		case 2:
    			return "Domingo";
    		case 3:
    			return "Segunda";
    		case 4:
    			return "Terça";
    		case 5:
    			return "Quarta";
    		case 6:
    			return "Quinta";
    		case 7:
    			return "Sexta";
    		default:
    			return "semanaInvalido";
    	}
    }
    
    void atualizaHistorico() {
    	painelListaHistoricos.removeAll();
    	carregarHistorico();
    	painelResumos.removeAll();
    	carregarResumos();
    	revalidate();
    	repaint();
    }

	private void carregarHistorico() {
		int dia = -1;
		List<JLabel> lblTitulos = new ArrayList<>();
		painelListaHistoricos.add(Box.createRigidArea(new Dimension(0, 10)));
		int x = -1;
		double precoTotalDoDia = 0.0;
        for (Historico historicoInd : historico) {
        	Calendar dataHist = Calendar.getInstance();
        	dataHist.setTime(historicoInd.getDataDoPedido());
        	
        	if(dataHist.get(Calendar.MONTH) == dataAgora.get(Calendar.MONTH) &&
        	   dataHist.get(Calendar.YEAR) == dataAgora.get(Calendar.YEAR)) {
	        	int diaDoHist = dataHist.get(Calendar.DAY_OF_MONTH);
	        	
	        	if(dia != diaDoHist) {
	        		if(!lblTitulos.isEmpty()) {
	        			lblTitulos.get(x).setText(lblTitulos.get(x).getText().replace("sas\n", "") + String.format(" Total: R$ %.2f", precoTotalDoDia));
	        		}
	        		JLabel lblTitulo = new JLabel("  Dia: " + diaDoHist + " (" + retornaSemana(dataHist.get(Calendar.DAY_OF_WEEK)).substring(0, 3) + ")sas\n");
		            lblTitulo.setFont(new Font("Arial", Font.BOLD, 30));
		            lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
		            lblTitulo.setForeground(Cores.BRANCO.cor);
		            painelListaHistoricos.add(lblTitulo);
		            lblTitulos.add(lblTitulo);
		            painelListaHistoricos.add(Box.createRigidArea(new Dimension(0, 10)));
		            x++;
		            precoTotalDoDia = 0.0;
	        	}
	        	
	        	dia = diaDoHist;
	        	
	        	precoTotalDoDia += historicoInd.obterPreco();
	        	
				painelListaHistoricos.add(criarItemHistorico(historicoInd));
				painelListaHistoricos.add(Box.createRigidArea(new Dimension(0, 10)));
        	}
        }
        if(!lblTitulos.isEmpty())
	        if(lblTitulos.get(x).getText().contains("sas\n"))
	        	lblTitulos.get(x).setText(lblTitulos.get(x).getText().replace("sas\n", "") + String.format(" Total: R$ %.2f", precoTotalDoDia));
	}
}
