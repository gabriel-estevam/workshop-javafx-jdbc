package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
import model.services.SellerService;

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
		loadView("/gui/SellerList.fxml", (SellerListController controller) -> {
			controller.setSellerService(new SellerService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemDepartmentAction()
	{
		/*Conforme a mudança da função loaderView, agora na chamada desse metodo, é setado
		 * um objeto do tipo DepartmentListController, em forma de expressão lambda.
		 * Atraves desse objeto vamos acessar a função setDepartmentService que vai instanciar a classe
		 * DepartmentService que é a classe, que por enquanto encontra-se um arrayList com  dados
		 * 
		 * Na sequencia o mesmo objeto vai chamar a função updateTableView da classe DepartmentListController
		 * essa função é resposavel por mostrar os dados na tableView*/
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemAboutAction()
	{
		loadView("/gui/About.fxml", x -> {});
	}
	
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) 
	{
		
	}
	
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction)
	{
		try
		{
			/*ALTERADO OS PARAMETROS DA FUNÇÃO loadView
			 * Agora a função loadView possui um segundo parametro -  "initializingAction" que é do tipo Consumer
			 * que é genérica, portanto no segundo parametro vamos informar qualquer função em forma de expressão
			 * lambda a quem tiver chamando a função em algum metodo*/
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
			
			//as duas linhas abaixo executa a função informada no segundo parametro 
			T controller = loader.getController();
			initializingAction.accept(controller);
		}
		catch(IOException e)
		{
			Alerts.showAlert("IO Exception", "Error on loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	
}
