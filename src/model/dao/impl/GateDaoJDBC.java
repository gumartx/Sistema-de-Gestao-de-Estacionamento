package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DB;
import db.DbException;
import model.dao.GateDao;
import model.entities.Gate;
import model.enums.GateType;

public class GateDaoJDBC implements GateDao {

	private Connection conn;

	public GateDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public Gate findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {

			st = conn.prepareStatement("SELECT gate.* FROM gate WHERE id = ?");

			st.setInt(1, id);

			rs = st.executeQuery();

			if (rs.next()) {
				Gate gate = instantiateGate(rs);
				return gate;
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

		return null;
	}

	private Gate instantiateGate(ResultSet rs) throws SQLException {
		Gate gate = new Gate(rs.getInt("id"), rs.getInt("number"), GateType.valueOf(rs.getString("type").toUpperCase()));
		return gate;
	}

	
	
}
