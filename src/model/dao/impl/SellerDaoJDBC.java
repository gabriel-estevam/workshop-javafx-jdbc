package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	
	private Connection conn; //objeto de conexão com o banco para realizar as operações
	
	public SellerDaoJDBC(Connection conn)
	{
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller obj) 
	{
		PreparedStatement st = null;
		
		try
		{
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ " VALUES "
					+ " (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = st.executeUpdate();
			if(rowsAffected > 0)
			{
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next())
				{
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else
			{
				throw new DbException("Unexpected error! No rows affected");
			}
		}
		catch(SQLException e)
		{
			throw new DbException(e.getMessage());
		}
		finally
		{
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Seller obj) 
	{
		PreparedStatement st = null;
		
		try 
		{
			st = conn.prepareStatement(
					"UPDATE seller "
					+ " SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " 
					+ " WHERE id = ?" 
					);
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();
		}
		catch(SQLException e)
		{
			throw new DbException(e.getMessage());
		}
		finally
		{
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) 
	{
		PreparedStatement st = null;
		try
		{
			st = conn.prepareStatement(
					" DELETE FROM seller "
					+ " WHERE id = ?");
			st.setInt(1, id);
			st.executeUpdate();
		}
		catch(SQLException e)
		{
			throw new DbException(e.getMessage());
		}
		finally
		{
			DB.closeStatement(st);
		}
	}

	@Override
	public Seller findById(Integer id) 
	{
		PreparedStatement st = null;
		ResultSet rs = null;
		try
		{
			st = conn.prepareStatement(
					" SELECT seller.*, department.Name as DepName "
					+ 
					" FROM seller INNER JOIN department " 
					+ 
					" ON seller.DepartmentId = department.Id " 
					+ 
					"WHERE seller.Id = ?;"
					);
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next())
			{
				Department dep = instantiateDepartment(rs);
				Seller obj = instantiateSeller(rs, dep);
				return obj;
			}
			return null;
		}
		catch(SQLException e)
		{
			throw new DbException(e.getMessage());
		}
		finally
		{
			//nesse caso não fechamos a conexão com o banco, porque em um mesmo
			//objeto dao pode haver outras operações sendo chamada, como por 
			//exemplo de inserção
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException 
	{
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException 
	{
		Department dep =  new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() 
	{
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try
		{
			st = conn.prepareStatement(
					" SELECT seller.*, department.Name as DepName " 
					+ 
					" FROM seller INNER JOIN department " 
					+ 
					" ON seller.DepartmentId = department.id " 
					+ 
					" ORDER BY Name; "
					);
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			while(rs.next())
			{
				Department dep = map.get(rs.getInt("DepartmentId"));
				if(dep == null)
				{
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
			}
			return list;
		}
		catch(SQLException e)
		{
			throw new DbException(e.getMessage());
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) 
	{
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try
		{
			st = conn.prepareStatement(" SELECT seller.*, department.Name as DepName " 
					+ 
					" FROM seller INNER JOIN department " 
					+ 
					" ON seller.DepartmentId = department.Id " 
					+ 
					" WHERE DepartmentId = ? " 
					+ 
					" ORDER BY Name; ");
			st.setInt(1, department.getId());
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>(); 
			while(rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null)
				{
					//essa bloco contra a instancia da classe department, para não ter que ficar instanciando para cada seller
					//so vai instanciar um department quando ele for nulo, isto é ainda não tenha sido instanciado antes
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
				
				/*A estrutura de busca por id de departamento é um pouco diferente
				 * Nesse caso um departamento pode ter 0 ou N Seller, portanto 
				 * a instacia do departamento deve ser feito uma vez para um
				 * grupo de seller que nela (departamento) trabalha.
				 * Para isso temos que controlar a instancia do departamento
				 * visto que, vamos ter uma estrutura que vai verificar quando 
				 * ja tiver uma instancia para um determinado departemento
				 * 
				 * Nesse caso, a forma que desenvolvemos ultilza um hash map
				 * onde ele vai verificar quando um departamento ja foi instanciado
				 * 
				 * Por exemplo, caso uma instancia departamento não tenho sido instanciado
				 * ele retorna null, é ai que devemos instanciar
				 * */
			}
			return list;
		}
		catch(SQLException e)
		{
			throw new DbException(e.getMessage());
		}
	}

}
