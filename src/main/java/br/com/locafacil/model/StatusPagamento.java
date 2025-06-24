package br.com.locafacil.model;

/**
 * Enum que representa os possíveis estados de um pagamento no sistema.
 * Define o ciclo de vida de um pagamento, desde sua criação até confirmação ou cancelamento.
 */
public enum StatusPagamento {
    /**
     * Pagamento registrado, mas ainda não confirmado.
     */
    PENDENTE,

    /**
     * Pagamento confirmado e processado com sucesso.
     */
    CONFIRMADO,

    /**
     * Pagamento cancelado antes de sua confirmação.
     */
    CANCELADO
}
