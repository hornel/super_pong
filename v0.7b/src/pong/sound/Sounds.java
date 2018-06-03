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
    private final static PongSoundPlayer missileFiredSound = new PongSoundPlayer("missileFired.wav");

    /**
     * Le son qui est joue lorsque l'utilisateur est touche par un missile.
     */
    private final static PongSoundPlayer missileHitSound = new PongSoundPlayer("missileHit.wav");

    /**
     * Le son qui est joue lorsque l'utilisateur touche la balle avec la raquette.
     */
    private final static PongSoundPlayer paddleHitSound = new PongSoundPlayer("paddleHit.wav");

    /**
     * Le son qui est joue lorsque l'utilisateur gagne un point ou perd la balle.
     */
    private final static PongSoundPlayer pointSound = new PongSoundPlayer("point.wav");

    /**
     * Le son qui est joue lorsque la balle touche le mur.
     */
    private final static PongSoundPlayer wallHitSound = new PongSoundPlayer("wallHit.wav");

    public static PongSoundPlayer getMissileFiredSound() {

        return missileFiredSound;
    }

    public static PongSoundPlayer getMissileHitSound() {

        return missileHitSound;
    }

    public static PongSoundPlayer getPaddleHitSound() {

        return paddleHitSound;
    }

    public static PongSoundPlayer getPointSound() {

        return pointSound;
    }

    public static PongSoundPlayer getWallHitSound() {

        return wallHitSound;
    }

    /**
     * Deinitialise les sons.
     */
    public static void deinitSounds() {

        missileFiredSound.deinit();
        missileHitSound.deinit();
        paddleHitSound.deinit();
        pointSound.deinit();
        wallHitSound.deinit();
    }
}