package br.com.locafacil.model;

/**
 * Enum que representa os possíveis estados de um contrato de locação.
 * Define o ciclo de vida de um contrato, desde a reserva até seu encerramento ou cancelamento.
 */
public enum StatusContrato {
    /**
     * Contrato em estado de reserva, ainda não iniciado.
     */
    RESERVA,

    /**
     * Contrato ativo, com veículo em posse do cliente.
     */
    ATIVO,

    /**
     * Contrato encerrado normalmente, com devolução do veículo.
     */
    ENCERRADO,

    /**
     * Contrato cancelado antes de sua conclusão normal.
     */
    CANCELADO
}
