package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable 
{
	//Classe é um clone da classe DepartmentFormController, trocamos todos os Department por Seller
	
	private Seller entity;
	private SellerService service;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	private DepartmentService departmentService; //dependencia que vai buscar do banco de dados
	//departamento
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private DatePicker dptBirthDate;
	
	@FXML
	private TextField txtBaseSalary;
	
	@FXML
	private ComboBox<Department> comboBoxDepartment;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Label labelErrorEmail;
	
	@FXML
	private Label labelErrorBirthDate;
	
	@FXML
	private Label labelErrorBaseSalary;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	private ObservableList<Department> obsList; //esse obj vai carregar um lista de departmentos na lista do combo Box
	
	public void setSeller(Seller entity) {
		this.entity = entity;
	}
	
	public void setService(SellerService service, DepartmentService deparmentService) {
		/*ATUALIZAÇÃO FEITA NO METODO
		 * foi atulizado o nome e acrescentado mais um parametro, que é o departmentService
		 * portanto, assim que foi injetado o metodo, tera que passar como parametro um SellerService
		 * e um DepartmentService
		 * */
		this.service = service;
		this.departmentService = deparmentService;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event)
	{
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		try 
		{
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}
		catch(ValidationException e)
		{
			setErrorMessages(e.getErrors());   
		}
		catch(DbException e) 
		{
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR); //lança uma alert
		}
	}
	
	private void notifyDataChangeListeners() 
	{
		for(DataChangeListener listener : dataChangeListeners)
		{
			listener.onDataChanged();
		}
	}

	private Seller getFormData() 
	{
		//metodo responsavel por pegar os dados no formulario e carregar no obj seller
		Seller obj = new Seller();
	
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty"); //valida o campo name
		} 
		obj.setName(txtName.getText()); //caso esteja preenchido, seta o nome ao obj
		
		if(txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			exception.addError("email", "Field can't be empty"); //valida o campo email
		}
		obj.setEmail(txtEmail.getText()); //caso esteja preencido, seta o valor para o obj
		
		if(dptBirthDate.getValue() == null) {
			exception.addError("birthDate", "Field can't be empty"); //valida se o datePicker esta nulo
		}
		else {
			//pegando valor do datePicker, tem que ser um uma variavel do tipo Instant
			Instant instant = Instant.from(dptBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setBirthDate(Date.from(instant)); //Date.From() converte o instant por Date
		}
		
		if(txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {
			exception.addError("baseSalary", "Field can't be empty"); //valida se o campo esta vazio
		}
		obj.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText())); //caso contrario, seta o salario
		
		obj.setDepartment(comboBoxDepartment.getValue());
		
		if(exception.getErrors().size() > 0 ) {
			throw exception;
		}
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event)
	{
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes()
	{
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dptBirthDate, "dd/MM/yyyy"); //chmando a função que formata a data
		//nesse caso sera para dd/MM/yyyy
		
		initializeComboBoxDepartment();
	}
	
	public void updateFormData()
	{	
		/*Metodo responsavel por pegar os dados do objeto Seller e preencher na tela*/
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		
		Locale.setDefault(Locale.US); //para garantir que vai ser colocado o ponto e não virgula no txt abaixo
		txtBaseSalary.setText(String.format("%.2f",entity.getBaseSalary()));
		
		if(entity.getBirthDate() != null) {
			/*o DatePicker trabalho com o tipo LocalDate e sera NECESSARIO 
			 * CONVERTER A DATA que esta na entity para LOCALDATE, no banco de dados
			 * é salvado a data independente de seu fuso horario, mas para
			 * que data seja exibido na tela baseado na data carregada na entidade, vamos 
			 * usar a função ofInstant(entity.getBirthDate().toInstant) passando a entity e 
			 * convertendo para instant (instante) 
			 * 
			 * ZoneId.systemDefault() - pega o fuso horario da maquina 
			 */
			dptBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		//preenchendo a comboBox
		if(entity.getDepartment() == null) {
			/*caso o objeto não possua um departmento, então vai buscar
			 * o primeiro da lista do comboBox*/
			comboBoxDepartment.getSelectionModel().selectFirst();
		}
		else {
			//caso cotrario, vai passar o valor associado ao objeto 
			comboBoxDepartment.setValue(entity.getDepartment());
		}
	}
	
	public void loadAssociatedObjects() 
	{
		/*Metodo responsavel por chamar todos os departmentos do banco de dados
		 * e passar para lista (osbservableList) do combo box*/
		if(departmentService == null) {
			//programação defensiva, caso tenha esquecido de injetar o departmentService
			throw new IllegalStateException("DepartmentService was null");
		}
		
		List<Department> list = departmentService.findAll(); //aqui estamos pegando todos os departmentos do banco, metodo
		//findAll, esses departmentos sera salvo em uma lista do tipo Department
		obsList = FXCollections.observableArrayList(list); //aqui estamos passando a lista de departmentos encontrados
		//no banco e passando para a observableList da comboBox
		comboBoxDepartment.setItems(obsList);//aqui estamos setando a nossa lista obsList
	}
	
	private void setErrorMessages(Map<String,String> errors)
	{
		Set<String> fields = errors.keySet();
		/*ALTERAÇÃO RELAIZADA NA FUNÇÃO,
		 * removido os if's e colocado operador condicional ternario direto no setText. Portanto,
		 * cada label, verifica se o campo foi preencido
		 * fields.contains("campo") ? error.get("campo") - exibe o label com a mensagem de erro
		 *  : "" não mostra nada
		 * OU caso o usuario tenha esquecido de preencher e o mesmo volte a preencher mas esquece 
		 * o proximo campo... o label da msg de erro é apagado
		 * */
		labelErrorName.setText((fields.contains("name") ? errors.get("name") : ""));
		labelErrorEmail.setText((fields.contains("email") ? errors.get("email")  : ""));
		labelErrorBirthDate.setText((fields.contains("birthDate") ? errors.get("birthDate") : ""));
		labelErrorBaseSalary.setText((fields.contains("baseSalary") ? errors.get("baseSalary") : ""));	
	}
	
	
	private void initializeComboBoxDepartment()
	{
		//Metodo responsavel por fazer a inicialização do comboxBox, imprimindo o
		//item da lista pelo nome (getName()), isso porque que setamos valores da obsList
		//para comboBox, o Observable pega o toString da classe
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) 
			{
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}
}
