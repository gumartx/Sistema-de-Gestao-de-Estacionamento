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
					"UPDATE parking_spot SET status = ?, vehicle_plate = ? WHERE spot_number = ?");

			st.setBoolean(1, obj.isStatus());
			try {
				st.setString(2, obj.getVehicle().getPlate());
			} catch (NullPointerException e) {
				st.setString(2, null);
			}
			st.setInt(3, obj.getNumber());
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

			Map<String, Vehicle> map = new HashMap<>();
			List<ParkingSpot> list = new ArrayList<>();

			while (rs.next()) {

				Vehicle dep = map.get(rs.getString("vehicle_plate"));

				if (dep == null) {
					dep = instantiateVehicle(rs);
					map.put(rs.getString("vehicle_plate"), dep);
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
			st = conn.prepareStatement("select parking_spot.* from parking_spot where vehicle_plate = ?");

			st.setString(1, obj.getPlate());
			rs = st.executeQuery();

			Map<String, Vehicle> map = new HashMap<>();
			List<ParkingSpot> list = new ArrayList<>();

			while (rs.next()) {

				Vehicle vec = map.get(rs.getString("vehicle_plate"));

				if (vec == null) {
					vec = instantiateVehicle(rs);
					map.put(rs.getString("vehicle_plate"), vec);
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
		Vehicle vehicle = vehicleDao.findByPlate(rs.getString("vehicle_plate"));
		return vehicle;
	}

	private ParkingSpot instantiateSpot(ResultSet rs, Vehicle vehicle) throws SQLException {
		ParkingSpot ps = new ParkingSpot(rs.getInt("spot_number"), rs.getBoolean("status"),
				Reserve.valueOf(rs.getString("reserve").toUpperCase()), vehicle);
		return ps;
	}

}
