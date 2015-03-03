package gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import solver.SudokuSolver;

/**
 * Main window of the Anagram Game application.
 */
public class SudokuGUI extends JFrame
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static void main(String[] args)
    {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try
        {
            javax.swing.UIManager.LookAndFeelInfo[] installedLookAndFeels = javax.swing.UIManager
                    .getInstalledLookAndFeels();
            for (int idx = 0; idx < installedLookAndFeels.length; idx++)
                if ("Nimbus".equals(installedLookAndFeels[idx].getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(installedLookAndFeels[idx].getClassName());
                    break;
                }
        }
        catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(SudokuGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(SudokuGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(SudokuGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(SudokuGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        // </editor-fold>

        /* Create and display the form */
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new SudokuGUI().setVisible(true);
            }
        });
    }
    
    /** Creates new form Anagrams */
    public SudokuGUI()
    {
        initComponents();
        pack();
        setLocation(100, 100);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents()
    {
        leftSudoku = new SudokuPanel();
        rightSudoku = new SudokuPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        solveButton = new javax.swing.JButton();
        resetLeftButton = new javax.swing.JButton();
        resetRightButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setTitle("Sudoku Solver");
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                exitForm(evt);
            }
        });

        leftSudoku.setBorder(new javax.swing.border.SoftBevelBorder(0));

        rightSudoku.setBorder(new javax.swing.border.SoftBevelBorder(0));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Sudoku Solver");
        jLabel1.setBorder(javax.swing.BorderFactory.createCompoundBorder());

        solveButton.setText("Solve");
        solveButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                solveButtonActionPerformed(evt);
            }
        });

        resetLeftButton.setText("Reset Left");
        resetLeftButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                resetLeftButtonActionPerformed(evt);
            }
        });

        resetRightButton.setText("Reset Right");
        resetRightButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                resetRightButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Created by: Tom Werner");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                        jPanel3Layout
                                .createSequentialGroup()
                                .addContainerGap()
                                .addGroup(
                                        jPanel3Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(resetLeftButton,
                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 154,
                                                        Short.MAX_VALUE)
                                                .addComponent(solveButton, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(resetRightButton, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(
                                                        jPanel3Layout.createSequentialGroup().addComponent(jLabel2)
                                                                .addGap(0, 0, Short.MAX_VALUE))).addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                        jPanel3Layout
                                .createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(solveButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(resetLeftButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(resetRightButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 314,
                                        Short.MAX_VALUE).addComponent(jLabel2).addContainerGap()));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(leftSudoku, javax.swing.GroupLayout.PREFERRED_SIZE, 478,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(rightSudoku, javax.swing.GroupLayout.PREFERRED_SIZE, 478,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                javax.swing.GroupLayout.Alignment.TRAILING,
                layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(rightSudoku, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(leftSudoku, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap()));
    }// </editor-fold>

    private void exitForm(java.awt.event.WindowEvent evt)
    {
        System.exit(0);
    }

    private void solveButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        String puzzle = leftSudoku.getSudoku();
        char[] result = new SudokuSolver().solveSudoku(puzzle, false);
        rightSudoku.setCells(result);
    }

    private void resetLeftButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        leftSudoku.resetCells();
    }

    private void resetRightButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        rightSudoku.resetCells();
    }

    // Variables declaration - do not modify
    private javax.swing.JLabel  jLabel1;
    private javax.swing.JLabel  jLabel2;
    private SudokuPanel  leftSudoku;
    private SudokuPanel  rightSudoku;
    private javax.swing.JPanel  jPanel3;
    private javax.swing.JButton resetLeftButton;
    private javax.swing.JButton resetRightButton;
    private javax.swing.JButton solveButton;
    // End of variables declaration

}
