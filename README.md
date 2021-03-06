GateIn Management Module
=============

A management module to run on top of GateIn.  This module includes a simple set of tools including an import/export
command line interface, RESTful services to manage portal objects, and a client API to manage portal objects.

Installation
-----------

`mvn clean install`

NOTE: Until xml parsing is included, you will also need stax-builder which can be obtained from:
[stax-builder](https://github.com/nscavell/stax-builder) and running `mvn clean install`

Once that's done copy the ear from `packaging/jbossas/ear/target` to the JBoss Application Server deploy directory.

Usage
-----------

### Restful Services
Assuming the server is running @ http://localhost:8080

NOTE: These URL's are subject to change.  To obtain data from the server you should use the client API's.

#### Site Management Services:

    http://localhost:8080/{rest-context-name}/private/portalobjects/sites/{ownerId}

    Examples:
    http://localhost:8080/rest/private/portalobjects/sites/classic

#### Page Management Services:

    http://localhost:8080/{rest-context-name}/private/portalobjects/pages?ownerType={ownerType}&ownerId={ownerId}
    http://localhost:8080/{rest-context-name}/private/portalobjects/pages/{name}?ownerType={ownerType}&ownerId={ownerId}

Note: ownerType is not required and will default to 'portal'.  If you want to specify a group or user page include the ownerType as such ownerType=group or ownerType=user as a URL parameter.

    Examples:
    All portal pages:
        http://localhost:8080/rest/private/portalobjects/pages?ownerId=classic
    Portal homepage:
        http://localhost:8080/rest/private/portalobjects/pages/homepage?ownerId=classic
    Application Registry
        http://localhost:8080/rest/private/portalobjects/pages/registry?ownerType=group&ownerId=/platform/administrators

#### Navigation Management Services

    http://localhost:8080/{rest-context-name}/private/portalobjects/navigations?ownerType={ownerType}&ownerId={ownerId}
    http://localhost:8080/{rest-context-name}/private/portalobjects/navigations/{nav-uri}?ownerType={ownerType}&ownerId={ownerId}

Note: ownerType is not required and will default to 'portal'.  If you want to specify a group or user page include the ownerType as such ownerType=group or ownerType=user as a URL parameter.
Note: nav-uri is the uri of the navigation, so if you have a nav of 'home-1' under home the nav-uri would be home/home-1

    Examples:
    All portal navigation:
        http://localhost:8080/rest/private/portalobjects/navigations?ownerId=classic
    Portal navigation homepage:
        http://localhost:8080/rest/private/portalobjects/navigations/home?ownerId=classic
    Application Registry navigation:
        http://localhost:8080/rest/private/portalobjects/navigations/administration/registry?ownerType=group&ownerId=/platform/administrators


### Client API's

PortalObjectsMgmtClient is the client interface to 'manage' the portal objects of the portal.

To obtain an instance of the PortalObjectsMgmtClient to communicate with a portal running @ http://localhost:8080/portal/

    PortalObjectsMgmtClient client = PortalObjectsMgmtClient.Factory.create(InetAddress.getByName("localhost"), 8080, "root", "gtn", "portal");

To get the homepage

    Page page = client.getPage("portal", "classic", "homepage");

To get homepage navigation

    PageNode navigation = client.getNavigationNode("portal", "classic", "home");