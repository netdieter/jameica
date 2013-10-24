/**********************************************************************
 * $Source: /cvsroot/jameica/jameica/src/de/willuhn/jameica/gui/boxes/FirstStart.java,v $
 * $Revision: 1.5 $
 * $Date: 2011/06/09 10:07:59 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.gui.boxes;

import java.rmi.RemoteException;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;

import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.internal.action.FileClose;
import de.willuhn.jameica.gui.internal.action.PluginInstall;
import de.willuhn.jameica.gui.internal.views.Settings;
import de.willuhn.jameica.gui.parts.Button;
import de.willuhn.jameica.gui.parts.InfoPanel;
import de.willuhn.jameica.messaging.Message;
import de.willuhn.jameica.messaging.MessageConsumer;
import de.willuhn.jameica.messaging.PluginCacheMessageConsumer;
import de.willuhn.jameica.messaging.PluginMessage;
import de.willuhn.jameica.messaging.PluginMessage.Event;
import de.willuhn.jameica.plugin.PluginLoader;
import de.willuhn.jameica.system.Application;
import de.willuhn.util.I18N;

/**
 * Eine Box, die angezeigt wird, wenn noch keine Plugins installiert sind.
 */
public class FirstStart extends AbstractBox
{
  private MessageConsumer mc = new MyMessageConsumer();

  /**
   * @see de.willuhn.jameica.gui.boxes.Box#getName()
   */
  public String getName()
  {
    return "Jameica: " + Application.getI18n().tr("Erster Start");
  }

  /**
   * @see de.willuhn.jameica.gui.boxes.AbstractBox#isActive()
   */
  public boolean isActive()
  {
    return isEnabled();
  }

  /**
   * @see de.willuhn.jameica.gui.boxes.AbstractBox#isEnabled()
   */
  public boolean isEnabled()
  {
    // Nur anzeigen, wenn wirklich noch keine Plugins vorhanden sind.
    PluginLoader loader = Application.getPluginLoader();
    return loader.getInstalledPlugins().size() == 0 && loader.getInitErrors().size() == 0;
  }

  /**
   * @see de.willuhn.jameica.gui.boxes.Box#setEnabled(boolean)
   */
  public void setEnabled(boolean enabled)
  {
    // Das darf der User nicht.
  }

  /**
   * @see de.willuhn.jameica.gui.boxes.Box#getDefaultEnabled()
   */
  public boolean getDefaultEnabled()
  {
    return true;
  }

  /**
   * @see de.willuhn.jameica.gui.boxes.Box#getDefaultIndex()
   */
  public int getDefaultIndex()
  {
    return 0;
  }

  /**
   * @see de.willuhn.jameica.gui.Part#paint(org.eclipse.swt.widgets.Composite)
   */
  public void paint(Composite parent) throws RemoteException
  {
    I18N i18n = Application.getI18n();
    
    // User hat gerade ein Plugin installiert - es ist aber noch nicht aktiviert
    boolean pending = PluginCacheMessageConsumer.getCache().size() > 0;

    InfoPanel panel = new InfoPanel();
    panel.setTitle(i18n.tr(pending ? "Plugin noch nicht aktiviert" : "Noch keine Plugins installiert"));
    panel.setText(i18n.tr(pending ? "Bitte starten Sie Jameica jetzt neu." : "Bitte klicken Sie auf \"Neues Plugin installieren...\", um ein neues Plugin hinzuzufügen."));
    
    if (pending)
      panel.addButton(new Button(i18n.tr("Jameica beenden"),new FileClose(),null,true,"window-close.png"));
    
    panel.addButton(new Button(i18n.tr("Neues Plugin installieren..."),new PluginInstall(),null,true,"emblem-package.png"));
    panel.paint(parent);
    
    Application.getMessagingFactory().registerMessageConsumer(this.mc);
    parent.addDisposeListener(new DisposeListener() {
      public void widgetDisposed(DisposeEvent e)
      {
        Application.getMessagingFactory().unRegisterMessageConsumer(mc);
      }
    });
  }
  
  /**
   * Wird benachrichtigt, wenn das Plugin installiert ist.
   */
  private class MyMessageConsumer implements MessageConsumer
  {
    /**
     * @see de.willuhn.jameica.messaging.MessageConsumer#getExpectedMessageTypes()
     */
    public Class[] getExpectedMessageTypes()
    {
      return new Class[]{PluginMessage.class};
    }

    /**
     * @see de.willuhn.jameica.messaging.MessageConsumer#handleMessage(de.willuhn.jameica.messaging.Message)
     */
    public void handleMessage(Message message) throws Exception
    {
      PluginMessage msg = (PluginMessage) message;
      if (msg.getEvent() == Event.INSTALLED)
        GUI.startView(Settings.class,1); // Wir springen gezielt auf das zweite Tab
    }

    /**
     * @see de.willuhn.jameica.messaging.MessageConsumer#autoRegister()
     */
    public boolean autoRegister()
    {
      return false;
    }
    
  }
}
