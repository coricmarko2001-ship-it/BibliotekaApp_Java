package Biblioteka;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import com.toedter.calendar.JDateChooser;

public class DodajBibliotekara extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldIme;
	private JTextField textFieldPrezime;
	private JTextField textFieldKorisnickoIme;
	private JTextField textFieldLozinka;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DodajBibliotekara frame = new DodajBibliotekara();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DodajBibliotekara() {
		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 440, 458);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		setLocationRelativeTo(null);
		setVisible(true);

		JButton btnDodajBibliotekaraPotvrdi = new JButton("Potvrdi");
		btnDodajBibliotekaraPotvrdi.setBounds(85, 349, 100, 30);
		contentPane.add(btnDodajBibliotekaraPotvrdi);

		JButton btnDodajBibliotekaraOdustani = new JButton("Odustani");
		btnDodajBibliotekaraOdustani.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Login login = new Login();
				login.setVisible(true);
			}
		});
		btnDodajBibliotekaraOdustani.setBounds(228, 349, 100, 30);
		contentPane.add(btnDodajBibliotekaraOdustani);

		JLabel lblDodajBibliotekaraIme = new JLabel("Ime");
		lblDodajBibliotekaraIme.setBounds(56, 107, 72, 14);
		contentPane.add(lblDodajBibliotekaraIme);

		JLabel lblDodajBibliotekaraPrezime = new JLabel("Prezime");
		lblDodajBibliotekaraPrezime.setBounds(56, 163, 72, 14);
		contentPane.add(lblDodajBibliotekaraPrezime);

		JLabel lblDodajBibliotekaraKorisnickoIme = new JLabel("Korisničko ime");
		lblDodajBibliotekaraKorisnickoIme.setBounds(56, 218, 93, 14);
		contentPane.add(lblDodajBibliotekaraKorisnickoIme);

		JLabel lblDodajBibliotekaraLozinka = new JLabel("Lozinka");
		lblDodajBibliotekaraLozinka.setBounds(56, 272, 47, 14);
		contentPane.add(lblDodajBibliotekaraLozinka);

		textFieldIme = new JTextField();
		textFieldIme.setBounds(186, 104, 142, 20);
		contentPane.add(textFieldIme);
		textFieldIme.setColumns(10);

		textFieldPrezime = new JTextField();
		textFieldPrezime.setBounds(186, 160, 142, 20);
		contentPane.add(textFieldPrezime);
		textFieldPrezime.setColumns(10);

		textFieldKorisnickoIme = new JTextField();
		textFieldKorisnickoIme.setBounds(186, 215, 140, 20);
		contentPane.add(textFieldKorisnickoIme);
		textFieldKorisnickoIme.setColumns(10);

		textFieldLozinka = new JTextField();
		textFieldLozinka.setBounds(186, 269, 140, 20);
		contentPane.add(textFieldLozinka);
		textFieldLozinka.setColumns(10);

		btnDodajBibliotekaraPotvrdi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ime = textFieldIme.getText();
				String prezime = textFieldPrezime.getText();
				String korisnicko_ime = textFieldKorisnickoIme.getText();
				String lozinka = textFieldLozinka.getText();

				if (ime.isEmpty() || prezime.isEmpty() || korisnicko_ime.isEmpty() || lozinka.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Sva polja su obavezna.", "Greška", JOptionPane.ERROR_MESSAGE);
				} else {
					try (Connection con = Konekcija.connect()) {
						String query = "INSERT INTO bibliotekar (ime, prezime, korisnicko_ime, lozinka) VALUES (?, ?, ?, ?)";
						PreparedStatement pst = con.prepareStatement(query);
						pst.setString(1, ime);
						pst.setString(2, prezime);
						pst.setString(3, korisnicko_ime);
						pst.setString(4, lozinka);

						pst.executeUpdate();
						JOptionPane.showMessageDialog(null, "Bibliotekar uspješno dodan.", "Uspjeh",
								JOptionPane.INFORMATION_MESSAGE);

						dispose();
						Login login = new Login();
						login.setVisible(true);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Greška pri dodavanju bibliotekara.", "Greška",
								JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
				}
			}
		});
	}
}
