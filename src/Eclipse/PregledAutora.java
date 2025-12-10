package Biblioteka;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PregledAutora extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private int selectedAuthorId = -1;
	private JTextField textFieldPretraziPoImenuAutora;

	public PregledAutora() {
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
		scrollPane.setBounds(10, 69, 1046, 282);
		contentPane.add(scrollPane);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				if (row >= 0) {
					selectedAuthorId = (int) table.getValueAt(row, 0);
				}
			}
		});
		scrollPane.setViewportView(table);

		JButton btnPregledAutoraIzmijeni = new JButton("Izmijeni");
		btnPregledAutoraIzmijeni.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedAuthorId == -1) {
					JOptionPane.showMessageDialog(null, "Nema selektovanog autora za izmene.", "Greška",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				dispose();
				IzmijeniAutora izmijeniAutoraFrame = new IzmijeniAutora(selectedAuthorId);
				izmijeniAutoraFrame.setVisible(true);
			}
		});
		btnPregledAutoraIzmijeni.setBounds(259, 378, 87, 23);
		contentPane.add(btnPregledAutoraIzmijeni);

		JButton btnPregledAutoraDodaj = new JButton("Dodaj");
		btnPregledAutoraDodaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				DodajAutora autor = new DodajAutora("autor", null);
				autor.setVisible(true);
			}
		});
		btnPregledAutoraDodaj.setBounds(450, 378, 87, 23);
		contentPane.add(btnPregledAutoraDodaj);

		JButton btnPregledAutoraObrisi = new JButton("Obriši");
		btnPregledAutoraObrisi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedAuthorId == -1) {
					JOptionPane.showMessageDialog(null, "Nema selektovanog autora za brisanje.", "Greška",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				int confirm = JOptionPane.showConfirmDialog(null,
						"Da li ste sigurni da želite da obrišete ovog autora?", "Potvrda brisanja",
						JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					try (Connection con = Konekcija.connect()) {
						String query = "DELETE FROM autori WHERE id_autora = ?";
						PreparedStatement pst = con.prepareStatement(query);
						pst.setInt(1, selectedAuthorId);
						pst.executeUpdate();
						JOptionPane.showMessageDialog(null, "Autor uspješno obrisan.", "",
								JOptionPane.INFORMATION_MESSAGE);
						prikaziAutore(null);
						selectedAuthorId = -1;
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Greška pri brisanju autora.", "Greška",
								JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
				}
			}
		});
		btnPregledAutoraObrisi.setBounds(641, 378, 87, 23);
		contentPane.add(btnPregledAutoraObrisi);

		JButton btnPregledAutoraIzadji = new JButton("Izađi");
		btnPregledAutoraIzadji.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Pocetna pocetna = new Pocetna();
				pocetna.setVisible(true);
			}
		});
		btnPregledAutoraIzadji.setBounds(933, 378, 87, 23);
		contentPane.add(btnPregledAutoraIzadji);

		JButton btnOsvjezi = new JButton("Osvježi");
		btnOsvjezi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				prikaziAutore(null);
			}
		});
		btnOsvjezi.setBounds(44, 378, 87, 23);
		contentPane.add(btnOsvjezi);

		textFieldPretraziPoImenuAutora = new JTextField();
		textFieldPretraziPoImenuAutora.setBounds(460, 28, 196, 20);
		contentPane.add(textFieldPretraziPoImenuAutora);
		textFieldPretraziPoImenuAutora.setColumns(10);

		JLabel lblNewLabel = new JLabel("Ime Autora");
		lblNewLabel.setBounds(363, 31, 87, 14);
		contentPane.add(lblNewLabel);

		textFieldPretraziPoImenuAutora.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				filterAutori();
			}

			public void removeUpdate(DocumentEvent e) {
				filterAutori();
			}

			public void insertUpdate(DocumentEvent e) {
				filterAutori();
			}

			private void filterAutori() {
				String searchText = textFieldPretraziPoImenuAutora.getText().toLowerCase();
				prikaziAutore(searchText);
			}
		});

		prikaziAutore(null);
	}

	private void prikaziAutore(String searchText) {
		try (Connection con = Konekcija.connect()) {
			String query = "SELECT id_autora AS 'ID Autora', ime_autora AS 'Ime autora', prezime_autora AS 'Prezime autora', DATE_FORMAT(godina_rodjenja, '%d.%m.%Y') AS 'Godina rođenja' FROM autori";
			if (searchText != null && !searchText.isEmpty()) {
				query += " WHERE LOWER(ime_autora) LIKE ?";
			}
			query += " ORDER BY id_autora";
			PreparedStatement pst = con.prepareStatement(query);
			if (searchText != null && !searchText.isEmpty()) {
				pst.setString(1, "%" + searchText + "%");
			}
			ResultSet rs = pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Greška pri prikazu autora.", "Greška", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}
}
