package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable
{
	private DepartmentService service; //declarado uma dependencia na classe
	//essa dependencia tem ser injetada sem colocar a implementação da classe
	//para isso foi cria o metodo setDepartmentService()
	
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Department> obsList;
	
	@FXML
	public void onBtNewAction()
	{
		System.out.println("OnBtNewAction");
	}
	
	public void setDepartmentService(DepartmentService service)
	{
		//criamos uma inversão de controle, isto é, essa função
		//tem um injeção depencia, onde é passado como paramtro
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
		//metodo responsavel por acessar o serviço, carregar o departamento
		//e mostrar os departamentos na obserableList
		
		if(service == null)
		{
			//caso o programador esqueça de passar o serviço, ele lança uma 
			//excessão para que assim o programador perceba que esqueceu
			throw new IllegalStateException("Service was null");
		}
		List<Department> list = service.findAll(); //pega os dados do banco
		obsList = FXCollections.observableArrayList(list); //carrega os dados do banco na obslist
		tableViewDepartment.setItems(obsList); //seta os valores da obslist na tela
	}
}
