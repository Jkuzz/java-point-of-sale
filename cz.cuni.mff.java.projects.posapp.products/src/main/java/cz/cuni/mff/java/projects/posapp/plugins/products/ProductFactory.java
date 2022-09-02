package cz.cuni.mff.java.projects.posapp.plugins.products;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;


public class ProductFactory {

    private final ProductGroupsModel groupsModel = ProductGroupsModel.getInstance();

    private ArrayList<ProductGroup> productGroupsList;
    private ArrayList<Product> productList;

    /**
     * Turn the ProductGroupsModel representation of product groups into a hierarchical tree
     * structure of groups. The groups in the returned array are those with no parent - root level.
     */
    public void makeGroupsList() {
        productGroupsList = new ArrayList<>();
        ArrayList<GroupComboBoxItem> items = groupsModel.getGroups();
        items.stream()
                .filter(g -> g.getId() != null && g.getParentId() == null)  // Remove null group and keep roots
                .forEach(g -> productGroupsList.add(makeGroup(g)));
    }


    /**
     * Get cached groups. Fill cache with root groups if required.
     * @return the model's groups.
     */
    public ArrayList<ProductGroup> getGroups() {
        if(productGroupsList == null) {
            makeGroupsList();
        }
        return productGroupsList;
    }


    /**
     * Transform the ProductsTableModel tabular array-based representation of products
     * into a better manageable data structure. Products are turned from Object arrays into
     * a hashmap indexed by the columns.
     * @param columnNames columns of the table
     * @param data rows of the table
     * @return products in the table
     */
    public ArrayList<Product> makeProductsList(ArrayList<String> columnNames, ArrayList<Object[]> data) {
        if(productList != null) return productList;
        getGroups();  // Make sure groups have been created first

        ArrayList<Product> products = new ArrayList<>();
        int groupIdIndex = columnNames.indexOf("group_id");

        data.forEach(row -> {
            System.out.println("Making product row");
            Integer groupId = (Integer) row[groupIdIndex];  // can be null
            ProductGroup parentGroup = findGroupById(groupId, productGroupsList);
            HashMap<String, Object> productFields = makeProductFields(row, columnNames);

            Product product = new Product(parentGroup, productFields);
            products.add(product);
        });

        productList = products;
        return productList;
    }

    /**
     * Turn the product row into a hashmap
     * @param row raw row array data
     * @param columnNames columns of product table
     * @return the fields as a hashmap
     */
    HashMap<String, Object> makeProductFields(Object[] row, ArrayList<String> columnNames) {
        HashMap<String, Object> fields = new HashMap<>();
        for(int i = 0; i < row.length; i += 1) {
            fields.put(columnNames.get(i), row[i]);
        }
        return fields;
    }

    /**
     * Find the group in the groups hierarchy
     * @param id of group to find
     * @return the group or null
     */
    ProductGroup findGroupById(int id, ArrayList<ProductGroup> haystack) {
        for(ProductGroup g: haystack) {
            if(g.getId() == id) return g;
            return findGroupById(id, g.getChildGroups());
        }
        return null;
    }

    /**
     * Make a group out of the ComboBoxItem
     * @param item to transform
     * @return the created group object
     */
    ProductGroup makeGroup(GroupComboBoxItem item) {
        int id = item.getId();
        System.out.println("Making group " + id);
        Stream<GroupComboBoxItem> children = groupsModel.getGroupChildren(id);
        ArrayList<ProductGroup> childGroups = new ArrayList<>();
        children.forEach(g -> childGroups.add(makeGroup(g)));

        ArrayList<Product> childProducts = new ArrayList<>();
        return new ProductGroup(childGroups, childProducts, id);
    }
}
