package test;

public class GeneralLayerTest {

    public static void main(String[] args) {
        System.out.println("==================================");
        System.out.println("INICIANDO TESTE GERAL DAS CAMADAS");
        System.out.println("==================================");

        run("ModelLayerTest", new Runnable() {
            @Override
            public void run() {
                ModelLayerTest.main(new String[0]);
            }
        });

        run("ServiceLayerTest", new Runnable() {
            @Override
            public void run() {
                ServiceLayerTest.main(new String[0]);
            }
        });

        run("ControllerLayerTest", new Runnable() {
            @Override
            public void run() {
                ControllerLayerTest.main(new String[0]);
            }
        });

        run("DaoSmokeTest", new Runnable() {
            @Override
            public void run() {
                DaoSmokeTest.main(new String[0]);
            }
        });

        System.out.println("==================================");
        System.out.println("TESTE GERAL FINALIZADO");
        System.out.println("==================================");
    }

    private static void run(String testName, Runnable runnable) {
        try {
            System.out.println(">> Executando: " + testName);
            runnable.run();
            System.out.println("OK - " + testName);
        } catch (Exception e) {
            System.out.println("FALHOU - " + testName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}