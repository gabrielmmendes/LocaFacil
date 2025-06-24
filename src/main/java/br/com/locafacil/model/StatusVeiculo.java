package br.com.locafacil.model;

/**
 * Enumeração que representa os possíveis status de um veículo na locadora.
 */
public enum StatusVeiculo {

    /**
     * O veículo está disponível para reserva ou locação.
     */
    DISPONIVEL,

    /**
     * O veículo foi reservado por um cliente, aguardando a retirada.
     */
    RESERVADO,

    /**
     * O veículo está atualmente alugado por um cliente.
     */
    ALUGADO,

    /**
     * O veículo está passando por manutenção e não pode ser alugado.
     */
    EM_MANUTENCAO,

    /**
     * O veículo foi retirado da frota e não está mais disponível para locação.
     */
    FORA_FROTA
}
