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
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PregledKnjiga extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private int selectedBookId = -1;
	private JTextField textFieldPretragaPoNazivuKnjige;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PregledKnjiga frame = new PregledKnjiga();
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
	public PregledKnjiga() {
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
		scrollPane.setBounds(10, 62, 1046, 289);
		contentPane.add(scrollPane);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				if (row >= 0) {

					selectedBookId = (int) table.getValueAt(row, 0);
				}
			}
		});
		scrollPane.setViewportView(table);

		JButton btnPregledKnjigaIzmijeni = new JButton("Izmijeni");
		btnPregledKnjigaIzmijeni.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedBookId == -1) {
					JOptionPane.showMessageDialog(null, "Nema selektovane knjige za izmjenu.", "Greška",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				dispose();
				IzmijeniKnjigu izmijeniKnjigu = new IzmijeniKnjigu(selectedBookId);
				izmijeniKnjigu.setVisible(true);
			}
		});
		btnPregledKnjigaIzmijeni.setBounds(259, 378, 87, 23);
		contentPane.add(btnPregledKnjigaIzmijeni);

		JButton btnPregeldKnjigaDodaj = new JButton("Dodaj");
		btnPregeldKnjigaDodaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				DodajKnjigu knjiga = new DodajKnjigu();
				knjiga.setVisible(true);
			}
		});
		btnPregeldKnjigaDodaj.setBounds(450, 378, 87, 23);
		contentPane.add(btnPregeldKnjigaDodaj);

		JButton btnPregledKnjigaObrisi = new JButton("Obriši");
		btnPregledKnjigaObrisi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] selectedRows = table.getSelectedRows();
				if (selectedRows.length == 0) {
					JOptionPane.showMessageDialog(null, "Nema selektovane knjige za brisanje.", "Greška",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				for (int row : selectedRows) {
					int idKnjige = (int) table.getValueAt(row, 0);
					if (IzdataKnjigaPoId(idKnjige)) {
						JOptionPane.showMessageDialog(null, "Označenu knjigu ne možete obrisati jer je izdata.",
								"Greška", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}

				int confirm = JOptionPane.showConfirmDialog(null, "Da li ste sigurni da želite da obrišete ove knjige?",
						"Potvrda brisanja", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					for (int row : selectedRows) {
						int idKnjige = (int) table.getValueAt(row, 0);
						try (Connection con = Konekcija.connect()) {
							String query = "DELETE FROM knjige WHERE id_knjige = ?";
							PreparedStatement pst = con.prepareStatement(query);
							pst.setInt(1, idKnjige);
							pst.executeUpdate();
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(null, "Greška pri brisanju knjige.", "Greška",
									JOptionPane.ERROR_MESSAGE);
							ex.printStackTrace();
						}
					}
					JOptionPane.showMessageDialog(null, "Knjiga je uspješno obrisana.", "",
							JOptionPane.INFORMATION_MESSAGE);
					prikaziKnjige(null);
				}
			}
		});
		btnPregledKnjigaObrisi.setBounds(641, 378, 87, 23);
		contentPane.add(btnPregledKnjigaObrisi);

		JButton btnPregledKnjigaIzadji = new JButton("Izađi");
		btnPregledKnjigaIzadji.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Pocetna pocetna = new Pocetna();
				pocetna.setVisible(true);
			}
		});
		btnPregledKnjigaIzadji.setBounds(933, 378, 87, 23);
		contentPane.add(btnPregledKnjigaIzadji);

		JLabel lblNewLabel = new JLabel("Naziv Knjige");
		lblNewLabel.setBounds(390, 23, 73, 14);
		contentPane.add(lblNewLabel);

		textFieldPretragaPoNazivuKnjige = new JTextField();
		textFieldPretragaPoNazivuKnjige.setBounds(483, 20, 165, 20);
		contentPane.add(textFieldPretragaPoNazivuKnjige);
		textFieldPretragaPoNazivuKnjige.setColumns(10);

		textFieldPretragaPoNazivuKnjige.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				filterKnjige();
			}

			public void removeUpdate(DocumentEvent e) {
				filterKnjige();
			}

			public void insertUpdate(DocumentEvent e) {
				filterKnjige();
			}

			private void filterKnjige() {
				String nazivKnjige = textFieldPretragaPoNazivuKnjige.getText().trim().toLowerCase();
				if (!nazivKnjige.isEmpty()) {
					prikaziKnjige(nazivKnjige);
				} else {
					prikaziKnjige(null);
				}
			}
		});

		prikaziKnjige(null);
	}

	private void prikaziKnjige(String nazivKnjige) {
		try (Connection con = Konekcija.connect()) {
			String query = "SELECT k.id_knjige AS 'ID Knjige', k.naziv_knjige AS 'Naziv knjige', k.godina_izdanja AS 'Godina izdanja', "
					+ "k.broj_dostupnih_knjiga AS 'Broj dostupnih knjiga', k.ukupan_broj_knjiga AS 'Ukupan broj knjiga', "
					+ "CONCAT(a.ime_autora, ' ', a.prezime_autora) AS 'Ime i prezime', "
					+ "z.naziv_zanra AS 'Naziv žanra', i.naziv_izdavaca AS 'Naziv izdavača' " + "FROM knjige k "
					+ "JOIN autori a ON k.id_autora = a.id_autora " + "JOIN zanr z ON k.id_zanra = z.id_zanra "
					+ "JOIN izdavaci i ON k.id_izdavaca = i.id_izdavaca ";
			if (nazivKnjige != null && !nazivKnjige.isEmpty()) {
				query += "WHERE LOWER(k.naziv_knjige) LIKE ? ";
			}
			query += "ORDER BY k.id_knjige";

			PreparedStatement pst = con.prepareStatement(query);
			if (nazivKnjige != null && !nazivKnjige.isEmpty()) {
				pst.setString(1, "%" + nazivKnjige + "%");
			}
			ResultSet rs = pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Greška pri prikazu knjiga.", "Greška", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	private boolean IzdataKnjigaPoId(int idKnjige) {
		boolean isIssued = false;
		try (Connection con = Konekcija.connect()) {
			String query = "SELECT COUNT(*) FROM izdavanje WHERE id_knjige = ?";
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, idKnjige);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				isIssued = rs.getInt(1) > 0;
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Greška pri provjeri izdatih knjiga.", "Greška",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
		return isIssued;
	}

}
