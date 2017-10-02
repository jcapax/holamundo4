/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package almacenes.vistas;

import CodigoControl.ControlCode;
import almacenes.conectorDB.DataBaseSqlite;
import almacenes.conectorDB.DatabaseUtils;
import almacenes.model.Caja;
import almacenes.model.DetalleTransaccion;
import almacenes.model.FacturaVenta;
import almacenes.model.Temporal;
import almacenes.model.ListaProductos;
import almacenes.model.Transaccion;
import dao.ArqueoDAOImpl;
import dao.CajaDAOImpl;
import dao.ClienteProveedorDAO;
import dao.ClienteProveedorDAOImpl;
import dao.CreditoDAO;
import dao.CreditoDAOImpl;
import dao.DetalleTransaccionDAOImpl;
import dao.FacturaVentaDAOImpl;
import dao.TemporalDAOImpl;
import dao.ProductoDAOImpl;
import dao.TransaccionDAOImpl;
import dao.UnidadProductoDAOImlp;
import dao.reportes.ReporteCreditoDAO;
import dao.reportes.ReporteCreditoDAOImpl;
import dao.reportes.ReporteFacturacionDAOImpl;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jcapax
 */
public class FormTransaccion1 extends javax.swing.JFrame {

    private DatabaseUtils databaseUtils;
    private Connection connectionDB, connectionTemp;
    private DefaultTableModel dtm;
    private int idTipoTransaccion; // tipo de proceso q se ejecutara
    private int idTipoTransaccionEntrega;  // tipo de entrega q se ejecutara
    private byte idLugar;
    private String usuario;
    private DecimalFormat df;

//    DefaultTableModel dtm;
    public FormTransaccion1(Connection connectionDB,
            int idTipoTransaccion, int idTipoTransaccionEntrega,
            String usuario, byte idLugar) {

        initComponents();

        this.setLocationRelativeTo(null);

        df = new DecimalFormat("###,###.00");

        this.databaseUtils = new DatabaseUtils();
        this.connectionDB = connectionDB;
        this.idTipoTransaccion = idTipoTransaccion;
        this.idTipoTransaccionEntrega = idTipoTransaccionEntrega;
        this.usuario = usuario;
        this.idLugar = idLugar;

        headerTabla();

        iniciarComponentes();

        abrirConexionTemp();

        createEnterKeybindings(jtProductos);
    }

    public void headerTabla() {
        Font f = new Font("Times New Roman", Font.BOLD, 13);

        jtProductos.getTableHeader().setFont(f);
        jtProductos.getTableHeader().setBackground(Color.orange);

        jtTemporal.getTableHeader().setFont(f);
        jtTemporal.getTableHeader().setBackground(Color.orange);
    }

    public void iniciarComponentes() {
        byte idTerminal = 1;
        
        ArqueoDAOImpl arq = new ArqueoDAOImpl(connectionDB);

        llenarTablaProductos("");
        jtxtDetalle.setText("");
        
        jlIdProducto.setVisible(false);
        jlIdUnidadMedida.setVisible(false);
        jlidClienteProveedor.setText("0");
        jlidClienteProveedor.setVisible(false);
        jcClienteProveedor.removeAllItems();

        switch (idTipoTransaccion) {
            case 1:  //compras
                jbTransaccion.setVisible(true);
                jlTituloFormulario.setText("COMPRAS");

                jlnit.setEnabled(false);
                jtxtNit.setEnabled(false);

                jtxtValorUnitario.setEnabled(true);
                jtxtValorUnitario.setEditable(true);
                jtxtValorUnitario.setText("");

                jlClienteProveedor.setVisible(false);
                jcClienteProveedor.setVisible(false);

                jlDetalle.setVisible(false);
                jtxtDetalle.setVisible(false);
                break;
            case 2:
                jbTransaccion.setVisible(true);
                jlTituloFormulario.setText("VENTAS");

                jlnit.setVisible(true);
                jtxtNit.setVisible(true);

                jtxtValorUnitario.setEnabled(false);
                jtxtValorUnitario.setEditable(false);

                jlClienteProveedor.setEnabled(false);
                jcClienteProveedor.setEnabled(false);

                jlDetalle.setEnabled(false);
                jtxtDetalle.setEnabled(false);
                break;
            case 3:
                jbTransaccion.setVisible(true);
                jlTituloFormulario.setText("CREDITO VENTAS");

                jlnit.setEnabled(false);
                jtxtNit.setEnabled(false);

                jlRazonSocial.setEnabled(false);
                jtxtRazonSocial.setEnabled(false);

                jtxtValorUnitario.setEnabled(false);
                jtxtValorUnitario.setEditable(false);

                jlClienteProveedor.setEnabled(true);
                jcClienteProveedor.setEnabled(true);

                jlDetalle.setVisible(true);
                jtxtDetalle.setVisible(true);

                String tipo = "C";//cliente
                llenarClienteProveedor(tipo);
                break;
            case 6:
                jlnit.setVisible(false);
                jtxtNit.setVisible(false);

                jbTransaccion.setVisible(true);
                jlTituloFormulario.setText("AJUSTE STOCK");
                jtxtValorUnitario.setEnabled(true);
                jtxtValorUnitario.setEditable(true);
                jtxtValorUnitario.setText("");

                jlClienteProveedor.setEnabled(false);
                jcClienteProveedor.setEnabled(false);

                jlDetalle.setEnabled(false);
                jtxtDetalle.setEnabled(false);
                break;
            default:
                System.err.println("nada");
        }

        int idArqueo = arq.getIdArqueo(idLugar, idTerminal, usuario);
//        System.out.println("idarqueo: "+idArqueo+" estado: "+arq.getEstadoCaja(idArqueo));
        if (!arq.getEstadoCaja(idArqueo).equals("A")) {
            JOptionPane.showMessageDialog(this, "No es posible realizar transacciones hasta que registre Caja Inicial");
            jbTransaccion.setEnabled(false);
        } else {
            jbTransaccion.setEnabled(true);
        }
    }

//    public void registrarCompra(int idTipoTransaccion, int idTipoTransaccionEntrega){
    public void registrarCompra() {

        int idTransaccion = 0;
        int idEntregaTransaccion = 0;

//        int idTipoTransaccion = 1; // compra
        idTransaccion = resgistrarTransaccion(idTipoTransaccion);
        registrarDetalleTransaccion(idTransaccion);

        //int idTipoTransaccionEntrega = 7; // recepcion - material
        idEntregaTransaccion = resgistrarTransaccion(idTipoTransaccionEntrega);

        registrarDetalleTransaccion(idEntregaTransaccion);

        registrarEntregaTransaccion(idEntregaTransaccion, idTransaccion);

        registrarCaja(idTransaccion);

        limpiar();

        JOptionPane.showMessageDialog(this, "Compra generada con exito");

        vaciarProductosTemporales();
    }

