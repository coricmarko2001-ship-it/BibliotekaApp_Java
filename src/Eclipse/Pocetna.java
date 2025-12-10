package Biblioteka;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Pocetna extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Pocetna frame = new Pocetna();
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
	public Pocetna() {
		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 798, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		setLocationRelativeTo(null);
		setVisible(true);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnKnjige = new JMenu("Knjige");
		menuBar.add(mnKnjige);

		JMenuItem mntmPregledKnjiga = new JMenuItem("Pregled knjiga");
		mnKnjige.add(mntmPregledKnjiga);

		mntmPregledKnjiga.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				PregledKnjiga pregledKnjiga = new PregledKnjiga();
				pregledKnjiga.setVisible(true);
			}
		});

		JMenu mnKorisnici = new JMenu("Korisnici");
		menuBar.add(mnKorisnici);

		JMenuItem mntmPregledKorisnika = new JMenuItem("Pregled korisnika");
		mnKorisnici.add(mntmPregledKorisnika);

		mntmPregledKorisnika.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				PregledKorisnika pregledKorisnika = new PregledKorisnika();
				pregledKorisnika.setVisible(true);
			}
		});

		JMenu mnBibliotekari = new JMenu("Bibliotekari");
		menuBar.add(mnBibliotekari);

		JMenuItem mntmPregledBibliotekara = new JMenuItem("Pregled bibliotekara");
		mnBibliotekari.add(mntmPregledBibliotekara);

		mntmPregledBibliotekara.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				PregledBibliotekara pregledBibliotekara = new PregledBibliotekara();
				pregledBibliotekara.setVisible(true);
			}
		});

		JMenu mnAutori = new JMenu("Autori");
		menuBar.add(mnAutori);

		JMenuItem mntmPregledAutora = new JMenuItem("Pregled autora");
		mnAutori.add(mntmPregledAutora);

		mntmPregledAutora.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				PregledAutora pregledAutora = new PregledAutora();
				pregledAutora.setVisible(true);
			}
		});

		JMenu mnIzdavanje = new JMenu("Izdavanje");
		menuBar.add(mnIzdavanje);

		JMenuItem mntmPregledIzdatihKnjiga = new JMenuItem("Pregled izdatih knjiga");
		mnIzdavanje.add(mntmPregledIzdatihKnjiga);

		mntmPregledIzdatihKnjiga.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				PregledIzdatihKnjiga pregledIzdatihKnjiga = new PregledIzdatihKnjiga();
				pregledIzdatihKnjiga.setVisible(true);
			}
		});

		JMenuItem mntmRezervisiKnjigu = new JMenuItem("Rezerviši knjigu");
		mnIzdavanje.add(mntmRezervisiKnjigu);

		mntmRezervisiKnjigu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				RezervisiKnjigu rezervisiKnjigu = new RezervisiKnjigu();
				rezervisiKnjigu.setVisible(true);
			}
		});

		JMenuItem mntmVratiKnjigu = new JMenuItem("Vrati knjigu");
		mnIzdavanje.add(mntmVratiKnjigu);

		mntmVratiKnjigu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				VratiKnjigu vratiKnjigu = new VratiKnjigu();
				vratiKnjigu.setVisible(true);
			}
		});

		JMenu mnIzdavaci = new JMenu("Izdavači");
		menuBar.add(mnIzdavaci);

		JMenuItem mntmPregledIzdavaca = new JMenuItem("Pregled izdavača");
		mnIzdavaci.add(mntmPregledIzdavaca);

		mntmPregledIzdavaca.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				PregledIzdavaca pregledIzdavaca = new PregledIzdavaca();
				pregledIzdavaca.setVisible(true);
			}
		});

		JButton btnIzadji = new JButton("Izađi");
		btnIzadji.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Login login = new Login();
				login.setVisible(true);
			}
		});
		btnIzadji.setBounds(659, 349, 100, 30);
		contentPane.add(btnIzadji);
	}
}
