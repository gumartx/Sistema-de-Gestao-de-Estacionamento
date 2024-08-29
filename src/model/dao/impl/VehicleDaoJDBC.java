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
import model.enums.Category;
import model.enums.VehicleType;

public class VehicleDaoJDBC implements VehicleDao {

	private Connection conn;

	public VehicleDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Vehicle obj) {
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO vehicle (license_plate, category, type) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getPlate());
			st.setString(2, obj.getCategory().name());
			st.setString(3, obj.getType().name());

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

	@Override
	public Vehicle findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {

			st = conn.prepareStatement("select vehicle.* from vehicle where Id = ?");

			st.setInt(1, id);

			rs = st.executeQuery();

			if (rs.next()) {
				Vehicle vehicle = instantiateVehicle(rs);
				return vehicle;
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

		return null;
	}
	
	@Override
	public Vehicle findByPlate(String id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {

			st = conn.prepareStatement("select vehicle.* from vehicle where license_plate = ?");

			st.setString(1, id);

			rs = st.executeQuery();

			if (rs.next()) {
				Vehicle vehicle = instantiateVehicle(rs);
				return vehicle;
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

		return null;
	}

	private Vehicle instantiateVehicle(ResultSet rs) throws SQLException {
		Vehicle vehicle = new Vehicle(rs.getInt("id"), rs.getString("license_plate"), Category.valueOf(rs.getString("category").toUpperCase()), VehicleType.valueOf(rs.getString("type").toUpperCase()));
		return vehicle;
	}

	
	
}
