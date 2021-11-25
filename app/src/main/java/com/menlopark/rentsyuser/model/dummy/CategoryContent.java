package com.menlopark.rentsyuser.model.dummy;

import com.menlopark.rentsyuser.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CategoryContent {
    public static final List<CategoryItem> ITEMS = new ArrayList<CategoryItem>();
    public static final Map<String, CategoryItem> ITEM_MAP = new HashMap<String, CategoryItem>();
    private static final int COUNT = 25;

    static {
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(CategoryItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static CategoryItem createDummyItem(int position) {
        return new CategoryItem(String.valueOf(position), "Company Name " + position,"10km", "Details About"+position,3,"https://scontent-bom1-1.xx.fbcdn.net/v/t1.0-9/28167096_1569195816449347_3455427507292652873_n.jpg?oh=1d8ce6306e85f50df72d24fd81ced89e&oe=5B3E01A9");
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        return builder.toString();
    }

    public static class CategoryItem {
        public final String id;
        public final String company_name;
        public final String category;
        public final String distance;
        public final float rate;
        public final String company_logo;

        public CategoryItem(String id, String company_name, String category, String distance, float rate,String company_logo) {
            this.id = id;
            this.company_name = company_name;
            this.category = category;
            this.distance = distance;
            this.rate = rate;
            this.company_logo = company_logo;
        }
    }
}