    public boolean validarRegistros() {
        boolean aux = true;
        if (jtTemporal.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No existen productos para ejectuar la transaccion!!!");
            aux = false;
        }
        if (idTipoTransaccion == 2) { //VENTA
            if (jtxtNit.getText().length() == 0) {
                JOptionPane.showMessageDialog(this, "El Nit o CI del cliente no pueden estar vacio!!!");
                aux = false;
            }
            if (jtxtRazonSocial.getText().length() == 0) {
                JOptionPane.showMessageDialog(this, "Favor registrar Razon Social del cliente!!!");
                aux = false;
            }
            try {
                Long x = Long.parseLong(jtxtNit.getText());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Nit o Ci no valido!!!");
                aux = false;
            }
        }
        if (idTipoTransaccion == 3) {
            if (jlClienteProveedor.getText().equals("0")) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un Cliente/Proveedor!!!");
                aux = false;
            }
        }

        return aux;
    }

    public void registrarStockInicial() {

        int idTransaccion = 0;
        int idEntregaTransaccion = 0;

//        int idTipoTransaccion = 6; // compra
        idTransaccion = resgistrarTransaccion(idTipoTransaccion);
        registrarDetalleTransaccion(idTransaccion);

        //int idTipoTransaccionEntrega = 7; // recepcion - material
        idEntregaTransaccion = resgistrarTransaccion(idTipoTransaccionEntrega);

        registrarDetalleTransaccion(idEntregaTransaccion);

        registrarEntregaTransaccion(idEntregaTransaccion, idTransaccion);

        registrarCaja(idTransaccion);

        limpiar();

        JOptionPane.showMessageDialog(this, "Stock Inicial registrado con exito");
        vaciarProductosTemporales();
    }

