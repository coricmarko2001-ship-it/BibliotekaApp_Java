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

public class PregledIzdavaca extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private int selectedPublisherId = -1;
	private JTextField textField;

	public PregledIzdavaca() {
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
		scrollPane.setBounds(10, 64, 1046, 287);
		contentPane.add(scrollPane);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				if (row >= 0) {
					selectedPublisherId = (int) table.getValueAt(row, 0);
				}
			}
		});
		scrollPane.setViewportView(table);

		JButton btnPregledIzdavacaIzmijeni = new JButton("Izmijeni");
		btnPregledIzdavacaIzmijeni.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedPublisherId == -1) {
					JOptionPane.showMessageDialog(null, "Nema selektovanog izdavača za izmjenu.", "Greška",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				dispose();
				IzmijeniIzdavaca izmijeniIzdavaca = new IzmijeniIzdavaca(selectedPublisherId);
				izmijeniIzdavaca.setVisible(true);
			}
		});
		btnPregledIzdavacaIzmijeni.setBounds(259, 378, 87, 23);
		contentPane.add(btnPregledIzdavacaIzmijeni);

		JButton btnPregledIzdavacaDodaj = new JButton("Dodaj");
		btnPregledIzdavacaDodaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				DodajIzdavaca izdavac = new DodajIzdavaca("izdavac", null);
				izdavac.setVisible(true);
			}
		});
		btnPregledIzdavacaDodaj.setBounds(450, 378, 87, 23);
		contentPane.add(btnPregledIzdavacaDodaj);

		JButton btnPregledIzdavacaObrisi = new JButton("Obriši");
		btnPregledIzdavacaObrisi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedPublisherId == -1) {
					JOptionPane.showMessageDialog(null, "Nema selektovanog izdavača za brisanje.", "Greška",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				int confirm = JOptionPane.showConfirmDialog(null,
						"Da li ste sigurni da želite da obrišete ovog izdavača?", "Potvrda brisanja",
						JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					try (Connection con = Konekcija.connect()) {
						String query = "DELETE FROM izdavaci WHERE id_izdavaca = ?";
						PreparedStatement pst = con.prepareStatement(query);
						pst.setInt(1, selectedPublisherId);
						pst.executeUpdate();
						JOptionPane.showMessageDialog(null, "Izdavač uspješno obrisan.", "",
								JOptionPane.INFORMATION_MESSAGE);
						prikaziIzdavace(null);
						selectedPublisherId = -1;
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Greška pri brisanju izdavača.", "Greška",
								JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
				}
			}
		});
		btnPregledIzdavacaObrisi.setBounds(641, 378, 87, 23);
		contentPane.add(btnPregledIzdavacaObrisi);

		JButton btnPregledIzdavacaIzadji = new JButton("Izađi");
		btnPregledIzdavacaIzadji.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Pocetna pocetna = new Pocetna();
				pocetna.setVisible(true);
			}
		});
		btnPregledIzdavacaIzadji.setBounds(933, 378, 87, 23);
		contentPane.add(btnPregledIzdavacaIzadji);

		JLabel lblNewLabel = new JLabel("Ime Izdavača");
		lblNewLabel.setBounds(374, 21, 78, 14);
		contentPane.add(lblNewLabel);

		textField = new JTextField();
		textField.setBounds(475, 18, 162, 20);
		contentPane.add(textField);
		textField.setColumns(10);

		textField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				filterIzdavace();
			}

			public void removeUpdate(DocumentEvent e) {
				filterIzdavace();
			}

			public void insertUpdate(DocumentEvent e) {
				filterIzdavace();
			}

			private void filterIzdavace() {
				String nazivIzdavaca = textField.getText().trim().toLowerCase();
				if (!nazivIzdavaca.isEmpty()) {
					prikaziIzdavace(nazivIzdavaca);
				} else {
					prikaziIzdavace(null);
				}
			}
		});

		prikaziIzdavace(null);
	}

	private void prikaziIzdavace(String nazivIzdavaca) {
		try (Connection con = Konekcija.connect()) {
			String query = "SELECT izdavaci.id_izdavaca AS 'ID Izdavača', izdavaci.naziv_izdavaca AS 'Naziv izdavača', "
					+ "izdavaci.kontakt_telefon AS 'Kontakt telefon', mjesto.naziv_mjesta AS Mjesto " + "FROM izdavaci "
					+ "JOIN mjesto ON izdavaci.id_mjesta = mjesto.id_mjesta ";
			if (nazivIzdavaca != null && !nazivIzdavaca.isEmpty()) {
				query += "WHERE LOWER(izdavaci.naziv_izdavaca) LIKE ? ";
			}
			query += "ORDER BY izdavaci.id_izdavaca";

			PreparedStatement pst = con.prepareStatement(query);
			if (nazivIzdavaca != null && !nazivIzdavaca.isEmpty()) {
				pst.setString(1, "%" + nazivIzdavaca + "%");
			}
			ResultSet rs = pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Greška pri prikazu izdavača.", "Greška", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}
}
