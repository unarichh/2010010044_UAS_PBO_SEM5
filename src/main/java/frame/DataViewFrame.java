package frame;

import helpers.Koneksi;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.*;

public class DataViewFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel caripanel;
    private JTextField CaritextField;
    private JButton cariButton;
    private JButton batalButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton cetakButton;
    private JButton tutupButton;
    private JTable viewTable;
    private JLabel lblfoto;
    private JButton inputButton;

    JFileChooser fChooser = new JFileChooser();
    private BufferedImage resizeImage(BufferedImage originalImage, int type){
        int IMG_HEIGHT = 0;
        int IMG_WIDTH = 0;
        BufferedImage resizeImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
        Graphics2D g = resizeImage.createGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
        g.dispose();
        return resizeImage;
    }
    public DataViewFrame(){
        cetakButton.addActionListener(e -> {
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files","jpg","png", "jpeg");
            fChooser.setFileFilter(filter);
            int result = fChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION){
                File file = fChooser.getSelectedFile();
                lblfoto.setIcon(new ImageIcon(String.valueOf(file)));
                    }

        }
                );
        batalButton.addActionListener(e ->{
            dispose(); 
        });
        addWindowFocusListener(new WindowAdapter() {
        public void windowActivated(WindowEvent e) {
            isiTable();

        }
        });
        cariButton.addActionListener(e ->{
            Connection c = Koneksi.getConnection();
            String keyword = "%" + CaritextField.getText() + "%";
            String searchSQL = "SELECT * FROM tbl_peserta WHERE nama_lengkap like ?";
            try {
                PreparedStatement ps = c.prepareStatement(searchSQL);
                ps.setString(1, keyword);
                ResultSet rs = ps.executeQuery();
                DefaultTableModel dtm = (DefaultTableModel) viewTable.getModel();
                dtm.setRowCount(0);
                Object[] row = new Object[7];
                while (rs.next()){
                    row[0] = rs.getInt("id");
                    row[1] = rs.getInt("NISN");
                    row[2] = rs.getString("nama_lengkap");
                    row[3] = rs.getString("tanggal_lahir");
                    row[4] = rs.getString("asal_sekolah");
                    row[5] = rs.getString("mata_pelajaran");
                    row[6] = rs.getString("alamat_rumah");
                    dtm.addRow(row);
                }
            } catch (SQLException ex){
                throw new RuntimeException(ex);
            }

        });
        hapusButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih == -1){
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih data dulu");
                return;
            }
        });
        isiTable();
        init();
    }

    public void init() {
        setContentPane(mainPanel);
        setTitle("Data Pendaftaran");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    public void isiTable() {
        Connection c = Koneksi.getConnection();
        String selectSQL = "SELECT * FROM tbl_peserta";
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);
            String header[] = {"Id","NISN","Nama Lengkap","Tanggal Lahir","Asal Sekolah","Mata Pelajaran","Alamat Rumah"};
            DefaultTableModel dtm = new DefaultTableModel(header,0);
            viewTable.setModel(dtm);
            Object[] row = new Object[7];
            while (rs.next()){
                row[0] = rs.getInt("id");
                row[1] = rs.getInt("NISN");
                row[2] = rs.getString("nama_lengkap");
                row[3] = rs.getString("tanggal_lahir");
                row[4] = rs.getString("asal_sekolah");
                row[5] = rs.getString("mata_pelajaran");
                row[6] = rs.getString("alamat_rumah");
                dtm.addRow(row);
            }
            }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}

