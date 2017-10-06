/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package almacenes.model;

import javax.swing.ImageIcon;

/**
 *
 * @author jcapax
 */
public class Producto {
    private int id;
    private String claseProducto;
    private int idRubroProducto;
    private String rubro;
    private String descripcion;
    private double precioVenta;
    private double precioCompra;
    private String tipoCuenta;
    private String estado;
    private int controlStock;
    private String usuario;
    private ImageIcon imagen;

    public String getRubro() {
        return rubro;
    }

    public void setRubro(String rubro) {
        this.rubro = rubro;
    }
    
    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    
    public ImageIcon getImagen() {
        return imagen;
    }

    public void setImagen(ImageIcon imagen) {
        this.imagen = imagen;
    }

    public Producto(String claseProducto, int idRubroProducto, 
                String descripcion, String tipoCuenta, String estado, 
                String usuario) {
        this.claseProducto = claseProducto;
        this.idRubroProducto = idRubroProducto;        
        this.descripcion = descripcion;
        this.tipoCuenta = tipoCuenta;
        this.estado = estado;                
        this.usuario = usuario;
    }
    
//    public Producto(String claseProducto, int idRubroProducto, 
//                String descripcion, String tipoCuenta, String estado, 
//                int recargo, int controlStock, String usuario) {
//        this.claseProducto = claseProducto;
//        this.idRubroProducto = idRubroProducto;        
//        this.descripcion = descripcion;
//        this.tipoCuenta = tipoCuenta;
//        this.estado = estado;        
//        this.controlStock = controlStock;
//        this.usuario = usuario;
//    }

    public Producto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClaseProducto() {
        return claseProducto;
    }

    public void setClaseProducto(String claseProducto) {
        this.claseProducto = claseProducto;
    }

    public int getIdRubroProducto() {
        return idRubroProducto;
    }

    public void setIdRubroProducto(int idRubroProducto) {
        this.idRubroProducto = idRubroProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getControlStock() {
        return controlStock;
    }

    public void setControlStock(int controlStock) {
        this.controlStock = controlStock;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    
    
    
}
