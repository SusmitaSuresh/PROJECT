package gui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class DocumentFilter2 extends DocumentFilter		//this class filters the input taken from keyboard
{
	int limit;
	public DocumentFilter2(int limit)
	{
		this.limit = limit;
	}
	
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
            throws BadLocationException 
    {
        
        int currentLength = fb.getDocument().getLength();
        int nextLength = currentLength + text.length() - length;

        if (nextLength <= limit) 
        {
            super.replace(fb, offset, length, text, attrs);
        } 
        else 
        {
            java.awt.Toolkit.getDefaultToolkit().beep();		//beeps when limit crossed
        }
    }
}