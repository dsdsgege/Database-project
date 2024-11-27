package com.teamsportsdb.ui;

import com.teamsportsdb.utils.Database;
import com.teamsportsdb.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComplexQueryController {

    @FXML
    private GridPane gridPane;

    @FXML
    private Label errorMessage,kettosLabel;

    @FXML
    private Button kettosButton, meccsekButton, meccsekExecute;

    private ArrayList<CheckBox> checkBoxList = new ArrayList<>();

    public void onBackButtonAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/teamsportsdb/Main.fxml"));
        try {
            Scene mainScene = new Scene(fxmlLoader.load(), Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());
            SceneManager.getPrimaryStage().setScene(mainScene);
        } catch (IOException e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }
    }

    public void onKettosButtonAction() {
        String query = "SELECT nev FROM tag WHERE tag_id IN (SELECT tag_id FROM tag_allampolgarsag GROUP BY tag_id HAVING count(allampolgarsag) > 1)";
        try (PreparedStatement preparedStatement = Database.getConnection().prepareStatement(query)) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                GridPane gp = new GridPane();
                gp.setHgap(10);
                gp.setVgap(10);
                gp.setAlignment(Pos.CENTER);
                Label elsosor = new Label("Kettős állampolgár(ok) neve(i):");

                elsosor.setStyle("-fx-border-color:black; -fx-border-width: 2px; -fx-padding: 5px; -fx-font-size: 16px");
                gp.add(elsosor,0,0);
                int row = 1;
                while(rs.next()) {
                    Label lb = new Label();
                    lb.setText(rs.getString("nev"));
                    lb.setStyle("-fx-border-color:grey; -fx-border-width: 1px; -fx-padding: 2px;");
                    gp.add(lb,0,++row);
                }
                Scene queryScene = new Scene(gp);
                Stage queryStage = new Stage();
                queryStage.setScene(queryScene);
                queryStage.show();
                errorMessage.setVisible(false);
            } catch (SQLException e) {
                errorMessage.setText(e.getMessage());
                errorMessage.setVisible(true);
            }


        } catch (SQLException e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }
    }

    public void onMeccsekButton() {
        errorMessage.setVisible(false);
        int rowIndex = 2;
        String sql = "SELECT nev FROM csapat";
        try (ResultSet rs = Database.getConnection().prepareStatement(sql).executeQuery()) {
            while(rs.next()) {
                CheckBox checkBox = new CheckBox(rs.getString(1));
                gridPane.add(checkBox,2,rowIndex++);
                checkBoxList.add(checkBox);
            }
            meccsekExecute.setVisible(true);
        } catch (SQLException e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }
    }

    public void onMeccsekExecuteAction() {
        ArrayList<String> teams = new ArrayList<>();
        errorMessage.setVisible(false);
        if(checkBoxList.size() == 0) {
            errorMessage.setText("Nincsenek kijelölendő csapatok!");
            errorMessage.setVisible(true);
            return;
        }

        boolean checked = false;
        for (CheckBox ch : checkBoxList) {
            if(ch.isSelected()) {
                checked = true;
                teams.add(ch.getText());
            }
        }

        if(!checked) {
            errorMessage.setText("Legalább egy csapatot válassz!");
            errorMessage.setVisible(true);
            return;
        }
        String teamsInString = "'";
        teamsInString += String.join("', '",teams);
        teamsInString += "'";
        String query = "SELECT csapat.nev AS nev, count(merkozik.merkozes_id) AS merkozesek FROM merkozes INNER JOIN merkozik ON merkozes.merkozes_id = merkozik.merkozes_id INNER JOIN csapat ON csapat.csapat_id = merkozik.csapat_id WHERE csapat.nev IN (%s) GROUP BY csapat.nev".formatted(teamsInString);
        try (PreparedStatement preparedStatement = Database.getConnection().prepareStatement(query)) {
            try {
                ResultSet rs = preparedStatement.executeQuery();
                GridPane gp = new GridPane();
                gp.setHgap(10);
                gp.setVgap(10);
                gp.setAlignment(Pos.CENTER);
                Label nev = new Label("Csapat név:");
                Label meccsek = new Label("Meccsek száma:");

                nev.setStyle("-fx-border-color:black; -fx-border-width: 2px; -fx-padding: 5px; -fx-font-size: 16px");
                meccsek.setStyle("-fx-border-color:black; -fx-border-width: 2px; -fx-padding: 5px; -fx-font-size: 16px");

                gp.add(nev,0,0);
                gp.add(meccsek,1,0);
                int rowIndex = 1;
                while(rs.next()) {
                    Label csapatNev = new Label(rs.getString("nev"));
                    Label merkozesekSzama = new Label(rs.getString("merkozesek"));
                    gp.add(csapatNev,0,rowIndex);
                    gp.add(merkozesekSzama,1,rowIndex++);
                }
                Scene newScene = new Scene(gp);
                Stage newStage = new Stage();
                newStage.setScene(newScene);
                newStage.show();
            } catch (SQLException e) {
                errorMessage.setText(e.getMessage() + teamsInString);
                errorMessage.setVisible(true);
            }
        } catch (SQLException e) {
            errorMessage.setText(e.getMessage());
            errorMessage.setVisible(true);
        }
    }
}