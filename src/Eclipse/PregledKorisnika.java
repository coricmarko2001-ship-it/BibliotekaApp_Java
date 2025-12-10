package Biblioteka;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PregledKorisnika extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private int selectedUserId = -1;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PregledKorisnika frame = new PregledKorisnika();
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
	public PregledKorisnika() {
		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1082, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		setLocationRelativeTo(null);
		setVisible(true);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 66, 1046, 285);
		contentPane.add(scrollPane);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				if (row >= 0) {

					selectedUserId = (int) table.getValueAt(row, 0);
				}
			}
		});
		scrollPane.setViewportView(table);

		JButton btnPregledKorisnikaIzmijeni = new JButton("Izmijeni");
		btnPregledKorisnikaIzmijeni.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedUserId == -1) {
					JOptionPane.showMessageDialog(null, "Nema selektovanog korisnika za izmjenu.", "Greška",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				dispose();
				IzmijeniKorisnika korisnik = new IzmijeniKorisnika(selectedUserId);
				korisnik.setVisible(true);
			}
		});
		btnPregledKorisnikaIzmijeni.setBounds(259, 378, 87, 23);
		contentPane.add(btnPregledKorisnikaIzmijeni);

		JButton btnPregledKorisnikaDodaj = new JButton("Dodaj");
		btnPregledKorisnikaDodaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				DodajKorisnika korisnik = new DodajKorisnika();
				korisnik.setVisible(true);
			}
		});
		btnPregledKorisnikaDodaj.setBounds(450, 378, 87, 23);
		contentPane.add(btnPregledKorisnikaDodaj);

		JButton btnPregledKorisnikaObrisi = new JButton("Obriši");
		btnPregledKorisnikaObrisi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedUserId == -1) {
					JOptionPane.showMessageDialog(null, "Nema selektovanog korisnika za brisanje.", "Greška",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (korisnikImaIzdatihKnjiga(selectedUserId)) {
					JOptionPane.showMessageDialog(null, "Korisnik ima izdate knjige i ne može biti obrisan.", "Greška",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				int confirm = JOptionPane.showConfirmDialog(null,
						"Da li ste sigurni da želite da obrišete ovog korisnika?", "Potvrda brisanja",
						JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					try (Connection con = Konekcija.connect()) {
						String query = "DELETE FROM korisnik WHERE id_korisnika = ?";
						PreparedStatement pst = con.prepareStatement(query);
						pst.setInt(1, selectedUserId);
						pst.executeUpdate();
						JOptionPane.showMessageDialog(null, "Korisnik uspješno obrisan.", "",
								JOptionPane.INFORMATION_MESSAGE);
						prikaziKorisnike(-1);
						selectedUserId = -1;
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Greška pri brisanju korisnika.", "Greška",
								JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
				}
			}
		});
		btnPregledKorisnikaObrisi.setBounds(641, 378, 87, 23);
		contentPane.add(btnPregledKorisnikaObrisi);

		JButton btnPregledKorisnikaIzadji = new JButton("Izađi");
		btnPregledKorisnikaIzadji.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Pocetna pocetna = new Pocetna();
				pocetna.setVisible(true);
			}
		});
		btnPregledKorisnikaIzadji.setBounds(933, 378, 87, 23);
		contentPane.add(btnPregledKorisnikaIzadji);

		JButton btnOsvjezi = new JButton("Osvježi");
		btnOsvjezi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				prikaziKorisnike(-1);
			}
		});
		btnOsvjezi.setBounds(44, 378, 87, 23);
		contentPane.add(btnOsvjezi);

		JLabel lblNewLabel = new JLabel("ID Korisnika");
		lblNewLabel.setBounds(417, 25, 73, 14);
		contentPane.add(lblNewLabel);

		textField = new JTextField();
		textField.setBounds(500, 22, 159, 20);
		contentPane.add(textField);
		textField.setColumns(10);

		textField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				filterKorisnici();
			}

			public void removeUpdate(DocumentEvent e) {
				filterKorisnici();
			}

			public void insertUpdate(DocumentEvent e) {
				filterKorisnici();
			}

			private void filterKorisnici() {
				String idKorisnika = textField.getText().trim();
				if (!idKorisnika.isEmpty() && idKorisnika.matches("\\d+")) {
					prikaziKorisnike(Integer.parseInt(idKorisnika));
				} else {
					prikaziKorisnike(-1);
				}
			}
		});

		prikaziKorisnike(-1);
	}

	private void prikaziKorisnike(int idKorisnika) {
		try (Connection con = Konekcija.connect()) {
			String query = "SELECT k.id_korisnika AS 'ID Korisnika', " + "k.ime AS Ime, " + "k.prezime AS Prezime, "
					+ "k.email AS Email, " + "k.telefon AS 'Kontakt telefon', " + "k.ulica_i_broj AS 'Ulica i broj', "
					+ "m.naziv_mjesta AS 'Naziv mjesta' " + "FROM korisnik k "
					+ "JOIN mjesto m ON k.id_mjesta = m.id_mjesta ";
			if (idKorisnika != -1) {
				query += "WHERE k.id_korisnika = ? ";
			}
			query += "ORDER BY k.id_korisnika";

			PreparedStatement pst = con.prepareStatement(query);
			if (idKorisnika != -1) {
				pst.setInt(1, idKorisnika);
			}
			ResultSet rs = pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Greška pri prikazu korisnika.", "Greška", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	private boolean korisnikImaIzdatihKnjiga(int idKorisnika) {
		boolean hasIssuedBooks = false;
		try (Connection con = Konekcija.connect()) {
			String query = "SELECT COUNT(*) FROM izdavanje WHERE id_korisnika = ?";
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, idKorisnika);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				hasIssuedBooks = rs.getInt(1) > 0;
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Greška pri provjeri izdatih knjiga korisnika.", "Greška",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
		return hasIssuedBooks;
	}
}
