package com.pcchecker.data;

import com.pcchecker.model.Shop;
import java.util.ArrayList;
import java.util.List;

public class ShopDatabase {
    public static List<Shop> getShops() {
        List<Shop> list = new ArrayList<>();
        
        // Sample shops in Metro Manila, Philippines
        list.add(new Shop("PC Express - Gilmore", "Gilmore Ave, New Manila, Quezon City", 14.6133, 121.0315, "One of the most popular computer shops in Gilmore."));
        list.add(new Shop("DynaQuest PC - Makati", "Guevara St, Makati, Metro Manila", 14.5630, 121.0116, "Reliable shop with great component selection."));
        list.add(new Shop("EasyPC - North EDSA", "North Ave, Quezon City, Metro Manila", 14.6562, 121.0312, "Competitive pricing and great online presence."));
        list.add(new Shop("PCHub - Gilmore", "Gilmore Tower, Quezon City", 14.6140, 121.0318, "Known for having the latest hardware in stock."));
        list.add(new Shop("VillMan - SM Megamall", "EDSA, Mandaluyong, Metro Manila", 14.5855, 121.0568, "Located inside SM Megamall, very accessible."));
        list.add(new Shop("Octagon - SM Mall of Asia", "Seaside Blvd, Pasay, Metro Manila", 14.5352, 120.9822, "Conveniently located in one of the largest malls."));
        
        return list;
    }
}
