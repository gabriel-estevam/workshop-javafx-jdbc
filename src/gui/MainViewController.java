package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

public class MainViewController implements Initializable
{
	//Atributos da tela mainView
	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	
	/*as fun��es abaixo s�o as EventHandler, para o funcionamento delas � necessario
	 	*configurar esse classe de controle (mainViewController.java) no scenebuilder.
	 	*Para isso siga os passos:
	 	*1 bot�o deireito na classe mainView.fxml - selecionar open with scenebuilder;
	 	*2 no scenebuilder selecionar a aba "Controller" que fica no canto superior esquerdo;
	 	*3 No campo Controller Classe, selecione a classe controladora, por 
	 	*4 padr�o ele mostra o nome do pacote em seguida do nome da classe(gui.MainViewController)
	 	*ATEN��O: ao selecionar a classe e n�o aparecer nada, feche o scenebuilder e abra novamente 
	 	*seguindo o passo dois acima
	 */
	
	@FXML
	public void onMenuItemSellerAction()
	{
		System.out.println("onMenuItemSellerAction");
	}
	
	@FXML
	public void onMenuItemDepartmentAction()
	{
		System.out.println("onMenuItemDepartmentAction");
	}
	
	@FXML
	public void onMenuItemAboutAction()
	{
		System.out.println("onMenuItemAboutAction");
	}
	
	
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) 
	{
		
	}
	
}