//    public void registrarVenta(int idTipoTransaccion, int idTipoTransaccionEntrega){
    public void registrarVenta() {

        int idTransaccion = 0;
        int idEntregaTransaccion = 0;

//        int idTipoTransaccion = 2; // venta -  plata
        idTransaccion = resgistrarTransaccion(idTipoTransaccion);
        registrarDetalleTransaccion(idTransaccion);

//        int idTipoTransaccionEntrega = 8; // entrega - material
        idEntregaTransaccion = resgistrarTransaccion(idTipoTransaccionEntrega);

        registrarDetalleTransaccion(idEntregaTransaccion);

        registrarEntregaTransaccion(idEntregaTransaccion, idTransaccion);

        registrarCaja(idTransaccion);

        registrarFactura(idTransaccion);

        limpiar();

        JOptionPane.showMessageDialog(this, "Venta generada con exito");
        vaciarProductosTemporales();
    }

    public void registrarCredito() {

        int idTransaccion = 0;
        int idEntregaTransaccion = 0;
        int idClienteProveedor = Integer.parseInt(jlidClienteProveedor.getText());
        String detalle = jtxtDetalle.getText().toUpperCase();
        
//        int idTipoTransaccion = 3; // credito -  plata
        idTransaccion = resgistrarTransaccion(idTipoTransaccion);
        registrarDetalleTransaccion(idTransaccion);

//        int idTipoTransaccionEntrega = 8; // entrega - material
        idEntregaTransaccion = resgistrarTransaccion(idTipoTransaccionEntrega);

        registrarDetalleTransaccion(idEntregaTransaccion);

        registrarEntregaTransaccion(idEntregaTransaccion, idTransaccion);
        
        insertarCredito(idTransaccion, idClienteProveedor, detalle);
        
        ReporteCreditoDAO rep = new ReporteCreditoDAOImpl(connectionDB, usuario);
        rep.vistaPreviaCredito(idTransaccion);

        limpiar();

        JOptionPane.showMessageDialog(this, "Credito generado con exito");        
        vaciarProductosTemporales();
        iniciarComponentes();
        
    }

    public void insertarCredito(int idTransaccion, int idClienteProveedor, String detalle){
        CreditoDAO cred = new CreditoDAOImpl(connectionDB);
        cred.insertarCredito(idTransaccion, idClienteProveedor, detalle);
        
    }
    
    public void registrarFactura(int idTransaccion) {
        TransaccionDAOImpl tran = new TransaccionDAOImpl(connectionDB);
        FacturaVentaDAOImpl facDaoImpl = new FacturaVentaDAOImpl(connectionDB);
        ControlCode controlCode = new ControlCode();

        String nit = jtxtNit.getText().trim();
        String razonSocial = jtxtRazonSocial.getText().trim().toUpperCase();

        int idSucursal = 1;
        String codigoControl = "";
        int correlativoSucursal = 1;

        int especificacion = 1;
        String estado = "V";
        String nroAutorizacion = facDaoImpl.getNroAutorizacion(idSucursal);
        Date fechaFactura = tran.getFechaTransaccion(idTransaccion);
        java.util.Date fechaFactura1 = new Date(fechaFactura.getTime());
        Date fechaLimiteEmision = facDaoImpl.getFechaLimiteEmision(nroAutorizacion);
        int idDosificacion = 1;
        int nroFactura = facDaoImpl.getNewNroFactura(nroAutorizacion);
        String llaveDosf = facDaoImpl.getLlaveDosificacion(nroAutorizacion);
        double importeTotal = tran.getValorTotalTransaccion(idTransaccion);
        double importeExportaciones = 0;
        double importeIce = 0;
        double importeRebajas = 0;
        double importeSubTotal = importeTotal - importeExportaciones - importeIce;
        double importeVentasTasaCero = 0;
        double importeBaseDebitoFiscal = importeSubTotal;
        double debitoFiscal = importeBaseDebitoFiscal * 0.13;

        Format fd = new SimpleDateFormat("yyyyMMdd");

        String auxFecha = fd.format(fechaFactura1).trim();

        codigoControl = controlCode.generate(nroAutorizacion,
                String.valueOf(nroFactura).trim(),
                nit.trim(),
                auxFecha.trim(),
                String.valueOf(importeTotal).trim(),
                llaveDosf.trim());

        FacturaVenta fact = new FacturaVenta();

        fact.setCodigoControl(codigoControl);
        fact.setCorrelativoSucursal(correlativoSucursal);
        fact.setDebitoFiscal(debitoFiscal);
        fact.setEspecificacion(especificacion);
        fact.setEstado(estado);
        fact.setFechaFactura(fechaFactura);
        fact.setFechaLimiteEmision(fechaLimiteEmision);
        fact.setIdDosificacion(idDosificacion);
        fact.setIdSucursal(idSucursal);
        fact.setIdTransaccion(idTransaccion);
        fact.setImporteBaseDebitoFiscal(importeBaseDebitoFiscal);
        fact.setImporteExportaciones(importeExportaciones);
        fact.setImporteIce(importeIce);
        fact.setImporteRebajas(importeRebajas);
        fact.setImporteSubtotal(importeSubTotal);
        fact.setImporteTotal(importeTotal);
        fact.setImporteVentasTasaCero(importeVentasTasaCero);
        fact.setNit(nit);
        fact.setNroAutorizacion(nroAutorizacion);
        fact.setNroFactura(nroFactura);
        fact.setRazonSocial(razonSocial);

        FacturaVentaDAOImpl factDaoImpl = new FacturaVentaDAOImpl(connectionDB);
        factDaoImpl.insertarFacturaVenta(fact);

        ReporteFacturacionDAOImpl repFactura = new ReporteFacturacionDAOImpl(connectionDB, estado);

        repFactura.VistaPreviaFacturaVenta(idTransaccion, facDaoImpl.getCadenaCodigoQr(idTransaccion), fact.getImporteTotal());
    }

    public void abrirConexionTemp() {
        DataBaseSqlite sqLite = new DataBaseSqlite();
        connectionTemp = sqLite.conexion();

        TemporalDAOImpl tempDAOImpl = new TemporalDAOImpl(connectionTemp);
        tempDAOImpl.vaciarProductoTemp();

    }

    public void calcularImportTotalTemp() {
        double importeTotal = 0;
        TemporalDAOImpl tempDAO = new TemporalDAOImpl(connectionTemp);
        importeTotal = tempDAO.totalProductosTemp();
        jtxtTotalTransaccion.setText(String.valueOf(df.format(importeTotal)));
    }

    public void eliminarProductoTemporal() {
        TemporalDAOImpl tempDAO = new TemporalDAOImpl(connectionTemp);

        int filSel = jtTemporal.getSelectedRow();
        int idProducto = (int) jtTemporal.getValueAt(filSel, 0);
        int idUnidadMedida = (int) jtTemporal.getValueAt(filSel, 1);

        tempDAO.eliminarProdcutoTemp(idProducto, idUnidadMedida);

        llenarTablaTemporal();
    }

    public void llenarTablaTemporal() {

        TemporalDAOImpl tempDAO = new TemporalDAOImpl(connectionTemp);

        ArrayList<Temporal> t = new ArrayList<Temporal>();

        t = tempDAO.getListaTemporal();

        dtm = (DefaultTableModel) this.jtTemporal.getModel();
        dtm.setRowCount(0);

        jtTemporal.setModel(dtm);

        Object[] fila = new Object[7];

        for (int i = 0; i < t.size(); i++) {
            fila[0] = t.get(i).getIdProducto();
            fila[1] = t.get(i).getIdUnidadMedida();
            fila[2] = t.get(i).getNombreProducto();
            fila[3] = t.get(i).getSimbolo();
            fila[4] = t.get(i).getCantidad();
//            fila[5] = df.format(t.get(i).getValorUnitario());
//            fila[6] = df.format(t.get(i).getValorTotal());
            fila[5] = t.get(i).getValorUnitario();
            fila[6] = t.get(i).getValorTotal();

            dtm.addRow(fila);
        }

        jtTemporal.setModel(dtm);

        calcularImportTotalTemp();
    }

    public void insertarTemp() {

        Temporal temp = new Temporal();

        int idProducto = Integer.valueOf(jlIdProducto.getText());
        int idUnidadMedida = Integer.valueOf(jlIdUnidadMedida.getText());
        String nombreProducto = jtxtNombreProducto.getText();
        String simbolo = "";
        double cantidad = Double.valueOf(jtxtCantidad.getText());
        double valorUnitario = Double.valueOf(jtxtValorUnitario.getText());
        double valorTotal = cantidad * valorUnitario;
        String tipoValor = "N";// normal

        temp.setCantidad(cantidad);
        temp.setIdProducto(idProducto);
        temp.setIdUnidadMedida(idUnidadMedida);
        temp.setNombreProducto(nombreProducto);
        temp.setSimbolo(simbolo);
        temp.setTipoValor(tipoValor);
        temp.setValorTotal(valorTotal);
        temp.setValorUnitario(valorUnitario);

        TemporalDAOImpl tempDAOImpl = new TemporalDAOImpl(connectionTemp);
        tempDAOImpl.insertarProductoTemp(temp);

        llenarTablaTemporal();
    }

    public void seleccionarProducto() {

        UnidadProductoDAOImlp up = new UnidadProductoDAOImlp(connectionDB);

        int filSel = jtProductos.getSelectedRow();

        int idProducto = (int) jtProductos.getValueAt(filSel, 0);
        int idUnidadMedida = (int) jtProductos.getValueAt(filSel, 1);
        String nombreProducto = jtProductos.getValueAt(filSel, 2).toString();
        Double valorUnitario = (double) jtProductos.getValueAt(filSel, 5);

        double stock = up.getStockProducto(idProducto, idUnidadMedida, idLugar);

        jtxtNombreProducto.setText(nombreProducto);
        jtxtValorUnitario.setText(valorUnitario.toString());
        jlIdProducto.setText(String.valueOf(idProducto));
        jlIdUnidadMedida.setText(String.valueOf(idUnidadMedida));

        jlStockProducto.setText(String.valueOf(stock));

    }

    public void llenarTablaProductos(String criterio) {
        ProductoDAOImpl prodDAOImpl = new ProductoDAOImpl(connectionDB);

        ArrayList<ListaProductos> lProd = new ArrayList<ListaProductos>();

        lProd = prodDAOImpl.getListaProductosVenta(criterio);

        dtm = (DefaultTableModel) this.jtProductos.getModel();
        dtm.setRowCount(0);

        jtProductos.setModel(dtm);

        Object[] fila = new Object[19];

//        System.out.println("nro de registros en pila: " + lProd.size());
        for (int i = 0; i < lProd.size(); i++) {
            fila[0] = lProd.get(i).getId();
            fila[1] = lProd.get(i).getIDUNIDADMEDIDA();
            fila[2] = lProd.get(i).getDescripcion();
            fila[3] = lProd.get(i).getNombreUnidadMedida();
            fila[4] = lProd.get(i).getMarca();
            fila[5] = lProd.get(i).getPRECIOVENTA();
            dtm.addRow(fila);
        }

        jtProductos.setAutoscrolls(false);
        jtProductos.setModel(dtm);
    }

    public void llenarClienteProveedor(String tipo) {
        String sel = "Sel";

        jcClienteProveedor.removeAllItems();
        jcClienteProveedor.addItem(sel);

        ClienteProveedorDAO cp = new ClienteProveedorDAOImpl(connectionDB);

        TreeMap<String, Integer> map = cp.clienteProveedorClaveValor(tipo);

        for (String s : map.keySet()) {
            jcClienteProveedor.addItem(s.toString());
        }
    }

    private void seleccionarClienteProveedor() {

        String sel = null;
        String tipo = "C";
        String comp = "Sel";

        ClienteProveedorDAO cp = new ClienteProveedorDAOImpl(connectionDB);

        TreeMap<String, Integer> map = cp.clienteProveedorClaveValor(tipo);

        try {
            sel = jcClienteProveedor.getSelectedItem().toString();

            if (sel.equals(comp)) {
                jlidClienteProveedor.setText("0");
            } else {
                jlidClienteProveedor.setText(map.get(sel).toString());
            }
        } catch (Exception e) {
        }
    }

    public void registrarCaja(int idTransaccion) {
        String estado = "A";
//        String usuario = "SYS";
        Date fecha = null;
        int nroCobro = 0, nroPago = 0;
        double importe = 0;

        TransaccionDAOImpl trans = new TransaccionDAOImpl(connectionDB);
        fecha = trans.getFechaTransaccion(idTransaccion);
        importe = trans.getValorTotalTransaccion(idTransaccion);

        CajaDAOImpl cajaDaoImpl = new CajaDAOImpl(connectionDB);

        Caja caja = new Caja();
        caja.setEstado(estado);
        caja.setFecha(fecha);
        caja.setIdTransaccion(idTransaccion);
        caja.setImporte(importe);
        caja.setNroCobro(nroCobro);
        caja.setNroPago(nroPago);
        caja.setUsuario(usuario);

        cajaDaoImpl.insertarCaja(caja);

    }

    public void registrarDetalleTransaccion(int idTransaccion) {

        DetalleTransaccionDAOImpl detTranDAOImpl = new DetalleTransaccionDAOImpl(connectionDB);

        int idProducto = 0;
        int idUnidadMedida = 0;
        double cantidad = 0;
        double valorUnitario = 0;
        double valorTotal = 0;
        String tipoValor = "N";

        ArrayList<DetalleTransaccion> detTrans = new ArrayList<DetalleTransaccion>();

        for (int fila = 0; fila < jtTemporal.getRowCount(); fila++) {
            idProducto = Integer.valueOf(jtTemporal.getValueAt(fila, 0).toString());
            idUnidadMedida = Integer.valueOf(jtTemporal.getValueAt(fila, 1).toString());
            cantidad = Double.valueOf(jtTemporal.getValueAt(fila, 4).toString());
            valorUnitario = Double.valueOf(jtTemporal.getValueAt(fila, 5).toString());
            valorTotal = Double.valueOf(jtTemporal.getValueAt(fila, 6).toString());

            DetalleTransaccion dt = new DetalleTransaccion();

            dt.setIdTransaccion(idTransaccion);
            dt.setIdProducto(idProducto);
            dt.setIdUnidadMedida(idUnidadMedida);
            dt.setCantidad(cantidad);
            dt.setValorUnitario(valorUnitario);
            dt.setValorTotal(valorTotal);
            dt.setTipoValor(tipoValor);

            detTrans.add(dt);

        }

        detTranDAOImpl.insertarDetalleTransaccion(detTrans);

    }

    public void limpiar() {
        dtm = (DefaultTableModel) this.jtTemporal.getModel();
        dtm.setRowCount(0);
        jtTemporal.setModel(dtm);

        jtxtNit.setText("");
        jtxtRazonSocial.setText("");

        jtxtTotalTransaccion.setText("");
    }

    public int resgistrarTransaccion(int idTipoTransaccion) {

        TransaccionDAOImpl transDaoImpl = new TransaccionDAOImpl(connectionDB);

        int nroTipoTransaccion = 0;
        int tipoMovimineto = transDaoImpl.getTipoMovimiento(idTipoTransaccion);
        int idTerminal = 1;
        int idTransaccion = 0;
        String estado = "A";
        String descripcion = "";
//        String usuario = "SYS";
        java.util.Date hoy = new java.util.Date();
        java.sql.Date fecha = new java.sql.Date(hoy.getTime());

        nroTipoTransaccion = transDaoImpl.getNroTipoTransaccion(idTipoTransaccion);

        descripcion = jtxtRazonSocial.getText().toUpperCase();

        Transaccion trans = new Transaccion(fecha, idTipoTransaccion, nroTipoTransaccion,
                idLugar, idTerminal, tipoMovimineto, estado, usuario, descripcion);

        idTransaccion = transDaoImpl.insertarTransaccion(trans);

        if (idTransaccion != 0) {
//            System.out.println("transaccion registrada nro: " + idTransaccion);
        } else {
//            System.out.println("error en el registro");
        }

        return idTransaccion;
    }

    public void registrarEntregaTransaccion(int idEntregaTransaccion, int idTransaccion) {
        TransaccionDAOImpl transDaoImpl = new TransaccionDAOImpl(connectionDB);
        transDaoImpl.insertarEntregaTransaccion(idTransaccion, idEntregaTransaccion);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jtProductos = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtTemporal = new javax.swing.JTable();
        jtxtNombreProducto = new javax.swing.JTextField();
        jtxtCantidad = new javax.swing.JTextField();
        jbSalir = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jtxtValorUnitario = new javax.swing.JTextField();
        jtxtxCriterio = new javax.swing.JTextField();
        jToggleButton3 = new javax.swing.JToggleButton();
        jbAgregar = new javax.swing.JButton();
        jlIdProducto = new javax.swing.JLabel();
        jlIdUnidadMedida = new javax.swing.JLabel();
        jtxtTotalTransaccion = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jtxtEliminar = new javax.swing.JButton();
        jbTransaccion = new javax.swing.JButton();
        jlTituloFormulario = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jlStockProducto = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jpanelCompraVenta = new javax.swing.JPanel();
        jlnit = new javax.swing.JLabel();
        jtxtNit = new javax.swing.JTextField();
        jlRazonSocial = new javax.swing.JLabel();
        jtxtRazonSocial = new javax.swing.JTextField();
        jlidClienteProveedor = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jlClienteProveedor = new javax.swing.JLabel();
        jcClienteProveedor = new javax.swing.JComboBox<>();
        jlDetalle = new javax.swing.JLabel();
        jtxtDetalle = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jtProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "idProducto", "idUnidadMedida", "Producto", "Simbolo", "Marca", "P/Venta"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jtProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtProductosMouseClicked(evt);
            }
        });
        jtProductos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtProductosKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtProductosKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jtProductos);
        if (jtProductos.getColumnModel().getColumnCount() > 0) {
            jtProductos.getColumnModel().getColumn(0).setMinWidth(0);
            jtProductos.getColumnModel().getColumn(0).setPreferredWidth(0);
            jtProductos.getColumnModel().getColumn(0).setMaxWidth(0);
            jtProductos.getColumnModel().getColumn(1).setMinWidth(0);
            jtProductos.getColumnModel().getColumn(1).setPreferredWidth(0);
            jtProductos.getColumnModel().getColumn(1).setMaxWidth(0);
            jtProductos.getColumnModel().getColumn(2).setMinWidth(200);
            jtProductos.getColumnModel().getColumn(2).setPreferredWidth(200);
            jtProductos.getColumnModel().getColumn(2).setMaxWidth(200);
            jtProductos.getColumnModel().getColumn(3).setMinWidth(50);
            jtProductos.getColumnModel().getColumn(3).setPreferredWidth(50);
            jtProductos.getColumnModel().getColumn(3).setMaxWidth(50);
            jtProductos.getColumnModel().getColumn(4).setMinWidth(60);
            jtProductos.getColumnModel().getColumn(4).setPreferredWidth(60);
            jtProductos.getColumnModel().getColumn(4).setMaxWidth(60);
            jtProductos.getColumnModel().getColumn(5).setMinWidth(60);
            jtProductos.getColumnModel().getColumn(5).setPreferredWidth(60);
            jtProductos.getColumnModel().getColumn(5).setMaxWidth(60);
        }

        jtTemporal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "idProducto", "idUnidadMedida", "Descripcion", "Simbolo", "Cantidad", "PrecioUnitario", "PrecioTotal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jtTemporal);
        if (jtTemporal.getColumnModel().getColumnCount() > 0) {
            jtTemporal.getColumnModel().getColumn(0).setMinWidth(0);
            jtTemporal.getColumnModel().getColumn(0).setPreferredWidth(0);
            jtTemporal.getColumnModel().getColumn(0).setMaxWidth(0);
            jtTemporal.getColumnModel().getColumn(1).setMinWidth(0);
            jtTemporal.getColumnModel().getColumn(1).setPreferredWidth(0);
            jtTemporal.getColumnModel().getColumn(1).setMaxWidth(0);
        }

        jtxtNombreProducto.setEnabled(false);
        jtxtNombreProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtNombreProductoActionPerformed(evt);
            }
        });

        jtxtCantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtCantidadActionPerformed(evt);
            }
        });

        jbSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/close_window.png"))); // NOI18N
        jbSalir.setText("Salir");
        jbSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSalirActionPerformed(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(153, 0, 51));
        jLabel1.setText("Producto");

        jLabel3.setForeground(new java.awt.Color(153, 0, 51));
        jLabel3.setText("Cantidad");

        jLabel4.setForeground(new java.awt.Color(153, 0, 51));
        jLabel4.setText("P/Unit");

        jtxtValorUnitario.setEditable(false);
        jtxtValorUnitario.setEnabled(false);

        jtxtxCriterio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtxCriterioKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtxCriterioKeyReleased(evt);
            }
        });

        jToggleButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Clear-icon.png"))); // NOI18N
        jToggleButton3.setText("Limpiar");
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });

        jbAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/aceptar.png"))); // NOI18N
        jbAgregar.setText("Agregar");
        jbAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAgregarActionPerformed(evt);
            }
        });

        jlIdProducto.setText("...");

        jlIdUnidadMedida.setText("...");

        jtxtTotalTransaccion.setEditable(false);
        jtxtTotalTransaccion.setEnabled(false);

        jLabel8.setForeground(new java.awt.Color(153, 0, 51));
        jLabel8.setText("Total");

        jtxtEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/trash_icon.png"))); // NOI18N
        jtxtEliminar.setText("Eliminar");
        jtxtEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtEliminarActionPerformed(evt);
            }
        });

        jbTransaccion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Configurar.png"))); // NOI18N
        jbTransaccion.setText("Transaccion");
        jbTransaccion.setAlignmentY(0.7F);
        jbTransaccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbTransaccionActionPerformed(evt);
            }
        });

        jlTituloFormulario.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        jlTituloFormulario.setForeground(new java.awt.Color(153, 0, 51));
        jlTituloFormulario.setText("titulo");

        jLabel5.setForeground(new java.awt.Color(153, 0, 51));
        jLabel5.setText("Stock Almacen");

        jlStockProducto.setText("...");

        jLabel2.setForeground(new java.awt.Color(153, 0, 51));
        jLabel2.setText("Criterio de Busqueda");

        jlnit.setForeground(new java.awt.Color(153, 0, 51));
        jlnit.setText("CI / NIT");

        jtxtNit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtNitKeyPressed(evt);
            }
        });

        jlRazonSocial.setForeground(new java.awt.Color(153, 0, 51));
        jlRazonSocial.setText("Razon Social");

        javax.swing.GroupLayout jpanelCompraVentaLayout = new javax.swing.GroupLayout(jpanelCompraVenta);
        jpanelCompraVenta.setLayout(jpanelCompraVentaLayout);
        jpanelCompraVentaLayout.setHorizontalGroup(
            jpanelCompraVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpanelCompraVentaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpanelCompraVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlnit)
                    .addComponent(jtxtNit, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpanelCompraVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlRazonSocial)
                    .addComponent(jtxtRazonSocial, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jpanelCompraVentaLayout.setVerticalGroup(
            jpanelCompraVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpanelCompraVentaLayout.createSequentialGroup()
                .addGroup(jpanelCompraVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlnit)
                    .addComponent(jlRazonSocial))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpanelCompraVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtNit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtRazonSocial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jlClienteProveedor.setForeground(new java.awt.Color(153, 0, 51));
        jlClienteProveedor.setText("Cliente");

        jcClienteProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcClienteProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcClienteProveedorActionPerformed(evt);
            }
        });

        jlDetalle.setForeground(new java.awt.Color(153, 0, 51));
        jlDetalle.setText("Detalle");

        jtxtDetalle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtDetalleKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jlClienteProveedor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcClienteProveedor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(2, 2, 2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jlDetalle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtxtDetalle)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jlClienteProveedor)
                    .addComponent(jcClienteProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jtxtDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlDetalle))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jbSalir)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jToggleButton3))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                                    .addComponent(jtxtxCriterio)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(jLabel2)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jlIdProducto)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jlIdUnidadMedida)
                                        .addGap(70, 70, 70)
                                        .addComponent(jLabel5)
                                        .addGap(18, 18, 18)
                                        .addComponent(jlStockProducto))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jbAgregar)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel1)
                                                .addComponent(jtxtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(18, 18, 18)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jtxtCantidad)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(jLabel3)
                                                    .addGap(0, 19, Short.MAX_VALUE)))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel4)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addGap(10, 10, 10)
                                                    .addComponent(jtxtValorUnitario, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jtxtEliminar)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel8)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jtxtTotalTransaccion, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 569, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jpanelCompraVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                                        .addComponent(jlidClienteProveedor)
                                        .addGap(95, 95, 95))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jbTransaccion)
                                        .addGap(6, 6, 6))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(472, 472, 472)
                        .addComponent(jlTituloFormulario)))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlTituloFormulario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jtxtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtxCriterio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jtxtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtValorUnitario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel4))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addComponent(jToggleButton3))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jlIdProducto)
                                    .addComponent(jlIdUnidadMedida)
                                    .addComponent(jLabel5)
                                    .addComponent(jlStockProducto)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jbAgregar)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jtxtTotalTransaccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jtxtEliminar))
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jlidClienteProveedor)
                                .addGap(42, 42, 42)
                                .addComponent(jbTransaccion))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jpanelCompraVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jbSalir, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(0, 19, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSalirActionPerformed
        dispose();
    }//GEN-LAST:event_jbSalirActionPerformed

    private void jtxtNombreProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtNombreProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtNombreProductoActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
