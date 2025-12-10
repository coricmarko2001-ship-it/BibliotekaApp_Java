package Biblioteka;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;
import com.toedter.calendar.JDateChooser;

public class IzmijeniAutora extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldIme;
	private JTextField textFieldPrezime;
	private JDateChooser dateChooser;
	private int autorId;

	public IzmijeniAutora(int autorId) {
		setUndecorated(true);
		setResizable(false);
		this.autorId = autorId;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		setLocationRelativeTo(null);
		setVisible(true);

		JLabel lblIme = new JLabel("Ime");
		lblIme.setBounds(56, 50, 72, 14);
		contentPane.add(lblIme);

		JLabel lblPrezime = new JLabel("Prezime");
		lblPrezime.setBounds(56, 100, 72, 14);
		contentPane.add(lblPrezime);

		JLabel lblDatumRodjenja = new JLabel("Datum rođenja");
		lblDatumRodjenja.setBounds(56, 150, 93, 14);
		contentPane.add(lblDatumRodjenja);

		textFieldIme = new JTextField();
		textFieldIme.setBounds(186, 47, 142, 20);
		contentPane.add(textFieldIme);
		textFieldIme.setColumns(10);

		textFieldPrezime = new JTextField();
		textFieldPrezime.setBounds(186, 97, 142, 20);
		contentPane.add(textFieldPrezime);
		textFieldPrezime.setColumns(10);

		dateChooser = new JDateChooser();
		dateChooser.setBounds(186, 147, 142, 20);
		contentPane.add(dateChooser);

		JButton btnIzmeniAutoraPotvrdi = new JButton("Potvrdi");
		btnIzmeniAutoraPotvrdi.setBounds(85, 200, 100, 30);
		contentPane.add(btnIzmeniAutoraPotvrdi);

		JButton btnIzmeniAutoraOdustani = new JButton("Odustani");
		btnIzmeniAutoraOdustani.setBounds(226, 200, 100, 30);
		contentPane.add(btnIzmeniAutoraOdustani);

		btnIzmeniAutoraOdustani.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				PregledAutora pregledAutora = new PregledAutora();
				pregledAutora.setVisible(true);
			}
		});

		btnIzmeniAutoraPotvrdi.addActionListener(new ActionListener() {
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
						String query = "UPDATE autori SET ime_autora = ?, prezime_autora = ?, godina_rodjenja = ? WHERE id_autora = ?";
						PreparedStatement pst = con.prepareStatement(query);
						pst.setString(1, ime);
						pst.setString(2, prezime);
						if (datumRodjenja != null) {
							String datumRodjenjaStr = sdf.format(datumRodjenja);
							pst.setString(3, datumRodjenjaStr);
						} else {
							pst.setNull(3, java.sql.Types.DATE);
						}
						pst.setInt(4, autorId);

						pst.executeUpdate();
						JOptionPane.showMessageDialog(null, "Autor uspješno ažuriran.", "Uspjeh",
								JOptionPane.INFORMATION_MESSAGE);

						dispose();
						PregledAutora autor = new PregledAutora();
						autor.setVisible(true);

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Greška pri ažuriranju autora.", "Greška",
								JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
				}
			}
		});

		loadAutorData();
	}

	private void loadAutorData() {
		try (Connection con = Konekcija.connect()) {
			String query = "SELECT ime_autora, prezime_autora, godina_rodjenja FROM autori WHERE id_autora = ?";
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, autorId);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				textFieldIme.setText(rs.getString("ime_autora"));
				textFieldPrezime.setText(rs.getString("prezime_autora"));
				java.util.Date datumRodjenja = rs.getDate("godina_rodjenja");
				dateChooser.setDate(datumRodjenja);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Greška pri učitavanju podataka autora.", "Greška",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}
}
