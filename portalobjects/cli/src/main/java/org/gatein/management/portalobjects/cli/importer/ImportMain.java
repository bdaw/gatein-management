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

package org.gatein.management.portalobjects.cli.importer;

import org.gatein.management.portalobjects.cli.Main;
import org.gatein.management.portalobjects.cli.Utils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public class ImportMain
{
   private static final String DEFAULT_CONFIG = "import.properties";

   private static final String IMPORTER_SPLASH =
      "-------------------------------------------------------------\n" +
      "*         Module:   Portal Objects CLI                      *\n" +
      "*         Program:  Importer                                *\n" +
      "*         Version:  1.0.0.Alpha-1                           *\n" +
      "* --------------------------------------------------------- *\n" +
      "*               For help run with --help                    *\n" +
      "-------------------------------------------------------------";

   public static void main(String...args)
   {
      System.out.println(IMPORTER_SPLASH);

      // Load default properties
      Properties properties = new Properties();
      try
      {
         properties.load(Main.class.getResourceAsStream(DEFAULT_CONFIG));
      }
      catch (IOException e)
      {
         System.err.println("Unable to load default configuration file. Reason: " + e.getMessage());
         System.exit(1);
      }

      // Create the importer
      Importer importer = new Importer();

      // Parse command line options
      CmdLineParser parser = new CmdLineParser(importer);
      try
      {
         parser.parseArgument(args);
      }
      catch (CmdLineException e)
      {
         System.err.println("Exception parsing arguments. Reason: " + e.getLocalizedMessage());
         System.exit(1);
      }

      File configFile = importer.configFile;
      if (configFile != null)
      {
         FileInputStream fis = null;
         try
         {
            fis = new FileInputStream(configFile);
         }
         catch (FileNotFoundException e)
         {
            System.err.println("Custom config file not found. Reason: " + e.getLocalizedMessage());
            System.exit(1);
         }
         try
         {
            // override any properties defined in default export.properties
            properties.load(fis);
         }
         catch (IOException e)
         {
            System.err.println("Exception loading properties file " + configFile + ". Reason: " + e.getLocalizedMessage());
            System.exit(1);
         }
         finally
         {
            try { fis.close(); } catch (Exception e) {}
         }
      }

      // Pass optional configurable properties as args if program args do not include them already
      List<String> argList = new ArrayList<String>();
      argList.addAll(Arrays.asList(args));
      try
      {
         Utils.addPropertiesAsArgs(Importer.class, properties, argList,
            new String[]{"username", "password", "host", "port", "portalContainer", "log4jFile", "logLevel", "overwrite"},
            new String[] {"overwrite"});
      }
      catch (Exception e)
      {
         System.err.println("Exception adding properties as arguments to program. Reason: " + e.getLocalizedMessage());
         System.exit(1);
      }

      args = argList.toArray(new String[argList.size()]);
      try
      {
         parser.parseArgument(args);
      }
      catch (CmdLineException e)
      {
         System.err.println("Exception parsing arguments. Reason: " + e.getLocalizedMessage());
         System.exit(1);
      }

      // Print help and exit
      if (importer.help)
      {
         parser.setUsageWidth(125);
         parser.printUsage(System.out);
         System.exit(0);
      }

      // Run importer
      importer.init();
      importer.doImport();
   }
}
