# Sistema de Comandas para Restaurante

Um sistema de gerenciamento de comandas e pedidos desenvolvido em **Java**, focado em simplicidade e eficiência para pequenos estabelecimentos.

## 📋 Sobre o Projeto
Este projeto é uma aplicação Desktop que permite o controle de mesas, comandas individuais e cardápio. Foi desenvolvido para reforçar conceitos de Programação Orientada a Objetos (POO), manipulação de estruturas de dados e construção de interfaces gráficas.

## 🚀 Funcionalidades
*   **Gerenciamento de Cardápio:** Adição e remoção de itens com preços personalizados.
*   **Controle de Comandas:**
    *   Abertura de mesas e comandas individuais.
    *   Adição de pedidos às comandas.
    *   Cálculo automático de totais e descontos.
*   **Histórico de Vendas:** Registro de pedidos finalizados com cálculo de faturamento (Bruto, Descontos e Líquido) do dia.
*   **Persistência de Dados:** O sistema salva automaticamente as informações (cardápio, histórico, comandas abertas) em arquivo local, garantindo que nenhum dados seja perdido ao fechar a aplicação.

## 🛠️ Tecnologias Utilizadas
*   **Linguagem:** Java (JDK 8+)
*   **Interface Gráfica (GUI):** Swing (JFrame, JPanel, CardLayout)
*   **Armazenamento:** Serialização de objetos Java (arquivos `.bin`)
*   **Padrão de Projeto:** MVC (Model-View-Controller) simplificado

## 📦 Como Usar
1.  Tenha o Java instalado na sua máquina.
2.  Compile os arquivos `.java` na pasta `src`.
3.  Execute a classe principal `TelaPrincipal`.
4.  O sistema criará automaticamente a pasta de salvamento (`C:/salvamento` ou similar configurado no código) na primeira execução.
