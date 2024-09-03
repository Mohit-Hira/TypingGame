package application;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
//Geometry
import javafx.geometry.Insets;
import javafx.geometry.Pos;

//Application quitting.
import javafx.application.Platform;

//Timing

import javafx.animation.KeyFrame;
import javafx.util.Duration;

import java.util.Random;

//custom fonts
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

//file reading
import java.io.FileReader;
import java.io.BufferedReader;

public class TypingGame extends Application {
	// declaring text field
	TextField txtfTyping;
	// labels
	Label lblSentence, lblInstr, lblErrors, lblTimeLeft;
	// buttons rating and start
	Button btnStart, btnrating;

	String[] sentences = new String[5];

	int errors = 0;
	int totaltime = 30;
	int timeleft = totaltime;
	int i = 0;
	boolean gamestart = false;
	// timer
	Timeline timeline;
	// progress bar
	ProgressBar progressBar;
	// progress indicator
	ProgressIndicator progressInd;

	public TypingGame() {
		// intiantiate components
		txtfTyping = new TextField();
		txtfTyping.setPromptText("press start");
		txtfTyping.setDisable(true);
		lblInstr = new Label("type each sentence into the lower textfield");
		lblErrors = new Label("error" + errors);
		lblTimeLeft = new Label("time left");
		progressBar = new ProgressBar(1);
		progressInd = new ProgressIndicator(0);
		btnStart = new Button("start");
		btnrating = new Button("rate the game");
		lblSentence = new Label();

	}

	public void start(Stage primaryStage) throws Exception {
		// give title width and height
		primaryStage.setTitle("typegaming");
		primaryStage.setWidth(600);
		primaryStage.setHeight(300);

		// create a root node
		BorderPane root = new BorderPane();
		// styling of our dialog box
		root.setStyle("-fx-background-color:pink");
//create a hbox of our center container
		HBox centerBox = new HBox(10);
		// set a alignment for the center box
		centerBox.setAlignment(Pos.CENTER_LEFT);

		centerBox.setPadding(new Insets(10));
		// add all the necessary components to the centre box
		centerBox.getChildren().addAll(txtfTyping, lblErrors);
		// create a vbox for the center box
		VBox topBox = new VBox(10);
		topBox.setAlignment(Pos.BASELINE_LEFT);
		topBox.setPadding(new Insets(10));
		// add all the necessary components to the topbox
		topBox.getChildren().addAll(lblInstr, lblSentence, btnrating);
		// create a vbox for the bottombox
		VBox bottomBox = new VBox(10);
		// set padding
		root.setPadding(new Insets(10));

		bottomBox.setPadding(new Insets(10));
		// set algnment
		bottomBox.setAlignment(Pos.BASELINE_LEFT);
		bottomBox.setPadding(new Insets(10));
		// add all the necessary components to the bottombox
		bottomBox.getChildren().addAll(lblTimeLeft, progressBar, btnStart);
		// set allignment
		bottomBox.setAlignment(Pos.CENTER);
		// set the size of button
		btnStart.setPrefSize(80, 40);
		// set the style of button start
		btnStart.setStyle("-fx-background-color:lightgreen");
		// set spacing
		bottomBox.setPadding(new Insets(-4));
		// add the center,top and bottom box to the root
		root.setCenter(centerBox);
		root.setTop(topBox);
		root.setBottom(bottomBox);
//create hbox 
		HBox hbox = new HBox();
		// add the progress ind to the right side
		root.setRight(hbox);
		// add component
		hbox.getChildren().addAll(progressInd);
		// set padding
		hbox.setPadding(new Insets(0, 20, 0, 0));

		// populate centerBox
		txtfTyping.setMinWidth(400);
		
		lblTimeLeft.setTextAlignment(TextAlignment.LEFT);
		lblTimeLeft.minWidthProperty().bind(progressBar.widthProperty());
		progressBar.setMinWidth(550);

		// adding to the scene
		Scene scene = new Scene(root);
	
		primaryStage.setScene(scene);
		//show the stage
		primaryStage.show();

	}

	public void init() {
//event handlings 
		btnStart.setOnAction(event -> startBtn());
		btnrating.setOnAction(event -> rating());

		txtfTyping.setOnKeyReleased(ae -> {
			// check if the start of each distance is the same
			if (lblSentence.getText().startsWith(txtfTyping.getText())) {
				// if it is then the text should be green
				txtfTyping.setStyle("-fx-text-fill: green;");
			} else {
				// otherwise red
				txtfTyping.setStyle("-fx-text-fill: red;");
				// increment errors
				errors++;
				// update the error label
				lblErrors.setText("error:" + errors);
			}
			// check if the user has finished typing the sentence
			if (txtfTyping.getText().equals(lblSentence.getText())) {
				// if user typing a correct sentence progress indicator goes increase
				progressInd.setProgress(progressInd.getProgress() + 0.2);

				if (i < sentences.length - 1) {
					i++;
					lblSentence.setText(sentences[i]);
					txtfTyping.clear();
				}

				else {

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("congratulation");
					alert.setHeaderText("Yoy won");
					alert.setContentText("Total Errors: " + errors + "\nSentences typed: 5/5");
					alert.show();

					gamestart = false;
					btnStart.setText("start");
					txtfTyping.setDisable(true);
					lblSentence.setText("");
					progressBar.setProgress(1);
					progressInd.setProgress(0);
					errors = 0;
					i = 0;
					timeleft = totaltime;

					lblTimeLeft.setText("time left: " + timeleft);
					timeline.stop();

				}
			}

		});

	}
//update the button 

