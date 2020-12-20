package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable{
	//Classe controladora da tela de Cadastro de departamentos
	
	private Department entity; //dependencia para o departamento, � a entidade relacionada a esse formulario
	
	private DepartmentService service;//dependencia para o departmentService, para usar as opera��es da classe
	//tem que injetar na classe DepartmentListController
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
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
	
	public void setDepartment(Department entity) {
		//Esse metodo recebe um department, tendo uma instancia do departamento
		this.entity = entity;
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		//fun��o responsavel por sobreescrever um objeto listener na lista
		//de DataChaangeListeners (lista de listeners)
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event)
	{
		//metodo passa a ter um parametro do tip ActionEvent, para fechar a janela
		
		if(entity == null) {
			//programa��o defensiva, caso o programador venha a esquecer de injetar a entidade
			//na classe controladora
			throw new IllegalStateException("Entity was null");
		}
		
		if(service == null) {
			//programa��o defensiva, caso o programador venha a esquecer de injetar o servi�o
			//na classe controladora
			throw new IllegalStateException("Service was null");
		}
		try 
		{
			/*O entity vai receber os dados da fun��o getformData, que � um novo obj com
			 os dados carregados do formul�rio.
			 Depois chama o servi�o passando como parametro o entity, salvando no banco de dados
			 em seguida fecha a janela
			 */
			
			entity = getFormData(); //recebe as informa��es
			service.saveOrUpdate(entity); //chama o servi�o com o banco de dados
			notifyDataChangeListeners();
			Utils.currentStage(event).close(); //fecha a tela
			//como � uma opera��o com o banco, ent�o pode gerar exception, ent�o 
			//foi necessario colocar em ty-cath, sendo o cath a exception personalida (DbException)
		}
		catch(DbException e) 
		{
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR); //lan�a uma alert
		}
	}
	
	private void notifyDataChangeListeners() 
	{
		/*Fun��o responsavel por "notificar" a lista de listener assim que
		 * um novo obj do tipo listener foi adcionado
		 */
		for(DataChangeListener listener : dataChangeListeners)
		{
			//foreach para percorre, atualizando a lista de listeners
			listener.onDataChanged();
		}
	}

	private Department getFormData() 
	{
		/*metodo responsavel por pegar os dados do formulario e passar para 
		 * o departamento
		 * ent�o instanciamos um Department
		 * em seguida chamamos a fun��o setId, usando a fun��o tryParToInt que foi
		 * criada na classe Utils para assim ativar o mecanismo da fun��o:
		 * caso o deparmento esteja null, passa como nulo, caso tenha valor, passa o valor
		 * para entidade.
		 * 
		 * Em seguida seta o nome e por fim retorna obj
		 */
		Department obj = new Department();
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setName(txtName.getText());
		
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event)
	{
		//o metodo agora tem um ActonEvent como parametro, para fechar a janela
		//ao clicar em cancelar
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes()
	{
		//fun��o que colocar restrin��es na txtId e txtName
		//txtId so aceita Integer e txtName tem no maximo 30 caracteres, acima
		// n�o vai permitir
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	public void updateFormData()
	{
		//Metodo responsavel por pegar os dados do departmento (entity) e passar para as caixinhas TextField
		
		if(entity == null) {
			//programa��o defensiva para verificar se entity esta nula, caso esteja
			//o programador esqueceu de aplicar a inje��o de dependecia, ent�o � lan�ado
			//um exception
			throw new IllegalStateException("Entity was null");
		}
		//passando os dados para as caixinhas (setText)
		
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}
}
