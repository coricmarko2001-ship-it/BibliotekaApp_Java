package Biblioteka;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.toedter.calendar.JDateChooser;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class DodajKnjigu extends JFrame implements RefreshAutora, RefreshZanr, RefreshIzdavac {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldNaziv;
	private JTextField textFieldBrDostupnih;
	private JTextField textFieldUkupanBr;
	private JComboBox<String> comboBoxAutor;
	private ArrayList<Integer> listaAutoraIds;
	private JComboBox<String> comboBoxZanr;
	private ArrayList<Integer> listaZanrovaIds;
	private JComboBox<String> comboBoxIzdavac;
	private ArrayList<Integer> listaIzdavacaIds;

	public DodajKnjigu() {
		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 525, 587);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		setLocationRelativeTo(null);

		JButton btnDodajKnjiguPotvrdi = new JButton("Potvrdi");
		btnDodajKnjiguPotvrdi.setBounds(130, 481, 100, 30);
		contentPane.add(btnDodajKnjiguPotvrdi);

		JButton btnDodajKnjiguOdustani = new JButton("Odustani");
		btnDodajKnjiguOdustani.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				PregledKnjiga knjiga = new PregledKnjiga();
				knjiga.setVisible(true);
			}
		});
		btnDodajKnjiguOdustani.setBounds(280, 481, 100, 30);
		contentPane.add(btnDodajKnjiguOdustani);

		JLabel lblDodajKnjiguNaziv = new JLabel("Naziv knjige");
		lblDodajKnjiguNaziv.setBounds(56, 107, 93, 14);
		contentPane.add(lblDodajKnjiguNaziv);

		JLabel lblDodajKnjiguGodina = new JLabel("Godina izdanja");
		lblDodajKnjiguGodina.setBounds(56, 160, 93, 14);
		contentPane.add(lblDodajKnjiguGodina);

		JLabel lblDodajKnjiguBrDostupnih = new JLabel("Broj dostupnih knjiga");
		lblDodajKnjiguBrDostupnih.setBounds(56, 213, 150, 14);
		contentPane.add(lblDodajKnjiguBrDostupnih);

		JLabel lblDodajKnjiguUkupanBr = new JLabel("Ukupan broj knjiga");
		lblDodajKnjiguUkupanBr.setBounds(56, 266, 150, 14);
		contentPane.add(lblDodajKnjiguUkupanBr);

		JLabel lblDodajKnjiguAutor = new JLabel("Autor");
		lblDodajKnjiguAutor.setBounds(56, 319, 93, 14);
		contentPane.add(lblDodajKnjiguAutor);

		JLabel lblDodajKnjiguZanr = new JLabel("Žanr");
		lblDodajKnjiguZanr.setBounds(56, 372, 93, 14);
		contentPane.add(lblDodajKnjiguZanr);

		JLabel lblDodajKnjiguIzdavac = new JLabel("Izdavač");
		lblDodajKnjiguIzdavac.setBounds(56, 425, 93, 14);
		contentPane.add(lblDodajKnjiguIzdavac);

		textFieldNaziv = new JTextField();
		textFieldNaziv.setBounds(215, 104, 150, 20);
		contentPane.add(textFieldNaziv);
		textFieldNaziv.setColumns(10);

		JDateChooser dateChooser = new JDateChooser();
		dateChooser.setBounds(215, 154, 150, 20);
		contentPane.add(dateChooser);

		textFieldBrDostupnih = new JTextField();
		textFieldBrDostupnih.setBounds(215, 210, 150, 20);
		contentPane.add(textFieldBrDostupnih);
		textFieldBrDostupnih.setColumns(10);

		textFieldUkupanBr = new JTextField();
		textFieldUkupanBr.setBounds(215, 263, 150, 20);
		contentPane.add(textFieldUkupanBr);
		textFieldUkupanBr.setColumns(10);

		comboBoxAutor = new JComboBox<String>();
		comboBoxAutor.setBounds(215, 316, 150, 22);
		contentPane.add(comboBoxAutor);

		comboBoxZanr = new JComboBox<String>();
		comboBoxZanr.setBounds(215, 369, 150, 22);
		contentPane.add(comboBoxZanr);

		comboBoxIzdavac = new JComboBox<String>();
		comboBoxIzdavac.setBounds(215, 422, 150, 22);
		contentPane.add(comboBoxIzdavac);

		popuniComboBoxAutora();
		popuniComboBoxZanra();
		popuniComboBoxIzdavaca();

		setVisible(true);

		JButton btnDodajKnjiguDodajAutora = new JButton("Dodaj Autora");
		btnDodajKnjiguDodajAutora.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DodajAutora autor = new DodajAutora("knjiga", DodajKnjigu.this);
				autor.setVisible(true);
			}
		});
		btnDodajKnjiguDodajAutora.setBounds(373, 316, 122, 23);
		contentPane.add(btnDodajKnjiguDodajAutora);

		JButton btnDodajKnjiguDodajIzdavaca = new JButton("Dodaj Izdavača");
		btnDodajKnjiguDodajIzdavaca.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DodajIzdavaca izdavac = new DodajIzdavaca("knjiga", DodajKnjigu.this);
				izdavac.izKnjige = true;
				izdavac.setVisible(true);
			}
		});
		btnDodajKnjiguDodajIzdavaca.setBounds(373, 422, 122, 23);
		contentPane.add(btnDodajKnjiguDodajIzdavaca);

		JButton btnDodajKnjiguDodajZanr = new JButton("Dodaj Žanr");
		btnDodajKnjiguDodajZanr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DodajZanr zanr = new DodajZanr("zanr", DodajKnjigu.this);
				zanr.setVisible(true);
			}
		});
		btnDodajKnjiguDodajZanr.setBounds(375, 368, 120, 23);
		contentPane.add(btnDodajKnjiguDodajZanr);

		btnDodajKnjiguPotvrdi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String naziv = textFieldNaziv.getText();
				java.util.Date godina = dateChooser.getDate();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String brdostupnih = textFieldBrDostupnih.getText();
				String ukupanbr = textFieldUkupanBr.getText();
				int selectedAutorId = listaAutoraIds.get(comboBoxAutor.getSelectedIndex());
				int selectedZanrId = listaZanrovaIds.get(comboBoxZanr.getSelectedIndex());
				int selectedIzdavacId = listaIzdavacaIds.get(comboBoxIzdavac.getSelectedIndex());

				if (naziv.isEmpty() || brdostupnih.isEmpty() || ukupanbr.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Sva polja su obavezna.", "Greška", JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						int brDostupnihInt = Integer.parseInt(brdostupnih);
						int ukupanBrInt = Integer.parseInt(ukupanbr);

						if (brDostupnihInt > ukupanBrInt) {
							JOptionPane.showMessageDialog(null,
									"Broj dostupnih knjiga ne može biti veći od ukupnog broja knjiga.", "Greška",
									JOptionPane.ERROR_MESSAGE);
							return;
						}

						try (Connection con = Konekcija.connect()) {
							String query = "INSERT INTO knjige (naziv_knjige, godina_izdanja, broj_dostupnih_knjiga, ukupan_broj_knjiga, id_autora, id_zanra, id_izdavaca) VALUES (?, ?, ?, ?, ?, ?, ?)";
							PreparedStatement pst = con.prepareStatement(query);
							pst.setString(1, naziv);
							pst.setString(2, sdf.format(godina));
							pst.setInt(3, brDostupnihInt);
							pst.setInt(4, ukupanBrInt);
							pst.setInt(5, selectedAutorId);
							pst.setInt(6, selectedZanrId);
							pst.setInt(7, selectedIzdavacId);

							pst.executeUpdate();
							JOptionPane.showMessageDialog(null, "Knjiga je uspješno dodana.", "Obavještenje",
									JOptionPane.INFORMATION_MESSAGE);

							dispose();
							PregledKnjiga knjiga = new PregledKnjiga();
							knjiga.setVisible(true);
						} catch (Exception ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(null, "Došlo je do greške prilikom dodavanja knjige.",
									"Greška", JOptionPane.ERROR_MESSAGE);
						}
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(null,
								"Broj dostupnih knjiga i ukupan broj knjiga moraju biti validni brojevi.", "Greška",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
	}

	@Override
	public void refreshComboBoxAutora() {
		popuniComboBoxAutora();
	}

	@Override
	public void refreshComboBoxZanra() {
		popuniComboBoxZanra();
	}

	@Override
	public void refreshComboBoxIzdavaca() {
		popuniComboBoxIzdavaca();
	}

	public void popuniComboBoxAutora() {
		comboBoxAutor.removeAllItems();
		listaAutoraIds = new ArrayList<>();
		try (Connection con = Konekcija.connect()) {
			String query = "SELECT id_autora, ime_autora, prezime_autora FROM autori";
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				comboBoxAutor.addItem(rs.getString("ime_autora") + " " + rs.getString("prezime_autora"));
				listaAutoraIds.add(rs.getInt("id_autora"));
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Greška pri dohvatu autora.", "Greška", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	public void popuniComboBoxZanra() {
		comboBoxZanr.removeAllItems();
		listaZanrovaIds = new ArrayList<>();
		try (Connection con = Konekcija.connect()) {
			String query = "SELECT id_zanra, naziv_zanra FROM zanr";
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				comboBoxZanr.addItem(rs.getString("naziv_zanra"));
				listaZanrovaIds.add(rs.getInt("id_zanra"));
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Greška pri dohvatu žanra.", "Greška", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	public void popuniComboBoxIzdavaca() {
		comboBoxIzdavac.removeAllItems();
		listaIzdavacaIds = new ArrayList<>();
		try (Connection con = Konekcija.connect()) {
			String query = "SELECT id_izdavaca, naziv_izdavaca FROM izdavaci";
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				comboBoxIzdavac.addItem(rs.getString("naziv_izdavaca"));
				listaIzdavacaIds.add(rs.getInt("id_izdavaca"));
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Greška pri dohvatu izdavača.", "Greška", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}
}
