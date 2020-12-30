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
	
	public void saveOrUpdate(Department obj)
	{
		//essa operação verifica se o obj do tipo Department sera inserido ou atulizado no banco de dados
		//então ele verifica se o obj tem um id nulo, caso tenha a operação do dao sera um insert
		//caso contrario é um update, atualização, no banco de dados
		if(obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Department obj)
	{
		//função para remover um departmento do banco de dados
		dao.deleteById(obj.getId());
	}
}
