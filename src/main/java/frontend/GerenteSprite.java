package frontend;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Sprite animado do Gerente. Invisível até handleMessage("...") ser chamado.
 */
public class GerenteSprite extends ImageView {

    private final Image[] frames;
    private int frameIndex = 0;
    private long lastUpdate = 0;
    private final long frameDuration;
    private final AnimationTimer timer;

    /**
     * @param framesArray     Array de Image com quadros do gerente (ex: 00.png a 07.png).
     * @param frameDurationMs Duração de cada quadro em ms (ex: 150).
     */
    public GerenteSprite(Image[] framesArray, long frameDurationMs) {
        super(framesArray[0]);
        this.frames = framesArray;
        this.frameDuration = frameDurationMs * 1_000_000L;

        setVisible(false);

        this.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= GerenteSprite.this.frameDuration) {
                    frameIndex = (frameIndex + 1) % frames.length;
                    setImage(frames[frameIndex]);
                    lastUpdate = now;
                }
            }
        };
    }

    /**
     * Ao receber a mensagem (ex: "startManaging"), torna o gerente visível e inicia animação.
     */
    public void handleMessage(String msg) {
        if (!isVisible()) {
            setVisible(true);
            startAnimation();
        }
        // Reaja a msg conforme necessidade (ex.: mudar expressão, parar depois de certo tempo, etc.)
    }

    private void startAnimation() {
        this.timer.start();
    }

    /** Para animação e esconde o gerente. */
    public void stopAndHide() {
        this.timer.stop();
        setVisible(false);
    }
}
