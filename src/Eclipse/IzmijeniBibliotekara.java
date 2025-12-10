package Biblioteka;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.awt.event.ActionEvent;

public class IzmijeniBibliotekara extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldIme;
	private JTextField textFieldPrezime;
	private JTextField textFieldKorisnickoIme;
	private JTextField textFieldLozinka;
	private int bibliotekarId;

	public IzmijeniBibliotekara(int id, String ime, String prezime, String korisnickoIme, String lozinka) {
		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 440, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		setLocationRelativeTo(null);
		setVisible(true);

		JLabel lblIme = new JLabel("Ime");
		lblIme.setBounds(56, 40, 72, 14);
		contentPane.add(lblIme);

		JLabel lblPrezime = new JLabel("Prezime");
		lblPrezime.setBounds(56, 80, 72, 14);
		contentPane.add(lblPrezime);

		JLabel lblKorisnickoIme = new JLabel("Korisničko ime");
		lblKorisnickoIme.setBounds(56, 120, 93, 14);
		contentPane.add(lblKorisnickoIme);

		JLabel lblLozinka = new JLabel("Lozinka");
		lblLozinka.setBounds(56, 160, 47, 14);
		contentPane.add(lblLozinka);

		textFieldIme = new JTextField();
		textFieldIme.setBounds(186, 37, 142, 20);
		contentPane.add(textFieldIme);
		textFieldIme.setColumns(10);

		textFieldPrezime = new JTextField();
		textFieldPrezime.setBounds(186, 77, 142, 20);
		contentPane.add(textFieldPrezime);
		textFieldPrezime.setColumns(10);

		textFieldKorisnickoIme = new JTextField();
		textFieldKorisnickoIme.setBounds(186, 117, 142, 20);
		contentPane.add(textFieldKorisnickoIme);
		textFieldKorisnickoIme.setColumns(10);

		textFieldLozinka = new JTextField();
		textFieldLozinka.setBounds(186, 157, 142, 20);
		contentPane.add(textFieldLozinka);
		textFieldLozinka.setColumns(10);

		JButton btnPotvrdi = new JButton("Potvrdi");
		btnPotvrdi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ime = textFieldIme.getText();
				String prezime = textFieldPrezime.getText();
				String korisnickoIme = textFieldKorisnickoIme.getText();
				String lozinka = textFieldLozinka.getText();

				if (ime.isEmpty() || prezime.isEmpty() || korisnickoIme.isEmpty() || lozinka.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Sva polja su obavezna.", "Greška", JOptionPane.ERROR_MESSAGE);
				} else {
					try (Connection con = Konekcija.connect()) {
						String query = "UPDATE bibliotekar SET ime = ?, prezime = ?, korisnicko_ime = ?, lozinka = ? WHERE id_bibliotekara = ?";
						PreparedStatement pst = con.prepareStatement(query);
						pst.setString(1, ime);
						pst.setString(2, prezime);
						pst.setString(3, korisnickoIme);
						pst.setString(4, lozinka);
						pst.setInt(5, bibliotekarId);
						pst.executeUpdate();
						JOptionPane.showMessageDialog(null, "Podaci uspešno ažurirani.", "Uspeh",
								JOptionPane.INFORMATION_MESSAGE);

						dispose();
						PregledBibliotekara pregled = new PregledBibliotekara();
						pregled.setVisible(true);

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Greška pri ažuriranju podataka.", "Greška",
								JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
				}
			}
		});
		btnPotvrdi.setBounds(85, 200, 100, 30);
		contentPane.add(btnPotvrdi);

		JButton btnOdustani = new JButton("Odustani");
		btnOdustani.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				PregledBibliotekara pregled = new PregledBibliotekara();
				pregled.setVisible(true);
			}
		});
		btnOdustani.setBounds(228, 200, 100, 30);
		contentPane.add(btnOdustani);

		// Popuni polja sa prosleđenim podacima
		textFieldIme.setText(ime);
		textFieldPrezime.setText(prezime);
		textFieldKorisnickoIme.setText(korisnickoIme);
		textFieldLozinka.setText(lozinka);

		bibliotekarId = id;
	}
}
