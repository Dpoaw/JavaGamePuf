/**
 * Created by sivanov on 8.4.2016 г..
 */
public class Launcher {
    public static void main(String[] args) {
        HandlingEvents ex = new HandlingEvents();
        new Thread(ex).start();
    }
}
