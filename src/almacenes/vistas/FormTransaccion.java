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
import almacenes.model.Producto;
import almacenes.model.Transaccion;
import dao.ArqueoDAOImpl;
import dao.CajaDAOImpl;
import dao.CreditoDAO;
import dao.CreditoDAOImpl;
import dao.DetalleTransaccionDAOImpl;
import dao.FacturaVentaDAOImpl;
import dao.ProductoDAO;
import dao.TemporalDAOImpl;
import dao.ProductoDAOImpl;
import dao.TransaccionDAOImpl;
import dao.reportes.ReporteCreditoDAO;
import dao.reportes.ReporteCreditoDAOImpl;
import dao.reportes.ReporteFacturacionDAOImpl;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author jcapax
 */
public class FormTransaccion extends javax.swing.JFrame {

    private DatabaseUtils databaseUtils;
    private Connection connectionDB, connectionTemp;
    private DefaultTableModel dtm;
    private int idTipoTransaccion; // tipo de proceso q se ejecutara
    private int idTipoTransaccionEntrega;  // tipo de entrega q se ejecutara
    private byte idLugar;
    private String usuario;
    private DecimalFormat df;

    //private static final int SHIFT_OPTION_MASK = InputEvent.ALT_MASK | InputEvent.SHIFT_MASK;
    private static final int SHIFT_OPTION_MASK = 17;

//    DefaultTableModel dtm;
    public FormTransaccion(Connection connectionDB,
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

        llenarProductos();

//        createEnterKeybindings(jtProductos);
    }

    public void headerTabla() {
        Font f = new Font("Times New Roman", Font.BOLD, 13);
        Font fh = new Font("Times New Roman", Font.BOLD, 20);

//        jtProductos.getTableHeader().setFont(f);
//        jtProductos.getTableHeader().setBackground(Color.orange);
        jtTemporal.getTableHeader().setFont(f);
        jtTemporal.getTableHeader().setBackground(Color.orange);

        JTableHeader header = jtTemporal.getTableHeader();
        header.setForeground(new Color(153, 0, 51));
        header.setFont(fh);
    }

