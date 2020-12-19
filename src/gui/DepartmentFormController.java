package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DepartmentFormController implements Initializable{
	//Classe controladora da tela de Cadastro de departamentos
	
	//atributos da tela a serem controlados
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label  labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	@FXML
	public void onBtSaveAction()
	{
		//fun��o de a��o do bot�o de salvar, por enquanto retona uma msg no console
		System.out.println("onBtSaveAction");
	}
	
	@FXML
	public void onBtCancelAction()
	{
		//fun��o de a��o do bot�o de a��o de cancelar, por enquanto retorna uma msg no console
		System.out.println("onBtCancelAction");
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes()
	{
		//fun��o que colocar restrin��es na txtId e txtName
		//txtId so aceita Integer e txtName tem no maximo 30 caracteres, acima
		// n�o vai permitit
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
}
