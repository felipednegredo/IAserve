package frontend;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Sprite animado do Garçom. Por padrão, invisível; só aparece e anima ao receber handleMessage().
 */
public class GarcomSprite extends ImageView {

    private final Image[] frames;
    private int frameIndex = 0;
    private long lastUpdate = 0;
    private final long frameDuration;
    private final AnimationTimer timer;

    /**
     * @param framesArray     Array de Image contendo cada quadro do garçom (ex: 00.png a 07.png).
     * @param frameDurationMs Duração de cada quadro em ms (ex: 150).
     */
    public GarcomSprite(Image[] framesArray, long frameDurationMs) {
        super(framesArray[0]);
        this.frames = framesArray;
        this.frameDuration = frameDurationMs * 1_000_000L;

        setVisible(false);

        this.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= GarcomSprite.this.frameDuration) {
                    frameIndex = (frameIndex + 1) % frames.length;
                    setImage(frames[frameIndex]);
                    lastUpdate = now;
                }
            }
        };
    }

    /**
     * Chamado pelo agente Garçom (via O2A) quando quiser que o sprite comece a aparecer e animar.
     * Por exemplo, msg = "walkToTable", "serve", etc.
     */
    public void handleMessage(String msg) {
        if (!isVisible()) {
            setVisible(true);
            startAnimation();
        }
        // Reaja a msg se precisar (ex.: mudar animação, parar depois de servir, etc.)
    }

    private void startAnimation() {
        this.timer.start();
    }

    /** Para animação e esconde o garçom. */
    public void stopAndHide() {
        this.timer.stop();
        setVisible(false);
    }
}
