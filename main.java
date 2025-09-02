package eeditura;

class Main {
    public static void main(String[] args) {
        ModernUI.initGlobalLookAndFeel();
        Catalog catalog = Catalog.incarcaDinFisier();

        javax.swing.SwingUtilities.invokeLater(() -> {
            SplashScreen splash = new SplashScreen() {
                @Override protected void onFinished() {
                    LoginFrame lf = new LoginFrame(catalog);
                    lf.setVisible(true);
                }
            };
            splash.start();
        });
    }
}
