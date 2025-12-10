package Biblioteka;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

public class DodajIzdavaca extends JFrame implements Refresh {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldNaziv;
	private JTextField textFieldTelefon;
	private JComboBox<String> comboBoxMjesto;
	private ArrayList<Integer> listaMjestaIds;
	private String ime_prozora;
	private RefreshIzdavac callback;
	public boolean izKnjige = false;

	/**
	 * Create the frame.
	 */
	public DodajIzdavaca(String ime_prozora, RefreshIzdavac callback) {
		this.ime_prozora = ime_prozora;
		this.callback = callback;

		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 476, 458);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		setLocationRelativeTo(null);
		setVisible(true);

		JLabel lblDodajIzdavacaNaziv = new JLabel("Naziv izdavača");
		lblDodajIzdavacaNaziv.setBounds(56, 107, 93, 14);
		contentPane.add(lblDodajIzdavacaNaziv);

		JLabel lblDodajIzdavacaTelefon = new JLabel("Kontakt telefon");
		lblDodajIzdavacaTelefon.setBounds(56, 163, 93, 14);
		contentPane.add(lblDodajIzdavacaTelefon);

		JLabel lblDodajIzdavacaMjesto = new JLabel("Mjesto");
		lblDodajIzdavacaMjesto.setBounds(56, 218, 93, 14);
		contentPane.add(lblDodajIzdavacaMjesto);

		textFieldNaziv = new JTextField();
		textFieldNaziv.setBounds(186, 104, 142, 20);
		contentPane.add(textFieldNaziv);
		textFieldNaziv.setColumns(10);

		textFieldTelefon = new JTextField();
		textFieldTelefon.setBounds(186, 160, 142, 20);
		contentPane.add(textFieldTelefon);
		textFieldTelefon.setColumns(10);

		comboBoxMjesto = new JComboBox<String>();
		comboBoxMjesto.setBounds(186, 215, 140, 20);
		contentPane.add(comboBoxMjesto);

		popuniComboBoxMjesta();

		JButton btnDodajIzdavacaPotvrdi = new JButton("Potvrdi");
		btnDodajIzdavacaPotvrdi.setBounds(85, 349, 100, 30);
		contentPane.add(btnDodajIzdavacaPotvrdi);

		JButton btnDodajIzdavacaOdustani = new JButton("Odustani");
		btnDodajIzdavacaOdustani.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				if (DodajIzdavaca.this.ime_prozora.equals("izdavac")) {
					PregledIzdavaca izdavac = new PregledIzdavaca();
					izdavac.setVisible(true);
				} else if (DodajIzdavaca.this.ime_prozora.equals("knjiga")) {
					DodajKnjigu knjiga = new DodajKnjigu();
					knjiga.setVisible(true);
				}

			}
		});
		btnDodajIzdavacaOdustani.setBounds(228, 349, 100, 30);
		contentPane.add(btnDodajIzdavacaOdustani);

		JButton btnNewButtonDodajMjesto = new JButton("Dodaj Mjesto");
		btnNewButtonDodajMjesto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DodajMjesto mjesto = new DodajMjesto("izdavac", DodajIzdavaca.this);
				mjesto.setVisible(true);
			}
		});
		btnNewButtonDodajMjesto.setBounds(337, 214, 113, 23);
		contentPane.add(btnNewButtonDodajMjesto);

		btnDodajIzdavacaPotvrdi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String naziv = textFieldNaziv.getText();
				String telefon = textFieldTelefon.getText();
				int selectedMjestoId = listaMjestaIds.get(comboBoxMjesto.getSelectedIndex());

				if (naziv.isEmpty() || telefon.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Naziv izdavača i kontakt telefon su obavezni.", "Greška",
							JOptionPane.ERROR_MESSAGE);
				} else {
					try (Connection con = Konekcija.connect()) {
						String query = "INSERT INTO izdavaci (naziv_izdavaca, kontakt_telefon, id_mjesta) VALUES (?, ?, ?)";
						PreparedStatement pst = con.prepareStatement(query);
						pst.setString(1, naziv);
						pst.setString(2, telefon);
						pst.setInt(3, selectedMjestoId);

						pst.executeUpdate();
						JOptionPane.showMessageDialog(null, "Izdavač uspješno dodan.", "Uspjeh",
								JOptionPane.INFORMATION_MESSAGE);

						if (callback != null) {
							callback.refreshComboBoxIzdavaca();
						}

						dispose();
						if (izKnjige != true) {
							PregledIzdavaca izdavac = new PregledIzdavaca();
							izdavac.setVisible(true);
						} else {

							callback.refreshComboBoxIzdavaca();
						}

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Greška pri dodavanju izdavača.", "Greška",
								JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
				}
			}
		});
	}

	private void popuniComboBoxMjesta() {
		listaMjestaIds = new ArrayList<>();
		comboBoxMjesto.removeAllItems();

		try (Connection con = Konekcija.connect()) {
			String query = "SELECT id_mjesta, naziv_mjesta FROM mjesto";
			PreparedStatement pst = con.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id_mjesta");
				String naziv = rs.getString("naziv_mjesta");
				comboBoxMjesto.addItem(naziv);
				listaMjestaIds.add(id);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Greška pri dohvatu mjesta.", "Greška", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	@Override
	public void refreshComboBox() {
		popuniComboBoxMjesta();
	}
}
