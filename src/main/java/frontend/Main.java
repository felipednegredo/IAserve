package frontend;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Pane raiz onde os sprites serão desenhados
        Pane root = new Pane();

        // Define o fundo como textura de madeira
        BackgroundImage bgImage = new BackgroundImage(
                new Image(getClass().getResource("/backgrounds/chao.png").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
        );

        System.out.println(getClass().getResource("/backgrounds/chao.png"));

        root.setBackground(new Background(bgImage));

        // Scene com cara de jogo
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Restaurante - Simulação JADE");
        primaryStage.setScene(scene);
        primaryStage.show();

        iniciarJade(); // chama seu método de inicialização JADE
    }

    private void iniciarJade() {
        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();
        p.setParameter(Profile.GUI, "true"); // Janela RMA do JADE
        rt.setCloseVM(false); // Evita que a JVM encerre com o JADE

        AgentContainer container = rt.createMainContainer(p);

        try {
            AgentController gerente = container.createNewAgent("gerente", "agentes.Gerente", null);
            gerente.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
