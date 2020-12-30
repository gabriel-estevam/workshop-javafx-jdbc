package model.services;

import java.util.List;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService 
{
	
	//essa classe � responsavel por pegar as informa��es do banco de dados
	
	//declarado uma dependencia - DepartmentDao que ja esta sendo injetada pelo DaoFactory que � 
	//a classe que chama 
	private DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	public List<Department> findAll()
	{
		return dao.findAll();
	}
	
	public void saveOrUpdate(Department obj)
	{
		//essa opera��o verifica se o obj do tipo Department sera inserido ou atulizado no banco de dados
		//ent�o ele verifica se o obj tem um id nulo, caso tenha a opera��o do dao sera um insert
		//caso contrario � um update, atualiza��o, no banco de dados
		if(obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Department obj)
	{
		//fun��o para remover um departmento do banco de dados
		dao.deleteById(obj.getId());
	}
}
