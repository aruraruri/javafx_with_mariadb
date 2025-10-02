import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private ComboBox<String> conversionTypeComboBox;
    private TextField tempAmtTextField = new TextField();
    private Label resultLabel = new Label();
    private double fahrenheit;

    private enum conversionTypes {FTC, KTC, CTK}
    private conversionTypes currentConversionType = conversionTypes.CTK;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        tempAmtTextField.setPromptText("Enter Celsius");

        ObservableList<String> conversionTypeStrings = FXCollections.observableArrayList(
                "Fahrenheit to Celsius",
                "Kelvin to Celsius",
                "Celsius to Kelvin"
        );

        conversionTypeComboBox = new ComboBox<>(conversionTypeStrings);

        conversionTypeComboBox.getSelectionModel().select(2); // Default to "Celsius to Kelvin"
        conversionTypeComboBox.setOnAction(e -> {
            int selectedIndex = conversionTypeComboBox.getSelectionModel().getSelectedIndex();
            switch (selectedIndex) {
                case 0:
                    currentConversionType = conversionTypes.FTC;
                    tempAmtTextField.setPromptText("Enter Fahrenheit");
                    break;
                case 1:
                    currentConversionType = conversionTypes.KTC;
                    tempAmtTextField.setPromptText("Enter Kelvin");
                    break;
                case 2:
                    currentConversionType = conversionTypes.CTK;
                    tempAmtTextField.setPromptText("Enter Celsius");
                    break;
            }
        });


        Button convertButton = new Button("Convert");
        convertButton.setOnAction(e -> convertTemperature(currentConversionType));

        Button saveButton = new Button("Save to DB");
        saveButton.setOnAction(e -> Database.saveTemperature(
                Double.parseDouble(tempAmtTextField.getText()), fahrenheit, resultLabel));

        VBox root = new VBox(10, tempAmtTextField, conversionTypeComboBox, convertButton, resultLabel, saveButton);
        Scene scene = new Scene(root, 300, 200);

        stage.setTitle("Celsius to Fahrenheit");
        stage.setScene(scene);
        stage.show();
    }

    private void convertTemperature(conversionTypes type) {
        System.out.println("Converting temperature... "+type);
        try {
            double inputTemp = Double.parseDouble(tempAmtTextField.getText());
            switch (type) {
                case FTC:
                    fahrenheit = (inputTemp - 32) * 5 / 9;
                    resultLabel.setText(String.format("%.2f Fahrenheit is %.2f Celsius", inputTemp, fahrenheit));
                    break;
                case KTC:
                    fahrenheit = inputTemp - 273.15;
                    resultLabel.setText(String.format("%.2f Kelvin is %.2f Celsius", inputTemp, fahrenheit));
                    break;
                case CTK:
                    fahrenheit = inputTemp + 273.15;
                    resultLabel.setText(String.format("%.2f Celsius is %.2f Kelvin", inputTemp, fahrenheit));
                    break;
            }
        } catch (NumberFormatException e) {
            resultLabel.setText("Invalid input. Please enter a valid number.");
        }
    }
}