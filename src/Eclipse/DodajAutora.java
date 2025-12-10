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
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import com.toedter.calendar.JDateChooser;

public class DodajAutora extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldIme;
	private JTextField textFieldPrezime;
	private JDateChooser dateChooser;
	private String ime_prozora;
	private RefreshAutora callback;

	public DodajAutora(String ime_prozora, RefreshAutora callback) {
		this.ime_prozora = ime_prozora;
		this.callback = callback;

		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 488, 413);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		setLocationRelativeTo(null);
		setVisible(true);

		JLabel lblDodajAutoraIme = new JLabel("Ime");
		lblDodajAutoraIme.setBounds(56, 107, 72, 14);
		contentPane.add(lblDodajAutoraIme);

		JLabel lblDodajAutoraPrezime = new JLabel("Prezime");
		lblDodajAutoraPrezime.setBounds(56, 163, 72, 14);
		contentPane.add(lblDodajAutoraPrezime);

		JLabel lblDodajAutoraDatumRodjenja = new JLabel("Datum rođenja");
		lblDodajAutoraDatumRodjenja.setBounds(56, 218, 93, 14);
		contentPane.add(lblDodajAutoraDatumRodjenja);

		textFieldIme = new JTextField();
		textFieldIme.setBounds(186, 104, 142, 20);
		contentPane.add(textFieldIme);
		textFieldIme.setColumns(10);

		textFieldPrezime = new JTextField();
		textFieldPrezime.setBounds(186, 160, 142, 20);
		contentPane.add(textFieldPrezime);
		textFieldPrezime.setColumns(10);

		dateChooser = new JDateChooser();
		dateChooser.setBounds(186, 212, 165, 20);
		contentPane.add(dateChooser);

		JLabel lblOptional = new JLabel("(nije obavezno)");
		lblOptional.setBounds(362, 218, 100, 14);
		contentPane.add(lblOptional);

		JButton btnDodajAutoraPotvrdi = new JButton("Potvrdi");
		btnDodajAutoraPotvrdi.setBounds(85, 311, 100, 30);
		contentPane.add(btnDodajAutoraPotvrdi);

		JButton btnDodajAutoraOdustani = new JButton("Odustani");
		btnDodajAutoraOdustani.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				if (DodajAutora.this.ime_prozora.equals("autor")) {
					PregledAutora autor = new PregledAutora();
					autor.setVisible(true);
				} else if (DodajAutora.this.ime_prozora.equals("knjiga")) {
					DodajKnjigu knjiga = new DodajKnjigu();
					knjiga.setVisible(true);
				}

			}
		});
		btnDodajAutoraOdustani.setBounds(226, 311, 100, 30);
		contentPane.add(btnDodajAutoraOdustani);

		btnDodajAutoraPotvrdi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ime = textFieldIme.getText();
				String prezime = textFieldPrezime.getText();
				java.util.Date datumRodjenja = dateChooser.getDate();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				if (ime.isEmpty() || prezime.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Ime i prezime su obavezni.", "Greška",
							JOptionPane.ERROR_MESSAGE);
				} else {
					try (Connection con = Konekcija.connect()) {
						String query = "INSERT INTO autori (ime_autora, prezime_autora, godina_rodjenja) VALUES (?, ?, ?)";
						PreparedStatement pst = con.prepareStatement(query);
						pst.setString(1, ime);
						pst.setString(2, prezime);
						if (datumRodjenja != null) {
							String datumRodjenjaStr = sdf.format(datumRodjenja);
							pst.setString(3, datumRodjenjaStr);
						} else {
							pst.setNull(3, java.sql.Types.DATE);
						}

						pst.executeUpdate();
						JOptionPane.showMessageDialog(null, "Autor uspješno dodan.", "Uspjeh",
								JOptionPane.INFORMATION_MESSAGE);

						if (callback != null) {
							callback.refreshComboBoxAutora();
						}

						dispose();

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Greška pri dodavanju autora.", "Greška",
								JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
				}
			}
		});
	}
}
