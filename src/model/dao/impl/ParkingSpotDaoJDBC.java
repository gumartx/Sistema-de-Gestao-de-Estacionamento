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
import model.dao.DaoFactory;
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
			try {
				st.setInt(2, obj.getVehicle().getId());
				st.setString(3, obj.getVehicle().getPlate());
			} catch (NullPointerException e) {
				st.setString(2, null);
				st.setString(3, null);
			}
			st.setInt(4, obj.getId());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public List<ParkingSpot> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"select parking_spot.* from parking_spot where status = false order by spot_number");

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

	@Override
	public List<ParkingSpot> findByVehicle(Vehicle obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("select parking_spot.* from parking_spot where vehicle_id = ?");

			st.setInt(1, obj.getId());
			rs = st.executeQuery();

			Map<Integer, Vehicle> map = new HashMap<>();
			List<ParkingSpot> list = new ArrayList<>();

			while (rs.next()) {

				Vehicle vec = map.get(rs.getInt("vehicle_id"));

				if (vec == null) {
					vec = instantiateVehicle(rs);
					map.put(rs.getInt("vehicle_id"), vec);
				}

				ParkingSpot ps = instantiateSpot(rs, vec);

				list.add(ps);
			}

			return list;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Vehicle instantiateVehicle(ResultSet rs) throws SQLException {
		VehicleDao vehicleDao = DaoFactory.createVehicleDao();
		Vehicle vehicle = vehicleDao.findById(rs.getInt("vehicle_id"));
		return vehicle;
	}

	private ParkingSpot instantiateSpot(ResultSet rs, Vehicle vehicle) throws SQLException {
		ParkingSpot ps = new ParkingSpot(rs.getInt("id"), rs.getInt("spot_number"), rs.getBoolean("status"),
				Reserve.valueOf(rs.getString("reserve").toUpperCase()), vehicle);
		return ps;
	}

}
