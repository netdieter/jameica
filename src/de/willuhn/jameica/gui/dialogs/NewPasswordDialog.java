/**********************************************************************
 * $Source: /cvsroot/jameica/jameica/src/de/willuhn/jameica/gui/dialogs/NewPasswordDialog.java,v $
 * $Revision: 1.2 $
 * $Date: 2005/02/01 17:15:19 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/
package de.willuhn.jameica.gui.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.util.Color;
import de.willuhn.jameica.gui.util.SWTUtil;
import de.willuhn.jameica.system.OperationCanceledException;

/**
 * Dialog zur Neuvergabe von Passworten.
 * Die Klasse implementiert bereits die Funktion<code>checkPassword(String,String)</code>
 * und prueft dort, ob ueberhaupt Passworter eingegeben wurden und ob beide
 * uebereinstimmen. Sollen weitere Pruefungen vorgenommen werden, dann bitte
 * einfach diese Funktion ueberschreiben.
 */
public class NewPasswordDialog extends SimpleDialog {

	private Composite comp = null;
	private Label label = null;
	private Label pLabel = null;

	private Label pLabel2 = null;

	private Label error = null;

	private Text password = null;
	private Text password2 = null;

	private Button button = null;
	private Button cancel = null;
	
	private String labelText 					= "";
	private String labelText2					= "";
	
	private String enteredPassword = "";

	/**
	 * Erzeugt einen neuen Passwort-Dialog.
	 * @param position Position des Dialogs.
	 * @see AbstractDialog#POSITION_MOUSE
	 * @see AbstractDialog#POSITION_CENTER
	 */
  public NewPasswordDialog(int position) {
    super(position);
    labelText = i18n.tr("Neues Passwort");
		labelText2 = i18n.tr("Passwort-Wiederholung");
		setSideImage(SWTUtil.getImage("password.gif"));
  }

	/**
	 * Speichert den Text, der links neben dem Eingabefeld fuer die
	 * Passwort-Eingabe angezeigt werden soll (Optional).
   * @param text anzuzeigender Text.
   */
  protected void setLabelText(String text)
	{
		if (text == null || text.length() == 0)
			return;
		labelText = text;
	}

	/**
	 * Speichert den Text, der links neben dem Eingabefeld fuer die
	 * Passwort-Wiederholung angezeigt werden soll (Optional).
	 * @param text anzuzeigender Text.
	 */
	protected void setLabel2Text(String text)
	{
		if (text == null || text.length() == 0)
			return;
		labelText2 = text;
	}

	/**
	 * Zeigt den uebergebenen Text rot markiert links neben dem OK-Button an.
	 * Diese Funktion sollte aus <code>checkPassword(String)</code> heraus
	 * aufgerufen werden, um dem benutzer zu zeigen, <b>warum</b> seine
	 * Passwort-Eingabe falsch war. 
   * @param text Der anzuzeigende Fehlertext.
   */
  protected final void setErrorText(String text)
	{
		if (text == null || text.length() == 0)
			return;
		error.setText(text);
	}