	public void startBtn() {
		// if the game hasn't started
		if (!gamestart) {
			// set the game to started
			gamestart = true;
			// enable the typing textfield
			txtfTyping.setDisable(false);
			try {
				// create a file reader
				FileReader reader = new FileReader("./asset/sentences.csv");
				// create a buffer reader
				BufferedReader br = new BufferedReader(reader);
				// create a temp string
				String line;
				// initialise the sentences index
				int j = 0;
				// while there are still lines to read
				while ((line = br.readLine()) != null) {
					// add the line to the sentences array
					sentences[j] = line;
					// increment the index
					j++;
				}
				// close the buffer reader
				br.close();
				// update the sentence label
				lblSentence.setText(sentences[i]);

			} catch (Exception e) {
				// if an exception is thrown
				// print a message to the console
				System.err.println(e.getMessage());
				// quit the application
				Platform.exit();
			}

			// update the button text
			btnStart.setText("stop");
			// create a timeline
			timeline = new Timeline(new KeyFrame(Duration.seconds(1), ae -> updateTime()));
			// set the timeline to repeat
			timeline.setCycleCount(Timeline.INDEFINITE);
			// start the timeline
			timeline.play();
			// update the progress indicator
			progressInd.setProgress(0);
			// update the progress bar
			progressBar.setProgress(1);
			// update the time label
			lblTimeLeft.setText("Time Left:" + timeleft);
			// update the font
			lblSentence.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
		} else {
			// if the game has started
			// stop the game
			stopGame();
		}
	}

	public void rating() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("rate the game");
		alert.setHeaderText("please rate 0 to 10 please");
		alert.setContentText("please rate the game from 0 to 10");
		TextField txtInput = new TextField();
		alert.getDialogPane().setContent(txtInput);
		ButtonType btnRate = new ButtonType("rate");
		ButtonType btnCancel = new ButtonType("cancel", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(btnRate, btnCancel);
		alert.showAndWait();
		if (alert.getResult() == btnRate) {
			String s = txtInput.getText().trim();
			int rate = Integer.parseInt(s);
			if (rate > 0 && rate <= 10) {
				Alert info = new Alert(AlertType.INFORMATION);
				info.setTitle("rate accepted");
				info.setHeaderText("rate accepted");
				info.setContentText("thank you for rating the game");
				info.show();

			} else {
				Alert info = new Alert(AlertType.WARNING);
				info.setTitle("invalid rate");
				info.setHeaderText("invalid rate");
				info.setContentText("please enter a rate from 0 to 10");
				info.show();

			}

		}

	}

	public void updateTime() {
		// decrement the timeleft
		timeleft--;
		// update the progress bar
		progressBar.setProgress((double) timeleft / totaltime);
		// update the progress indicator
		// progInd.setProgress((double)timeleft/totaltime);
		// update the time label
		lblTimeLeft.setText("Time Left: " + timeleft);
		// if the timeleft is 0
		if (timeleft == 0) {
			// stop the game
			stopGame();
		}
	}

	public void stopGame() {
		// stop the timeline
		timeline.stop();
		// set the game to stopped
		gamestart = false;
		// disable the typing textfield
		txtfTyping.setDisable(true);
		// update the button text
		btnStart.setText("start");
		// update the progress indicator
		progressInd.setProgress(0);
		// update the progress bar
		progressBar.setProgress(0);
		// show the end dialog
		showEndDialog();
	}

	public void showEndDialog() {
		// create an alert
		Alert alert = new Alert(AlertType.INFORMATION);
		// set the title
		alert.setTitle("Game Over");
		// set the header
		alert.setHeaderText("Game Over!");
		// set the message
		alert.setContentText("Total Errors: " + errors + "\nSentences typed: " + i + "/" + sentences.length);
		// show the alert
		alert.show();
		// reset the variables
		errors = 0;
		timeleft = totaltime;
		i = 0;
		lblSentence.setText(sentences[0]);
		lblErrors.setText("Errors:" + errors);
		lblTimeLeft.setText("Time Left: " + timeleft);

	}

	public static void main(String[] args) {
		//launching the app
		launch(args);

	}

}