package br.com.locafacil.model;

/**
 * Enum que representa os métodos de pagamento disponíveis no sistema.
 * Define as formas de pagamento que podem ser utilizadas nos contratos de locação.
 */
public enum MetodoPagamento {
    /**
     * Pagamento em dinheiro físico.
     */
    DINHEIRO,

    /**
     * Pagamento com cartão de crédito ou débito.
     */
    CARTAO,

    /**
     * Pagamento via transferência bancária.
     */
    TRANSFERENCIA,

    /**
     * Pagamento via boleto bancário.
     */
    BOLETO
}
