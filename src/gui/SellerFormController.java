package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable 
{
	//Classe é um clone da classe DepartmentFormController, trocamos todos os Department por Seller
	
	private Seller entity;
	private SellerService service;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
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
	
	public void setSeller(Seller entity) {
		this.entity = entity;
	}
	
	public void setSellerService(SellerService service) {
		this.service = service;
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
		Seller obj = new Seller();
		
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if(txtName.getText() == null || txtName.getText().trim().equals("")) 
		{
			exception.addError("name", "Field can't be empty");
		} 
		else 
		{
			obj.setName(txtName.getText());
		}
		
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
	}
	
	public void updateFormData()
	{	
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
	}
	
	private void setErrorMessages(Map<String,String> errors)
	{
		Set<String> fields = errors.keySet();
		
		if(fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}
}
