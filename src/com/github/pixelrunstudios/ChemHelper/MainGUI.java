package com.github.pixelrunstudios.ChemHelper;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class MainGUI extends JFrame{

	private static final long serialVersionUID = 278653524858150605L;

	private JPanel contentPane;
	private JTextField textField;
	private JLabel label;
	private JTextField inEq;
	private JTextField outEq;
	private JTextField amt;
	private JTextField outSub;
	private JTextField inSub;
	private JLabel result;
	private JTextField inB;
	private JTextField outB;
	private JLabel coeff;

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
		setBounds(100, 100, 450, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Molar Mass", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		contentPane.add(panel_3);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		panel_3.add(panel);

		JLabel lblCompound = new JLabel("Compound:");
		panel.add(lblCompound);
		lblCompound.setBounds(28, 35, 86, 16);

		textField = new JTextField("H2O");
		textField.setToolTipText("Substance");
		panel.add(textField);
		textField.setBounds(113, 29, 134, 28);
		textField.setColumns(10);

		JPanel panel_1 = new JPanel();
		panel_3.add(panel_1);

		JLabel lblMolar = new JLabel("Molar mass:");
		panel_1.add(lblMolar);
		lblMolar.setBounds(27, 63, 76, 16);

		label = new JLabel("");
		panel_1.add(label);
		label.setBounds(113, 63, 179, 16);

		JPanel panel_4 = new JPanel();
		panel_3.add(panel_4);

		JButton btnGo = new JButton("Go");
		panel_4.add(btnGo);
		btnGo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					label.setText(String.valueOf(MolarMass.calculate(
							ParseCompound.parseExpression(textField.getText())
							, FileToMap.readMapFromFile(new File("elements.txt")))));
				}
				catch(Exception e1){
					// TODO Auto-generated catch block
					e1.printStackTrace();
					label.setText("Error!");
				}
			}
		});
		btnGo.setBounds(249, 30, 54, 29);

		JPanel panel_10 = new JPanel();
		panel_10.setBorder(new TitledBorder(null, "Equation Balancing", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel_10);
		panel_10.setLayout(new BoxLayout(panel_10, BoxLayout.Y_AXIS));

		JPanel panel_11 = new JPanel();
		panel_10.add(panel_11);

		JLabel label_2 = new JLabel("Equation:");
		panel_11.add(label_2);

		inB = new JTextField("C8H18 + O2");
		inB.setColumns(10);
		panel_11.add(inB);

		JLabel label_3 = new JLabel("->");
		panel_11.add(label_3);

		outB = new JTextField("CO2 + H2O");
		outB.setColumns(10);
		panel_11.add(outB);

		JPanel panel_13 = new JPanel();
		panel_10.add(panel_13);

		JLabel lblResult_1 = new JLabel("Coefficients:");
		panel_13.add(lblResult_1);

		coeff = new JLabel("");
		panel_13.add(coeff);

		JPanel panel_12 = new JPanel();
		panel_10.add(panel_12);

		JButton btnGo_2 = new JButton("Go");
		btnGo_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					Pair<ChemistryUnit, ChemistryUnit> pair =
							ParseCompound.parseEquation(inB.getText(), outB.getText());
					String out = pair.getValueOne() + " -> " + pair.getValueTwo();
					coeff.setText(out);
				}
				catch(Exception e1){
					e1.printStackTrace();
					coeff.setText("Error!");
				}
			}
		});
		panel_12.add(btnGo_2);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Stoichiometry", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		contentPane.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));

		JPanel panel_5 = new JPanel();
		panel_2.add(panel_5);

		JLabel lblEquation = new JLabel("Equation:");
		panel_5.add(lblEquation);

		inEq = new JTextField("2H2 + O2");
		panel_5.add(inEq);
		inEq.setColumns(10);

		JLabel label_1 = new JLabel("->");
		panel_5.add(label_1);

		outEq = new JTextField("2H2O");
		panel_5.add(outEq);
		outEq.setColumns(10);

		JPanel panel_6 = new JPanel();
		panel_2.add(panel_6);

		JLabel lblConvert = new JLabel("Convert From:");
		panel_6.add(lblConvert);

		amt = new JTextField();
		amt.setToolTipText("Amount");
		amt.setText("1");
		panel_6.add(amt);
		amt.setColumns(4);

		JComboBox<String> inUnit = new JComboBox<String>();
		inUnit.setToolTipText("Unit");
		inUnit.addItem("g");
		inUnit.addItem("mol");
		panel_6.add(inUnit);

		inSub = new JTextField("H2");
		inSub.setToolTipText("Substance");
		panel_6.add(inSub);
		inSub.setColumns(8);

		JPanel panel_7 = new JPanel();
		panel_2.add(panel_7);

		JLabel lblTo = new JLabel("Convert To:");
		panel_7.add(lblTo);

		JComboBox<String> outUnit = new JComboBox<String>();
		outUnit.setToolTipText("Unit");
		outUnit.addItem("g");
		outUnit.addItem("mol");
		panel_7.add(outUnit);

		outSub = new JTextField("H2O");
		outSub.setToolTipText("Substance");
		panel_7.add(outSub);
		outSub.setColumns(8);

		JPanel panel_8 = new JPanel();
		panel_2.add(panel_8);

		JLabel lblResult = new JLabel("Result:");
		panel_8.add(lblResult);

		result = new JLabel("");
		panel_8.add(result);

		JPanel panel_9 = new JPanel();
		panel_2.add(panel_9);

		JButton btnGo_1 = new JButton("Go");
		panel_9.add(btnGo_1);
		btnGo_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					double out = Stoichiometry.convert(inEq.getText(), outEq.getText(),
							Double.parseDouble(amt.getText()),
							inUnit.getItemAt(inUnit.getSelectedIndex()),
							inSub.getText(),
							outUnit.getItemAt(outUnit.getSelectedIndex()),
							outSub.getText(),
							FileToMap.readMapFromFile(new File("elements.txt")));
					result.setText(String.valueOf(out));
				}
				catch(Exception e1){
					// TODO Auto-generated catch block
					e1.printStackTrace();
					coeff.setText("Error!");
				}
			}
		});
	}
}
