package com.github.pixelrunstudios.ChemHelper;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class MainGUI extends JFrame{

	private static final long serialVersionUID = 278653524858150605L;

	private JPanel contentPane;
	private JTextField textField;
	private JLabel label;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			@Override
			public void run(){
				try{
					MainGUI frame = new MainGUI();
					frame.setVisible(true);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainGUI(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 329, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textField = new JTextField();
		textField.setBounds(113, 29, 134, 28);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel lblCompound = new JLabel("Compound:");
		lblCompound.setBounds(28, 35, 86, 16);
		contentPane.add(lblCompound);

		JButton btnGo = new JButton("GO");
		btnGo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					label.setText(String.valueOf(MolarMass.calculate(
							ParseCompound.parseCompound(textField.getText())
							, FileToMap.readMapFromFile(new File("elements.txt")), textField.getText())));
				}
				catch(Exception e1){
					// TODO Auto-generated catch block
					e1.printStackTrace();
					label.setText("Error!");
				}
			}
		});
		btnGo.setBounds(249, 30, 54, 29);
		contentPane.add(btnGo);

		JLabel lblMolar = new JLabel("Molar mass:");
		lblMolar.setBounds(27, 63, 76, 16);
		contentPane.add(lblMolar);

		label = new JLabel("");
		label.setBounds(113, 63, 179, 16);
		contentPane.add(label);
	}
}
