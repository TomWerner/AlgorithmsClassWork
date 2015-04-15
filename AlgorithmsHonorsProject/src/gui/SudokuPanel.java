package gui;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@SuppressWarnings("serial")
public class SudokuPanel extends JPanel
{

    private JTextField[] cells;

    /**
     * Create the panel.
     */
    @SuppressWarnings("deprecation")
    public SudokuPanel()
    {
        super();
        cells = new JTextField[81];

        GridLayout layout = new GridLayout(9, 9, 10, 10);
        setLayout(layout);

        for (int i = 0; i < 81; i++)
        {
            cells[i] = new JTextField(1);
            add("cell" + i, cells[i]);
        }
        for (int i = 0; i < 81; i++)
        {
            final JTextField temp = cells[i];
            final int j = i;
            temp.getDocument().addDocumentListener(new DocumentListener()
            {
                public void changedUpdate(DocumentEvent e)
                {
                    handleInput(e);
                }

                public void removeUpdate(DocumentEvent e)
                {
                    handleInput(e);
                }

                public void insertUpdate(DocumentEvent e)
                {
                    handleInput(e);
                }

                private void handleInput(DocumentEvent e)
                {
                }
            });
            cells[i].setNextFocusableComponent(cells[(i + 1) % 81]);
        }

        int size = Math.min(getWidth(), getHeight());
        setBounds(getX(), getY(), size, size);
    }

    public String getSudoku()
    {
        StringBuilder result = new StringBuilder(81);
        for (int i = 0; i < cells.length; i++)
        {
            String text = cells[i].getText();
            if (text.length() == 0 || (text.length() > 0 && !Character.isDigit(text.charAt(0))))
                result.append('.');
            else
                result.append(text.charAt(0));
        }
        return result.toString();
    }

    public void setCells(char[] result)
    {
        assert (result.length == 81);
        for (int i = 0; i < result.length; i++)
            cells[i].setText("" + result[i]);
    }

    public void resetCells()
    {
        for (int i = 0; i < cells.length; i++)
            cells[i].setText("");
    }
}
