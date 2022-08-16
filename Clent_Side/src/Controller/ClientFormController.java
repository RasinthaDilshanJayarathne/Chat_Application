package Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.Socket;

public class ClientFormController {
    public static String userName;
    public AnchorPane clientContext;
    public Label lblName;
    public TextField txtType;
    //public TextArea txtChatArea;
    public Button btnSend;
    public Button btnCamera;
    public Button btnEmoji;
    public ImageView imgProfile;
    public ComboBox<String> cmbInfo;
    public ScrollPane scrollPane;
    public VBox messageText;
    public boolean saveControl = false;
    BufferedReader reader;

    BufferedWriter writer;
    Socket socket = null;

    public void setData(String name) {
        lblName.setText(name);
    }


    public void initialize() {

//        cmbInfo.getItems().addAll(
//                "Logout"
//        );
//
//        cmbInfo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue!=null){
//                if (newValue.equals("Logout")){
//                    Stage window = (Stage) clientContext.getScene().getWindow();
//                    try {
//                        window.setScene(new Scene( FXMLLoader.load(getClass().getResource("../View/Login.fxml"))));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
        try {
            this.socket = new Socket("localhost", 9000);
            //System.out.println("Socket is connected with server!");
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.writer.write(userName);
            this.writer.newLine();
            this.writer.flush();

            new Thread(() -> {
                String msg;
                try {
                    while (true) {
                        msg = reader.readLine();
                        System.out.println("Message :"+msg);

                        if (msg.startsWith("IMG")) {

                            System.out.println("AAAAAAAAAAAAAAA");

                            String replace = msg.replace("IMG", " ");
                            String[] split = replace.split("=");

                            System.out.println(split[0]);
                            System.out.println(split[1]);

                            HBox hBox = new HBox();
                            hBox.setAlignment(Pos.CENTER_LEFT);
                            hBox.setPadding(new Insets(5, 5, 5, 10));

                            Text text1 = new Text(split[0] + " : ");
                            TextFlow textFlow1 = new TextFlow(text1);
                            textFlow1.setStyle("-fx-font-weight: bold;" + "-fx-background-color:#aab1af;");
                            textFlow1.setPadding(new Insets(5, 10, 5, 10));
                            text1.setFill(Color.color(1, 1, 1, 1));

                            ImageView imageView = new ImageView();
                            //Setting image to the image view
                            imageView.setImage(new Image(new File(split[1]).toURI().toString()));
                            //Setting the image view parameters
                            imageView.setFitWidth(300);
                            imageView.setPreserveRatio(true);

                            hBox.getChildren().add(textFlow1);
                            hBox.getChildren().add(imageView);

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    messageText.getChildren().add(hBox);
                                }
                            });

                        }else {
                            HBox hBox = new HBox();
                            hBox.setAlignment(Pos.CENTER_LEFT);
                            hBox.setPadding(new Insets(5, 5, 5, 10));

                            Text text = new Text(msg);
                            TextFlow textFlow = new TextFlow(text);

                            textFlow.setStyle("-fx-font-weight: bold;" + "-fx-background-color:#aab1af;" + "-fx-background-radius:10px");

                            textFlow.setPadding(new Insets(5, 10, 5, 10));
                            text.setFill(Color.color(1, 1, 1, 1));
                            hBox.getChildren().add(textFlow);

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    messageText.getChildren().add(hBox);
                                }
                            });
                        }
                    }

                } catch (IOException e) {
//                    try {
//                        reader.close();
//                        writer.close();
//                        socket.close();
//                    } catch (IOException e1) {
//                        e1.printStackTrace();
//                    }
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendOnAction(ActionEvent event) throws IOException {
        String msg = txtType.getText();

//        txtChatArea.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
//        txtChatArea.appendText("Me: " + msg + "\n\n");
//        txtType.setText("");
//        if(msg.equalsIgnoreCase("Bye") || msg.equalsIgnoreCase("Exit")) {
//            System.exit(0);
//        }

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text("Me: " + msg);
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle("-fx-font-weight: bold;" + "-fx-background-color:#10ac84;" + "-fx-background-radius:10px");

        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(1, 1, 1, 1));
        hBox.getChildren().add(textFlow);
        messageText.getChildren().add(hBox);

        writer.write(userName + " : " + msg);
        writer.newLine();
        writer.flush();
        txtType.clear();
    }

    public void imageOnAction(ActionEvent event) throws IOException {

//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        fileChooser = new FileChooser();
//        fileChooser.setTitle("Open Image");
//        this.filePath = fileChooser.showOpenDialog(stage);
//        saveControl = true;

        //saveImage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a Image");
        File file = fileChooser.showOpenDialog(null);

        writer.write("IMG" + userName + " =" + file.getPath());
        writer.newLine();
        writer.flush();

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text("Me : ");
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-font-weight: bold;" + "-fx-background-color:#10ac84;");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(1, 1, 1, 1));


        ImageView imageView = new ImageView();
        //Setting image to the image view
        imageView.setImage(new Image(new File(file.getPath()).toURI().toString()));
        //Setting the image view parameters
        imageView.setFitWidth(300);
        imageView.setPreserveRatio(true);

        hBox.getChildren().add(textFlow);
        hBox.getChildren().add(imageView);

        messageText.getChildren().add(hBox);
    }

//    public void saveImage() {
//        if (saveControl) {
//
//            try {
//                BufferedImage bufferedImage = ImageIO.read(filePath);
//                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
//                ImageView imageView = new ImageView();
//                imageView.setImage(image);
//                imageView.setFitHeight(30);
//                imageView.setFitWidth(30);
//                saveControl = false;
//                writer.println(lblName.getText() + ": " + imageView.getImage());
//                txtChatArea.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
//                txtChatArea.appendText("Me: " + imageView.getImage() + "\n\n");
//
//
//                BufferedImage bi = bufferedImage;
//                File outputfile = new File("saved.png");
//                ImageIO.write(bi, "png", outputfile);
//
//            } catch (IOException exception) {
//                exception.printStackTrace();
//            }
//
//
//        }
//    }

    public void emojiOnAction(ActionEvent event) {

    }
}
