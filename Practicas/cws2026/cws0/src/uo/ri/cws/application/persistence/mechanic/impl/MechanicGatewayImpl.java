package uo.ri.cws.application.persistence.mechanic.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.util.exception.PersistenceException;


public class MechanicGatewayImpl implements MechanicGateway{

    @Override
    public void add(MechanicRecord m) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        //no puede estra dentro de un try pq la cerraria cuando termine 
        
            try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TMECHANICS_ADD"))) {
                pst.setString(1, m.id);
                pst.setString(2, m.nif);
                pst.setString(3, m.name);
                pst.setString(4, m.surname);
                pst.setLong(5, m.version);
                pst.setTimestamp(6, Timestamp.valueOf(m.createdAt));
                pst.setTimestamp(7, Timestamp.valueOf(m.updatedAt));
                pst.setString(8, m.entityState);             

                pst.executeUpdate();

          
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
        
    }

    @Override
    public void remove(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TMECHANICS_DELETE"))) {
            pst.setString(1, id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
        
    }

    @Override
    public void update(MechanicRecord t) throws PersistenceException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Optional<MechanicRecord> findById(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TMECHANICS_FINDBYID"))) {
            pst.setString(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new MechanicRecordAssembler().toRecord(rs));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<MechanicRecord> findAll() throws PersistenceException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Optional<MechanicRecord> findByNif(String nif) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TMECHANICS_FINDBYNIF"))) {
            pst.setString(1, nif);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new MechanicRecordAssembler().toRecord(rs));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

}
