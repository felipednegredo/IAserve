package frontend;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Sprite animado do Cliente. Por padrão, permanece invisível e só aparece
 * e inicia sua animação ao receber handleMessage("...").
 */
public class ClienteSprite extends ImageView {

    private final Image[] frames;
    private int frameIndex = 0;
    private long lastUpdate = 0;
    private final long frameDuration; // em nanosegundos
    private final AnimationTimer timer;

    /**
     * @param framesArray     Array de Image contendo cada quadro de cliente (ex: 00.png a 07.png).
     * @param frameDurationMs Duração de cada quadro em milissegundos (ex: 150).
     */
    public ClienteSprite(Image[] framesArray, long frameDurationMs) {
        super(framesArray[0]);
        this.frames = framesArray;
        this.frameDuration = frameDurationMs * 1_000_000L; // converte ms → ns

        // Inicia invisível
        setVisible(false);

        // Timer que percorre os frames
        this.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= ClienteSprite.this.frameDuration) {
                    frameIndex = (frameIndex + 1) % frames.length;
                    setImage(frames[frameIndex]);
                    lastUpdate = now;
                }
            }
        };
    }

    /**
     * Quando receber uma mensagem (ex: "arrived", "sit", etc), o cliente aparece e anima.
     * Você pode filtrar msgs específicas ou simplesmente fazer aparecer por qualquer mensagem.
     */
    public void handleMessage(String msg) {
        if (!isVisible()) {
            setVisible(true);
            startAnimation();
        }
        // Aqui você pode reagir conforme 'msg' (por ex. mudar de direção, para de animar, etc).
    }

    /** Inicia o AnimationTimer interno (roda a animação). */
    private void startAnimation() {
        this.timer.start();
    }

    /** Pausa a animação e esconde o cliente. */
    public void stopAndHide() {
        this.timer.stop();
        setVisible(false);
    }
}
