/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import almacenes.model.ListaProductos;
import almacenes.model.Producto;
import almacenes.model.StockProducto;
import java.io.InputStream;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author jcapax
 */
public interface ProductoDAO {
    
    public ArrayList<ListaProductos> getListaProductos();
    public ArrayList<ListaProductos> getListaProductosVenta(String criterio);
    public void insertarProducto(Producto producto, InputStream image, int longitudBytes);
    public ArrayList<StockProducto> getListaStockProducto(byte idLugar, String criterio);
    public byte getControlStock(int idProducto);
    public void editarProducto(Producto producto, InputStream image, int longitudBytes);
//    public ImageIcon getImage(int idProducto, int width, int height);
    public InputStream getImage(int idProducto);
}
