package main;

public class MainJADE {
    public static void main(String[] args) {
        jade.Boot.main(new String[] {
            "-gui",
            "gerente:agentes.Gerente;" +
            "garcom:agentes.Garcom;" +
            "cliente:agentes.Cliente;" +
            "chef:agentes.Chef"
        });
    }
}
