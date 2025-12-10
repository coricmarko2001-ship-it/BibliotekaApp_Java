package Biblioteka;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import com.toedter.calendar.JDateChooser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class IzmijeniIzdavanja extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JComboBox<String> cmbKorisnici;
	private JComboBox<String> cmbKnjige;
	private JComboBox<String> cmbBibliotekari;
	private JDateChooser dateChooserDatumIzdavanja;
	private JDateChooser dateChooserDatumVracanja;
	private int idIzdavanja;
	private JTextField textFieldIDIzdavanja;

	public IzmijeniIzdavanja(int idIzdavanja, int idKorisnika, int idKnjige, int idBibliotekara, String datumIzdavanja,
			String datumVracanja) {
		this.idIzdavanja = idIzdavanja;

		System.out.println(idIzdavanja);

		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 447);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		setLocationRelativeTo(null);
		setVisible(true);

		JLabel lblKorisnik = new JLabel("Korisnik");
		lblKorisnik.setBounds(34, 74, 70, 14);
		contentPane.add(lblKorisnik);

		cmbKorisnici = new JComboBox<>();
		cmbKorisnici.setBounds(173, 71, 200, 20);
		contentPane.add(cmbKorisnici);

		JLabel lblKnjiga = new JLabel("Knjiga");
		lblKnjiga.setBounds(34, 127, 70, 14);
		contentPane.add(lblKnjiga);

		cmbKnjige = new JComboBox<>();
		cmbKnjige.setBounds(173, 124, 200, 20);
		contentPane.add(cmbKnjige);

		JLabel lblBibliotekar = new JLabel("Bibliotekar");
		lblBibliotekar.setBounds(34, 174, 80, 14);
		contentPane.add(lblBibliotekar);

		cmbBibliotekari = new JComboBox<>();
		cmbBibliotekari.setBounds(173, 171, 200, 20);
		contentPane.add(cmbBibliotekari);

		JLabel lblDatumIzdavanja = new JLabel("Datum izdavanja");
		lblDatumIzdavanja.setBounds(34, 227, 120, 14);
		contentPane.add(lblDatumIzdavanja);

		dateChooserDatumIzdavanja = new JDateChooser();
		dateChooserDatumIzdavanja.setBounds(180, 221, 193, 20);
		contentPane.add(dateChooserDatumIzdavanja);

		JLabel lblDatumVracanja = new JLabel("Datum vraćanja");
		lblDatumVracanja.setBounds(34, 277, 120, 14);
		contentPane.add(lblDatumVracanja);

		dateChooserDatumVracanja = new JDateChooser();
		dateChooserDatumVracanja.setBounds(180, 271, 193, 20);
		contentPane.add(dateChooserDatumVracanja);

		JButton btnPotvrdi = new JButton("Potvrdi");
		btnPotvrdi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveChanges();
				PregledIzdatihKnjiga knjiga = new PregledIzdatihKnjiga();
				knjiga.setVisible(true);
			}
		});
		btnPotvrdi.setBounds(109, 353, 100, 23);
		contentPane.add(btnPotvrdi);

		JButton btnOdustani = new JButton("Odustani");
		btnOdustani.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				PregledIzdatihKnjiga knjiga = new PregledIzdatihKnjiga();
				knjiga.setVisible(true);
			}
		});
		btnOdustani.setBounds(251, 353, 100, 23);
		contentPane.add(btnOdustani);

		JLabel lblNewLabel = new JLabel("ID Izdavanja");
		lblNewLabel.setBounds(34, 33, 100, 14);
		contentPane.add(lblNewLabel);

		textFieldIDIzdavanja = new JTextField();
		textFieldIDIzdavanja.setBounds(173, 30, 200, 20);
		contentPane.add(textFieldIDIzdavanja);
		textFieldIDIzdavanja.setColumns(10);
		textFieldIDIzdavanja.setEditable(false); // ID field should not be editable

		// Display the idIzdavanja in the textFieldIDIzdavanja
		textFieldIDIzdavanja.setText(String.valueOf(idIzdavanja));

		loadData(idKorisnika, idKnjige, idBibliotekara, datumIzdavanja, datumVracanja);
	}

	private void loadData(int idKorisnika, int idKnjige, int idBibliotekara, String datumIzdavanja,
			String datumVracanja) {
		try (Connection con = Konekcija.connect()) {
			// Load korisnici
			String query = "SELECT id_korisnika, CONCAT(ime, ' ', prezime) AS korisnik FROM korisnik";
			PreparedStatement pst = con.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				cmbKorisnici.addItem(rs.getString("korisnik"));
			}
			cmbKorisnici.setSelectedItem(getKorisnikNameById(idKorisnika, con));

			// Load knjige
			query = "SELECT id_knjige, naziv_knjige FROM knjige";
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				cmbKnjige.addItem(rs.getString("naziv_knjige"));
			}
			cmbKnjige.setSelectedItem(getKnjigaNameById(idKnjige, con));

			// Load bibliotekari
			query = "SELECT id_bibliotekara, CONCAT(ime, ' ', prezime) AS bibliotekar FROM bibliotekar";
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				cmbBibliotekari.addItem(rs.getString("bibliotekar"));
			}
			cmbBibliotekari.setSelectedItem(getBibliotekarNameById(idBibliotekara, con));

			// Set dates
			dateChooserDatumIzdavanja.setDate(java.sql.Date.valueOf(datumIzdavanja));
			dateChooserDatumVracanja.setDate(java.sql.Date.valueOf(datumVracanja));
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Greška pri učitavanju podataka.", "Greška", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	private String getKorisnikNameById(int id, Connection con) throws Exception {
		String query = "SELECT CONCAT(ime, ' ', prezime) AS korisnik FROM korisnik WHERE id_korisnika = ?";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setInt(1, id);
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			return rs.getString("korisnik");
		}
		return null;
	}

	private String getKnjigaNameById(int id, Connection con) throws Exception {
		String query = "SELECT naziv_knjige FROM knjige WHERE id_knjige = ?";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setInt(1, id);
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			return rs.getString("naziv_knjige");
		}
		return null;
	}

	private String getBibliotekarNameById(int id, Connection con) throws Exception {
		String query = "SELECT CONCAT(ime, ' ', prezime) AS bibliotekar FROM bibliotekar WHERE id_bibliotekara = ?";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setInt(1, id);
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			return rs.getString("bibliotekar");
		}
		return null;
	}

	private void saveChanges() {
		try (Connection con = Konekcija.connect()) {

			int idKorisnika = getIdFromComboBox(cmbKorisnici, "korisnik", "id_korisnika", "CONCAT(ime, ' ', prezime)");
			int idKnjige = getIdFromComboBox(cmbKnjige, "knjige", "id_knjige", "naziv_knjige");
			int idBibliotekara = getIdFromComboBox(cmbBibliotekari, "bibliotekar", "id_bibliotekara",
					"CONCAT(ime, ' ', prezime)");
			java.sql.Date datumIzdavanja = new java.sql.Date(dateChooserDatumIzdavanja.getDate().getTime());
			java.sql.Date datumVracanja = new java.sql.Date(dateChooserDatumVracanja.getDate().getTime());

			String query = "UPDATE izdavanje SET id_korisnika = ?, id_knjige = ?, id_bibliotekara = ?, datum_izdavanja = ?, datum_vracanja = ? WHERE id_izdavanja = ?";
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, idKorisnika);
			pst.setInt(2, idKnjige);
			pst.setInt(3, idBibliotekara);
			pst.setDate(4, datumIzdavanja);
			pst.setDate(5, datumVracanja);
			pst.setInt(6, idIzdavanja);

			int rowsUpdated = pst.executeUpdate();
			if (rowsUpdated > 0) {
				JOptionPane.showMessageDialog(null, "Podaci su uspješno ažurirani.", "",
						JOptionPane.INFORMATION_MESSAGE);
				dispose();
			} else {
				JOptionPane.showMessageDialog(null, "Greška pri ažuriranju podataka.", "Greška",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Greška pri čuvanju podataka.", "Greška", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	private int getIdFromComboBox(JComboBox<String> comboBox, String tableName, String idColumnName,
			String nameColumnName) throws Exception {
		String selectedItem = (String) comboBox.getSelectedItem();
		String query = "SELECT " + idColumnName + " FROM " + tableName + " WHERE " + nameColumnName + " = ?";
		PreparedStatement pst = Konekcija.connect().prepareStatement(query);
		pst.setString(1, selectedItem);
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			return rs.getInt(idColumnName);
		}
		return -1;
	}

}
