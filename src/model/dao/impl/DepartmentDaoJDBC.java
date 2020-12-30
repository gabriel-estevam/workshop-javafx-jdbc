package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao
{
	private static Connection conn;
	
	public DepartmentDaoJDBC(Connection conn)
	{
		this.conn = conn;
	}
	
	@Override
	public void insert(Department obj) 
	{
		PreparedStatement st = null;
		try
		{
			st = conn.prepareStatement("INSERT INTO department(name) VALUES(?)" 
					,Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getName());
			int rowsAffected = st.executeUpdate();
			if(rowsAffected > 0)
			{
				ResultSet rs = st.getGeneratedKeys();
				while(rs.next())
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
	public void update(Department obj) 
	{
		PreparedStatement st = null;
		try 
		{
			st = conn.prepareStatement("UPDATE department SET Name = ? WHERE Id = ?");
			
			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());
			st.executeUpdate();
		} 
		catch (SQLException e) 
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
			st = conn.prepareStatement("DELETE FROM department WHERE Id = ? ");
			st.setInt(1, id);
			int rows = st.executeUpdate();
			/*Esse estrutura é complementar, onde e lançado uma exception caso o Id não exista
			 * optei por não implentar, pois em um cenario com um interface grafica não seria necessario
			 * if(rows == 0)
			{
				throw new DbException("no rows affected! the id doesn't exist");
			}*/
		}
		catch(SQLException e)
		{
			throw new DbIntegrityException(e.getMessage());
		}
		finally
		{
			DB.closeStatement(st);
		}
	}

	@Override
	public Department findById(Integer id) 
	{
		PreparedStatement st = null;
		ResultSet rs = null;
		try
		{
			st = conn.prepareStatement(
				"SELECT * FROM department WHERE Id = ?");
			st.setInt(1, id);
			
			rs = st.executeQuery();
			while(rs.next())
			{
				Department obj = new Department();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
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
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Department> findAll() 
	{
		PreparedStatement st = null;
		ResultSet rs = null;
		try
		{
			st = conn.prepareStatement("SELECT * FROM department;");
			rs = st.executeQuery();
			List<Department> list = new  ArrayList<>();
			while(rs.next())
			{
				Department obj = new Department();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
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
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
