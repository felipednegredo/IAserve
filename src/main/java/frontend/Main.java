package frontend;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import agentes.Gerente;
import agentes.Garcom;
import agentes.Chef;
import agentes.Cliente;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private ChefSprite chefSprite;
    private GarcomSprite garcomSprite;
    private ClienteSprite clienteSprite;
    private GerenteSprite gerenteSprite;

    // Lista estática de mesas para os agentes acessarem
    private static final List<Table> tables = new ArrayList<>();

    public static List<Table> getTables() {
        return tables;
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();

        // 1) Fundo do restaurante
        ImageView fundo = new ImageView(
                new Image(getClass().getResource("/backgrounds/fundo.png").toExternalForm())
        );
        root.getChildren().add(fundo);

        double sceneWidth  = fundo.getImage().getWidth();
        double sceneHeight = fundo.getImage().getHeight();

        // 2) Imagens de mesa e cadeira
        Image mesaMediaImg = new Image(
                getClass().getResource("/objetos/mesa-m.png").toExternalForm()
        );
        Image cadeiraImg = new Image(
                getClass().getResource("/objetos/cadeira.png").toExternalForm()
        );
        double larguraCadeira = 40;

        // 3) Parâmetros de layout das mesas
        int    numMesasEmCima   = 3;
        int    numMesasEmBaixo  = 2;
        double gapEntreMesas    = 80;  // espaçamento horizontal maior entre cada mesa

        // 3.1) LINHA SUPERIOR (3 mesas)
        double totalWidthCima = numMesasEmCima * mesaMediaImg.getWidth()
                + (numMesasEmCima - 1) * gapEntreMesas;
        double startXcima     = (sceneWidth - totalWidthCima) / 2.0;
        double yCima          = sceneHeight - 300;

        for (int i = 0; i < numMesasEmCima; i++) {
            double xMesa = startXcima + i * (mesaMediaImg.getWidth() + gapEntreMesas);
            Table mesa = new Table(root, mesaMediaImg, cadeiraImg, xMesa, yCima, larguraCadeira);
            tables.add(mesa);
        }

        // 3.2) LINHA INFERIOR (2 mesas)
        double totalWidthBaixo = numMesasEmBaixo * mesaMediaImg.getWidth()
                + (numMesasEmBaixo - 1) * gapEntreMesas;
        double startXbaixo     = (sceneWidth - totalWidthBaixo) / 2.0;
        double yBaixo          = yCima + 150;

        for (int i = 0; i < numMesasEmBaixo; i++) {
            double xMesa = startXbaixo + i * (mesaMediaImg.getWidth() + gapEntreMesas);
            Table mesa = new Table(root, mesaMediaImg, cadeiraImg, xMesa, yBaixo, larguraCadeira);
            tables.add(mesa);
        }

//        // 4) Instancia sprites (invisíveis até handleMessage)
//        // 4.1) Chef
//        Image[] chefFrames = new Image[8];
//        for (int i = 0; i < 8; i++) {
//            chefFrames[i] = new Image(
//                    getClass().getResource("/characters/chef/00" + i + ".png").toExternalForm()
//            );
//        }
//        chefSprite = new ChefSprite(chefFrames, 150);
//        chefSprite.setFitWidth(80);
//        chefSprite.setPreserveRatio(true);
//        double chefX = (sceneWidth / 2.0) - (chefSprite.getFitWidth() / 2.0) + 20;
//        double chefY = 120;
//        chefSprite.setLayoutX(chefX);
//        chefSprite.setLayoutY(chefY);
//        root.getChildren().add(chefSprite);
//
//        // 4.2) Garçom
//        Image[] garcomFrames = new Image[8];
//        for (int i = 0; i < 8; i++) {
//            garcomFrames[i] = new Image(
//                    getClass().getResource("/characters/garcom/00" + i + ".png").toExternalForm()
//            );
//        }
//        garcomSprite = new GarcomSprite(garcomFrames, 150);
//        garcomSprite.setFitWidth(60);
//        garcomSprite.setPreserveRatio(true);
//        double garcomX = chefX + 100;
//        double garcomY = chefY + 40;
//        garcomSprite.setLayoutX(garcomX);
//        garcomSprite.setLayoutY(garcomY);
//        root.getChildren().add(garcomSprite);
//
//        // 4.3) Cliente
//        Image[] clienteFrames = new Image[8];
//        for (int i = 0; i < 8; i++) {
//            clienteFrames[i] = new Image(
//                    getClass().getResource("/characters/cliente/00" + i + ".png").toExternalForm()
//            );
//        }
//        clienteSprite = new ClienteSprite(clienteFrames, 150);
//        clienteSprite.setFitWidth(60);
//        clienteSprite.setPreserveRatio(true);
//        double clienteX = 30;
//        double clienteY = sceneHeight - 100;
//        clienteSprite.setLayoutX(clienteX);
//        clienteSprite.setLayoutY(clienteY);
//        root.getChildren().add(clienteSprite);
//
//        // 4.4) Gerente
//        Image[] gerenteFrames = new Image[8];
//        for (int i = 0; i < 8; i++) {
//            gerenteFrames[i] = new Image(
//                    getClass().getResource("/characters/gerente/00" + i + ".png").toExternalForm()
//            );
//        }
//        gerenteSprite = new GerenteSprite(gerenteFrames, 150);
//        gerenteSprite.setFitWidth(70);
//        gerenteSprite.setPreserveRatio(true);
//        double gerenteX = chefX - 80;
//        double gerenteY = chefY + 20;
//        gerenteSprite.setLayoutX(gerenteX);
//        gerenteSprite.setLayoutY(gerenteY);
//        root.getChildren().add(gerenteSprite);

        // 5) Legenda de horário do Gerente no canto superior direito
        Label legenda = new Label();
        legenda.setStyle(
                "-fx-background-color: rgba(0,0,0,0.6); " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 5 10 5 10; " +
                        "-fx-background-radius: 5;"
        );
        legenda.layoutXProperty().bind(
                root.widthProperty().subtract(legenda.widthProperty()).subtract(20)
        );
        legenda.setLayoutY(20);
        legenda.setText(Gerente.formatHorario(Gerente.getCurrentTick()));

        Timeline timerLegenda = new Timeline(
                new KeyFrame(Duration.ZERO, e ->
                        legenda.setText(Gerente.formatHorario(Gerente.getCurrentTick()))
                ),
                new KeyFrame(Duration.seconds(1))
        );
        timerLegenda.setCycleCount(Timeline.INDEFINITE);
        timerLegenda.play();

        root.getChildren().add(legenda);

        // 6) Cria a cena e mostra
        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        primaryStage.setTitle("Restaurante - Simulação JADE");
        primaryStage.setScene(scene);
        primaryStage.show();

        // 7) Inicia o JADE e injeta sprites via O2A
        iniciarJade();
    }

    private void iniciarJade() {
        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();
        p.setParameter(Profile.GUI, "true");
        rt.setCloseVM(false);

        AgentContainer container = rt.createMainContainer(p);
        try {
            // 7.1) Gerente
            AgentController gerenteAgent = container.createNewAgent(
                    "gerente", "agentes.Gerente", null
            );
            gerenteAgent.start();
            gerenteAgent.putO2AObject(gerenteSprite, true);

            // 7.2) Garçom
            AgentController garcomAgent = container.createNewAgent(
                    "garcom", "agentes.GarcomAgent", null
            );
            garcomAgent.start();
            garcomAgent.putO2AObject(garcomSprite, true);

            // 7.3) Chef
            AgentController chefAgent = container.createNewAgent(
                    "chef", "agentes.ChefAgent", null
            );
            chefAgent.start();
            chefAgent.putO2AObject(chefSprite, true);

            // 7.4) Cliente
            AgentController clienteAgent = container.createNewAgent(
                    "cliente", "agentes.ClienteAgent", null
            );
            clienteAgent.start();
            clienteAgent.putO2AObject(clienteSprite, true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
