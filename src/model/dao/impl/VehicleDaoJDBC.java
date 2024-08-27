package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import db.DB;
import db.DbException;
import model.dao.VehicleDao;
import model.entities.Vehicle;

public class VehicleDaoJDBC implements VehicleDao {

	private Connection conn;

	public VehicleDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Vehicle obj) {
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO vehicle (license_plate, category) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getPlate());
			st.setString(2, obj.getCategory().name());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	
	
}
