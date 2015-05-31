/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.prog2.DempereGuillermoGerman.vista;

import edu.ub.prog2.DempereGuillermoGerman.vista.forms.FormAddImageToLib;
import edu.ub.prog2.DempereGuillermoGerman.controlador.CtrlVisor;
import edu.ub.prog2.DempereGuillermoGerman.model.AlbumImatges;
import edu.ub.prog2.DempereGuillermoGerman.model.BibliotecaImatges;
import edu.ub.prog2.DempereGuillermoGerman.model.Imatge;
import edu.ub.prog2.DempereGuillermoGerman.model.ImatgeBN;
import edu.ub.prog2.DempereGuillermoGerman.model.ImatgeSepia;
import edu.ub.prog2.DempereGuillermoGerman.vista.forms.FormAddImageToAlbum;
import edu.ub.prog2.DempereGuillermoGerman.vista.forms.FormEditAlbum;
import edu.ub.prog2.DempereGuillermoGerman.vista.forms.FormEditImage;
import edu.ub.prog2.DempereGuillermoGerman.vista.listener.VisorTimerListener;
import edu.ub.prog2.utils.ImageFile;
import edu.ub.prog2.utils.ImageList;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

// TODO Image Edition popup menu
// TODO Add images to album from library list
// TODO Finish up and testing
public class VisorUB4 extends javax.swing.JFrame {

    private static final boolean DEBUG = false;

    CtrlVisor ctrl;

    // Private Vars
    private final String LIBRARY = "Biblioteca";
    private final String NEW_ALBUM = "Nou Album...";

    private ImageList currentList;
    private Imatge currentImg;
    private int imageListIndex;

