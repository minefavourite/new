import java.util.ArrayList;

public class TestList {

    public static void main(String[] args) {
        ArrayList<String> cities = new ArrayList<String>(){{
            add("ShangHai");
            add("TianJin");
            add("Beijing");
            add("DaLian");
        }};

        ArrayList<String> city1 = new ArrayList<String>();
        ArrayList<String> city2 = new ArrayList<String>();
        city1.addAll(cities.subList(0, cities.size() / 2 + cities.size()%2));
        city2.addAll(cities.subList( cities.size() / 2 + cities.size()%2,cities.size() ));
        System.out.println(city1);
        System.out.println(city2);
        System.out.println(cities);
    }
}
