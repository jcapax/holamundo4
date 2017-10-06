/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package almacenes.vistas;

import almacenes.conectorDB.DatabaseUtils;
import almacenes.model.Producto;
import dao.ProductoDAO;
import dao.ProductoDAOImpl;
import dao.RubroDAOImpl;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author jcapax
 */
public class FormProducto extends javax.swing.JFrame {

    DefaultTableModel dtm;
    private DatabaseUtils databaseUtils;
    private Connection connectionDB;
    private String usuario;
    private int clickImagen = 0;

    FileInputStream fileInputStream;
    int longitudBytes;

    public FormProducto() {
        initComponents();
    }

    public void headerTabla() {
        Font f = new Font("Times New Roman", Font.BOLD, 13);

        jtProductos.getTableHeader().setFont(f);
        jtProductos.getTableHeader().setBackground(Color.orange);
    }

    public void llenarTablaProductos() {
        DecimalFormat df = new DecimalFormat("###,##0.00");

        ProductoDAOImpl prodDAOImpl = new ProductoDAOImpl(connectionDB);

        ArrayList<Producto> lProd = new ArrayList<>();

        lProd = prodDAOImpl.getListaProductos();

        dtm = (DefaultTableModel) this.jtProductos.getModel();
        dtm.setRowCount(0);

        jtProductos.setModel(dtm);

        Object[] fila = new Object[7];

        boolean aux = true;

        for (int i = 0; i < lProd.size(); i++) {
            fila[0] = lProd.get(i).getId();
            fila[1] = lProd.get(i).getIdRubroProducto();                        
            fila[2] = lProd.get(i).getRubro();
            fila[3] = lProd.get(i).getDescripcion();
            fila[4] = lProd.get(i).getPrecioVenta();
            fila[5] = lProd.get(i).getPrecioCompra();            
            fila[6] = (lProd.get(i).getEstado().equals("V")) ? true : false;            
            
            dtm.addRow(fila);
        }

        TableColumnModel columnModel = jtProductos.getColumnModel();
//
//        TableColumn colStockMin = columnModel.getColumn(13);
//        TableColumn colPVenta = columnModel.getColumn(14);
//        TableColumn colPCompra = columnModel.getColumn(17);
//
//        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
//        renderer.setHorizontalAlignment(jLabel4.RIGHT);
//
//        colStockMin.setCellRenderer(renderer);
//        colPVenta.setCellRenderer(renderer);
//        colPCompra.setCellRenderer(renderer);

        jtProductos.setModel(dtm);
    }

    public void guardarProducto() {

        int idRubroproducto = Integer.valueOf(jlIdRubroProducto.getText());

        String estado;
        int controlStock;

        String descripcion = jtxtNombreProducto.getText().toUpperCase();

        if (jchEstado.isSelected()) {
            estado = "V";
        } else {
            estado = "A";
        }

        ProductoDAOImpl prodDAOImpl = new ProductoDAOImpl(connectionDB);

        if (ljEditar.getText().equals("1")) {
            Producto producto = new Producto();            
            producto.setDescripcion(descripcion.toUpperCase());
            producto.setEstado(estado);
            producto.setId(Integer.parseInt(jlIdProducto.getText()));
            producto.setIdRubroProducto(idRubroproducto);
            producto.setUsuario(usuario);
            producto.setPrecioVenta(Double.valueOf(jtxtPrecioVenta.getText()));
            producto.setPrecioCompra(Double.valueOf(jtxtPrecioCompra.getText()));

            if(clickImagen==1){
                prodDAOImpl.editarProducto(producto, fileInputStream, longitudBytes);
            }
            else{
                prodDAOImpl.editarProducto(producto);
            }            
        } else {
            Producto producto = new Producto("", idRubroproducto, descripcion, "",
                    estado, usuario);

            if(clickImagen==1){
                prodDAOImpl.insertarProducto(producto, this.fileInputStream, this.longitudBytes);
            }else{
                prodDAOImpl.insertarProducto(producto);
            }            
        }

        llenarTablaProductos();
    }

    public FormProducto(Connection connectionDB, String usuario) {
        initComponents();
        this.setLocationRelativeTo(null);
        ljEditar.setVisible(false);
        jlIdProducto.setVisible(false);
        jbGuardar.setEnabled(false);

        this.databaseUtils = new DatabaseUtils();
        this.connectionDB = connectionDB;
        this.usuario = usuario;

        headerTabla();

        llenarComboRubro();
        llenarTablaProductos();
    }

