/**********************************************************************
 * $Source: /cvsroot/jameica/jameica/src/de/willuhn/jameica/gui/views/Attic/SimpleDialog.java,v $
 * $Revision: 1.2 $
 * $Date: 2004/02/17 00:53:47 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/
package de.willuhn.jameica.gui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Billiger Dialog, der nur einen Text und einen OK-Button enthaelt.
 */
public class SimpleDialog extends Dialog {

	private Composite comp = null;
	private Label label = null;
	private Button button = null;
	
  /**
   * Erzeugt den Dialog.
   */
  public SimpleDialog() {
    super();
  }

	/**
	 * Speichert den anzuzeigenden Text.
   * @param text anzuzeigender Text.
   */
  public void setText(String text)
	{
		if (label != null && comp != null)
		{
			label.setText(text);
			return;
		}

		shell.setLayout(new GridLayout(1,false));

		comp = new Composite(shell,SWT.NONE);
		comp.setLayoutData(new GridData(GridData.FILL_BOTH));
		comp.setLayout(new GridLayout(1,false));
		
		label = new Label(comp,SWT.WRAP);
		label.setText(text);
		label.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		button = new Button(comp, SWT.NONE);
		button.setText("    OK    ");
		button.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		button.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				close();
			}
		});
	}
}


/**********************************************************************
 * $Log: SimpleDialog.java,v $
 * Revision 1.2  2004/02/17 00:53:47  willuhn
 * *** empty log message ***
 *
 * Revision 1.1  2004/02/12 23:46:27  willuhn
 * *** empty log message ***
 *
 **********************************************************************/