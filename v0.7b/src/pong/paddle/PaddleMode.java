package pong.paddle;

/**
 * Enumeration decrivant les differents modes de controle pour une raquette.
 */
public enum PaddleMode {

    /**
     * Si la raquette est conrolee par un etre humain.
     */
    HUMAN,

    /**
     * Si la raquette est controlee par l'IA.
     */
    AI,

    /**
     * Si la raquette n'est pas controlee.
     */
    NONE
}