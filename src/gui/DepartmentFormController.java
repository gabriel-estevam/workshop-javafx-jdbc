package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable{
	//Classe controladora da tela de Cadastro de departamentos
	
	private Department entity; //dependencia para o departamento, é a entidade relacionada a esse formulario
	
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
		//função de ação do botão de salvar, por enquanto retona uma msg no console
		System.out.println("onBtSaveAction");
	}
	
	@FXML
	public void onBtCancelAction()
	{
		//função de ação do botão de ação de cancelar, por enquanto retorna uma msg no console
		System.out.println("onBtCancelAction");
	}
	
	public void setDepartment(Department entity) {
		//Esse metodo recebe um department, tendo uma instancia do departamento
		this.entity = entity;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes()
	{
		//função que colocar restrinções na txtId e txtName
		//txtId so aceita Integer e txtName tem no maximo 30 caracteres, acima
		// não vai permitir
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	public void updateFormData()
	{
		//Metodo responsavel por pegar os dados do departmento (entity) e passar para as caixinhas TextField
		
		if(entity == null) {
			//programação defensiva para verificar se entity esta nula, caso esteja
			//o programador esqueceu de aplicar a injeção de dependecia, então é lançado
			//um exception
			throw new IllegalStateException("Entity was null");
		}
		//passando os dados para as caixinhas (setText)
		
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}
}
