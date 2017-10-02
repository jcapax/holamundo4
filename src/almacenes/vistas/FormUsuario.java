/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package almacenes.vistas;

import almacenes.conectorDB.DatabaseUtils;
import almacenes.model.Usuario;
import dao.UsuariosDAOImpl;
import java.sql.Connection;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jcapax
 */
public class FormUsuario extends javax.swing.JFrame {

    private DatabaseUtils databaseUtils;
    private Connection connectionDB;
    DefaultTableModel dtm;
    private boolean edicionUsuario;

    public FormUsuario() {
        initComponents();
    }

    public FormUsuario(Connection connectionDB) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.databaseUtils = new DatabaseUtils();
        this.connectionDB = connectionDB;
        this.edicionUsuario = false;
        llenarTablaUsuarios();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jtxtLogin = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jpassContrasena1 = new javax.swing.JPasswordField();
        jpassContrasena2 = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        jpassContrasenaAnterior = new javax.swing.JPasswordField();
        jbSalir = new javax.swing.JButton();
        jbEditar = new javax.swing.JButton();
        jbCancelar = new javax.swing.JButton();
        jbGuardar = new javax.swing.JButton();
        jbNuevo = new javax.swing.JButton();
        jbEliminar = new javax.swing.JButton();
        jchAdministrador = new javax.swing.JCheckBox();
        jchEstado = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtUsuarios = new javax.swing.JTable();
        jlTituloFormulario = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel2.setForeground(new java.awt.Color(153, 0, 51));
        jLabel2.setText("Login");

        jLabel3.setForeground(new java.awt.Color(153, 0, 51));
        jLabel3.setText("Contrasena");

        jLabel4.setForeground(new java.awt.Color(153, 0, 51));
        jLabel4.setText("Repetir Contrasena");

        jLabel5.setForeground(new java.awt.Color(153, 0, 51));
        jLabel5.setText("Contrasena Anterior");

        jbSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/close_window.png"))); // NOI18N
        jbSalir.setText("Salir");
        jbSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSalirActionPerformed(evt);
            }
        });

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

        jbGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Save-icon.png"))); // NOI18N
        jbGuardar.setText("Guardar");
        jbGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbGuardarActionPerformed(evt);
            }
        });

        jbNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/folder-add-icon.png"))); // NOI18N
        jbNuevo.setText("Nuevo");
        jbNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbNuevoActionPerformed(evt);
            }
        });

        jbEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/trash_icon.png"))); // NOI18N
        jbEliminar.setText("Eliminar");
        jbEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEliminarActionPerformed(evt);
            }
        });

        jchAdministrador.setForeground(new java.awt.Color(153, 0, 51));
        jchAdministrador.setText("Administrador");
        jchAdministrador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchAdministradorActionPerformed(evt);
            }
        });

        jchEstado.setForeground(new java.awt.Color(153, 0, 51));
        jchEstado.setText("Estado");
        jchEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchEstadoActionPerformed(evt);
            }
        });

        jtUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Login", "rol", "Estado"
            }
        ));
        jtUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtUsuariosMouseClicked(evt);
            }
        });
        jtUsuarios.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtUsuariosKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jtUsuarios);
        if (jtUsuarios.getColumnModel().getColumnCount() > 0) {
            jtUsuarios.getColumnModel().getColumn(1).setMinWidth(0);
            jtUsuarios.getColumnModel().getColumn(1).setPreferredWidth(0);
            jtUsuarios.getColumnModel().getColumn(1).setMaxWidth(0);
        }

        jlTituloFormulario.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        jlTituloFormulario.setForeground(new java.awt.Color(153, 0, 51));
        jlTituloFormulario.setText("Usuarios");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel5)
                                .addComponent(jpassContrasena1)
                                .addComponent(jLabel3)
                                .addComponent(jpassContrasenaAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(99, 99, 99)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jlTituloFormulario)
                                .addComponent(jpassContrasena2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jchAdministrador)
                            .addGap(39, 39, 39)
                            .addComponent(jchEstado))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jbNuevo)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jbGuardar)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jbEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jbCancelar)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jbEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jbSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2)
                    .addComponent(jtxtLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlTituloFormulario)
                .addGap(14, 14, 14)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jtxtLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpassContrasenaAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addGap(5, 5, 5)
                        .addComponent(jpassContrasena1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jchAdministrador)
                                .addComponent(jchEstado))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jpassContrasena2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbNuevo)
                    .addComponent(jbGuardar)
                    .addComponent(jbEditar)
                    .addComponent(jbCancelar)
                    .addComponent(jbEliminar)
                    .addComponent(jbSalir))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSalirActionPerformed
        dispose();
    }//GEN-LAST:event_jbSalirActionPerformed

    private void jbEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEditarActionPerformed
        byte x = 3;//editar
        botones(x);
        jtxtLogin.setEnabled(false);

    }//GEN-LAST:event_jbEditarActionPerformed

    private void jbCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelarActionPerformed
        byte x = 4;//cancelar
        botones(x);        
        jtxtLogin.setEnabled(true);
    }//GEN-LAST:event_jbCancelarActionPerformed

    private void jbGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbGuardarActionPerformed
        byte x = 2;//guardar
        botones(x);        
        jtxtLogin.setEnabled(true);
        llenarTablaUsuarios();
    }//GEN-LAST:event_jbGuardarActionPerformed

    private void jbNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbNuevoActionPerformed
        byte x = 1;//nuevo
        botones(x);
        jtxtLogin.setEnabled(true);
    }//GEN-LAST:event_jbNuevoActionPerformed

    private void jbEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEliminarActionPerformed
        byte x = 5; //eliminar
        botones(x);
        llenarTablaUsuarios();
    }//GEN-LAST:event_jbEliminarActionPerformed

    private void jchAdministradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchAdministradorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jchAdministradorActionPerformed

    private void jchEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchEstadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jchEstadoActionPerformed

    private void jtUsuariosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtUsuariosKeyReleased
        seleccionarUsuario();
    }//GEN-LAST:event_jtUsuariosKeyReleased

    private void jtUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtUsuariosMouseClicked
        seleccionarUsuario();
    }//GEN-LAST:event_jtUsuariosMouseClicked

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
            java.util.logging.Logger.getLogger(FormUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormUsuario().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbCancelar;
    private javax.swing.JButton jbEditar;
    private javax.swing.JButton jbEliminar;
    private javax.swing.JButton jbGuardar;
    private javax.swing.JButton jbNuevo;
    private javax.swing.JButton jbSalir;
    private javax.swing.JCheckBox jchAdministrador;
    private javax.swing.JCheckBox jchEstado;
    private javax.swing.JLabel jlTituloFormulario;
    private javax.swing.JPasswordField jpassContrasena1;
    private javax.swing.JPasswordField jpassContrasena2;
    private javax.swing.JPasswordField jpassContrasenaAnterior;
    private javax.swing.JTable jtUsuarios;
    private javax.swing.JTextField jtxtLogin;
    // End of variables declaration//GEN-END:variables

    private void botones(byte x) {
        switch (x) {
            case 1: //nuevo
                jbNuevo.setEnabled(false);
                jbGuardar.setEnabled(true);
                jbEditar.setEnabled(false);
                jbCancelar.setEnabled(true);
                edicionUsuario = false;
                limpiar();
                break;
            case 2: //guardar
                jbNuevo.setEnabled(true);
                jbGuardar.setEnabled(false);
                jbEditar.setEnabled(true);
                jbCancelar.setEnabled(false);
                if (edicionUsuario) {
                    guardarEdicionUsuario();
                } else {
                    guardarUsuario();
                }
                edicionUsuario = false;
                break;
            case 3: // editar
                jbNuevo.setEnabled(false);
                jbGuardar.setEnabled(true);
                jbEditar.setEnabled(false);
                jbCancelar.setEnabled(true);
                edicionUsuario = true;
                break;
            case 4: // cancelar
                jbNuevo.setEnabled(true);
                jbGuardar.setEnabled(false);
                jbEditar.setEnabled(true);
                jbCancelar.setEnabled(false);
                edicionUsuario = false;
                limpiar();
                break;
            case 5: //eliminar
                eliminarUsuario();
                edicionUsuario = false;
                byte y = 4; // cancelar
                botones(y);
                break;
        }
    }

    public void llenarTablaUsuarios() {
        UsuariosDAOImpl rub = new UsuariosDAOImpl(connectionDB);

        ArrayList<Usuario> r = new ArrayList<Usuario>();

        r = rub.getlistaUsuarios();

        dtm = (DefaultTableModel) this.jtUsuarios.getModel();
        dtm.setRowCount(0);

        jtUsuarios.setModel(dtm);

        Object[] fila = new Object[3];

//        System.out.println("nro de registros en pila: " + r.size());

        for (int i = 0; i < r.size(); i++) {
            fila[0] = r.get(i).getLogin();            
            fila[1] = r.get(i).getRol();
            fila[2] = r.get(i).getEstado();

            dtm.addRow(fila);
        }

        jtUsuarios.setModel(dtm);
    }

    public void guardarUsuario() {
        UsuariosDAOImpl usuarioDAO = new UsuariosDAOImpl(connectionDB);
        Usuario us = new Usuario();

        String login = null;
        String pass = null;
        String pass2 = null;
        String estado = null;
        String rol = null;
        boolean checkAll = false;
        boolean verificarPass;

        login = jtxtLogin.getText().trim();

        char[] passChar1 = jpassContrasena1.getPassword();
        char[] passChar2 = jpassContrasena2.getPassword();
        pass = new String(passChar1);

        if (login.length() != 0) {
            checkAll = true;
        } else {
            checkAll = false;
            JOptionPane.showMessageDialog(this, "el registro login es obligatorio");
            jtxtLogin.requestFocus(true);
            return;
        }

        if (pass.length() != 0) {
            checkAll = true;
        } else {
            checkAll = false;
            JOptionPane.showMessageDialog(this, "Introducir contrasena");
            jpassContrasena1.requestFocus(true);
            return;
        }

        verificarPass = usuarioDAO.verificarPass1Pass2(passChar1, passChar2);

        if (verificarPass) {
            checkAll = true;
        } else {
            checkAll = false;
            JOptionPane.showMessageDialog(this, "Error en el registro de contrasenas, favor verificar");
            jpassContrasena1.requestFocus(true);
            jpassContrasena1.setText("");
            jpassContrasena2.setText("");
            return;
        }

        if (checkAll) {
            rol = (jchAdministrador.isSelected()) ? "1" : "2";

            estado = (jchEstado.isSelected()) ? "V" : "N";

            us.setLogin(login);            
            us.setRol(rol);
            us.setContrasenia(pass);
            us.setEstado(estado);

            usuarioDAO.insertarUsuario(us);
        } else {
            byte x = 4;// cancelar
            botones(x);
        }

    }

    public void limpiar() {        
        jtxtLogin.setText("");
        jpassContrasenaAnterior.setText("");
        jpassContrasena1.setText("");
        jpassContrasena2.setText("");
        jchAdministrador.setSelected(false);
        jchEstado.setSelected(false);
    }

    public void seleccionarUsuario() {
        int fila = jtUsuarios.getSelectedRow();
        String login = jtUsuarios.getValueAt(fila, 0).toString();

        jtxtLogin.setText(login);

        String rol = jtUsuarios.getValueAt(fila, 1).toString();
        if (rol.equals("1")) {
            jchAdministrador.setSelected(true);
        } else {
            jchAdministrador.setSelected(false);
        }

        String estado = jtUsuarios.getValueAt(fila, 2).toString();
        if (estado.equals("V")) {
            jchEstado.setSelected(true);
        } else {
            jchEstado.setSelected(false);
        }

    }

    public void eliminarUsuario() {
        UsuariosDAOImpl us = new UsuariosDAOImpl(connectionDB);
        int fila = jtUsuarios.getSelectedRow();
        String usuario = jtUsuarios.getValueAt(fila, 1).toString();
        boolean aux = us.validarEliminacionUsuario(usuario);
        if (aux) {
            us.eliminarUsuario(usuario);
            llenarTablaUsuarios();
        } else {
            JOptionPane.showMessageDialog(this, "El usuario no puede ser eliminado");
        }
    }

    private void guardarEdicionUsuario() {
        UsuariosDAOImpl us = new UsuariosDAOImpl(connectionDB);
        Usuario usuario = new Usuario();
        boolean checkAll = false;
        
        String login = jtxtLogin.getText();
        
        char[] passAnterior = jpassContrasenaAnterior.getPassword();
        
        char[] pass1 = jpassContrasena1.getPassword();
        char[] pass2 = jpassContrasena2.getPassword();
        
        String estado = null;
        estado = (jchEstado.isSelected())?"V":"A";
        
        String rol = null;
        rol = (jchAdministrador.isSelected())?"1":"2";
        
        if(us.verificarPassEdicionUsuario(login, new String(passAnterior))){
            checkAll = true;
        }
        else{
            checkAll = false;
            JOptionPane.showMessageDialog(this, "Favor registrar el pasword anterior");
            return;
        }
        
        if(us.verificarPass1Pass2(pass1, pass2)){
            checkAll = true;
        }
        else{
            checkAll = false;
            JOptionPane.showMessageDialog(this, "Las contrasenas introducidas no son iguales");
            return;
        }
        
        usuario.setLogin(login);
        usuario.setContrasenia(new String(pass1));
        usuario.setEstado(estado);
        usuario.setRol(rol);        
                
        if (checkAll) {
            us.editarUsuario(usuario);
        }
    }
}
