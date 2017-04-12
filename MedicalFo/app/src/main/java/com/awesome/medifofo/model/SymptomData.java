package com.awesome.medifofo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 17 on 2017-04-09.
 */

public class SymptomData {

    private static final String[] titles = {"Agitation", "Anxiety", "Apathy", "Bald spots (hair)", "Blackouts (memory time loss)", "Bleeding"};

    public static List<ListItem> getListData() {
        List<ListItem> data = new ArrayList<>();

        for (int i = 0; i < titles.length; i++) {
            ListItem item = new ListItem();
            item.setTitle(titles[i]);
            data.add(item);
        }

        return data;
    }

}

