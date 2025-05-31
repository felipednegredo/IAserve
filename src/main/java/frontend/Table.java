package frontend;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Representa uma única mesa (média) com duas cadeiras (esquerda e direita)
 * e controle de ocupação. Ajusta as cadeiras para ficarem perfeitamente alinhadas
 * sob as bordas laterais do tampo.
 */
public class Table {

    private final ImageView mesaView;
    private final ImageView cadeiraEsquerda;
    private final ImageView cadeiraDireita;
    private boolean ocupada = false;

    /**
     * Constrói uma mesa média com duas cadeiras,
     * adicionando tudo ao Pane especificado.
     *
     * @param root           Pane onde mesa e cadeiras serão adicionadas.
     * @param imagemMesa     Imagem da mesa (por ex. "/objetos/mesa-m.png").
     * @param imagemCadeira  Imagem da cadeira (por ex. "/objetos/cadeira.png").
     * @param x              Coordenada X (px) para desenhar a mesa.
     * @param y              Coordenada Y (px) para desenhar a mesa.
     * @param larguraCadeira Largura (px) que a cadeira deve ter ao ser exibida.
     */
    public Table(Pane root, Image imagemMesa, Image imagemCadeira, double x, double y, double larguraCadeira) {
        // 1) Captura dimensões da mesa
        double mesaWidth  = imagemMesa.getWidth();
        double mesaHeight = imagemMesa.getHeight();

        // 2) Calcula dimensões da cadeira para manter proporção
        double proporcaoCadeira = imagemCadeira.getHeight() / imagemCadeira.getWidth();
        double cadeiraWidthPx    = larguraCadeira;
        double cadeiraHeightPx   = cadeiraWidthPx * proporcaoCadeira;

        // 3) Calcula Y das cadeiras de modo que fiquem encaixadas atrás do tampo:
        //    Usamos y + mesaHeight - (cadeiraHeightPx * 0.5)
        //    Para que metade da altura da cadeira fique "pra baixo" do tampo, e a outra metade atrás.
        double yCadeira = y + mesaHeight - (cadeiraHeightPx * 0.5);

        // 4) Cálculo de buffer lateral: 10% da largura da mesa
        double bufferLateral = mesaWidth * 0.10;

        // 5) Desenha as cadeiras PRIMEIRO, de forma que fiquem atrás da mesa

        // 5.1) Cadeira da esquerda
        cadeiraEsquerda = new ImageView(imagemCadeira);
        cadeiraEsquerda.setFitWidth(cadeiraWidthPx);
        cadeiraEsquerda.setPreserveRatio(true);
        double xCadeiraEsq = x + bufferLateral;
        cadeiraEsquerda.setLayoutX(xCadeiraEsq);
        cadeiraEsquerda.setLayoutY(yCadeira);
        root.getChildren().add(cadeiraEsquerda);

        // 5.2) Cadeira da direita (espelhada)
        cadeiraDireita = new ImageView(imagemCadeira);
        cadeiraDireita.setFitWidth(cadeiraWidthPx);
        cadeiraDireita.setPreserveRatio(true);
        cadeiraDireita.setScaleX(-1);
        double xCadeiraDir = x + mesaWidth - bufferLateral - cadeiraWidthPx;
        cadeiraDireita.setLayoutX(xCadeiraDir);
        cadeiraDireita.setLayoutY(yCadeira);
        root.getChildren().add(cadeiraDireita);

        // 6) Agora desenha a mesa SOBRE as cadeiras (tampo na frente)
        mesaView = new ImageView(imagemMesa);
        mesaView.setLayoutX(x);
        mesaView.setLayoutY(y);
        root.getChildren().add(mesaView);
    }

    /** Retorna true se a mesa estiver ocupada. */
    public boolean isOcupada() {
        return ocupada;
    }

    /** Marca a mesa como ocupada. */
    public void ocupar() {
        ocupada = true;
    }

    /** Libera a mesa (tornando-a disponível). */
    public void liberar() {
        ocupada = false;
    }

    /**
     * Retorna a coordenada X central aproximada do tampo da mesa,
     * para que um cliente possa ser posicionado no meio dela.
     */
    public double getCenterX() {
        return mesaView.getLayoutX() + mesaView.getImage().getWidth() / 2.0;
    }

    /**
     * Retorna a coordenada Y central aproximada do tampo da mesa,
     * para que um cliente possa ser posicionado “sentado” aproximadamente no meio.
     */
    public double getCenterY() {
        return mesaView.getLayoutY() + mesaView.getImage().getHeight() / 2.0;
    }

}
