/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.gatein.management.portalobjects.client.api;

import org.exoplatform.portal.config.model.Page;
import org.exoplatform.portal.config.model.PageNavigation;
import org.exoplatform.portal.config.model.PageNode;
import org.exoplatform.portal.config.model.PortalConfig;
import org.gatein.management.binding.api.BindingProvider;
import org.gatein.management.binding.core.api.BindingProviderImpl;
import org.gatein.management.portalobjects.client.impl.ClientDataStorageImpl;
import org.gatein.management.portalobjects.client.impl.RestfulPortalObjectsMgmtClient;
import org.gatein.management.portalobjects.exportimport.api.ExportContext;
import org.gatein.management.portalobjects.exportimport.api.ExportHandler;
import org.gatein.management.portalobjects.exportimport.api.ImportContext;
import org.gatein.management.portalobjects.exportimport.api.ImportHandler;
import org.gatein.management.portalobjects.exportimport.impl.ExportHandlerImpl;
import org.gatein.management.portalobjects.exportimport.impl.ImportHandlerImpl;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public interface PortalObjectsMgmtClient
{
   List<PortalConfig> getPortalConfig(String ownerType) throws ClientException;

   PortalConfig getPortalConfig(String ownerType, String ownerId) throws ClientException;

   void updatePortalConfig(PortalConfig portalConfig) throws ClientException;

   List<Page> getPages(String ownerType, String ownerId) throws ClientException;

   Page getPage(String ownerType, String ownerId, String pageName) throws ClientException;

   Page createPage(String ownerType, String ownerId, String pageName, String title) throws ClientException;

   void deletePage(String ownerType, String ownerId, String pageName) throws ClientException;

   void updatePage(Page page) throws ClientException;

   PageNavigation getNavigation(String ownerType, String ownerId) throws ClientException;

   PageNode getNavigationNode(String ownerType, String ownerId, String navigationUri) throws ClientException;

   PageNavigation createNavigation(String ownerType, String ownerId, int priority) throws ClientException;

   PageNode createNavigationNode(String ownerType, String ownerId, String name, String label) throws ClientException;

   PageNode createNavigationNode(String ownerType, String ownerId, String parentNavigationUri, String name, String label) throws ClientException;

   void updateNavigation(PageNavigation navigation) throws ClientException;

   void updateNavigationNode(String ownerType, String ownerId, PageNode node) throws ClientException;

   void deleteNavigationNode(String ownerType, String ownerId, String navigationUri) throws ClientException;

   ExportContext createExportContext() throws ClientException;

   void exportToZip(ExportContext context, File file) throws IOException;

   ImportContext importFromZip(File file) throws IOException;

   void importContext(ImportContext context) throws ClientException;

   public static class Factory
   {
      public static PortalObjectsMgmtClient create(InetAddress address, int port, String username, String password, String portalContainerName)
      {
         BindingProvider bindingProvider = new BindingProviderImpl();
         bindingProvider.load();
         RestfulPortalObjectsMgmtClient client =
            new RestfulPortalObjectsMgmtClient(address, port, username, password, portalContainerName, bindingProvider);

         ExportHandler exportHandler = new ExportHandlerImpl(bindingProvider);
         ImportHandler importHandler = new ImportHandlerImpl(bindingProvider, new ClientDataStorageImpl(client));

         client.setExportHandler(exportHandler);
         client.setImportHandler(importHandler);

         return client;
      }
   }
}
