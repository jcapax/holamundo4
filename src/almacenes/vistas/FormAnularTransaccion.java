/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package almacenes.vistas;

import almacenes.conectorDB.DatabaseUtils;
import almacenes.model.AnularTransaccion;
import dao.AnularTransaccionDAOImpl;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jcapax
 */
public class FormAnularTransaccion extends javax.swing.JFrame {

    private DatabaseUtils databaseUtils;
    private Connection connectionDB;
    DefaultTableModel dtm;
    String usuario;
    
    public FormAnularTransaccion(Connection connectionDB, String usuario) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.databaseUtils = new DatabaseUtils();
        this.connectionDB = connectionDB;
        this.usuario = usuario;
                
        headerTabla();
        llenarTablaAnularTransaccion();
    }

    public FormAnularTransaccion() {
        initComponents();
    }
    
    public void headerTabla(){
        Font f = new Font("Times New Roman", Font.BOLD, 13);
        
        jtAnularComprobante.getTableHeader().setFont(f);
        jtAnularComprobante.getTableHeader().setBackground(Color.orange);
        
    }

    public void anularTransaccion() {
        AnularTransaccionDAOImpl anuTrans = new AnularTransaccionDAOImpl(connectionDB);

        int idTransaccion = 0;
        int idEntregaTransaccion = 0;

        int filSel = jtAnularComprobante.getSelectedRow();

        if (filSel != -1) {

            idEntregaTransaccion = (int) jtAnularComprobante.getValueAt(filSel, 0);
            idTransaccion = (int) jtAnularComprobante.getValueAt(filSel, 1);

            anuTrans.anularCaja(idTransaccion);
            anuTrans.anularFactura(idTransaccion);
            anuTrans.anularTrans(idTransaccion, idEntregaTransaccion);
            
            JOptionPane.showMessageDialog(this, "Transaccion Anulada");
            
            llenarTablaAnularTransaccion();
        }

    }

    public void llenarTablaAnularTransaccion() {
        AnularTransaccionDAOImpl rub = new AnularTransaccionDAOImpl(connectionDB);

        ArrayList<AnularTransaccion> r = new ArrayList<AnularTransaccion>();

        byte idTipoTransaccion = 2;
        r = rub.getListaTransaccionesAnular(idTipoTransaccion, usuario);

        dtm = (DefaultTableModel) this.jtAnularComprobante.getModel();
        dtm.setRowCount(0);

        jtAnularComprobante.setModel(dtm);

        Object[] fila = new Object[7];

        for (int i = 0; i < r.size(); i++) {
            fila[0] = r.get(i).getIdEntregaTransaccion();
            fila[1] = r.get(i).getIdTransaccion();
            fila[2] = r.get(i).getFecha();
            fila[3] = r.get(i).getValorTotal();
            fila[4] = r.get(i).getNit();
            fila[5] = r.get(i).getRazonSocial();
            fila[6] = r.get(i).getNroFactura();

            dtm.addRow(fila);
        }

        jtAnularComprobante.setModel(dtm);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnEliminar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtAnularComprobante = new javax.swing.JTable();
        btnEliminar1 = new javax.swing.JButton();
        jlTituloFormulario = new javax.swing.JLabel();
        jbSalir = new javax.swing.JButton();

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/trash_icon.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Anular Transacciones");
        setLocation(new java.awt.Point(100, 100));
        setResizable(false);

        jtAnularComprobante.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "idTransaccionEntrega", "idEntrega", "Fecha", "valorTotal", "nit", "razon social", "nroFactura"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jtAnularComprobante);
        if (jtAnularComprobante.getColumnModel().getColumnCount() > 0) {
            jtAnularComprobante.getColumnModel().getColumn(0).setMinWidth(0);
            jtAnularComprobante.getColumnModel().getColumn(0).setPreferredWidth(0);
            jtAnularComprobante.getColumnModel().getColumn(0).setMaxWidth(0);
            jtAnularComprobante.getColumnModel().getColumn(1).setMinWidth(0);
            jtAnularComprobante.getColumnModel().getColumn(1).setPreferredWidth(0);
            jtAnularComprobante.getColumnModel().getColumn(1).setMaxWidth(0);
            jtAnularComprobante.getColumnModel().getColumn(2).setMinWidth(100);
            jtAnularComprobante.getColumnModel().getColumn(2).setPreferredWidth(100);
            jtAnularComprobante.getColumnModel().getColumn(2).setMaxWidth(100);
            jtAnularComprobante.getColumnModel().getColumn(3).setMinWidth(100);
            jtAnularComprobante.getColumnModel().getColumn(3).setPreferredWidth(100);
            jtAnularComprobante.getColumnModel().getColumn(3).setMaxWidth(100);
            jtAnularComprobante.getColumnModel().getColumn(4).setMinWidth(100);
            jtAnularComprobante.getColumnModel().getColumn(4).setPreferredWidth(100);
            jtAnularComprobante.getColumnModel().getColumn(4).setMaxWidth(100);
            jtAnularComprobante.getColumnModel().getColumn(5).setMinWidth(350);
            jtAnularComprobante.getColumnModel().getColumn(5).setPreferredWidth(350);
            jtAnularComprobante.getColumnModel().getColumn(5).setMaxWidth(350);
            jtAnularComprobante.getColumnModel().getColumn(6).setMinWidth(100);
            jtAnularComprobante.getColumnModel().getColumn(6).setPreferredWidth(100);
            jtAnularComprobante.getColumnModel().getColumn(6).setMaxWidth(100);
        }

        btnEliminar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/exit.png"))); // NOI18N
        btnEliminar1.setText("Anular");
        btnEliminar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminar1ActionPerformed(evt);
            }
        });

        jlTituloFormulario.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        jlTituloFormulario.setForeground(new java.awt.Color(153, 0, 51));
        jlTituloFormulario.setText("Anular Transacciones");

        jbSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/close_window.png"))); // NOI18N
        jbSalir.setText("Salir");
        jbSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(14, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 760, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(345, 345, 345)
                        .addComponent(btnEliminar1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(254, 254, 254)
                .addComponent(jlTituloFormulario)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jlTituloFormulario)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminar1)
                    .addComponent(jbSalir))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed

    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnEliminar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminar1ActionPerformed
        anularTransaccion();
    }//GEN-LAST:event_btnEliminar1ActionPerformed

    private void jbSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSalirActionPerformed
        dispose();
    }//GEN-LAST:event_jbSalirActionPerformed

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
            java.util.logging.Logger.getLogger(FormAnularTransaccion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormAnularTransaccion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormAnularTransaccion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormAnularTransaccion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormAnularTransaccion().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnEliminar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbSalir;
    private javax.swing.JLabel jlTituloFormulario;
    private javax.swing.JTable jtAnularComprobante;
    // End of variables declaration//GEN-END:variables
}
