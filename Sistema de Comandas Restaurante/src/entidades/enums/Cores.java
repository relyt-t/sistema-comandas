package entidades.enums;

import java.awt.Color;

public enum Cores {

    VERDE(new Color(108, 199, 55)),
    AZUL(new Color(99, 142, 206)),
    VERMELHO(new Color(219, 80, 80)),
    BRANCO(new Color(255, 255, 255)),
    CINZA(new Color(217, 217, 217)),
    CINZA_ESCURO(new Color(104, 104, 104)),
    PRETO(new Color(0));

	public Color cor;
	
    Cores(Color cor) {
    	this.cor = cor;
    }
}
