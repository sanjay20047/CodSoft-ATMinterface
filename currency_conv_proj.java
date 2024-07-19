package CurrencyConverter;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class currency_conv_proj extends JFrame {

	  private static final String API_KEY = "YOUR_API_KEY";
	    private static final String API_URL = "https://open.er-api.com/v6/latest/";

	    private JComboBox<String> baseCurrencyComboBox;
	    private JComboBox<String> targetCurrencyComboBox;
	    private JTextField amountTextField;
	    private JLabel resultLabel;

	    public currency_conv_proj() {
	        setTitle("Currency Converter");
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setSize(500, 300);
	        setLocationRelativeTo(null);

	        initializeComponents();
	    }

	    private void initializeComponents() {
	        JPanel panel = new JPanel(new GridBagLayout());
	        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

	        String[] currencies = {"USD", "EUR", "GBP", "JPY", "AUD", "CAD", "INR"};
	        baseCurrencyComboBox = new JComboBox<>(currencies);
	        targetCurrencyComboBox = new JComboBox<>(currencies);
	        amountTextField = new JTextField(10);
	        resultLabel = new JLabel();

	        JButton convertButton = new JButton("Convert");
	        convertButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                convertCurrency();
	            }
	        });

	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.insets = new Insets(5, 5, 5, 5);

	        gbc.gridx = 0;
	        gbc.gridy = 0;
	        panel.add(new JLabel("Base Currency:"), gbc);

	        gbc.gridx = 1;
	        panel.add(baseCurrencyComboBox, gbc);

	        gbc.gridx = 0;
	        gbc.gridy = 1;
	        panel.add(new JLabel("Target Currency:"), gbc);

	        gbc.gridx = 1;
	        panel.add(targetCurrencyComboBox, gbc);

	        gbc.gridx = 0;
	        gbc.gridy = 2;
	        panel.add(new JLabel("Amount:"), gbc);

	        gbc.gridx = 1;
	        panel.add(amountTextField, gbc);

	        gbc.gridx = 0;
	        gbc.gridy = 3;
	        gbc.gridwidth = 2;
	        gbc.anchor = GridBagConstraints.CENTER;
	        panel.add(convertButton, gbc);

	        gbc.gridx = 0;
	        gbc.gridy = 4;
	        panel.add(resultLabel, gbc);

	        add(panel);
	    }

	 

	    private void convertCurrency() {
	        String baseCurrency = (String) baseCurrencyComboBox.getSelectedItem();
	        String targetCurrency = (String) targetCurrencyComboBox.getSelectedItem();
	        String amountStr = amountTextField.getText();

	        try {
	            double amount = Double.parseDouble(amountStr);
	            double exchangeRate = getExchangeRate(baseCurrency, targetCurrency);
	            double convertedAmount = amount * exchangeRate;

	            resultLabel.setText(String.format("Converted Amount: %.2f %s", convertedAmount, targetCurrency));
	        } catch (NumberFormatException e) {
	            resultLabel.setText("Invalid amount");
	        }
	    }

	    private double getExchangeRate(String baseCurrency, String targetCurrency) {
	        try {
	            URL url = new URL(API_URL + baseCurrency + "?apikey=" + API_KEY);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("GET");

	            int responseCode = connection.getResponseCode();

	            if (responseCode == HttpURLConnection.HTTP_OK) {
	                Scanner scanner = new Scanner(connection.getInputStream());
	                StringBuilder response = new StringBuilder();

	                while (scanner.hasNextLine()) {
	                    response.append(scanner.nextLine());
	                }

	                scanner.close();
	                connection.disconnect();

	                String jsonResponse = response.toString();
	                double exchangeRate = Double.parseDouble(jsonResponse
	                        .split("\"" + targetCurrency + "\":")[1]
	                        .split(",")[0]);

	                return exchangeRate;
	            } else {
	                throw new IOException("Failed to fetch exchange rates. HTTP response code: " + responseCode);
	            }
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        return 0.0;
	    }

	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	                new currency_conv_proj().setVisible(true);
	            }
	        });
	    }
}