    public VisorUB4() {
	initComponents();
	if (DEBUG) System.out.println("Created GUI.");

	ctrl = new CtrlVisor();
	ctrl.addTimerListener(new CustomTimerListener());
	ctrl.setTimer(1000);

	jImageList.addMouseListener(new PopClickListener());

	refreshAlbumsComboBox();
	this.currentList = ctrl.getData().getLib();
	refreshImageList();

	// Speed JSlider
	jSpeedSlider.addChangeListener(new ChangeListener() {
	    @Override
	    public void stateChanged(ChangeEvent e) {
		// Update the speed text field, minimun 100 ms
		int speed = Math.max(100, jSpeedSlider.getValue());
		jPlaySpeed.setText(speed + " ms");

		// If the user hasn't released the mouse yet, do nothing
		if (((JSlider)e.getSource()).getValueIsAdjusting())
		    return;

		// If playing, ignore
		if (!ctrl.isPlaying())
		    ctrl.setTimer(speed);
	    }
	});

	// Exit listener, saves if saveOnExit is ticket
	this.addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(WindowEvent e) {
		System.out.println("Closing main window...");
		if (jMenuItemSaveOnExit.isSelected()) {
		    ctrl.saveDataToObjectStream("data.bin");
		    System.out.println("Saved on exit to data.bin.");
		}
		System.exit(0);
	    }
	});

    }

    private void refreshAlbumsComboBox() {
	if (DEBUG) System.out.println("Refreshed Combo Box.");

	ArrayList<String> alTitles = new ArrayList<String>();
	alTitles.add(LIBRARY);
	for (AlbumImatges a : ctrl.getData().getAlbums()) {
	    alTitles.add(a.getTitle() + " - " + a.getAuthor());
	}
	alTitles.add(NEW_ALBUM);
	jAlbumComboBox.setModel(new DefaultComboBoxModel(alTitles.toArray()));

	// BUGFIX - If after deleting an album, the new selected item is the lib
	// disable the delete button
	if (((String)jAlbumComboBox.getSelectedItem()).equals(LIBRARY)) {
	    jEditAlbum.setEnabled(false);
	    jDeleteAlbum.setEnabled(false);
	}
    }

    private void refreshImageList() {
	refreshInfo();
	if (DEBUG) System.out.println("Refreshed Image List.");

	// invalidate the current image reference
	currentImg = null;
	showImage(null);
	jImageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	// Refresh the image list
	DefaultListModel dlm = new DefaultListModel();
	if (currentList != null)
	    for (ImageFile img : currentList.getList())
		dlm.addElement(img.toString());

	jImageList.setModel(dlm);

	ListSelectionModel lsm = jImageList.getSelectionModel();
	lsm.addListSelectionListener(new ImageListSelectionListener());

    }

    private void refreshInfo() {
	if (DEBUG) System.out.println("Refreshed Information Panel.");

	if (currentList instanceof BibliotecaImatges) {
	    jAuthorField.setText("N/a");
	    jAlbumTitle.setText(LIBRARY);
	    jImageNo.setText(currentList.getSize() + "");
	} else {
	    AlbumImatges al = (AlbumImatges)currentList;
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
	if (tempBufferImg != null)
	    tempBufferImg.flush();

	//tempBufferImg = (BufferedImage) img.getImage();
	// Get new scaled down bufferedimage from passed image and apply filters
	tempBufferImg = img.resizeImage(456, 366, true);

	// Apply filter if selected
	if (jImageBlackWhite.isSelected())
	    ImatgeBN.applyBWFilter(tempBufferImg);

	if (jImageSepia.isSelected())
	    ImatgeSepia.applySepiaFilter(tempBufferImg);

	jImageLabel.setIcon(new ImageIcon(tempBufferImg));
	jImageLabel.setText("");
    }

    /**
     * Helper method to delete the currently selected Image.
     */
    private void deleteSelectedImage() {
	if (currentImg == null) return;

	if (currentList instanceof BibliotecaImatges)
	    ctrl.removeImageFromAll(currentImg);
	else
	    currentList.removeImage(currentImg);

	refreshImageList();
	showImage(null);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jImageModeButtons = new javax.swing.ButtonGroup();
        jPopupImageList = new javax.swing.JPopupMenu();
        jPopItemEditImage = new javax.swing.JMenuItem();
        jPopItemDeleteImage = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        jImageList = new javax.swing.JList();
        jLabelImatges = new javax.swing.JLabel();
        jAlbumComboBox = new javax.swing.JComboBox();
        jSeparatorImatges = new javax.swing.JSeparator();
        jAddImage = new javax.swing.JButton();
        jDeleteImage = new javax.swing.JButton();
        jImagePanel = new javax.swing.JPanel();
        jImageLabel = new javax.swing.JLabel();
        jOptionsPanel = new javax.swing.JPanel();
        jImageDefault = new javax.swing.JRadioButton();
        jImageSepia = new javax.swing.JRadioButton();
        jImageBlackWhite = new javax.swing.JRadioButton();
        jPlaySpeed = new javax.swing.JTextField();
        jSpeedSlider = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        jShufflePlay = new javax.swing.JToggleButton();
        jInformationPanel = new javax.swing.JPanel();
        jLabelNImages = new javax.swing.JLabel();
        jLabelAuthor = new javax.swing.JLabel();
        jLabelTitle = new javax.swing.JLabel();
        jAuthorField = new javax.swing.JTextField();
        jAlbumTitle = new javax.swing.JTextField();
        jImageNo = new javax.swing.JTextField();
        jPlaylistControlsPanel = new javax.swing.JPanel();
        jPlayPlaylist = new javax.swing.JButton();
        jPausePlaylist = new javax.swing.JButton();
        jStopPlaylist = new javax.swing.JButton();
        jPlaylistProgress = new javax.swing.JProgressBar();
        jEditAlbum = new javax.swing.JButton();
        jDeleteAlbum = new javax.swing.JButton();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemLoad = new javax.swing.JMenuItem();
        jMenuItemLoad.setAccelerator(  KeyStroke.getKeyStroke(  KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        jMenuItemSave = new javax.swing.JMenuItem();
        jMenuItemSave.setAccelerator(  KeyStroke.getKeyStroke(  KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        jMenuItemSeparator = new javax.swing.JPopupMenu.Separator();
        jMenuExit = new javax.swing.JMenuItem();
        jMenuExit.setAccelerator(  KeyStroke.getKeyStroke(  KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        jMenuOptions = new javax.swing.JMenu();
        jMenuItemSaveOnExit = new javax.swing.JCheckBoxMenuItem();
        jMenuItemSaveOnExit.setAccelerator(  KeyStroke.getKeyStroke(  KeyEvent.VK_S, ActionEvent.ALT_MASK));

        jPopItemEditImage.setText("Editar Imatge");
        jPopItemEditImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPopItemEditImageActionPerformed(evt);
            }
        });
        jPopupImageList.add(jPopItemEditImage);

        jPopItemDeleteImage.setText("Elimnar Imatge");
        jPopItemDeleteImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPopItemDeleteImageActionPerformed(evt);
            }
        });
        jPopupImageList.add(jPopItemDeleteImage);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
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

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 180, 250));

        jLabelImatges.setText("Imatges");
        getContentPane().add(jLabelImatges, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 90, -1));

        jAlbumComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Biblioteca..." }));
        jAlbumComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAlbumComboBoxActionPerformed(evt);
            }
        });
        getContentPane().add(jAlbumComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 180, 20));
        getContentPane().add(jSeparatorImatges, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 180, 10));

        jAddImage.setText("Afegir");
        jAddImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAddImageActionPerformed(evt);
            }
        });
        getContentPane().add(jAddImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 80, -1));

        jDeleteImage.setText("Eliminar");
        jDeleteImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDeleteImageActionPerformed(evt);
            }
        });
        getContentPane().add(jDeleteImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 360, 80, -1));

        jImagePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jImageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jImageLabel.setText("No Imatge");

        javax.swing.GroupLayout jImagePanelLayout = new javax.swing.GroupLayout(jImagePanel);
        jImagePanel.setLayout(jImagePanelLayout);
        jImagePanelLayout.setHorizontalGroup(
            jImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jImageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
        );
        jImagePanelLayout.setVerticalGroup(
            jImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jImageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
        );

        getContentPane().add(jImagePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, 460, 340));

        jOptionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Opcions"));

        jImageModeButtons.add(jImageDefault);
        jImageDefault.setSelected(true);
        jImageDefault.setText("Normal");

        jImageModeButtons.add(jImageSepia);
        jImageSepia.setText("Sepia");

        jImageModeButtons.add(jImageBlackWhite);
        jImageBlackWhite.setText("B&W");

        jPlaySpeed.setEditable(false);
        jPlaySpeed.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPlaySpeed.setText("1000 ms");

        jSpeedSlider.setMajorTickSpacing(1000);
        jSpeedSlider.setMaximum(5000);
        jSpeedSlider.setMinorTickSpacing(500);
        jSpeedSlider.setPaintTicks(true);
        jSpeedSlider.setValue(1000);

        jLabel1.setText("Velocidat:");

        jShufflePlay.setText("Shuffle");

        javax.swing.GroupLayout jOptionsPanelLayout = new javax.swing.GroupLayout(jOptionsPanel);
        jOptionsPanel.setLayout(jOptionsPanelLayout);
        jOptionsPanelLayout.setHorizontalGroup(
            jOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jOptionsPanelLayout.createSequentialGroup()
                .addGroup(jOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jImageDefault)
                    .addComponent(jImageSepia)
                    .addComponent(jImageBlackWhite))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jOptionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSpeedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPlaySpeed)
                    .addComponent(jShufflePlay, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPlaySpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSpeedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jShufflePlay)
                .addContainerGap())
        );

        getContentPane().add(jOptionsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 160, 100, 220));

        jInformationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Informació"));

        jLabelNImages.setText("Nº Imgs.:");

        jLabelAuthor.setText("Autor:");

        jLabelTitle.setText("Titol:");

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
                            .addComponent(jLabelTitle)
                            .addComponent(jLabelAuthor)
                            .addGroup(jInformationPanelLayout.createSequentialGroup()
                                .addComponent(jLabelNImages)
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
                .addComponent(jLabelAuthor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jAuthorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jAlbumTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNImages)
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
                .addComponent(jPlaylistProgress, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
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

        getContentPane().add(jPlaylistControlsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 350, 460, 40));

        jEditAlbum.setText("Editar");
        jEditAlbum.setDefaultCapable(false);
        jEditAlbum.setEnabled(false);
        jEditAlbum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jEditAlbumActionPerformed(evt);
            }
        });
        getContentPane().add(jEditAlbum, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 80, 20));

        jDeleteAlbum.setText("Elimiar");
        jDeleteAlbum.setDefaultCapable(false);
        jDeleteAlbum.setEnabled(false);
        jDeleteAlbum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDeleteAlbumActionPerformed(evt);
            }
        });
        getContentPane().add(jDeleteAlbum, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, 80, 20));

        jMenuFile.setText("Fitxer");

        jMenuItemLoad.setText("Obrir Arxiu...");
        jMenuItemLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemLoadActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemLoad);

        jMenuItemSave.setText("Guardar...");
        jMenuItemSave.setToolTipText("");
        jMenuItemSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemSave);
        jMenuFile.add(jMenuItemSeparator);

        jMenuExit.setText("Sortir");
        jMenuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuExitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuExit);

        jMenuBar.add(jMenuFile);

        jMenuOptions.setText("Opcions");

        jMenuItemSaveOnExit.setText("Guardar al sortir");
        jMenuOptions.add(jMenuItemSaveOnExit);

        jMenuBar.add(jMenuOptions);

        setJMenuBar(jMenuBar);

        setSize(new java.awt.Dimension(793, 450));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jAddImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAddImageActionPerformed
	if (DEBUG) System.out.println("Adding image to: " + currentList);

	// Start new Image Forms
	JFrame newForm = null;
	if (currentList instanceof BibliotecaImatges)
	    // Add Image to library
	    newForm = new FormAddImageToLib(ctrl);
	else
	    // Add iamge to Album
	    newForm = new FormAddImageToAlbum(currentList, ctrl);

	if (newForm == null) throw new Error("Could not start a new form.");

	// Show form and refresh imagelist on form close.
	newForm.setVisible(true);
	newForm.setLocationRelativeTo(this);
	newForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	newForm.addWindowListener(new RefreshOnCloseListener());
    }//GEN-LAST:event_jAddImageActionPerformed

    /**
     * Library, album, or new album selected on the combo box.
     */
    private void jAlbumComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAlbumComboBoxActionPerformed
	// <editor-fold defaultstate="collapsed" desc="jImageListSelectorActionPerformed code.">
	JComboBox cbox = (JComboBox)evt.getSource();
	// The albm index is the index -1 since the first item is the library.
	int alIndex = cbox.getSelectedIndex() - 1;

	// TODO Handle selection
	switch ((String)cbox.getSelectedItem()) {
	    case LIBRARY:
		if (DEBUG) System.out.println("Library selected");

		this.currentList = ctrl.getData().getLib();
		jEditAlbum.setEnabled(false);
		jDeleteAlbum.setEnabled(false);
		this.refreshImageList();
		break;
	    case NEW_ALBUM:
		if (DEBUG) System.out.println("New Album selected");

		jEditAlbum.setEnabled(false);
		jDeleteAlbum.setEnabled(false);
		String alTitle = JOptionPane.showInputDialog(this, "Entra un nou Titol per l'album");
		String alAuthor = JOptionPane.showInputDialog(this, "Entra un autor per l'album");
		// Exit if used closed any dialog
		if (alTitle == null || alAuthor == null)
		    break;

		if ((ctrl.addAlbum(alTitle, alAuthor, 10)))
		    JOptionPane.showMessageDialog(this, "Album afegit correctament.");
		else
		    JOptionPane.showMessageDialog(this, "Error afegint el nou album.", "Error", JOptionPane.ERROR_MESSAGE);

		refreshAlbumsComboBox();
		break;
	    default:
		if (DEBUG) System.out.println("Album selected");
		this.currentList = ctrl.getData().getAlbum(alIndex);
		jEditAlbum.setEnabled(true);
		jDeleteAlbum.setEnabled(true);
		this.refreshImageList();
		break;
	}
	// </editor-fold>
    }//GEN-LAST:event_jAlbumComboBoxActionPerformed

    private void jMenuItemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveActionPerformed
	JFileChooser fc = new JFileChooser(".");
	// If cancelled or error, exit.
	if (fc.showDialog(this, null) != JFileChooser.APPROVE_OPTION) return;

	File output = fc.getSelectedFile();
	if (ctrl.saveDataToObjectStream(output.getAbsolutePath()))
	    JOptionPane.showMessageDialog(this, "Base de dades guardada correctament.");
	else
	    JOptionPane.showMessageDialog(this, "Error guardant la base de dades.", "Error", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_jMenuItemSaveActionPerformed

    private void jMenuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuExitActionPerformed
	// Call the window closing event handler
	this.getWindowListeners()[0].windowClosing(new WindowEvent(this, 0));
    }//GEN-LAST:event_jMenuExitActionPerformed

    private void jMenuItemLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLoadActionPerformed
	JFileChooser fc = new JFileChooser(".");
	// If cancelled or error, exit
	if (fc.showDialog(this, null) != JFileChooser.APPROVE_OPTION) return;

	File input = fc.getSelectedFile();
	if (ctrl.loadDataFromObjectStream(input.getAbsolutePath())) {
	    JOptionPane.showMessageDialog(this, "Base de dades carregada correctament.");
	    this.currentList = ctrl.getData().getLib();
	    this.refreshAlbumsComboBox();
	    this.refreshImageList();
	} else
	    JOptionPane.showMessageDialog(this, "Error carregant la base de dades.", "Error", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_jMenuItemLoadActionPerformed

    /**
     * Delete an image from the current list.
     */
    private void jDeleteImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDeleteImageActionPerformed
	deleteSelectedImage();
    }//GEN-LAST:event_jDeleteImageActionPerformed

    private void jEditAlbumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jEditAlbumActionPerformed
	// The selected list is an album and so we are to delete it
	JFrame form = new FormEditAlbum((AlbumImatges)currentList);
	// Refresh album info on close
	form.addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosed(WindowEvent e) {
		currentList = ctrl.getData().getLib();
		refreshAlbumsComboBox();
		refreshImageList();
	    }
	});
	form.setVisible(true);
    }//GEN-LAST:event_jEditAlbumActionPerformed

    private Stack<Integer> playOrder = new Stack<Integer>();

    private void jPlayPlaylistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPlayPlaylistActionPerformed
	if (currentList != null && currentList.getSize() > 1) {
	    // Createa a play order to use, shuffling if necessary
	    playOrder.clear();
	    // Add back to front since it's a Stack
	    for (int i = currentList.getSize() - 1; i >= 0; i--)
		playOrder.add(i);
	    // Shuffle
	    if (jShufflePlay.isSelected())
		Collections.shuffle(playOrder);

	    if (DEBUG) System.out.println(playOrder.toString());
	    ctrl.setTimer(jSpeedSlider.getValue());
	    ctrl.play(currentList.getSize());
	} else {
	    JOptionPane.showMessageDialog(this, "La llista es massa petita per fer Play.");
	}
    }//GEN-LAST:event_jPlayPlaylistActionPerformed

    private void jPausePlaylistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPausePlaylistActionPerformed
	ctrl.pause();
    }//GEN-LAST:event_jPausePlaylistActionPerformed

    private void jStopPlaylistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jStopPlaylistActionPerformed
	ctrl.stop();
    }//GEN-LAST:event_jStopPlaylistActionPerformed

    private void jPopItemEditImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPopItemEditImageActionPerformed
	JFrame form = new FormEditImage(currentImg, currentList, ctrl);
	form.setVisible(true);
	// Refresh list and display on close
	form.addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosed(WindowEvent e) {
		refreshImageList();
		currentImg = null;
		showImage(null);
	    }
	});
    }//GEN-LAST:event_jPopItemEditImageActionPerformed

    private void jPopItemDeleteImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPopItemDeleteImageActionPerformed
	deleteSelectedImage();
    }//GEN-LAST:event_jPopItemDeleteImageActionPerformed

    private void jDeleteAlbumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDeleteAlbumActionPerformed
	ctrl.getData().removeAlbum((AlbumImatges)currentList);
	refreshAlbumsComboBox();
	jAlbumComboBox.setSelectedIndex(0);
	refreshImageList();
    }//GEN-LAST:event_jDeleteAlbumActionPerformed

    private class RefreshOnCloseListener extends WindowAdapter {

	@Override
	public void windowClosed(WindowEvent e) {
	    refreshImageList();
	}
    }

    private class CustomTimerListener extends VisorTimerListener {

	// <editor-fold defaultstate="collapsed" desc="CustomTimerListener code for list playback code.">
	@Override
	public void onStart() {
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
	    int next = playOrder.pop();

	    showImage((Imatge)currentList.getAt(next));
	    jPlaylistProgress.setValue(index + 1);
	    jPlaylistProgress.setString(index + 1 + "/" + currentList.getSize());
	}

	@Override
	public void onPause() {
	    jPlayPlaylist.setEnabled(true);
	    jPausePlaylist.setEnabled(false);
	    jStopPlaylist.setEnabled(true);
	}

	@Override
	public void onResume() {
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
	// </editor-fold>
    }

    private class ImageListSelectionListener implements ListSelectionListener {
	// <editor-fold defaultstate="collapsed" desc="ImageListSelectionListener code for showing images from the list.">

	@Override
	public void valueChanged(ListSelectionEvent e) {
	    if (e.getValueIsAdjusting()) {
		return;
	    }

	    int index = jImageList.getSelectedIndex();
	    if (index < 0) {
		return;
	    }

	    if (DEBUG) System.out.println("Triggered list: " + index);

	    if (index >= currentList.getSize()) {
		return;
	    }

	    Imatge img = (Imatge)currentList.getAt(index);
	    showImage(img);

	}

	// </editor-fold>
    }

    /**
     * Popup menu listener for the Image list
     */
    private class PopClickListener extends MouseAdapter {

	@Override
	public void mouseReleased(MouseEvent e) {
	    if (e.isPopupTrigger())
		doPop(e);
	}

	private void doPop(MouseEvent e) {
	    // If we right-clicked on the JList go ahead
	    if (e.getComponent() instanceof JList) {
		JList list = (JList)e.getComponent();
		imageListIndex = list.locationToIndex(e.getPoint());

		// If we selected nothing, return
		if (imageListIndex == -1) return;

		// Set the new index and show the popup menu
		int x = e.getX();
		int y = e.getY();
		list.setSelectedIndex(imageListIndex);
		jPopupImageList.show(e.getComponent(), x, y);
	    }
	}
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jAddImage;
    private javax.swing.JComboBox jAlbumComboBox;
    private javax.swing.JTextField jAlbumTitle;
    private javax.swing.JTextField jAuthorField;
    private javax.swing.JButton jDeleteAlbum;
    private javax.swing.JButton jDeleteImage;
    private javax.swing.JButton jEditAlbum;
    private javax.swing.JRadioButton jImageBlackWhite;
    private javax.swing.JRadioButton jImageDefault;
    private javax.swing.JLabel jImageLabel;
    private javax.swing.JList jImageList;
    private javax.swing.ButtonGroup jImageModeButtons;
    private javax.swing.JTextField jImageNo;
    private javax.swing.JPanel jImagePanel;
    private javax.swing.JRadioButton jImageSepia;
    private javax.swing.JPanel jInformationPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelAuthor;
    private javax.swing.JLabel jLabelImatges;
    private javax.swing.JLabel jLabelNImages;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuExit;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenuItem jMenuItemLoad;
    private javax.swing.JMenuItem jMenuItemSave;
    private javax.swing.JCheckBoxMenuItem jMenuItemSaveOnExit;
    private javax.swing.JPopupMenu.Separator jMenuItemSeparator;
    private javax.swing.JMenu jMenuOptions;
    private javax.swing.JPanel jOptionsPanel;
    private javax.swing.JButton jPausePlaylist;
    private javax.swing.JButton jPlayPlaylist;
    private javax.swing.JTextField jPlaySpeed;
    private javax.swing.JPanel jPlaylistControlsPanel;
    private javax.swing.JProgressBar jPlaylistProgress;
    private javax.swing.JMenuItem jPopItemDeleteImage;
    private javax.swing.JMenuItem jPopItemEditImage;
    private javax.swing.JPopupMenu jPopupImageList;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparatorImatges;
    private javax.swing.JToggleButton jShufflePlay;
    private javax.swing.JSlider jSpeedSlider;
    private javax.swing.JButton jStopPlaylist;
    // End of variables declaration//GEN-END:variables
}
