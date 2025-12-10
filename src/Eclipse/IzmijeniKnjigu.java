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

public class IzmijeniKnjigu extends JFrame {

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
	private JDateChooser dateChooser;
	private int knjigaId;

	public IzmijeniKnjigu(int knjigaId) {
		this.knjigaId = knjigaId;

		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 525, 587);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		setLocationRelativeTo(null);
		setVisible(true);

		JButton btnIzmijeniKnjiguPotvrdi = new JButton("Potvrdi");
		btnIzmijeniKnjiguPotvrdi.setBounds(130, 481, 100, 30);
		contentPane.add(btnIzmijeniKnjiguPotvrdi);

		JButton btnIzmijeniKnjiguOdustani = new JButton("Odustani");
		btnIzmijeniKnjiguOdustani.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				PregledKnjiga knjiga = new PregledKnjiga();
				knjiga.setVisible(true);
			}
		});
		btnIzmijeniKnjiguOdustani.setBounds(280, 481, 100, 30);
		contentPane.add(btnIzmijeniKnjiguOdustani);

		JLabel lblIzmijeniKnjiguNaziv = new JLabel("Naziv knjige");
		lblIzmijeniKnjiguNaziv.setBounds(56, 107, 93, 14);
		contentPane.add(lblIzmijeniKnjiguNaziv);

		JLabel lblIzmijeniKnjiguGodina = new JLabel("Godina izdanja");
		lblIzmijeniKnjiguGodina.setBounds(56, 160, 93, 14);
		contentPane.add(lblIzmijeniKnjiguGodina);

		JLabel lblIzmijeniKnjiguBrDostupnih = new JLabel("Broj dostupnih knjiga");
		lblIzmijeniKnjiguBrDostupnih.setBounds(56, 213, 150, 14);
		contentPane.add(lblIzmijeniKnjiguBrDostupnih);

		JLabel lblIzmijeniKnjiguUkupanBr = new JLabel("Ukupan broj knjiga");
		lblIzmijeniKnjiguUkupanBr.setBounds(56, 266, 150, 14);
		contentPane.add(lblIzmijeniKnjiguUkupanBr);

		JLabel lblIzmijeniKnjiguAutor = new JLabel("Autor");
		lblIzmijeniKnjiguAutor.setBounds(56, 319, 93, 14);
		contentPane.add(lblIzmijeniKnjiguAutor);

		JLabel lblIzmijeniKnjiguZanr = new JLabel("Žanr");
		lblIzmijeniKnjiguZanr.setBounds(56, 372, 93, 14);
		contentPane.add(lblIzmijeniKnjiguZanr);

		JLabel lblIzmijeniKnjiguIzdavac = new JLabel("Izdavač");
		lblIzmijeniKnjiguIzdavac.setBounds(56, 425, 93, 14);
		contentPane.add(lblIzmijeniKnjiguIzdavac);

		textFieldNaziv = new JTextField();
		textFieldNaziv.setBounds(215, 104, 150, 20);
		contentPane.add(textFieldNaziv);
		textFieldNaziv.setColumns(10);

		dateChooser = new JDateChooser();
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
		popuniPodatkeKnjige();

		btnIzmijeniKnjiguPotvrdi.addActionListener(new ActionListener() {
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
					try (Connection con = Konekcija.connect()) {
						String query = "UPDATE knjige SET naziv_knjige = ?, godina_izdanja = ?, broj_dostupnih_knjiga = ?, ukupan_broj_knjiga = ?, id_autora = ?, id_zanra = ?, id_izdavaca = ? WHERE id_knjige = ?";
						PreparedStatement pst = con.prepareStatement(query);
						pst.setString(1, naziv);
						if (godina != null) {
							String godinaStr = sdf.format(godina);
							pst.setString(2, godinaStr);
						} else {
							pst.setNull(2, java.sql.Types.DATE);
						}
						pst.setString(3, brdostupnih);
						pst.setString(4, ukupanbr);
						pst.setInt(5, selectedAutorId);
						pst.setInt(6, selectedZanrId);
						pst.setInt(7, selectedIzdavacId);
						pst.setInt(8, knjigaId);

						pst.executeUpdate();
						JOptionPane.showMessageDialog(null, "Knjiga je uspješno izmijenjena.", "Uspjeh",
								JOptionPane.INFORMATION_MESSAGE);

						dispose();
						PregledKnjiga pregledKnjiga = new PregledKnjiga();
						pregledKnjiga.setVisible(true);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Greška pri izmjeni knjige.", "Greška",
								JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
				}
			}
		});
	}

	public void popuniComboBoxAutora() {
		listaAutoraIds = new ArrayList<>();
		comboBoxAutor.removeAllItems();

		try (Connection con = Konekcija.connect()) {
			String query = "SELECT id_autora, CONCAT(ime_autora, ' ', prezime_autora) AS autor FROM autori";
			PreparedStatement pst = con.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				listaAutoraIds.add(rs.getInt("id_autora"));
				comboBoxAutor.addItem(rs.getString("autor"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void popuniComboBoxZanra() {
		listaZanrovaIds = new ArrayList<>();
		comboBoxZanr.removeAllItems();

		try (Connection con = Konekcija.connect()) {
			String query = "SELECT id_zanra, naziv_zanra FROM zanr";
			PreparedStatement pst = con.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				listaZanrovaIds.add(rs.getInt("id_zanra"));
				comboBoxZanr.addItem(rs.getString("naziv_zanra"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void popuniComboBoxIzdavaca() {
		listaIzdavacaIds = new ArrayList<>();
		comboBoxIzdavac.removeAllItems();

		try (Connection con = Konekcija.connect()) {
			String query = "SELECT id_izdavaca, naziv_izdavaca FROM izdavaci";
			PreparedStatement pst = con.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				listaIzdavacaIds.add(rs.getInt("id_izdavaca"));
				comboBoxIzdavac.addItem(rs.getString("naziv_izdavaca"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void popuniPodatkeKnjige() {
		try (Connection con = Konekcija.connect()) {
			String query = "SELECT * FROM knjige WHERE id_knjige = ?";
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, knjigaId);
			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				textFieldNaziv.setText(rs.getString("naziv_knjige"));
				dateChooser.setDate(rs.getDate("godina_izdanja"));
				textFieldBrDostupnih.setText(rs.getString("broj_dostupnih_knjiga"));
				textFieldUkupanBr.setText(rs.getString("ukupan_broj_knjiga"));

				int idAutora = rs.getInt("id_autora");
				for (int i = 0; i < listaAutoraIds.size(); i++) {
					if (listaAutoraIds.get(i) == idAutora) {
						comboBoxAutor.setSelectedIndex(i);
						break;
					}
				}

				int idZanra = rs.getInt("id_zanra");
				for (int i = 0; i < listaZanrovaIds.size(); i++) {
					if (listaZanrovaIds.get(i) == idZanra) {
						comboBoxZanr.setSelectedIndex(i);
						break;
					}
				}

				int idIzdavaca = rs.getInt("id_izdavaca");
				for (int i = 0; i < listaIzdavacaIds.size(); i++) {
					if (listaIzdavacaIds.get(i) == idIzdavaca) {
						comboBoxIzdavac.setSelectedIndex(i);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
