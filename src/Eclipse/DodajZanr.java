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

public class DodajZanr extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldNazivZanra;
	private String ime_prozora;
	private RefreshZanr callback;

	/**
	 * Create the frame.
	 */
	public DodajZanr(String ime_prozora, RefreshZanr callback) {
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

		JButton btnDodajZanrPotvrdi = new JButton("Potvrdi");
		btnDodajZanrPotvrdi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnDodajZanrPotvrdi.setBounds(66, 238, 100, 30);
		contentPane.add(btnDodajZanrPotvrdi);

		JButton btnDodajMjestoOdustani = new JButton("Odustani");
		btnDodajMjestoOdustani.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				if (DodajZanr.this.ime_prozora.equals("zanr")) {
					DodajKnjigu knjiga = new DodajKnjigu();
					knjiga.setVisible(true);
				}
			}
		});
		btnDodajMjestoOdustani.setBounds(201, 238, 100, 30);
		contentPane.add(btnDodajMjestoOdustani);

		JLabel lblDodajZanrNaziv = new JLabel("Naziv žanra");
		lblDodajZanrNaziv.setBounds(56, 107, 93, 14);
		contentPane.add(lblDodajZanrNaziv);

		textFieldNazivZanra = new JTextField();
		textFieldNazivZanra.setBounds(186, 104, 142, 20);
		contentPane.add(textFieldNazivZanra);
		textFieldNazivZanra.setColumns(10);

		btnDodajZanrPotvrdi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String naziv = textFieldNazivZanra.getText();

				if (naziv.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Sva polja su obavezna.", "Greška", JOptionPane.ERROR_MESSAGE);
				} else {
					try (Connection con = Konekcija.connect()) {
						String query = "INSERT INTO zanr (naziv_zanra) VALUES (?)";
						PreparedStatement pst = con.prepareStatement(query);
						pst.setString(1, naziv);

						pst.executeUpdate();
						JOptionPane.showMessageDialog(null, "Žanr uspješno dodat.", "",
								JOptionPane.INFORMATION_MESSAGE);

						if (callback != null) {
							callback.refreshComboBoxZanra();
						}

						dispose();

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Greška pri dodavanju žanra.", "Greška",
								JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
				}
			}
		});
	}

}
