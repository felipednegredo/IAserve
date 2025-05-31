package frontend;

import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class RestauranteView extends Pane {
    private ImageView clienteSprite, garcomSprite;

    public RestauranteView() {
        // Carregue imagens dos sprites
        clienteSprite = new ImageView(new Image("file:cliente.png"));
        garcomSprite  = new ImageView(new Image("file:garcom.png"));
        clienteSprite.setLayoutX(50); clienteSprite.setLayoutY(400);
        garcomSprite.setLayoutX(300); garcomSprite.setLayoutY(400);
        getChildren().addAll(clienteSprite, garcomSprite);
    }

    public void clienteEntra() {
        // Anima cliente entrando
        TranslateTransition tt = new TranslateTransition(Duration.seconds(2), clienteSprite);
        tt.setToX(200);
        tt.play();
    }

    public void garcomAtende() {
        // Anima garçom indo até o cliente
        TranslateTransition tt = new TranslateTransition(Duration.seconds(1), garcomSprite);
        tt.setToX(-150);
        tt.play();
    }

    // Adicione métodos para outras animações (chef, gerente, etc)
}