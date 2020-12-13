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
		loadView("/gui/DepartmentList.fxml");
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
		/*Função para carregar a tela de about
		 	* essa função tem como parametro o caminho para o fxml da tela about
		 	* que vai ser informada pelo programador;
		 	* 
		 	*  Seu funcionamento esta incluido dentro de um bloco try-catch, pois
		 	*  pode gerar uma exceção, essa exceção esta sendo tratada pela nossa classe
		 	*  Constraints.java, que vai retornar um alert;
		 	*  
		 	*   Na função mainView foi necessario criar um atributo do tipo static que 
		 	*   vai contem a tela principal (scene) e tambem foi cria uma função para
		 	*  retornar essa tela, tambem do tipo static é do tipo Scene;
		 	*  
		 	*Apatir desse analogia não sera necessario carregar uma outra tela sobrepondo a "mainView",
		 	* pois a logica fica contida em pegar o conteudo da tela de about e 
		 	* inserir na tela principal, ficando apenas uma tela em produção
		 	*
		 	*  FXMLLoader "loader" - objeto responsavel por carregar os recursos da dela
		 	*  sendo o caminho aonde esta salvo o "fxml" ate sua criação em "fmxl"
		 	*  
		 	*  VBox newVBox - objeto responsavel por mostra (carregar) a tela no tipo "VBox"
		 	*
		 	*	Scene "MainScene" - objeto responsavel por pegar os recursos da "mainView"
		 	*	
		 	*	VBox "mainVBox" - objeto responsavel por pegar os conteudos da "mainview"
		 	*
		 	*	Node "mainMenu" - objeto responsavel por pegar os filhos (tags) 
		 	*	
		 */
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
}
