package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class MainViewController implements Initializable
{
	//Atributos da tela mainView
	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemSellerAction()
	{
		System.out.println("onMenuItemSellerAction");
	}
	
	@FXML
	public void onMenuItemDepartmentAction()
	{
		loadView2("/gui/DepartmentList.fxml");
	}
	
	@FXML
	public void onMenuItemAboutAction()
	{
		loadView("/gui/About.fxml");
	}
	
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) 
	{
		
	}
	
	private synchronized void loadView(String absoluteName)
	{
		try
		{
			//carrega o fxml
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			
			VBox newVBox = loader.load(); //objeto do tipo VBox para carregar a cena
			
			Scene mainScene = Main.getMainScene(); //"pega" a tela principal
			
			//esse objeto é responsavel por pegar todos os recursos da tela princiapl
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0); //"pega" os filhos da tela principal (as tags <>)
			
			mainVBox.getChildren().clear(); //limpa a tela principal
			mainVBox.getChildren().add(mainMenu); //adiciona a nova tela ja com o conteudo do "mainAbout"
			mainVBox.getChildren().addAll(newVBox.getChildren()); //passa o novos filhos(tags) da tela about
		}
		catch(IOException e)
		{
			Alerts.showAlert("IO Exception", "Error on loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	
	private synchronized void loadView2(String absoluteName)
	{
		//função temporaria por enquanto
		try
		{
			//carrega o fxml
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			
			VBox newVBox = loader.load(); //objeto do tipo VBox para carregar a cena
			
			Scene mainScene = Main.getMainScene(); //"pega" a tela principal
			
			//esse objeto é responsavel por pegar todos os recursos da tela princiapl
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0); //"pega" os filhos da tela principal (as tags <>)
			
			mainVBox.getChildren().clear(); //limpa a tela principal
			mainVBox.getChildren().add(mainMenu); //adiciona a nova tela ja com o conteudo do "mainAbout"
			mainVBox.getChildren().addAll(newVBox.getChildren()); //passa o novos filhos(tags) da tela about
			
			DepartmentListController controller = loader.getController();
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		}
		catch(IOException e)
		{
			Alerts.showAlert("IO Exception", "Error on loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