  /**
   * @see de.willuhn.jameica.gui.dialogs.AbstractDialog#paint(org.eclipse.swt.widgets.Composite)
   */
  protected void paint(Composite parent) throws Exception
	{
		// Composite um alles drumrum.
		comp = new Composite(parent,SWT.NONE);
		comp.setBackground(Color.BACKGROUND.getSWTColor());
		comp.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout gl = new GridLayout(3,false);
		comp.setLayout(gl);
		
		// Text
		label = GUI.getStyleFactory().createLabel(comp,SWT.WRAP);
		label.setText(getText());
		GridData grid = new GridData(GridData.FILL_HORIZONTAL);
		grid.horizontalSpan = 3;
		label.setLayoutData(grid);
		
		// Fehlertext 
		error = GUI.getStyleFactory().createLabel(comp,SWT.WRAP);
		error.setForeground(Color.ERROR.getSWTColor());
		GridData grid2 = new GridData(GridData.FILL_HORIZONTAL);
		grid2.horizontalSpan = 3;
		grid2.horizontalIndent = 0;
		error.setLayoutData(grid2);

		// Label vor Eingabefeld
		pLabel = GUI.getStyleFactory().createLabel(comp,SWT.NONE);
		pLabel.setText(labelText);
		pLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

		password = GUI.getStyleFactory().createText(comp);
		password.setEchoChar('*');
		GridData grid3 = new GridData(GridData.FILL_HORIZONTAL);
		grid3.horizontalSpan = 2;
		password.setLayoutData(grid3);

		pLabel2 = GUI.getStyleFactory().createLabel(comp,SWT.NONE);
		pLabel2.setText(labelText2);
		pLabel2.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

		password2 = GUI.getStyleFactory().createText(comp);
		password2.setEchoChar('*');
		GridData grid4 = new GridData(GridData.FILL_HORIZONTAL);
		grid4.horizontalSpan = 2;
		password2.setLayoutData(grid4);

		// Dummy-Label damit die Buttons buendig unter dem Eingabefeld stehen
		Label dummy = GUI.getStyleFactory().createLabel(comp,SWT.NONE);
		dummy.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// OK-Button
		button = GUI.getStyleFactory().createButton(comp);
		button.setText("    " + i18n.tr("OK") + "    ");
		button.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		getShell().setDefaultButton(button);
		button.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				String p = password.getText();
				String p2 = password2.getText();

				if (!checkPassword(p,p2))
					return;

				enteredPassword = p;
				close();
			}
		});

		// Abbrechen-Button
		cancel = GUI.getStyleFactory().createButton(comp);
		cancel.setText(i18n.tr("Abbrechen"));
		cancel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cancel.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				throw new OperationCanceledException("Dialog abgebrochen");
      }
    });

		// so und jetzt noch der Shell-Listener, damit der
		// User den Dialog nicht schliessen kann ohne was
		// einzugeben ;)
		addShellListener(new ShellListener() {
			public void shellClosed(ShellEvent e) {
				throw new OperationCanceledException("dialog cancelled via close button");
			}
      public void shellActivated(ShellEvent e) {}
      public void shellDeactivated(ShellEvent e) {}
      public void shellDeiconified(ShellEvent e) {}
      public void shellIconified(ShellEvent e) {}
    });
	}		

  /**
   * Prueft die Eingabe der Passworte.
   * @param password das gerade eingegebene Passwort.
   * @param password2 die Passwort-Wiederholung.
   * @return true, wenn die Eingabe OK ist, andernfalls false.
   */
  protected boolean checkPassword(String password, String password2)
  {
  	boolean b = (password != null && password.length() > 0 &&
  							 password2 != null && password2.length() > 0 &&
  					     password.equals(password2));
    if (!b)
    	setErrorText(i18n.tr("Die eingegebenen Passworte stimmen nicht �berein."));
    return b;
  }
	
  /**
   * @see de.willuhn.jameica.gui.dialogs.AbstractDialog#getData()
   */
  protected Object getData() throws Exception {
    return enteredPassword;
  }

}


/**********************************************************************
 * $Log: NewPasswordDialog.java,v $
 * Revision 1.2  2005/02/01 17:15:19  willuhn
 * *** empty log message ***
 *
 * Revision 1.1  2005/01/30 20:47:43  willuhn
 * *** empty log message ***
 *
 * Revision 1.14  2004/10/19 23:33:44  willuhn
 * *** empty log message ***
 *
 * Revision 1.13  2004/10/18 23:37:42  willuhn
 * *** empty log message ***
 *
 * Revision 1.12  2004/08/15 17:55:17  willuhn
 * @C sync handling
 *
 * Revision 1.11  2004/07/27 23:41:30  willuhn
 * *** empty log message ***
 *
 * Revision 1.10  2004/06/10 20:56:53  willuhn
 * @D javadoc comments fixed
 *
 * Revision 1.9  2004/05/23 16:34:18  willuhn
 * *** empty log message ***
 *
 * Revision 1.8  2004/05/23 15:30:52  willuhn
 * @N new color/font management
 * @N new styleFactory
 *
 * Revision 1.7  2004/03/06 18:24:24  willuhn
 * @D javadoc
 *
 * Revision 1.6  2004/03/03 22:27:10  willuhn
 * @N help texts
 * @C refactoring
 *
 * Revision 1.5  2004/02/24 22:46:53  willuhn
 * @N GUI refactoring
 *
 * Revision 1.4  2004/02/23 20:30:34  willuhn
 * @C refactoring in AbstractDialog
 *
 * Revision 1.3  2004/02/22 20:05:21  willuhn
 * @N new Logo panel
 *
 * Revision 1.2  2004/02/21 19:49:41  willuhn
 * *** empty log message ***
 *
 * Revision 1.1  2004/02/20 20:45:24  willuhn
 * *** empty log message ***
 *
 * Revision 1.2  2004/02/20 01:25:06  willuhn
 * @N nice dialog
 * @N busy indicator
 * @N new status bar
 *
 * Revision 1.1  2004/02/17 00:53:47  willuhn
 * *** empty log message ***
 *
 * Revision 1.1  2004/02/12 23:46:27  willuhn
 * *** empty log message ***
 *
 **********************************************************************/