package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener
{
	private DepartmentService service; //declarado uma dependencia na classe
	//essa dependencia tem ser injetada sem colocar a implementa��o da classe
	//para isso foi cria o metodo setDepartmentService()
	
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private TableColumn<Department, Department> tableColumnEDIT;
	
	@FXML
	private TableColumn<Department,Department> tableColumnREMOVE;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Department> obsList;
	
	@FXML
	public void onBtNewAction(ActionEvent event)
	{
		/*Essa fun��o passa a ter um parametro do tipo ActionEvent, que sera fundamental
		 * para usarmos como parametro na fun��o currentStage() da classe Utils,
		 * Em seguida declaramos um objeto do tipo "Stage" que chama a a fun��o currentStage da classe Utils
		 * esse Stage basicamente tem todos os recursos da tela principal, que sera usado como parametro na fun��o
		 * createDialogForm
		 * Em seguida chamamos a fun��o createDialogForm(), passando como parametro o caminho da tela
		 * e o Stage da tela principal, que esta no parentStage,*/
		Stage parentStage = Utils.currentStage(event);
		/*ALTERA��O REALIZADA NA FUN��O
		 * agora assim que a fun��o for chamada, sera instanciado um novo objeto
		 * do tipo "Department" vazio, em seguida � passado como parametro na 
		 * fun��o createDialogForm, ficando como uma inje��o de dependencia*/
		
		Department obj = new Department();
		createDialogForm(obj,"/gui/DepartmentForm.fxml", parentStage);
	}
	
	public void setDepartmentService(DepartmentService service)
	{
		//criamos uma invers�o de controle, isto �, essa fun��o
		//tem um inje��o depencia, onde � passado como paramtro
		//um tipo DepartMnetService
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes()
	{
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		//macete para fazer o tableView acompanhar a largura da janela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView()
	{
		//metodo responsavel por acessar o servi�o, carregar o departamento
		//e mostrar os departamentos na obserableList
		
		if(service == null)
		{
			//caso o programador esque�a de passar o servi�o, ele lan�a uma 
			//excess�o para que assim o programador perceba que esqueceu
			throw new IllegalStateException("Service was null");
		}
		List<Department> list = service.findAll(); //pega os dados do banco
		obsList = FXCollections.observableArrayList(list); //carrega os dados do banco na obslist
		tableViewDepartment.setItems(obsList); //seta os valores da obslist na tela
		initEditButtons(); //carrega a fun��o que edita o registro do banco de dados
		initRemoveButtons(); //carrega a fun��o que remove um registro do banco de dados
	}
	
	private void createDialogForm(Department obj,String absoluteName,Stage parentStage)
	{
		/*fun��o responsavel pela cria��o de uma nova tela.
		 * Tem como parametro o caminho da tela (deve ser informada pelo programador), e um 
		 * Stage "parentStage", esse parametro sera fundamental, alem de ser a tela principal,
		 * vamos usar como parametro na fun��o initOnwer() essa fun��o nos retorna quem � o 
		 * stage pai da janela que esta sendo criada.*/
		try 
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName)); //instanciamos um FXML
			Pane pane = loader.load(); //declarado um obj do tipo Pane que recebe os recursos do obj "loader"
			
			/*dialogStage
			 * Abaixo estamos criando um novo palco (nova janela), nele configuramos todos os parametros
			 * necessario para o seu funcionamento, sendo eles: 
			 * Titulo, cria��o de um novo palco (que � a tela), definimos que n�o sera redimensionavel,
			 * definimos que � seu owner (stage pai da tela atual), definomos que sera modal, isto � 
			 * quando o usuario clicar fora da tela n�o sera permitido, e por fim assim que a tela for
			 * chamada sera mantida ate que seja fechada pelo usu�rio 
			 * */
			
			/*ALTERA��O FEITA NA FUN��O
			 * agora a fun��o tem uma dependencia do tipo "Department" como parametro
			 
			 As linhas abaixo s�o a inje��o de dependencia da classe "Department"
			 ent�o foi criado uma referencia do tipo "DepartmentFormController" - controller
			 essa referencia pega os controladores da tela e em seguida injeta a dependencia
			 Fazendo um setDepartment(obj) passando o "Department" do parametro
			 e em seguida um updateFormData, que � captura para os txtField Id e Name
			 */
			
			/*ALTERA��O FEITA NA FUN��O
			 * agora a fun��o tem inje��o de dependencia com DepartmentService
			 */
			
			DepartmentFormController controller = loader.getController();
			controller.setDepartment(obj);
			controller.setDepartmentService(new DepartmentService()); //injeta a dependencia do seriv�o
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();
			
			Stage dialogStage = new Stage(); //objeto responsavel pela tela
			dialogStage.setTitle("Enter Department data"); //titulo da tela
			dialogStage.setScene(new Scene(pane)); // novo palco
			dialogStage.setResizable(false); //n�o redimensionavel
			dialogStage.initOwner(parentStage); //informamos o pai da tela atual
			dialogStage.initModality(Modality.WINDOW_MODAL); //sera modal
			dialogStage.showAndWait(); //mantem a tela apos ser chamada
		}
		catch(IOException e)
		{
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() 
	{
		/*A implementa��o chama a fun��o updateTableView() que � responsavel por atualizar os dados
		 * da tabela*/
		updateTableView();
	}
	
	
	private void initEditButtons()
	{
		/*Essa fun��o cria um bot�o para cada linha (registro) da tabela department*/
		
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("edit");
			
			@Override
			protected void updateItem(Department obj, boolean empty)
			{
				super.updateItem(obj, empty);
				
				if(obj == null)
				{
					setGraphic(null);
					return;
				}
				
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
			}
		});
		
		
	}
	
	private void initRemoveButtons()
	{
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Department, Department>(){
			private final Button button = new Button("remove");
			
			@Override
			protected void updateItem(Department obj, boolean empty)
			{
				super.updateItem(obj, empty);
				
				if(obj == null) 
				{
					setGraphic(null);
					return;
				}
				
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Department obj) 
	{
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		if(result.get() == ButtonType.OK)
		{
			if(service == null)
			{
				throw new IllegalStateException("Service was null");
			}
			try 
			{
				service.remove(obj);
				updateTableView();
			}
			catch(DbIntegrityException e)
			{
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}
