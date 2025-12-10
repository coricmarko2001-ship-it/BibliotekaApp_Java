package Biblioteka;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class IzmijeniKorisnika extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtIme;
	private JTextField txtPrezime;
	private JTextField txtEmail;
	private JTextField txtTelefon;
	private JTextField txtUlicaIBroj;
	private JComboBox<String> cbMjesto;
	private int userId;

	public IzmijeniKorisnika(int userId) {
		setUndecorated(true);
		setResizable(false);
		this.userId = userId;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		setLocationRelativeTo(null);
		setVisible(true);

		JLabel lblIme = new JLabel("Ime");
		lblIme.setBounds(10, 14, 66, 14);
		contentPane.add(lblIme);

		txtIme = new JTextField();
		txtIme.setBounds(154, 11, 146, 20);
		contentPane.add(txtIme);
		txtIme.setColumns(10);

		JLabel lblPrezime = new JLabel("Prezime");
		lblPrezime.setBounds(10, 45, 66, 14);
		contentPane.add(lblPrezime);

		txtPrezime = new JTextField();
		txtPrezime.setBounds(154, 42, 146, 20);
		contentPane.add(txtPrezime);
		txtPrezime.setColumns(10);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setBounds(10, 76, 98, 14);
		contentPane.add(lblEmail);

		txtEmail = new JTextField();
		txtEmail.setBounds(154, 73, 146, 20);
		contentPane.add(txtEmail);
		txtEmail.setColumns(10);

		JLabel lblTelefon = new JLabel("Telefon");
		lblTelefon.setBounds(10, 107, 98, 14);
		contentPane.add(lblTelefon);

		txtTelefon = new JTextField();
		txtTelefon.setBounds(154, 104, 146, 20);
		contentPane.add(txtTelefon);
		txtTelefon.setColumns(10);

		JLabel lblUlicaIBroj = new JLabel("Ulica i broj");
		lblUlicaIBroj.setBounds(10, 138, 98, 14);
		contentPane.add(lblUlicaIBroj);

		txtUlicaIBroj = new JTextField();
		txtUlicaIBroj.setBounds(154, 135, 146, 20);
		contentPane.add(txtUlicaIBroj);
		txtUlicaIBroj.setColumns(10);

		JLabel lblMjesto = new JLabel("Mjesto");
		lblMjesto.setBounds(10, 169, 89, 14);
		contentPane.add(lblMjesto);

		cbMjesto = new JComboBox<>();
		cbMjesto.setBounds(154, 166, 146, 20);
		contentPane.add(cbMjesto);

		JButton btnSave = new JButton("Potvrdi");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateKorisnik();
			}
		});
		btnSave.setBounds(107, 217, 89, 23);
		contentPane.add(btnSave);

		JButton btnCancel = new JButton("Odustani");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(244, 217, 89, 23);
		contentPane.add(btnCancel);

		loadMjesta();
		loadKorisnikData();
	}

	private void loadMjesta() {
		try (Connection con = Konekcija.connect()) {
			String query = "SELECT naziv_mjesta FROM mjesto";
			PreparedStatement pst = con.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			cbMjesto.removeAllItems(); // Clear previous items
			while (rs.next()) {
				cbMjesto.addItem(rs.getString("naziv_mjesta"));
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Greška pri učitavanju mjesta.", "Greška", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	private void loadKorisnikData() {
		try (Connection con = Konekcija.connect()) {
			String query = "SELECT ime, prezime, email, telefon, ulica_i_broj, naziv_mjesta FROM korisnik JOIN mjesto ON korisnik.id_mjesta = mjesto.id_mjesta WHERE id_korisnika = ?";
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, userId);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				txtIme.setText(rs.getString("ime"));
				txtPrezime.setText(rs.getString("prezime"));
				txtEmail.setText(rs.getString("email"));
				txtTelefon.setText(rs.getString("telefon"));
				txtUlicaIBroj.setText(rs.getString("ulica_i_broj"));
				cbMjesto.setSelectedItem(rs.getString("naziv_mjesta"));
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Greška pri učitavanju podataka korisnika.", "Greška",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	private void updateKorisnik() {
		try (Connection con = Konekcija.connect()) {
			String query = "UPDATE korisnik SET ime = ?, prezime = ?, email = ?, telefon = ?, ulica_i_broj = ?, id_mjesta = (SELECT id_mjesta FROM mjesto WHERE naziv_mjesta = ?) WHERE id_korisnika = ?";
			PreparedStatement pst = con.prepareStatement(query);
			pst.setString(1, txtIme.getText());
			pst.setString(2, txtPrezime.getText());
			pst.setString(3, txtEmail.getText());
			pst.setString(4, txtTelefon.getText());
			pst.setString(5, txtUlicaIBroj.getText());
			pst.setString(6, (String) cbMjesto.getSelectedItem());
			pst.setInt(7, userId);
			pst.executeUpdate();
			JOptionPane.showMessageDialog(null, "Podaci uspješno ažurirani.", "", JOptionPane.INFORMATION_MESSAGE);

			dispose();
			PregledKorisnika korisnik = new PregledKorisnika();
			korisnik.setVisible(true);

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Greška pri ažuriranju podataka korisnika.", "Greška",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}
}
