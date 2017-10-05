/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import almacenes.conectorDB.DatabaseUtils;
import almacenes.model.ListaProductos;
import almacenes.model.Producto;
import almacenes.model.StockProducto;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author jcapax
 */
public class ProductoDAOImpl implements ProductoDAO {

    private DatabaseUtils databaseUtils;
    private Connection connectionDB;

    public ProductoDAOImpl(Connection _connectionDB) {
        this.databaseUtils = new DatabaseUtils();
        this.connectionDB = _connectionDB;
    }

    @Override
    public ArrayList<Producto> getListaProductos() {
        String sql = "SELECT * FROM producto";

        ArrayList<Producto> lproducto = new ArrayList<Producto>();

        try {
            PreparedStatement ps = connectionDB.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Producto lProd = new Producto();

                lProd.setId(rs.getInt("id"));                
                lProd.setDescripcion(rs.getString("descripcion"));
                lProd.setEstado(rs.getString("estado"));
                lProd.setIdRubroProducto(rs.getInt("idRubroProducto"));
                
                lproducto.add(lProd);
            }

        } catch (SQLException ex) {
            Logger.getLogger(RubroDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lproducto;
    }

    @Override
    public void insertarProducto(Producto producto, InputStream image, int longitudBytes) {
        String sqlProd = "insert into "
                + "producto(idRubroProducto, descripcion, estado, usuario, imagen) "
                + "values(?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = connectionDB.prepareStatement(sqlProd);

            ps.setInt(1, producto.getIdRubroProducto());
            ps.setString(2, producto.getDescripcion());
            ps.setString(3, producto.getEstado());            
            ps.setString(4, producto.getUsuario());
            ps.setBlob(5, image, longitudBytes);

            int n = ps.executeUpdate();
            if (n > 0) {
//                System.out.println("producto insertado");
            } else {
//                System.out.println("error en el registro");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void insertarProducto(Producto producto) {
        String sqlProd = "insert into "
                + "producto(idRubroProducto, descripcion, estado, usuario) "
                + "values(?, ?, ?, ?)";

        try {
            PreparedStatement ps = connectionDB.prepareStatement(sqlProd);

            ps.setInt(1, producto.getIdRubroProducto());            
            ps.setString(2, producto.getDescripcion());
            ps.setString(3, producto.getEstado());            
            ps.setString(4, producto.getUsuario());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProductoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public ArrayList<ListaProductos> getListaProductosVenta(String criterio) {
        String sql = "SELECT * FROM vproductos WHERE estado = 'V' and descripcion like '%" + criterio + "%'";

        ArrayList<ListaProductos> lproducto = new ArrayList<ListaProductos>();

        try {
            PreparedStatement ps = connectionDB.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ListaProductos lProd = new ListaProductos();

                lProd.setId(rs.getInt("idProducto"));
                lProd.setACTUALIZACION(rs.getInt("actualizacion"));
                lProd.setControlStock(rs.getInt("controlStock"));
                lProd.setDescripcion(rs.getString("descripcion"));
                lProd.setEstado(rs.getString("estado"));
                lProd.setIDUNIDADMEDIDA(rs.getInt("idUnidadMedida"));
                lProd.setIdMarca(rs.getInt("idMarca"));
                lProd.setIdProcedencia(rs.getInt("idProcedencia"));
                lProd.setIdRubroProducto(rs.getInt("idRubroProducto"));
                lProd.setMarca(rs.getString("marca"));
                lProd.setNombreUnidadMedida(rs.getString("nombreUnidadMedida"));
                lProd.setPRECIOCOMPRA(rs.getDouble("precioCompra"));
                lProd.setPRECIOVENTA(rs.getDouble("precioVenta"));
                lProd.setPRECIOVENTAAUMENTO(rs.getDouble("precioVentaAumento"));
                lProd.setPRECIOVENTAREBAJA(rs.getDouble("precioVentaRebaja"));
                lProd.setProcedencia(rs.getString("procedencia"));
                lProd.setRubro(rs.getString("rubro"));
                lProd.setSTOCKMINIMO(rs.getDouble("stockMinimo"));
                lProd.setUNIDADPRINCIPAL(rs.getInt("unidadPrincipal"));

                lproducto.add(lProd);
            }

        } catch (SQLException ex) {
            Logger.getLogger(RubroDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lproducto;
    }

    @Override
    public ArrayList<StockProducto> getListaStockProducto(byte idLugar, String criterio) {
        ArrayList<StockProducto> lStockProducto = new ArrayList<StockProducto>();

        String sql = "SELECT p.descripcion, p.nombreUnidadMedida, s.stock "
                + "FROM vstock s"
                + " JOIN vproductos p ON p.idproducto = s.idproducto "
                + " WHERE s.idLugar = ? and p.descripcion like '%" + criterio + "%'"
                + " ORDER BY s.stock";

        try {
            PreparedStatement ps = connectionDB.prepareStatement(sql);
            ps.setByte(1, idLugar);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                StockProducto sp = new StockProducto();

                sp.setNombrePRoducto(rs.getString("descripcion"));
                sp.setNombreUnidadMedida(rs.getString("nombreUnidadMedida"));
                sp.setStock(rs.getDouble("stock"));

                lStockProducto.add(sp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lStockProducto;
    }

    @Override
    public byte getControlStock(int idProducto) {
        byte controlStock = 0;
        String sql = "select controlStock from producto where id = ?";
        try {
            PreparedStatement ps = connectionDB.prepareStatement(sql);
            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                controlStock = rs.getByte("controlStock");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return controlStock;

    }

    @Override
    public void editarProducto(Producto producto, InputStream image, int longitudBytes) {
        String sql = "update producto "
                + "set idRubroProducto = ?, "
                + "descripcion = ?, "
                + "estado = ?, "
                + "usuario = ?, "
                + "imagen = ? "
                + "where id = ?";
        try {
            PreparedStatement ps = connectionDB.prepareStatement(sql);
            ps.setInt(1, producto.getIdRubroProducto());
            ps.setString(2, producto.getDescripcion());
            ps.setString(3, producto.getEstado());            
            ps.setString(4, producto.getUsuario());
            ps.setBlob(5, image, longitudBytes);
            ps.setInt(6, producto.getId());

            ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(ProductoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void editarProducto(Producto producto) {
        String sql = "update producto "
                + "set idRubroProducto = ?, "
                + "descripcion = ?, "
                + "estado = ?, "
                + "controlStock = ?, usuario = ? "                
                + "where id = ?";
        try {
            PreparedStatement ps = connectionDB.prepareStatement(sql);
            ps.setInt(1, producto.getIdRubroProducto());
            ps.setString(2, producto.getDescripcion());
            ps.setString(3, producto.getEstado());
            ps.setInt(4, producto.getControlStock());
            ps.setString(5, producto.getUsuario());            
            ps.setInt(6, producto.getId());

            ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(ProductoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    @Override
    public ImageIcon getImage(int idProducto, int width, int height) {
        ImageIcon ii = null;
        InputStream is = null;

        String sql = "select imagen from producto where id = ?";
        PreparedStatement ps;
        try {
            ps = connectionDB.prepareStatement(sql);
            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                try {
                    is = rs.getBinaryStream(1);
                    BufferedImage bi = resize(ImageIO.read(is), width, height);
                    ii = new ImageIcon(bi);
                } catch (Exception e) {                    
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ii;
    }
//    @Override
//    public FileInputStream getImage(int idProducto) {
//        
//        FileInputStream is = null;
//
//        String sql = "select imagen from producto where id = ? and imagen is not null";
//        PreparedStatement ps;
//        try {
//            ps = connectionDB.prepareStatement(sql);
//            ps.setInt(1, idProducto);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                try {
//                    is = rs.getBinaryStream(1);                    
//                } catch (Exception e) {
//                }
//
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(ProductoDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return is;
//    }

    public static BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = (Graphics2D) bi.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        return bi;
    }

}
