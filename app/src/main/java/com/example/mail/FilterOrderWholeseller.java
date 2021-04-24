package com.example.mail;

import android.widget.Filter;

import java.util.ArrayList;

public class FilterOrderWholeseller extends Filter {
    private AdapterOrderWholeseller adapter;
    private ArrayList<ModelOrderWholeseller> filterlist;

    public FilterOrderWholeseller(AdapterOrderWholeseller adapter, ArrayList<ModelOrderWholeseller> filterlist){
        this.adapter = adapter;
        this.filterlist = filterlist;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if(constraint != null && constraint.length() >0 ){
            constraint = constraint.toString().toUpperCase();
            //store filter list
            ArrayList<ModelOrderWholeseller> filtermodels = new ArrayList<>();
            for(int i=0 ; i<filterlist.size(); i++){
                if(filterlist.get(i).getOrderStatus().toUpperCase().contains(constraint)){
                    filtermodels.add(filterlist.get(i));
                }
            }
            results.count = filtermodels.size();
            results.values = filtermodels;
        }
        else{
            results.count = filterlist.size();
            results.values = filterlist;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.modelOrderWholesellerArrayList = (ArrayList<ModelOrderWholeseller>) results.values;
        adapter.notifyDataSetChanged();

    }
}
