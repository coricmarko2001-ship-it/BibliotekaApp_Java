package Biblioteka;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PregledIzdatihKnjiga extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private int selectedRowId = -1;
	private JTextField textFieldPretraziPoImenuKorisnika;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PregledIzdatihKnjiga frame = new PregledIzdatihKnjiga();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public PregledIzdatihKnjiga() {
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

		setLocationRelativeTo(null);
		setVisible(true);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 68, 1046, 283);
		contentPane.add(scrollPane);

		table = new JTable();
		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				int row = table.getSelectedRow();
				if (row >= 0) {
					selectedRowId = (int) table.getValueAt(row, 0);
				}
			}
		});
		scrollPane.setViewportView(table);

		JButton btnOsvjezi = new JButton("Osvježi");
		btnOsvjezi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				prikaziIzdavanja(null);
			}
		});
		btnOsvjezi.setBounds(44, 378, 87, 23);
		contentPane.add(btnOsvjezi);

		JButton btnIzmijeni = new JButton("Izmijeni");
		btnIzmijeni.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedRowId == -1) {
					JOptionPane.showMessageDialog(null, "Nema selektovanog izdanja za izmjenu.", "Greška",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				// Open IzmijeniIzdavanja form with selected data
				try (Connection con = Konekcija.connect()) {
					String query = "SELECT izdavanje.id_izdavanja, izdavanje.datum_izdavanja, izdavanje.datum_vracanja, "
							+ "izdavanje.id_korisnika, izdavanje.id_knjige, izdavanje.id_bibliotekara "
							+ "FROM izdavanje " + "WHERE izdavanje.id_izdavanja = ?";
					PreparedStatement pst = con.prepareStatement(query);
					pst.setInt(1, selectedRowId);
					ResultSet rs = pst.executeQuery();
					if (rs.next()) {
						int idIzdavanja = rs.getInt("id_izdavanja");

						int idKorisnika = rs.getInt("id_korisnika");
						int idKnjige = rs.getInt("id_knjige");
						int idBibliotekara = rs.getInt("id_bibliotekara");
						String datumIzdavanja = rs.getString("datum_izdavanja");
						String datumVracanja = rs.getString("datum_vracanja");

						dispose();
						IzmijeniIzdavanja izmijeniIzdavanja = new IzmijeniIzdavanja(idIzdavanja, idKorisnika, idKnjige,
								idBibliotekara, datumIzdavanja, datumVracanja);
						izmijeniIzdavanja.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(null, "Greška pri učitavanju podataka za izmjenu.", "Greška",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Greška pri učitavanju podataka za izmjenu.", "Greška",
							JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}

			}
		});
		btnIzmijeni.setBounds(141, 378, 87, 23);
		contentPane.add(btnIzmijeni);

		JButton btnPregledIzdatihKnjigaIzadji = new JButton("Izađi");
		btnPregledIzdatihKnjigaIzadji.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Pocetna pocetna = new Pocetna();
				pocetna.setVisible(true);
			}
		});
		btnPregledIzdatihKnjigaIzadji.setBounds(933, 378, 87, 23);
		contentPane.add(btnPregledIzdatihKnjigaIzadji);

		JLabel lblNewLabel = new JLabel("Ime Korisnika");
		lblNewLabel.setBounds(384, 22, 108, 14);
		contentPane.add(lblNewLabel);

		textFieldPretraziPoImenuKorisnika = new JTextField();
		textFieldPretraziPoImenuKorisnika.setBounds(502, 19, 174, 20);
		contentPane.add(textFieldPretraziPoImenuKorisnika);
		textFieldPretraziPoImenuKorisnika.setColumns(10);

		textFieldPretraziPoImenuKorisnika.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				filterIzdavanja();
			}

			public void removeUpdate(DocumentEvent e) {
				filterIzdavanja();
			}

			public void insertUpdate(DocumentEvent e) {
				filterIzdavanja();
			}

			private void filterIzdavanja() {
				String imeKorisnika = textFieldPretraziPoImenuKorisnika.getText().trim().toLowerCase();
				if (!imeKorisnika.isEmpty()) {
					prikaziIzdavanja(imeKorisnika);
				} else {
					prikaziIzdavanja(null);
				}
			}
		});

		prikaziIzdavanja(null);
	}

	private void prikaziIzdavanja(String imeKorisnika) {
		try (Connection con = Konekcija.connect()) {
			String query = "SELECT izdavanje.id_izdavanja AS 'ID Izdavanja', "
					+ "DATE_FORMAT(izdavanje.datum_izdavanja, '%d.%m.%Y') AS 'Datum izdavanja', "
					+ "DATE_FORMAT(izdavanje.datum_vracanja, '%d.%m.%Y') AS 'Datum vraćanja', "
					+ "CONCAT(korisnik.ime, ' ', korisnik.prezime) AS 'Korisnik', "
					+ "knjige.naziv_knjige AS 'Naziv knjige', "
					+ "CONCAT(bibliotekar.ime, ' ', bibliotekar.prezime) AS 'Bibliotekar' " + "FROM izdavanje "
					+ "JOIN korisnik ON izdavanje.id_korisnika = korisnik.id_korisnika "
					+ "JOIN knjige ON izdavanje.id_knjige = knjige.id_knjige "
					+ "JOIN bibliotekar ON izdavanje.id_bibliotekara = bibliotekar.id_bibliotekara ";
			if (imeKorisnika != null && !imeKorisnika.isEmpty()) {
				query += "WHERE LOWER(CONCAT(korisnik.ime, ' ', korisnik.prezime)) LIKE ? ";
			}
			query += "ORDER BY izdavanje.id_izdavanja";

			PreparedStatement pst = con.prepareStatement(query);
			if (imeKorisnika != null && !imeKorisnika.isEmpty()) {
				pst.setString(1, "%" + imeKorisnika + "%");
			}
			ResultSet rs = pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Greška pri prikazu izdatih knjiga.", "Greška",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}
}
