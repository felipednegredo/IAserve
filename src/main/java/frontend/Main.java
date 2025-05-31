package frontend;

// Main.java
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        RestauranteView view = new RestauranteView();
        Scene scene = new Scene(view, 800, 600);
        primaryStage.setTitle("Restaurante - Simulação JADE");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Exemplo: simular eventos recebidos do JADE
        view.clienteEntra();
        view.garcomAtende();
        // Integração real: escute eventos do JADE e chame métodos do view
    }

    public static void main(String[] args) {
        launch(args);
    }
}