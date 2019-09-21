package backyardregister.fallfestregister;


public class SaleList {

    String name;
    SaleItem[] list;

    public SaleList(String inName, backyardregister.fallfestregister.SaleItem[] inList) {
        name = inName;
        list = inList;
    }

    public String getName() {
        return name;
    }

    public backyardregister.fallfestregister.SaleItem[] getList() {
        return list;
    }
}
