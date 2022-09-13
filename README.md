# java-point-of-sale

This repository contains a modular Point of Sale (POS) application developed as a credit project for the Advanced Java Programming course.
It provides a modular, extensible application, which is usable in a customer-facing establishment by employees. The modules provided 
enable a generic establishment to hold information about products, handle tabs, sales and inventory management.


## Setup
To start using the application, some setup is required. Namely, the following steps:


#### Database
A database is required as the application's persistent storage. The application was developed for a MariaDB 10.4.24, although any modern MySQL database should work.
 - Create a database user with the `SELECT, INSERT, UPDATE, CREATE, ALTER`	privileges.
 - provide an implementation of `cz.cuni.mff.java.projects.posapp.database.DBUser` interface
 - change the `cz.cuni.mff.java.projects.posapp.database.Database` class constructor to use your implementation
 
 
#### Compilation
Simply package the root `pom.xml` using `mvn package`. The packaged files are placed in the `/target/jars/` folders of each module.


#### Execute
Execute using the provided `exec.sh` script. This launches the application including all the modules. 

Alternatively, execute using the `java` command. Add all desired plugins to the modulepath, whilst maintaining their dependencies.
The Main class is `cz.cuni.mff.java.projects.posapp.core/cz.cuni.mff.java.projects.posapp.core.App`.


## Base Modules
The modularity of the application is an essential design feature. The project is split into modules, which are each discussed below.
Of the modules discussed, only the Core module is mandataory, although it provides no user functionality on its own. 
Add the desired modules and their dependencies to the modulepath to use them.


### Core module
This module is the core of the application. It handles the loading of other modules and displays them in the main GUI.
The `cz.cuni.mff.java.projects.posapp.core/cz.cuni.mff.java.projects.posapp.core.App` class is the entrypoint to the application.

All plugins implement the `POSPlugin` interface, which is used by the module. Each `POSPlugin` is then shown by the Core.
The POSPlugins create their own main panel, which is displayed in the large right panel. This main panel provides all user
interface for the plugin. The left sidebar allows the Core to switch between the displayed plugin panels.


### Database module
This module provides a database access to other modules. The Database class holds single connection point to the database server and all
DB communication flows through it.

For a plugin to retreive the database connection from this module, it must declare its required database tables programatically. 
The module then ensures the appropriate tables exist in the DB, according to the provided definitions. This way, no database setup is required initially by the user, 
and plugins are sure that their requirements exist in the database.

For implementing a plugin using the Database, refer to the javadoc of this module.


### Products module
This module provides the functionality to define products and product groups. A product is an object that can be sold for some price, and is used
by other plugins to calculate payment, inventories etc.. Define products in the plugin interface by filling the product information in the UI.

A product group is a group that can contain products or other product groups.
The product groups therefore create a hierarchical tree structure, with products as leaves. This hierarchy is used to structure the products
into better manageable groups, but is ultimately optional but recommended.
Product groups can also be created using the plugin's interface.


### Inventory module
This module is a rather simple, optional extension of the Products module. It provides the option of tracking the stock of each product.
The stock is updatable within the plugin's interface, but mainly updates automatically with each payment transaction from the Payment module.


### Payment module
This module provides tabs, which can be created and filled with products. Tabs can be created, added to and paid for. 
The Tabs panel of this plugin holds all of this functionality. The "New" button will create a blank tab.
To name it, click on its name and enter the new one, followed by pressing enter.

Products are taken from the Products module's model. To add new products, see the Products module.

The tab can finally be paid, which will clear the tab.
The payment functionality is intentiionally omitted, as it is out of scope of this project.
A successful payment will notify the Inventory plugin of the transaction, if that plugin is present.
The successful payments are stored in the database, along with the list of products that were sold.
The payments are not available in the application, only in the database for tax reasons.


## Tables module
This module provides an extension of the Payment module. Tables can be placed and edited using the editor.
The placed tables can be created as interactable, which will enable them to hold Tabs, much like the Payment module.
Clicking the interactable table will bring up its tab view. The functions of this view are identical to the Payment module.

#### Editor
The tables editor enables the definition of the layout by placing rectangular shapes.
 - The rectangle tool allows for placing of interactable tables by right-click dragging and non-interactable by left-click dragging.
New tables are always placed on top of old ones.
 - The Move tools enables moving, deleting and duplicating of existing tables. Move a table by dragging it. This places it in front of others.
 Delete it by selecting it and pressing backspace or delete. Duplicate it by selecting it and pressing 'D'.
 - Save the new layout by pressing save. This saves it to the database as well as the tables view screen.


## Developer documentation
The source code is accompanied by a complete Javadoc documentation, available in the repository, or using the `javadoc` command on the deisred modules.

## Extending the application
To extend the application, create any number of modules. To have a module appear in the App as a plugin, implement the `cz.cuni.mff.java.projects.posapp.plugins.POSPlugin` interface and have your module `provide` the implementation. When placed on the modulepath,
the App's ServiceLoader will automatically initialise the plugin and provide it in the UI. 
A new module must also be aded to the project's root `pom.xml` as a module.

To communicate with other modules, use the `App.messagePlugin` method and provide the target's classname.
To utilise the database module, implement the `DBClient` Interface and provide it to the `Database` singleton getter. Refer to the Database javadoc for more information.

