import helpers.Koneksi;
import frame.DataViewFrame;

public class Main {
    public static void main(String[] args) {
        Koneksi.getConnection();
        DataViewFrame viewFrame = new  DataViewFrame();
        viewFrame.setVisible(true);

    }
}
