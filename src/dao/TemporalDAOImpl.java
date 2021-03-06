/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import almacenes.conectorDB.DatabaseUtils;
import almacenes.model.Temporal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jcapax
 */
public class TemporalDAOImpl implements TemporalDAO {

    private DatabaseUtils databaseUtils;
    private Connection sqlite;

    public TemporalDAOImpl(Connection _connectionDB) {
        this.databaseUtils = new DatabaseUtils();
        this.sqlite = _connectionDB;
    }

    public boolean productoExistente(int idProducto) {
        boolean aux = false;

        String sql = "select * from detalleTransaccionTemp where idProducto = ?";
        try {
            PreparedStatement ps = sqlite.prepareStatement(sql);
            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                aux = true;
            }
//            int n = ps.executeUpdate();
//            if (n > 0) {
//                aux = true;
//            }
        } catch (SQLException ex) {
            Logger.getLogger(TemporalDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aux;
    }

    public int cantidadProductoExistente(int idProducto) {
        int cantidad = 0;

        String sql = "select cantidad from detalleTransaccionTemp where idProducto = ?";
        try {
            PreparedStatement ps = sqlite.prepareStatement(sql);
            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cantidad = rs.getInt(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(TemporalDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cantidad;
    }

    public double pUnitProductoExistente(int idProducto) {
        double pUnit = 0;

        String sql = "select valorUnitario from detalleTransaccionTemp where idProducto = ?";
        try {
            PreparedStatement ps = sqlite.prepareStatement(sql);
            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pUnit = rs.getDouble(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(TemporalDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pUnit;
    }

    @Override
    public void insertarProductoTemp(Temporal detTransTemp) {

        if (productoExistente(detTransTemp.getIdProducto())) {
            int cantidad = cantidadProductoExistente(detTransTemp.getIdProducto());

            String sql = "update detalleTransaccionTemp set cantidad = ?, valorTotal = ? where idProducto = ?";
            try {
                PreparedStatement ps = sqlite.prepareStatement(sql);
                ps.setInt(1, cantidad + 1);
                ps.setDouble(2, (cantidad + 1) * detTransTemp.getValorUnitario());
                ps.setInt(3, detTransTemp.getIdProducto());
                ps.execute();
            } catch (SQLException ex) {
                Logger.getLogger(TemporalDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            String sql = "INSERT INTO detalleTransaccionTemp("
                    + "idProducto, nombreProducto, idUnidadMedida, simbolo, "
                    + "cantidad, valorUnitario, valorTotal, tipoValor) "
                    + "values(?, ?, ?, ?, ?, ?, ?, ?)";

            try {
                PreparedStatement ps = sqlite.prepareStatement(sql);
                ps.setInt(1, detTransTemp.getIdProducto());
                ps.setString(2, detTransTemp.getNombreProducto());
                ps.setInt(3, detTransTemp.getIdUnidadMedida());
                ps.setString(4, detTransTemp.getSimbolo());
                ps.setDouble(5, detTransTemp.getCantidad());
                ps.setDouble(6, detTransTemp.getValorUnitario());
                ps.setDouble(7, detTransTemp.getValorTotal());
                ps.setString(8, detTransTemp.getTipoValor());

                int n = ps.executeUpdate();
                if (n != 0) {
//                System.out.println("registrado con exito");
                }

            } catch (SQLException ex) {
                Logger.getLogger(TemporalDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void eliminarProdcutoTemp(int idProducto, int idUnidadMedida, boolean shift) {

        if (productoExistente(idProducto) && shift) {
            if (cantidadProductoExistente(idProducto) >= 2) {
                int cantidad = cantidadProductoExistente(idProducto);
                double pUnit = pUnitProductoExistente(idProducto);

                String sql = "update detalleTransaccionTemp set cantidad = ?, valorTotal = ? where idProducto = ?";
                try {
                    PreparedStatement ps = sqlite.prepareStatement(sql);
                    ps.setInt(1, cantidad - 1);
                    ps.setDouble(2, (cantidad - 1) * pUnit);
                    ps.setInt(3, idProducto);
                    ps.execute();
                } catch (SQLException ex) {
                    Logger.getLogger(TemporalDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        } else {
            String sql = "delete from detalleTransaccionTemp "
                    + "where idProducto = ? and idUnidadMedida = ?";
            try {
                PreparedStatement ps = sqlite.prepareStatement(sql);
                ps.setInt(1, idProducto);
                ps.setInt(2, idUnidadMedida);

                int n = ps.executeUpdate();
                if (n != 0) {
//                System.out.println("eliminado con exito");
                }

            } catch (SQLException ex) {
                Logger.getLogger(TemporalDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void eliminarProdcutoTemp(int idProducto, int idUnidadMedida) {

        String sql = "delete from detalleTransaccionTemp "
                + "where idProducto = ? and idUnidadMedida = ?";
        try {
            PreparedStatement ps = sqlite.prepareStatement(sql);
            ps.setInt(1, idProducto);
            ps.setInt(2, idUnidadMedida);

            int n = ps.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(TemporalDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void vaciarProductoTemp() {
        String sqlVaciarDetTransTemp = "delete from detalleTransaccionTemp";

        PreparedStatement ps;
        try {
            ps = sqlite.prepareStatement(sqlVaciarDetTransTemp);
            ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(TemporalDAOImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public ArrayList<Temporal> getListaTemporal() {
        String sql = "SELECT * FROM detalleTransaccionTemp";

        ArrayList<Temporal> lTemporal = new ArrayList<Temporal>();

        try {
            PreparedStatement pst = sqlite.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Temporal temporal = new Temporal();

                temporal.setCantidad(rs.getDouble("cantidad"));
                temporal.setIdProducto(rs.getInt("idProducto"));
                temporal.setIdUnidadMedida(rs.getInt("idUnidadMedida"));
                temporal.setNombreProducto(rs.getString("nombreProducto"));
                temporal.setSimbolo(rs.getString("simbolo"));
                temporal.setTipoValor(rs.getString("tipoValor"));
                temporal.setValorTotal(rs.getDouble("valorTotal"));
                temporal.setValorUnitario(rs.getDouble("valorUnitario"));

                lTemporal.add(temporal);

            }

        } catch (SQLException ex) {
            Logger.getLogger(RubroDAOImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return lTemporal;
    }

    @Override
    public double totalProductosTemp() {
        double importeTotal = 0;

        String sql = "select sum(valorTotal) as totalTemp from detalleTransaccionTemp";

        PreparedStatement pst;
        try {
            pst = sqlite.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                importeTotal = rs.getDouble("totalTemp");

            }
        } catch (SQLException ex) {
            Logger.getLogger(TemporalDAOImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return importeTotal;
    }

}
