package Biblioteka;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class IzmijeniIzdavaca extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldNaziv;
	private JTextField textFieldTelefon;
	private JComboBox<String> comboBoxMjesto;
	private ArrayList<Integer> listaMjestaIds;
	private int izdavacId;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IzmijeniIzdavaca frame = new IzmijeniIzdavaca(1);
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
	public IzmijeniIzdavaca(int izdavacId) {
		this.izdavacId = izdavacId;

		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 440, 458);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		setLocationRelativeTo(null);
		setVisible(true);

		JLabel lblIzmijeniIzdavacaNaziv = new JLabel("Naziv izdavača");
		lblIzmijeniIzdavacaNaziv.setBounds(56, 107, 93, 14);
		contentPane.add(lblIzmijeniIzdavacaNaziv);

		JLabel lblIzmijeniIzdavacaTelefon = new JLabel("Kontakt telefon");
		lblIzmijeniIzdavacaTelefon.setBounds(56, 163, 93, 14);
		contentPane.add(lblIzmijeniIzdavacaTelefon);

		JLabel lblIzmijeniIzdavacaMjesto = new JLabel("Mjesto");
		lblIzmijeniIzdavacaMjesto.setBounds(56, 218, 93, 14);
		contentPane.add(lblIzmijeniIzdavacaMjesto);

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
		ucitajPodatkeIzdavaca();

		JButton btnIzmijeniIzdavacaPotvrdi = new JButton("Potvrdi");
		btnIzmijeniIzdavacaPotvrdi.setBounds(85, 349, 100, 30);
		contentPane.add(btnIzmijeniIzdavacaPotvrdi);

		JButton btnIzmijeniIzdavacaOdustani = new JButton("Odustani");
		btnIzmijeniIzdavacaOdustani.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				PregledIzdavaca pregledIzdavaca = new PregledIzdavaca();
				pregledIzdavaca.setVisible(true);
			}
		});
		btnIzmijeniIzdavacaOdustani.setBounds(228, 349, 100, 30);
		contentPane.add(btnIzmijeniIzdavacaOdustani);

		btnIzmijeniIzdavacaPotvrdi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String naziv = textFieldNaziv.getText();
				String telefon = textFieldTelefon.getText();
				int selectedMjestoId = listaMjestaIds.get(comboBoxMjesto.getSelectedIndex());

				if (naziv.isEmpty() || telefon.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Naziv izdavača i kontakt telefon su obavezni.", "Greška",
							JOptionPane.ERROR_MESSAGE);
				} else {
					try (Connection con = Konekcija.connect()) {
						String query = "UPDATE izdavaci SET naziv_izdavaca = ?, kontakt_telefon = ?, id_mjesta = ? WHERE id_izdavaca = ?";
						PreparedStatement pst = con.prepareStatement(query);
						pst.setString(1, naziv);
						pst.setString(2, telefon);
						pst.setInt(3, selectedMjestoId);
						pst.setInt(4, izdavacId);

						pst.executeUpdate();
						JOptionPane.showMessageDialog(null, "Izdavač uspješno ažuriran.", "Uspjeh",
								JOptionPane.INFORMATION_MESSAGE);

						dispose();
						PregledIzdavaca pregledIzdavaca = new PregledIzdavaca();
						pregledIzdavaca.setVisible(true);

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Greška pri ažuriranju izdavača.", "Greška",
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

	private void ucitajPodatkeIzdavaca() {
		try (Connection con = Konekcija.connect()) {
			String query = "SELECT naziv_izdavaca, kontakt_telefon, id_mjesta FROM izdavaci WHERE id_izdavaca = ?";
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, izdavacId);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				textFieldNaziv.setText(rs.getString("naziv_izdavaca"));
				textFieldTelefon.setText(rs.getString("kontakt_telefon"));
				int mjestoId = rs.getInt("id_mjesta");
				comboBoxMjesto.setSelectedIndex(listaMjestaIds.indexOf(mjestoId));
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Greška pri učitavanju podataka izdavača.", "Greška",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}
}
