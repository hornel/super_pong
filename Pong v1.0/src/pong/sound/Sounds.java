package pong.sound;

/**
 * Classe qui permet de chercher un son. Cela evite d'ouvrir a chaque fois un nouveau son, ce qui evite de
 * creer un nouveau Thread pour chaque son ouvert (dans le cas du mode casse-brique, cela aurait donne des centaines
 * de Threads) et permet aussi d'eviter un probleme dans Java SE 6 ou seulement 32 sons peuvent etre ouverts a la fois.
 */
public final class Sounds {

    /**
     * Le son qui est joue lorsque l'utilisateur tire un missile.
     */
    private final static PongSoundPlayer MISSILE_FIRED_SOUND = new PongSoundPlayer("missileFired.wav");

    /**
     * Le son qui est joue lorsque l'utilisateur est touche par un missile.
     */
    private final static PongSoundPlayer MISSILE_HIT_SOUND = new PongSoundPlayer("missileHit.wav");

    /**
     * Le son qui est joue lorsque l'utilisateur touche la balle avec la raquette.
     */
    private final static PongSoundPlayer PADDLE_HIT_SOUND = new PongSoundPlayer("paddleHit.wav");

    /**
     * Le son qui est joue lorsque l'utilisateur gagne un point ou perd la balle.
     */
    private final static PongSoundPlayer POINT_SOUND = new PongSoundPlayer("point.wav");

    /**
     * Le son qui est joue lorsque la balle touche le mur.
     */
    private final static PongSoundPlayer WALL_HIT_SOUND = new PongSoundPlayer("wallHit.wav");

    public static PongSoundPlayer getMissileFiredSound() {

        return MISSILE_FIRED_SOUND;
    }

    public static PongSoundPlayer getMissileHitSound() {

        return MISSILE_HIT_SOUND;
    }

    public static PongSoundPlayer getPaddleHitSound() {

        return PADDLE_HIT_SOUND;
    }

    public static PongSoundPlayer getPointSound() {

        return POINT_SOUND;
    }

    public static PongSoundPlayer getWallHitSound() {

        return WALL_HIT_SOUND;
    }

    /**
     * Deinitialise les sons.
     */
    public static void deinitSounds() {

        MISSILE_FIRED_SOUND.deinit();
        MISSILE_HIT_SOUND.deinit();
        PADDLE_HIT_SOUND.deinit();
        POINT_SOUND.deinit();
        WALL_HIT_SOUND.deinit();
    }
}