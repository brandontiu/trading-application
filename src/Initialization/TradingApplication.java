package Initialization;

/**
 * <h1>Initialization.TradingApplication</h1>
 * <p>Contains the application's main entry point.</p>
 */
public class TradingApplication {

    /**
     * The program's main entry point.
     */
    public static void main(String[] args) {
        TradingSystem tradingSystem = new TradingSystem();
        tradingSystem.run();
    }
}
