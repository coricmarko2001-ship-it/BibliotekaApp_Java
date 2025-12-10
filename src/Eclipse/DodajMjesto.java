package Biblioteka;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class DodajMjesto extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldNazivMjesta;
	private JTextField textFieldPostanskiBroj;
	private String ime_prozora;
	private Refresh callback;

	/**
	 * Create the frame.
	 */
	public DodajMjesto(String ime_prozora, Refresh callback) {
		this.ime_prozora = ime_prozora;
		this.callback = callback;

		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 412, 354);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		setLocationRelativeTo(null);
		setVisible(true);

		JButton btnDodajMjestoPotvrdi = new JButton("Potvrdi");
		btnDodajMjestoPotvrdi.setBounds(66, 238, 100, 30);
		contentPane.add(btnDodajMjestoPotvrdi);

		JButton btnDodajMjestoOdustani = new JButton("Odustani");
		btnDodajMjestoOdustani.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				if (DodajMjesto.this.ime_prozora.equals("korisnik")) {
					DodajKorisnika korisnik = new DodajKorisnika();
					korisnik.setVisible(true);
				} else if (DodajMjesto.this.ime_prozora.equals("izdavac")) {
					DodajIzdavaca izdavac = new DodajIzdavaca("mjesto", null);
					izdavac.setVisible(true);
				}
			}
		});
		btnDodajMjestoOdustani.setBounds(201, 238, 100, 30);
		contentPane.add(btnDodajMjestoOdustani);

		JLabel lblDodajMjestoNaziv = new JLabel("Naziv mjesta");
		lblDodajMjestoNaziv.setBounds(56, 107, 93, 14);
		contentPane.add(lblDodajMjestoNaziv);

		JLabel lblDodajMjestoPostanski = new JLabel("Poštanski broj");
		lblDodajMjestoPostanski.setBounds(56, 157, 93, 14);
		contentPane.add(lblDodajMjestoPostanski);

		textFieldNazivMjesta = new JTextField();
		textFieldNazivMjesta.setBounds(186, 104, 142, 20);
		contentPane.add(textFieldNazivMjesta);
		textFieldNazivMjesta.setColumns(10);

		textFieldPostanskiBroj = new JTextField();
		textFieldPostanskiBroj.setBounds(186, 154, 142, 20);
		contentPane.add(textFieldPostanskiBroj);
		textFieldPostanskiBroj.setColumns(10);

		btnDodajMjestoPotvrdi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String naziv = textFieldNazivMjesta.getText();
				String postanski = textFieldPostanskiBroj.getText();

				if (naziv.isEmpty() || postanski.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Sva polja su obavezna.", "Greška", JOptionPane.ERROR_MESSAGE);
				} else {
					try (Connection con = Konekcija.connect()) {
						String query = "INSERT INTO mjesto (naziv_mjesta, postanski_broj) VALUES (?, ?)";
						PreparedStatement pst = con.prepareStatement(query);
						pst.setString(1, naziv);
						pst.setString(2, postanski);

						pst.executeUpdate();
						JOptionPane.showMessageDialog(null, "Mjesto uspješno dodano.", "",
								JOptionPane.INFORMATION_MESSAGE);

						if (callback != null) {
							callback.refreshComboBox();
						}

						dispose();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Greška pri dodavanju mjesta.", "Greška",
								JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
				}
			}
		});
	}
}
