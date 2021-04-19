package com.example.mail;
import android.widget.Filter;
import java.util.ArrayList;
public class FilterProduct_retailer extends Filter {
    private AdapterProductRetailer adapter;
    private ArrayList<ModelProduct> filterlist;

    public FilterProduct_retailer(AdapterProductRetailer adapter, ArrayList<ModelProduct> filterlist){
        this.adapter = adapter;
        this.filterlist = filterlist;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if(constraint != null && constraint.length() >0 ){
            constraint = constraint.toString().toUpperCase();
            //store filter list
            ArrayList<ModelProduct> filtermodels = new ArrayList<>();
            for(int i=0 ; i<filterlist.size(); i++){
                if(filterlist.get(i).getProductTitle().toUpperCase().contains(constraint) ||
                        filterlist.get(i).getProductcategory().toUpperCase().contains(constraint)){
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
        adapter.productList = (ArrayList<ModelProduct>) results.values;
        adapter.notifyDataSetChanged();

    }
}
