/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.prog2.DempereGuillermoGerman.vista;

import edu.ub.prog2.DempereGuillermoGerman.controlador.CtrlVisor;
import edu.ub.prog2.DempereGuillermoGerman.model.AlbumImatges;
import edu.ub.prog2.DempereGuillermoGerman.model.BibliotecaImatges;
import edu.ub.prog2.DempereGuillermoGerman.model.Imatge;
import edu.ub.prog2.DempereGuillermoGerman.model.ImatgeBN;
import edu.ub.prog2.DempereGuillermoGerman.model.ImatgeSepia;
import edu.ub.prog2.DempereGuillermoGerman.vista.Listener.VisorTimerListener;
import edu.ub.prog2.utils.ImageFile;
import edu.ub.prog2.utils.ImageList;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class VisorUB4 extends javax.swing.JFrame {
    
    CtrlVisor ctrl;

    // Private Vars
    private final String LIBRARY = "Biblioteca";
    private final String NEW_ALBUM = "Nou Album...";

    private ImageList currentList;
    private Imatge currentImg;

    public VisorUB4() {
        initComponents();

        /*
         ctrl.addAlbum("Fortress", "Al1", 10);
         ctrl.addAlbum("Vacation", "Mom", 10);
         ctrl.addImageToList("Earth", "img/earth.jpg", ctrl.getData().getLib());
         ctrl.addImageToList("Earth-Half", "img/earth-half.jpg", ctrl.getData().getLib());
         ctrl.addImageToList("Sunset", "img/sunset.jpg", ctrl.getData().getLib());
         ctrl.addImageToList("Highway", "img/highway.jpg", ctrl.getData().getAlbum(0));
         ctrl.addImageToList("Sunset", "img/sunset.jpg", ctrl.getData().getAlbum(1));
         */
        ctrl = new CtrlVisor();
        ctrl.addTimerListener(new CustomTimerListener());
        ctrl.setTimer(1000);

        refreshAlbumsComboBox();
        this.currentList = ctrl.getData().getLib();
        refreshImageList();

    }

    private void refreshAlbumsComboBox() {
        ArrayList<String> alTitles = new ArrayList<String>();
        alTitles.add(LIBRARY);
        for (AlbumImatges a : ctrl.getData().getAlbums()) {
            alTitles.add(a.getTitle() + " - " + a.getAuthor());
        }
        alTitles.add(NEW_ALBUM);
        jImageListSelector.setModel(new DefaultComboBoxModel(alTitles.toArray()));
    }

    private void refreshImageList() {
        refreshInfo();
        //System.out.println("Refreshed");
        jImageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Refresh the image list
        DefaultListModel dlm = new DefaultListModel();
        for (ImageFile img : currentList.getList()) {
            dlm.addElement(img.toString());
        }
        jImageList.setModel(dlm);

        ListSelectionModel lsm = jImageList.getSelectionModel();
        lsm.addListSelectionListener(new ImageListSelectionListener());
    }

    private void refreshInfo() {
        if (currentList instanceof BibliotecaImatges) {
            jAuthorField.setText("N/a");
            jAlbumTitle.setText(LIBRARY);
            jImageNo.setText(currentList.getSize() + "");
        } else {
            AlbumImatges al = (AlbumImatges) currentList;
            jAuthorField.setText(al.getAuthor());
            jAlbumTitle.setText(al.getTitle());
            jImageNo.setText(al.getSize() + "");
        }
    }

    // Temp buffer for the display Image
    private BufferedImage tempBufferImg;

    public void showImage(Imatge img) {
        if (img == null) {
            jImageLabel.setText("No Imatge");
            jImageLabel.setIcon(null);
            return;
        }
        jImageLabel.setText(null);
        currentImg = img;

        // Clear temp  buffer
        if (tempBufferImg != null) {
            tempBufferImg.flush();
        }
        //tempBufferImg = (BufferedImage) img.getImage();

        // Get new scaled down bufferedimage from passed image and apply filters
        tempBufferImg = img.resizeImage(376, 366, true);

        // Apply filter if selected
        if (jImageBlackWhite.isSelected()) {
            ImatgeBN.applyBWFilter(tempBufferImg);
        }
        if (jImageSepia.isSelected()) {
            ImatgeSepia.applySepiaFilter(tempBufferImg);
        }

        jImageLabel.setIcon(new ImageIcon(tempBufferImg));
        //jImageLabel.setIcon(new ImageIcon(img.getBufferedImage()));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        buttonGroup6 = new javax.swing.ButtonGroup();
        buttonGroup7 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jImageList = new javax.swing.JList();
        jLabelImatges = new javax.swing.JLabel();
        jImageListSelector = new javax.swing.JComboBox();
        jSeparatorImatges = new javax.swing.JSeparator();
        jAfegirImatge = new javax.swing.JButton();
        jEliminarImatge = new javax.swing.JButton();
        jImagePanel = new javax.swing.JPanel();
        jImageLabel = new javax.swing.JLabel();
        jOptionsPanel = new javax.swing.JPanel();
        jImageDefault = new javax.swing.JRadioButton();
        jImageSepia = new javax.swing.JRadioButton();
        jImageBlackWhite = new javax.swing.JRadioButton();
        jPlaySpeed = new javax.swing.JTextField();
        jIncreaseSpeed = new javax.swing.JButton();
        jDecreaseSpeed = new javax.swing.JButton();
        jInformationPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jAuthorField = new javax.swing.JTextField();
        jAlbumTitle = new javax.swing.JTextField();
        jImageNo = new javax.swing.JTextField();
        jPlaylistControlsPanel = new javax.swing.JPanel();
        jPlayPlaylist = new javax.swing.JButton();
        jPausePlaylist = new javax.swing.JButton();
        jStopPlaylist = new javax.swing.JButton();
        jPlaylistProgress = new javax.swing.JProgressBar();
        jDeleteAlbum = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuLoad = new javax.swing.JMenuItem();
        jMenuLoad.setAccelerator(  KeyStroke.getKeyStroke(  KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        jMenuSave = new javax.swing.JMenuItem();
        jMenuSave.setAccelerator(  KeyStroke.getKeyStroke(  KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuExit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Visor d'Imatges UB");
        setMaximumSize(null);
        setName("frameMainWindow"); // NOI18N
        setPreferredSize(new java.awt.Dimension(720, 450));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jImageList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Imatge 1", "Imatge 2", " " };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jImageList);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 180, 280));

        jLabelImatges.setText("Imatges");
        getContentPane().add(jLabelImatges, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 90, -1));

        jImageListSelector.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Biblioteca..." }));
        jImageListSelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jImageListSelectorActionPerformed(evt);
            }
        });
        getContentPane().add(jImageListSelector, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, 20));
        getContentPane().add(jSeparatorImatges, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 57, 180, -1));

        jAfegirImatge.setText("Afegir");
        jAfegirImatge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAfegirImatgeActionPerformed(evt);
            }
        });
        getContentPane().add(jAfegirImatge, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 80, -1));

        jEliminarImatge.setText("Eliminar");
        jEliminarImatge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jEliminarImatgeActionPerformed(evt);
            }
        });
        getContentPane().add(jEliminarImatge, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 360, 80, -1));

        jImagePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jImageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jImageLabel.setText("No Imatge");

        javax.swing.GroupLayout jImagePanelLayout = new javax.swing.GroupLayout(jImagePanel);
        jImagePanel.setLayout(jImagePanelLayout);
        jImagePanelLayout.setHorizontalGroup(
            jImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jImagePanelLayout.setVerticalGroup(
            jImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jImageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
        );

        getContentPane().add(jImagePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, 380, 340));

        jOptionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Opcions"));

        buttonGroup1.add(jImageDefault);
        jImageDefault.setSelected(true);
        jImageDefault.setText("Normal");
        jImageDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jImageDefaultActionPerformed(evt);
            }
        });

        buttonGroup1.add(jImageSepia);
        jImageSepia.setText("Sepia");

        buttonGroup1.add(jImageBlackWhite);
        jImageBlackWhite.setText("B&W");

        jPlaySpeed.setEditable(false);
        jPlaySpeed.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPlaySpeed.setText("1000");

        jIncreaseSpeed.setText("Rapid");
        jIncreaseSpeed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jIncreaseSpeedActionPerformed(evt);
            }
        });

        jDecreaseSpeed.setText("Lent");
        jDecreaseSpeed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDecreaseSpeedActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jOptionsPanelLayout = new javax.swing.GroupLayout(jOptionsPanel);
        jOptionsPanel.setLayout(jOptionsPanelLayout);
        jOptionsPanelLayout.setHorizontalGroup(
            jOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jOptionsPanelLayout.createSequentialGroup()
                .addGroup(jOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jImageDefault, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jImageSepia, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jImageBlackWhite, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jOptionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jIncreaseSpeed, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                    .addComponent(jPlaySpeed))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jOptionsPanelLayout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jDecreaseSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jOptionsPanelLayout.setVerticalGroup(
            jOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jOptionsPanelLayout.createSequentialGroup()
                .addComponent(jImageDefault)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jImageSepia)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jImageBlackWhite)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jIncreaseSpeed)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPlaySpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDecreaseSpeed)
                .addGap(7, 7, 7))
        );

        getContentPane().add(jOptionsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 160, 100, 190));

        jInformationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Informació"));

        jLabel1.setText("Nº Imgs.:");

        jLabel4.setText("Autor:");

        jLabel5.setText("Titol:");

        jAuthorField.setEditable(false);
        jAuthorField.setText("n/a");

        jAlbumTitle.setEditable(false);
        jAlbumTitle.setText("Biblioteca");

        jImageNo.setEditable(false);
        jImageNo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jImageNo.setText("-");
        jImageNo.setPreferredSize(new java.awt.Dimension(20, 20));

        javax.swing.GroupLayout jInformationPanelLayout = new javax.swing.GroupLayout(jInformationPanel);
        jInformationPanel.setLayout(jInformationPanelLayout);
        jInformationPanelLayout.setHorizontalGroup(
            jInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInformationPanelLayout.createSequentialGroup()
                .addGroup(jInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jInformationPanelLayout.createSequentialGroup()
                        .addGroup(jInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addGroup(jInformationPanelLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jImageNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 2, Short.MAX_VALUE))
                    .addComponent(jAlbumTitle)
                    .addComponent(jAuthorField))
                .addContainerGap())
        );
        jInformationPanelLayout.setVerticalGroup(
            jInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInformationPanelLayout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jAuthorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jAlbumTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jImageNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39))
        );

        getContentPane().add(jInformationPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, 100, 140));

        jPlayPlaylist.setText("Play");
        jPlayPlaylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPlayPlaylistActionPerformed(evt);
            }
        });

        jPausePlaylist.setText("Pausa");
        jPausePlaylist.setEnabled(false);
        jPausePlaylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPausePlaylistActionPerformed(evt);
            }
        });

        jStopPlaylist.setText("Stop");
        jStopPlaylist.setEnabled(false);
        jStopPlaylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jStopPlaylistActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPlaylistControlsPanelLayout = new javax.swing.GroupLayout(jPlaylistControlsPanel);
        jPlaylistControlsPanel.setLayout(jPlaylistControlsPanelLayout);
        jPlaylistControlsPanelLayout.setHorizontalGroup(
            jPlaylistControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPlaylistControlsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPlayPlaylist)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPausePlaylist)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jStopPlaylist)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPlaylistProgress, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPlaylistControlsPanelLayout.setVerticalGroup(
            jPlaylistControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPlaylistControlsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPlaylistControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPlaylistControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jPlayPlaylist)
                        .addComponent(jPausePlaylist)
                        .addComponent(jStopPlaylist))
                    .addComponent(jPlaylistProgress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(jPlaylistControlsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 350, 380, 40));

        jDeleteAlbum.setText("Elimiar");
        jDeleteAlbum.setDefaultCapable(false);
        jDeleteAlbum.setEnabled(false);
        jDeleteAlbum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDeleteAlbumActionPerformed(evt);
            }
        });
        getContentPane().add(jDeleteAlbum, new org.netbeans.lib.awtextra.AbsoluteConstraints(113, 10, 80, 20));

        jMenu1.setText("Fitxer");

        jMenuLoad.setText("Obrir Arxiu...");
        jMenuLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuLoadActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuLoad);

        jMenuSave.setText("Guardar...");
        jMenuSave.setToolTipText("");
        jMenuSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuSaveActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuSave);
        jMenu1.add(jSeparator2);

        jMenuExit.setText("Sortir");
        jMenuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuExitActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuExit);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Editar");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        setSize(new java.awt.Dimension(720, 450));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jAfegirImatgeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAfegirImatgeActionPerformed
        FrmAfegirImatge newImageForm = new FrmAfegirImatge(currentList, ctrl);
        newImageForm.setVisible(true);
        newImageForm.setLocationRelativeTo(this);
        newImageForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Refresh imagelist on form close
        newImageForm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                refreshImageList();
            }
        });
    }//GEN-LAST:event_jAfegirImatgeActionPerformed

    /**
     * Library, album, or new album selected on the combo box.
     */
    private void jImageListSelectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jImageListSelectorActionPerformed
        // <editor-fold defaultstate="collapsed" desc="jImageListSelectorActionPerformed code.">
        JComboBox cbox = (JComboBox) evt.getSource();
        // The albm index is the index -1 since the first item is the library.
        int alIndex = cbox.getSelectedIndex() - 1;

        // TODO Handle selection
        switch ((String) cbox.getSelectedItem()) {
            case LIBRARY:
                System.out.println("Library selected");
                this.currentList = ctrl.getData().getLib();
                this.jDeleteAlbum.setEnabled(false);
                this.refreshImageList();
                break;
            case NEW_ALBUM:
                System.out.println("New Album selected");
                this.jDeleteAlbum.setEnabled(false);
                break;
            default:
                System.out.println("Album selected");
                this.currentList = ctrl.getData().getAlbum(alIndex);
                this.jDeleteAlbum.setEnabled(true);
                this.refreshImageList();
                break;

        }
        // </editor-fold>
    }//GEN-LAST:event_jImageListSelectorActionPerformed

    private void jMenuSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuSaveActionPerformed
        JFileChooser fc = new JFileChooser(".");
        fc.showDialog(this, null);

        File output = fc.getSelectedFile();
        if (ctrl.saveDataToObjectStream(output.getAbsolutePath())) {
            JOptionPane.showMessageDialog(this, "Base de dades guardada correctament.");
        } else {
            JOptionPane.showMessageDialog(
                    this, "Error guardant la base de dades.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuSaveActionPerformed

    private void jMenuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuExitActionPerformed
        this.dispose();
        System.exit(0);
    }//GEN-LAST:event_jMenuExitActionPerformed

    private void jMenuLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuLoadActionPerformed
        JFileChooser fc = new JFileChooser(".");
        fc.showDialog(this, null);

        File input = fc.getSelectedFile();
        if (ctrl.loadDataFromObjectStream(input.getAbsolutePath())) {
            JOptionPane.showMessageDialog(this, "Base de dades carregada correctament.");
            this.currentList = ctrl.getData().getLib();
            this.refreshAlbumsComboBox();
            this.refreshImageList();
        } else {
            JOptionPane.showMessageDialog(
                    this, "Error carregant la base de dades.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuLoadActionPerformed

    private void jImageDefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jImageDefaultActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jImageDefaultActionPerformed

    private void jIncreaseSpeedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jIncreaseSpeedActionPerformed
        int speed = Integer.parseInt(jPlaySpeed.getText());
        speed -= 100;
        speed = speed < 100 ? 100 : speed; // limit lower bound for speed to 100 ms
        jPlaySpeed.setText(speed + "");
        ctrl.changeTimer(speed);
    }//GEN-LAST:event_jIncreaseSpeedActionPerformed

    private void jDecreaseSpeedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDecreaseSpeedActionPerformed
        int speed = Integer.parseInt(jPlaySpeed.getText());
        speed += 100;
        jPlaySpeed.setText(speed + "");
        ctrl.changeTimer(speed);
    }//GEN-LAST:event_jDecreaseSpeedActionPerformed

    /**
     * Delete an image from the current list.
     */
    private void jEliminarImatgeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jEliminarImatgeActionPerformed
        if (currentImg == null) {
            return;
        }

        if (currentList instanceof BibliotecaImatges) {
            ctrl.removeImageFromAll(currentImg);
        } else {
            currentList.removeImage(currentImg);
        }
        refreshImageList();
        showImage(null);
    }//GEN-LAST:event_jEliminarImatgeActionPerformed

    private void jDeleteAlbumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDeleteAlbumActionPerformed
        // The selected list is an album and so we are to delete it
        ctrl.removeAlbum(currentList);
        this.currentImg = null;
        this.currentList = ctrl.getData().getLib();
        refreshAlbumsComboBox();
        refreshImageList();
    }//GEN-LAST:event_jDeleteAlbumActionPerformed

    private void jPlayPlaylistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPlayPlaylistActionPerformed
        if (currentList != null && currentList.getSize() > 1) {
            ctrl.play(currentList.getSize());
        }
    }//GEN-LAST:event_jPlayPlaylistActionPerformed

    private void jPausePlaylistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPausePlaylistActionPerformed
        ctrl.pause();
    }//GEN-LAST:event_jPausePlaylistActionPerformed

    private void jStopPlaylistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jStopPlaylistActionPerformed
        ctrl.stop();
    }//GEN-LAST:event_jStopPlaylistActionPerformed

    private class CustomTimerListener extends VisorTimerListener {
       

        @Override
        public void onStart() {
            //showImage((Imatge)currentList.getAt(0));
            
            jPlaylistProgress.setMinimum(0);
            jPlaylistProgress.setValue(0);
            jPlaylistProgress.setMaximum(currentList.getSize());
            jPlaylistProgress.setStringPainted(true);

            jPlayPlaylist.setEnabled(false);
            jPausePlaylist.setEnabled(true);
            jStopPlaylist.setEnabled(true);
        }

        @Override
        public void onTimer(int index) {
            showImage((Imatge)currentList.getAt(index));
            jPlaylistProgress.setValue(index+1);
            
            jPlaylistProgress.setString(index+1 + "/" +currentList.getSize());
        }

        @Override
        public void onPause() {
            jPlayPlaylist.setEnabled(true);
            jPausePlaylist.setEnabled(false);
            jStopPlaylist.setEnabled(true);
        }
        
        @Override
        public void onResume(){
            jPlayPlaylist.setEnabled(false);
            jPausePlaylist.setEnabled(true);
            jStopPlaylist.setEnabled(true);            
        }

        @Override
        public void onStop() {
            jPlayPlaylist.setEnabled(true);
            jPausePlaylist.setEnabled(false);
            jStopPlaylist.setEnabled(false);
            
            jPlaylistProgress.setValue(0);
            jPlaylistProgress.setStringPainted(false);
        }

        @Override
        public void onFinish() {
            jPlayPlaylist.setEnabled(true);
            jPausePlaylist.setEnabled(false);
            jStopPlaylist.setEnabled(false);
            
        }

    }

    private class ImageListSelectionListener implements ListSelectionListener {
        // <editor-fold defaultstate="collapsed" desc="ImageListSelectionListener code.">

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) {
                return;
            }

            int index = jImageList.getSelectedIndex();
            if (index < 0) {
                return;
            }

            System.out.println("Triggered list: " + index);

            if (index >= currentList.getSize()) {
                return;
            }

            Imatge img = (Imatge) currentList.getAt(index);
            showImage(img);

        }

        // </editor-fold>
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.ButtonGroup buttonGroup6;
    private javax.swing.ButtonGroup buttonGroup7;
    private javax.swing.JButton jAfegirImatge;
    private javax.swing.JTextField jAlbumTitle;
    private javax.swing.JTextField jAuthorField;
    private javax.swing.JButton jDecreaseSpeed;
    private javax.swing.JButton jDeleteAlbum;
    private javax.swing.JButton jEliminarImatge;
    private javax.swing.JRadioButton jImageBlackWhite;
    private javax.swing.JRadioButton jImageDefault;
    private javax.swing.JLabel jImageLabel;
    private javax.swing.JList jImageList;
    private javax.swing.JComboBox jImageListSelector;
    private javax.swing.JTextField jImageNo;
    private javax.swing.JPanel jImagePanel;
    private javax.swing.JRadioButton jImageSepia;
    private javax.swing.JButton jIncreaseSpeed;
    private javax.swing.JPanel jInformationPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabelImatges;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuExit;
    private javax.swing.JMenuItem jMenuLoad;
    private javax.swing.JMenuItem jMenuSave;
    private javax.swing.JPanel jOptionsPanel;
    private javax.swing.JButton jPausePlaylist;
    private javax.swing.JButton jPlayPlaylist;
    private javax.swing.JTextField jPlaySpeed;
    private javax.swing.JPanel jPlaylistControlsPanel;
    private javax.swing.JProgressBar jPlaylistProgress;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JSeparator jSeparatorImatges;
    private javax.swing.JButton jStopPlaylist;
    // End of variables declaration//GEN-END:variables
}

