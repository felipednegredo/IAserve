package frontend;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Representa o sprite animado do chef.
 * Por padrão, começa invisível e só inicia a animação ao receber handleMessage().
 */
public class ChefSprite extends ImageView {

    private final Image[] frames;
    private int frameIndex = 0;
    private long lastUpdate = 0;
    private final long frameDuration; // duração de cada frame em nanosegundos
    private final AnimationTimer timer;

    /**
     * Constrói o ChefSprite, deixando-o invisível.
     *
     * @param framesArray    Array de Image contendo cada quadro da animação (ex: 8 imagens).
     * @param frameDurationMs  Duração de cada frame em milissegundos (ex: 150).
     */
    public ChefSprite(Image[] framesArray, long frameDurationMs) {
        super(framesArray[0]);
        this.frames = framesArray;
        this.frameDuration = frameDurationMs * 1_000_000L; // converte ms → ns

        // Inicialmente, o chef não deve aparecer nem animar:
        setVisible(false);

        this.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= ChefSprite.this.frameDuration) {
                    frameIndex = (frameIndex + 1) % frames.length;
                    setImage(frames[frameIndex]);
                    lastUpdate = now;
                }
            }
        };
    }

    /**
     * Quando receber uma mensagem (por ex. do agente Chef no JADE),
     * o sprite fica visível e inicia sua animação.
     *
     * @param msg Texto da mensagem recebida.
     */
    public void handleMessage(String msg) {
        // Você pode filtrar a mensagem, se tiver diferentes comandos
        // Aqui, qualquer mensagem faz o chef aparecer e começar a animar:
        if (!isVisible()) {
            setVisible(true);
            startAnimation();
        }

        // Se quiser reagir a diferentes mensagens, faça algo como:
        // if ("startCooking".equalsIgnoreCase(msg)) { ... }
    }

    /**
     * Inicia a animação do chef. (Chamado dentro de handleMessage)
     */
    private void startAnimation() {
        this.timer.start();
    }

    /**
     * Para (pausa) a animação do chef e esconde a imagem.
     * Você pode chamar isso quando, por exemplo, o chef terminar de cozinhar.
     */
    public void stopAndHide() {
        this.timer.stop();
        setVisible(false);
    }
}
