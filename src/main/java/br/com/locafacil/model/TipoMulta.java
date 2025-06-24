package br.com.locafacil.model;

/**
 * Enumeração que representa os tipos de multas aplicáveis em um contrato de locação.
 */
public enum TipoMulta {

    /**
     * Multa por devolução do veículo com atraso em relação ao prazo estipulado.
     */
    ATRASO,

    /**
     * Multa por exceder o limite de quilometragem previsto no contrato.
     */
    QUILOMETRAGEM_EXCEDENTE,

    /**
     * Multa por danos físicos ou mecânicos causados ao veículo durante a locação.
     */
    DANO,

    /**
     * Multa por devolução do veículo com nível de combustível inferior ao acordado.
     */
    COMBUSTIVEL
}
