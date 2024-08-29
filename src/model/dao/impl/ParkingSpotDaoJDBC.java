package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.ParkingSpotDao;
import model.dao.VehicleDao;
import model.entities.ParkingSpot;
import model.entities.Vehicle;
import model.enums.Reserve;

public class ParkingSpotDaoJDBC implements ParkingSpotDao {

	private Connection conn;

	public ParkingSpotDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public ParkingSpot findByNumber(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {

			st = conn.prepareStatement("SELECT parking_spot.* FROM parking_spot WHERE spot_number = ?");

			st.setInt(1, id);

			rs = st.executeQuery();

			if (rs.next()) {
				Vehicle v = instantiateVehicle(rs);
				ParkingSpot ps = instantiateSpot(rs, v);
				return ps;
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
	public void update(ParkingSpot obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE parking_spot SET status = ?, vehicle_id = ?, license_plate = ? WHERE Id = ?");

			st.setBoolean(1, obj.isStatus());
			st.setInt(2, obj.getVehicle().getId());
			st.setString(3, obj.getVehicle().getPlate());
			st.setInt(4, obj.getId());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	private Vehicle instantiateVehicle(ResultSet rs) throws SQLException {
		VehicleDao vehicleDao = new VehicleDaoJDBC(conn);
		Vehicle vehicle = vehicleDao.findById(rs.getInt("vehicle_id"));
		return vehicle;
	}

	private ParkingSpot instantiateSpot(ResultSet rs, Vehicle vehicle) throws SQLException {
		ParkingSpot ps = new ParkingSpot(rs.getInt("id"), rs.getInt("spot_number"), rs.getBoolean("status"),
				Reserve.valueOf(rs.getString("reserve").toUpperCase()), vehicle);
		return ps;
	}

	@Override
	public List<ParkingSpot> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"select parking_spot.* from parking_spot order by number");

			rs = st.executeQuery();

			Map<Integer, Vehicle> map = new HashMap<>();
			List<ParkingSpot> list = new ArrayList<>();

			while (rs.next()) {

				Vehicle dep = map.get(rs.getInt("vehicle_id"));

				if (dep == null) {
					dep = instantiateVehicle(rs);
					map.put(rs.getInt("vehicle_id"), dep);
				}

				ParkingSpot obj = instantiateSpot(rs, dep);

				list.add(obj);
			}

			return list;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
