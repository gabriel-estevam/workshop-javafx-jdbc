package model.services;

import java.util.List;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService 
{
	
	//essa classe é responsavel por pegar as informações do banco de dados
	
	//declarado uma dependencia - DepartmentDao que ja esta sendo injetada pelo DaoFactory que é 
	//a classe que chama 
	private DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	public List<Department> findAll()
	{
		return dao.findAll();
	}
}
