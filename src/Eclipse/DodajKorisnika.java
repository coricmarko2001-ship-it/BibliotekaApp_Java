package Biblioteka;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class DodajKorisnika extends JFrame implements Refresh {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldPrezime;
	private JTextField textFieldIme;
	private JTextField textFieldEmail;
	private JTextField textFieldTelefon;
	private JTextField textFieldUlicaIBroj;
	private JComboBox<String> comboBoxMjesto;
	private ArrayList<Integer> listaMjestaIds;

	public DodajKorisnika() {
		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 483, 518);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		setLocationRelativeTo(null);
		setVisible(true);

		JButton btnDodajKorisnikaPotvrdi = new JButton("Potvrdi");
		btnDodajKorisnikaPotvrdi.setBounds(123, 408, 100, 30);
		contentPane.add(btnDodajKorisnikaPotvrdi);

		JButton btnDodajKorisnikaOdustani = new JButton("Odustani");
		btnDodajKorisnikaOdustani.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				PregledKorisnika korisnik = new PregledKorisnika();
				korisnik.setVisible(true);
			}
		});
		btnDodajKorisnikaOdustani.setBounds(269, 408, 100, 30);
		contentPane.add(btnDodajKorisnikaOdustani);

		JLabel lblDodajKorisnikaIme = new JLabel("Ime");
		lblDodajKorisnikaIme.setBounds(56, 107, 93, 14);
		contentPane.add(lblDodajKorisnikaIme);

		JLabel lblDodajKorisnikaPrezime = new JLabel("Prezime");
		lblDodajKorisnikaPrezime.setBounds(56, 157, 93, 14);
		contentPane.add(lblDodajKorisnikaPrezime);

		JLabel lblDodajKorisnikaEmail = new JLabel("Email");
		lblDodajKorisnikaEmail.setBounds(56, 200, 93, 14);
		contentPane.add(lblDodajKorisnikaEmail);

		JLabel lblDodajKorisnikaTelefon = new JLabel("Telefon");
		lblDodajKorisnikaTelefon.setBounds(56, 247, 93, 14);
		contentPane.add(lblDodajKorisnikaTelefon);

		JLabel lblDodajKorisnikaUlicaIBroj = new JLabel("Ulica i broj");
		lblDodajKorisnikaUlicaIBroj.setBounds(56, 292, 93, 14);
		contentPane.add(lblDodajKorisnikaUlicaIBroj);

		JLabel lblDodajKorisnikaMjesto = new JLabel("Mjesto");
		lblDodajKorisnikaMjesto.setBounds(56, 333, 93, 14);
		contentPane.add(lblDodajKorisnikaMjesto);

		textFieldPrezime = new JTextField();
		textFieldPrezime.setBounds(186, 154, 142, 20);
		contentPane.add(textFieldPrezime);

		textFieldIme = new JTextField();
		textFieldIme.setBounds(186, 104, 142, 20);
		contentPane.add(textFieldIme);
		textFieldIme.setColumns(10);

		textFieldEmail = new JTextField();
		textFieldEmail.setBounds(186, 197, 142, 20);
		contentPane.add(textFieldEmail);
		textFieldEmail.setColumns(10);

		textFieldTelefon = new JTextField();
		textFieldTelefon.setBounds(186, 244, 142, 20);
		contentPane.add(textFieldTelefon);
		textFieldTelefon.setColumns(10);

		textFieldUlicaIBroj = new JTextField();
		textFieldUlicaIBroj.setBounds(186, 289, 142, 20);
		contentPane.add(textFieldUlicaIBroj);
		textFieldUlicaIBroj.setColumns(10);

		comboBoxMjesto = new JComboBox<String>();
		comboBoxMjesto.setBounds(186, 329, 134, 22);
		contentPane.add(comboBoxMjesto);

		JButton btnDodajKorisnikaDodajMjesto = new JButton("Dodaj Mjesto");
		btnDodajKorisnikaDodajMjesto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DodajMjesto mjesto = new DodajMjesto("korisnik", DodajKorisnika.this);
				mjesto.setVisible(true);
			}
		});
		btnDodajKorisnikaDodajMjesto.setBounds(346, 329, 111, 23);
		contentPane.add(btnDodajKorisnikaDodajMjesto);

		listaMjestaIds = new ArrayList<Integer>();

		popuniComboBoxMjesta();

		btnDodajKorisnikaPotvrdi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ime = textFieldIme.getText();
				String prezime = textFieldPrezime.getText();
				String email = textFieldEmail.getText();
				String telefon = textFieldTelefon.getText();
				String ulica_i_broj = textFieldUlicaIBroj.getText();
				int selectedMjestoId = listaMjestaIds.get(comboBoxMjesto.getSelectedIndex());

				if (ime.isEmpty() || prezime.isEmpty() || email.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Ime, prezime i email su obaezna polja.", "Greška",
							JOptionPane.ERROR_MESSAGE);
				} else {
					try (Connection con = Konekcija.connect()) {
						String query = "INSERT INTO korisnik (ime, prezime, email, telefon, ulica_i_broj, id_mjesta) VALUES (?, ?, ?, ?, ?, ?)";
						PreparedStatement pst = con.prepareStatement(query);
						pst.setString(1, ime);
						pst.setString(2, prezime);
						pst.setString(3, email);
						pst.setString(4, telefon);
						pst.setString(5, ulica_i_broj);
						pst.setInt(6, selectedMjestoId);

						pst.executeUpdate();
						JOptionPane.showMessageDialog(null, "Korisnik uspješno dodan.", "",
								JOptionPane.INFORMATION_MESSAGE);

						dispose();
						PregledKorisnika korisnik = new PregledKorisnika();
						korisnik.setVisible(true);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Greška pri dodavanju korisnika.", "Greška",
								JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	public void refreshComboBox() {
		popuniComboBoxMjesta();
	}

	private void popuniComboBoxMjesta() {
		comboBoxMjesto.removeAllItems();
		listaMjestaIds.clear();
		try (Connection con = Konekcija.connect()) {
			String query = "SELECT id_mjesta, naziv_mjesta FROM mjesto";
			PreparedStatement pst = con.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id_mjesta");
				String naziv = rs.getString("naziv_mjesta");
				comboBoxMjesto.addItem(naziv);
				listaMjestaIds.add(id);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Greška pri dohvatu mjesta.", "Greška", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}
}
