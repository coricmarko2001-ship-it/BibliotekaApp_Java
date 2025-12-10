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
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PregledBibliotekara extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private int selectedRowId = -1;
	private JTextField textFieldPregledBibliotekaraPoImenu;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PregledBibliotekara frame = new PregledBibliotekara();
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
	public PregledBibliotekara() {
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
		scrollPane.setBounds(10, 69, 1046, 298);
		contentPane.add(scrollPane);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				if (row >= 0) {

					selectedRowId = (int) table.getValueAt(row, 0);
				}
			}
		});
		scrollPane.setViewportView(table);

		JButton btnPregledBibliotekaraIzmijeni = new JButton("Izmijeni");
		btnPregledBibliotekaraIzmijeni.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedRowId == -1) {
					JOptionPane.showMessageDialog(null, "Nema selektovanog bibliotekara za izmene.", "Greška",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				try (Connection con = Konekcija.connect()) {
					String query = "SELECT ime, prezime, korisnicko_ime, lozinka FROM bibliotekar WHERE id_bibliotekara = ?";
					PreparedStatement pst = con.prepareStatement(query);
					pst.setInt(1, selectedRowId);
					ResultSet rs = pst.executeQuery();

					if (rs.next()) {
						String ime = rs.getString("ime");
						String prezime = rs.getString("prezime");
						String korisnickoIme = rs.getString("korisnicko_ime");
						String lozinka = rs.getString("lozinka");

						dispose();
						IzmijeniBibliotekara izmijeni = new IzmijeniBibliotekara(selectedRowId, ime, prezime,
								korisnickoIme, lozinka);
						izmijeni.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(null, "Nema podataka za selektovanog bibliotekara.", "Greška",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Greška pri preuzimanju podataka.", "Greška",
							JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		});
		btnPregledBibliotekaraIzmijeni.setBounds(406, 378, 87, 23);
		contentPane.add(btnPregledBibliotekaraIzmijeni);

		JButton btnPregledBibliotekaraObrisi = new JButton("Obriši");
		btnPregledBibliotekaraObrisi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedRowId == -1) {
					JOptionPane.showMessageDialog(null, "Nema selektovanog bibliotekara za brisanje.", "Greška",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				int confirm = JOptionPane.showConfirmDialog(null,
						"Da li ste sigurni da želite da obrišete ovog bibliotekara?", "Potvrda brisanja",
						JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					try (Connection con = Konekcija.connect()) {
						String query = "DELETE FROM bibliotekar WHERE id_bibliotekara = ?";
						PreparedStatement pst = con.prepareStatement(query);
						pst.setInt(1, selectedRowId);
						pst.executeUpdate();
						JOptionPane.showMessageDialog(null, "Bibliotekar uspešno obrisan.", "",
								JOptionPane.INFORMATION_MESSAGE);
						prikaziBibliotekare(null);
						selectedRowId = -1;
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Greška pri brisanju bibliotekara.", "Greška",
								JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
				}
			}
		});
		btnPregledBibliotekaraObrisi.setBounds(553, 378, 87, 23);
		contentPane.add(btnPregledBibliotekaraObrisi);

		JButton btnPregledBibliotekaraIzadji = new JButton("Izađi");
		btnPregledBibliotekaraIzadji.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Pocetna pocetna = new Pocetna();
				pocetna.setVisible(true);
			}
		});
		btnPregledBibliotekaraIzadji.setBounds(933, 378, 87, 23);
		contentPane.add(btnPregledBibliotekaraIzadji);

		textFieldPregledBibliotekaraPoImenu = new JTextField();
		textFieldPregledBibliotekaraPoImenu.setBounds(515, 26, 178, 20);
		contentPane.add(textFieldPregledBibliotekaraPoImenu);
		textFieldPregledBibliotekaraPoImenu.setColumns(10);

		JLabel lblNewLabel = new JLabel("Pregled Bibliotekara");
		lblNewLabel.setBounds(381, 29, 119, 14);
		contentPane.add(lblNewLabel);

		textFieldPregledBibliotekaraPoImenu.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				filterBibliotekari();
			}

			public void removeUpdate(DocumentEvent e) {
				filterBibliotekari();
			}

			public void insertUpdate(DocumentEvent e) {
				filterBibliotekari();
			}

			private void filterBibliotekari() {
				String searchText = textFieldPregledBibliotekaraPoImenu.getText().toLowerCase();
				prikaziBibliotekare(searchText);
			}
		});

		prikaziBibliotekare(null);
	}

	private void prikaziBibliotekare(String searchText) {
		try (Connection con = Konekcija.connect()) {
			String query = "SELECT id_bibliotekara AS 'ID Bibliotekara', ime AS Ime, prezime AS Prezime, korisnicko_ime AS 'Korisničko ime', lozinka AS Lozinka FROM bibliotekar";
			if (searchText != null && !searchText.isEmpty()) {
				query += " WHERE LOWER(ime) LIKE ?";
			}
			PreparedStatement pst = con.prepareStatement(query);
			if (searchText != null && !searchText.isEmpty()) {
				pst.setString(1, "%" + searchText + "%");
			}
			ResultSet rs = pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Greška pri prikazu bibliotekara.", "Greška",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}
}
