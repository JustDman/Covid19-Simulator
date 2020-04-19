package schulbeispiel;

public class Main {

    public static void main(String[] args) {

        Simulator sim = new Simulator();

        // sim.simuliere(100);
        while (true) {

            sim.simulateOneStep();
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

}