    public void llenarProductos() {
        ProductoDAO prodDAOImpl = new ProductoDAOImpl(connectionDB);

        int width = 150;
        int height = width;
        int lowimage = 30;

        ArrayList<Producto> lProd = new ArrayList<Producto>();
        lProd = prodDAOImpl.getListaProductos();
        for (int i = 0; i < lProd.size(); i++) {
            JButton bot = new JButton();
            bot.setVisible(true);
            bot.setName(String.valueOf(lProd.get(i).getId()));
            bot.setText(lProd.get(i).getDescripcion());
            bot.setPreferredSize(new Dimension(width, height));
            bot.setIcon(prodDAOImpl.getImage(lProd.get(i).getId(), width - lowimage, height - lowimage));
            bot.setHorizontalTextPosition(AbstractButton.CENTER);
            bot.setVerticalTextPosition(AbstractButton.BOTTOM);
            bot.updateUI();

            bot.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String[] cade = e.toString().split(" ");
                    boolean aux = false;
                    int idProducto = Integer.parseInt(cade[cade.length - 1]);
                    if ((e.getModifiers() == SHIFT_OPTION_MASK)) {
                        TemporalDAOImpl tempDAO = new TemporalDAOImpl(connectionTemp);
                        aux = true;
                        tempDAO.eliminarProdcutoTemp(idProducto, 0, true);

                        llenarTablaTemporal();
                    }
                    insertarTemp(idProducto, aux);
                }
            });

            jPanelProductos.add(bot);
        }
    }

    public void iniciarComponentes() {
        byte idTerminal = 1;

        ArqueoDAOImpl arq = new ArqueoDAOImpl(connectionDB);

        llenarTablaProductos("");
//        jtxtDetalle.setText("");

//        jlIdProducto.setVisible(false);
//        jlIdUnidadMedida.setVisible(false);
        jlidClienteProveedor.setText("0");
        jlidClienteProveedor.setVisible(false);
//        jcClienteProveedor.removeAllItems();

        switch (idTipoTransaccion) {
            case 1:  //compras
                jbTransaccion.setVisible(true);
                jlTituloFormulario.setText("COMPRAS");

                jlnit.setEnabled(false);
                jtxtNit.setEnabled(false);

//                jtxtValorUnitario.setEnabled(true);
//                jtxtValorUnitario.setEditable(true);
//                jtxtValorUnitario.setText("");
//                jlClienteProveedor.setVisible(false);
//                jcClienteProveedor.setVisible(false);
//                jlDetalle.setVisible(false);
//                jtxtDetalle.setVisible(false);
                break;
            case 2:
                jbTransaccion.setVisible(true);
                jlTituloFormulario.setText("VENTAS");

                jlnit.setVisible(true);
                jtxtNit.setVisible(true);

//                jtxtValorUnitario.setEnabled(false);
//                jtxtValorUnitario.setEditable(false);
//                jlClienteProveedor.setEnabled(false);
//                jcClienteProveedor.setEnabled(false);
//
//                jlDetalle.setEnabled(false);
//                jtxtDetalle.setEnabled(false);
                break;
            case 3:
                jbTransaccion.setVisible(true);
                jlTituloFormulario.setText("CREDITO VENTAS");

                jlnit.setEnabled(false);
                jtxtNit.setEnabled(false);

                jlRazonSocial.setEnabled(false);
                jtxtRazonSocial.setEnabled(false);

//                jtxtValorUnitario.setEnabled(false);
//                jtxtValorUnitario.setEditable(false);
//                jlClienteProveedor.setEnabled(true);
//                jcClienteProveedor.setEnabled(true);
//
//                jlDetalle.setVisible(true);
//                jtxtDetalle.setVisible(true);
                String tipo = "C";//cliente
                llenarClienteProveedor(tipo);
                break;
            case 6:
                jlnit.setVisible(false);
                jtxtNit.setVisible(false);

                jbTransaccion.setVisible(true);
                jlTituloFormulario.setText("AJUSTE STOCK");
//                jtxtValorUnitario.setEnabled(true);
//                jtxtValorUnitario.setEditable(true);
//                jtxtValorUnitario.setText("");

//                jlClienteProveedor.setEnabled(false);
//                jcClienteProveedor.setEnabled(false);
//
//                jlDetalle.setEnabled(false);
//                jtxtDetalle.setEnabled(false);
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
//        if (idTipoTransaccion == 3) {
//            if (jlClienteProveedor.getText().equals("0")) {
//                JOptionPane.showMessageDialog(this, "Debe seleccionar un Cliente/Proveedor!!!");
//                aux = false;
//            }
//        }

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
//        String detalle = jtxtDetalle.getText().toUpperCase();

//        int idTipoTransaccion = 3; // credito -  plata
        idTransaccion = resgistrarTransaccion(idTipoTransaccion);
        registrarDetalleTransaccion(idTransaccion);

//        int idTipoTransaccionEntrega = 8; // entrega - material
        idEntregaTransaccion = resgistrarTransaccion(idTipoTransaccionEntrega);

        registrarDetalleTransaccion(idEntregaTransaccion);

        registrarEntregaTransaccion(idEntregaTransaccion, idTransaccion);

//        insertarCredito(idTransaccion, idClienteProveedor, detalle);
        ReporteCreditoDAO rep = new ReporteCreditoDAOImpl(connectionDB, usuario);
        rep.vistaPreviaCredito(idTransaccion);

        limpiar();

        JOptionPane.showMessageDialog(this, "Credito generado con exito");
        vaciarProductosTemporales();
        iniciarComponentes();

    }

    public void insertarCredito(int idTransaccion, int idClienteProveedor, String detalle) {
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

    public void eliminarProductoTemporal(boolean dynamic) {
        TemporalDAOImpl tempDAO = new TemporalDAOImpl(connectionTemp);

        int filSel = jtTemporal.getSelectedRow();
        int idProducto = (int) jtTemporal.getValueAt(filSel, 0);

        if (dynamic) {
            tempDAO.eliminarProdcutoTemp(idProducto, 0, false);
        } else {
            tempDAO.eliminarProdcutoTemp(idProducto, 1);
        }

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

    public void insertarTemp(int idProducto, boolean delete) {
        ProductoDAO p = new ProductoDAOImpl(connectionDB);
        Temporal temp = new Temporal();

        ArrayList<Producto> getP = p.getProducto(idProducto);

        int idUnidadMedida = 1;
        String nombreProducto = getP.get(0).getDescripcion();
        String simbolo = "";
        double cantidad = 1;
        double valorUnitario = getP.get(0).getPrecioVenta();
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

        if (!delete) {
            tempDAOImpl.insertarProductoTemp(temp);
        }
        llenarTablaTemporal();
    }

    public void seleccionarProducto() {

//        UnidadProductoDAOImlp up = new UnidadProductoDAOImlp(connectionDB);
//
//        int filSel = jtProductos.getSelectedRow();
//
//        int idProducto = (int) jtProductos.getValueAt(filSel, 0);
//        int idUnidadMedida = (int) jtProductos.getValueAt(filSel, 1);
//        String nombreProducto = jtProductos.getValueAt(filSel, 2).toString();
//        Double valorUnitario = (double) jtProductos.getValueAt(filSel, 5);
//
//        double stock = up.getStockProducto(idProducto, idUnidadMedida, idLugar);
//
//        jtxtNombreProducto.setText(nombreProducto);
//        jtxtValorUnitario.setText(valorUnitario.toString());
//        jlIdProducto.setText(String.valueOf(idProducto));
//        jlIdUnidadMedida.setText(String.valueOf(idUnidadMedida));
//
//        jlStockProducto.setText(String.valueOf(stock));
    }

    public void llenarTablaProductos(String criterio) {
//        ProductoDAOImpl prodDAOImpl = new ProductoDAOImpl(connectionDB);
//
//        ArrayList<ListaProductos> lProd = new ArrayList<ListaProductos>();
//
//        lProd = prodDAOImpl.getListaProductosVenta(criterio);
//
//        dtm = (DefaultTableModel) this.jtProductos.getModel();
//        dtm.setRowCount(0);
//
//        jtProductos.setModel(dtm);
//
//        Object[] fila = new Object[19];
//
////        System.out.println("nro de registros en pila: " + lProd.size());
//        for (int i = 0; i < lProd.size(); i++) {
//            fila[0] = lProd.get(i).getId();
//            fila[1] = lProd.get(i).getIDUNIDADMEDIDA();
//            fila[2] = lProd.get(i).getDescripcion();
//            fila[3] = lProd.get(i).getNombreUnidadMedida();
//            fila[4] = lProd.get(i).getMarca();
//            fila[5] = lProd.get(i).getPRECIOVENTA();
//            dtm.addRow(fila);
//        }
//
//        jtProductos.setAutoscrolls(false);
//        jtProductos.setModel(dtm);
    }

    public void llenarClienteProveedor(String tipo) {
//        String sel = "Sel";
//
//        jcClienteProveedor.removeAllItems();
//        jcClienteProveedor.addItem(sel);
//
//        ClienteProveedorDAO cp = new ClienteProveedorDAOImpl(connectionDB);
//
//        TreeMap<String, Integer> map = cp.clienteProveedorClaveValor(tipo);
//
//        for (String s : map.keySet()) {
//            jcClienteProveedor.addItem(s.toString());
//        }
    }

    private void seleccionarClienteProveedor() {

//        String sel = null;
//        String tipo = "C";
//        String comp = "Sel";
//
//        ClienteProveedorDAO cp = new ClienteProveedorDAOImpl(connectionDB);
//
//        TreeMap<String, Integer> map = cp.clienteProveedorClaveValor(tipo);
//
//        try {
//            sel = jcClienteProveedor.getSelectedItem().toString();
//
//            if (sel.equals(comp)) {
//                jlidClienteProveedor.setText("0");
//            } else {
//                jlidClienteProveedor.setText(map.get(sel).toString());
//            }
//        } catch (Exception e) {
//        }
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

        jlidClienteProveedor = new javax.swing.JLabel();
        jPanelProductos = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtTemporal = new javax.swing.JTable();
        jtxtEliminar = new javax.swing.JButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jLabel8 = new javax.swing.JLabel();
        jtxtTotalTransaccion = new javax.swing.JTextField();
        jpanelCompraVenta = new javax.swing.JPanel();
        jlnit = new javax.swing.JLabel();
        jtxtNit = new javax.swing.JTextField();
        jlRazonSocial = new javax.swing.JLabel();
        jtxtRazonSocial = new javax.swing.JTextField();
        jbTransaccion = new javax.swing.JButton();
        jbSalir = new javax.swing.JToggleButton();
        jlTituloFormulario = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1900, 830));

        jPanelProductos.setBackground(new java.awt.Color(243, 250, 180));

        jtTemporal.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jtTemporal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "idProducto", "idUnidadMedida", "Descripcion", "Simbolo", "Cantidad", "P/Unit.", "P/Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

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
            jtTemporal.getColumnModel().getColumn(2).setMinWidth(280);
            jtTemporal.getColumnModel().getColumn(2).setPreferredWidth(280);
            jtTemporal.getColumnModel().getColumn(2).setMaxWidth(280);
            jtTemporal.getColumnModel().getColumn(3).setMinWidth(0);
            jtTemporal.getColumnModel().getColumn(3).setPreferredWidth(0);
            jtTemporal.getColumnModel().getColumn(3).setMaxWidth(0);
            jtTemporal.getColumnModel().getColumn(4).setMinWidth(100);
            jtTemporal.getColumnModel().getColumn(4).setPreferredWidth(100);
            jtTemporal.getColumnModel().getColumn(4).setMaxWidth(100);
            jtTemporal.getColumnModel().getColumn(5).setMinWidth(100);
            jtTemporal.getColumnModel().getColumn(5).setPreferredWidth(100);
            jtTemporal.getColumnModel().getColumn(5).setMaxWidth(100);
            jtTemporal.getColumnModel().getColumn(6).setMinWidth(120);
            jtTemporal.getColumnModel().getColumn(6).setPreferredWidth(120);
            jtTemporal.getColumnModel().getColumn(6).setMaxWidth(120);
        }

        jtxtEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/trash_icon.png"))); // NOI18N
        jtxtEliminar.setText("Eliminar");
        jtxtEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtEliminarActionPerformed(evt);
            }
        });

        jToggleButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Clear-icon.png"))); // NOI18N
        jToggleButton3.setText("Limpiar");
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });

        jLabel8.setForeground(new java.awt.Color(153, 0, 51));
        jLabel8.setText("Total");

        jtxtTotalTransaccion.setEditable(false);
        jtxtTotalTransaccion.setEnabled(false);

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

        jbTransaccion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Configurar.png"))); // NOI18N
        jbTransaccion.setText("Transaccion");
        jbTransaccion.setAlignmentY(0.7F);
        jbTransaccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbTransaccionActionPerformed(evt);
            }
        });

        jbSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/close_window.png"))); // NOI18N
        jbSalir.setText("Salir");
        jbSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 569, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jtxtEliminar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton3))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(436, 436, 436)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtxtTotalTransaccion, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(453, 453, 453)
                                .addComponent(jbSalir))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jpanelCompraVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbTransaccion)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtTotalTransaccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jtxtEliminar)
                    .addComponent(jToggleButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpanelCompraVenta, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbTransaccion, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(17, 17, 17)
                .addComponent(jbSalir)
                .addGap(14, 14, 14))
        );

        jlTituloFormulario.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        jlTituloFormulario.setForeground(new java.awt.Color(153, 0, 51));
        jlTituloFormulario.setText("titulo");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jlTituloFormulario)
                    .addComponent(jPanelProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 823, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlidClienteProveedor)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jlTituloFormulario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanelProductos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(198, 198, 198)
                        .addComponent(jlidClienteProveedor)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSalirActionPerformed
        dispose();
    }//GEN-LAST:event_jbSalirActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
