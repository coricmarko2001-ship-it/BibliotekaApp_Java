package Biblioteka;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class VratiKnjigu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JComboBox<String> comboBoxKorisnici;
	private JComboBox<String> comboBoxKnjige;
	private JComboBox<String> comboBoxBibliotekari;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VratiKnjigu frame = new VratiKnjigu();
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
	public VratiKnjigu() {
		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 486, 290);
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

		JButton btnPotvrdi = new JButton("Potvrdi");
		btnPotvrdi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String korisnik = (String) comboBoxKorisnici.getSelectedItem();
				String knjiga = (String) comboBoxKnjige.getSelectedItem();
				String bibliotekar = (String) comboBoxBibliotekari.getSelectedItem();

				if (korisnik == null || knjiga == null || bibliotekar == null) {
					JOptionPane.showMessageDialog(null, "Sva polja su obavezna.", "Greška", JOptionPane.ERROR_MESSAGE);
				} else {
					try (Connection con = Konekcija.connect()) {

						int korisnikId = getIdForKorisnik(korisnik, con);
						int knjigaId = getIdForKnjiga(knjiga, con);
						int bibliotekarId = getIdForBibliotekar(bibliotekar, con);

						String deleteQuery = "DELETE FROM izdavanje WHERE id_korisnika = ? AND id_knjige = ?";
						PreparedStatement pstDelete = con.prepareStatement(deleteQuery);
						pstDelete.setInt(1, korisnikId);
						pstDelete.setInt(2, knjigaId);
						pstDelete.executeUpdate();

						povecajBrojDostupnihKnjiga(knjigaId, con);

						JOptionPane.showMessageDialog(null, "Knjiga uspješno vraćena.", "Uspjeh",
								JOptionPane.INFORMATION_MESSAGE);
						dispose();
						PregledIzdatihKnjiga pregledIzdatihKnjiga = new PregledIzdatihKnjiga();
						pregledIzdatihKnjiga.setVisible(true);

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Greška pri vraćanju knjige.", "Greška",
								JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
				}
			}
		});
		btnPotvrdi.setBounds(91, 170, 100, 30);
		contentPane.add(btnPotvrdi);

		JButton btnOdustani = new JButton("Odustani");
		btnOdustani.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Pocetna pocetna = new Pocetna();
				pocetna.setVisible(true);
			}
		});
		btnOdustani.setBounds(258, 170, 100, 30);
		contentPane.add(btnOdustani);

		popuniComboBoxove();
	}

	private void popuniComboBoxove() {
		try (Connection con = Konekcija.connect()) {

			String queryKorisnici = "SELECT CONCAT(ime, ' ', prezime) AS korisnik FROM korisnik";
			PreparedStatement pstKorisnici = con.prepareStatement(queryKorisnici);
			ResultSet rsKorisnici = pstKorisnici.executeQuery();
			while (rsKorisnici.next()) {
				comboBoxKorisnici.addItem(rsKorisnici.getString("korisnik"));
			}

			String queryKnjige = "SELECT naziv_knjige FROM knjige";
			PreparedStatement pstKnjige = con.prepareStatement(queryKnjige);
			ResultSet rsKnjige = pstKnjige.executeQuery();
			while (rsKnjige.next()) {
				comboBoxKnjige.addItem(rsKnjige.getString("naziv_knjige"));
			}

			String queryBibliotekari = "SELECT CONCAT(ime, ' ', prezime) AS bibliotekar FROM bibliotekar";
			PreparedStatement pstBibliotekari = con.prepareStatement(queryBibliotekari);
			ResultSet rsBibliotekari = pstBibliotekari.executeQuery();
			while (rsBibliotekari.next()) {
				comboBoxBibliotekari.addItem(rsBibliotekari.getString("bibliotekar"));
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Greška pri punjenju combo boxova.", "Greška",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	private int getIdForKorisnik(String korisnik, Connection con) throws Exception {
		String query = "SELECT id_korisnika FROM korisnik WHERE CONCAT(ime, ' ', prezime) = ?";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setString(1, korisnik);
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			return rs.getInt("id_korisnika");
		} else {
			throw new Exception("Korisnik nije pronađen.");
		}
	}

	private int getIdForKnjiga(String knjiga, Connection con) throws Exception {
		String query = "SELECT id_knjige FROM knjige WHERE naziv_knjige = ?";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setString(1, knjiga);
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			return rs.getInt("id_knjige");
		} else {
			throw new Exception("Knjiga nije pronađena.");
		}
	}

	private int getIdForBibliotekar(String bibliotekar, Connection con) throws Exception {
		String query = "SELECT id_bibliotekara FROM bibliotekar WHERE CONCAT(ime, ' ', prezime) = ?";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setString(1, bibliotekar);
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			return rs.getInt("id_bibliotekara");
		} else {
			throw new Exception("Bibliotekar nije pronađen.");
		}
	}

	private void povecajBrojDostupnihKnjiga(int knjigaId, Connection con) throws Exception {
		String query = "UPDATE knjige SET broj_dostupnih_knjiga = broj_dostupnih_knjiga + 1 WHERE id_knjige = ?";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setInt(1, knjigaId);
		pst.executeUpdate();
	}
}
