package Biblioteka;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RezervisiKnjigu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JComboBox<String> comboBoxKorisnici;
	private JComboBox<String> comboBoxKnjige;
	private JComboBox<String> comboBoxBibliotekari;
	private JDateChooser dateChooserIzdavanja;
	private JDateChooser dateChooserVracanja;

	private HashMap<String, Integer> korisniciMap = new HashMap<>();
	private HashMap<String, Integer> knjigeMap = new HashMap<>();
	private HashMap<String, Integer> bibliotekariMap = new HashMap<>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RezervisiKnjigu frame = new RezervisiKnjigu();
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
	public RezervisiKnjigu() {
		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 486, 373);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		setLocationRelativeTo(null);
		setVisible(true);

		JLabel lblKorisnik = new JLabel("Ime i prezime korisnika");
		lblKorisnik.setBounds(20, 30, 160, 25);
		contentPane.add(lblKorisnik);

		comboBoxKorisnici = new JComboBox<>();
		comboBoxKorisnici.setBounds(190, 30, 206, 25);
		contentPane.add(comboBoxKorisnici);

		JLabel lblKnjiga = new JLabel("Naziv knjige");
		lblKnjiga.setBounds(20, 70, 160, 25);
		contentPane.add(lblKnjiga);

		comboBoxKnjige = new JComboBox<>();
		comboBoxKnjige.setBounds(190, 70, 206, 25);
		contentPane.add(comboBoxKnjige);

		JLabel lblBibliotekar = new JLabel("Ime i prezime bibliotekara");
		lblBibliotekar.setBounds(20, 110, 160, 25);
		contentPane.add(lblBibliotekar);

		comboBoxBibliotekari = new JComboBox<>();
		comboBoxBibliotekari.setBounds(190, 110, 206, 25);
		contentPane.add(comboBoxBibliotekari);

		JLabel lblDatumIzdavanja = new JLabel("Datum izdavanja");
		lblDatumIzdavanja.setBounds(20, 150, 160, 25);
		contentPane.add(lblDatumIzdavanja);

		dateChooserIzdavanja = new JDateChooser();
		dateChooserIzdavanja.setBounds(190, 150, 229, 25);
		contentPane.add(dateChooserIzdavanja);

		JLabel lblDatumVracanja = new JLabel("Datum vraćanja");
		lblDatumVracanja.setBounds(20, 190, 160, 25);
		contentPane.add(lblDatumVracanja);

		dateChooserVracanja = new JDateChooser();
		dateChooserVracanja.setBounds(190, 190, 229, 25);
		contentPane.add(dateChooserVracanja);

		JButton btnPotvrdi = new JButton("Potvrdi");
		btnPotvrdi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String korisnik = (String) comboBoxKorisnici.getSelectedItem();
				String knjiga = (String) comboBoxKnjige.getSelectedItem();
				String bibliotekar = (String) comboBoxBibliotekari.getSelectedItem();
				Date datumIzdavanja = dateChooserIzdavanja.getDate();
				Date datumVracanja = dateChooserVracanja.getDate();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				if (korisnik == null || knjiga == null || bibliotekar == null || datumIzdavanja == null
						|| datumVracanja == null) {
					JOptionPane.showMessageDialog(null, "Sva polja su obavezna.", "Greška", JOptionPane.ERROR_MESSAGE);
				} else {
					try (Connection con = Konekcija.connect()) {
						int idKorisnika = korisniciMap.get(korisnik);
						int idKnjige = knjigeMap.get(knjiga);
						int idBibliotekara = bibliotekariMap.get(bibliotekar);

						String query = "INSERT INTO izdavanje (datum_izdavanja, datum_vracanja, id_korisnika, id_knjige, id_bibliotekara) VALUES (?, ?, ?, ?, ?)";
						PreparedStatement pst = con.prepareStatement(query);
						pst.setString(1, sdf.format(datumIzdavanja));
						pst.setString(2, sdf.format(datumVracanja));
						pst.setInt(3, idKorisnika);
						pst.setInt(4, idKnjige);
						pst.setInt(5, idBibliotekara);

						pst.executeUpdate();

						smanjiBrojDostupnihKnjiga(idKnjige);

						JOptionPane.showMessageDialog(null, "Knjiga uspješno rezervisana.", "Uspjeh",
								JOptionPane.INFORMATION_MESSAGE);
						dispose();
						PregledIzdatihKnjiga pregledIzdatihKnjiga = new PregledIzdatihKnjiga();
						pregledIzdatihKnjiga.setVisible(true);

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Greška pri rezervaciji knjige.", "Greška",
								JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
				}
			}
		});
		btnPotvrdi.setBounds(91, 262, 100, 30);
		contentPane.add(btnPotvrdi);

		JButton btnOdustani = new JButton("Odustani");
		btnOdustani.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Pocetna pocetna = new Pocetna();
				pocetna.setVisible(true);
			}
		});
		btnOdustani.setBounds(258, 262, 100, 30);
		contentPane.add(btnOdustani);

		popuniComboBoxove();
	}

	private void popuniComboBoxove() {
		try (Connection con = Konekcija.connect()) {

			String queryKorisnici = "SELECT ID_KORISNIKA, CONCAT(ime, ' ', prezime) AS korisnik FROM korisnik";
			PreparedStatement pstKorisnici = con.prepareStatement(queryKorisnici);
			ResultSet rsKorisnici = pstKorisnici.executeQuery();
			while (rsKorisnici.next()) {
				int idKorisnika = rsKorisnici.getInt("ID_KORISNIKA");
				String imeKorisnika = rsKorisnici.getString("korisnik");
				korisniciMap.put(imeKorisnika, idKorisnika);
				comboBoxKorisnici.addItem(imeKorisnika);
			}

			String queryKnjige = "SELECT ID_KNJIGE, naziv_knjige FROM knjige";
			PreparedStatement pstKnjige = con.prepareStatement(queryKnjige);
			ResultSet rsKnjige = pstKnjige.executeQuery();
			while (rsKnjige.next()) {
				int idKnjige = rsKnjige.getInt("ID_KNJIGE");
				String nazivKnjige = rsKnjige.getString("naziv_knjige");
				knjigeMap.put(nazivKnjige, idKnjige);
				comboBoxKnjige.addItem(nazivKnjige);
			}

			String queryBibliotekari = "SELECT ID_BIBLIOTEKARA, CONCAT(ime, ' ', prezime) AS bibliotekar FROM bibliotekar";
			PreparedStatement pstBibliotekari = con.prepareStatement(queryBibliotekari);
			ResultSet rsBibliotekari = pstBibliotekari.executeQuery();
			while (rsBibliotekari.next()) {
				int idBibliotekara = rsBibliotekari.getInt("ID_BIBLIOTEKARA");
				String imeBibliotekara = rsBibliotekari.getString("bibliotekar");
				bibliotekariMap.put(imeBibliotekara, idBibliotekara);
				comboBoxBibliotekari.addItem(imeBibliotekara);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Greška pri punjenju combo boxova.", "Greška",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	private void smanjiBrojDostupnihKnjiga(int idKnjige) {
		try (Connection con = Konekcija.connect()) {
			String query = "UPDATE knjige SET broj_dostupnih_knjiga = broj_dostupnih_knjiga - 1 WHERE ID_KNJIGE = ?";
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, idKnjige);
			pst.executeUpdate();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Greška pri ažuriranju broja dostupnih knjiga.", "Greška",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}
}
