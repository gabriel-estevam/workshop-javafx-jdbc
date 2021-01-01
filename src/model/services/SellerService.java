package model.services;

import java.util.List;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService 
{
	
	//essa classe � responsavel por pegar as informa��es do banco de dados
	
	//declarado uma dependencia - SellerDao que ja esta sendo injetada pelo DaoFactory que � 
	//a classe que chama 
	private SellerDao dao = DaoFactory.createSellerDao();
	
	public List<Seller> findAll()
	{
		return dao.findAll();
	}
	
	public void saveOrUpdate(Seller obj)
	{
		//essa opera��o verifica se o obj do tipo Seller sera inserido ou atulizado no banco de dados
		//ent�o ele verifica se o obj tem um id nulo, caso tenha a opera��o do dao sera um insert
		//caso contrario � um update, atualiza��o, no banco de dados
		if(obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Seller obj)
	{
		//fun��o para remover um departmento do banco de dados
		dao.deleteById(obj.getId());
	}
}