//        TemporalDAOImpl tempDAOImpl = new TemporalDAOImpl(connectionTemp);  
//        tempDAOImpl.vaciarProductoTemp();
        limpiar();
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void createEnterKeybindings(JTable table) {
        table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        table.getActionMap().put("Enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
//                jtxtCantidad.requestFocus();
            }
        });
    }

    private void jtxtEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtEliminarActionPerformed
        boolean dynamic = false;
        eliminarProductoTemporal(dynamic);
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelProductos;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jbSalir;
    private javax.swing.JButton jbTransaccion;
    private javax.swing.JLabel jlRazonSocial;
    private javax.swing.JLabel jlTituloFormulario;
    private javax.swing.JLabel jlidClienteProveedor;
    private javax.swing.JLabel jlnit;
    private javax.swing.JPanel jpanelCompraVenta;
    private javax.swing.JTable jtTemporal;
    private javax.swing.JButton jtxtEliminar;
    private javax.swing.JTextField jtxtNit;
    private javax.swing.JTextField jtxtRazonSocial;
    private javax.swing.JTextField jtxtTotalTransaccion;
    // End of variables declaration//GEN-END:variables

    private void vaciarProductosTemporales() {
        TemporalDAOImpl tempDAOImpl = new TemporalDAOImpl(connectionTemp);
        tempDAOImpl.vaciarProductoTemp();
    }
}
