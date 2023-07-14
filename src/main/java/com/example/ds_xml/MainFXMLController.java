/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
//package dssproj;
package com.example.ds_xml;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainFXMLController implements Initializable {

    FileChooser fileChooser;
    String xml, OutText;
    File inputPath, outputPath;

    boolean graphReady;

    @FXML
    private TextArea inTA;
    @FXML
    private TextArea outTA;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
           fileChooser = new FileChooser();
        //fileChooser.setInitialDirectory(new File("src\\sample"));

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        FileChooser.ExtensionFilter extFilter2 = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.getExtensionFilters().add(extFilter2);
    }    

    @FXML
    private void OnLoadFileButton(ActionEvent event)
    {
        inputPath = fileChooser.showOpenDialog(new Stage());

        if (inputPath != null) {
            InputStream orgInStream = System.in;
            try {
                System.setIn(new FileInputStream(inputPath));
            } catch (FileNotFoundException ee) {
                ee.printStackTrace();
            }
            xml = CustomStdIn.readString().replaceAll("\r", "");
            inTA.setText(xml);
            CustomStdIn.close();
            System.setIn(orgInStream);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("You must provide a file");
            alert.setContentText("You didn't provide a file");
            alert.showAndWait();
        }
    }

    @FXML
    private void OnSaveFileButton(ActionEvent event) {
        xml = inTA.getText();
        outputPath = fileChooser.showSaveDialog(new Stage());

        if (outputPath != null) {
            PrintStream orgOutStream = System.out;
            try {
                System.setOut(new PrintStream(outputPath));
            } catch (FileNotFoundException ee) {
                ee.printStackTrace();
            }
            CustomStdOut.write(OutText);

            CustomStdOut.close();
            System.setOut(orgOutStream);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("You must provide a path");
            alert.setContentText("You didn't provide a path to save the file");
            alert.showAndWait();
        }
    }

    @FXML
    private void OnJSONButton(ActionEvent event) {
        outTA.clear();
        xml = inTA.getText();
        if (checkIfEmpty(xml))
            return;

        ConsistencyCheck checker = new ConsistencyCheck(xml);
        if (checker.isFileConsistent() && !xml.isEmpty()) {
            OutText = XML_TO_JSON.xml2json(xml);
            outTA.setText(OutText);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Consistency error");
            alert.setHeaderText("Not consistent");
            alert.setContentText("The provided XML has to be consistent to be converted to JSON");
            alert.showAndWait();
        }
    }

    @FXML
    private void OnConsistencyCheckButton(ActionEvent event) {
        outTA.clear();
        xml = inTA.getText();
        if (checkIfEmpty(xml))
            return;

        ConsistencyCheck checker = new ConsistencyCheck(xml);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Consistency check");
        alert.setHeaderText("Consistency check");

        if (!checker.CheckTagsCorrectness()){
            StringBuilder msg = new StringBuilder();
            msg.append("Errors count = ").append(checker.getErrorCounter())
                    .append("\n").append("Correct the following tag/s, then check again:\n");

            for (String s : checker.getWrongTags()) {
                msg.append(s).append("\n");
            }
            outTA.setText(msg.toString());
            return; /* do not check for balanced tags until tags are corrected */
        }

        if (checker.CheckTagsBalance()) {
            alert.setContentText("XML file is consistent");
            outTA.clear();
        }
        else
        {
            StringBuilder msg = new StringBuilder();
            msg.append("Errors count = ").append(checker.getErrorCounter())
                    .append("\n").append("Error/s in the following tag/s:\n");

            for (String s : checker.getWrongTags()) {
                msg.append(s).append("\n");
            }

            alert.setContentText("XML file is NOT consistent");
            outTA.setText(msg.toString());
        }
        alert.showAndWait();
    }

    @FXML
    private void OnFormatButton(ActionEvent event) {
        outTA.clear();
        xml = inTA.getText();
        if (checkIfEmpty(xml))
            return;

        OutText = Formatting.Format(xml);
        outTA.setText(OutText);
    }

    @FXML
    private void OnMinifyButton(ActionEvent event)
    {
        outTA.clear();
        xml = inTA.getText();
        if (checkIfEmpty(xml))
            return;

        OutText = Minify.toMinify(xml);
        outTA.setText(OutText);
    }

    @FXML
    private void OnCompressButton(ActionEvent event) {
        outTA.clear();
        xml = inTA.getText();
        if (checkIfEmpty(xml))
            return;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Compression status");
        alert.setHeaderText("Compression status");

        outputPath = fileChooser.showSaveDialog(new Stage());

        if (outputPath != null)
        {
            HuffmanCompression.compress(xml, outputPath);
            alert.setContentText("Compression completed");

            InputStream InStream = System.in;
            try {
                System.setIn(new FileInputStream(outputPath));
            } catch (FileNotFoundException ee) {
                ee.printStackTrace();
            }
            OutText = CustomStdIn.readString();

            CustomStdIn.close();
            System.setIn(InStream);

            outTA.setText(OutText);
        }
        else {
            alert.setContentText("Please, Provide a path to save the file");
            alert.showAndWait();
        }
    }

    @FXML
    private void OnDecompressButton(ActionEvent event)
    {
        File orgFile, newFile;

        orgFile = fileChooser.showOpenDialog(new Stage());
        newFile = fileChooser.showSaveDialog(new Stage());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Decompression status");
        alert.setHeaderText("Decompression status");

        if (orgFile != null && newFile != null)
        {
            HuffmanCompression.decompress(orgFile, newFile);
            alert.setContentText("Decompression completed");

            InputStream InStream = System.in;

            try {
                System.setIn(new FileInputStream(orgFile));
            } catch (FileNotFoundException ee) {
                ee.printStackTrace();
            }

            inTA.setText(CustomStdIn.readString());
            CustomStdIn.close();
            try {
                System.setIn(new FileInputStream(newFile));
            } catch (FileNotFoundException ee) {
                ee.printStackTrace();
            }

            xml = CustomStdIn.readString().replaceAll("\r", "");
            CustomStdIn.close();
            System.setIn(InStream);
            outTA.setText(xml);
        }
        else {
            alert.setContentText("Please, Provide a path/s to save/load the files");
            alert.showAndWait();
        }
    }

    @FXML
    private void OnGraphButton(ActionEvent event)
    {
        outTA.clear();
        xml = inTA.getText();
        if (checkIfEmpty(xml))
            return;

        ConsistencyCheck checker = new ConsistencyCheck(xml);
        if (checker.isFileConsistent() && !xml.isEmpty()) {
            synchronized (Graph.class) {
                OutText = Graph.xmlToGraph(xml).printGraph();
                graphReady = true;
                outTA.setText(OutText);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Consistency error");
            alert.setHeaderText("Not consistent");
            alert.setContentText("The provided XML has to be consistent to be converted to visualized");
            alert.showAndWait();
        }
    }

    private static boolean checkIfEmpty(String xml) {
        if (xml.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No XML");
            alert.setHeaderText("You must provide an XML text or file");
            alert.setContentText("Provide an XML");
            alert.showAndWait();
            return true;
        }
        return false;
    }

    public void OnCorrectionButton(ActionEvent actionEvent)
    {
        outTA.clear();

        xml = inTA.getText();
        if (checkIfEmpty(xml))
            return;

        ConsistencyCheck checker = new ConsistencyCheck(xml);

        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
        alert1.setTitle("Correction status");
        alert1.setHeaderText("Correction status");

        if (checker.isFileConsistent())
        {
            alert1.setContentText("Nothing to correct as XML file is already correct");
            alert1.showAndWait();
            return;
        }

        if (!checker.CheckTagsCorrectness()){

            OutText = checker.correctTagBrackets();

            alert1.setContentText("All missing Tag brackets are corrected successfully");
            alert1.showAndWait();

            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("Important GuideLines");
            alert2.setHeaderText("Important GuideLines");

            alert2.setContentText("""
                    1) Save the corrected file in your desired path
                    2) Load this file and check for its consistency again
                    3) If the file is still not consistent then press Correction Button again
                    """);

            alert2.showAndWait();

            outTA.setText(OutText);
            return;
        }

        if (checker.CheckTagsBalance()) {
            alert1.setContentText("No Tag Balance errors" +
                    "The input XML file is correct");
            alert1.showAndWait();
            outTA.clear();
        }
        else
        {
            OutText = checker.correctTagBalance(xml);
            alert1.setContentText("XML file is corrected successfully");
            alert1.showAndWait();
            outTA.setText(OutText);
        }
    }

    public void OnMostInfluencerButton(ActionEvent actionEvent) {
        outTA.clear();
        xml = inTA.getText();
        if (checkIfEmpty(xml))
            return;

        OutText = Network.getMostInfluencer(xml);
        outTA.setText(OutText);

    }

    public void OnMostActiveButton(ActionEvent actionEvent) {
        outTA.clear();
        xml = inTA.getText();
        if (checkIfEmpty(xml))
            return;

        OutText = Network.getMostActive(xml);
        outTA.setText(OutText);
    }
}