//        TemporalDAOImpl tempDAOImpl = new TemporalDAOImpl(connectionTemp);  
//        tempDAOImpl.vaciarProductoTemp();
        limpiar();
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jtxtCantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtCantidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtCantidadActionPerformed

    private void jtProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtProductosMouseClicked
        seleccionarProducto();
    }//GEN-LAST:event_jtProductosMouseClicked

    private void jtProductosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtProductosKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_TAB) {
            jtxtCantidad.requestFocus();
        }
    }//GEN-LAST:event_jtProductosKeyPressed

    private void createEnterKeybindings(JTable table) {
        table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        table.getActionMap().put("Enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                jtxtCantidad.requestFocus();
            }
        });
    }

    private void jbAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAgregarActionPerformed
        boolean aux = true;
        if (jtxtCantidad.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Registrar la cantidad!!!");
            jtxtCantidad.setText("");
            jtxtCantidad.requestFocus();
            aux = false;
            return;
        }

        try {
            Double x = Double.parseDouble(jtxtCantidad.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "La cantidad registrada no es valida!!!");
            jtxtCantidad.setText("");
            jtxtCantidad.requestFocus();
            aux = false;
            return;
        }

        if (jtxtCantidad.getText().equals("0")) {
            JOptionPane.showMessageDialog(this, "La cantidad registrada no es valida!!!");
            jtxtCantidad.setText("");
            jtxtCantidad.requestFocus();
            aux = false;
            return;
        }

        try {
            Integer y = Integer.parseInt(jlIdProducto.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se ha seleccionado ningun producto!!!");
            jtxtCantidad.requestFocus();
            aux = false;
            return;
        }

        if (idTipoTransaccion == 2) { // solo para ventas
            ProductoDAOImpl prod = new ProductoDAOImpl(connectionDB);
            byte controlStock = prod.getControlStock(Integer.parseInt(jlIdProducto.getText()));

            if (controlStock == 1) {
                double stock = Double.valueOf(jlStockProducto.getText());
                if (Double.parseDouble(jtxtCantidad.getText()) > stock) {
                    JOptionPane.showMessageDialog(this, "No existe la suficiente cantidad del producto seleccionado en Almacen!!!");
                    aux = false;
                    jtxtCantidad.requestFocus();
                    return;
                }
            }
        }

        if (aux) {
            insertarTemp();
        }
        jtxtNombreProducto.setText("");
        jtxtCantidad.setText("");
        jtxtValorUnitario.setText("");
        jlStockProducto.setText("...");
    }//GEN-LAST:event_jbAgregarActionPerformed

    private void jtxtEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtEliminarActionPerformed
        eliminarProductoTemporal();
    }//GEN-LAST:event_jtxtEliminarActionPerformed

    private void jbTransaccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbTransaccionActionPerformed
        switch (idTipoTransaccion) {
            case 1:
                if (!validarRegistros()) {
                    return;
                }
                registrarCompra();
                break;
            case 2:
                if (!validarRegistros()) {
                    return;
                }
                registrarVenta();
                break;
            case 3:
                if (!validarRegistros()) {
                    return;
                }
                registrarCredito();
                break;
            case 6:
                if (!validarRegistros()) {
                    return;
                }
                registrarStockInicial();
                break;
        }


    }//GEN-LAST:event_jbTransaccionActionPerformed

    private void jtxtNitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtNitKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            if (jtxtNit.getText().equals("0")) {
                jtxtRazonSocial.setText("SIN NOMBRE");
            } else {
                FacturaVentaDAOImpl fac = new FacturaVentaDAOImpl(connectionDB);
                jtxtRazonSocial.setText(fac.getRazonSocialFactura(jtxtNit.getText()));
                jtxtRazonSocial.requestFocus();
            }
        }
    }//GEN-LAST:event_jtxtNitKeyPressed

    private void jtxtxCriterioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtxCriterioKeyReleased
        String criterio = "";
        if (jtxtxCriterio.getText().length() != 0) {

            criterio = jtxtxCriterio.getText();
        } else {
            criterio = "";
        }
        llenarTablaProductos(criterio);

    }//GEN-LAST:event_jtxtxCriterioKeyReleased

    private void jtProductosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtProductosKeyReleased
        seleccionarProducto();

    }//GEN-LAST:event_jtProductosKeyReleased

    private void jtxtxCriterioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtxCriterioKeyPressed

    }//GEN-LAST:event_jtxtxCriterioKeyPressed

    private void jcClienteProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcClienteProveedorActionPerformed
        seleccionarClienteProveedor();
    }//GEN-LAST:event_jcClienteProveedorActionPerformed

    private void jtxtDetalleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtDetalleKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtDetalleKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JButton jbAgregar;
    private javax.swing.JToggleButton jbSalir;
    private javax.swing.JButton jbTransaccion;
    private javax.swing.JComboBox<String> jcClienteProveedor;
    private javax.swing.JLabel jlClienteProveedor;
    private javax.swing.JLabel jlDetalle;
    private javax.swing.JLabel jlIdProducto;
    private javax.swing.JLabel jlIdUnidadMedida;
    private javax.swing.JLabel jlRazonSocial;
    private javax.swing.JLabel jlStockProducto;
    private javax.swing.JLabel jlTituloFormulario;
    private javax.swing.JLabel jlidClienteProveedor;
    private javax.swing.JLabel jlnit;
    private javax.swing.JPanel jpanelCompraVenta;
    private javax.swing.JTable jtProductos;
    private javax.swing.JTable jtTemporal;
    private javax.swing.JTextField jtxtCantidad;
    private javax.swing.JTextField jtxtDetalle;
    private javax.swing.JButton jtxtEliminar;
    private javax.swing.JTextField jtxtNit;
    private javax.swing.JTextField jtxtNombreProducto;
    private javax.swing.JTextField jtxtRazonSocial;
    private javax.swing.JTextField jtxtTotalTransaccion;
    private javax.swing.JTextField jtxtValorUnitario;
    private javax.swing.JTextField jtxtxCriterio;
    // End of variables declaration//GEN-END:variables

    private void vaciarProductosTemporales() {
        TemporalDAOImpl tempDAOImpl = new TemporalDAOImpl(connectionTemp);
        tempDAOImpl.vaciarProductoTemp();
    }
}
