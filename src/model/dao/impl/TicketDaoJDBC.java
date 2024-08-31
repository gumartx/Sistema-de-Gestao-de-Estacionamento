package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.DaoFactory;
import model.dao.GateDao;
import model.dao.ParkingSpotDao;
import model.dao.TicketDao;
import model.dao.VehicleDao;
import model.entities.Gate;
import model.entities.Ticket;
import model.entities.Vehicle;

public class TicketDaoJDBC implements TicketDao {

	private Connection conn;

	public TicketDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void update(Ticket obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE ticket SET exit_gate_id = ?, exit_time = ?, amount_paid = ? WHERE id = ?");

			st.setInt(1, obj.getExitGate().getId());
			st.setTimestamp(2, Timestamp.valueOf(obj.getExitTime()));
			st.setDouble(3, obj.getAmountPaid());
			st.setInt(4, obj.getId());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void insert(Ticket obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO ticket (vehicle_plate, parking_spot, entry_gate_id, entry_time) VALUES (?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getVehicle().getPlate());
			st.setInt(2, obj.getSpots().stream().map(x -> x.getNumber()).findFirst().get());
			st.setInt(3, obj.getEntryGate().getId());
			st.setTimestamp(4, Timestamp.valueOf(obj.getEntryTime()));

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
	public List<Ticket> findByVehicle(Vehicle vehicle) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("select ticket.* from ticket where vehicle_plate = ?");

			st.setString(1, vehicle.getPlate());
			rs = st.executeQuery();

			Map<Integer, Vehicle> map = new HashMap<>();
			List<Ticket> list = new ArrayList<>();

			while (rs.next()) {

				Vehicle vec = map.get(rs.getInt("vehicle_id"));

				if (vec == null) {
					vec = instantiateVehicle(rs);
					map.put(rs.getInt("vehicle_id"), vec);
				}

				Ticket obj = instantiateTicket(rs, vec);

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

	private Ticket instantiateTicket(ResultSet rs, Vehicle vec) throws SQLException {
		GateDao gateDao = DaoFactory.createGateDao();
		Gate entryGate = gateDao.findById(rs.getInt("entry_gate_id"));
		LocalDateTime entryTime = rs.getTimestamp("entry_time").toLocalDateTime();
		Double amount = rs.getDouble("amount_paid");
		Ticket ticket = new Ticket(rs.getInt("id"), vec, null, entryGate, entryTime, null, amount);
		return ticket;
	}

	@Override
	public Ticket findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {

			st = conn.prepareStatement("select ticket.* from ticket where id = ?");

			st.setInt(1, id);

			rs = st.executeQuery();

			if (rs.next()) {
				Ticket ticket = instantiateTicket(rs);
				return ticket;
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

		return null;
	}

	private Ticket instantiateTicket(ResultSet rs) throws SQLException {
		VehicleDao vehicleDao = DaoFactory.createVehicleDao();
		ParkingSpotDao parkingDao = DaoFactory.createParkingSpotDao();
		GateDao gateDao = DaoFactory.createGateDao();
		Vehicle vehicle = vehicleDao.findByPlate(rs.getString("vehicle_plate"));
		Gate entryGate = gateDao.findById(rs.getInt("entry_gate_id"));
		Gate exitGate = gateDao.findById(rs.getInt("exit_gate_id"));
		LocalDateTime entryTime = rs.getTimestamp("entry_time").toLocalDateTime();
		LocalDateTime exitTime = rs.getTimestamp("exit_time").toLocalDateTime();
		Double amount = rs.getDouble("amount_paid");
		Ticket ticket = new Ticket(rs.getInt("id"), vehicle, exitGate, entryGate, entryTime, exitTime, amount);
		ticket.getSpots().add(parkingDao.findByNumber(rs.getInt("parking_spot")));
		return ticket;
	}

	private Vehicle instantiateVehicle(ResultSet rs) throws SQLException {
		VehicleDao vehicleDao = DaoFactory.createVehicleDao();
		Vehicle vehicle = vehicleDao.findByPlate(rs.getString("vehicle_plate"));
		return vehicle;
	}

}
