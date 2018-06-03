package pong.mode;

import pong.paddle.PaddleAI;

/**
 * Interface decrivant un PongCanvas avec IA.
 */
public interface AIPongCanvas {

    /**
     * Cherche l'IA de la raquette controlee par l'ordinateur.
     */
    PaddleAI getAI();
}