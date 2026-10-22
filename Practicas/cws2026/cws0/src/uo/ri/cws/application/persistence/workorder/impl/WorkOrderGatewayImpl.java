package uo.ri.cws.application.persistence.workorder.impl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.util.exception.PersistenceException;

public class WorkOrderGatewayImpl implements WorkOrderGateway {

    @Override
    public List<String> findIdsByMechanicId(String mechanicId) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        List<String> res = new ArrayList<>();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TWORKORDERS_FIND_IDS_BY_MECHANIC_ID"))) {
            pst.setString(1, mechanicId);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    res.add(rs.getString("id"));
                }
            }
            return res;

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

}