    public void llenarComboRubro() {

        String sel = "Sel";

        jcRubro.removeAllItems();
        jcRubro.addItem(sel);

        RubroDAOImpl rubroDAOImpl = new RubroDAOImpl(connectionDB);

        HashMap<String, Integer> map = rubroDAOImpl.rubroClaveValor();

        for (String s : map.keySet()) {
            jcRubro.addItem(s.toString());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jbGuardar = new javax.swing.JButton();
        jlIdMarca = new javax.swing.JLabel();
        jlIdProcedencia = new javax.swing.JLabel();
        jlIdRubroProducto = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtProductos = new javax.swing.JTable();
        jbEditar = new javax.swing.JButton();
        ljEditar = new javax.swing.JLabel();
        jlIdProducto = new javax.swing.JLabel();
        jbCancelar = new javax.swing.JButton();
        jbNuevo = new javax.swing.JButton();
        jlTituloFormulario = new javax.swing.JLabel();
        jbSalir = new javax.swing.JButton();
        jlImage = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jcRubro = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jtxtNombreProducto = new javax.swing.JTextField();
        jtxtPrecioVenta = new javax.swing.JTextField();
        jtxtPrecioCompra = new javax.swing.JTextField();
        jchEstado = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        jbGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Save-icon.png"))); // NOI18N
        jbGuardar.setText("Guardar");
        jbGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbGuardarActionPerformed(evt);
            }
        });

        jtProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "idRubro", "Rubro", "Descripcion", "Precio Venta", "Precio Compra", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

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
            jtProductos.getColumnModel().getColumn(2).setMinWidth(80);
            jtProductos.getColumnModel().getColumn(2).setPreferredWidth(80);
            jtProductos.getColumnModel().getColumn(2).setMaxWidth(80);
            jtProductos.getColumnModel().getColumn(3).setMinWidth(250);
            jtProductos.getColumnModel().getColumn(3).setPreferredWidth(250);
            jtProductos.getColumnModel().getColumn(3).setMaxWidth(250);
            jtProductos.getColumnModel().getColumn(4).setMinWidth(110);
            jtProductos.getColumnModel().getColumn(4).setPreferredWidth(110);
            jtProductos.getColumnModel().getColumn(4).setMaxWidth(110);
            jtProductos.getColumnModel().getColumn(5).setMinWidth(110);
            jtProductos.getColumnModel().getColumn(5).setPreferredWidth(110);
            jtProductos.getColumnModel().getColumn(5).setMaxWidth(110);
            jtProductos.getColumnModel().getColumn(6).setMinWidth(60);
            jtProductos.getColumnModel().getColumn(6).setPreferredWidth(60);
            jtProductos.getColumnModel().getColumn(6).setMaxWidth(60);
        }

        jbEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/editar.png"))); // NOI18N
        jbEditar.setText("Editar");
        jbEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEditarActionPerformed(evt);
            }
        });

        jbCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/exit.png"))); // NOI18N
        jbCancelar.setText("Cancelar");
        jbCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCancelarActionPerformed(evt);
            }
        });

        jbNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/folder-add-icon.png"))); // NOI18N
        jbNuevo.setText("Nuevo");
        jbNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbNuevoActionPerformed(evt);
            }
        });

        jlTituloFormulario.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        jlTituloFormulario.setForeground(new java.awt.Color(153, 0, 51));
        jlTituloFormulario.setText("Registro de Productos");

        jbSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/close_window.png"))); // NOI18N
        jbSalir.setText("Salir");
        jbSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSalirActionPerformed(evt);
            }
        });

        jlImage.setBackground(new java.awt.Color(204, 204, 204));
        jlImage.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jlImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlImage.setText("Cargar Imagen (Click)");
        jlImage.setOpaque(true);
        jlImage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlImageMouseClicked(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(153, 0, 51));
        jLabel3.setText("Rubro");

        jcRubro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcRubro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcRubroActionPerformed(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(153, 0, 51));
        jLabel4.setText("Nombre prod.");

        jtxtPrecioVenta.setText("0");

        jtxtPrecioCompra.setText("0");

        jchEstado.setForeground(new java.awt.Color(153, 0, 51));
        jchEstado.setText("Estado");
        jchEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchEstadoActionPerformed(evt);
            }
        });

        jLabel5.setForeground(new java.awt.Color(153, 0, 51));
        jLabel5.setText("Precio Venta");

        jLabel6.setForeground(new java.awt.Color(153, 0, 51));
        jLabel6.setText("Precio Compra");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtxtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcRubro, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jchEstado)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jtxtPrecioCompra, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                        .addComponent(jtxtPrecioVenta, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(jcRubro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jtxtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))
                                .addGap(18, 18, 18)
                                .addComponent(jtxtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addComponent(jtxtPrecioCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6))
                .addGap(28, 28, 28)
                .addComponent(jchEstado)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(582, 582, 582)
                                .addComponent(jlIdProcedencia)
                                .addGap(269, 269, 269)
                                .addComponent(jlIdRubroProducto))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 827, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(305, 305, 305)
                                .addComponent(jlIdMarca)
                                .addGap(324, 324, 324)
                                .addComponent(ljEditar)
                                .addGap(204, 204, 204)
                                .addComponent(jlIdProducto))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jbNuevo)
                                        .addGap(18, 18, 18)
                                        .addComponent(jbGuardar)
                                        .addGap(18, 18, 18)
                                        .addComponent(jbEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jbCancelar)
                                        .addGap(18, 18, 18)
                                        .addComponent(jbSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(32, 32, 32)))
                                .addGap(18, 18, 18)
                                .addComponent(jlImage, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(366, 366, 366)
                        .addComponent(jlTituloFormulario)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jlTituloFormulario)
                .addGap(3, 3, 3)
                .addComponent(jlIdMarca)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlIdProcedencia)
                    .addComponent(jlIdRubroProducto))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlImage, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbNuevo)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jbEditar)
                        .addComponent(ljEditar)
                        .addComponent(jlIdProducto)
                        .addComponent(jbCancelar)
                        .addComponent(jbGuardar)
                        .addComponent(jbSalir)))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jchEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchEstadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jchEstadoActionPerformed

    private void jcRubroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcRubroActionPerformed
        seleccionarElementoRubro();
    }//GEN-LAST:event_jcRubroActionPerformed

    private void jbGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbGuardarActionPerformed
        byte x = 2;
        botones(x);
        guardarProducto();
        clickImagen = 0;
    }//GEN-LAST:event_jbGuardarActionPerformed

    private void jtProductosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtProductosKeyReleased
        seleccionarProducto();
    }//GEN-LAST:event_jtProductosKeyReleased

    private void jtProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtProductosMouseClicked
        seleccionarProducto();
    }//GEN-LAST:event_jtProductosMouseClicked

    private void jbEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEditarActionPerformed
        byte x = 3;
        botones(x);
        ljEditar.setText("1");
    }//GEN-LAST:event_jbEditarActionPerformed

    private void jbNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbNuevoActionPerformed
        byte x = 1;
        botones(x);
        ljEditar.setText("0");
        clickImagen = 1;
        jlImage.setIcon(null);
        jlImage.updateUI();
    }//GEN-LAST:event_jbNuevoActionPerformed

    private void jbCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelarActionPerformed
        byte x = 4;
        botones(x);
        clickImagen = 0;
    }//GEN-LAST:event_jbCancelarActionPerformed

    private void jbSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSalirActionPerformed
        dispose();
    }//GEN-LAST:event_jbSalirActionPerformed

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained

    }//GEN-LAST:event_formFocusGained

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        llenarTablaProductos();
    }//GEN-LAST:event_formWindowGainedFocus

    private void jlImageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlImageMouseClicked
        if (ljEditar.getText().equals("1") || clickImagen == 1) {
            clickImagen = 1;
            
            jlImage.setIcon(null);
            jlImage.updateUI();
            
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int estado = jFileChooser.showOpenDialog(null);
            if (estado == JFileChooser.APPROVE_OPTION) {
                try {
                    fileInputStream = new FileInputStream(jFileChooser.getSelectedFile());
                    longitudBytes = (int) jFileChooser.getSelectedFile().length();
                    Image image = ImageIO.read(jFileChooser.getSelectedFile()).getScaledInstance(jlImage.getWidth(), jlImage.getHeight(), Image.SCALE_DEFAULT);
                    jlImage.setIcon(new ImageIcon(image));
                    jlImage.updateUI();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(FormProducto.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(FormProducto.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_jlImageMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FormProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormProducto().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbCancelar;
    private javax.swing.JButton jbEditar;
    private javax.swing.JButton jbGuardar;
    private javax.swing.JButton jbNuevo;
    private javax.swing.JButton jbSalir;
    private javax.swing.JComboBox<String> jcRubro;
    private javax.swing.JCheckBox jchEstado;
    private javax.swing.JLabel jlIdMarca;
    private javax.swing.JLabel jlIdProcedencia;
    private javax.swing.JLabel jlIdProducto;
    private javax.swing.JLabel jlIdRubroProducto;
    private javax.swing.JLabel jlImage;
    private javax.swing.JLabel jlTituloFormulario;
    private javax.swing.JTable jtProductos;
    private javax.swing.JTextField jtxtNombreProducto;
    private javax.swing.JTextField jtxtPrecioCompra;
    private javax.swing.JTextField jtxtPrecioVenta;
    private javax.swing.JLabel ljEditar;
    // End of variables declaration//GEN-END:variables

    private void seleccionarElementoRubro() {
        jlIdRubroProducto.setVisible(false);
        String sel = null;

        String comp = "Sel";

        RubroDAOImpl rubroDAOImpl = new RubroDAOImpl(connectionDB);

        HashMap<String, Integer> map = rubroDAOImpl.rubroClaveValor();

        try {
            sel = jcRubro.getSelectedItem().toString();

//            System.out.println("elemento seleccionado "+ sel);
            if (sel.equals(comp)) {
                jlIdRubroProducto.setText("0");
            } else {
                jlIdRubroProducto.setText(map.get(sel).toString());
            }
        } catch (Exception e) {
        }
    }

    private void seleccionarProducto() {       
        
        int fila = jtProductos.getSelectedRow();
        int idProducto = Integer.valueOf(jtProductos.getValueAt(fila, 0).toString());

        jlIdProducto.setText(String.valueOf(idProducto));

        String rubro = jtProductos.getValueAt(fila, 2).toString();
        jcRubro.setSelectedItem(rubro);

        jtxtNombreProducto.setText(jtProductos.getValueAt(fila, 3).toString());
        jtxtPrecioVenta.setText(jtProductos.getValueAt(fila, 4).toString());
        jtxtPrecioCompra.setText(jtProductos.getValueAt(fila, 5).toString());

        jchEstado.setSelected(Boolean.parseBoolean(jtProductos.getValueAt(fila, 6).toString()));

        recuperarImagen(idProducto);
    }

    public void recuperarImagen(int idProducto) {
        ProductoDAO p = new ProductoDAOImpl(connectionDB);

        int width = jlImage.getWidth();
        int height = jlImage.getHeight();

        if (p.getImage(idProducto, width, height) == null) {
            jlImage.setIcon(null);
            jlImage.updateUI();
        } else {            
            jlImage.setIcon(p.getImage(idProducto, width-20, height-20));
            jlImage.updateUI();
        }
    }

    public static BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = (Graphics2D) bi.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        return bi;
    }

    public void botones(byte x) {
        switch (x) {
            case 1: //nuevo
                jbNuevo.setEnabled(false);
                jbGuardar.setEnabled(true);
                jbEditar.setEnabled(false);
                jbCancelar.setEnabled(true);
                limpiar();
                break;
            case 2: //guardar
                jbNuevo.setEnabled(true);
                jbGuardar.setEnabled(false);
                jbEditar.setEnabled(true);
                jbCancelar.setEnabled(false);

                break;
            case 3: // editar
                jbNuevo.setEnabled(false);
                jbGuardar.setEnabled(true);
                jbEditar.setEnabled(false);
                jbCancelar.setEnabled(true);
                break;
            case 4: // cancelar
                jbNuevo.setEnabled(true);
                jbGuardar.setEnabled(false);
                jbEditar.setEnabled(true);
                jbCancelar.setEnabled(false);
                break;
        }
    }

    private void limpiar() {
        llenarComboRubro();
        jtxtNombreProducto.setText("");
        jchEstado.setSelected(false);
    }
}
